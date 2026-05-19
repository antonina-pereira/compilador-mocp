package mocp.semantic;

import java.util.ArrayList;
import java.util.List;

import mocp.ast.*;

/**
 * Analisador semântico do compilador MOCP.
 *
 * Percorre a AST em dois passos:
 *  1. Registo dos símbolos de topo (variáveis globais, protótipos, assinaturas de funções).
 *  2. Análise dos corpos das funções (verificação de tipos, variáveis não declaradas, etc.).
 *
 * Os erros são recolhidos numa lista e expostos através de {@link #getErros()}.
 */
public class SemanticAnalyzer {

    private final Scope escopo = new Scope();
    private final List<String> erros = new ArrayList<>();

    // Tipo de retorno da função atualmente em análise
    private String tipoFuncaoAtual = null;

    // -------------------------------------------------------------------------
    // API pública
    // -------------------------------------------------------------------------

    public void analisar(ProgramaNode programa) {
        // Passo 1 — registar todos os símbolos de topo
        for (ASTNode elem : programa.elementos) {
            if (elem instanceof DeclaracaoNode) {
                registarDeclaracao((DeclaracaoNode) elem);
            } else if (elem instanceof PrototipoNode) {
                registarPrototipo((PrototipoNode) elem);
            } else if (elem instanceof FuncaoNode) {
                registarAssinaturaFuncao((FuncaoNode) elem);
            }
        }

        // Passo 2 — analisar corpos das funções
        for (ASTNode elem : programa.elementos) {
            if (elem instanceof FuncaoNode) {
                analisarCorpoFuncao((FuncaoNode) elem);
            }
        }
    }

    public boolean temErros() {
        return !erros.isEmpty();
    }

    public List<String> getErros() {
        return erros;
    }

    // -------------------------------------------------------------------------
    // Registo de símbolos
    // -------------------------------------------------------------------------

    private void registarDeclaracao(DeclaracaoNode decl) {
        for (DeclaradorNode d : decl.declaradores) {
            SymbolInfo.Categoria cat = d.vetor
                    ? SymbolInfo.Categoria.VETOR
                    : SymbolInfo.Categoria.VARIAVEL;
            String tipoSimbolo = d.vetor ? decl.tipo + "[]" : decl.tipo;
            SymbolInfo info = new SymbolInfo(d.nome, tipoSimbolo, cat);
            if (!escopo.adicionar(info)) {
                erro("Variável já declarada no mesmo escopo: '" + d.nome + "'");
            }
        }
    }

    private void registarPrototipo(PrototipoNode proto) {
        if (escopo.procurar(proto.nome) != null) {
            // Protótipos duplicados são ignorados silenciosamente (compatível com C)
            return;
        }
        SymbolInfo info = new SymbolInfo(proto.nome, proto.tipo, SymbolInfo.Categoria.FUNCAO);
        for (ParametroNode p : proto.parametros) {
            info.adicionarParametro(p.vetor ? p.tipo + "[]" : p.tipo);
        }
        escopo.adicionar(info);
    }

    private void registarAssinaturaFuncao(FuncaoNode func) {
        SymbolInfo existente = escopo.procurar(func.nome);
        if (existente != null && existente.getCategoria() == SymbolInfo.Categoria.FUNCAO) {
            // Já registado via protótipo — verificar consistência do tipo de retorno
            if (!existente.getTipo().equals(func.tipo)) {
                erro("Tipo de retorno de '" + func.nome
                        + "' difere do protótipo declarado");
            }
            return;
        }
        SymbolInfo info = new SymbolInfo(func.nome, func.tipo, SymbolInfo.Categoria.FUNCAO);
        for (ParametroNode p : func.parametros) {
            info.adicionarParametro(p.vetor ? p.tipo + "[]" : p.tipo);
        }
        if (!escopo.adicionar(info)) {
            erro("Função já definida: '" + func.nome + "'");
        }
    }

    // -------------------------------------------------------------------------
    // Análise de funções
    // -------------------------------------------------------------------------

    private void analisarCorpoFuncao(FuncaoNode func) {
        tipoFuncaoAtual = func.tipo;
        escopo.entrarEscopo();

        // Adicionar parâmetros ao escopo da função
        for (ParametroNode p : func.parametros) {
            if (p.nome != null) {
                SymbolInfo.Categoria cat = p.vetor
                        ? SymbolInfo.Categoria.VETOR
                        : SymbolInfo.Categoria.PARAMETRO;
                String tipoSimbolo = p.vetor ? p.tipo + "[]" : p.tipo;
                SymbolInfo info = new SymbolInfo(p.nome, tipoSimbolo, cat);
                if (!escopo.adicionar(info)) {
                    erro("Parâmetro duplicado: '" + p.nome + "' na função '" + func.nome + "'");
                }
            }
        }

        analisarComposta(func.corpo);

        escopo.sairEscopo();
        tipoFuncaoAtual = null;
    }

    // -------------------------------------------------------------------------
    // Análise de afirmações
    // -------------------------------------------------------------------------

    private void analisarComposta(AfirmacaoCompostaNode bloco) {
        if (bloco == null) return;
        escopo.entrarEscopo();
        for (ASTNode elem : bloco.corpo) {
            analisarElemento(elem);
        }
        escopo.sairEscopo();
    }

    private void analisarElemento(ASTNode no) {
        if (no instanceof DeclaracaoNode) {
            analisarDeclaracaoLocal((DeclaracaoNode) no);
        } else if (no instanceof AfirmacaoExpressaoNode) {
            analisarExpressao(((AfirmacaoExpressaoNode) no).expressao);
        } else if (no instanceof AfirmacaoCompostaNode) {
            analisarComposta((AfirmacaoCompostaNode) no);
        } else if (no instanceof SeNode) {
            analisarSe((SeNode) no);
        } else if (no instanceof EnquantoNode) {
            analisarEnquanto((EnquantoNode) no);
        } else if (no instanceof ParaNode) {
            analisarPara((ParaNode) no);
        } else if (no instanceof RetornarNode) {
            analisarRetornar((RetornarNode) no);
        }
        // AfirmacaoNode genérico — delega para os seus filhos, não necessita ação extra
    }

    private void analisarDeclaracaoLocal(DeclaracaoNode decl) {
        for (DeclaradorNode d : decl.declaradores) {
            SymbolInfo.Categoria cat = d.vetor
                    ? SymbolInfo.Categoria.VETOR
                    : SymbolInfo.Categoria.VARIAVEL;
            String tipoSimbolo = d.vetor ? decl.tipo + "[]" : decl.tipo;
            SymbolInfo info = new SymbolInfo(d.nome, tipoSimbolo, cat);
            if (!escopo.adicionar(info)) {
                erro("Variável já declarada no mesmo escopo: '" + d.nome + "'");
            }
            if (d.init != null) {
                for (ASTNode elem : d.init.elementos) {
                    analisarExpressao(elem);
                }
            }
        }
    }

    private void analisarSe(SeNode no) {
        analisarExpressao(no.condicao);
        analisarComposta(no.entao);
        if (no.senao != null) analisarComposta(no.senao);
    }

    private void analisarEnquanto(EnquantoNode no) {
        analisarExpressao(no.condicao);
        analisarComposta(no.corpo);
    }

    private void analisarPara(ParaNode no) {
        if (no.init != null) analisarExpressao(no.init);
        if (no.condicao != null) analisarExpressao(no.condicao);
        if (no.incremento != null) analisarExpressao(no.incremento);
        analisarComposta(no.corpo);
    }

    private void analisarRetornar(RetornarNode no) {
        if (no.expressao != null) {
            Tipo tipoExpr = analisarExpressao(no.expressao);
            if (tipoFuncaoAtual != null && tipoFuncaoAtual.equals("vazio")) {
                erro("Função 'vazio' não pode retornar um valor");
            }
        } else {
            if (tipoFuncaoAtual != null && !tipoFuncaoAtual.equals("vazio")) {
                erro("Função '" + tipoFuncaoAtual + "' deve retornar um valor");
            }
        }
    }

    // -------------------------------------------------------------------------
    // Análise e inferência de tipo de expressões
    // -------------------------------------------------------------------------

    private Tipo analisarExpressao(ASTNode no) {
        if (no == null) return Tipo.VAZIO;

        if (no instanceof LiteralIntNode) {
            return ((LiteralIntNode) no).tipo; // INTEIRO
        }

        if (no instanceof LiteralRealNode) {
            return ((LiteralRealNode) no).tipo; // REAL
        }

        if (no instanceof LiteralStringNode) {
            return ((LiteralStringNode) no).tipo; // STRING
        }

        if (no instanceof IDNode) {
            return analisarID((IDNode) no);
        }

        if (no instanceof OpBinNode) {
            return analisarOpBin((OpBinNode) no);
        }

        if (no instanceof OpUnNode) {
            return analisarOpUn((OpUnNode) no);
        }

        if (no instanceof ChamadaFuncaoNode) {
            return analisarChamada((ChamadaFuncaoNode) no);
        }

        if (no instanceof AcessoVetorNode) {
            return analisarAcessoVetor((AcessoVetorNode) no);
        }

        return Tipo.DESCONHECIDO;
    }

    private Tipo analisarID(IDNode no) {
        SymbolInfo info = escopo.procurar(no.nome);
        if (info == null) {
            erro("Variável não declarada: '" + no.nome + "'");
            no.tipo = Tipo.DESCONHECIDO;
            return Tipo.DESCONHECIDO;
        }
        Tipo t = stringParaTipo(info.getTipo());
        no.tipo = t;
        return t;
    }

    private Tipo analisarOpBin(OpBinNode no) {
        Tipo esq = analisarExpressao(no.esq);
        Tipo dir = analisarExpressao(no.dir);

        Tipo resultado;
        switch (no.op) {
            case "=":
                // Atribuição: tipo é o lado direito; verificar compatibilidade simples
                if (!compativel(esq, dir)) {
                    erro("Tipos incompatíveis na atribuição: " + esq + " = " + dir);
                }
                resultado = dir;
                break;
            case "&&": case "||": case "==": case "!=":
            case "<":  case ">":  case "<=": case ">=":
                // Operadores relacionais/lógicos produzem INTEIRO (0 ou 1)
                resultado = Tipo.INTEIRO;
                break;
            default:
                // Aritméticos: promover para REAL se algum operando for REAL
                resultado = promover(esq, dir);
        }
        no.tipo = resultado;
        return resultado;
    }

    private Tipo analisarOpUn(OpUnNode no) {
        Tipo t = analisarExpressao(no.operando);
        Tipo resultado;
        if (no.op.equals("!")) {
            resultado = Tipo.INTEIRO;
        } else if (no.op.startsWith("(")) {
            // Cast: extrai o tipo do operador "(inteiro)" ou "(real)"
            String castStr = no.op.substring(1, no.op.length() - 1);
            resultado = stringParaTipo(castStr);
        } else {
            resultado = t; // negação aritmética mantém o tipo
        }
        no.tipo = resultado;
        return resultado;
    }

    private Tipo analisarChamada(ChamadaFuncaoNode no) {
        SymbolInfo info = escopo.procurar(no.nome);
        if (info == null) {
            erro("Função não declarada: '" + no.nome + "'");
            no.tipo = Tipo.DESCONHECIDO;
            return Tipo.DESCONHECIDO;
        }
        if (info.getCategoria() != SymbolInfo.Categoria.FUNCAO) {
            erro("'" + no.nome + "' não é uma função");
            no.tipo = Tipo.DESCONHECIDO;
            return Tipo.DESCONHECIDO;
        }

        // Verificar número de argumentos (funções built-in ignoradas)
        int nParams = info.getTiposParametros().size();
        int nArgs = no.argumentos.size();
        if (nParams > 0 && nArgs != nParams) {
            erro("Número de argumentos incorreto em chamada a '" + no.nome
                    + "': esperado " + nParams + ", recebido " + nArgs);
        }

        for (ASTNode arg : no.argumentos) {
            analisarExpressao(arg);
        }

        Tipo t = stringParaTipo(info.getTipo());
        no.tipo = t;
        return t;
    }

    private Tipo analisarAcessoVetor(AcessoVetorNode no) {
        Tipo base = analisarExpressao(no.base);
        Tipo idx = analisarExpressao(no.indice);
        if (idx != Tipo.INTEIRO && idx != Tipo.DESCONHECIDO) {
            erro("Índice de vetor deve ser inteiro");
        }
        // Elemento de INTEIRO_VETOR é INTEIRO, de REAL_VETOR é REAL
        Tipo elem = (base == Tipo.REAL_VETOR) ? Tipo.REAL : Tipo.INTEIRO;
        no.tipo = elem;
        return elem;
    }

    // -------------------------------------------------------------------------
    // Utilitários
    // -------------------------------------------------------------------------

    private Tipo stringParaTipo(String s) {
        if (s == null) return Tipo.DESCONHECIDO;
        switch (s) {
            case "inteiro":   return Tipo.INTEIRO;
            case "inteiro[]": return Tipo.INTEIRO_VETOR;
            case "real":      return Tipo.REAL;
            case "real[]":    return Tipo.REAL_VETOR;
            case "vazio":     return Tipo.VAZIO;
            default:          return Tipo.DESCONHECIDO;
        }
    }

    private Tipo promover(Tipo a, Tipo b) {
        if (a == Tipo.REAL || b == Tipo.REAL) return Tipo.REAL;
        if (a == Tipo.INTEIRO || b == Tipo.INTEIRO) return Tipo.INTEIRO;
        return Tipo.DESCONHECIDO;
    }

    private boolean compativel(Tipo alvo, Tipo origem) {
        if (alvo == Tipo.DESCONHECIDO || origem == Tipo.DESCONHECIDO) return true;
        if (alvo == origem) return true;
        // Permite promover INTEIRO → REAL
        if (alvo == Tipo.REAL && origem == Tipo.INTEIRO) return true;
        return false;
    }

    private void erro(String mensagem) {
        erros.add("[Semântico] " + mensagem);
    }
}
