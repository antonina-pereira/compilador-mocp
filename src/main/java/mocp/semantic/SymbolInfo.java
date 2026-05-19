package mocp.semantic;

import java.util.List;
import java.util.ArrayList;

public class SymbolInfo {

    public enum Categoria {
        VARIAVEL,
        FUNCAO,
        PARAMETRO,
        VETOR
    }

    private String nome;  // Identificador como x, y, arr
    private String tipo;  // Tipo semântico como inteiro, real, vazio
    private Categoria categoria; // Para distinguir variável de função, etc.

    // Para vetores
    private List<Integer> dimensoes = new ArrayList<>();

    // Para funções
    private List<String> tiposParametros = new ArrayList<>();

    public SymbolInfo(String nome, String tipo, Categoria categoria) {
        this.nome = nome;
        this.tipo = tipo;
        this.categoria = categoria;
    }

    // Getters
    public String getNome() { return nome; }
    public String getTipo() { return tipo; }
    public Categoria getCategoria() { return categoria; }
    public List<Integer> getDimensoes() { return dimensoes; }
    public List<String> getTiposParametros() { return tiposParametros; }

    // Para vetores
    public void adicionarDimensao(int tamanho) {
        dimensoes.add(tamanho);
    }

    // Para funções
    public void adicionarParametro(String tipo) {
        tiposParametros.add(tipo);
    }

    @Override
    public String toString() {
        return "SymbolInfo{" +
                "nome='" + nome + '\'' +
                ", tipo='" + tipo + '\'' +
                ", categoria=" + categoria +
                ", dimensoes=" + dimensoes +
                ", tiposParametros=" + tiposParametros +
                '}';
    }
}
