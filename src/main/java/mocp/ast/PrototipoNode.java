package mocp.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * Nó que representa a declaração de protótipo de uma função.
 * Ex.: "inteiro fact(inteiro);"
 */
public class PrototipoNode extends ASTNode {

    public final String tipo;
    public final String nome;
    public final List<ParametroNode> parametros = new ArrayList<>();

    public PrototipoNode(String tipo, String nome) {
        this.tipo = tipo;
        this.nome = nome;
    }

    public void adicionarParametro(ParametroNode p) {
        if (p != null) parametros.add(p);
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Prototipo: " + tipo + " " + nome);
        for (ParametroNode p : parametros) p.print(indent + "  ");
    }
}
