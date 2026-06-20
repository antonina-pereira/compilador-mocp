package mocp.semantic;

import java.util.ArrayList;
import java.util.List;

public class SymbolInfo {
    private String nome;
    private String tipo;        // "inteiro", "real", ou "vazio"
    private Categoria categoria;

    // Se for FUNCAO ou PROTOTIPO, guardamos a lista dos tipos dos parâmetros
    // Ex: para a função fact(inteiro k), esta lista terá ["inteiro"]
    private List<String> tiposParametros;

    public SymbolInfo(String nome, String tipo, Categoria categoria) {
        this.nome = nome;
        this.tipo = tipo;
        this.categoria = categoria;
        this.tiposParametros = new ArrayList<>();
    }

    public String getNome() { return nome; }
    public String getTipo() { return tipo; }
    public Categoria getCategoria() { return categoria; }

    public List<String> getTiposParametros() { return tiposParametros; }

    public void addTipoParametro(String tipoParam) {
        this.tiposParametros.add(tipoParam);
    }

    // Método utilitário para facilitar a impressão da tabela (Debug)
    @Override
    public String toString() {
        if (categoria == Categoria.FUNCAO || categoria == Categoria.PROTOTIPO) {
            return categoria + " " + nome + "(" + String.join(", ", tiposParametros) + ") -> " + tipo;
        }
        return categoria + " " + tipo + " " + nome;
    }
}
