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

NUMBER : [0-9]+ ;
ID     : [a-zA-Z_][a-zA-Z0-9_]* ;

WS : [ \t\r\n]+ -> skip ;


/* Parser Rules */

/* TODO */

/* Lexer Rules */

/* TODO */

ANY : . ; // regra catch-all
