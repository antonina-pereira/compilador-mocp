package mocp;

import mocp.ast.ASTNode;

public class ASTBuilder extends MOCPBaseVisitor<ASTNode> {

    // Nível superior
    @Override
    public ASTNode visitPrograma(MOCPParser.ProgramaContext ctx) {
        // TODO
        return null;
    }

    @Override
    public ASTNode visitDeclaracao(MOCPParser.DeclaracaoContext ctx) {
        // TODO
        return null;
    }

    @Override
    public ASTNode visitDeclarador(MOCPParser.DeclaradorContext ctx) {
        // TODO
        return null;
    }

    @Override
    public ASTNode visitInicializador(MOCPParser.InicializadorContext ctx) {
        // TODO
        return null;
    }

    @Override
    public ASTNode visitDefinicaoPrototipo(MOCPParser.DefinicaoPrototipoContext ctx) {
        // TODO
        return null;
    }

    @Override
    public ASTNode visitDefinicaoFuncao(MOCPParser.DefinicaoFuncaoContext ctx) {
        // TODO
        return null;
    }

    // Parâmetros e argumentos
    @Override
    public ASTNode visitChamadaFuncao(MOCPParser.ChamadaFuncaoContext ctx) {
        // TODO
        return null;
    }

    @Override
    public ASTNode visitListaParametro(MOCPParser.ListaParametroContext ctx) {
        // TODO
        return null;
    }

    @Override
    public ASTNode visitParametro(MOCPParser.ParametroContext ctx) {
        // TODO
        return null;
    }

    @Override
    public ASTNode visitListaArgumento(MOCPParser.ListaArgumentoContext ctx) {
        // TODO
        return null;
    }

    // Afirmações
    @Override
    public ASTNode visitAfirmacaoExpressao(MOCPParser.AfirmacaoExpressaoContext ctx) {
        // TODO
        return null;
    }

    @Override
    public ASTNode visitAfirmacaoComposta(MOCPParser.AfirmacaoCompostaContext ctx) {
        // TODO
        return null;
    }

    @Override
    public ASTNode visitAfirmacaoSe(MOCPParser.AfirmacaoSeContext ctx) {
        // TODO
        return null;
    }

    @Override
    public ASTNode visitAfirmacaoEnquanto(MOCPParser.AfirmacaoEnquantoContext ctx) {
        // TODO
        return null;
    }

    @Override
    public ASTNode visitAfirmacaoPara(MOCPParser.AfirmacaoParaContext ctx) {
        // TODO
        return null;
    }

    @Override
    public ASTNode visitAfirmacaoRetornar(MOCPParser.AfirmacaoRetornarContext ctx) {
        // TODO
        return null;
    }

    // Expressões
    @Override
    public ASTNode visitExpressao(MOCPParser.ExpressaoContext ctx) {
        // TODO
        return null;
    }

    @Override
    public ASTNode visitExpressaoAtribuir(MOCPParser.ExpressaoAtribuirContext ctx) {
        // TODO
        return null;
    }

    @Override
    public ASTNode visitExpressaoOULogica(MOCPParser.ExpressaoOULogicaContext ctx) {
        // TODO
        return null;
    }

    @Override
    public ASTNode visitExpressaoELogica(MOCPParser.ExpressaoELogicaContext ctx) {
        // TODO
        return null;
    }

    @Override
    public ASTNode visitExpressaoIgualdade(MOCPParser.ExpressaoIgualdadeContext ctx) {
        // TODO
        return null;
    }

    @Override
    public ASTNode visitExpressaoRelacional(MOCPParser.ExpressaoRelacionalContext ctx) {
        // TODO
        return null;
    }

    @Override
    public ASTNode visitExpressaoAditiva(MOCPParser.ExpressaoAditivaContext ctx) {
        // TODO
        return null;
    }

    @Override
    public ASTNode visitExpressaoMultiplicativa(MOCPParser.ExpressaoMultiplicativaContext ctx) {
        // TODO
        return null;
    }

    @Override
    public ASTNode visitExpressaoUnaria(MOCPParser.ExpressaoUnariaContext ctx) {
        // TODO
        return null;
    }

    @Override
    public ASTNode visitExpressaoVetor(MOCPParser.ExpressaoVetorContext ctx) {
        // TODO
        return null;
    }

    @Override
    public ASTNode visitExpressaoSimples(MOCPParser.ExpressaoSimplesContext ctx) {
        // TODO
        return null;
    }
}
