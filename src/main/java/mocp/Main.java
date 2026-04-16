package mocp;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Main {

  public static void main(String[] args) throws Exception {
    if(args.length == 0) {
      System.err.println("Uso: java mocp.Main <ficheiro.mocp>");
      return;
    }

    String filename = args[0];

    // Ler o ficheiro
    CharStream input = CharStreams.fromFileName(filename);

    // Criar o lexer
    MOCPLexer lexer = new MOCPLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);

    // Criar o parser
    MOCPParser parser = new MOCPParser(tokens);

    // Gerar a parse tree
    ParseTree tree = parser.program();

    // Imprimir a árvore sintática abstrata
    System.out.println(tree.toStringTree(parser));
  }
}
