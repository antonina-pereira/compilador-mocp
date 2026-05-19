package mocp.ast;

public class AcessoVetorNode extends ASTNode {
    private final String id;
    private final ASTNode indice;

    public AcessoVetorNode(String id, ASTNode indice) {
        this.id = id;
        this.indice = indice;
    }

    public String getId() {
        return id;
    }

    public ASTNode getIndice() {
        return indice;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Acesso Vetor: " + id);
        if (indice != null) {
            indice.print(indent + "  [Indice]: ");
        }
    }
}