package mocp;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;

// Error listener personalizado para reportar erros léxicos e sintáticos
public class MOCPErrorListener extends BaseErrorListener {

  private int numErros = 0;

  @Override
  public void syntaxError(Recognizer<?, ?> recognizer,
                          Object offendingSymbol,
                          int line, int charPositionInLine,
                          String msg,
                          RecognitionException e) {
    numErros++;

    // Se a mensagem contiver "token recognition error", é um erro léxico
    if (msg != null && msg.contains("token recognition error")) {
      System.err.println("Erro léxico: " + msg + " na linha " + line + ", coluna " + charPositionInLine);
      return;
    }

    // se não, trata como um erro sintático normal
    System.err.println("Erro na linha " + line + ", coluna " + charPositionInLine + ": " + msg);
  }

  // Retorna true se encontrar erros durante a análise
  public boolean temErros() {
    return numErros > 0;
  }

  // Retorna o número total de erros encontrados
  public int getNumErros() {
    return numErros;
  }
}