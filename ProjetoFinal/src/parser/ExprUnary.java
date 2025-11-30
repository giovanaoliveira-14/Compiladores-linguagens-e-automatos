package parser;

public class ExprUnary extends Expr {
    public final String op;
    public final Expr expr;

    public ExprUnary(String op, Expr expr) {
        this.op = op;
        this.expr = expr;
    }
}
