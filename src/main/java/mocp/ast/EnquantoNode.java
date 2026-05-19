package mocp.ast;

/**
 * Nó que representa um ciclo enquanto.
 * Ex.: "enquanto (x > 0) { ... }"
 */
public class EnquantoNode extends AfirmacaoNode {

    public final ASTNode condicao;
    public final AfirmacaoCompostaNode corpo;

    public EnquantoNode(ASTNode condicao, AfirmacaoCompostaNode corpo) {
        this.condicao = condicao;
        this.corpo = corpo;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Enquanto");
        System.out.println(indent + "  Condicao:");
        if (condicao != null) condicao.print(indent + "    ");
        System.out.println(indent + "  Corpo:");
        if (corpo != null) corpo.print(indent + "    ");
    }
}
