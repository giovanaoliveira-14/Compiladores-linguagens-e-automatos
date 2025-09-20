**CONSTRUÇÃO DE AUTÔNOMOS FINITOS DETERMINÍSTICOS**

**Palavras-chave:**
stateDiagram-v2

    \[\*\] --> S0

    S0 --> S1: "S"

    S1 --> S2: "e"

    S2 --> \[\*\]: palavra-chave "Se"

stateDiagram-v2

    \[\*\] --> S0

    S0 --> S3: "F"

    S3 --> S4: "i"

    S4 --> S5: "m"

    S5 --> \[\*\]: palavra-chave "Fim"

stateDiagram-v2

    \[\*\] --> S0

    S0 --> S6: "I"

    S6 --> S7: "m"

    S7 --> S8: "p"

    S8 --> S9: "r"

    S9 --> S10: "i"

    S10 --> S11: "m"

    S11 --> S12: "a"

    S12 --> \[\*\]: palavra-chave "Imprima"

**Identificadores:**
stateDiagram-v2

    \[\*\] --> S0

    S0 --> S1: \[a-z\]

    S1 --> S1: \[a-zA-Z0-9\]

    S1 --> \[\*\]

stateDiagram-v2

    \[\*\] --> S0

    S0 --> S2: "\_"

    S2 --> S3: \[a-z0-9\_\]

    S3 --> S3: \[a-z0-9\_\]

    S3 --> \[\*\]

**Números inteiros:** 

stateDiagram-v2

    \[\*\] --> S0

    S0 --> S1: \[0-9\]

    S1 --> S1: \[0-9\]

    S1 --> \[\*\]

**Strings:**

stateDiagram-v2

    \[\*\] --> S0

    S0 --> S1: '"'

    S1 --> S1: caractere != \[" , \\\]

    S1 --> S1: sequência de escape (\\x)

    S1 --> S2: '"'

    S2 --> \[\*\]

**Operadores:**

*   **Aritméticos**: 
    

stateDiagram-v2

    \[\*\] --> S0

    S0 --> S1: + | - | \* | /

    S1 --> \[\*\]

*   **Relacionais**: 
    

stateDiagram-v2

    \[\*\] --> S0

    S0 --> S1: "="

    S1 --> S2: "="

    S2 --> \[\*\]: "=="

stateDiagram-v2

    \[\*\] --> S0

    S0 --> S3: "!"

    S3 --> S4: "="

    S4 --> \[\*\]: "!="

stateDiagram-v2

    \[\*\] --> S0

    S0 --> S5: "<"

    S5 --> S6: "="

    S6 --> \[\*\]: "<="

    S5 --> \[\*\]: "<"

    S0 --> S7: ">"

    S7 --> S8: "="

    S8 --> \[\*\]: ">="

    S7 --> \[\*\]: ">"

*   (E|Ou|Não)
    

stateDiagram-v2

    \[\*\] --> S0

    S0 --> S1: "E"

    S1 --> \[\*\]: operador lógico "E"

stateDiagram-v2

    \[\*\] --> S0

    S0 --> S2: "O"

    S2 --> S3: "u"

    S3 --> \[\*\]: operador lógico "Ou"

stateDiagram-v2

    \[\*\] --> S0

    S0 --> S4: "N"

    S4 --> S5: "ã"

    S5 --> S6: "o"

    S6 --> \[\*\]: operador lógico "Não"

**Delimitadores e Símbolos:**

**stateDiagram-v2**

    \[\*\] **\-->** S0

    S0 **\-->** LBR**:** "\["

    LBR **\-->** \[\*\]**:** delimitador "\["

    S0 **\-->** RBR**:** "\]"

    RBR **\-->** \[\*\]**:** delimitador "\]"

    S0 **\-->** LP**:** "("

    LP **\-->** \[\*\]**:** delimitador "("

    S0 **\-->** RP**:** ")"

    RP **\-->** \[\*\]**:** delimitador ")"

    S0 **\-->** LCB**:** "{"

    LCB **\-->** \[\*\]**:** delimitador "{"

    S0 **\-->** RCB**:** "}"

    RCB **\-->** \[\*\]**:** delimitador "}"

    S0 **\-->** COMMA**:** ","

    COMMA **\-->** \[\*\]**:** delimitador ","

**Espaços em branco e Comentários:**

*   Espaços em branco: \\t|\\r|\\n|\\s
    

stateDiagram-v2

    \[\*\] --> S0

    S0 --> S1: espaço/tab/newline

    S1 --> S1: espaço/tab/newline

    S1 --> \[\*\]: ignorar (não gera token)

*   Comentários de linha: \\#.\*
    

stateDiagram-v2

    \[\*\] --> S0

    S0 --> S1: "#"

    S1 --> S1: qualquer caractere até fim da linha

    S1 --> \[\*\]: ignorar

**CÓDIGO**

import java.util.\*;

class AFD {

    private int estadoInicial;

    private Set estadosFinais;

    private Map\> transicoes;

    public AFD(int estadoInicial) {

        this.estadoInicial = estadoInicial;

        this.estadosFinais = new HashSet<>();

        this.transicoes = new HashMap<>();

    }

    public void addEstadoFinal(int estado) {

        estadosFinais.add(estado);

    }

    public void addTransicao(int de, char simbolo, int para) {

        transicoes.computeIfAbsent(de, k -> new HashMap<>()).put(simbolo, para);

    }

    // Reconhece se a entrada é aceita pelo AFD

    public boolean reconhecer(String entrada) {

        int estadoAtual = estadoInicial;

        for (char c : entrada.toCharArray()) {

            Map mapa = transicoes.get(estadoAtual);

            if (mapa == null || !mapa.containsKey(c)) {

                return false; // transição inválida → rejeitado

            }

            estadoAtual = mapa.get(c);

        }

        return estadosFinais.contains(estadoAtual);

    }

}

//AFD para Palavras-chave

class PalavrasChave {

    public static AFD criarAFDSe() {

        AFD afd = new AFD(0);

        afd.addTransicao(0, 'S', 1);

        afd.addTransicao(1, 'e', 2);

        afd.addEstadoFinal(2);

        return afd;

    }

    public static AFD criarAFDFim() {

        AFD afd = new AFD(0);

        afd.addTransicao(0, 'F', 1);

        afd.addTransicao(1, 'i', 2);

        afd.addTransicao(2, 'm', 3);

        afd.addEstadoFinal(3);

        return afd;

    }

    public static AFD criarAFDImprima() {

        AFD afd = new AFD(0);

        afd.addTransicao(0, 'I', 1);

        afd.addTransicao(1, 'm', 2);

        afd.addTransicao(2, 'p', 3);

        afd.addTransicao(3, 'r', 4);

        afd.addTransicao(4, 'i', 5);

        afd.addTransicao(5, 'm', 6);

        afd.addTransicao(6, 'a', 7);

        afd.addEstadoFinal(7);

        return afd;

    }

}

//AFD para identificadores

class Identificador {

    public static AFD criarAFD() {

        AFD afd = new AFD(0);

        // Letras minúsculas → estado 1

        for (char c = 'a'; c <= 'z'; c++) {

            afd.addTransicao(0, c, 1);

            afd.addTransicao(1, c, 1);

        }

        // Letras maiúsculas dentro de identificador

        for (char c = 'A'; c <= 'Z'; c++) {

            afd.addTransicao(1, c, 1);

        }

        // Dígitos

        for (char c = '0'; c <= '9'; c++) {

            afd.addTransicao(1, c, 1);

        }

        // Underscore

        afd.addTransicao(0, '\_', 2);

        afd.addTransicao(2, '\_', 2);

        for (char c = 'a'; c <= 'z'; c++) afd.addTransicao(2, c, 2);

        for (char c = '0'; c <= '9'; c++) afd.addTransicao(2, c, 2);

        afd.addEstadoFinal(1);

        afd.addEstadoFinal(2);

        return afd;

    }

}

//AFD para números inteiros

class Numero {

    public static AFD criarAFD() {

        AFD afd = new AFD(0);

        for (char c = '0'; c <= '9'; c++) {

            afd.addTransicao(0, c, 1);

            afd.addTransicao(1, c, 1);

        }

        afd.addEstadoFinal(1);

        return afd;

    }

}

//AFD para Strings

class StringLiteral {

    public static AFD criarAFD() {

        AFD afd = new AFD(0);

        // Estado inicial abre aspas

        afd.addTransicao(0, '"', 1);

        // Caracteres dentro da string

        // Para simplificação, aceitamos qualquer caractere exceto " e \\

        for (char c = 32; c <= 126; c++) {

            if (c != '"' && c != '\\\\') {

                afd.addTransicao(1, c, 1);

            }

        }

        // Escape sequence simplificada

        afd.addTransicao(1, '\\\\', 2); // \\

        for (char c = 32; c <= 126; c++) {

            afd.addTransicao(2, c, 1);

        }

        // Fechamento da string

        afd.addTransicao(1, '"', 3);

        afd.addEstadoFinal(3);

        return afd;

    }

}

//AFD para Operadores lógicos (E, Ou, Não)

class OperadoresLogicos {

    public static AFD criarAFDE() {

        AFD afd = new AFD(0);

        afd.addTransicao(0, 'E', 1);

        afd.addEstadoFinal(1);

        return afd;

    }

    public static AFD criarAFDOu() {

        AFD afd = new AFD(0);

        afd.addTransicao(0, 'O', 1);

        afd.addTransicao(1, 'u', 2);

        afd.addEstadoFinal(2);

        return afd;

    }

    public static AFD criarAFDNao() {

        AFD afd = new AFD(0);

        afd.addTransicao(0, 'N', 1);

        afd.addTransicao(1, 'ã', 2);

        afd.addTransicao(2, 'o', 3);

        afd.addEstadoFinal(3);

        return afd;

    }

}

//AFD para Delimitadores e símbolos

class Delimitadores {

    public static AFD criarAFD() {

        AFD afd = new AFD(0);

        char\[\] simbolos = {'\[', '\]', '(', ')', '{', '}', ',', ':'};

        for (char s : simbolos) {

            afd.addTransicao(0, s, 1);

        }

        afd.addEstadoFinal(1);

        return afd;

    }

}

//AFD para Espaços em branco e comentários

class EspacosComentarios {

    public static AFD criarAFDEspacos() {

        AFD afd = new AFD(0);

        char\[\] espacos = {' ', '\\t', '\\n', '\\r'};

        for (char c : espacos) {

            afd.addTransicao(0, c, 1);

            afd.addTransicao(1, c, 1);

        }

        afd.addEstadoFinal(1);

        return afd;

    }

    public static AFD criarAFDComentario() {

        AFD afd = new AFD(0);

        afd.addTransicao(0, '#', 1);

        // qualquer caractere até o fim da linha

        for (char c = 32; c <= 126; c++) {

            afd.addTransicao(1, c, 1);

        }

        afd.addEstadoFinal(1);

        return afd;

    }

}

class Main {

    public static void main(String\[\] args) {

        testarAFD("Palavras-chave 'Se'", PalavrasChave.criarAFDSe(), new String\[\]{"Se"}, new String\[\]{"se", "Sen"});

        testarAFD("Palavras-chave 'Fim'", PalavrasChave.criarAFDFim(), new String\[\]{"Fim"}, new String\[\]{"fim", "Fi"});

        testarAFD("Palavras-chave 'Imprima'", PalavrasChave.criarAFDImprima(), new String\[\]{"Imprima"}, new String\[\]{"imprima", "Imp"});

        testarAFD("Identificadores válidos", Identificador.criarAFD(),

                new String\[\]{"pontuacao", "vidasRestantes", "jogador\_1\_nome", "\_variavel1"},

                new String\[\]{"Pontuacao", "vidas-restantes", "jogador nome", "2vidas"});

        testarAFD("Números inteiros", Numero.criarAFD(),

                new String\[\]{"0", "42", "100"},

                new String\[\]{"3.14", "1a", "abc"});

        testarAFD("Strings", StringLiteral.criarAFD(),

                new String\[\]{"\\"Você venceu\\"", "\\"linha\\\\nquebrada\\""},

                new String\[\]{"'Texto'", "\\"Incompleto"});

        testarAFD("Operador 'E'", OperadoresLogicos.criarAFDE(), new String\[\]{"E"}, new String\[\]{"e"});

        testarAFD("Operador 'Ou'", OperadoresLogicos.criarAFDOu(), new String\[\]{"Ou"}, new String\[\]{"ou"});

        testarAFD("Operador 'Não'", OperadoresLogicos.criarAFDNao(), new String\[\]{"Não"}, new String\[\]{"nao"});

        testarAFD("Delimitadores", Delimitadores.criarAFD(),

                new String\[\]{"\[", "\]", "(", ")", "{", "}", ",", ":"},

                new String\[\]{";", "."});

        testarAFD("Espaços", EspacosComentarios.criarAFDEspacos(),

                new String\[\]{" ", "\\t", "\\n", "\\r", "  \\t\\n"},

                new String\[\]{});

        testarAFD("Comentários", EspacosComentarios.criarAFDComentario(),

                new String\[\]{"#", "# Comentário"},

                new String\[\]{"##"});

    }

    static void testarAFD(String nome, AFD afd, String\[\] validos, String\[\] invalidos) {

        System.out.println("\\n=== Testando " + nome + " ===");

        for (String s : validos)

            System.out.println("Válido '" + s + "': " + (afd.reconhecer(s) ? "ACEITO" : "REJEITADO"));

        for (String s : invalidos)

            System.out.println("Inválido '" + s + "': " + (afd.reconhecer(s) ? "ACEITO" : "REJEITADO"));

    }

}