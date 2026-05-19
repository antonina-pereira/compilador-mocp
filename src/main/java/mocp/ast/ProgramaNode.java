package mocp.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * Nó raiz da AST. Contém todas as declarações, protótipos e
 * definições de funções presentes no programa.
 */
public class ProgramaNode extends ASTNode {

    /** Elementos de topo: DeclaracaoNode, PrototipoNode, FuncaoNode. */
    public final List<ASTNode> elementos = new ArrayList<>();

    public void adicionar(ASTNode no) {
        if (no != null) elementos.add(no);
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Programa");
        for (ASTNode e : elementos) {
            e.print(indent + "  ");
        }
    }
}
