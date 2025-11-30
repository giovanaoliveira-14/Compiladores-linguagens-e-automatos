package parser;

import lexer.Lexer.Token;
import java.util.*;

public class Parser {
    private final List<Token> tokens;
    private int pos = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private Token peek() {
        return tokens.get(pos);
    }

    private Token consume() {
        return tokens.get(pos++);
    }

    private boolean check(String type) {
        return peek().type.equals(type);
    }

    private boolean match(String... types) {
        for (String t : types)
            if (check(t)) {
                consume();
                return true;
            }
        return false;
    }

    private void expect(String type) {
        if (!check(type))
            throw new RuntimeException("Esperado " + type + " mas veio " + peek());
        consume();
    }

    public ASTProgram parse() {
        return parseProgram();
    }

    public ASTProgram parseProgram() {
        List<ASTNode> funcs = new ArrayList<>();
        while (!check("EOF")) {
            if (check("CRIAR"))
                funcs.add(parseFunction());
            else {

                consume();
            }
        }
        return new ASTProgram(funcs);
    }

    private ASTFunction parseFunction() {
        consume();

        Token ttype = consume();
        String ret = ttype.text.toUpperCase();
        Token name = consume();
        if (!name.type.equals("IDENT"))
            throw new RuntimeException("Nome de função esperado");
        expect("OP");

        List<ASTParam> params = new ArrayList<>();
        if (!peek().text.equals(")")) {
            while (true) {
                Token ptype = consume();
                String ptypeText = ptype.text.toUpperCase();
                Token pid = consume();
                if (!pid.type.equals("IDENT"))
                    throw new RuntimeException("Nome de parametro esperado");
                params.add(new ASTParam(ptypeText, pid.text));
                if (peek().text.equals(")"))
                    break;
                expect("OP");
                if (peek().text.equals(")"))
                    break;
            }
        }

        if (peek().text.equals(")"))
            consume();
        else
            throw new RuntimeException("')' esperado");

        List<ASTNode> body = parseBlock();
       return new ASTFunction(name.text, ret, params, body);
    }

    private List<ASTNode> parseBlock() {
        List<ASTNode> stmts = new ArrayList<>();
        if (peek().text.equals("{")) {
            consume();
            while (!peek().text.equals("}"))
                stmts.add(parseStmt());
            consume();
            return stmts;
        }

        while (!check("CRIAR") && !check("EOF")) {
            stmts.add(parseStmt());
        }
        return stmts;
    }

    private ASTNode parseStmt() {
        if (check("CRIAR"))
            return parseVarDecl();
        if (check("SE"))
            return parseIf();
        if (check("ENQUANTO"))
            return parseWhile();
        if (check("RETORNE"))
            return parseReturn();

        if (check("IDENT")) {
            Token id = consume();
            if (peek().text.equals("=")) {
                consume();
                Expr e = parseExpr();
                return new StmtAssign(id.text, e);
            } else if (peek().text.equals("(")) {

                ExprCall call = parseCall(id.text);
                return new StmtExpr(call);
            } else {

                ExprVar v = new ExprVar(id.text);
                return new StmtExpr(v);
            }
        }

        Expr e = parseExpr();
        return new StmtExpr(e);
    }

    private StmtVarDecl parseVarDecl() {
        consume();
        Token t = consume();
        String type = t.text.toUpperCase();
        Token id = consume();
        if (!id.type.equals("IDENT"))
            throw new RuntimeException("Nome de variavel esperado");
        Expr init = null;
        if (peek().text.equals("recebendo") || peek().text.equalsIgnoreCase("recebendo")) {
            consume();
            init = parseExpr();
        }
        return new StmtVarDecl(type, id.text, init);
    }

    private StmtIf parseIf() {
        consume();

        if (peek().text.equals("("))
            consume();
        Expr cond = parseExpr();
        if (peek().text.equals(")"))
            consume();
        List<ASTNode> thenB = parseBlock();
        List<ASTNode> elseB = null;
        if (check("SENAO")) {
            consume();
            elseB = parseBlock();
        }
        return new StmtIf(cond, thenB, elseB);
    }

    private StmtWhile parseWhile() {
        consume();
        if (peek().text.equals("("))
            consume();
        Expr cond = parseExpr();
        if (peek().text.equals(")"))
            consume();
        List<ASTNode> body = parseBlock();
        return new StmtWhile(cond, body);
    }

    private StmtReturn parseReturn() {
        consume();
        Expr e = parseExpr();
        return new StmtReturn(e);
    }

    private Expr parseExpr() {
        return parseOr();
    }

    private Expr parseOr() {
        Expr left = parseAnd();
        while (peek().type.equals("IDENT") && peek().text.equalsIgnoreCase("ou")) {
            consume();
            Expr r = parseAnd();
            left = new ExprBinary(left, "ou", r);
        }
        return left;
    }

    private Expr parseAnd() {
        Expr left = parseEquality();
        while (peek().type.equals("IDENT") && peek().text.equalsIgnoreCase("e")) {
            consume();
            Expr r = parseEquality();
            left = new ExprBinary(left, "e", r);
        }
        return left;
    }

    private Expr parseEquality() {
        Expr left = parseRelational();
        while (peek().type.equals("OP") && peek().text.equals("==")) {
            String op = consume().text;
            Expr r = parseRelational();
            left = new ExprBinary(left, op, r);
        }
        return left;
    }

    private Expr parseRelational() {
        Expr left = parseAdd();
        while (peek().type.equals("OP") && (peek().text.equals("<") || peek().text.equals(">")
                || peek().text.equals("<=") || peek().text.equals(">=") || peek().text.equals("!="))) {
            String op = consume().text;
            Expr r = parseAdd();
            left = new ExprBinary(left, op, r);
        }
        return left;
    }

    private Expr parseAdd() {
        Expr left = parseMul();
        while (peek().type.equals("OP") && (peek().text.equals("+") || peek().text.equals("-"))) {
            String op = consume().text;
            Expr r = parseMul();
            left = new ExprBinary(left, op, r);
        }
        return left;
    }

    private Expr parseMul() {
        Expr left = parseUnary();
        while (peek().type.equals("OP") && (peek().text.equals("*") || peek().text.equals("/"))) {
            String op = consume().text;
            Expr r = parseUnary();
            left = new ExprBinary(left, op, r);
        }
        return left;
    }

    private Expr parseUnary() {
        if (peek().type.equals("OP") && peek().text.equals("!")) {
            String op = consume().text;
            Expr e = parseUnary();
            return new ExprUnary(op, e);
        }
        return parsePrimary();
    }

    private Expr parsePrimary() {
        Token t = peek();
        if (t.type.equals("INT")) {
            consume();
            return new ExprInt(t.text);
        }
        if (t.type.equals("FLOAT")) {
            consume();
            return new ExprFloat(t.text);
        }
        if (t.type.equals("STRING")) {
            consume();
            return new ExprString(t.text);
        }
        if (t.type.equals("VERDADEIRO")) {
            consume();
            return new ExprBool(true);
        }
        if (t.type.equals("FALSO")) {
            consume();
            return new ExprBool(false);
        }
        if (t.type.equals("IDENT")) {
            Token id = consume();
            if (peek().text.equals("(")) {
                return parseCall(id.text);
            }
            return new ExprVar(id.text);
        }
        if (t.type.equals("OP") && t.text.equals("(")) {
            consume();
            Expr e = parseExpr();
            if (peek().type.equals("OP") && peek().text.equals(")"))
                consume();
            return e;
        }
        throw new RuntimeException("Expressao invalida: " + t);
    }

    private ExprCall parseCall(String name) {
        consume();
        List<Expr> args = new ArrayList<>();
        if (!peek().text.equals(")")) {
            while (true) {
                Expr e = parseExpr();
                args.add(e);
                if (peek().text.equals(")"))
                    break;

                if (peek().text.equals(","))
                    consume();
                else
                    break;
            }
        }
        if (peek().text.equals(")"))
            consume();
        return new ExprCall(name, args);
    }
}
