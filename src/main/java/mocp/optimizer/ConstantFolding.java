package mocp.optimizer;

import java.util.ArrayList;
import java.util.List;
import mocp.tac.*;

public class ConstantFolding {

    public List<Instruction> otimizar(List<Instruction> instrucoes) {
        List<Instruction> resultado = new ArrayList<>();
        for (Instruction instr : instrucoes) {
            if (instr instanceof OpBinInstr) {
                OpBinInstr op = (OpBinInstr) instr;
                // ALTERADO: Usar 'esq' e 'dir' que são os atributos reais da tua classe!
                if (isNumerico(op.esq) && isNumerico(op.dir)) {
                    String val = calcular(op.esq, op.op, op.dir);
                    if (val != null) {
                        resultado.add(new AtribuirInstr(op.dest, val));
                        continue;
                    }
                }
            } else if (instr instanceof OpUnInstr) {
                OpUnInstr op = (OpUnInstr) instr;
                if (op.op.equals("-") && isNumerico(op.operando)) {
                    double v = Double.parseDouble(op.operando);
                    resultado.add(new AtribuirInstr(op.dest, formatNum(-v)));
                    continue;
                }
                if (op.op.equals("!") && isNumerico(op.operando)) {
                    double v = Double.parseDouble(op.operando);
                    resultado.add(new AtribuirInstr(op.dest, v == 0 ? "1" : "0"));
                    continue;
                }
            }
            resultado.add(instr);
        }
        return resultado;
    }

    private boolean isNumerico(String s) {
        if (s == null) return false;
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String calcular(String esqStr, String op, String dirStr) {
        try {
            double a = Double.parseDouble(esqStr);
            double b = Double.parseDouble(dirStr);
            switch (op) {
                case "+":  return formatNum(a + b);
                case "-":  return formatNum(a - b);
                case "*":  return formatNum(a * b);
                case "/":  return b == 0 ? null : formatNum(a / b);
                case "%":  return b == 0 ? null : formatNum(a % b);
                case "<":  return (a <  b) ? "1" : "0";
                case ">":  return (a >  b) ? "1" : "0";
                case "<=": return (a <= b) ? "1" : "0";
                case ">=": return (a >= b) ? "1" : "0";
                case "==": return (a == b) ? "1" : "0";
                case "!=": return (a != b) ? "1" : "0";
                case "&&": return (a != 0 && b != 0) ? "1" : "0";
                case "||": return (a != 0 || b != 0) ? "1" : "0";
                default:   return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String formatNum(double d) {
        if (d == Math.floor(d) && !Double.isInfinite(d)) {
            return String.valueOf((long) d);
        }
        return String.valueOf(d);
    }
}