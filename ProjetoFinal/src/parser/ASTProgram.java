package parser;

import java.util.*;

public class ASTProgram extends ASTNode {
    public final List<ASTNode> declarations;

    public ASTProgram(List<ASTNode> declarations) {
        this.declarations = declarations;
    }
}
