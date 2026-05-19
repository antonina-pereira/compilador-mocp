package mocp;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import mocp.ast.ASTNode;
import mocp.ast.ProgramaNode;
import mocp.semantic.SemanticAnalyzer;
import mocp.tac.Instruction;
import mocp.tac.TACGenerator;
import mocp.optimizer.Optimizer;

import java.util.List;

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

    // Verificar erros léxicos/sintáticos
    if(errorListener.temErros()) {
      System.err.println(errorListener.getNumErros() + " erro(s) encontrado(s). Compilação interrompida.");
      System.exit(1);
    }

    // Construir a AST
    ASTBuilder builder = new ASTBuilder();
    ProgramaNode ast = (ProgramaNode) builder.visit(tree);

    // Imprimir a AST
    System.out.println("=== AST ===");
    ast.print("");

    // Análise semântica
    SemanticAnalyzer semantico = new SemanticAnalyzer();
    semantico.analisar(ast);
    if(semantico.temErros()) {
      System.err.println("\n=== Erros Semânticos ===");
      for(String erro : semantico.getErros()) {
        System.err.println(erro);
      }
      System.exit(1);
    }

    // Geração de código TAC
    TACGenerator gerador = new TACGenerator();
    List<Instruction> tac = gerador.gerar(ast);

    System.out.println("\n=== TAC (antes de otimizar) ===");
    for(Instruction instr : tac) {
      System.out.println("  " + instr);
    }

    // Otimização
    Optimizer otimizador = new Optimizer();
    List<Instruction> tacOtimizado = otimizador.otimizar(tac);

    System.out.println("\n=== TAC (otimizado) ===");
    for(Instruction instr : tacOtimizado) {
      System.out.println("  " + instr);
    }
  }
}
