package mocp.ast;

public class SeNode extends ASTNode {
    private ASTNode condicao;
    private ASTNode blocoSe;
    private ASTNode blocoSenao = null;

    public void setCondicao(ASTNode condicao) {
        this.condicao = condicao;
    }

    public void setBlocoSe(ASTNode blocoSe) {
        this.blocoSe = blocoSe;
    }

    public void setBlocoSenao(ASTNode blocoSenao) {
        this.blocoSenao = blocoSenao;
    }

    // --- GETTERS ADICIONADOS PARA O SEMÂNTICO ---
    public ASTNode getCondicao() {
        return condicao;
    }

    public ASTNode getBlocoSe() {
        return blocoSe;
    }

    public ASTNode getBlocoSenao() {
        return blocoSenao;
    }
    // --------------------------------------------

    @Override
    public void print(String indent) {
        System.out.println(indent + "Instrucao SE");
        if (condicao != null) {
            System.out.println(indent + "  Condicao:");
            condicao.print(indent + "    ");
        }
        if (blocoSe != null) {
            System.out.println(indent + "  Entao:");
            blocoSe.print(indent + "    ");
        }
        if (blocoSenao != null) {
            System.out.println(indent + "  Senao:");
            blocoSenao.print(indent + "    ");
        }
    }
}