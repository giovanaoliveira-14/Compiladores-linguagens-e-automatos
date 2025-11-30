package parser;

public class StmtReturn extends Stmt {
    public final Expr expr;

    public StmtReturn(Expr expr) {
        this.expr = expr;
    }
}
