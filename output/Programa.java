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

  private static int TAMANHO_MAX = 100;
  private static double PI = 3.14159;
  private static int contador = 0;
  public static void cabecalho() {
    System.out.print("===== PROGRAMA DE TESTE MOCP =====\n");
  }

  public static int fatorial(int n) {
    if ((n <= 1)) {
      return 1;
    } else {
      return (n * fatorial((n - 1)));
    }
  }

  public static double media(int[] v, int tamanho) {
    int i;
    double soma = 0.0;
    for (i = 0; (i < tamanho); i = (i + 1)) {
      soma = (soma + (double)(v[i]));
    }
    return (soma / (double)(tamanho));
  }

  public static int soma_vetor(int[] v, int tamanho) {
    int i;
    int soma = 0;
    for (i = 0; (i < tamanho); i = (i + 1)) {
      soma = (soma + v[i]);
    }
    return soma;
  }

  public static void escrever_vetor(int[] v, int tamanho) {
    int i;
    System.out.print("{");
    for (i = 0; (i < tamanho); i = (i + 1)) {
      System.out.print(v[i]);
      if ((i < (tamanho - 1))) {
        System.out.print(", ");
      }
    }
    System.out.print("}\n");
  }

  public static int eh_primo(int n) {
    int i;
    if ((n <= 1)) {
      return 0;
    }
    if ((n == 2)) {
      return 1;
    }
    if (((n % 2) == 0)) {
      return 0;
    }
    i = 3;
    while (((i * i) <= n)) {
      if (((n % i) == 0)) {
        return 0;
      }
      i = (i + 2);
    }
    return 1;
  }

  public static void main(String[] args) {
    cabecalho();
    int n;
    int i;
    int opcao;
    int[] v = new int[10];
    double r;
    int carac;
    int[] vetor_lido;
    int tamanho_lido = 0;
    int a = -5;
    double b = ((double)(-a) * 2.5);
    System.out.print("Teste cast/unário: a = ");
    System.out.print(a);
    System.out.print(", b = ");
    System.out.print(b);
    System.out.print("\n");
    System.out.print("Digite um caractere: ");
    carac = scanner.next().charAt(0);
    System.out.print((char)(carac));
    System.out.print("\n");
    System.out.print("Digite uma string (máx 10): ");
    vetor_lido = lerStringParaVetor(scanner.next());
    tamanho_lido = 0;
    while ((vetor_lido[tamanho_lido] != 0)) {
      tamanho_lido = (tamanho_lido + 1);
    }
    System.out.print("String lida: ");
    imprimirStringVetor(vetor_lido);
    System.out.print("\n");
    n = scanner.nextInt();
    n = 5;
    for (i = 0; (i < n); i = (i + 1)) {
      v[i] = ((i * 2) + 1);
    }
    int soma = soma_vetor(v, n);
    double med = media(v, n);
    System.out.print("Vetor: ");
    escrever_vetor(v, n);
    System.out.print("Soma = ");
    System.out.print(soma);
    System.out.print("\n");
    System.out.print("Média = ");
    System.out.print(med);
    System.out.print("\n");
    System.out.print("Fatorial de 5: ");
    System.out.print(fatorial(5));
    System.out.print("\n");
    System.out.print("Números primos até 20: ");
    for (i = 2; (i <= 20); i = (i + 1)) {
      if ((eh_primo(i) == 1)) {
        System.out.print(i);
        System.out.print(" ");
      }
    }
    System.out.print("\n");
    int x = 10;
    int y = 20;
    if (((x > 0) && (y > 0))) {
      System.out.print("Ambos positivos\n");
    } else {
      System.out.print("Algum não é positivo\n");
    }
    int count = 0;
    while ((count < 3)) {
      System.out.print("count = ");
      System.out.print(count);
      System.out.print("\n");
      count = (count + 1);
    }
    for (i = 0; (i < 5); i = (i + 1)) {
      v[i] = (v[i] * 2);
    }
    System.out.print("Vetor após duplicar: ");
    escrever_vetor(v, n);
    double pi = PI;
    int pi_int = (int)(pi);
    System.out.print("PI = ");
    System.out.print(pi);
    System.out.print(", PI como inteiro = ");
    System.out.print(pi_int);
    System.out.print("\n");
    int flag = 1;
    if ((flag == 0)) {
      System.out.print("Flag é falso\n");
    } else {
      System.out.print("Flag é verdadeiro\n");
    }
    System.out.print("v[3] = ");
    System.out.print(v[3]);
    System.out.print("\n");
    int c = 65;
    System.out.print((char)(c));
    System.out.print("\n");
    System.out.print("Fim do programa!\n");
  }

}
