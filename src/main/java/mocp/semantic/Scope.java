// Gere uma pilha de tabelas de símbolos permitindo escopos aninhados
// Por exemplo: global -> função -> bloco

package mocp.semantic;

import java.util.Stack;

public class Scope {

    private Stack<SymbolTable> stack;

    public Scope() {
        stack = new Stack<>();
        // Escopo global
        stack.push(new SymbolTable());
    }

    // Entrar num novo escopo (ex: função, bloco)
    public void enterScope() {
        stack.push(new SymbolTable());
    }

    // Sair do escopo atual
    public void exitScope() {
        if (stack.size() > 1) {
            stack.pop();
        }
    }

    // Inserir símbolo no escopo atual
    public boolean add(SymbolInfo symbol) {
        return stack.peek().insert(symbol);
    }

    // Procurar símbolo em todos os escopos (de cima para baixo)
    public SymbolInfo search(String name) {
        for (int i = stack.size() - 1; i >= 0; i--) {
            SymbolTable tabel = stack.get(i);
            if (tabel.exists(name)) {
                return tabel.get(name);
            }
        }
        return null;
    }

    // Verificar se existe no escopo atual
    public boolean existsInCurrentScope(String name) {
        return stack.peek().exists(name);
    }

    @Override
    public String toString() {
        return stack.toString();
    }
}
