package parser;

public class ExprBinary extends Expr {
    public final Expr left;
    public final String op;
    public final Expr right;

    public ExprBinary(Expr left, String op, Expr right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }
}
