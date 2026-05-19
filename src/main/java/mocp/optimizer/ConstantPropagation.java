package mocp.optimizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mocp.tac.*;

/**
 * Otimização de propagação de constantes.
 *
 * Rastreia variáveis com valor constante conhecido. Quando uma variável com
 * valor constante é usada numa instrução, substitui-a pelo valor.
 * Ex.: "x = 5; t1 = x + 1"  →  "x = 5; t1 = 5 + 1"
 *
 * Rótulos invalidam o mapa de constantes (possível ponto de entrada de salto).
 */
public class ConstantPropagation {

    public List<Instruction> otimizar(List<Instruction> instrucoes) {
        Map<String, String> constantes = new HashMap<>();
        List<Instruction> resultado = new ArrayList<>();

        for (Instruction instr : instrucoes) {
            if (instr instanceof AtribuirInstr) {
                AtribuirInstr a = (AtribuirInstr) instr;
                String src = substituir(a.src, constantes);
                if (isConstante(src)) {
                    constantes.put(a.dest, src);
                } else {
                    constantes.remove(a.dest);
                }
                resultado.add(new AtribuirInstr(a.dest, src));

            } else if (instr instanceof OpBinInstr) {
                OpBinInstr op = (OpBinInstr) instr;
                String esq = substituir(op.esq, constantes);
                String dir = substituir(op.dir, constantes);
                constantes.remove(op.dest);
                resultado.add(new OpBinInstr(op.dest, esq, op.op, dir));

            } else if (instr instanceof OpUnInstr) {
                OpUnInstr op = (OpUnInstr) instr;
                String operando = substituir(op.operando, constantes);
                constantes.remove(op.dest);
                resultado.add(new OpUnInstr(op.dest, op.op, operando));

            } else if (instr instanceof SeFalsoInstr) {
                SeFalsoInstr sf = (SeFalsoInstr) instr;
                String cond = substituir(sf.cond, constantes);
                resultado.add(new SeFalsoInstr(cond, sf.rotulo));

            } else if (instr instanceof RetornaInstr) {
                RetornaInstr r = (RetornaInstr) instr;
                String val = r.val != null ? substituir(r.val, constantes) : null;
                resultado.add(new RetornaInstr(val));

            } else if (instr instanceof ParamInstr) {
                ParamInstr p = (ParamInstr) instr;
                resultado.add(new ParamInstr(substituir(p.arg, constantes)));

            } else if (instr instanceof VetorGuardarInstr) {
                VetorGuardarInstr vg = (VetorGuardarInstr) instr;
                String src = substituir(vg.src, constantes);
                String idx = substituir(vg.indice, constantes);
                resultado.add(new VetorGuardarInstr(vg.base, idx, src));

            } else if (instr instanceof VetorCarregarInstr) {
                VetorCarregarInstr vc = (VetorCarregarInstr) instr;
                String idx = substituir(vc.indice, constantes);
                constantes.remove(vc.dest);
                resultado.add(new VetorCarregarInstr(vc.dest, vc.base, idx));

            } else if (instr instanceof RotuloInstr) {
                // Rótulo: ponto de entrada de salto — invalida todas as constantes
                constantes.clear();
                resultado.add(instr);

            } else {
                resultado.add(instr);
            }
        }
        return resultado;
    }

    private String substituir(String s, Map<String, String> constantes) {
        if (s == null) return null;
        return constantes.getOrDefault(s, s);
    }

    private boolean isConstante(String s) {
        if (s == null) return false;
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return s.startsWith("\"");
        }
    }
}
