package mocp.tac;

// dest = op operando
public class OpUnInstr extends Instruction {
    public final String dest;
    public final String op;
    public final String operando;

    public OpUnInstr(String dest, String op, String operando) {
        this.dest = dest;
        this.op = op;
        this.operando = operando;
    }

    @Override
    public String toString() {
        return dest + " = " + op + " " + operando;
    }
}
