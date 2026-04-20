package mocp;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Main {

  public static void main(String[] args) throws Exception {
    if(args.length == 0) {
      System.err.println("Uso: java mocp.Main <ficheiro.mocp>");
      System.exit(1);
    }

    String filename = args[0];

    // Ler o ficheiro
    CharStream input = CharStreams.fromFileName(filename);

    // Criar o lexer e substituir o error listener padrão pelo personalizado
    MOCPLexer lexer = new MOCPLexer(input);
    MOCPErrorListener errorListener = new MOCPErrorListener();
    lexer.removeErrorListeners();
    lexer.addErrorListener(errorListener);
    CommonTokenStream tokens = new CommonTokenStream(lexer);

    // Criar o parser e usar o mesmo error listener
    MOCPParser parser = new MOCPParser(tokens);
    parser.removeErrorListeners();
    parser.addErrorListener(errorListener);

    // Gerar a árvore sintática abstrata
    ParseTree tree = parser.programa();

    // Verificar se foram encontrados erros
    if(errorListener.temErros()) {
      System.err.println(errorListener.getNumErros() + " erro(s) encontrado(s). Árvore não gerada.");
      System.exit(1);
    }

    // Imprimir a árvore sintática abstrata (apenas se não houver erros)
    System.out.println(tree.toStringTree(parser));
  }
}
