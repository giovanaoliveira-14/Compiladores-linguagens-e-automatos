package parser;

import java.util.List;

public class ASTFunction extends ASTNode {
    public final String name;
    public final String returnType;
    public final List<ASTParam> params;
    public final List<ASTNode> body;

    public ASTFunction(String name, String returnType,
                       List<ASTParam> params, List<ASTNode> body) {
        this.name = name;
        this.returnType = returnType;
        this.params = params;
        this.body = body;
    }
}
