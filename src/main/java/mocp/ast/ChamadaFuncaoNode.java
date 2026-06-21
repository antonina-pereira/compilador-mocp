package mocp.ast;
import mocp.semantic.SymbolTable;
import mocp.semantic.SymbolInfo;
import mocp.semantic.Categoria;

import java.util.ArrayList;
import java.util.List;

/**
 * Nó que representa uma chamada de função.
 * Ex.: "fact(n)", "escrever(x)", "ler()"
 * O tipo de retorno é anotado pelo analisador semântico.
 */
public class ChamadaFuncaoNode extends ASTNode {
    private final String nome;
    private final List<ASTNode> argumentos = new ArrayList<>();

    public ChamadaFuncaoNode(String nome) {
        this.nome = nome;
    }

    public void addArgumento(ASTNode arg) {
        if (arg != null) argumentos.add(arg);
    }

    public String getNome() {
        return nome;
    }

    public List<ASTNode> getArgumentos() {
        return argumentos;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Chamada Funcao: " + nome);
        for (ASTNode arg : argumentos) {
            if (arg != null) arg.print(indent + "  [Arg]: ");
        }
    }
}
