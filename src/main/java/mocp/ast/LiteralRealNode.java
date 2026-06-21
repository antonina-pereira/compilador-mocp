package mocp.ast;

/**
 * Nó que representa um literal real (número decimal).
 * Ex.: 3.14, 0.5
 */
public class LiteralRealNode extends ASTNode {
    private final String valor;

    public LiteralRealNode(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Literal Real: " + valor);
    }
}
