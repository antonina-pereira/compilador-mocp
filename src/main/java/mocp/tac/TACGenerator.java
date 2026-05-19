package mocp.tac;

import java.util.ArrayList;
import java.util.List;
import mocp.ast.*;

public class TACGenerator {

    private final TempCreator temps = new TempCreator();
    private final LabelCreator labels = new LabelCreator();
    private final List<Instruction> instrucoes = new ArrayList<>();

    public List<Instruction> gerar(ASTNode programa) {
        System.out.println("\n--- A INICIAR GERAÇÃO DE CÓDIGO INTERMÉDIO (TAC) ---");
        if (programa instanceof ProgramaNode) {
            for (ASTNode elem : ((ProgramaNode) programa).getFilhos()) {
                if (elem instanceof FuncaoNode) {
                    gerarFuncao((FuncaoNode) elem);
                }
            }
        }
        imprimirInstrucoes();
        return instrucoes;
    }

    private void gerarFuncao(FuncaoNode func) {
        emit(new RotuloInstr(func.getNome())); // rotulo:
        gerarElemento(func.getBloco());
    }

    private void gerarComposta(AfirmacaoCompostaNode bloco) {
        if (bloco == null) return;
        for (ASTNode elem : bloco.getInstrucoes()) {
            gerarElemento(elem);
        }
    }

    private void gerarElemento(ASTNode no) {
        if (no == null) return;

        if (no instanceof DeclaracaoNode) {
            gerarDeclaracao((DeclaracaoNode) no);
        } else if (no instanceof AfirmacaoCompostaNode) {
            gerarComposta((AfirmacaoCompostaNode) no);
        } else if (no instanceof SeNode) {
            gerarSe((SeNode) no);
        } else if (no instanceof RetornarNode) {
            gerarRetornar((RetornarNode) no);
        } else if (no instanceof OpBinNode) {
            gerarExpressao(no); // Atribuições soltas: x = 5;
        }
        // Nota: EnquantoNode e ParaNode podem ser adicionados aqui usando os respetivos getters!
    }

    private void gerarDeclaracao(DeclaracaoNode decl) {
        for (ASTNode item : decl.getItens()) {
            if (item instanceof DeclaradorNode) {
                DeclaradorNode d = (DeclaradorNode) item;
                if (d.getInicializador() != null) {
                    String val = gerarExpressao(d.getInicializador());
                    emit(new AtribuirInstr(d.getId(), val));
                }
            }
        }
    }

    private void gerarSe(SeNode no) {
        String cond = gerarExpressao(no.getCondicao());
        String rotSenao = labels.newLabel();
        emit(new SeFalsoInstr(cond, rotSenao));

        gerarComposta((AfirmacaoCompostaNode) no.getBlocoSe());

        if (no.getBlocoSenao() != null) {
            String rotFim = labels.newLabel();
            emit(new VaiParaInstr(rotFim));
            emit(new RotuloInstr(rotSenao));
            gerarComposta((AfirmacaoCompostaNode) no.getBlocoSenao());
            emit(new RotuloInstr(rotFim));
        } else {
            emit(new RotuloInstr(rotSenao));
        }
    }

    private void gerarRetornar(RetornarNode no) {
        String val = no.getExpressao() != null ? gerarExpressao(no.getExpressao()) : null;
        emit(new RetornaInstr(val));
    }

    // Devolve o temporário (t1, t2) ou o ID (x, y) da expressão
    // Devolve o temporário (t1, t2) ou o ID (x, y) da expressão
    private String gerarExpressao(ASTNode no) {
        if (no == null) return null;

        if (no instanceof IDNode) {
            return ((IDNode) no).getNome();
        }
        if (no instanceof OpBinNode) {
            return gerarOpBin((OpBinNode) no);
        }

        // CORREÇÃO: Como o teu getValor() já devolve String, basta chamá-lo diretamente!
        if (no instanceof LiteralIntNode) {
            return ((LiteralIntNode) no).getValor();
        }
        if (no instanceof LiteralRealNode) {
            return ((LiteralRealNode) no).getValor();
        }

        return null;
    }

    private String gerarOpBin(OpBinNode no) {
        if (no.getOperador().equals("=")) {
            String dir = gerarExpressao(no.getDireita());
            if (no.getEsquerda() instanceof IDNode) {
                String dest = ((IDNode) no.getEsquerda()).getNome();
                emit(new AtribuirInstr(dest, dir));
                return dest;
            }
            return dir;
        }
        // Expressão Matemática/Lógica: t1 = esq + dir
        String esq = gerarExpressao(no.getEsquerda());
        String dir = gerarExpressao(no.getDireita());
        String dest = temps.newTemp();
        emit(new OpBinInstr(dest, esq, no.getOperador(), dir));
        return dest;
    }

    private void emit(Instruction instr) {
        instrucoes.add(instr);
    }

    public void imprimirInstrucoes() {
        System.out.println("---------------------------------------------------");
        for (Instruction i : instrucoes) {
            System.out.println(i.toString());
        }
        System.out.println("---------------------------------------------------");
    }
}