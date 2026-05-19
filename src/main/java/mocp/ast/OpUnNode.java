package mocp.ast;

/**
 * Nó que representa uma operação unária.
 * Cobre negação lógica (!), negação aritmética (-) e cast ((tipo) expr).
 * O tipo de resultado é anotado pelo analisador semântico.
 */
public class OpUnNode extends ASTNode {

    public final String op;          // "!", "-" ou "(inteiro)" / "(real)"
    public final ASTNode operando;
    public Tipo tipo = Tipo.DESCONHECIDO;

    public OpUnNode(String op, ASTNode operando) {
        this.op = op;
        this.operando = operando;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "OpUn: " + op);
        operando.print(indent + "  ");
    }
}
