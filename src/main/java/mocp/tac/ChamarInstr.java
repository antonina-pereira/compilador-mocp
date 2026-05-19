package mocp.tac;

// dest = call nome, nArgs
public class ChamarInstr extends Instruction {
    public final String dest;   // null se a função não retorna valor útil
    public final String nome;
    public final int nArgs;

    public ChamarInstr(String dest, String nome, int nArgs) {
        this.dest = dest;
        this.nome = nome;
        this.nArgs = nArgs;
    }

    @Override
    public String toString() {
        String chamada = "call " + nome + ", " + nArgs;
        return dest != null ? dest + " = " + chamada : chamada;
    }
}
