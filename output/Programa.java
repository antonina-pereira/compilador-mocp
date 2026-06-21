import java.util.Scanner;

class Main {
  private static Scanner scanner = new Scanner(System.in);

  private static int fator_global = 2;
  private static double pi = 3.1415;
  public static int calcula_dobro(int x) {
    return (x * fator_global);
  }

  public static void saudacao() {
    System.out.print("Bem-vindo ao Compilador MOCP!");
  }

  public static void main(String[] args) {
    int n;
    double resultado;
    int letra = 'A';
    saudacao();
    System.out.print("Introduza um numero: ");
    n = scanner.nextInt();
    resultado = (double)(-n);
    if ((n > 0)) {
      int temp = (n * 10);
      System.out.print(temp);
    } else {
      int temp = 0;
      System.out.print(temp);
    }
    resultado = calcula_dobro(n);
    System.out.print(resultado);
  }

}
