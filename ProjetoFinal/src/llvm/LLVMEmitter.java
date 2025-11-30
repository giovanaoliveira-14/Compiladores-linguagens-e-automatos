package llvm;

import parser.*;
import java.util.*;

public class LLVMEmitter {
    private final StringBuilder sb = new StringBuilder();
    private int strId = 0;
    private final Map<String,String> strConsts = new LinkedHashMap<>();

    private String mapType(String t){ switch(t){ case "INTEIRO": return "i32"; case "FLUTUANTE": return "double"; case "LOGICO": return "i1"; case "TEXTO": return "i8*"; default: return "i32"; } }

    public String emit(ASTNode node){
        sb.setLength(0);
        sb.append("; ModuleID = 'module'

");
        sb.append("declare i32 @printf(i8*, ...)

");
        if (node instanceof ASTProgram){ ASTProgram p = (ASTProgram)node; 
         
            for(ASTNode f : p.functions) emitFunction((ASTFunction)f);
     
            StringBuilder header = new StringBuilder();
            for (Map.Entry<String,String> e : strConsts.entrySet()){
                String name = e.getKey(); String val = e.getValue();
                header.append(name).append(" = constant [").append(val.length()+1).append(" x i8] c\"").append(escape(val)).append("\00\"
");
            }
            sb.insert(0, header.toString() + "
");
        }
        return sb.toString();
    }

    private void emitFunction(ASTFunction f){
        String ret = mapType(f.returnType);
        sb.append("define ").append(ret).append(" @").append(f.name).append("(");
        for (int i=0;i<f.params.size();i++){ ASTParam p = f.params.get(i); sb.append(mapType(p.type)).append(" %").append(p.name); if (i+1<f.params.size()) sb.append(", "); }
        sb.append(") {
");
        
        int tmp = 0;
        for(ASTNode s : f.body){ if (s instanceof StmtVarDecl){ StmtVarDecl d = (StmtVarDecl)s;
                sb.append("  %").append(d.name).append(" = alloca ").append(mapType(d.type)).append("
");
                if (d.init!=null){ 
                    String val = emitExpr(d.init, tmp++);
                    sb.append("  store ").append(mapType(d.type)).append(" ").append(val).append(", ").append(mapType(d.type)).append("* %").append(d.name).append("
");
                }
            } else if (s instanceof StmtExpr){ StmtExpr se = (StmtExpr)s; if (se.expr instanceof ExprCall){ ExprCall c = (ExprCall)se.expr; if (c.name.equalsIgnoreCase("IMPRIMA")){ 
                            for(Expr arg: c.args){ String aval = emitExpr(arg, tmp++); if (arg instanceof ExprString){ 
                                        String constName = addString(((ExprString)arg).value);
                                        sb.append("  %calltmp").append(tmp).append(" = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([").append(((ExprString)arg).value.length()+1).append(" x i8], [").append(((ExprString)arg).value.length()+1).append(" x i8]* ").append(constName).append(", i32 0, i32 0), i8* null)
");
                                    } else if (arg instanceof ExprInt){ sb.append("  %calltmp").append(tmp).append(" = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.strint, i32 0, i32 0), i32 ").append(((ExprInt)arg).value).append(")
"); }
                                }
                        }
                }
            } else if (s instanceof StmtReturn){ StmtReturn r = (StmtReturn)s; if (r.expr==null) sb.append("  ret ").append(ret).append(" 0
"); else { String v = emitExpr(r.expr, tmp++); sb.append("  ret ").append(ret).append(" ").append(v).append("
"); } }
        }
        sb.append("}

");
    }

    private String emitExpr(Expr e, int tmp){
        if (e instanceof ExprInt) return ((ExprInt)e).value;
        if (e instanceof ExprFloat) return ((ExprFloat)e).value;
        if (e instanceof ExprString){ String name = addString(((ExprString)e).value); return "getelementptr inbounds ( [" + (((ExprString)e).value.length()+1) + " x i8], [" + (((ExprString)e).value.length()+1) + " x i8]* " + name + ", i32 0, i32 0)"; }
        if (e instanceof ExprVar) return "%" + ((ExprVar)e).name;
        if (e instanceof ExprBinary){ ExprBinary b = (ExprBinary)e; String l = emitExpr(b.left, tmp+1); String r = emitExpr(b.right, tmp+2); 
         
            if (b.op.equals("+")) return "add i32 " + l + ", " + r; 
        }
        return "0";
    }

    private String addString(String s){ String key = "@.str" + (++strId); strConsts.put(key, s); return key; }

    private String escape(String s){ return s.replace("\","\\").replace("\"","\\""); }
}
