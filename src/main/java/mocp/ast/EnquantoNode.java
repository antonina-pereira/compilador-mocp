package mocp.ast;

public class EnquantoNode extends ASTNode {
    private final ASTNode condicao;
    private final ASTNode corpo;

    public EnquantoNode(ASTNode condicao, ASTNode corpo) {
        this.condicao = condicao;
        this.corpo = corpo;
    }

    // ADICIONA ESTES MÉTODOS:
    public ASTNode getCondicao() {
        return condicao;
    }

    public ASTNode getCorpo() {
        return corpo;
    }


    @Override
    public void print(String indent) {
        System.out.println(indent + "Loop ENQUANTO");
        if (condicao != null) condicao.print(indent + "  Condicao: ");
        if (corpo != null) corpo.print(indent + "  Bloco: ");
    }
}