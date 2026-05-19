package mocp.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * Nó que representa um bloco de afirmações delimitado por chavetas.
 * Pode conter declarações e afirmações intercaladas.
 */
public class AfirmacaoCompostaNode extends AfirmacaoNode {

    public final List<ASTNode> corpo = new ArrayList<>();

    public void adicionar(ASTNode no) {
        if (no != null) corpo.add(no);
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Bloco");
        for (ASTNode s : corpo) s.print(indent + "  ");
    }
}
