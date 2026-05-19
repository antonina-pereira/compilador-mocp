package mocp.ast;

/**
 * Nó que representa o acesso a um elemento de vetor.
 * Ex.: "v[i]", "v[i+1]"
 * O tipo de resultado é o tipo do elemento (INTEIRO ou REAL),
 * anotado pelo analisador semântico.
 */
public class AcessoVetorNode extends ASTNode {

    public final ASTNode base;    // normalmente IDNode, mas pode ser outro AcessoVetorNode
    public final ASTNode indice;
    public Tipo tipo = Tipo.DESCONHECIDO;

    public AcessoVetorNode(ASTNode base, ASTNode indice) {
        this.base = base;
        this.indice = indice;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "AcessoVetor");
        System.out.println(indent + "  Base:");
        base.print(indent + "    ");
        System.out.println(indent + "  Indice:");
        indice.print(indent + "    ");
    }
}
