package mocp.tac;

// Cria variáveis artificiais apenas usadas no TAC
public class TempCreator {
    private int count = 0;
    public String newTemp() {
        return "t" + (++count);
    }
}
