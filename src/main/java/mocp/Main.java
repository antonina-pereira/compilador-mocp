package mocp;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import mocp.ast.ASTNode;
import mocp.semantic.SemanticAnalyzer;

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

    // Gerar a parse tree
    ParseTree tree = parser.programa();

    // Construir a AST
    ASTBuilder builder = new ASTBuilder();
    ASTNode ast = builder.visit(tree);

    // Verificar se foram encontrados erros
    if(errorListener.temErros()) {
      System.err.println(errorListener.getNumErros() + " erro(s) encontrado(s). Árvore não gerada.");
      System.exit(1);
    }

    // Imprimir a AST (apenas se não houver erros)
    System.out.println("AST:");
    ast.print("");

    SemanticAnalyzer analisadorSemantico = new SemanticAnalyzer();
    analisadorSemantico.analisar(ast);

    // Depois da analise semântica terminar, gerar o Three Address Code:
    mocp.tac.TACGenerator geradorTac = new mocp.tac.TACGenerator();
    geradorTac.gerar(ast);

  }
}
