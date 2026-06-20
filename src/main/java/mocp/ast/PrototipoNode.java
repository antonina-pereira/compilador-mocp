package mocp.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * Nó que representa a declaração de protótipo de uma função.
 * Ex.: "inteiro fact(inteiro);"
 */
public class PrototipoNode extends ASTNode {
    private final String tipo;
    private final String nome;
    private final List<String> tiposParametros = new ArrayList<>();

    public PrototipoNode(String tipo, String nome) {
        this.tipo = tipo;
        this.nome = nome;
    }

    public void addTipoParametro(String tipoParam) {
        this.tiposParametros.add(tipoParam);
    }

    // getters para a semantica
    public String getTipo() { return tipo; }
    public String getNome() { return nome; }
    public List<String> getTiposParametros() { return tiposParametros; }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Prototipo Funcao: " + nome + " (Retorno: " + tipo + ", Params: " + tiposParametros + ")");
    }
}

