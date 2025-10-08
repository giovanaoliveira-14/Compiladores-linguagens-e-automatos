import java.util.*;
import java.util.regex.*;

public class Main {
    static class AFD {
        int estadoInicial;
        Set<Integer> estadosFinais = new HashSet<>();
        Map<Integer, Map<Character, Integer>> transicoes = new HashMap<>();

        public AFD(int inicial) { this.estadoInicial = inicial; }

        public void addTransicao(int de, char simbolo, int para) {
            transicoes.computeIfAbsent(de, k -> new HashMap<>()).put(simbolo, para);
        }

        public void addEstadoFinal(int estado) { estadosFinais.add(estado); }

        public boolean reconhecer(String entrada) {
            int estadoAtual = estadoInicial;
            for (char c : entrada.toCharArray()) {
                Map<Character, Integer> mapa = transicoes.get(estadoAtual);
                if (mapa == null || !mapa.containsKey(c)) return false;
                estadoAtual = mapa.get(c);
            }
            return estadosFinais.contains(estadoAtual);
        }
    }


    static class Token {
        String lexema, tipo;
        int linha, coluna;
        public Token(String lexema, String tipo, int linha, int coluna) {
            this.lexema = lexema; this.tipo = tipo; this.linha = linha; this.coluna = coluna;
        }
        @Override
        public String toString() {
            return String.format("%-15s | %-15s (linha %d, col %d)", lexema, tipo, linha, coluna);
        }
    }


    static class Lexer {
        List<Token> tokens = new ArrayList<>();
        List<String> erros = new ArrayList<>();
        LinkedHashMap<Pattern, String> regras = new LinkedHashMap<>();

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
            for (int i = 0; i < linhas.length; i++) {
                String linha = linhas[i]; int numLinha = i + 1; int col = 1;
                while (!linha.isEmpty()) {
                    boolean reconhecido = false;
                    for (var entry : regras.entrySet()) {
                        Matcher m = entry.getKey().matcher(linha);
                        if (m.find()) {
                            String lexema = m.group();
                            if (!entry.getValue().equals("IGNORADO"))
                                tokens.add(new Token(lexema, entry.getValue(), numLinha, col));
                            linha = linha.substring(lexema.length());
                            col += lexema.length();
                            reconhecido = true;
                            break;
                        }
                    }
                    if (!reconhecido) {
                        char c = linha.charAt(0);
                        erros.add("Erro léxico na linha " + numLinha + ", coluna " + col + ": caractere inválido '" + c + "'");
                        linha = linha.substring(1); col++;
                    }
                }
            }
        }

        public void imprimirTokens() { tokens.forEach(System.out::println); }
        public void imprimirErros() { if (!erros.isEmpty()) { System.out.println("\n=== ERROS LÉXICOS ==="); erros.forEach(System.out::println); } }
    }


    public static void main(String[] args) {
        String codigo = """
        Programa
        Se x <= 10 Então
            Imprima "Você venceu"
        Fim
        # Comentário de linha
        """;

        Lexer lexer = new Lexer();
        lexer.analisar(codigo);
        lexer.imprimirTokens();
        lexer.imprimirErros();
    }
}


