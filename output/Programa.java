class Main {
  public static void imprimirVetor(int[] v) {
    System.out.print("{");
    for (int i = 0; i < v.length; i++) {
      if (i > 0) System.out.print(",");
      System.out.print(v[i]);
    }
    System.out.print("}");
  }

  public static void main(String[] args) {
    int[] vetor = {1, 2, 3};
    imprimirVetor(vetor);
    System.exit(0);
  }

}
