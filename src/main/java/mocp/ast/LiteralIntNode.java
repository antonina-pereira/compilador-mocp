package mocp.ast;

/**
 * Nó que representa um literal inteiro.
 * Ex.: 42, 0, 100
 */
public class LiteralIntNode extends ASTNode {

    public final int valor;
    public Tipo tipo = Tipo.INTEIRO;

    public LiteralIntNode(int valor) {
        this.valor = valor;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "LiteralInt: " + valor);
    }
}
