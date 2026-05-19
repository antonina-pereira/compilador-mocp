package mocp.semantic;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {

    private Map<String, SymbolInfo> simbolos = new HashMap<>();

    // Inserir símbolo no escopo atual
    public boolean inserir(SymbolInfo simbolo) {
        String nome = simbolo.getNome();

        // Se já existe, erro semântico e devolve falso
        if (simbolos.containsKey(nome)) {
            return false;
        }

        simbolos.put(nome, simbolo);
        return true;
    }

    // Verificar se existe no escopo atual
    public boolean existe(String nome) {
        return simbolos.containsKey(nome);
    }

    // Obter símbolo ou null se não existir
    public SymbolInfo obter(String nome) {
        return simbolos.get(nome);
    }

    @Override
    public String toString() {
        return simbolos.toString();
    }
}
