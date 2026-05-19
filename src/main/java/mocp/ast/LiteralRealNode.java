package mocp.ast;

/**
 * Nó que representa um literal real (número decimal).
 * Ex.: 3.14, 0.5
 */
public class LiteralRealNode extends ASTNode {

    public final double valor;
    public Tipo tipo = Tipo.REAL;

    public LiteralRealNode(double valor) {
        this.valor = valor;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "LiteralReal: " + valor);
    }
}
