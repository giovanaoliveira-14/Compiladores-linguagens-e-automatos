package llvm;

import parser.*;
import java.util.*;

public class LLVMEmitter {

    private StringBuilder ir = new StringBuilder();

    public String generate(ASTProgram program) {
        emitHeader();

        for (ASTNode n : program.declarations) {
            ASTFunction f = (ASTFunction) n;
            emitFunction(f);
        }

        return ir.toString();
    }

    private void emitHeader() {
        ir.append("; LLVM IR gerado pelo compilador\n");
        ir.append("declare i32 @printf(i8*, ...)\n\n");
    }

    private void emitFunction(ASTFunction fn) {
    if (!fn.name.equals("main"))
        return;

    ir.append("define i32 @main() {\n");

    boolean hasReturn = false;

    for (ASTNode stmt : fn.body) {
        if (stmt instanceof StmtReturn)
            hasReturn = true;

        emitStmt(stmt);
    }

    if (!hasReturn) {
        ir.append("  ret i32 0\n");
    }

    ir.append("}\n");
}


    private void emitStmt(ASTNode stmt) {
        if (stmt instanceof StmtReturn) {
            StmtReturn r = (StmtReturn) stmt;
            int value = evalExpr(r.expr);
            ir.append("  ret i32 ").append(value).append("\n");
        }
    }

    private int evalExpr(Expr e) {
        if (e instanceof ExprInt) {
            return Integer.parseInt(((ExprInt) e).value);
        }

        if (e instanceof ExprBinary) {
            ExprBinary b = (ExprBinary) e;
            int left = evalExpr(b.left);
            int right = evalExpr(b.right);

            switch (b.op) {
                case "+": return left + right;
                case "-": return left - right;
                case "*": return left * right;
                case "/": return left / right;
            }
        }

        throw new RuntimeException(
            "Expressao nao suportada no gerador LLVM: " + e.getClass()
        );
    }
}
