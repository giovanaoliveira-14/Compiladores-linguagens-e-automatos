package parser;

import java.util.List;

public class StmtIf extends Stmt {
    public final Expr cond;
    public final List<ASTNode> thenBranch;
    public final List<ASTNode> elseBranch;

    public StmtIf(Expr cond, List<ASTNode> thenBranch, List<ASTNode> elseBranch) {
        this.cond = cond;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }
}
