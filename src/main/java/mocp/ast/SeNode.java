package mocp.ast;

/**
 * Nó que representa uma afirmação condicional se/senao.
 * O ramo "senao" é opcional (pode ser null).
 */
public class SeNode extends ASTNode {
    private ASTNode condicao;
    private ASTNode blocoSe;
    private ASTNode blocoSenao;

    public SeNode(ASTNode condicao, ASTNode blocoSe, ASTNode blocoSenao) {
        this.condicao = condicao;
        this.blocoSe = blocoSe;
        this.blocoSenao = blocoSenao;
    }

    public ASTNode getCondicao() { return condicao; }
    public ASTNode getBlocoSe() { return blocoSe; }
    public ASTNode getBlocoSenao() { return blocoSenao; }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Instrucao SE");
        if (condicao != null) condicao.print(indent + "  Cond:");
        if (blocoSe != null) blocoSe.print(indent + "  Entao:");
        if (blocoSenao != null) blocoSenao.print(indent + "  Senao:");
    }
}

