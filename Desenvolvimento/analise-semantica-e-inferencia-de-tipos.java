import java.util.*;


enum Tipo {
    INTEIRO,
    TEXTO,
    BOOLEANO,
    LISTA,     
    UNDEFINED, 
}

class Simbolo {
    final String nome;
    Tipo tipo;
    final boolean constante;
    final int linhaDecl;

    Simbolo(String nome, Tipo tipo, boolean constante, int linhaDecl) {
        this.nome = nome;
        this.tipo = tipo;
        this.constante = constante;
        this.linhaDecl = linhaDecl;
    }

    @Override
    public String toString() {
        return String.format("%s : %s %s (linha %d)",
                nome,
                tipo,
                constante ? "(constante)" : "",
                linhaDecl);
    }
}

class TabelaSimbolos {
    private final Map<String, Simbolo> tabela = new HashMap<>();

    public boolean existe(String nome) {
        return tabela.containsKey(nome);
    }

    public void declarar(String nome, Tipo tipo, boolean constante, int linha) {
        tabela.put(nome, new Simbolo(nome, tipo, constante, linha));
    }

    public Simbolo obter(String nome) {
        return tabela.get(nome);
    }

    public Collection<Simbolo> listar() {
        return tabela.values();
    }
}

abstract class ASTNode {
    final int linha;
    ASTNode(int linha) { this.linha = linha; }
    abstract void aceitar(VisitorSemantico visitor);
}

class DeclaracaoNode extends ASTNode {
    final boolean constante;
    final String tipoAnnot; 
    final String nome;
    final ExpressaoNode inicializador; 

    DeclaracaoNode(boolean constante, String tipoAnnot, String nome, ExpressaoNode inicializador, int linha) {
        super(linha);
        this.constante = constante;
        this.tipoAnnot = tipoAnnot;
        this.nome = nome;
        this.inicializador = inicializador;
    }

    @Override
    void aceitar(VisitorSemantico visitor) { visitor.visitar(this); }
}

class AtribuicaoNode extends ASTNode {
    final String nome;
    final ExpressaoNode expr;

    AtribuicaoNode(String nome, ExpressaoNode expr, int linha) {
        super(linha);
        this.nome = nome;
        this.expr = expr;
    }

    @Override
    void aceitar(VisitorSemantico visitor) { visitor.visitar(this); }
}

class IfNode extends ASTNode {
    final ExpressaoNode condicao;
    final List<ASTNode> corpo;
    final List<ASTNode> senao;

    IfNode(ExpressaoNode condicao, List<ASTNode> corpo, List<ASTNode> senao, int linha) {
        super(linha);
        this.condicao = condicao;
        this.corpo = corpo;
        this.senao = senao;
    }

    @Override
    void aceitar(VisitorSemantico visitor) { visitor.visitar(this); }
}

class PrintNode extends ASTNode {
    final ExpressaoNode expr;
    PrintNode(ExpressaoNode expr, int linha) { super(linha); this.expr = expr; }
    @Override
    void aceitar(VisitorSemantico visitor) { visitor.visitar(this); }
}

abstract class ExpressaoNode extends ASTNode {
    ExpressaoNode(int linha) { super(linha); }
}

class LiteralInteiro extends ExpressaoNode {
    final int valor;
    LiteralInteiro(int valor, int linha) { super(linha); this.valor = valor; }
    @Override
    void aceitar(VisitorSemantico visitor) { visitor.visitar(this); }
}

class LiteralTexto extends ExpressaoNode {
    final String valor;
    LiteralTexto(String valor, int linha) { super(linha); this.valor = valor; }
    @Override
    void aceitar(VisitorSemantico visitor) { visitor.visitar(this); }
}

class LiteralBooleano extends ExpressaoNode {
    final boolean valor;
    LiteralBooleano(boolean valor, int linha) { super(linha); this.valor = valor; }
    @Override
    void aceitar(VisitorSemantico visitor) { visitor.visitar(this); }
}

class IdentificadorExpr extends ExpressaoNode {
    final String nome;
    IdentificadorExpr(String nome, int linha) { super(linha); this.nome = nome; }
    @Override
    void aceitar(VisitorSemantico visitor) { visitor.visitar(this); }
}

class BinOp extends ExpressaoNode {
    final String op;
    final ExpressaoNode esquerda;
    final ExpressaoNode direita;
    BinOp(String op, ExpressaoNode esquerda, ExpressaoNode direita, int linha) {
        super(linha);
        this.op = op; this.esquerda = esquerda; this.direita = direita;
    }
    @Override
    void aceitar(VisitorSemantico visitor) { visitor.visitar(this); }
}

class ListaLiteral extends ExpressaoNode {
    final List<ExpressaoNode> elementos;
    ListaLiteral(List<ExpressaoNode> elementos, int linha) { super(linha); this.elementos = elementos; }
    @Override
    void aceitar(VisitorSemantico visitor) { visitor.visitar(this); }
}

class VisitorSemantico {
    final TabelaSimbolos tabela = new TabelaSimbolos();
    final List<String> erros = new ArrayList<>();

    public void analisar(List<ASTNode> nodes) {
        for (ASTNode n : nodes) {
            n.aceitar(this);
        }
    }

    void registrarErro(int linha, String msg) {
        erros.add(String.format("Linha %d: %s", linha, msg));
    }

    void visitar(DeclaracaoNode no) {
        if (tabela.existe(no.nome)) {
            registrarErro(no.linha, "Redeclaração da variável '" + no.nome + "'");
            return;
        }

        Tipo tipoAnot = Tipo.UNDEFINED;
        if (no.tipoAnnot != null) {
            tipoAnot = mapTipo(no.tipoAnnot);
            if (tipoAnot == Tipo.UNDEFINED) {
                registrarErro(no.linha, "Tipo anotado desconhecido: " + no.tipoAnnot);
            }
        }

       
        Tipo tipoInferido = Tipo.UNDEFINED;
        if (no.inicializador != null) {
            tipoInferido = inferirTipoExpressao(no.inicializador);
        }

        Tipo tipoFinal = tipoAnot;
        if (tipoFinal == Tipo.UNDEFINED) tipoFinal = tipoInferido;
        if (tipoFinal == Tipo.UNDEFINED) tipoFinal = Tipo.UNDEFINED; 

       
        tabela.declarar(no.nome, tipoFinal, no.constante, no.linha);

        
        if (no.tipoAnnot != null && no.inicializador != null) {
            if (!compatibilidade(tipoFinal, tipoInferido)) {
                registrarErro(no.linha, "Incompatibilidade: anotação tipo " + tipoFinal +
                        " não corresponde ao inicializador (" + tipoInferido + ")");
            }
        }
    }

    void visitar(AtribuicaoNode no) {
        if (!tabela.existe(no.nome)) {
            registrarErro(no.linha, "Variável '" + no.nome + "' não foi declarada");
            return;
        }
        Simbolo s = tabela.obter(no.nome);
        if (s.constante) {
            registrarErro(no.linha, "Não é possível atribuir a constante '" + no.nome + "'");
            return;
        }
        Tipo tipoExpr = inferirTipoExpressao(no.expr);
        if (s.tipo == Tipo.UNDEFINED) {
            s.tipo = tipoExpr;
        } else {
            if (!compatibilidade(s.tipo, tipoExpr)) {
                registrarErro(no.linha, "Atribuição inválida: tipo da variável " + s.tipo +
                        " incompatível com expressão " + tipoExpr);
            }
        }
    }

    void visitar(IfNode no) {
        Tipo tcond = inferirTipoExpressao(no.condicao);
        if (tcond != Tipo.BOOLEANO) {
            registrarErro(no.linha, "Condição do 'Se' deve ser booleano, encontrado: " + tcond);
        }
        for (ASTNode stmt : no.corpo) stmt.aceitar(this);
        for (ASTNode stmt : no.senao) stmt.aceitar(this);
    }

    void visitar(PrintNode no) {
        inferirTipoExpressao(no.expr); 
    }

    Tipo inferirTipoExpressao(ExpressaoNode expr) {
        if (expr instanceof LiteralInteiro) return Tipo.INTEIRO;
        if (expr instanceof LiteralTexto) return Tipo.TEXTO;
        if (expr instanceof LiteralBooleano) return Tipo.BOOLEANO;
        if (expr instanceof IdentificadorExpr) {
            IdentificadorExpr id = (IdentificadorExpr) expr;
            if (!tabela.existe(id.nome)) {
                registrarErro(id.linha, "Variável '" + id.nome + "' não foi declarada");
                return Tipo.UNDEFINED;
            }
            Simbolo s = tabela.obter(id.nome);
            if (s.tipo == Tipo.UNDEFINED) {
                registrarErro(id.linha, "Tipo da variável '" + id.nome + "' é desconhecido (inferencia pendente)");
                return Tipo.UNDEFINED;
            }
            return s.tipo;
        }
        if (expr instanceof BinOp) {
            BinOp b = (BinOp) expr;
            Tipo le = inferirTipoExpressao(b.esquerda);
            Tipo ri = inferirTipoExpressao(b.direita);

            
            if (b.op.equals("+") || b.op.equals("-") || b.op.equals("*") || b.op.equals("/")) {
                if (le == Tipo.INTEIRO && ri == Tipo.INTEIRO) return Tipo.INTEIRO;
                
                if (b.op.equals("+") && (le == Tipo.TEXTO || ri == Tipo.TEXTO)) return Tipo.TEXTO;
                registrarErro(b.linha, "Operador aritmético '" + b.op + "' requer INTEIRO; encontrados: " + le + ", " + ri);
                return Tipo.UNDEFINED;
            }

           
            if (b.op.equals("<") || b.op.equals(">") || b.op.equals("<=") || b.op.equals(">=") || b.op.equals("==") || b.op.equals("!=")) {
                
                if (le == ri && le != Tipo.UNDEFINED) return Tipo.BOOLEANO;
                
                registrarErro(b.linha, "Comparação inválida entre tipos: " + le + " e " + ri);
                return Tipo.UNDEFINED;
            }

            
            if (b.op.equals("E") || b.op.equals("Ou")) {
                if (le == Tipo.BOOLEANO && ri == Tipo.BOOLEANO) return Tipo.BOOLEANO;
                registrarErro(b.linha, "Operador lógico '" + b.op + "' requer BOOLEANO; encontrados: " + le + ", " + ri);
                return Tipo.UNDEFINED;
            }
        }
        if (expr instanceof ListaLiteral) {
            ListaLiteral lst = (ListaLiteral) expr;
            Tipo elementoTipo = null;
            for (ExpressaoNode e : lst.elementos) {
                Tipo te = inferirTipoExpressao(e);
                if (elementoTipo == null) elementoTipo = te;
                else if (elementoTipo != te) {
                    registrarErro(lst.linha, "Lista com elementos de tipos diferentes: " + elementoTipo + " vs " + te);
                    elementoTipo = Tipo.UNDEFINED;
                }
            }
            if (elementoTipo == Tipo.INTEIRO || elementoTipo == Tipo.TEXTO || elementoTipo == Tipo.BOOLEANO) {
                return Tipo.LISTA; 
            } else return Tipo.UNDEFINED;
        }
        return Tipo.UNDEFINED;
    }

    boolean compatibilidade(Tipo esperado, Tipo obtido) {
        if (esperado == Tipo.UNDEFINED || obtido == Tipo.UNDEFINED) return false;
        if (esperado == obtido) return true;
      
        return false;
    }

    Tipo mapTipo(String annot) {
        if (annot == null) return Tipo.UNDEFINED;
        switch (annot.toLowerCase()) {
            case "inteiro": return Tipo.INTEIRO;
            case "texto": return Tipo.TEXTO;
            case "booleano": return Tipo.BOOLEANO;
            case "lista": return Tipo.LISTA;
            default: return Tipo.UNDEFINED;
        }
    }

    void imprimirRelatorio() {
        System.out.println("=== TABELA DE SÍMBOLOS ===");
        for (Simbolo s : tabela.listar()) {
            System.out.println(s);
        }
        System.out.println("\n=== ERROS SEMÂNTICOS ===");
        if (erros.isEmpty()) {
            System.out.println("Nenhum erro encontrado.");
        } else {
            erros.forEach(System.out::println);
        }
    }
}

public class SemanticAnalyzerDemo {
    public static void main(String[] args) {
        List<ASTNode> programa = new ArrayList<>();
        
        programa.add(new DeclaracaoNode(false, "Inteiro", "x", new LiteralInteiro(10, 1), 1));
        
        programa.add(new AtribuicaoNode("y", new BinOp("+", new IdentificadorExpr("x", 2), new LiteralInteiro(5, 2), 2), 2));
        
        programa.add(new DeclaracaoNode(false, "Texto", "z", new LiteralTexto("ola", 3), 3));
       
        programa.add(new AtribuicaoNode("z", new BinOp("+", new IdentificadorExpr("z", 4), new LiteralInteiro(5,4), 4), 4));
        
        List<ASTNode> corpo = new ArrayList<>();
        corpo.add(new PrintNode(new LiteralTexto("pequeno",5),5));
        List<ASTNode> senao = new ArrayList<>();
        senao.add(new PrintNode(new LiteralTexto("grande",6),6));
        programa.add(new IfNode(new BinOp("<", new IdentificadorExpr("x",7), new LiteralInteiro(5,7),7), corpo, senao, 7));

        VisitorSemantico analisador = new VisitorSemantico();
        analisador.analisar(programa);
        analisador.imprimirRelatorio();
    }
}
