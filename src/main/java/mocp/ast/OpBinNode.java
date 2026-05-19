package mocp.ast;

/**
 * Nó que representa uma operação binária.
 * Cobre operações aritméticas (+, -, *, /, %), lógicas (&&, ||),
 * relacionais (==, !=, <, >, <=, >=) e atribuição (=).
 * O tipo de resultado é anotado pelo analisador semântico.
 */
public class OpBinNode extends ASTNode {

    public final ASTNode esq;
    public final String op;
    public final ASTNode dir;
    public Tipo tipo = Tipo.DESCONHECIDO;

    public OpBinNode(ASTNode esq, String op, ASTNode dir) {
        this.esq = esq;
        this.op = op;
        this.dir = dir;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "OpBin: " + op);
        esq.print(indent + "  ");
        dir.print(indent + "  ");
    }
}
