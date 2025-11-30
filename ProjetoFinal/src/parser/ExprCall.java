package parser;

import java.util.List;

public class ExprCall extends Expr {
    public final String name;
    public final List<Expr> args;

    public ExprCall(String name, List<Expr> args) {
        this.name = name;
        this.args = args;
    }
}
