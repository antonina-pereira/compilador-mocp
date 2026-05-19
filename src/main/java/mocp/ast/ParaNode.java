package mocp.ast;

/**
 * Nó que representa um ciclo para.
 * Ex.: "para (i = 0; i < n; i = i + 1) { ... }"
 * Qualquer das três partes pode ser null (omitida).
 */
public class ParaNode extends AfirmacaoNode {

    public final ASTNode init;      // null se omitido
    public final ASTNode condicao;  // null se omitido
    public final ASTNode incremento;// null se omitido
    public final AfirmacaoCompostaNode corpo;

    public ParaNode(ASTNode init, ASTNode condicao, ASTNode incremento, AfirmacaoCompostaNode corpo) {
        this.init = init;
        this.condicao = condicao;
        this.incremento = incremento;
        this.corpo = corpo;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Para");
        if (init != null)       { System.out.println(indent + "  Init:"); init.print(indent + "    "); }
        if (condicao != null)   { System.out.println(indent + "  Condicao:"); condicao.print(indent + "    "); }
        if (incremento != null) { System.out.println(indent + "  Incremento:"); incremento.print(indent + "    "); }
        System.out.println(indent + "  Corpo:");
        if (corpo != null) corpo.print(indent + "    ");
    }
}
