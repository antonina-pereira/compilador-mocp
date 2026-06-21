package mocp.semantic;

import java.util.Stack;

public class SymbolTable {
    private Stack<Scope> pilhaEscopos;

    public SymbolTable() {
        this.pilhaEscopos = new Stack<>();
        // Assim que a tabela é criada, abrimos automaticamente o Escopo Global
        enterScope();
    }

    // Abre um novo escopo (ex: ao entrar numa função ou bloco)
    public void enterScope() {
        pilhaEscopos.push(new Scope());
    }

    // Fecha o escopo atual (ex: ao sair de uma função, as variáveis locais morrem)
    public void exitScope() {
        if (!pilhaEscopos.isEmpty()) {
            pilhaEscopos.pop();
        }
    }

    // Tenta inserir um símbolo. Retorna 'false' se já existir uma variável com o mesmo nome neste escopo
    public boolean inserir(SymbolInfo simbolo) {
        Scope escopoAtual = pilhaEscopos.peek();
        if (escopoAtual.existeLocal(simbolo.getNome())) {
            return false; // Erro: Variável duplicada no mesmo escopo
        }
        escopoAtual.inserir(simbolo);
        return true;
    }

    // Procura um símbolo de cima para baixo (do escopo mais local até ao global)
    public SymbolInfo procurar(String nome) {
        for (int i = pilhaEscopos.size() - 1; i >= 0; i--) {
            SymbolInfo sim = pilhaEscopos.get(i).procurarLocal(nome);
            if (sim != null) {
                return sim; // Encontrou!
            }
        }
        return null; // Não encontrou em lado nenhum (Variável não declarada!)
    }

    // Imprime a tabela toda para ajudar no Debug
    public void printTable() {
        System.out.println("\n--- TABELA DE SIMBOLOS ---");
        for (int i = 0; i < pilhaEscopos.size(); i++) {
            System.out.println("Escopo Nivel " + i + (i == 0 ? " (Global):" : " (Local):"));
            pilhaEscopos.get(i).printScope("  ");
        }
        System.out.println("--------------------------\n");
    }
}
