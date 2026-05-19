package mocp.ast;

/**
 * Enum que representa os tipos de dados da linguagem MOCP.
 * Anotado pelo analisador semântico nos nós de expressão.
 */
public enum Tipo {
    INTEIRO,
    REAL,
    VAZIO,
    INTEIRO_VETOR,
    REAL_VETOR,
    STRING,
    DESCONHECIDO  // tipo ainda não inferido ou inválido
}
