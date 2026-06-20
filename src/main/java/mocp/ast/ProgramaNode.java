package mocp.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * Nó raiz da AST. Contém todas as declarações, protótipos e
 * definições de funções presentes no programa.
 */
public class ProgramaNode extends ASTNode {
    private final List<ASTNode> filhos = new ArrayList<>();

    // Método para o ASTBuilder adicionar os nós globais
    public void addNode(ASTNode node) {
        if (node != null) {
            filhos.add(node);
        }
    }

    public List<ASTNode> getFilhos() {
        return filhos;
>>>>>>> origin/jose_barroso
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Programa");
        for (ASTNode filho : filhos) {
            filho.print(indent + "  ");
        }
    }
}

