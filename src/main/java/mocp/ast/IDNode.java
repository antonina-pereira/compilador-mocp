package mocp.ast;

/**
 * Nó que representa uma referência a um identificador (variável).
 * O tipo é anotado pelo analisador semântico.
 */
public class IDNode extends ASTNode {

    public final String nome;
    public Tipo tipo = Tipo.DESCONHECIDO;

    public IDNode(String nome) {
        this.nome = nome;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "ID: " + nome);
    }
}
