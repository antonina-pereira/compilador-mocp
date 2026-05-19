package mocp.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * Nó que representa uma declaração de variáveis.
 * Ex.: "inteiro i, j = 0;" ou "real v[100];"
 */
public class DeclaracaoNode extends ASTNode {

    public final String tipo;
    public final List<DeclaradorNode> declaradores = new ArrayList<>();

    public DeclaracaoNode(String tipo) {
        this.tipo = tipo;
    }

    public void adicionarDeclarador(DeclaradorNode d) {
        if (d != null) declaradores.add(d);
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Declaracao: " + tipo);
        for (DeclaradorNode d : declaradores) d.print(indent + "  ");
    }
}
