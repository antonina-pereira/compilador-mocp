package mocp.semantic;

import mocp.ast.*;
import java.util.List;

public class SemanticAnalyzer {

    private SymbolTable tabelaSimbolos = new SymbolTable();
    private String tipoFuncaoAtual = "";
    private int numErros = 0;

    public void analisar(ASTNode raiz) {
        System.out.println("\n--- A INICIAR ANÁLISE SEMÂNTICA ---");

        validarNo(raiz);

        // REGRA DO ENUNCIADO: Verificar se a função principal existe
        SymbolInfo principal = tabelaSimbolos.procurar("principal");
        if (principal == null || (principal.getCategoria() != Categoria.FUNCAO && principal.getCategoria() != Categoria.PROTOTIPO)) {
            reportarErro("A função 'principal' não foi encontrada no programa!");
        }

        if (numErros == 0) {
            System.out.println("Análise Semântica concluída com SUCESSO! 0 erros.");
        } else {
            System.err.println("Análise Semântica FALHOU com " + numErros + " erro(s).");
        }
    }

    private void validarNo(ASTNode no) {
        if (no == null) return;

        if (no instanceof ProgramaNode) {
            boolean podeTerPrototipo = true;
            for (ASTNode filho : ((ProgramaNode) no).getFilhos()) {
                if (filho instanceof PrototipoNode) {
                    if (!podeTerPrototipo) {
                        reportarErro("Os protótipos devem ser declarados ANTES de qualquer função ou variável global!");
                    }
                    validarPrototipo((PrototipoNode) filho);
                } else {
                    podeTerPrototipo = false;
                    validarNo(filho);
                }
            }
        }
        else if (no instanceof DeclaracaoNode) {
            validarDeclaracao((DeclaracaoNode) no);
        }
        else if (no instanceof FuncaoNode) {
            validarFuncao((FuncaoNode) no);
        }
        // CORREÇÃO 1: Gestão correta de escopo para blocos compostos { ... }
        else if (no instanceof AfirmacaoCompostaNode) {
            tabelaSimbolos.enterScope();
            for (ASTNode instr : ((AfirmacaoCompostaNode) no).getInstrucoes()) {
                validarNo(instr);
            }
            tabelaSimbolos.exitScope();
        }
        else if (no instanceof SeNode) {
            validarNo(((SeNode) no).getCondicao());
            validarNo(((SeNode) no).getBlocoSe());
            validarNo(((SeNode) no).getBlocoSenao());
        }
        else if (no instanceof RetornarNode) {
            ASTNode expr = ((RetornarNode) no).getExpressao();
            if (expr != null) {
                validarNo(expr);
                String tipoRetornado = deduzirTipo(expr);
                // CORREÇÃO 3: Verificação rigorosa do tipo de retorno face à assinatura
                if (!tipoFuncaoAtual.equals("vazio") && !tiposCompativeis(tipoFuncaoAtual, tipoRetornado)) {
                    reportarErro("Incompatibilidade: A função espera retornar '" + tipoFuncaoAtual + "' mas obteve '" + tipoRetornado + "'!");
                }
            }

            if (tipoFuncaoAtual.equals("vazio") && expr != null) {
                reportarErro("Incompatibilidade: Uma função do tipo 'vazio' não pode retornar valores!");
            }
            if (!tipoFuncaoAtual.equals("vazio") && expr == null) {
                reportarErro("Incompatibilidade: A função tem o tipo '" + tipoFuncaoAtual + "' mas o 'retornar' está vazio!");
            }
        }
        // CORREÇÃO 2 & 3: Suporte e validação de Chamadas de Função e Tipos
        else if (no instanceof ChamadaFuncaoNode) {
            validarChamadaFuncao((ChamadaFuncaoNode) no);
        }
        else if (no instanceof OpBinNode) {
            OpBinNode opBin = (OpBinNode) no;
            validarNo(opBin.getEsquerda());
            validarNo(opBin.getDireita());

            String tEsq = deduzirTipo(opBin.getEsquerda());
            String tDir = deduzirTipo(opBin.getDireita());
            if (!tiposCompativeis(tEsq, tDir)) {
                reportarErro("Incompatibilidade de tipos na operação '" + opBin.getOperador() + "': não é possível operar '" + tEsq + "' com '" + tDir + "'!");
            }
        }
        else if (no instanceof OpUnNode) {
            // Valida a expressão que está lá dentro
            validarNo(((OpUnNode) no).getExpressao());
        }
        else if (no instanceof IDNode) {
            validarUsoVariavel((IDNode) no);
        }
    }

    private void validarPrototipo(PrototipoNode proto) {
        String nome = proto.getNome();
        SymbolInfo info = new SymbolInfo(nome, proto.getTipo(), Categoria.PROTOTIPO);
        for (String param : proto.getTiposParametros()) {
            info.addTipoParametro(param);
        }

        if (!tabelaSimbolos.inserir(info)) {
            reportarErro("O protótipo '" + nome + "' já foi declarado anteriormente!");
        }
    }

    private void validarDeclaracao(DeclaracaoNode decNode) {
        String tipo = decNode.getTipo();
        for (ASTNode item : decNode.getItens()) {
            if (item instanceof DeclaradorNode) {
                DeclaradorNode declarador = (DeclaradorNode) item;
                String nome = declarador.getId();
                // CORREÇÃO VETORES: guarda se é vetor
                boolean isVetor = declarador.getTamanhoVetor() >= 0;
                SymbolInfo novaVar = new SymbolInfo(nome, tipo, Categoria.VARIAVEL, isVetor);

                if (!tabelaSimbolos.inserir(novaVar)) {
                    reportarErro("A variável '" + nome + "' já foi declarada neste escopo!");
                }
                if (declarador.getInicializador() != null) {
                    validarNo(declarador.getInicializador());
                    String tipoInit = deduzirTipo(declarador.getInicializador());
                    if (!tiposCompativeis(tipo, tipoInit)) {
                        reportarErro("Incompatibilidade de tipos: Inicializador do tipo '" + tipoInit + "' não pode ser atribuído a '" + tipo + "'!");
                    }
                }
            }
        }
    }

    private void validarFuncao(FuncaoNode funcao) {
        String nome = funcao.getNome();
        tipoFuncaoAtual = funcao.getTipo();

        SymbolInfo infoFuncao = new SymbolInfo(nome, tipoFuncaoAtual, Categoria.FUNCAO);
        if (funcao.getParametros() instanceof AfirmacaoCompostaNode) {
            for (ASTNode paramNo : ((AfirmacaoCompostaNode) funcao.getParametros()).getInstrucoes()) {
                if (paramNo instanceof ParametroNode) {
                    infoFuncao.addTipoParametro(((ParametroNode) paramNo).getTipo());
                }
            }
        }

        if (!tabelaSimbolos.inserir(infoFuncao)) {
            SymbolInfo existente = tabelaSimbolos.procurar(nome);
            if (existente != null && existente.getCategoria() != Categoria.PROTOTIPO) {
                reportarErro("Já existe uma declaração com o nome '" + nome + "'!");
            } else if (existente != null && existente.getCategoria() == Categoria.PROTOTIPO) {
                tabelaSimbolos.inserir(infoFuncao);
            }
        }

        tabelaSimbolos.enterScope();

        if (funcao.getParametros() != null) {
            ASTNode params = funcao.getParametros();
            if (params instanceof AfirmacaoCompostaNode) {
                for (ASTNode paramNo : ((AfirmacaoCompostaNode) params).getInstrucoes()) {
                    if (paramNo instanceof ParametroNode) {
                        ParametroNode pNode = (ParametroNode) paramNo;
                        // CORREÇÃO VETORES: guarda a flag isVetor no SymbolInfo
                        SymbolInfo infoParam = new SymbolInfo(pNode.getId(), pNode.getTipo(), Categoria.VARIAVEL, pNode.isEsVetor());

                        if (!tabelaSimbolos.inserir(infoParam)) {
                            reportarErro("O parâmetro '" + pNode.getId() + "' já foi declarado na assinatura desta função!");
                        }
                    }
                }
            }
        }

        if (funcao.getBloco() != null) {
            // CORREÇÃO: Cast explícito e seguro de ASTNode para AfirmacaoCompostaNode
            if (funcao.getBloco() instanceof AfirmacaoCompostaNode) {
                for (ASTNode instr : ((AfirmacaoCompostaNode) funcao.getBloco()).getInstrucoes()) {
                    validarNo(instr);
                }
            } else {
                validarNo(funcao.getBloco());
            }
        }

        tabelaSimbolos.exitScope();
        tipoFuncaoAtual = "";
    }

    // CORREÇÃO 2: Metodo dedicado para validação de Chamadas de Função e Tratar o parâmetro "vazio" como 0 argumentos
    private void validarChamadaFuncao(ChamadaFuncaoNode call) {
        String nome = call.getNome();

        // Ignorar funções nativas de I/O na validação de assinatura se não estiverem na tabela
        if (nome.equals("ler") || nome.equals("escrever") || nome.equals("lerc") || nome.equals("lers") || nome.equals("escrevers") || nome.equals("escreverc") || nome.equals("escreverv")) {
            for (ASTNode arg : call.getArgumentos()) {
                validarNo(arg);
            }
            return;
        }

        SymbolInfo funcInfo = tabelaSimbolos.procurar(nome);
        if (funcInfo == null || (funcInfo.getCategoria() != Categoria.FUNCAO && funcInfo.getCategoria() != Categoria.PROTOTIPO)) {
            reportarErro("A função '" + nome + "' não foi declarada ou estruturada antes de ser chamada!");
            return;
        }

        List<ASTNode> argumentos = call.getArgumentos();
        List<String> parametrosEsperados = funcInfo.getTiposParametros();

        // Se o único parâmetro esperado for "vazio", então o número de argumentos esperados é 0
        int numParametrosEsperados = (parametrosEsperados.size() == 1 && parametrosEsperados.get(0).equals("vazio")) ? 0 : parametrosEsperados.size();

        if (argumentos.size() != numParametrosEsperados) {
            reportarErro("Chamada incorreta da função '" + nome + "': Esperava " + numParametrosEsperados + " argumentos mas recebeu " + argumentos.size() + "!");
            return;
        }

        for (int i = 0; i < argumentos.size(); i++) {
            ASTNode arg = argumentos.get(i);
            validarNo(arg);
            String tArg = deduzirTipo(arg);
            String tParam = parametrosEsperados.get(i);
            if (!tiposCompativeis(tParam, tArg)) {
                reportarErro("Erro no argumento " + (i + 1) + " da chamada a '" + nome + "': Tipo esperado é '" + tParam + "' mas foi passado '" + tArg + "'!");
            }
        }
    }

    private void validarUsoVariavel(IDNode idNode) {
        String nome = idNode.getNome();
        SymbolInfo info = tabelaSimbolos.procurar(nome);
        if (info == null) {
            reportarErro("A variável '" + nome + "' não foi declarada antes de ser utilizada!");
        }
    }

    // CORREÇÃO 3: Sistema auxiliar para dedução e compatibilidade de tipos ensinar os tipos de retorno das funções nativas do MOCP
    private String deduzirTipo(ASTNode no) {
        if (no == null) return "vazio";
        if (no instanceof LiteralIntNode) return "inteiro";
        if (no instanceof LiteralRealNode) return "real";
        if (no instanceof LiteralStringNode) return "string";
        if (no instanceof LiteralCharNode) return "char";

        // CORREÇÃO: Ensinar o analisador a ler o tipo dos Casts e Unários!
        // Substitui os contains() por verificações exatas das novas mnemónicas
        if (no instanceof OpUnNode) {
            OpUnNode unNode = (OpUnNode) no;
            String op = unNode.getOperador();

            if (op.equals("int2real")) return "real";
            if (op.equals("real2int")) return "inteiro";

            // Se for um "-" ou "!", herda o tipo da expressão interior
            return deduzirTipo(unNode.getExpressao());
        }

        if (no instanceof IDNode) {
            SymbolInfo inf = tabelaSimbolos.procurar(((IDNode) no).getNome());
            if (inf == null) return "erro";
            // CORREÇÃO VETORES: devolve com [] se for vetor
            if (inf.isVetor()) return inf.getTipo() + "[]";
            return inf.getTipo();
        }
        if (no instanceof AcessoVetorNode) {
            AcessoVetorNode acesso = (AcessoVetorNode) no;
            SymbolInfo inf = tabelaSimbolos.procurar(acesso.getId());
            if (inf == null) return "erro";
            // Acesso a um elemento do vetor devolve o tipo base (sem [])
            return inf.getTipo();
        }
        if (no instanceof OpBinNode) {
            String tEsq = deduzirTipo(((OpBinNode) no).getEsquerda());
            String tDir = deduzirTipo(((OpBinNode) no).getDireita());
            if (tEsq.equals("real") || tDir.equals("real")) return "real";
            return tEsq;
        }
        if (no instanceof ChamadaFuncaoNode) {
            String nomeFunc = ((ChamadaFuncaoNode) no).getNome();
            if (nomeFunc.equals("ler") || nomeFunc.equals("lerc")) return "inteiro";
            if (nomeFunc.equals("lers")) return "inteiro[]";   // <--- CORREÇÃO
            if (nomeFunc.equals("escrever") || nomeFunc.equals("escreverc") ||
                    nomeFunc.equals("escrevers") || nomeFunc.equals("escreverv")) return "vazio";
            SymbolInfo inf = tabelaSimbolos.procurar(nomeFunc);
            return inf != null ? inf.getTipo() : "vazio";
        }
        return "inteiro";
    }
    // CORREÇÃO: Inteiros aceitam caracteres (ASCII) e strings
    private boolean tiposCompativeis(String esperado, String recebido) {
        // CORREÇÃO VETORES: suporte a [] na comparação
        boolean espVetor = esperado.endsWith("[]");
        boolean recVetor = recebido.endsWith("[]");
        if (espVetor != recVetor) return false;

        // Remover [] para comparar base
        String baseEsp = espVetor ? esperado.substring(0, esperado.length()-2) : esperado;
        String baseRec = recVetor ? recebido.substring(0, recebido.length()-2) : recebido;

        if (baseEsp.equals(baseRec)) return true;
        if (baseEsp.equals("real") && baseRec.equals("inteiro")) return true; // Promoção implícita
        if (baseEsp.equals("inteiro") && (baseRec.equals("char") || baseRec.equals("string"))) return true; // MOCP: inteiros guardam ASCII
        return false;
    }

    private void reportarErro(String mensagem) {
        System.err.println("[Erro Semântico] " + mensagem);
        numErros++;
    }

    public boolean houveErros() {
        return numErros > 0;
    }
}