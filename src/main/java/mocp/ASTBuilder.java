import mocp.ast.*;

public class ASTBuilder extends MOCPBaseVisitor<ASTNode> {
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

    @Override
    public ASTNode visitDefinicaoFuncao(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    // Parâmetros e argumentos
    @Override
    public ASTNode visitChamadaFuncao(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    @Override
    public ASTNode visitListaParametro(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    @Override
    public ASTNode visitParametro(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    @Override
    public ASTNode visitListaArgumento(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    // Afirmações
    @Override
    public ASTNode visitAfirmacaoExpressao(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    @Override
    public ASTNode visitAfirmacaoComposta(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    @Override
    public ASTNode visitAfirmacaoSe(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    @Override
    public ASTNode visitAfirmacaoEnquanto(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    @Override
    public ASTNode visitAfirmacaoPara(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    @Override
    public ASTNode visitAfirmacaoRetornar(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    // Expressões
    @@Override
    public ASTNode visitExpressao(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    @Override
    public ASTNode visitExpressaoAtribuir(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    @Override
    public ASTNode visitExpressaoOULogica(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    @Override
    public ASTNode visitExpressaoELogica(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    @Override
    public ASTNode visitExpressaoIgualdade(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    @Override
    public ASTNode visitExpressaoRelacional(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    @Override
    public ASTNode visitExpressaoAditiva(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    @Override
    public ASTNode visitExpressaoMultiplicativa(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    @Override
    public ASTNode visitExpressaoUnaria(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    @Override
    public ASTNode visitExpressaoVetor(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }

    @Override
    public ASTNode visitExpressaoSimples(MOCParser.DeclaracaoContext ctx) {
        DeclaracaoNode d = new DeclaracaoNode();
        d.tipo = ctx.especificadorTipo().getText();
        d.nome = ctx.declarador().ID().getText();
        return d;
    }
}
