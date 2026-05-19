package mocp.ast;

public class FuncaoNode extends ASTNode {
    private final String tipo;
    private final String nome;
    private ASTNode bloco;
    private ASTNode parametros = null;

    public FuncaoNode(String tipo, String nome) {
        this.tipo = tipo;
        this.nome = nome;
    }

    public void setBloco(ASTNode bloco) {
        this.bloco = bloco;
    }

    public void setParametros(ASTNode parametros) {
        this.parametros = parametros;
    }

    // --- GETTERS ADICIONADOS PARA O SEMÂNTICO ---
    public String getTipo() {
        return tipo;
    }

    public String getNome() {
        return nome;
    }

    public ASTNode getBloco() {
        return bloco;
    }

    public ASTNode getParametros() {
        return parametros;
    }
    // --------------------------------------------

    @Override
    public void print(String indent) {
        System.out.println(indent + "Funcao: " + nome + " (Retorno: " + tipo + ")");
        if (parametros != null) {
            System.out.println(indent + "  Parametros:");
            parametros.print(indent + "    ");
        }
        if (bloco != null) {
            bloco.print(indent + "  ");
        }
    }
}