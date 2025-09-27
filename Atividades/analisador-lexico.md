# Analisador Léxico – Entrega Parcial do Compilador

##  Descrição
Este é o **analisador léxico** do compilador, desenvolvido em Java.  
Ele reconhece os seguintes tipos de tokens:

- **PALAVRA_CHAVE**: termos reservados da linguagem (ex: Programa, Se, Fim, Imprima, etc.)  
- **IDENTIFICADOR**: nomes de variáveis ou funções (camelCase ou snake_case)  
- **NUMERO**: números inteiros  
- **STRING**: cadeias de texto entre aspas duplas  
- **OP_REL**: operadores relacionais (==, !=, <=, >=, <, >)  
- **OP_ARIT**: operadores aritméticos (+, -, *, /)  
- **DELIMITADOR**: símbolos especiais ([ ] ( ) { } , :)  

Se houver caracteres **inválidos**, o programa gera **erros léxicos** indicando linha e coluna.

---

## Como usar

### 1. Copiar o código
Copie o arquivo `LexerOnline.java` para um compilador online de Java (por exemplo, [JDoodle](https://www.jdoodle.com/online-java-compiler/) ou [OnlineGDB](https://www.onlinegdb.com/online_java_compiler)).

### 2. Inserir código de teste
No arquivo, dentro da variável `codigo` no `Main`, cole o código que você deseja analisar.  
Exemplo:

```java
String codigo = """
Programa
Se x <= 10 Então
    Imprima "Você venceu"
Fim
""";


