package parser;

import java.util.List;

public class StmtWhile extends Stmt {
    public final Expr cond;
    public final List<ASTNode> body;

    public StmtWhile(Expr cond, List<ASTNode> body) {
        this.cond = cond;
        this.body = body;
    }
}
