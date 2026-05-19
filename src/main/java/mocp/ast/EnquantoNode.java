package mocp.ast;

public class EnquantoNode extends ASTNode {
    private ASTNode condicao;
    private ASTNode bloco;

    public void setCondicao(ASTNode condicao) {
        this.condicao = condicao;
    }

    public void setBloco(ASTNode bloco) {
        this.bloco = bloco;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Loop ENQUANTO");
        if (condicao != null) {
            System.out.print(indent + "  Condicao: ");
            condicao.print("");
        }
        if (bloco != null) {
            bloco.print(indent + "  ");
        }
    }
}