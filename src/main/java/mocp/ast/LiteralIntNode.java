package mocp.ast;

public class LiteralIntNode extends ASTNode {
    private final String valor;

    public LiteralIntNode(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }



    @Override
    public void print(String indent) {
        System.out.println(indent + "Literal Inteiro: " + valor);
    }
}