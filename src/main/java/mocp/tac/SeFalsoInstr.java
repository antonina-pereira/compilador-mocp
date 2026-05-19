package mocp.tac;

// seFalso cond goto rotulo
public class SeFalsoInstr extends Instruction {
    public final String cond;
    public final String rotulo;

    public SeFalsoInstr(String cond, String rotulo) {
        this.cond = cond;
        this.rotulo = rotulo;
    }

    @Override
    public String toString() {
        return "seFalso " + cond + " goto " + rotulo;
    }
}
