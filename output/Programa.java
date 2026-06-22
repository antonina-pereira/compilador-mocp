import java.util.Scanner;

class Main {
  private static Scanner scanner = new Scanner(System.in);

  public static int[] lerStringParaVetor(String s) {
    int[] v = new int[s.length() + 1];
    for (int i = 0; i < s.length(); i++) {
      v[i] = (int) s.charAt(i);
    }
    v[s.length()] = 0;
    return v;
  }

  public static void imprimirStringVetor(int[] v) {
    for (int i = 0; i < v.length && v[i] != 0; i++) {
      System.out.print((char) v[i]);
    }
  }

  public static int calcular_tamanho(int[] str) {
    int i = 0;
    while ((str[i] != 0)) {
      i = (i + 1);
    }
    return i;
  }

  public static void inicializar_escondida(int[] esc, int tam) {
    int i;
    for (i = 0; (i < tam); i = (i + 1)) {
      esc[i] = '_';
    }
    esc[tam] = 0;
  }

  public static void limpar_ecra() {
    int i;
    for (i = 0; (i < 50); i = (i + 1)) {
      System.out.print("\n");
    }
  }

  public static void desenhar_forca(int erros) {
    System.out.print("\n +---+\n |   |\n");
    if ((erros == 0)) {
      System.out.print("     |\n     |\n     |\n");
    }
    if ((erros == 1)) {
      System.out.print(" O   |\n     |\n     |\n");
    }
    if ((erros == 2)) {
      System.out.print(" O   |\n |   |\n     |\n");
    }
    if ((erros == 3)) {
      System.out.print(" O   |\n/|   |\n     |\n");
    }
    if ((erros == 4)) {
      System.out.print(" O   |\n/|\\  |\n     |\n");
    }
    if ((erros == 5)) {
      System.out.print(" O   |\n/|\\  |\n/    |\n");
    }
    if ((erros >= 6)) {
      System.out.print(" O   |\n/|\\  |\n/ \\  |\n");
    }
    System.out.print("=========\n");
  }

  public static void main(String[] args) {
    int[] secreta;
    int[] escondida = new int[50];
    int tamanho;
    int erros;
    int acertos;
    int max_erros;
    int i;
    int letra;
    int acertou_nesta_ronda;
    erros = 0;
    acertos = 0;
    max_erros = 6;
    System.out.print("==================================\n");
    System.out.print("    BEM-VINDO AO JOGO DA FORCA    \n");
    System.out.print("==================================\n");
    System.out.print("[JOGADOR 1] Introduz a palavra secreta: ");
    secreta = lerStringParaVetor(scanner.next());
    tamanho = calcular_tamanho(secreta);
    inicializar_escondida(escondida, tamanho);
    limpar_ecra();
    System.out.print("=== O JOGO COMECOU! ===\n");
    System.out.print("Dica: Usa apenas letras minusculas.\n");
    while (((erros < max_erros) && (acertos < tamanho))) {
      desenhar_forca(erros);
      System.out.print("\nPalavra: ");
      imprimirStringVetor(escondida);
      System.out.print("\n");
      System.out.print("Erros: ");
      System.out.print(erros);
      System.out.print("/");
      System.out.print(max_erros);
      System.out.print("\n");
      System.out.print("\n[JOGADOR 2] Tenta uma letra: ");
      letra = scanner.next().charAt(0);
      acertou_nesta_ronda = 0;
      for (i = 0; (i < tamanho); i = (i + 1)) {
        if (((secreta[i] == letra) && (escondida[i] == '_'))) {
          escondida[i] = letra;
          acertos = (acertos + 1);
          acertou_nesta_ronda = 1;
        }
      }
      if ((acertou_nesta_ronda == 0)) {
        erros = (erros + 1);
      }
    }
    limpar_ecra();
    desenhar_forca(erros);
    if ((acertos == tamanho)) {
      System.out.print("\n[VITORIA] PARABENS! Tu salvaste o boneco!\n");
      System.out.print("A palavra era: ");
      imprimirStringVetor(secreta);
      System.out.print("\n");
    } else {
      System.out.print("\n[DERROTA] GAME OVER! O boneco foi enforcado.\n");
      System.out.print("A palavra secreta era: ");
      imprimirStringVetor(secreta);
      System.out.print("\n");
    }
    System.exit(0);
  }

}
