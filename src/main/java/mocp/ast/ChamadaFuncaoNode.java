package mocp.ast;

import java.util.ArrayList;
import java.util.List;

public class ChamadaFuncaoNode extends ASTNode {
    private final String nome;
    private final List<ASTNode> argumentos = new ArrayList<>();

    public ChamadaFuncaoNode(String nome) {
        this.nome = nome;
    }

    public void addArgumento(ASTNode arg) {
        if (arg != null) argumentos.add(arg);
    }

    // MÉTODOS ADICIONADOS PARA O TACGenerator:

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
            arg.print(indent + "  [Arg]: ");
        }
    }
}