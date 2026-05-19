package mocp.ast;

public class EnquantoNode extends ASTNode {
    private ASTNode condicao;
    private ASTNode corpo;

    public EnquantoNode(ASTNode condicao, ASTNode corpo) {
        this.condicao = condicao;
        this.corpo = corpo;
    }

    public ASTNode getCondicao() { return condicao; }
    public ASTNode getCorpo() { return corpo; }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Loop ENQUANTO");
        if (condicao != null) condicao.print(indent + "  Cond:");
        if (corpo != null) corpo.print(indent + "  Bloco:");
    }
}