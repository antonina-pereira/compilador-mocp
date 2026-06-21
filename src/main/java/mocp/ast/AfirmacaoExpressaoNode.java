package mocp.ast;

/**
 * Nó que representa uma afirmação-expressão.
 * Ex.: "i = i + 1;" ou "escrever(x);"
 * A expressão interna pode ser null para afirmações vazias (";").
 */
public class AfirmacaoExpressaoNode extends AfirmacaoNode {

    public final ASTNode expressao; // pode ser null

    public AfirmacaoExpressaoNode(ASTNode expressao) {
        this.expressao = expressao;
    }

    public ASTNode getExpressao() {
      return expressao;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "AfirmacaoExpressao");
        if (expressao != null) expressao.print(indent + "  ");
    }
}
