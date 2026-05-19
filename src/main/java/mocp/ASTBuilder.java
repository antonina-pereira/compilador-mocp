public class ASTBuilder extends MOCBaseVisitor<ASTNode> {
    // Nível superior
    @Override
    public ASTNode visitPrograma(MOCParser.ProgramaContext ctx) {
        ProgramaNode prog = new ProgramaNode();

        for (var elem : ctx.children) {
            ASTNode n = elem.accept(this);
            if (n != null) prog.elementos.add(n);
        }

        return prog;
    }

    @Override
    public ASTNode visitDeclaracao(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    // TODO
    @Override
    public ASTNode visitDeclarador(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    } 

    @Override
    public ASTNode visitInicializador(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }


    @Override
    public ASTNode visitDefinicaoPrototipo(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    @Overriderototipo
    public ASTNode visitDefinicaoFuncao(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    // Parâmetros e argumentos
    @Overriderototipo
    public ASTNode visitChamadaFuncao(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    @Overriderototipo
    public ASTNode visitListaParametro(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    @Overriderototipo
    public ASTNode visitParametro(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    @Overriderototipo
    public ASTNode visitListaArgumento(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    // Afirmações
    @Overriderototipo
    public ASTNode visitAfirmacaoExpressao(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    @Overriderototipo
    public ASTNode visitAfirmacaoComposta(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    @Overriderototipo
    public ASTNode visitAfirmacaoSe(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    @Overriderototipo
    public ASTNode visitAfirmacaoEnquanto(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    @Overriderototipo
    public ASTNode visitAfirmacaoPara(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    @Overriderototipo
    public ASTNode visitAfirmacaoRetornar(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    // Expressões
    @Overriderototipo
    public ASTNode visitExpressao(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    @Overriderototipo
    public ASTNode visitExpressaoAtribuir(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    @Overriderototipo
    public ASTNode visitExpressaoOULogica(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    @Overriderototipo
    public ASTNode visitExpressaoELogica(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    @Overriderototipo
    public ASTNode visitExpressaoIgualdade(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    @Overriderototipo
    public ASTNode visitExpressaoRelacional(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    @Overriderototipo
    public ASTNode visitExpressaoAditiva(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    @Overriderototipo
    public ASTNode visitExpressaoMultiplicativa(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    @Overriderototipo
    public ASTNode visitExpressaoUnaria(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    @Overriderototipo
    public ASTNode visitExpressaoVetor(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    @Overriderototipo
    public ASTNode visitExpressaoSimples(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }
}
