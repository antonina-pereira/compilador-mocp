package mocp.ast;

public class LiteralStringNode extends ASTNode {
    private final String conteudo;

    public LiteralStringNode(String conteudo) {
        this.conteudo = conteudo;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Literal String: " + conteudo);
    }
}