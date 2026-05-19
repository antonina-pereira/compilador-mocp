package mocp.ast;

public class DeclaradorNode extends ASTNode {
    private final String id;
    private ASTNode inicializador = null;

    public DeclaradorNode(String id) {
        this.id = id;
    }

    public void setInicializador(ASTNode inicializador) {
        this.inicializador = inicializador;
    }

    // --- GETTERS ADICIONADOS PARA O SEMÂNTICO ---
    public String getId() {
        return id;
    }

    public ASTNode getInicializador() {
        return inicializador;
    }
    // --------------------------------------------

    @Override
    public void print(String indent) {
        System.out.println(indent + "ID: " + id);
        if (inicializador != null) {
            System.out.println(indent + "  Initializador:");
            inicializador.print(indent + "    ");
        }
    }
}