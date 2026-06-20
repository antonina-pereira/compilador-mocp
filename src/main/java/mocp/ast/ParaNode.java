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

    public void setComponentes(ASTNode init, ASTNode cond, ASTNode inc, ASTNode bloco) {
        this.inicializacao = init;
        this.condicao = cond;
        this.incremento = inc;
        this.bloco = bloco;
    }

    // --- MÉTODOS PÚBLICOS PARA O GERADOR TAC ---

    public ASTNode getInit() {
        return this.inicializacao;
    }

    public ASTNode getCondicao() {
        return this.condicao;
    }

    public ASTNode getIncremento() {
        return this.incremento;
    }

    public ASTNode getCorpo() {
        return this.bloco;
    }

    public ParaNode(ASTNode init, ASTNode cond, ASTNode inc, ASTNode bloco) {
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

