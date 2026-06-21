package mocp.ast;

public class LiteralCharNode extends ASTNode {
    private final String valor;

    public LiteralCharNode(String valor) {
        this.valor = valor;
    }

    // Método necessário para a geração de TAC no Canvas (TACGenerator.java)
    public String getValor() {
        return valor;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Literal Char: " + valor);
    }
}