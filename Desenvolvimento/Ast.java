import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Ast {

    interface Node { }
    static class Program implements Node {
        List<Stmt> stmts = new ArrayList<>();
        Program(List<Stmt> s){ this.stmts = s; }
    }

    static abstract class Stmt implements Node {}
    static class VarDecl extends Stmt {
        String name; String type; Expr init;
        VarDecl(String name, String type, Expr init){ this.name=name; this.type=type; this.init=init; }
    }
    static class Assign extends Stmt {
        String name; Expr expr;
        Assign(String n, Expr e){ name=n; expr=e; }
    }
    static class PrintStmt extends Stmt {
        Expr expr;
        PrintStmt(Expr e){ expr=e; }
    }
    static class IfStmt extends Stmt {
        Expr cond; List<Stmt> thenBlock; List<Stmt> elseBlock;
        IfStmt(Expr c, List<Stmt> t, List<Stmt> e){ cond=c; thenBlock=t; elseBlock=e; }
    }
    static class BlockStmt extends Stmt {
        List<Stmt> body;
        BlockStmt(List<Stmt> b){ body=b; }
    }

    static abstract class Expr implements Node {}
    static class IntLit extends Expr {
        int value; IntLit(int v){ value=v; }
    }
    static class StringLit extends Expr {
        String value; StringLit(String v){ value=v; }
    }
    static class VarRef extends Expr {
        String name; VarRef(String n){ name=n; }
    }
    static class BinOp extends Expr {
        String op; Expr left; Expr right;
        BinOp(String op, Expr l, Expr r){ this.op=op; left=l; right=r; }
    }
    static class RelOp extends Expr {
        String op; Expr left; Expr right;
        RelOp(String op, Expr l, Expr r){ this.op=op; left=l; right=r; }
    }

    private final PrintWriter out;
    private int tmpCounter = 0;
    private int labelCounter = 0;
    private Map<String,String> locals = new LinkedHashMap<>(); 
    private Map<String,Integer> globalStrings = new LinkedHashMap<>(); 
    private List<String> globalStringList = new ArrayList<>();

    public Ast(PrintWriter out) {
        this.out = out;
    }

    private String newTmp(){ return "%t" + (tmpCounter++); }
    private String newLabel(){ return "L" + (labelCounter++); }

   
    public void emitPrelude() {
        out.println("; ModuleID = 'module'");
        out.println("declare i32 @printf(i8*, ...)");
        out.println("declare i32 @puts(i8*)");
        out.println("");
        out.println("@.fmt_int = private constant [4 x i8] c\"%d\\0A\\00\"");
        out.println("");
    }

  
    private String makeGlobalString(String s) {
        if (globalStrings.containsKey(s)) return "@.str" + globalStrings.get(s);
        int id = globalStringList.size();
        globalStringList.add(s);
        globalStrings.put(s, id);
        return "@.str" + id;
    }

    public void emitGlobalStrings() {
        for (int i = 0; i < globalStringList.size(); i++) {
            String s = globalStringList.get(i);
        
            String esc = s.replace("\\", "\\5C").replace("\"","\\22").replace("\n","\\0A");
            int len = esc.getBytes().length + 1;
            out.println(String.format("@.str%d = private constant [%d x i8] c\"%s\\00\"", i, len, esc));
        }
        out.println("");
    }
    public void emitProgram(Program p) {
        out.println("define i32 @main() {");
        out.println("entry:");
        for (Stmt s : p.stmts) {
            if (s instanceof VarDecl) {
                VarDecl vd = (VarDecl)s;
                String alloca = "%" + vd.name;
                locals.put(vd.name, alloca);
                if ("inteiro".equalsIgnoreCase(vd.type)) {
                    out.println("  " + alloca + " = alloca i32");
                } else if ("texto".equalsIgnoreCase(vd.type)) {
                    out.println("  " + alloca + " = alloca i8*");
                } else {
                    out.println("  " + alloca + " = alloca i32");
                }
            }
        }
        for (Stmt s : p.stmts) {
            emitStmt(s);
        }

        out.println("  ret i32 0");
        out.println("}");
    }

    private void emitStmt(Stmt s) {
        if (s instanceof VarDecl) {
            VarDecl vd = (VarDecl)s;
            if (vd.init != null) {
              
                ValueWithType v = emitExpr(vd.init);
                String alloca = locals.get(vd.name);
                if (v.type.equals("i32")) {
                    out.println("  store i32 " + v.value + ", i32* " + alloca);
                } else if (v.type.equals("i8*")) {
                  
                    out.println("  store i8* " + v.value + ", i8** " + alloca);
                } else {
                    out.println("  ; unsupported var init type: " + v.type);
                }
            } else {
              
                String alloca = locals.get(vd.name);
                if ("inteiro".equalsIgnoreCase(vd.type)) {
                    out.println("  store i32 0, i32* " + alloca);
                } else {
                    out.println("  store i8* null, i8** " + alloca);
                }
            }
        } else if (s instanceof Assign) {
            Assign a = (Assign)s;
            ValueWithType v = emitExpr(a.expr);
            String alloca = locals.get(a.name);
            if (alloca == null) {
                out.println("  ; ERRO: variável não declarada: " + a.name);
                return;
            }
            if (v.type.equals("i32")) {
                out.println("  store i32 " + v.value + ", i32* " + alloca);
            } else if (v.type.equals("i8*")) {
                out.println("  store i8* " + v.value + ", i8** " + alloca);
            } else {
                out.println("  ; unsupported assign type: " + v.type);
            }
        } else if (s instanceof PrintStmt) {
            PrintStmt ps = (PrintStmt)s;
            ValueWithType v = emitExpr(ps.expr);
            if (v.type.equals("i32")) {
                String tmp = newTmp();
                
                out.println("  " + tmp + " = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.fmt_int, i32 0, i32 0), i32 " + v.value + ")");
            } else if (v.type.equals("i8*")) {
                out.println("  call i32 @puts(i8* " + v.value + ")");
            } else {
                out.println("  ; print: tipo não suportado: " + v.type);
            }
        } else if (s instanceof IfStmt) {
            IfStmt is = (IfStmt)s;
           
            ValueWithType cond = emitExpr(is.cond);
          
            String condI1;
            if (cond.type.equals("i1")) {
                condI1 = cond.value;
            } else if (cond.type.equals("i32")) {
                String tmp = newTmp();
                out.println("  " + tmp + " = icmp ne i32 " + cond.value + ", 0");
                condI1 = tmp;
            } else {
                
                condI1 = "0";
            }
            String thenLabel = newLabel();
            String elseLabel = newLabel();
            String endLabel = newLabel();

            out.println("  br i1 " + condI1 + ", label %" + thenLabel + ", label %" + elseLabel);
          
            out.println(thenLabel + ":");
            for (Stmt st : is.thenBlock) emitStmt(st);
            out.println("  br label %" + endLabel);
          
            out.println(elseLabel + ":");
            if (is.elseBlock != null) {
                for (Stmt st : is.elseBlock) emitStmt(st);
            }
            out.println("  br label %" + endLabel);
            out.println(endLabel + ":");
        } else if (s instanceof BlockStmt) {
            BlockStmt bs = (BlockStmt)s;
            for (Stmt st : bs.body) emitStmt(st);
        } else {
            out.println("  ; stmt tipo desconhecido");
        }
    }
    static class ValueWithType {
        String value; String type;
        ValueWithType(String v, String t){ value=v; type=t; }
        public String toString(){ return value + ":" + type; }
    }

    private ValueWithType emitExpr(Expr e) {
        if (e instanceof IntLit) {
            IntLit il = (IntLit)e;
            return new ValueWithType(String.valueOf(il.value), "i32");
        } else if (e instanceof StringLit) {
            StringLit sl = (StringLit)e;
            String gname = makeGlobalString(sl.value);
            String tmp = newTmp();
            int id = globalStrings.get(sl.value);
            String gep = "getelementptr inbounds ([" + ((sl.value.getBytes().length)+1) + " x i8], [" + ((sl.value.getBytes().length)+1) + " x i8]* " + gname + ", i32 0, i32 0)";
            
            return new ValueWithType(gep, "i8*");
        } else if (e instanceof VarRef) {
            VarRef vr = (VarRef)e;
            String alloca = locals.get(vr.name);
            if (alloca == null) {
                out.println("  ; ERRO: variável não declarada (expressão): " + vr.name);
                return new ValueWithType("0","i32");
            }
            String tmp = newTmp();
            
            out.println("  " + tmp + " = load i32, i32* " + alloca);
            return new ValueWithType(tmp,"i32");
        } else if (e instanceof BinOp) {
            BinOp bo = (BinOp)e;
            ValueWithType L = emitExpr(bo.left);
            ValueWithType R = emitExpr(bo.right);
           
            if (!L.type.equals("i32") || !R.type.equals("i32")) {
                out.println("  ; WARNING: operação aritmética com tipos não-i32 (gerador educativo)");
            }
            String tmp = newTmp();
            switch (bo.op) {
                case "+": out.println("  " + tmp + " = add i32 " + L.value + ", " + R.value); break;
                case "-": out.println("  " + tmp + " = sub i32 " + L.value + ", " + R.value); break;
                case "*": out.println("  " + tmp + " = mul i32 " + L.value + ", " + R.value); break;
                case "/": out.println("  " + tmp + " = sdiv i32 " + L.value + ", " + R.value); break;
                default:
                    out.println("  ; Unsupported binop " + bo.op);
                    out.println("  " + tmp + " = add i32 0, 0");
            }
            return new ValueWithType(tmp, "i32");
        } else if (e instanceof RelOp) {
            RelOp ro = (RelOp)e;
            ValueWithType L = emitExpr(ro.left);
            ValueWithType R = emitExpr(ro.right);
            String tmp = newTmp();
            switch (ro.op) {
                case "==": out.println("  " + tmp + " = icmp eq i32 " + L.value + ", " + R.value); break;
                case "!=": out.println("  " + tmp + " = icmp ne i32 " + L.value + ", " + R.value); break;
                case ">": out.println("  " + tmp + " = icmp sgt i32 " + L.value + ", " + R.value); break;
                case "<": out.println("  " + tmp + " = icmp slt i32 " + L.value + ", " + R.value); break;
                case ">=": out.println("  " + tmp + " = icmp sge i32 " + L.value + ", " + R.value); break;
                case "<=": out.println("  " + tmp + " = icmp sle i32 " + L.value + ", " + R.value); break;
                default:
                    out.println("  ; unsupported relop " + ro.op);
                    out.println("  " + tmp + " = icmp eq i32 0, 1");
            }
            return new ValueWithType(tmp, "i1");
        } else {
            out.println("  ; expr tipo desconhecido");
            return new ValueWithType("0","i32");
        }
    }

    public static void main(String[] args) throws Exception {

        List<Stmt> stmts = new ArrayList<>();
        stmts.add(new VarDecl("a","inteiro", new IntLit(10)));
        stmts.add(new VarDecl("b","inteiro", new IntLit(20)));
        stmts.add(new PrintStmt(new BinOp("+", new VarRef("a"), new VarRef("b"))));

        List<Stmt> thenBlock = new ArrayList<>();
        thenBlock.add(new PrintStmt(new StringLit("a menor")));
        List<Stmt> elseBlock = new ArrayList<>();
        elseBlock.add(new PrintStmt(new StringLit("a nao menor")));

        stmts.add(new IfStmt(new RelOp("<", new VarRef("a"), new VarRef("b")), thenBlock, elseBlock));

        Program prog = new Program(stmts);

        Path outPath = Paths.get("module.ll");
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(outPath))) {
            Ast gen = new Ast(pw);
            gen.emitPrelude();
            gen.emitProgram(prog);
            gen.emitGlobalStrings();
        }
        System.out.println("Arquivo gerado: " + outPath.toAbsolutePath());
    }
}
