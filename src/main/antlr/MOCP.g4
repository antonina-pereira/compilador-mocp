grammar MOCP;

/* Parser Rules */

// Programa
programa
  : (declaracao
  | definicaoFuncao
  | afirmacao
  )* EOF
  ;

// Especificação dos tipos
especificadorTipo
  : INTEIRO
  | REAL
  | VAZIO
  ;

// Declaração
declaracao
  : especificadorTipo listaDeclarador SEMIVIRGULA
  ;

listaDeclarador
  : declarador (VIRGULA declarador)*
  ;

declarador
  : ID (ECOLCHETE NUM_INTEIRO DCOLCHETE)* (ATRIBUIR expressao)?
  ;

// Definição de funções
definicaoFuncao
  : especificadorTipo ID EPAREN listaParametro? DPAREN afirmacaoComposta?
  ;

// Chamada de funções
chamadaFuncao
  : ID EPAREN listaArgumento? DPAREN
  ;

listaParametro
  : parametro (VIRGULA parametro)*
  ;

parametro
  : especificadorTipo ID* (ECOLCHETE NUM_INTEIRO? DCOLCHETE)*
  ;

listaArgumento
  : expressao (VIRGULA expressao)*
  ;

// Afirmações
afirmacao
  : afirmacaoExpressao
  | afirmacaoComposta
  | afirmacaoSe
  | afirmacaoEnquanto
  | afirmacaoPara
  | afirmacaoRetornar
  ;

afirmacaoExpressao
  : expressao? SEMIVIRGULA
  ;

afirmacaoComposta
  : ECHAVE (declaracao | afirmacao)* DCHAVE
  ;

afirmacaoSe
  : SE EPAREN expressao DPAREN afirmacaoComposta (SENAO afirmacaoComposta)?
  ;

afirmacaoEnquanto
  : ENQUANTO EPAREN expressao DPAREN afirmacaoComposta
  ;

afirmacaoPara
  : PARA EPAREN expressao? SEMIVIRGULA expressao? SEMIVIRGULA expressao? DPAREN afirmacaoComposta
  ;

afirmacaoRetornar
  : RETORNAR expressao? SEMIVIRGULA
  ;

// Expressões
expressao
  : expressaoAtribuir
  ;

expressaoAtribuir
  : expressaoOULogica (ATRIBUIR expressaoAtribuir)?
  ;

expressaoOULogica
  : expressaoELogica (OU expressaoELogica)*
  ;

expressaoELogica
  : expressaoIgualdade (E expressaoIgualdade)*
  ;

expressaoIgualdade
  : expressaoRelacional ((IGUAL | DIFERENTE) expressaoRelacional)*
  ;

expressaoRelacional
  : expressaoAditiva ((MAIOR | MENOR | MAIOR_IGUAL | MENOR_IGUAL) expressaoAditiva)*
  ;

expressaoAditiva
  : expressaoMultiplicativa ((MAIS | MENOS) expressaoMultiplicativa)*
  ;

expressaoMultiplicativa
  : expressaoUnaria ((MULT | DIV | MODULO) expressaoUnaria)*
  ;

expressaoUnaria
  : NAO expressaoUnaria
  | MENOS expressaoUnaria
  | EPAREN especificadorTipo DPAREN expressaoUnaria
  | expressaoVetor
  ;

expressaoVetor
  : expressaoSimples (ECOLCHETE expressao DCOLCHETE)*
  ;

expressaoSimples
  : ID
  | NUM_INTEIRO
  | NUM_REAL
  | STRING
  | EPAREN expressao DPAREN
  | chamadaFuncao
  ;

/* Lexer Rules */

fragment DIGITO : [0-9] ;

NUM_INTEIRO : DIGITO+ ; // representa números inteiros

NUM_REAL : NUM_INTEIRO '.' NUM_INTEIRO ; // representa números decimais até 15 dígitos de precisão

STRING
  : '"' (~["\\] | '\\' .)* '"'
  ;

ESPACOBRANCO
  : [ \t\r\n\f]+ -> skip
  ;

COMENTARIO_LINHA
  : '//' ~[\r\n]* -> skip
  ;

COMENTARIO_BLOCO
  : '/*' .*? '*/' -> skip
  ;

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
