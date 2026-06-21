package mocp.ast;

/**
 * Nó que representa o acesso a um elemento de vetor.
 * Ex.: "v[i]", "v[i+1]"
 * O tipo de resultado é o tipo do elemento (INTEIRO ou REAL),
 * anotado pelo analisador semântico.
 */
public class AcessoVetorNode extends ASTNode {
    private final String id;
    private final ASTNode indice;

    public AcessoVetorNode(String id, ASTNode indice) {
        this.id = id;
        this.indice = indice;
    }

    // O TACGenerator usa getId()
    public String getId() {
        return id;
    }

    // O TACGenerator usa getIndice()
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

