package mocp.ast;

/**
 * Nó que representa um único declarador dentro de uma declaração.
 * Ex.: "n", "v[100]", "x = 5", "v[] = {1,2,3}"
 */
public class DeclaradorNode extends ASTNode {

    public final String nome;
    public final boolean vetor;
    public final Integer dimensao;       // null se sem dimensão explícita
    public final InicializadorNode init; // null se sem inicializador

    public DeclaradorNode(String nome, boolean vetor, Integer dimensao, InicializadorNode init) {
        this.nome = nome;
        this.vetor = vetor;
        this.dimensao = dimensao;
        this.init = init;
    }

    @Override
    public void print(String indent) {
        StringBuilder sb = new StringBuilder(indent + "Declarador: " + nome);
        if (vetor) {
            sb.append("[");
            if (dimensao != null) sb.append(dimensao);
            sb.append("]");
        }
        System.out.println(sb);
        if (init != null) init.print(indent + "  ");
    }
}
