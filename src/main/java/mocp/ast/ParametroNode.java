package mocp.ast;

/**
 * Nó que representa um parâmetro formal de uma função.
 * Ex.: "inteiro k", "real v[]"
 */
public class ParametroNode extends ASTNode {
    private final String tipo;
    private final String id;
    private final boolean esVetor;

    public ParametroNode(String tipo, String id, boolean esVetor) {
        this.tipo = tipo;
        this.id = id;
        this.esVetor = esVetor;
    }

    // --- GETTERS ADICIONADOS PARA O SEMÂNTICO ---
    public String getTipo() { return tipo; }
    public String getId() { return id; }
    public boolean isEsVetor() { return esVetor; }
    // --------------------------------------------

    @Override
    public void print(String indent) {
        System.out.println(indent + "Parametro -> " + tipo + (esVetor ? "[] " : " ") + id);
    }
}

