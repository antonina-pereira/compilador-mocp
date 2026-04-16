grammar MOCP;

program
    : statement* EOF
    ;

statement
    : expr ';'
    ;

expr
    : NUMBER
    | ID
    ;

/* Parser Rules */

/* TODO */

/* Lexer Rules */

fragment DIGITO : [0-9] ;

NUM_INTEIRO : DIGITO+ ; // representa números inteiros

NUM_REAL : NUM_INTEIRO '.' NUM_INTEIRO ; // representa números decimais até 15 dígitos de precisão

NOVALINHA : '\r'? '\n' ;

ESPACOBRANCO : [ \t\r\n\f]+ -> skip ;

COMENTARIO_LINHA : '//' ~[\r\n]* ;

COMENTARIO_BLOCO : '/*' .*? '*/' ;

// Operadores
ATRIBUIR : '=' ;

// operadores aritméticos
MAIS : '+' ;
MENOS : '-' ;
MULT : '*' ;
DIV : '/' ;
MODULO : '%' ;

// operadores lógicos
E : '&&' ;
OU : '||' ;
NAO : '!' ;

// operadores relacionais
MENOR : '<' ;
MAIOR : '>' ;
MENOR_IGUAL : '<=' ;
MAIOR_IGUAL : '>=' ;
IGUAL : '==' ;
DIFERENTE : '!=' ;

// Vírgulas
VIRGULA : ',' ;
SEMIVIRGULA : ';' ;

// Parentêses
EPAREN      : '(' ;
DPAREN      : ')' ;
ECHAVE      : '{' ;
DCHAVE      : '}' ;
ECOLCHETE      : '[' ;
DCOLCHETE      : ']' ;

// Tipos
INTEIRO : 'inteiro' ;
REAL : 'real' ;
VAZIO : 'vazio' ;

// Estruturas de controlo
SE : 'se' ;
SENAO : 'senao' ;
ENQUANTO : 'enquanto' ;
PARA : 'para' ;
RETORNAR : 'retornar' ;

ID : [a-zA-Z_] [a-zA-Z0-9_]* ;

ANY : . ; // regra catch-all
