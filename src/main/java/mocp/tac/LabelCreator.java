package mocp.tac;

// Cria rótulos artificiais apenas para uso do TAC
public class LabelCreator {
    private int count = 0;
    public String newLabel() {
        return "L" + (++count);
    }
}
