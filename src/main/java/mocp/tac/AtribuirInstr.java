package mocp.tac;

// dest = src
public class AtribuirInstr extends Instruction {
    public final String dest;
    public final String src;

    public AtribuirInstr(String dest, String src) {
        this.dest = dest;
        this.src = src;
    }

    @Override
    public String toString() {
        return dest + " = " + src;
    }
}
