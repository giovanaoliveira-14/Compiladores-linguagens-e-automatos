package sema;

import parser.*;
import java.util.*;

public class SemanticAnalyzer {
    static class Symbol { public final String type; public Symbol(String t){ this.type=t; } }

    static class FunctionSig { public final String returnType; public final List<ASTParam> params; public FunctionSig(String r, List<ASTParam> p){ this.returnType=r; this.params=p; } }

    private final Map<String, FunctionSig> functions = new HashMap<>();

    public void visit(ASTProgram prog){
        
        for(ASTNode n : prog.functions){
            if (!(n instanceof ASTFunction)) throw new RuntimeException("Top-level node deve ser funcao");
            ASTFunction f = (ASTFunction)n;
            if (functions.containsKey(f.name)) throw new RuntimeException("Funcao duplicada: " + f.name);
            functions.put(f.name, new FunctionSig(f.returnType, f.params));
        }
       
        if (!functions.containsKey("main")) throw new RuntimeException("Funcao main faltando");
       
        for(ASTNode n : prog.functions){ analyzeFunction((ASTFunction)n); }
    }

    private void analyzeFunction(ASTFunction fn){
        Map<String, Symbol> sym = new HashMap<>();
       
        for(ASTParam p : fn.params) sym.put(p.name, new Symbol(p.type));
        for(ASTNode st : fn.body) analyzeStmt(st, sym, fn);
    }

    private void analyzeStmt(ASTNode node, Map<String, Symbol> sym, ASTFunction fn){
        if (node instanceof StmtVarDecl){ StmtVarDecl d = (StmtVarDecl)node; if (sym.containsKey(d.name)) throw new RuntimeException("Variavel ja declarada: " + d.name); if (d.init!=null){ String t = analyzeExpr(d.init, sym); if (!typesCompatible(d.type, t)) throw new RuntimeException("Inicializacao: tipo " + t + " nao compativel com " + d.type); } sym.put(d.name, new Symbol(d.type)); return; }
        if (node instanceof StmtAssign){ StmtAssign a = (StmtAssign)node; if (!sym.containsKey(a.name)) throw new RuntimeException("Variavel nao declarada: " + a.name); String t = analyzeExpr(a.expr, sym); String varT = sym.get(a.name).type; if (!typesCompatible(varT, t)) throw new RuntimeException("Atribuicao: tipo " + t + " nao compativel com " + varT); return; }
        if (node instanceof StmtIf){ StmtIf s = (StmtIf)node; String t = analyzeExpr(s.cond, sym); if (!t.equals("LOGICO")) throw new RuntimeException("Condicao if deve ser LOGICO"); for(ASTNode b : s.thenBranch) analyzeStmt(b, sym, fn); if (s.elseBranch!=null) for(ASTNode b : s.elseBranch) analyzeStmt(b, sym, fn); return; }
        if (node instanceof StmtWhile){ StmtWhile w = (StmtWhile)node; String t = analyzeExpr(w.cond, sym); if (!t.equals("LOGICO")) throw new RuntimeException("Condicao while deve ser LOGICO"); for(ASTNode b : w.body) analyzeStmt(b, sym, fn); return; }
        if (node instanceof StmtReturn){ StmtReturn r = (StmtReturn)node; String t = r.expr==null?"VOID":analyzeExpr(r.expr, sym); if (!typesCompatible(fn.returnType, t)) throw new RuntimeException("Retorno: tipo " + t + " nao compativel com declaracao " + fn.returnType); return; }
        if (node instanceof StmtExpr){ StmtExpr e = (StmtExpr)node; analyzeExpr(e.expr, sym); return; }
        throw new RuntimeException("Unknown stmt: " + node.getClass());
    }

    private String analyzeExpr(ASTNode node, Map<String, Symbol> sym){
        if (node instanceof ExprInt) return "INTEIRO";
        if (node instanceof ExprFloat) return "FLUTUANTE";
        if (node instanceof ExprString) return "TEXTO";
        if (node instanceof ExprBool) return "LOGICO";
        if (node instanceof ExprVar){ ExprVar v = (ExprVar)node; if (!sym.containsKey(v.name)) throw new RuntimeException("Variavel nao declarada: " + v.name); return sym.get(v.name).type; }
        if (node instanceof ExprUnary){ ExprUnary u = (ExprUnary)node; String t = analyzeExpr(u.expr, sym); if (u.op.equals("!") && t.equals("LOGICO")) return "LOGICO"; throw new RuntimeException("Operador unario invalido para tipo " + t); }
        if (node instanceof ExprBinary){ ExprBinary b = (ExprBinary)node; String left = analyzeExpr(b.left, sym); String right = analyzeExpr(b.right, sym);
           
            if (b.op.equalsIgnoreCase("e") || b.op.equalsIgnoreCase("ou")){
                if (left.equals("LOGICO") && right.equals("LOGICO")) return "LOGICO";
                throw new RuntimeException("Operadores logicos exigem LOGICO");
            }
          
            if (b.op.equals("<")||b.op.equals(">")||b.op.equals("<=")||b.op.equals(">=")||b.op.equals("==")||b.op.equals("!=")){
                if (left.equals(right)) return "LOGICO";
               
                throw new RuntimeException("Comparacao entre tipos diferentes: " + left + " vs " + right);
            }
          
            if (b.op.equals("+")||b.op.equals("-")||b.op.equals("*")||b.op.equals("/")){
                if (left.equals("INTEIRO") && right.equals("INTEIRO")) return "INTEIRO";
                if (left.equals("TEXTO") || right.equals("TEXTO")) return "TEXTO";
                if (left.equals(right)) return left; 
                throw new RuntimeException("Operacao aritmetica entre tipos incompativeis: " + left + " vs " + right);
            }
            throw new RuntimeException("Operador desconhecido: " + b.op);
        }
        if (node instanceof ExprCall){ ExprCall c = (ExprCall)node; if (!functions.containsKey(c.name)) throw new RuntimeException("Funcao não declarada: " + c.name); FunctionSig sig = functions.get(c.name); if (sig.params.size() != c.args.size()) throw new RuntimeException("Numero de argumentos diferente para " + c.name); for (int i=0;i<c.args.size();i++){ String ta = analyzeExpr(c.args.get(i), sym); String tp = sig.params.get(i).type; if (!typesCompatible(tp, ta)) throw new RuntimeException("Argumento " + i + " tipo " + ta + " nao compativel com " + tp); } return sig.returnType; }
        throw new RuntimeException("Expressao desconhecida em semantica: " + node.getClass());
    }

    private boolean typesCompatible(String dest, String src){
        if (dest.equals(src)) return true;
    
        if (dest.equals("TEXTO")) return true;
     
        return false;
    }
}
