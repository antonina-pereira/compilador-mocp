package mocp.ast;

/**
 * Nó que representa uma operação binária.
 * Cobre operações aritméticas (+, -, *, /, %), lógicas (&&, ||),
 * relacionais (==, !=, <, >, <=, >=) e atribuição (=).
 * O tipo de resultado é anotado pelo analisador semântico.
 */

public class OpBinNode extends ASTNode {
    private final String operador;
    private final ASTNode esquerda;
    private final ASTNode direita;

    public OpBinNode(String operador, ASTNode esquerda, ASTNode direita) {
        this.operador = operador;
        this.esquerda = esquerda;
        this.direita = direita;
    }

    // --- GETTERS ADICIONADOS PARA O SEMÂNTICO ---
    public String getOperador() {
        return operador;
    }

    public ASTNode getEsquerda() {
        return esquerda;
    }

    public ASTNode getDireita() {
        return direita;
    }
    // --------------------------------------------

    @Override
    public void print(String indent) {
        System.out.println(indent + "OpBin (" + operador + ")");
        if (esquerda != null) esquerda.print(indent + "  ");
        if (direita != null) direita.print(indent + "  ");
    }
}

