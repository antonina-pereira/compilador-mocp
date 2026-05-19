package mocp.optimizer;

import java.util.List;

import mocp.tac.Instruction;

/**
 * Orquestrador de otimizações TAC.
 *
 * Executa as otimizações em sequência até a lista de instruções estabilizar
 * (ponto fixo). A ordem é:
 *  1. Dobramento de constantes ({@link ConstantFolding})
 *  2. Propagação de constantes ({@link ConstantPropagation})
 *  3. Eliminação de código morto ({@link DeadCodeElimination})
 */
public class Optimizer {

    private final ConstantFolding cf = new ConstantFolding();
    private final ConstantPropagation cp = new ConstantPropagation();
    private final DeadCodeElimination dce = new DeadCodeElimination();

    public List<Instruction> otimizar(List<Instruction> instrucoes) {
        List<Instruction> atual = instrucoes;
        List<Instruction> anterior;
        do {
            anterior = atual;
            atual = cf.otimizar(atual);
            atual = cp.otimizar(atual);
            atual = dce.otimizar(atual);
        } while (!iguais(atual, anterior));
        return atual;
    }

    private boolean iguais(List<Instruction> a, List<Instruction> b) {
        if (a.size() != b.size()) return false;
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).toString().equals(b.get(i).toString())) return false;
        }
        return true;
    }
}
