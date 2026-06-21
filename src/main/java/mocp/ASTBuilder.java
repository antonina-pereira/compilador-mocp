package mocp;

import mocp.ast.*;

public class ASTBuilder extends MOCPBaseVisitor<ASTNode> {

    @Override
    public ASTNode visitPrograma(MOCPParser.ProgramaContext ctx) {
        ProgramaNode programa = new ProgramaNode();
        for (int i = 0; i < ctx.getChildCount(); i++) {
            ASTNode node = visit(ctx.getChild(i));
            if (node != null) programa.addNode(node);
        }
        return programa;
    }

    @Override
    public ASTNode visitDec_variavel(MOCPParser.Dec_variavelContext ctx) {
        DeclaracaoNode decNode = new DeclaracaoNode(ctx.tipo().getText());
        for (MOCPParser.Dec_itemContext itemCtx : ctx.dec_item()) {
            ASTNode item = visit(itemCtx);
            if (item != null) decNode.addItem(item);
        }
        return decNode;
    }

    @Override
    public ASTNode visitDec_item(MOCPParser.Dec_itemContext ctx) {
        DeclaradorNode declarador = new DeclaradorNode(ctx.ID().getText());

        // Verifica se há definição de tamanho de vetor
        if (ctx.ABRE_RET() != null) {
            if (ctx.INT_VAL() != null) {
                int tamanho = Integer.parseInt(ctx.INT_VAL().getText());
                declarador.setTamanhoVetor(tamanho);
            } else {
                // Caso seja apenas "[]" sem tamanho explícito, usamos 0 para indicar vetor sem tamanho fixo
                declarador.setTamanhoVetor(0);
            }
        }

        if (ctx.inicializador() != null) {
            declarador.setInicializador(visit(ctx.inicializador()));
        }
        return declarador;
    }

    @Override
    public ASTNode visitInicializador(MOCPParser.InicializadorContext ctx) {
        if (ctx.ABRE_CHAV() != null) {
            AfirmacaoCompostaNode listaInit = new AfirmacaoCompostaNode();
            for (MOCPParser.ExpressaoContext exprCtx : ctx.expressao()) listaInit.addInstrucao(visit(exprCtx));
            return listaInit;
        }
        return visit(ctx.expressao(0));
    }

    @Override
    public ASTNode visitPrototipo(MOCPParser.PrototipoContext ctx) {
        PrototipoNode prototipo = new PrototipoNode(ctx.tipo().getText(), ctx.ID() != null ? ctx.ID().getText() : ctx.PRINCIPAL().getText());
        if (ctx.prototipo_params() != null && ctx.prototipo_params().param_tipo() != null) {
            for (MOCPParser.Param_tipoContext pt : ctx.prototipo_params().param_tipo()) {
                prototipo.addTipoParametro(pt.tipo().getText() + (pt.ABRE_RET() != null ? "[]" : ""));
            }
        }
        return prototipo;
    }

    @Override
    public ASTNode visitPrototipo_params(MOCPParser.Prototipo_paramsContext ctx) { return null; }

    @Override
    public ASTNode visitParam_tipo(MOCPParser.Param_tipoContext ctx) { return null; }

    @Override
    public ASTNode visitFuncao(MOCPParser.FuncaoContext ctx) {
        FuncaoNode funcao = new FuncaoNode(ctx.tipo().getText(), ctx.ID() != null ? ctx.ID().getText() : ctx.PRINCIPAL().getText());
        if (ctx.parametros() != null) funcao.setParametros(visit(ctx.parametros()));
        if (ctx.bloco() != null) funcao.setBloco(visit(ctx.bloco()));
        return funcao;
    }

    @Override
    public ASTNode visitParametros(MOCPParser.ParametrosContext ctx) {
        if (ctx.T_VAZIO() != null || ctx.param_dec() == null) return null;
        AfirmacaoCompostaNode listaParams = new AfirmacaoCompostaNode();
        for (MOCPParser.Param_decContext pCtx : ctx.param_dec()) listaParams.addInstrucao(visit(pCtx));
        return listaParams;
    }

    @Override
    public ASTNode visitParam_dec(MOCPParser.Param_decContext ctx) {
        return new ParametroNode(ctx.tipo().getText(), ctx.ID().getText(), ctx.ABRE_RET() != null);
    }

    @Override
    public ASTNode visitBloco(MOCPParser.BlocoContext ctx) {
        AfirmacaoCompostaNode bloco = new AfirmacaoCompostaNode();
        for (MOCPParser.InstrucaoContext instrCtx : ctx.instrucao()) bloco.addInstrucao(visit(instrCtx));
        return bloco;
    }

    @Override
    public ASTNode visitInstrucao(MOCPParser.InstrucaoContext ctx) {
        if (ctx.RETORNAR() != null) {
            RetornarNode ret = new RetornarNode();
            if (ctx.expressao() != null) ret.setExpressao(visit(ctx.expressao()));
            return ret;
        }
        if (ctx.dec_variavel() != null) return visit(ctx.dec_variavel());
        if (ctx.expressao() != null) return visit(ctx.expressao());
        if (ctx.instrucao_se() != null) return visit(ctx.instrucao_se());
        if (ctx.instrucao_enquanto() != null) return visit(ctx.instrucao_enquanto());
        if (ctx.instrucao_para() != null) return visit(ctx.instrucao_para());
        return null;
    }

    @Override
    public ASTNode visitInstrucao_se(MOCPParser.Instrucao_seContext ctx) {
        return new SeNode(visit(ctx.expressao()), visit(ctx.bloco(0)), (ctx.SENAO() != null && ctx.bloco().size() > 1) ? visit(ctx.bloco(1)) : null);
    }

    @Override
    public ASTNode visitInstrucao_enquanto(MOCPParser.Instrucao_enquantoContext ctx) {
        return new EnquantoNode(visit(ctx.expressao()), visit(ctx.bloco()));
    }

    @Override
    public ASTNode visitInstrucao_para(MOCPParser.Instrucao_paraContext ctx) {
        ASTNode init = null, cond = null, inc = null;
        int count = ctx.expressao().size();
        if (count == 3) { init = visit(ctx.expressao(0)); cond = visit(ctx.expressao(1)); inc = visit(ctx.expressao(2)); }
        else if (count == 2) {
            if (ctx.getChild(2).getText().equals(";")) { init = visit(ctx.expressao(0)); inc = visit(ctx.expressao(1)); }
            else { init = visit(ctx.expressao(0)); cond = visit(ctx.expressao(1)); }
        } else if (count == 1) cond = visit(ctx.expressao(0));
        return new ParaNode(init, cond, inc, visit(ctx.bloco()));
    }

    @Override
    public ASTNode visitLista_args(MOCPParser.Lista_argsContext ctx) {
        if (ctx.expressao() == null) return null;
        AfirmacaoCompostaNode listaArgs = new AfirmacaoCompostaNode();
        for (MOCPParser.ExpressaoContext eCtx : ctx.expressao()) listaArgs.addInstrucao(visit(eCtx));
        return listaArgs;
    }

    @Override public ASTNode visitExprParenteses(MOCPParser.ExprParentesesContext ctx) { return visit(ctx.expressao()); }
    //Melhorado o output do TAC para quando ha um cast em vez de aparecer "(cast para real)" aparece "int2real" ou em vez de "(cast para inteiro)" agora aparece "real2int"
    //É para ser mais facil ao fazer o gerador de assembly para entender essa instrucao.
    @Override
    public ASTNode visitExprCast(MOCPParser.ExprCastContext ctx) {
        String tipoDestino = ctx.tipo().getText();
        // Cria uma mnemónica limpa baseada no destino
        String opCast = tipoDestino.equals("real") ? "int2real" : "real2int";
        return new OpUnNode(opCast, visit(ctx.expressao()));
    }
    @Override public ASTNode visitExprMenosUnario(MOCPParser.ExprMenosUnarioContext ctx) { return new OpUnNode("-", visit(ctx.expressao())); }
    @Override public ASTNode visitExprNao(MOCPParser.ExprNaoContext ctx) { return new OpUnNode("!", visit(ctx.expressao())); }
    @Override public ASTNode visitExprMultDiv(MOCPParser.ExprMultDivContext ctx) { return new OpBinNode(ctx.getChild(1).getText(), visit(ctx.expressao(0)), visit(ctx.expressao(1))); }
    @Override public ASTNode visitExprSomaSub(MOCPParser.ExprSomaSubContext ctx) { return new OpBinNode(ctx.getChild(1).getText(), visit(ctx.expressao(0)), visit(ctx.expressao(1))); }
    @Override public ASTNode visitExprRelacional(MOCPParser.ExprRelacionalContext ctx) { return new OpBinNode(ctx.getChild(1).getText(), visit(ctx.expressao(0)), visit(ctx.expressao(1))); }
    @Override public ASTNode visitExprIgualdade(MOCPParser.ExprIgualdadeContext ctx) { return new OpBinNode(ctx.getChild(1).getText(), visit(ctx.expressao(0)), visit(ctx.expressao(1))); }
    @Override public ASTNode visitExprE(MOCPParser.ExprEContext ctx) { return new OpBinNode("&&", visit(ctx.expressao(0)), visit(ctx.expressao(1))); }
    @Override public ASTNode visitExprOu(MOCPParser.ExprOuContext ctx) { return new OpBinNode("||", visit(ctx.expressao(0)), visit(ctx.expressao(1))); }
    @Override public ASTNode visitExprVetorAtrib(MOCPParser.ExprVetorAtribContext ctx) { return new OpBinNode("=", new AcessoVetorNode(ctx.ID().getText(), visit(ctx.expressao(0))), visit(ctx.expressao(1))); }
    @Override public ASTNode visitExprVetor(MOCPParser.ExprVetorContext ctx) { return new AcessoVetorNode(ctx.ID().getText(), visit(ctx.expressao())); }
    @Override public ASTNode visitExprAtribuicao(MOCPParser.ExprAtribuicaoContext ctx) { return new OpBinNode("=", new IDNode(ctx.ID().getText()), visit(ctx.expressao())); }
    @Override
    public ASTNode visitExprChamadaFuncao(MOCPParser.ExprChamadaFuncaoContext ctx) {
        // CORREÇÃO: getChild(0) apanha o ID ou LER, ESCREVER, etc.
        String nomeFuncao = ctx.getChild(0).getText();
        ChamadaFuncaoNode chamada = new ChamadaFuncaoNode(nomeFuncao);

        if (ctx.lista_args() != null) {
            for (MOCPParser.ExpressaoContext exprCtx : ctx.lista_args().expressao()) {
                chamada.addArgumento(visit(exprCtx));
            }
        }
        return chamada;
    }
    @Override public ASTNode visitExprInt(MOCPParser.ExprIntContext ctx) { return new LiteralIntNode(ctx.INT_VAL().getText()); }
    @Override public ASTNode visitExprReal(MOCPParser.ExprRealContext ctx) { return new LiteralRealNode(ctx.FLOAT_VAL().getText()); }
    @Override
    public ASTNode visitExprString(MOCPParser.ExprStringContext ctx) {
        String text = ctx.STRING_VAL().getText();
        // Remove as aspas duplas das extremidades
        if (text.length() >= 2 && text.startsWith("\"") && text.endsWith("\"")) {
            text = text.substring(1, text.length() - 1);
        }
        return new LiteralStringNode(text);
    }
    @Override public ASTNode visitExprChar(MOCPParser.ExprCharContext ctx) { return new LiteralCharNode(ctx.CHAR_VAL().getText()); }
    @Override public ASTNode visitExprId(MOCPParser.ExprIdContext ctx) { return new IDNode(ctx.ID().getText()); }
}