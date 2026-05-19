package mocp.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * Nó que representa uma chamada de função.
 * Ex.: "fact(n)", "escrever(x)", "ler()"
 * O tipo de retorno é anotado pelo analisador semântico.
 */
public class ChamadaFuncaoNode extends ASTNode {

    public final String nome;
    public final List<ASTNode> argumentos = new ArrayList<>();
    public Tipo tipo = Tipo.DESCONHECIDO;

    public ChamadaFuncaoNode(String nome) {
        this.nome = nome;
    }

    public void adicionarArgumento(ASTNode arg) {
        if (arg != null) argumentos.add(arg);
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "ChamadaFuncao: " + nome);
        for (ASTNode a : argumentos) a.print(indent + "  ");
    }
}
