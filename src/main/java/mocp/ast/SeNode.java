package mocp.ast;

/**
 * Nó que representa uma afirmação condicional se/senao.
 * O ramo "senao" é opcional (pode ser null).
 */
public class SeNode extends AfirmacaoNode {

    public final ASTNode condicao;
    public final AfirmacaoCompostaNode entao;
    public final AfirmacaoCompostaNode senao; // null se não houver senao

    public SeNode(ASTNode condicao, AfirmacaoCompostaNode entao, AfirmacaoCompostaNode senao) {
        this.condicao = condicao;
        this.entao = entao;
        this.senao = senao;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Se");
        System.out.println(indent + "  Condicao:");
        if (condicao != null) condicao.print(indent + "    ");
        System.out.println(indent + "  Entao:");
        if (entao != null) entao.print(indent + "    ");
        if (senao != null) {
            System.out.println(indent + "  Senao:");
            senao.print(indent + "    ");
        }
    }
}
