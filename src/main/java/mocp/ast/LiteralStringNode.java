package mocp.ast;

/**
 * Nó que representa um literal string.
 * Ex.: "Hello, World!"
 * O valor armazenado não inclui as aspas externas.
 */
public class LiteralStringNode extends ASTNode {

    public final String valor;
    public Tipo tipo = Tipo.STRING;

    public LiteralStringNode(String valor) {
        this.valor = valor;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "LiteralString: \"" + valor + "\"");
    }
}
