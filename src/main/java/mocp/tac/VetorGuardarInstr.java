package mocp.tac;

// base[indice] = src
public class VetorGuardarInstr extends Instruction {
    public final String base;
    public final String indice;
    public final String src;

    public VetorGuardarInstr(String base, String indice, String src) {
        this.base = base;
        this.indice = indice;
        this.src = src;
    }

    @Override
    public String toString() {
        return base + "[" + indice + "] = " + src;
    }
}
