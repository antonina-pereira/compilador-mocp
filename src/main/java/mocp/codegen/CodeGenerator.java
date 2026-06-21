package mocp.codegen;

import mocp.ast.*;

// Gerar código JAVA
public class CodeGenerator {
  private StringBuilder sb;
  private int indent = 0;

  // Indentação
  private void tab() {
    for (int i = 0; i < indent; i++) sb.append("  ");
  }

  public String gerar(ASTNode ast) {
    sb = new StringBuilder();

    sb.append("class Main {\n\n");
    indent++;

    // Gerar conteúdo
    gerarNo(ast);

    indent--;

    sb.append("}\n");

    return sb.toString();
  }

  private void gerarNo(ASTNode node) {
    if (node instanceof ProgramaNode) {
      for (ASTNode child : ((ProgramaNode) node).getFilhos()) {
        gerarNo(child);
      }
    }

    else if (node instanceof FuncaoNode) {
      FuncaoNode f = (FuncaoNode) node;
      if (f.getNome().equals("principal")) {
        gerarMain(f);
      } else {
        gerarFuncao(f);
      }
    }
  }

  // GERAR MAIN
  private void gerarMain(FuncaoNode f) {
    tab();

    sb.append("public static void main(String[] args) {\n");

    indent++;

    gerarBloco(f.getBloco());

    indent--;

    tab();

    sb.append("}\n\n");
  }
  
  
  // GERAR FUNÇÃO
  private void gerarFuncao(FuncaoNode f) {
    tab();

    sb.append("public static ")
      .append(mapTipo(f.getTipo()))
      .append(" ")
      .append(f.getNome())
      .append("(");

    gerarParametros(f);

    sb.append(") {\n");

    indent++;

    gerarBloco(f.getBloco());

    indent--;

    tab();

    sb.append("}\n\n");
  }

  // GERAR BLOCO
  private void gerarBloco(ASTNode bloco) {

    if (bloco == null) return;

    if (bloco instanceof AfirmacaoCompostaNode) {
        for (ASTNode instr :
             ((AfirmacaoCompostaNode) bloco).getInstrucoes()) {
            gerarInstrucao(instr);
        }
    }
  }

  // GERAR PARÂMETROS
  private void gerarParametros(FuncaoNode f) {

    if (f.getParametros() == null) return;

    AfirmacaoCompostaNode params =
        (AfirmacaoCompostaNode) f.getParametros();

    boolean first = true;

    for (ASTNode p : params.getInstrucoes()) {

        if (p instanceof ParametroNode) {

            ParametroNode param = (ParametroNode) p;

            if (!first) sb.append(", ");

            sb.append(mapTipo(param.getTipo()))
              .append(" ")
              .append(param.getId());

            first = false;
        }
    }
  }

  // GERAR INSTRUÇÃO
  private void gerarInstrucao(ASTNode node) {

    if (node instanceof DeclaracaoNode) {
        gerarDeclaracao((DeclaracaoNode) node);
    }

    else if (node instanceof AfirmacaoExpressaoNode) {
        tab();
        gerarExpressao(((AfirmacaoExpressaoNode) node).getExpressao());
        sb.append(";\n");
    }

    else if (node instanceof RetornarNode) {
        gerarReturn((RetornarNode) node);
    }

    else if (node instanceof SeNode) {
        gerarIf((SeNode) node);
    }

    else if (node instanceof EnquantoNode) {
        gerarWhile((EnquantoNode) node);
    }

    else if (node instanceof ParaNode) {
        gerarFor((ParaNode) node);
    }
  }

  // GERAR DECLARAÇÃO
  private void gerarDeclaracao(DeclaracaoNode dec) {

    for (ASTNode item : dec.getItens()) {

        if (item instanceof DeclaradorNode) {

            DeclaradorNode d = (DeclaradorNode) item;

            tab();

            sb.append(mapTipo(dec.getTipo()))
              .append(" ")
              .append(d.getId());

            if (d.getInicializador() != null) {
                sb.append(" = ");
                gerarExpressao(d.getInicializador());
            }

            sb.append(";\n");
        }
    }
  }

  // GERAR RETURN
  private void gerarReturn(RetornarNode ret) {

    tab();

    sb.append("return");

    if (ret.getExpressao() != null) {
        sb.append(" ");
        gerarExpressao(ret.getExpressao());
    }

    sb.append(";\n");
  }

  // GERAR IF
  private void gerarIf(SeNode node) {

    tab();

    sb.append("if (");

    gerarExpressao(node.getCondicao());

    sb.append(") {\n");

    indent++;
    gerarBloco(node.getBlocoSe());
    indent--;

    tab();

    sb.append("}");

    if (node.getBlocoSenao() != null) {
        sb.append(" else {\n");
        gerarBloco(node.getBlocoSenao());
        indent--;
        tab();
        sb.append("}");
    }

    sb.append("\n");
  }

  // GERAR WHILE LOOP
  private void gerarWhile(EnquantoNode node) {

    tab();
    sb.append("while (");

    gerarExpressao(node.getCondicao());

    sb.append(") {\n");

    indent++;
    gerarBloco(node.getCorpo());
    indent--;

    tab();

    sb.append("}\n");
  }

  // GERAR FOR LOOP
  private void gerarFor(ParaNode node) {

    tab();

    sb.append("for (");

    gerarExpressao(node.getInit());

    sb.append("; ");

    gerarExpressao(node.getCondicao());

    sb.append("; ");

    gerarExpressao(node.getIncremento());

    sb.append(") {\n");

    indent++;
    gerarBloco(node.getCorpo());
    indent--;

    tab();

    sb.append("}\n");
  }

  // GERAR EXPRESSÃO
  private void gerarExpressao(ASTNode node) {

    if (node instanceof LiteralIntNode) {
        sb.append(((LiteralIntNode) node).getValor());
    }

    else if (node instanceof LiteralRealNode) {
        sb.append(((LiteralRealNode) node).getValor());
    }

    else if (node instanceof LiteralStringNode) {
        sb.append("\"")
          .append(((LiteralStringNode) node).getValor())
          .append("\"");
    }

    else if (node instanceof IDNode) {
        sb.append(((IDNode) node).getNome());
    }

    else if (node instanceof OpBinNode) {
        gerarOpBin((OpBinNode) node);
    }

    else if (node instanceof OpUnNode) {
      sb.append(((OpUnNode) node).getExpressao());
    }

    else if (node instanceof ChamadaFuncaoNode) {
        gerarChamada((ChamadaFuncaoNode) node);
    }
  }

  // GERAR OPBIN
  private void gerarOpBin(OpBinNode op) {

    sb.append("(");

    gerarExpressao(op.getEsquerda());

    sb.append(" ").append(op.getOperador()).append(" ");

    gerarExpressao(op.getDireita());

    sb.append(")");
  }


  // GERAR CHAMADA
  private void gerarChamada(ChamadaFuncaoNode c) {
    sb.append(c.getNome()).append("(");

    boolean first = true;

    for (ASTNode arg : c.getArgumentos()) {

        if (!first) sb.append(", ");

        gerarExpressao(arg);

        first = false;
    }

    sb.append(")");
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
