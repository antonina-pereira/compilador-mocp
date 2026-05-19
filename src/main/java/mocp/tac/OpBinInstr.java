package mocp.tac;

// dest = esq op dir
public class OpBinInstr extends Instruction {
    public final String dest;
    public final String esq;
    public final String op;
    public final String dir;

    public OpBinInstr(String dest, String esq, String op, String dir) {
        this.dest = dest;
        this.esq = esq;
        this.op = op;
        this.dir = dir;
    }

    @Override
    public String toString() {
        return dest + " = " + esq + " " + op + " " + dir;
    }
}
