package parser;

public class StmtExpr extends Stmt {
    public final Expr expr;

    public StmtExpr(Expr expr) {
        this.expr = expr;
    }
}
