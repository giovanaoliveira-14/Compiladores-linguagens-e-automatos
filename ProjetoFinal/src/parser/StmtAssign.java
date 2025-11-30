package parser;

public class StmtAssign extends Stmt {
    public final String name;
    public final Expr expr;

    public StmtAssign(String name, Expr expr) {
        this.name = name;
        this.expr = expr;
    }
}
