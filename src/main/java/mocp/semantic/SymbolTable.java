package mocp.semantic;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {

    private Map<String, SymbolInfo> symbols = new HashMap<>();

    // Inserir símbolo no escopo atual
    public boolean insert(SymbolInfo symbol) {
        String name = symbol.getName();

        // Se já existe, erro semântico e devolve falso
        if (symbols.containsKey(name)) {
            return false;
        }

        symbols.put(name, symbol);
        return true;
    }

    // Verificar se existe no escopo atual
    public boolean exists(String name) {
        return symbols.containsKey(name);
    }

    // Obter símbolo ou null se não existir
    public SymbolInfo get(String name) {
        return symbols.get(name);
    }

    @Override
    public String toString() {
        return symbols.toString();
    }
}
