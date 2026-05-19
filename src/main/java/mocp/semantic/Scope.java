// Gere uma pilha de tabelas de símbolos permitindo escopos aninhados
// Por exemplo: global -> função -> bloco

package mocp.semantic;

import java.util.Stack;

public class Scope {

    private Stack<SymbolTable> pilha;

    public Scope() {
        pilha = new Stack<>();
        // Escopo global
        pilha.push(new SymbolTable());
    }

    // Entrar num novo escopo (ex: função, bloco)
    public void entrarEscopo() {
        pilha.push(new SymbolTable());
    }

    // Sair do escopo atual
    public void sairEscopo() {
        if (pilha.size() > 1) {
            pilha.pop();
        }
    }

    // Inserir símbolo no escopo atual
    public boolean adicionar(SymbolInfo simbolo) {
        return pilha.peek().inserir(simbolo);
    }

    // Procurar símbolo em todos os escopos (de cima para baixo)
    public SymbolInfo procurar(String nome) {
        for (int i = pilha.size() - 1; i >= 0; i--) {
            SymbolTable tabela = pilha.get(i);
            if (tabela.existe(nome)) {
                return tabela.obter(nome);
            }
        }
        return null;
    }

    // Verificar se existe no escopo atual
    public boolean existeNoEscopoAtual(String nome) {
        return pilha.peek().existe(nome);
    }

    @Override
    public String toString() {
        return pilha.toString();
    }
}
