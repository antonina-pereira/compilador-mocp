package mocp.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * Nó que representa um bloco de afirmações delimitado por chavetas.
 * Pode conter declarações e afirmações intercaladas.
 */
public class AfirmacaoCompostaNode extends ASTNode {
    private final List<ASTNode> instrucoes = new ArrayList<>();

    public void addInstrucao(ASTNode instr) {
        if (instr != null) {
            instrucoes.add(instr);
        }
    }

    // --- GETTERS ADICIONADOS PARA O SEMÂNTICO ---
    public List<ASTNode> getInstrucoes() {
        return instrucoes;
    }
    // --------------------------------------------

    @Override
    public void print(String indent) {
        System.out.println(indent + "Bloco {");
        for (ASTNode instr : instrucoes) {
            instr.print(indent + "  ");
        }
        System.out.println(indent + "}");
    }
}

