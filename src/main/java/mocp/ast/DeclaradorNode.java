package mocp.ast;

/**
 * Nó que representa um único declarador dentro de uma declaração.
 * Ex.: "n", "v[100]", "x = 5", "v[] = {1,2,3}"
 */
public class DeclaradorNode extends ASTNode {
    private final String id;
    private ASTNode inicializador = null;
    private int tamanhoVetor = -1; // -1 = não é vetor

    public DeclaradorNode(String id) {
        this.id = id;
    }

    public void setInicializador(ASTNode inicializador) {
        this.inicializador = inicializador;
    }

    public void setTamanhoVetor(int tamanho) {
        this.tamanhoVetor = tamanho;
    }

    public String getId() {
        return id;
    }

    public ASTNode getInicializador() {
        return inicializador;
    }

    public int getTamanhoVetor() {
        return tamanhoVetor;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "ID: " + id +
                (tamanhoVetor >= 0 ? "[" + (tamanhoVetor == 0 ? "" : tamanhoVetor) + "]" : ""));
        if (inicializador != null) {
            System.out.println(indent + "  Initializador:");
            inicializador.print(indent + "    ");
        }
    }
}

