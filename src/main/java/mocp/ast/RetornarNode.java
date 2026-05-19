package mocp.ast;

/**
 * Nó que representa uma afirmação retornar.
 * Ex.: "retornar x * 2;" ou "retornar;" (para funções vazio)
 */
public class RetornarNode extends AfirmacaoNode {

    public final ASTNode expressao; // null em "retornar;"

    public RetornarNode(ASTNode expressao) {
        this.expressao = expressao;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Retornar");
        if (expressao != null) expressao.print(indent + "  ");
    }
}
