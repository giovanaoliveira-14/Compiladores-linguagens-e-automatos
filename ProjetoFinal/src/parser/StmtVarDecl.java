package parser;

public class StmtVarDecl extends Stmt {
    public final String type;
    public final String name;
    public final Expr init;

    public StmtVarDecl(String type, String name, Expr init) {
        this.type = type;
        this.name = name;
        this.init = init;
    }
}
