package mocp.semantic;

import java.util.List;
import java.util.ArrayList;

public class SymbolInfo {

    public enum Category {
        VARIAVEL,
        FUNCAO,
        PARAMETRO,
        VETOR
    }

    private String name;  // Identificador como x, y, arr
    private String type;  // Tipo semântico como inteiro, real, vazio
    private Categoria category; // Para distinguir variável de função, etc.

    // Para vetores
    private List<Integer> dimensions = new ArrayList<>();

    // Para funções
    private List<String> parametersTypes = new ArrayList<>();

    public SymbolInfo(String name, String type, Categoria category) {
        this.name = nome;
        this.type = type;
        this.category = category;
    }

    // Getters
    public String getName() { return name; }
    public String getType() { return type; }
    public Categoria getCategory() { return category; }
    public List<Integer> getDimensions() { return dimensions; }
    public List<String> getParametersTypes() { return parametersTypes; }

    // Para vetores
    public void addDimension(int size) {
        dimensions.add(size);
    }

    // Para funções
    public void addParameters(String type) {
        parametersTypes.add(type);
    }

    @Override
    public String toString() {
        return "SymbolInfo{" +
                "nome='" + name + '\'' +
                ", tipo='" + type + '\'' +
                ", categoria=" + category +
                ", dimensoes=" + dimensions +
                ", tiposParametros=" + parametersTypes +
                '}';
    }
}
