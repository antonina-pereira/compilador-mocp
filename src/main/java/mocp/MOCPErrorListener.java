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

    // 1. Se a mensagem contiver "token recognition error", é um erro léxico genérico (ex: caracteres não reconhecidos)
    if (msg != null && msg.contains("token recognition error")) {
      System.err.println("Erro léxico: " + msg + " na linha " + line + ", coluna " + charPositionInLine);
      return;
    }

    // 2. NOVA VERIFICAÇÃO: Apanha o "Token Venenoso" das keywords de C
    if (offendingSymbol instanceof Token) {
      Token token = (Token) offendingSymbol;

      // Verifica se o token pertence à nossa regra ERR_C_KEYWORD
      if (token.getType() == MOCPLexer.ERR_C_KEYWORD) {
        System.err.println("[Erro Léxico/Sintático] Linha " + line + ", coluna " + charPositionInLine +
                ": Palavra-chave de C '" + token.getText() +
                "' não é permitida. Na linguagem MOCP utilize o equivalente em português!");
        return; // Retorna para não imprimir a mensagem sintática genérica abaixo
      }
    }

    // 3. Se não for nenhum dos casos acima, trata como um erro sintático normal
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