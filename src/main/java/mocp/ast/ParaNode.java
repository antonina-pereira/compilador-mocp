package mocp.ast;

public class ParaNode extends ASTNode {
    private ASTNode inicializacao;
    private ASTNode condicao;
    private ASTNode incremento;
    private ASTNode bloco;

    public void setComponentes(ASTNode init, ASTNode cond, ASTNode inc, ASTNode bloco) {
        this.inicializacao = init;
        this.condicao = cond;
        this.incremento = inc;
        this.bloco = bloco;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Loop PARA");
        if (inicializacao != null) { System.out.print(indent + "  Init: "); inicializacao.print(""); }
        if (condicao != null)      { System.out.print(indent + "  Cond: "); condicao.print(""); }
        if (incremento != null)    { System.out.print(indent + "  Inc:  "); incremento.print(""); }
        if (bloco != null)         { bloco.print(indent + "  "); }
    }
}