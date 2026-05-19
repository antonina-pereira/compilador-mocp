package mocp.tac;

// rotulo:
public class RotuloInstr extends Instruction {
    public final String rotulo;

    public RotuloInstr(String rotulo) {
        this.rotulo = rotulo;
    }

    @Override
    public String toString() {
        return rotulo + ":";
    }
}
