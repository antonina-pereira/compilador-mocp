package mocp.ast;

public class ParametroNode extends ASTNode {
    private final String tipo;
    private final String id;
    private final boolean esVetor;

    public ParametroNode(String tipo, String id, boolean esVetor) {
        this.tipo = tipo;
        this.id = id;
        this.esVetor = esVetor;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Parametro -> " + tipo + (esVetor ? "[] " : " ") + id);
    }
}