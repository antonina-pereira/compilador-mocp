package mocp.ast;

/**
 * Nó que representa um ciclo para.
 * Ex.: "para (i = 0; i < n; i = i + 1) { ... }"
 * Qualquer das três partes pode ser null (omitida).
 */
public class ParaNode extends ASTNode {
    private ASTNode inicializacao;
    private ASTNode condicao;
    private ASTNode incremento;
    private ASTNode bloco;

    public ParaNode(ASTNode init, ASTNode cond, ASTNode inc, ASTNode bloco) {
        this.inicializacao = init;
        this.condicao = cond;
        this.incremento = inc;
        this.bloco = bloco;
    }

    public ASTNode getInit() { return inicializacao; }
    public ASTNode getCondicao() { return condicao; }
    public ASTNode getIncremento() { return incremento; }
    public ASTNode getCorpo() { return bloco; }

    public ParaNode(ASTNode init, ASTNode cond, ASTNode inc, ASTNode bloco) {
        this.inicializacao = init;
        this.condicao = cond;
        this.incremento = inc;
        this.bloco = bloco;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Loop PARA");
        if (inicializacao != null) inicializacao.print(indent + "  Init:");
        if (condicao != null) condicao.print(indent + "  Cond:");
        if (incremento != null) incremento.print(indent + "  Inc:");
        if (bloco != null) bloco.print(indent + "  Bloco:");
    }
}

