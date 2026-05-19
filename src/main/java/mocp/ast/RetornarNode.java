package mocp.ast;

public class RetornarNode extends ASTNode {
    private ASTNode expressao = null;

    // Permite definir a expressão a retornar (ex: retornar x;)
    public void setExpressao(ASTNode expressao) {
        this.expressao = expressao;
    }

    // --- GETTERS ADICIONADOS PARA O SEMÂNTICO ---
    public ASTNode getExpressao() {
        return expressao;
    }
    // --------------------------------------------

    @Override
    public void print(String indent) {
        System.out.println(indent + "Retornar");
        if (expressao != null) {
            expressao.print(indent + "  ");
        }
    }
}