package mocp.codegen;

import mocp.ast.*;

// Gerar código JAVA
public class CodeGenerator {
  private StringBuilder sb;

  public String gerar(ASTNode ast) {
    sb = new StringBuilder();

    sb.append("public class Programa {\n\n");

    // gerar conteúdo
    gerarNo(ast);

    sb.append("\n}\n");

    return sb.toString();
  }

  private void gerarNo(ASTNode node) {
    if (node instanceof ProgramaNode) {
      for (ASTNode child : ((ProgramaNode) node).getFilhos()) {
        gerarNo(child);
      }
    }

    else if (node instanceof FuncaoNode) {
      gerarFuncao((FuncaoNode) node);
    }
  }

  private void gerarFuncao(FuncaoNode func) {
    sb.append("public static ")
      .append(mapTipo(func.getTipo()))
      .append(" ")
      .append(func.getNome())
      .append("() {\n");

    sb.append("}\n\n");
  }

  // Helper functions
  // Mapeia os tipos em MOCP para java
  private String mapTipo(String tipo) {
    switch(tipo) {
      case "inteiro": return "int";
      case "real": return "double";
      case "vazio": return "void";
    }
    return "int";
  }
}
