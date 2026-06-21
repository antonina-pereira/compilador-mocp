# Compilador MOCP
Implementação de um compilador para a linguagem MOCP, uma variante da linguagem C com sintaxe em português.

## Arquitetura
O compilador segue um pipeline estruturado:
1. **Frontend:** Lexer e Parser e construção da Árvore Sintática Abstrata (AST) através de uma gramática para ANTLR4.
2. **Análise Semântica:** Validação de tipos, verificação de escopos e consistência de declarações.
3. **Backend (TAC):** Geração de Código de Três Endereços.
4. **Otimizador:** Aplicação de técnicas de dobramento de constantes, propagação de constantes e eliminação de código morto (ponto fixo).

## Requisitos
- Java JDK 17 ou superior
- ANTLR 4.13.2
- GNU Make

## Como correr o programa
### Passo 1
Na raiz do projeto, executar:
```
make
```
Este passo vai criar a pasta build e a pasta src/generated e gerar os ficheiros necessários para correr o programa.

### Passo 2
Correr o programa sobre um ficheiro MOCP usando como exemplo o ficheiro de teste `caso_sucesso_0.mocp`:
```
make run FILE=src/test/caso_sucesso_0.mocp
```
### Limpar os ficheiros gerados
```
make clean
```

### Resultado
O programa lê o ficheiro `.mocp`, imprime a árvore sintática (AST) no terminal e realiza uma análise semântica rigorosa.

* **Se forem detetados erros** (léxicos, sintáticos ou semânticos), o compilador imprime as mensagens de erro detalhadas no terminal e interrompe o processo imediatamente, garantindo que não é gerado código inválido.
* **Se não houver erros**, o compilador prossegue para as fases de *backend*:
    * **Geração de Código Intermédio (TAC):** Traduz a AST validada para uma lista linear de instruções de três endereços, estruturada com rótulos e variáveis temporárias.
    * **Otimização:** Aplica sucessivamente os módulos de *Constant Folding*, *Constant Propagation* e *Dead Code Elimination* até atingir um estado de estabilidade (ponto fixo).
    * **Output Final:** Imprime no terminal o Código de Três Endereços (TAC) otimizado e limpo, pronto para a etapa final de tradução.

> [!NOTE]
> ANTLR deve estar instalado e acessível usando o comando `antlr4`.
> Os ficheiros gerados pelo ANTLR (lexer e parser) não estão incluídos no repositório devido ao .gitignore, por isso têm de ser gerarados localmente.

## Definição da linguagem MOCP (My Own C in Português)
Na MOCP, a sintaxe formal da linguagem é portuguesa.
Todas as palavras-chave, tipos e funções reservadas devem ser escritas em português,
conforme definido neste documento.
A utilização das palavras-chave da linguagem C original (como int, if, else, while, return,
etc.) constitui erro sintático.
A MOCP é a linguagem C simplificada, mas com palavras-chave e funções em português,
mantendo as mesmas regras e restrições.
Não existem diretivas # (como #include).
Apenas existem os tipos inteiro e real.
As variáveis podem ser simples ou vetores.
Inteiros podem representar caracteres (ASCII).
Vetores de inteiros podem representar strings (terminadas em 0).
Os comentários são o habitual do C (//comment our /*comment*/.
Os operadores são mesmo da linguagem C.
