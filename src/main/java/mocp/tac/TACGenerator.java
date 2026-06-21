package mocp.tac;

import java.util.ArrayList;
import java.util.List;
import mocp.ast.*;

public class TACGenerator {

    private final TempCreator temps = new TempCreator();
    private final LabelCreator labels = new LabelCreator();
    private final List<Instruction> instrucoes = new ArrayList<>();

    /*Correçao: A funcao gerar() foi atualizada para processar nós do tipo DeclaracaoNode a nivel global
    O código antes ignorava inicializacoes globais. exemplo: inteiro global = 10*/
    public List<Instruction> gerar(ASTNode programa) {
        System.out.println("\n--- A INICIAR GERAÇÃO DE CÓDIGO INTERMÉDIO (TAC) ---");
        if (programa instanceof ProgramaNode) {
            for (ASTNode elem : ((ProgramaNode) programa).getFilhos()) {
                if (elem instanceof FuncaoNode) {
                    gerarFuncao((FuncaoNode) elem);
                } else if (elem instanceof DeclaracaoNode) {
                    gerarDeclaracao((DeclaracaoNode) elem);
                }
            }
        }
        imprimirInstrucoes();
        return instrucoes;
    }

    private void gerarFuncao(FuncaoNode func) {
        emit(new RotuloInstr(func.getNome()));
        gerarElemento(func.getBloco());
    }

    private void gerarComposta(AfirmacaoCompostaNode bloco) {
        if (bloco == null) return;
        for (ASTNode elem : bloco.getInstrucoes()) gerarElemento(elem);
    }

    private void gerarElemento(ASTNode no) {
        if (no == null) return;
        if (no instanceof DeclaracaoNode) gerarDeclaracao((DeclaracaoNode) no);
        else if (no instanceof AfirmacaoCompostaNode) gerarComposta((AfirmacaoCompostaNode) no);
        else if (no instanceof SeNode) gerarSe((SeNode) no);
        else if (no instanceof EnquantoNode) gerarEnquanto((EnquantoNode) no);
        else if (no instanceof RetornarNode) gerarRetornar((RetornarNode) no);
        else if (no instanceof ParaNode) gerarPara((ParaNode) no);
        else if (no instanceof OpBinNode) gerarExpressao(no);
    }

    private void gerarDeclaracao(DeclaracaoNode decl) {
        for (ASTNode item : decl.getItens()) {
            if (item instanceof DeclaradorNode) {
                DeclaradorNode d = (DeclaradorNode) item;
                if (d.getInicializador() != null) {
                    emit(new AtribuirInstr(d.getId(), gerarExpressao(d.getInicializador())));
                }
            }
        }
    }

    private void gerarSe(SeNode no) {
        String cond = gerarExpressao(no.getCondicao());
        String rotSenao = labels.newLabel();
        emit(new SeFalsoInstr(cond, rotSenao));
        gerarElemento(no.getBlocoSe());
        if (no.getBlocoSenao() != null) {
            String rotFim = labels.newLabel();
            emit(new VaiParaInstr(rotFim));
            emit(new RotuloInstr(rotSenao));
            gerarElemento(no.getBlocoSenao());
            emit(new RotuloInstr(rotFim));
        } else {
            emit(new RotuloInstr(rotSenao));
        }
    }

    private void gerarEnquanto(EnquantoNode no) {
        String rotInicio = labels.newLabel();
        String rotFim = labels.newLabel();
        emit(new RotuloInstr(rotInicio));
        emit(new SeFalsoInstr(gerarExpressao(no.getCondicao()), rotFim));
        gerarElemento(no.getCorpo());
        emit(new VaiParaInstr(rotInicio));
        emit(new RotuloInstr(rotFim));
    }

    private void gerarPara(ParaNode no) {
        if (no.getInit() != null) gerarExpressao(no.getInit());
        String rotInicio = labels.newLabel();
        String rotFim = labels.newLabel();
        emit(new RotuloInstr(rotInicio));
        if (no.getCondicao() != null) emit(new SeFalsoInstr(gerarExpressao(no.getCondicao()), rotFim));
        if (no.getCorpo() != null) gerarElemento(no.getCorpo());
        if (no.getIncremento() != null) gerarExpressao(no.getIncremento());
        emit(new VaiParaInstr(rotInicio));
        emit(new RotuloInstr(rotFim));
    }

    private void gerarRetornar(RetornarNode no) {
        emit(new RetornaInstr(no.getExpressao() != null ? gerarExpressao(no.getExpressao()) : null));
    }

    private String gerarExpressao(ASTNode no) {
        if (no == null) return null;
        if (no instanceof IDNode) return ((IDNode) no).getNome();
        if (no instanceof LiteralIntNode) return ((LiteralIntNode) no).getValor();
        if (no instanceof LiteralRealNode) return ((LiteralRealNode) no).getValor();
        /*Correção: Adicionado verificacões para LiteralStringNode e LiteralCharNode, retornando o valor diretamente*/
        if (no instanceof LiteralStringNode) return ((LiteralStringNode) no).getValor();
        if (no instanceof LiteralCharNode) return ((LiteralCharNode) no).getValor();

        /*Correção: No processamento do OpUnNode, criar variaveis temporarias e emitir instricoes explicitas
        * ex: gerar OpUnIntr(dest,op,operando) em vez de devolver a apenas a expressao interna.
        * É crucial para o Constant Folding funcionar */
        if (no instanceof OpUnNode) {
            OpUnNode unNode = (OpUnNode) no;
            String operando = gerarExpressao(unNode.getExpressao());
            String dest = temps.newTemp();
            emit(new OpUnInstr(dest, unNode.getOperador(), operando));
            return dest;
        }

        if (no instanceof ChamadaFuncaoNode) {
            ChamadaFuncaoNode call = (ChamadaFuncaoNode) no;
            for (ASTNode arg : call.getArgumentos()) emit(new ParamInstr(gerarExpressao(arg)));
            String temp = temps.newTemp();
            emit(new ChamarInstr(temp, call.getNome(), call.getArgumentos().size()));
            return temp;
        }
        if (no instanceof AcessoVetorNode) {
            AcessoVetorNode vet = (AcessoVetorNode) no;
            String temp = temps.newTemp();
            emit(new VetorCarregarInstr(temp, vet.getId(), gerarExpressao(vet.getIndice())));
            return temp;
        }
        if (no instanceof OpBinNode) return gerarOpBin((OpBinNode) no);
        return null;
    }

    private String gerarOpBin(OpBinNode no) {
        if (no.getOperador().equals("=")) {
            String dir = gerarExpressao(no.getDireita());
            if (no.getEsquerda() instanceof IDNode) {
                emit(new AtribuirInstr(((IDNode) no.getEsquerda()).getNome(), dir));
                return ((IDNode) no.getEsquerda()).getNome();
            } else if (no.getEsquerda() instanceof AcessoVetorNode) {
                AcessoVetorNode vet = (AcessoVetorNode) no.getEsquerda();
                emit(new VetorGuardarInstr(vet.getId(), gerarExpressao(vet.getIndice()), dir));
                return dir;
            }
        }
        String dest = temps.newTemp();
        emit(new OpBinInstr(dest, gerarExpressao(no.getEsquerda()), no.getOperador(), gerarExpressao(no.getDireita())));
        return dest;
    }

    private void emit(Instruction instr) { instrucoes.add(instr); }
    public void imprimirInstrucoes() {
        System.out.println("---------------------------------------------------");
        for (Instruction i : instrucoes) System.out.println(i.toString());
        System.out.println("---------------------------------------------------");
    }
}