package mocp;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import mocp.ast.ASTNode;
import mocp.semantic.SemanticAnalyzer;
import mocp.semantic.SymbolTable;
import mocp.codegen.CodeGenerator;

import java.util.List;
import java.io.FileWriter;
import java.io.IOException;

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

    // Verificar se foram encontrados erros de sintaxe
    if(errorListener.temErros()) {
      System.err.println(errorListener.getNumErros() + " erro(s) encontrado(s). Compilação interrompida.");
      System.exit(1);
    }

    // Imprimir a AST
    System.out.println("AST:");
    ast.print("");

    // Executa o Analisador Semântico
    SemanticAnalyzer analisadorSemantico = new SemanticAnalyzer();
    analisadorSemantico.analisar(ast);

    // --- PROTEÇÃO SEMÂNTICA ---
    // Se houve erros semânticos, abortamos a geração de código
    if (analisadorSemantico.houveErros()) {
      System.err.println("Erro: A análise semântica detetou problemas. Geração de TAC cancelada.");
      System.exit(1);
    }
    // --------------------------

    // Executa o Gerador de Código TAC
    mocp.tac.TACGenerator geradorTac = new mocp.tac.TACGenerator();
    List<mocp.tac.Instruction> tacOriginal = geradorTac.gerar(ast);

    // Executa o Otimizador de Código
    mocp.optimizer.Optimizer otimizador = new mocp.optimizer.Optimizer();
    List<mocp.tac.Instruction> tacOtimizado = otimizador.otimizar(tacOriginal);

    // 4. Imprime o resultado final otimizado
    System.out.println("\n--- CÓDIGO INTERMÉDIO OTIMIZADO (TAC) ---");
    System.out.println("---------------------------------------------------");
    for (mocp.tac.Instruction i : tacOtimizado) {
      System.out.println(i.toString());
    }
    System.out.println("---------------------------------------------------");

    // --- GERAÇÃO DE CÓDIGO ---
    CodeGenerator gen = new CodeGenerator();
    String code = gen.gerar(ast);

    // Guardar código gerado num ficheiro
    try (FileWriter writer = new FileWriter("output/Programa.java")) {
      writer.write(code);
      System.out.println("Ficheiro gerado com sucesso!");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
