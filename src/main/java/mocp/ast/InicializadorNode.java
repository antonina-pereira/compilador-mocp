package mocp.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * Nó que representa o inicializador de um declarador.
 * Pode ser uma expressão simples (ex.: x = 5) ou uma lista (ex.: v[] = {1,2,3}).
 */
public class InicializadorNode extends ASTNode {

    /** true se for lista {e1, e2, ...}; false se for expressão simples. */
    public final boolean isLista;
    public final List<ASTNode> elementos = new ArrayList<>();

    public InicializadorNode(boolean isLista) {
        this.isLista = isLista;
    }

    public void adicionarElemento(ASTNode no) {
        if (no != null) elementos.add(no);
    }

    @Override
    public void print(String indent) {
        if (isLista) {
            System.out.println(indent + "Inicializador: lista");
            for (ASTNode e : elementos) e.print(indent + "  ");
        } else if (!elementos.isEmpty()) {
            System.out.println(indent + "Inicializador:");
            elementos.get(0).print(indent + "  ");
        }
    }
}
