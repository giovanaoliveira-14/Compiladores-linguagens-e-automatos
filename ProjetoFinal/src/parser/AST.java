package parser;

import java.util.*;

public abstract class ASTNode { public <R> R accept(ASTVisitor<R> v) { return null; } }

public class ASTProgram extends ASTNode {
    public final List<ASTNode> functions = new ArrayList<>();
    public ASTProgram(List<ASTNode> funcs){ functions.addAll(funcs); }
}


public class ASTFunction extends ASTNode {
    public final String returnType;
    public final String name;
    public final List<ASTParam> params;
    public final List<ASTNode> body;
    public ASTFunction(String returnType, String name, List<ASTParam> params, List<ASTNode> body){
        this.returnType = returnType; this.name = name; this.params = params; this.body = body;
    }
}

public class ASTParam {
    public final String type; public final String name;
    public ASTParam(String t, String n){ this.type=t; this.name=n; }
}


public abstract class Stmt extends ASTNode {}
public class StmtVarDecl extends Stmt { public final String type; public final String name; public final Expr init; public StmtVarDecl(String t,String n, Expr i){this.type=t;this.name=n;this.init=i;} }
public class StmtAssign extends Stmt { public final String name; public final Expr expr; public StmtAssign(String n, Expr e){this.name=n;this.expr=e;} }
public class StmtIf extends Stmt { public final Expr cond; public final List<ASTNode> thenBranch; public final List<ASTNode> elseBranch; public StmtIf(Expr c,List<ASTNode> t,List<ASTNode> e){this.cond=c;this.thenBranch=t;this.elseBranch=e;} }
public class StmtWhile extends Stmt { public final Expr cond; public final List<ASTNode> body; public StmtWhile(Expr c,List<ASTNode> b){this.cond=c;this.body=b;} }
public class StmtReturn extends Stmt { public final Expr expr; public StmtReturn(Expr e){this.expr=e;} }
public class StmtExpr extends Stmt { public final Expr expr; public StmtExpr(Expr e){this.expr=e;} }


public abstract class Expr extends ASTNode {}
public class ExprInt extends Expr { public final String value; public ExprInt(String v){this.value=v;} }
public class ExprFloat extends Expr { public final String value; public ExprFloat(String v){this.value=v;} }
public class ExprString extends Expr { public final String value; public ExprString(String v){this.value=v;} }
public class ExprBool extends Expr { public final boolean value; public ExprBool(boolean v){this.value=v;} }
public class ExprVar extends Expr { public final String name; public ExprVar(String n){this.name=n;} }
public class ExprBinary extends Expr { public final Expr left; public final String op; public final Expr right; public ExprBinary(Expr l,String o,Expr r){this.left=l;this.op=o;this.right=r;} }
public class ExprUnary extends Expr { public final String op; public final Expr expr; public ExprUnary(String o,Expr e){this.op=o;this.expr=e;} }
public class ExprCall extends Expr { public final String name; public final List<Expr> args; public ExprCall(String n,List<Expr> a){this.name=n;this.args=a;} }
