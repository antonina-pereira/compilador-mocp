package mocp.tac;

// vaiPara rotulo
public class VaiParaInstr extends Instruction {
    public final String rotulo;

    public VaiParaInstr(String rotulo) {
        this.rotulo = rotulo;
    }

    @Override
    public String toString() {
        return "vaiPara " + rotulo;
    }
}
