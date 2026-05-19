package mocp.tac;

import java.util.ArrayList;
import java.util.List;

import mocp.ast.*;

/**
 * Gerador de Código de Três Endereços (TAC).
 *
 * Percorre a AST e produz uma lista plana de instruções TAC.
 * Cada expressão retorna o nome do temporário ou variável que guarda o resultado.
 * Funções integradas (ler, escrever, etc.) são tratadas como chamadas normais.
 */
public class TACGenerator {

    private final TempCreator temps = new TempCreator();
    private final LabelCreator labels = new LabelCreator();
    private final List<Instruction> instrucoes = new ArrayList<>();

    // -------------------------------------------------------------------------
    // API pública
    // -------------------------------------------------------------------------

    public List<Instruction> gerar(ProgramaNode programa) {
        for (ASTNode elem : programa.elementos) {
            if (elem instanceof FuncaoNode) {
                gerarFuncao((FuncaoNode) elem);
            }
            // Protótipos e declarações globais não geram código TAC
        }
        return instrucoes;
    }

    // -------------------------------------------------------------------------
    // Funções
    // -------------------------------------------------------------------------

    private void gerarFuncao(FuncaoNode func) {
        emit(new RotuloInstr(func.nome));
        gerarComposta(func.corpo);
    }

    // -------------------------------------------------------------------------
    // Afirmações
    // -------------------------------------------------------------------------

    private void gerarComposta(AfirmacaoCompostaNode bloco) {
        if (bloco == null) return;
        for (ASTNode elem : bloco.corpo) {
            gerarElemento(elem);
        }
    }

    private void gerarElemento(ASTNode no) {
        if (no instanceof DeclaracaoNode) {
            gerarDeclaracao((DeclaracaoNode) no);
        } else if (no instanceof AfirmacaoExpressaoNode) {
            gerarExpressao(((AfirmacaoExpressaoNode) no).expressao);
        } else if (no instanceof AfirmacaoCompostaNode) {
            gerarComposta((AfirmacaoCompostaNode) no);
        } else if (no instanceof SeNode) {
            gerarSe((SeNode) no);
        } else if (no instanceof EnquantoNode) {
            gerarEnquanto((EnquantoNode) no);
        } else if (no instanceof ParaNode) {
            gerarPara((ParaNode) no);
        } else if (no instanceof RetornarNode) {
            gerarRetornar((RetornarNode) no);
        }
    }

    private void gerarDeclaracao(DeclaracaoNode decl) {
        for (DeclaradorNode d : decl.declaradores) {
            if (d.init == null) continue;
            if (!d.init.isLista) {
                // inicializador simples: dest = expressao
                String val = gerarExpressao(d.init.elementos.get(0));
                emit(new AtribuirInstr(d.nome, val));
            } else {
                // lista: base[0] = e0, base[1] = e1, ...
                int i = 0;
                for (ASTNode elem : d.init.elementos) {
                    String val = gerarExpressao(elem);
                    emit(new VetorGuardarInstr(d.nome, String.valueOf(i++), val));
                }
            }
        }
    }

    private void gerarSe(SeNode no) {
        String cond = gerarExpressao(no.condicao);
        String rotSenao = labels.newLabel();
        emit(new SeFalsoInstr(cond, rotSenao));
        gerarComposta(no.entao);
        if (no.senao != null) {
            String rotFim = labels.newLabel();
            emit(new VaiParaInstr(rotFim));
            emit(new RotuloInstr(rotSenao));
            gerarComposta(no.senao);
            emit(new RotuloInstr(rotFim));
        } else {
            emit(new RotuloInstr(rotSenao));
        }
    }

    private void gerarEnquanto(EnquantoNode no) {
        String rotInicio = labels.newLabel();
        String rotFim = labels.newLabel();
        emit(new RotuloInstr(rotInicio));
        String cond = gerarExpressao(no.condicao);
        emit(new SeFalsoInstr(cond, rotFim));
        gerarComposta(no.corpo);
        emit(new VaiParaInstr(rotInicio));
        emit(new RotuloInstr(rotFim));
    }

    private void gerarPara(ParaNode no) {
        if (no.init != null) gerarExpressao(no.init);
        String rotInicio = labels.newLabel();
        String rotFim = labels.newLabel();
        emit(new RotuloInstr(rotInicio));
        if (no.condicao != null) {
            String cond = gerarExpressao(no.condicao);
            emit(new SeFalsoInstr(cond, rotFim));
        }
        gerarComposta(no.corpo);
        if (no.incremento != null) gerarExpressao(no.incremento);
        emit(new VaiParaInstr(rotInicio));
        emit(new RotuloInstr(rotFim));
    }

    private void gerarRetornar(RetornarNode no) {
        String val = no.expressao != null ? gerarExpressao(no.expressao) : null;
        emit(new RetornaInstr(val));
    }

    // -------------------------------------------------------------------------
    // Expressões — devolve o nome do lugar que guarda o resultado
    // -------------------------------------------------------------------------

    private String gerarExpressao(ASTNode no) {
        if (no == null) return null;

        if (no instanceof LiteralIntNode) {
            return String.valueOf(((LiteralIntNode) no).valor);
        }
        if (no instanceof LiteralRealNode) {
            return String.valueOf(((LiteralRealNode) no).valor);
        }
        if (no instanceof LiteralStringNode) {
            return "\"" + ((LiteralStringNode) no).valor + "\"";
        }
        if (no instanceof IDNode) {
            return ((IDNode) no).nome;
        }
        if (no instanceof OpBinNode) {
            return gerarOpBin((OpBinNode) no);
        }
        if (no instanceof OpUnNode) {
            return gerarOpUn((OpUnNode) no);
        }
        if (no instanceof ChamadaFuncaoNode) {
            return gerarChamada((ChamadaFuncaoNode) no);
        }
        if (no instanceof AcessoVetorNode) {
            return gerarAcessoVetor((AcessoVetorNode) no);
        }
        return null;
    }

    private String gerarOpBin(OpBinNode no) {
        if (no.op.equals("=")) {
            String dir = gerarExpressao(no.dir);
            if (no.esq instanceof IDNode) {
                String dest = ((IDNode) no.esq).nome;
                emit(new AtribuirInstr(dest, dir));
                return dest;
            }
            if (no.esq instanceof AcessoVetorNode) {
                AcessoVetorNode av = (AcessoVetorNode) no.esq;
                String base = gerarExpressao(av.base);
                String idx = gerarExpressao(av.indice);
                emit(new VetorGuardarInstr(base, idx, dir));
                return dir;
            }
            return dir;
        }
        String esq = gerarExpressao(no.esq);
        String dir = gerarExpressao(no.dir);
        String dest = temps.newTemp();
        emit(new OpBinInstr(dest, esq, no.op, dir));
        return dest;
    }

    private String gerarOpUn(OpUnNode no) {
        String operando = gerarExpressao(no.operando);
        String dest = temps.newTemp();
        emit(new OpUnInstr(dest, no.op, operando));
        return dest;
    }

    private String gerarChamada(ChamadaFuncaoNode no) {
        // Avaliar argumentos e emitir instruções param
        List<String> args = new ArrayList<>();
        for (ASTNode arg : no.argumentos) {
            args.add(gerarExpressao(arg));
        }
        for (String a : args) {
            emit(new ParamInstr(a));
        }
        String dest = temps.newTemp();
        emit(new ChamarInstr(dest, no.nome, no.argumentos.size()));
        return dest;
    }

    private String gerarAcessoVetor(AcessoVetorNode no) {
        String base = gerarExpressao(no.base);
        String idx = gerarExpressao(no.indice);
        String dest = temps.newTemp();
        emit(new VetorCarregarInstr(dest, base, idx));
        return dest;
    }

    private void emit(Instruction instr) {
        instrucoes.add(instr);
    }
}
