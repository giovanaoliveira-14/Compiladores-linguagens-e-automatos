# 1. Princípio do Match Mais Longo (Maximal Munch)

O **princípio do match mais longo**, também conhecido como *longest match*, é uma regra utilizada em **análise léxica** para resolver ambiguidades na identificação de tokens.  

Segundo este princípio, o **scanner (lexer)** deve sempre reconhecer o **maior lexema possível** que corresponde a uma determinada expressão regular, antes de tentar formar outros tokens menores.

### 🧩 Exemplo

Considere a entrada: >=.


Sem o princípio do match mais longo, o lexer poderia reconhecer `>` e depois `=` como dois tokens separados.  
Aplicando o princípio, o lexer reconhece `>=` como um **único token** de operador relacional.

Essa regra garante que o código fonte seja tokenizado de forma **consistente** e **sem ambiguidades**.

---

# 2. Estrutura do Token

Um **token** é a menor unidade significativa de um programa que o analisador sintático (*parser*) pode processar.

Cada token possui uma estrutura básica composta por:

- **Lexema:** o texto original do programa que representa o token.  
- **Tipo:** a categoria do token (ex.: palavra-chave, identificador, número, operador).  
- **Linha e coluna:** a posição do token no código fonte, usada para mensagens de erro ou depuração.

---

# 3. Bufferização

A **bufferização** é a técnica utilizada pelo lexer para ler o código fonte de forma eficiente, controlando a leitura por blocos em vez de processar caractere por caractere sem controle.

Geralmente, o lexer mantém um **buffer de entrada**, que armazena uma porção do código fonte que será analisada.  
O buffer permite **“voltar” e “avançar”** quando o lexer precisa decidir qual token é o mais longo que casa.

Essa técnica é essencial para implementar corretamente o princípio do *match mais longo*, pois o lexer precisa **olhar à frente** para decidir se deve continuar formando um token maior.

---

# 4. Integração com Análise Sintática

Após a geração dos tokens pelo lexer, o **parser (analisador sintático)** consome esses tokens para construir uma **estrutura hierárquica do programa**, que geralmente é uma **árvore sintática abstrata (AST)**.

O parser **não lê o código fonte diretamente**, mas sim os tokens gerados pelo lexer.  

Cada token fornece informações detalhadas (*tipo, lexema, posição*) que o parser usa para verificar a **gramática da linguagem**.  

Caso o parser encontre uma sequência de tokens que não corresponde à gramática, ele gera um **erro sintático** indicando a posição do problema.

---

## 💻 CÓDIGO (Exemplo em Java)

```java
import java.util.*;
import java.util.regex.*;

class Token {
    String lexema;
    String tipo;
    int linha;
    int coluna;

    public Token(String lexema, String tipo, int linha, int coluna) {
        this.lexema = lexema;
        this.tipo = tipo;
        this.linha = linha;
        this.coluna = coluna;
    }

    @Override
    public String toString() {
        return String.format("%-15s | %-15s (linha %d, col %d)", 
                lexema, tipo, linha, coluna);
    }
}

class Lexer {
    private final LinkedHashMap<Pattern, String> regras = new LinkedHashMap<>();
    private final List<Token> tokens = new ArrayList<>();
    private final List<String> erros = new ArrayList<>();

    public Lexer() {
        regras.put(Pattern.compile("^(Programa|Armazene|Guarde|mutável|Inteiro|Se|Então|Senão|Fim|ParaCada|Imprima|leia_entrada|para_inteiro)"), "PALAVRA_CHAVE");
        regras.put(Pattern.compile("^(E|Ou|Não)"), "OP_LOGICO");
        regras.put(Pattern.compile("^([a-z][a-zA-Z0-9]*|_[a-z0-9_]+)"), "IDENTIFICADOR");
        regras.put(Pattern.compile("^(\\d+)"), "NUMERO");
        regras.put(Pattern.compile("^\"(?:[^\"\\\\]|\\\\.)*\""), "STRING");
        regras.put(Pattern.compile("^(==|!=|<=|>=|<|>)"), "OP_REL");
        regras.put(Pattern.compile("^[+\\-*/]"), "OP_ARIT");
        regras.put(Pattern.compile("^[\\[\\]\\(\\)\\{\\},:]"), "DELIMITADOR");
        regras.put(Pattern.compile("^\\s+"), "IGNORADO");
        regras.put(Pattern.compile("^#.*"), "IGNORADO");
    }

    public void analisar(String codigo) {
        String[] linhas = codigo.split("\n");
        int linhaNum = 1;

        for (String linha : linhas) {
            int coluna = 1;
            String buffer = linha;

            while (!buffer.isEmpty()) {
                boolean reconhecido = false;
                Token tokenMaisLongo = null;

                for (var entry : regras.entrySet()) {
                    Matcher m = entry.getKey().matcher(buffer);
                    if (m.find()) {
                        String lexema = m.group();

                        if (tokenMaisLongo == null || lexema.length() > tokenMaisLongo.lexema.length()) {
                            tokenMaisLongo = new Token(lexema, entry.getValue(), linhaNum, coluna);
                        }
                    }
                }

                if (tokenMaisLongo != null) {
                    if (!tokenMaisLongo.tipo.equals("IGNORADO")) {
                        tokens.add(tokenMaisLongo);
                    }
                    buffer = buffer.substring(tokenMaisLongo.lexema.length());
                    coluna += tokenMaisLongo.lexema.length();
                    reconhecido = true;
                }

                if (!reconhecido) {
                    char c = buffer.charAt(0);
                    erros.add("Erro léxico na linha " + linhaNum + ", coluna " + coluna + ": caractere inválido '" + c + "'");
                    buffer = buffer.substring(1);
                    coluna++;
                }
            }

            linhaNum++;
        }
    }

    public List<Token> getTokens() { return tokens; }
    public List<String> getErros() { return erros; }

    public void imprimirTokens() { tokens.forEach(System.out::println); }
    public void imprimirErros() {
        if (!erros.isEmpty()) {
            System.out.println("\n=== ERROS LÉXICOS ===");
            erros.forEach(System.out::println);
        }
    }
}


class Node {
    String tipo;
    List<Node> filhos = new ArrayList<>();

    public Node(String tipo) {
        this.tipo = tipo;
    }

    public void addFilho(Node n) {
        filhos.add(n);
    }

    public void imprimir(String indent) {
        System.out.println(indent + tipo);
        for (Node f : filhos) {
            f.imprimir(indent + "  ");
        }
    }
}

class Parser {
    private final List<Token> tokens;
    private int pos = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private Token peek() {
        return pos < tokens.size() ? tokens.get(pos) : null;
    }

    private Token next() {
        return pos < tokens.size() ? tokens.get(pos++) : null;
    }

    public Node analisar() {
        Node raiz = new Node("Programa");

        while (peek() != null) {
            Token t = peek();

            if (t.lexema.equals("Inteiro")) {
                raiz.addFilho(parseDeclaracao());
            }
            else if (t.lexema.equals("Se")) {
                raiz.addFilho(parseIf());
            }
            else {
                next(); // Ignora tokens inesperados
            }
        }

        return raiz;
    }

    private Node parseDeclaracao() {
        Token tipo = next();
        Token id = next();
        Token op = next();
        Token num = next();

        Node decl = new Node("Declaração");
        decl.addFilho(new Node("Tipo: " + tipo.lexema));
        decl.addFilho(new Node("Identificador: " + id.lexema));

        Node atrib = new Node("Atribuição");
        atrib.addFilho(new Node("Número: " + num.lexema));

        decl.addFilho(atrib);

        return decl;
    }

    private Node parseIf() {
        next(); // "Se"

        Token id = next();
        Token op = next();
        Token num = next();
        Token entao = next();

        Node noIf = new Node("Se");

        Node cond = new Node("Condição");
        cond.addFilho(new Node("Identificador: " + id.lexema));
        cond.addFilho(new Node("Operador: " + op.lexema));
        cond.addFilho(new Node("Número: " + num.lexema));

        noIf.addFilho(cond);

        Node corpo = new Node("Corpo");
        while (peek() != null && !peek().lexema.equals("Fim")) {
            if (peek().lexema.equals("Imprima")) {
                corpo.addFilho(parseImprima());
            } else {
                next();
            }
        }

        if (peek() != null && peek().lexema.equals("Fim")) next();

        noIf.addFilho(corpo);
        return noIf;
    }

    private Node parseImprima() {
        next(); // Imprimir
        Token str = next();

        Node n = new Node("Imprima");
        n.addFilho(new Node("String: " + str.lexema));

        return n;
    }
}

public class Main {
    public static void main(String[] args) {
        String codigo = """
        Inteiro x = 10
        Se x <= 10 Então
            Imprima "Você venceu"
        Fim
        # comentário
        """;

        Lexer lexer = new Lexer();
        lexer.analisar(codigo);

        System.out.println(" TOKENS");
        lexer.imprimirTokens();
        lexer.imprimirErros();

        Parser parser = new Parser(lexer.getTokens());
        Node arvore = parser.analisar();

        System.out.println("\n ÁRVORE SINTÁTICA");
        arvore.imprimir("");
    }
}



