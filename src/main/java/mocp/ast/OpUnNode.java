package mocp.ast;

/**
 * Nó que representa uma operação unária.
 * Cobre negação lógica (!), negação aritmética (-) e cast ((tipo) expr).
 * O tipo de resultado é anotado pelo analisador semântico.
 */
public class OpUnNode extends ASTNode {
    private final String operador;
    private final ASTNode expressao;

    public OpUnNode(String operador, ASTNode expressao) {
        this.operador = operador;
        this.expressao = expressao;
    }

    public ASTNode getExpressao() {
        return expressao;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "OpUnaria (" + operador + ")");
        if (expressao != null) expressao.print(indent + "  ");
    }
}

