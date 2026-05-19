package mocp.tac;

// param arg  — empurra argumento antes de uma chamada de função
public class ParamInstr extends Instruction {
    public final String arg;

    public ParamInstr(String arg) {
        this.arg = arg;
    }

    @Override
    public String toString() {
        return "param " + arg;
    }
}
