package mocp.tac;

// retorna val  — val pode ser null para funções vazio
public class RetornaInstr extends Instruction {
    public final String val; // null se retorno sem valor

    public RetornaInstr(String val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return val != null ? "retorna " + val : "retorna";
    }
}
