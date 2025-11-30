package lexer;

import java.util.*;

public class Lexer {
    private final String src;
    private int pos = 0;
    private final int len;

    public Lexer(String s) { this.src = s; this.len = s.length(); }

    public static class Token {
        public final String type;
        public final String text;
        public Token(String type, String text) { this.type = type; this.text = text; }
        public String toString(){return type+":"+text;}
    }

    private static final Set<String> KEYWORDS = new HashSet<>(Arrays.asList(
        "SE","SENAO","ENQUANTO","CRIAR","RETORNE","IMPRIMA",
        "INTEIRO","FLUTUANTE","LOGICO","TEXTO","VERDADEIRO","FALSO"
    ));

    public List<Token> tokenize(){
        List<Token> out = new ArrayList<>();
        while (pos < len) {
            char c = peek();
            if (Character.isWhitespace(c)) { advance(); continue; }
            if (c=='\"') { out.add(readString()); continue; }
            if (Character.isLetter(c) || c=='_') { out.add(readIdentOrKeyword()); continue; }
            if (Character.isDigit(c)) { out.add(readNumber()); continue; }
            
            if (c=='+'||c=='-'||c=='*'||c=='/'||c=='('||c==')'||c=='{'||c=='}'||c==','||c==';'||c=='<'||c=='>'||c=='='||c=='!'){
                out.add(readOp()); continue;
            }
           
            advance();
        }
        out.add(new Token("EOF",""));
        return out;
    }

    private char peek(){ return src.charAt(pos); }
    private char advance(){ return src.charAt(pos++); }

    private Token readString(){
        StringBuilder sb = new StringBuilder();
        advance(); 
        while (pos < len && peek() != '"') {
            char c = advance();
            if (c=='\\' && pos < len) {
                char n = advance(); sb.append(n);
            } else sb.append(c);
        }
        if (pos < len && peek()=='"') advance();
        return new Token("STRING", sb.toString());
    }

    private Token readIdentOrKeyword(){
        int start = pos;
        while (pos<len && (Character.isLetterOrDigit(peek()) || peek()=='_')) advance();
        String word = src.substring(start,pos);
        String up = word.toUpperCase();
        if (KEYWORDS.contains(up)) return new Token(up,word);
        return new Token("IDENT", word);
    }

    private Token readNumber(){
        int start = pos;
        while (pos<len && Character.isDigit(peek())) advance();
        if (pos<len && peek()=='.'){
            advance();
            while (pos<len && Character.isDigit(peek())) advance();
            return new Token("FLOAT", src.substring(start,pos));
        }
        return new Token("INT", src.substring(start,pos));
    }

    private Token readOp(){
        char c = advance();
       
        if ((c=='='||c=='<'||c=='>'||c=='!') && pos < len && src.charAt(pos)=='='){
            char n = advance();
            return new Token("OP", ""+c+n);
        }
        return new Token("OP", ""+c);
    }
}
