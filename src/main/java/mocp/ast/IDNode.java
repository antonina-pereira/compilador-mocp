package mocp.ast;

public class IDNode extends ASTNode {
    private final String nome;

    public IDNode(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Identificador: " + nome);
    }
}