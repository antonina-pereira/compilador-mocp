package mocp.semantic;

import mocp.ast.*;

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
            // REGRA DO ENUNCIADO: Os protótipos têm de vir antes de qualquer variável ou função
            boolean podeTerPrototipo = true;

            for (ASTNode filho : ((ProgramaNode) no).getFilhos()) {
                if (filho instanceof PrototipoNode) {
                    if (!podeTerPrototipo) {
                        reportarErro("Os protótipos devem ser declarados ANTES de qualquer função ou variável global!");
                    }
                    validarPrototipo((PrototipoNode) filho);
                } else {
                    podeTerPrototipo = false; // Apanhou uma declaração ou função; bloqueia novos protótipos
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
        else if (no instanceof AfirmacaoCompostaNode) {
            for (ASTNode instr : ((AfirmacaoCompostaNode) no).getInstrucoes()) {
                validarNo(instr);
            }
        }
        else if (no instanceof SeNode) {
            validarNo(((SeNode) no).getCondicao());
            validarNo(((SeNode) no).getBlocoSe());
            validarNo(((SeNode) no).getBlocoSenao());
        }
        else if (no instanceof RetornarNode) {
            ASTNode expr = ((RetornarNode) no).getExpressao();
            if (expr != null) validarNo(expr);

            if (tipoFuncaoAtual.equals("vazio") && expr != null) {
                reportarErro("Incompatibilidade: Uma função do tipo 'vazio' não pode retornar valores!");
            }
            if (!tipoFuncaoAtual.equals("vazio") && expr == null) {
                reportarErro("Incompatibilidade: A função tem o tipo '" + tipoFuncaoAtual + "' mas o 'retornar' está vazio!");
            }
        }
        else if (no instanceof OpBinNode) {
            validarNo(((OpBinNode) no).getEsquerda());
            validarNo(((OpBinNode) no).getDireita());
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
                SymbolInfo novaVar = new SymbolInfo(nome, tipo, Categoria.VARIAVEL);

                if (!tabelaSimbolos.inserir(novaVar)) {
                    reportarErro("A variável '" + nome + "' já foi declarada neste escopo!");
                }
                if (declarador.getInicializador() != null) {
                    validarNo(declarador.getInicializador());
                }
            }
        }
    }

    private void validarFuncao(FuncaoNode funcao) {
        String nome = funcao.getNome();
        tipoFuncaoAtual = funcao.getTipo();

        // 1. Registar função no escopo Global
        SymbolInfo infoFuncao = new SymbolInfo(nome, tipoFuncaoAtual, Categoria.FUNCAO);
        if (!tabelaSimbolos.inserir(infoFuncao)) {
            // Se já existir, pode ser o protótipo! Verificamos:
            SymbolInfo existente = tabelaSimbolos.procurar(nome);
            if (existente != null && existente.getCategoria() != Categoria.PROTOTIPO) {
                reportarErro("Já existe uma declaração com o nome '" + nome + "'!");
            } else if (existente != null && existente.getCategoria() == Categoria.PROTOTIPO) {
                // Se era um protótipo, atualizamos a categoria para FUNCAO (já foi implementada)
                tabelaSimbolos.inserir(infoFuncao);
            }
        }

        // 2. Entrar no escopo local da função
        tabelaSimbolos.enterScope();

        // 3. Registar os parâmetros da função como variáveis locais no novo escopo!
        if (funcao.getParametros() != null) {
            ASTNode params = funcao.getParametros();
            // No ASTBuilder, nós guardámos a lista de parâmetros dentro de um AfirmacaoCompostaNode
            if (params instanceof AfirmacaoCompostaNode) {
                for (ASTNode paramNo : ((AfirmacaoCompostaNode) params).getInstrucoes()) {
                    if (paramNo instanceof ParametroNode) {
                        ParametroNode pNode = (ParametroNode) paramNo;

                        // Decide se é VETOR ou VARIAVEL simples
                        Categoria cat = pNode.isEsVetor() ? Categoria.VETOR : Categoria.VARIAVEL;
                        SymbolInfo infoParam = new SymbolInfo(pNode.getId(), pNode.getTipo(), cat);

                        // Tenta registar o parâmetro na Tabela de Símbolos (no escopo local)
                        if (!tabelaSimbolos.inserir(infoParam)) {
                            reportarErro("O parâmetro '" + pNode.getId() + "' já foi declarado na assinatura desta função!");
                        }
                    }
                }
            }
        }

        // 4. Validar o bloco de código (instruções)
        if (funcao.getBloco() != null) {
            validarNo(funcao.getBloco());
        }

        // 5. Sair do escopo local (destrói as variáveis locais)
        tabelaSimbolos.exitScope();
        tipoFuncaoAtual = "";
    }

    private void validarUsoVariavel(IDNode idNode) {
        String nome = idNode.getNome();
        SymbolInfo info = tabelaSimbolos.procurar(nome);
        if (info == null) {
            reportarErro("A variável '" + nome + "' não foi declarada antes de ser utilizada!");
        }
    }

    private void reportarErro(String mensagem) {
        System.err.println("[Erro Semântico] " + mensagem);
        numErros++;
    }

    public boolean houveErros() {
        return numErros > 0;
    }
}