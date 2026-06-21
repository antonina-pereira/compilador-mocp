package mocp.ast;

/**
 * Nó que representa um literal string.
 * Ex.: "Hello, World!"
 * O valor armazenado não inclui as aspas externas.
 */
public class LiteralStringNode extends ASTNode {
    private final String conteudo;

    public LiteralStringNode(String conteudo) {
        this.conteudo = conteudo;

    }

    public String getValor() {
      return conteudo.toString();
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Literal String: " + conteudo);
    }
}
