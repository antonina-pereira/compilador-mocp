package mocp.ast;

/**
 * Nó que representa um parâmetro formal de uma função.
 * Ex.: "inteiro k", "real v[]"
 */
public class ParametroNode extends ASTNode {

    public final String tipo;
    public final String nome;   // pode ser null em protótipos
    public final boolean vetor;

    public ParametroNode(String tipo, String nome, boolean vetor) {
        this.tipo = tipo;
        this.nome = nome;
        this.vetor = vetor;
    }

    @Override
    public void print(String indent) {
        String sufixo = vetor ? "[]" : "";
        String id = nome != null ? " " + nome : "";
        System.out.println(indent + "Parametro: " + tipo + sufixo + id);
    }
}
