package mocp.ast;

import java.util.ArrayList;
import java.util.List;

public class DeclaracaoNode extends ASTNode {
    private final String tipo;
    private final List<ASTNode> itens = new ArrayList<>();

    // Construtor que recebe o tipo básico (inteiro, real, vazio)
    public DeclaracaoNode(String tipo) {
        this.tipo = tipo;
    }

    // Método para adicionar cada variável individual (DeclaradorNode)
    public void addItem(ASTNode item) {
        if (item != null) {
            itens.add(item);
        }
    }

    // --- GETTERS ADICIONADOS PARA O SEMÂNTICO ---
    public String getTipo() {
        return tipo;
    }

    public List<ASTNode> getItens() {
        return itens;
    }
    // --------------------------------------------

    @Override
    public void print(String indent) {
        System.out.println(indent + "Declaracao Variavel (Tipo: " + tipo + ")");
        for (ASTNode item : itens) {
            item.print(indent + "  ");
        }
    }
}