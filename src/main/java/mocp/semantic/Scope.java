package mocp.semantic;

import java.util.LinkedHashMap;
import java.util.Map;

public class Scope {
    // Usamos LinkedHashMap para manter a ordem em que as variáveis foram declaradas
    private Map<String, SymbolInfo> simbolos;

    public Scope() {
        this.simbolos = new LinkedHashMap<>();
    }

    // Insere um símbolo apenas neste escopo
    public void inserir(SymbolInfo simbolo) {
        simbolos.put(simbolo.getNome(), simbolo);
    }

    // Procura um símbolo apenas neste escopo
    public SymbolInfo procurarLocal(String nome) {
        return simbolos.get(nome);
    }

    // Verifica se já existe algo com este nome NESTE escopo específico
    public boolean existeLocal(String nome) {
        return simbolos.containsKey(nome);
    }

    // Método utilitário para imprimir o conteúdo do escopo (Debug)
    public void printScope(String indent) {
        for (SymbolInfo info : simbolos.values()) {
            System.out.println(indent + info.toString());
        }
    }
}