package mocp.optimizer;

import java.util.ArrayList;
import java.util.List;

import mocp.tac.*;

/**
 * Otimização de eliminação de código morto.
 *
 * Remove instruções que surgem após um salto incondicional ({@link VaiParaInstr})
 * ou um retorno ({@link RetornaInstr}) e que não são rótulos.
 * Um rótulo repõe a acessibilidade do código seguinte (pode ser alvo de salto).
 */
public class DeadCodeElimination {

    public List<Instruction> otimizar(List<Instruction> instrucoes) {
        List<Instruction> resultado = new ArrayList<>();
        boolean morto = false;

        for (Instruction instr : instrucoes) {
            if (instr instanceof RotuloInstr) {
                // Um rótulo pode ser alvo de salto: código volta a ser acessível
                morto = false;
            }
            if (!morto) {
                resultado.add(instr);
            }
            if (instr instanceof RetornaInstr || instr instanceof VaiParaInstr) {
                morto = true;
            }
        }
        return resultado;
    }
}
