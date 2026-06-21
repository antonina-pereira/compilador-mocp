package mocp.semantic;

import java.util.ArrayList;
import java.util.List;

public class SymbolInfo {
    private String nome;
    private String tipo;        // "inteiro", "real", ou "vazio"
    private Categoria categoria;
    private boolean isVetor;    // true se for um vetor

    private List<String> tiposParametros;

    // Construtor principal (com isVetor)
    public SymbolInfo(String nome, String tipo, Categoria categoria, boolean isVetor) {
        this.nome = nome;
        this.tipo = tipo;
        this.categoria = categoria;
        this.isVetor = isVetor;
        this.tiposParametros = new ArrayList<>();
    }

    // Construtor para compatibilidade (assume não vetor)
    public SymbolInfo(String nome, String tipo, Categoria categoria) {
        this(nome, tipo, categoria, false);
    }

    public String getNome() { return nome; }
    public String getTipo() { return tipo; }
    public Categoria getCategoria() { return categoria; }
    public boolean isVetor() { return isVetor; }
    public void setVetor(boolean v) { isVetor = v; }

    public List<String> getTiposParametros() { return tiposParametros; }

    public void addTipoParametro(String tipoParam) {
        this.tiposParametros.add(tipoParam);
    }

    @Override
    public String toString() {
        String tipoCompleto = isVetor ? tipo + "[]" : tipo;
        if (categoria == Categoria.FUNCAO || categoria == Categoria.PROTOTIPO) {
            return categoria + " " + nome + "(" + String.join(", ", tiposParametros) + ") -> " + tipo;
        }
        return categoria + " " + tipoCompleto + " " + nome;
    }
}
