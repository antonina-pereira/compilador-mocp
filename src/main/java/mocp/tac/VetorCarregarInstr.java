package mocp.tac;

// dest = base[indice]
public class VetorCarregarInstr extends Instruction {
    public final String dest;
    public final String base;
    public final String indice;

    public VetorCarregarInstr(String dest, String base, String indice) {
        this.dest = dest;
        this.base = base;
        this.indice = indice;
    }

    @Override
    public String toString() {
        return dest + " = " + base + "[" + indice + "]";
    }
}
