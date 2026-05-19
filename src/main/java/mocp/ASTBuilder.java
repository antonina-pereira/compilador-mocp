package mocp;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import mocp.ast.*;

public class ASTBuilder extends MOCPBaseVisitor<ASTNode> {

    // -------------------------------------------------------------------------
    // Nível superior
    // -------------------------------------------------------------------------

    @Override
    public ASTNode visitPrograma(MOCPParser.ProgramaContext ctx) {
        ProgramaNode no = new ProgramaNode();
        for (ParseTree filho : ctx.children) {
            if (filho instanceof MOCPParser.DeclaracaoContext
                    || filho instanceof MOCPParser.DefinicaoPrototipoContext
                    || filho instanceof MOCPParser.DefinicaoFuncaoContext) {
                no.adicionar(visit(filho));
            }
        }
        return no;
    }

    @Override
    public ASTNode visitDeclaracao(MOCPParser.DeclaracaoContext ctx) {
        String tipo = ctx.especificadorTipo().getText();
        DeclaracaoNode no = new DeclaracaoNode(tipo);
        for (MOCPParser.DeclaradorContext d : ctx.listaDeclarador().declarador()) {
            no.adicionarDeclarador((DeclaradorNode) visit(d));
        }
        return no;
    }

    @Override
    public ASTNode visitDeclarador(MOCPParser.DeclaradorContext ctx) {
        String nome = ctx.ID().getText();
        boolean vetor = !ctx.ECOLCHETE().isEmpty();
        Integer dimensao = null;
        if (!ctx.NUM_INTEIRO().isEmpty()) {
            dimensao = Integer.parseInt(ctx.NUM_INTEIRO(0).getText());
        }
        InicializadorNode init = null;
        if (ctx.inicializador() != null) {
            init = (InicializadorNode) visit(ctx.inicializador());
        }
        return new DeclaradorNode(nome, vetor, dimensao, init);
    }

    @Override
    public ASTNode visitInicializador(MOCPParser.InicializadorContext ctx) {
        if (ctx.ECHAVE() != null) {
            // Lista: { e1, e2, ... }
            InicializadorNode no = new InicializadorNode(true);
            for (MOCPParser.ExpressaoContext e : ctx.expressao()) {
                no.adicionarElemento(visit(e));
            }
            return no;
        } else {
            // Expressão simples
            InicializadorNode no = new InicializadorNode(false);
            no.adicionarElemento(visit(ctx.expressao(0)));
            return no;
        }
    }

    @Override
    public ASTNode visitDefinicaoPrototipo(MOCPParser.DefinicaoPrototipoContext ctx) {
        String tipo = ctx.especificadorTipo().getText();
        String nome = ctx.ID().getText();
        PrototipoNode no = new PrototipoNode(tipo, nome);
        if (ctx.listaParametro() != null) {
            for (MOCPParser.ParametroContext p : ctx.listaParametro().parametro()) {
                no.adicionarParametro((ParametroNode) visit(p));
            }
        }
        return no;
    }

    @Override
    public ASTNode visitDefinicaoFuncao(MOCPParser.DefinicaoFuncaoContext ctx) {
        String tipo = ctx.especificadorTipo().getText();
        String nome = ctx.ID().getText();
        FuncaoNode no = new FuncaoNode(tipo, nome);
        if (ctx.listaParametro() != null) {
            for (MOCPParser.ParametroContext p : ctx.listaParametro().parametro()) {
                no.adicionarParametro((ParametroNode) visit(p));
            }
        }
        no.corpo = (AfirmacaoCompostaNode) visit(ctx.afirmacaoComposta());
        return no;
    }

    // -------------------------------------------------------------------------
    // Parâmetros e argumentos
    // -------------------------------------------------------------------------

    @Override
    public ASTNode visitParametro(MOCPParser.ParametroContext ctx) {
        String tipo = ctx.especificadorTipo().getText();
        String nome = ctx.ID() != null ? ctx.ID().getText() : null;
        boolean vetor = !ctx.ECOLCHETE().isEmpty();
        return new ParametroNode(tipo, nome, vetor);
    }

    @Override
    public ASTNode visitChamadaFuncao(MOCPParser.ChamadaFuncaoContext ctx) {
        String nome = ctx.ID().getText();
        ChamadaFuncaoNode no = new ChamadaFuncaoNode(nome);
        if (ctx.listaArgumento() != null) {
            for (MOCPParser.ExpressaoContext e : ctx.listaArgumento().expressao()) {
                no.adicionarArgumento(visit(e));
            }
        }
        return no;
    }

    @Override
    public ASTNode visitListaParametro(MOCPParser.ListaParametroContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public ASTNode visitListaArgumento(MOCPParser.ListaArgumentoContext ctx) {
        return visitChildren(ctx);
    }

    // -------------------------------------------------------------------------
    // Afirmações
    // -------------------------------------------------------------------------

    @Override
    public ASTNode visitAfirmacaoExpressao(MOCPParser.AfirmacaoExpressaoContext ctx) {
        ASTNode expr = ctx.expressao() != null ? visit(ctx.expressao()) : null;
        return new AfirmacaoExpressaoNode(expr);
    }

    @Override
    public ASTNode visitAfirmacaoComposta(MOCPParser.AfirmacaoCompostaContext ctx) {
        AfirmacaoCompostaNode no = new AfirmacaoCompostaNode();
        for (ParseTree filho : ctx.children) {
            if (filho instanceof MOCPParser.DeclaracaoContext
                    || filho instanceof MOCPParser.AfirmacaoContext) {
                no.adicionar(visit(filho));
            }
        }
        return no;
    }

    @Override
    public ASTNode visitAfirmacaoSe(MOCPParser.AfirmacaoSeContext ctx) {
        ASTNode cond = visit(ctx.expressao());
        AfirmacaoCompostaNode entao = (AfirmacaoCompostaNode) visit(ctx.afirmacaoComposta(0));
        AfirmacaoCompostaNode senao = ctx.afirmacaoComposta().size() > 1
                ? (AfirmacaoCompostaNode) visit(ctx.afirmacaoComposta(1))
                : null;
        return new SeNode(cond, entao, senao);
    }

    @Override
    public ASTNode visitAfirmacaoEnquanto(MOCPParser.AfirmacaoEnquantoContext ctx) {
        ASTNode cond = visit(ctx.expressao());
        AfirmacaoCompostaNode corpo = (AfirmacaoCompostaNode) visit(ctx.afirmacaoComposta());
        return new EnquantoNode(cond, corpo);
    }

    @Override
    public ASTNode visitAfirmacaoPara(MOCPParser.AfirmacaoParaContext ctx) {
        // afirmacaoPara: PARA EPAREN expressao? SEMIVIRGULA expressao? SEMIVIRGULA expressao? DPAREN afirmacaoComposta
        // Determina init/cond/incr pela posição relativa às SEMIVIRGULA
        ASTNode init = null, cond = null, incr = null;
        int semiCount = 0;
        for (ParseTree filho : ctx.children) {
            if (filho instanceof TerminalNode) {
                if (((TerminalNode) filho).getSymbol().getType() == MOCPParser.SEMIVIRGULA) {
                    semiCount++;
                }
            } else if (filho instanceof MOCPParser.ExpressaoContext) {
                ASTNode expr = visit(filho);
                if (semiCount == 0) init = expr;
                else if (semiCount == 1) cond = expr;
                else incr = expr;
            }
        }
        AfirmacaoCompostaNode corpo = (AfirmacaoCompostaNode) visit(ctx.afirmacaoComposta());
        return new ParaNode(init, cond, incr, corpo);
    }

    @Override
    public ASTNode visitAfirmacaoRetornar(MOCPParser.AfirmacaoRetornarContext ctx) {
        ASTNode expr = ctx.expressao() != null ? visit(ctx.expressao()) : null;
        return new RetornarNode(expr);
    }

    // -------------------------------------------------------------------------
    // Expressões
    // -------------------------------------------------------------------------

    @Override
    public ASTNode visitExpressao(MOCPParser.ExpressaoContext ctx) {
        return visit(ctx.expressaoAtribuir());
    }

    @Override
    public ASTNode visitExpressaoAtribuir(MOCPParser.ExpressaoAtribuirContext ctx) {
        ASTNode esq = visit(ctx.expressaoOULogica());
        if (ctx.expressaoAtribuir() != null) {
            return new OpBinNode(esq, "=", visit(ctx.expressaoAtribuir()));
        }
        return esq;
    }

    @Override
    public ASTNode visitExpressaoOULogica(MOCPParser.ExpressaoOULogicaContext ctx) {
        java.util.List<MOCPParser.ExpressaoELogicaContext> ops = ctx.expressaoELogica();
        ASTNode result = visit(ops.get(0));
        for (int i = 1; i < ops.size(); i++) {
            result = new OpBinNode(result, "||", visit(ops.get(i)));
        }
        return result;
    }

    @Override
    public ASTNode visitExpressaoELogica(MOCPParser.ExpressaoELogicaContext ctx) {
        java.util.List<MOCPParser.ExpressaoIgualdadeContext> ops = ctx.expressaoIgualdade();
        ASTNode result = visit(ops.get(0));
        for (int i = 1; i < ops.size(); i++) {
            result = new OpBinNode(result, "&&", visit(ops.get(i)));
        }
        return result;
    }

    @Override
    public ASTNode visitExpressaoIgualdade(MOCPParser.ExpressaoIgualdadeContext ctx) {
        java.util.List<MOCPParser.ExpressaoRelacionalContext> ops = ctx.expressaoRelacional();
        ASTNode result = visit(ops.get(0));
        // Recolhe os operadores (== ou !=) pela ordem em que aparecem nos filhos
        java.util.List<String> operadores = new java.util.ArrayList<>();
        for (ParseTree filho : ctx.children) {
            if (filho instanceof TerminalNode) {
                int tipo = ((TerminalNode) filho).getSymbol().getType();
                if (tipo == MOCPParser.IGUAL) operadores.add("==");
                else if (tipo == MOCPParser.DIFERENTE) operadores.add("!=");
            }
        }
        for (int i = 1; i < ops.size(); i++) {
            result = new OpBinNode(result, operadores.get(i - 1), visit(ops.get(i)));
        }
        return result;
    }

    @Override
    public ASTNode visitExpressaoRelacional(MOCPParser.ExpressaoRelacionalContext ctx) {
        java.util.List<MOCPParser.ExpressaoAditivaContext> ops = ctx.expressaoAditiva();
        ASTNode result = visit(ops.get(0));
        java.util.List<String> operadores = new java.util.ArrayList<>();
        for (ParseTree filho : ctx.children) {
            if (filho instanceof TerminalNode) {
                int tipo = ((TerminalNode) filho).getSymbol().getType();
                if (tipo == MOCPParser.MAIOR) operadores.add(">");
                else if (tipo == MOCPParser.MENOR) operadores.add("<");
                else if (tipo == MOCPParser.MAIOR_IGUAL) operadores.add(">=");
                else if (tipo == MOCPParser.MENOR_IGUAL) operadores.add("<=");
            }
        }
        for (int i = 1; i < ops.size(); i++) {
            result = new OpBinNode(result, operadores.get(i - 1), visit(ops.get(i)));
        }
        return result;
    }

    @Override
    public ASTNode visitExpressaoAditiva(MOCPParser.ExpressaoAditivaContext ctx) {
        java.util.List<MOCPParser.ExpressaoMultiplicativaContext> ops = ctx.expressaoMultiplicativa();
        ASTNode result = visit(ops.get(0));
        java.util.List<String> operadores = new java.util.ArrayList<>();
        for (ParseTree filho : ctx.children) {
            if (filho instanceof TerminalNode) {
                int tipo = ((TerminalNode) filho).getSymbol().getType();
                if (tipo == MOCPParser.MAIS) operadores.add("+");
                else if (tipo == MOCPParser.MENOS) operadores.add("-");
            }
        }
        for (int i = 1; i < ops.size(); i++) {
            result = new OpBinNode(result, operadores.get(i - 1), visit(ops.get(i)));
        }
        return result;
    }

    @Override
    public ASTNode visitExpressaoMultiplicativa(MOCPParser.ExpressaoMultiplicativaContext ctx) {
        java.util.List<MOCPParser.ExpressaoUnariaContext> ops = ctx.expressaoUnaria();
        ASTNode result = visit(ops.get(0));
        java.util.List<String> operadores = new java.util.ArrayList<>();
        for (ParseTree filho : ctx.children) {
            if (filho instanceof TerminalNode) {
                int tipo = ((TerminalNode) filho).getSymbol().getType();
                if (tipo == MOCPParser.MULT) operadores.add("*");
                else if (tipo == MOCPParser.DIV) operadores.add("/");
                else if (tipo == MOCPParser.MODULO) operadores.add("%");
            }
        }
        for (int i = 1; i < ops.size(); i++) {
            result = new OpBinNode(result, operadores.get(i - 1), visit(ops.get(i)));
        }
        return result;
    }

    @Override
    public ASTNode visitExpressaoUnaria(MOCPParser.ExpressaoUnariaContext ctx) {
        if (ctx.NAO() != null) {
            return new OpUnNode("!", visit(ctx.expressaoUnaria()));
        } else if (ctx.MENOS() != null) {
            return new OpUnNode("-", visit(ctx.expressaoUnaria()));
        } else if (ctx.especificadorTipo() != null) {
            String cast = "(" + ctx.especificadorTipo().getText() + ")";
            return new OpUnNode(cast, visit(ctx.expressaoUnaria()));
        } else {
            return visit(ctx.expressaoVetor());
        }
    }

    @Override
    public ASTNode visitExpressaoVetor(MOCPParser.ExpressaoVetorContext ctx) {
        ASTNode base = visit(ctx.expressaoSimples());
        for (MOCPParser.ExpressaoContext idx : ctx.expressao()) {
            base = new AcessoVetorNode(base, visit(idx));
        }
        return base;
    }

    @Override
    public ASTNode visitExpressaoSimples(MOCPParser.ExpressaoSimplesContext ctx) {
        if (ctx.chamadaFuncao() != null) {
            return visit(ctx.chamadaFuncao());
        } else if (ctx.expressao() != null) {
            return visit(ctx.expressao());
        } else if (ctx.ID() != null) {
            return new IDNode(ctx.ID().getText());
        } else if (ctx.NUM_INTEIRO() != null) {
            return new LiteralIntNode(Integer.parseInt(ctx.NUM_INTEIRO().getText()));
        } else if (ctx.NUM_REAL() != null) {
            return new LiteralRealNode(Double.parseDouble(ctx.NUM_REAL().getText()));
        } else {
            // STRING — remove as aspas delimitadoras
            String raw = ctx.STRING().getText();
            return new LiteralStringNode(raw.substring(1, raw.length() - 1));
        }
    }
}
