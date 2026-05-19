package mocp.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * Nó que representa a definição completa de uma função.
 * Ex.: "inteiro fact(inteiro k) { ... }"
 */
public class FuncaoNode extends ASTNode {

    public final String tipo;
    public final String nome;
    public final List<ParametroNode> parametros = new ArrayList<>();
    public AfirmacaoCompostaNode corpo;

    public FuncaoNode(String tipo, String nome) {
        this.tipo = tipo;
        this.nome = nome;
    }

    public void adicionarParametro(ParametroNode p) {
        if (p != null) parametros.add(p);
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Funcao: " + tipo + " " + nome);
        for (ParametroNode p : parametros) p.print(indent + "  ");
        if (corpo != null) corpo.print(indent + "  ");
    }
}
