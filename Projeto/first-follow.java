
import java.util.*;
public class FirstFollowCalculator_YourGrammar {
    Map<String, Set<String>> FIRST = new LinkedHashMap<>();
    Map<String, Set<String>> FOLLOW = new LinkedHashMap<>();
    Map<String, List<List<String>>> G;
    String start;

    public FirstFollowCalculator_YourGrammar(Map<String, List<List<String>>> grammar, String start) {
        this.G = grammar;
        this.start = start;
        for (String nt : grammar.keySet()) {
            FIRST.put(nt, new LinkedHashSet<>());
            FOLLOW.put(nt, new LinkedHashSet<>());
        }
    }

    boolean isTerminal(String s) {
        return !G.containsKey(s) && !"ε".equals(s);
    }

    public void computeFIRST() {
        boolean changed = true;
        while (changed) {
            changed = false;
            for (String A : G.keySet()) {
                for (List<String> prod : G.get(A)) {
                    if (prod.size()==0 || (prod.size()==1 && prod.get(0).equals("ε"))) {
                        if (FIRST.get(A).add("ε")) changed = true;
                    } else {
                        boolean allEps = true;
                        for (String X : prod) {
                            if (isTerminal(X)) {
                                if (FIRST.get(A).add(X)) changed = true;
                                allEps = false;
                                break;
                            } else {
                                for (String t : FIRST.get(X)) {
                                    if (!t.equals("ε")) {
                                        if (FIRST.get(A).add(t)) changed = true;
                                    }
                                }
                                if (FIRST.get(X).contains("ε")) {
                                    allEps = true;
                                } else {
                                    allEps = false;
                                    break;
                                }
                            }
                        }
                        if (allEps) {
                            if (FIRST.get(A).add("ε")) changed = true;
                        }
                    }
                }
            }
        }
    }

    public Set<String> firstOfString(List<String> beta) {
        Set<String> res = new LinkedHashSet<>();
        if (beta.size()==0) { res.add("ε"); return res; }
        boolean allEps = true;
        for (String X : beta) {
            if (isTerminal(X)) {
                res.add(X); allEps = false; break;
            } else {
                for (String t : FIRST.get(X)) if (!t.equals("ε")) res.add(t);
                if (FIRST.get(X).contains("ε")) { allEps = true; } else { allEps = false; break; }
            }
        }
        if (allEps) res.add("ε");
        return res;
    }

    public void computeFOLLOW() {
        FOLLOW.get(start).add("$");
        boolean changed = true;
        while (changed) {
            changed = false;
            for (String A : G.keySet()) {
                for (List<String> prod : G.get(A)) {
                    for (int i=0;i<prod.size();i++) {
                        String B = prod.get(i);
                        if (G.containsKey(B)) {
                            int before = FOLLOW.get(B).size();
                            List<String> beta = prod.subList(i+1, prod.size());
                            Set<String> firstBeta = firstOfString(beta);
                            for (String t : firstBeta) if (!t.equals("ε")) FOLLOW.get(B).add(t);
                            if (firstBeta.contains("ε")) {
                                FOLLOW.get(B).addAll(FOLLOW.get(A));
                            }
                            if (FOLLOW.get(B).size()>before) changed = true;
                        }
                    }
                }
            }
        }
    }

    public void demonstrate() {
        computeFIRST();
        System.out.println("=== FIRST ===");
        for (String A : FIRST.keySet()) {
            System.out.println("FIRST("+A+") = "+FIRST.get(A));
        }
        computeFOLLOW();
        System.out.println("\n=== FOLLOW ===");
        for (String A : FOLLOW.keySet()) {
            System.out.println("FOLLOW("+A+") = "+FOLLOW.get(A));
        }
    }

    public static Map<String, List<List<String>>> buildGrammar() {
        Map<String, List<List<String>>> g = new LinkedHashMap<>();
        g.put("Programa", Arrays.asList(
            Arrays.asList("DeclaracaoSeq","Fim")));
        g.put("DeclaracaoSeq", Arrays.asList(
            Arrays.asList("Declaracao","DeclaracaoSeq"),
            Arrays.asList("ε")));
        g.put("Declaracao", Arrays.asList(
            Arrays.asList("inteiro","Ident",";"),
            Arrays.asList("texto","Ident",";"),
            Arrays.asList("Bloco")));
        g.put("Bloco", Arrays.asList(
            Arrays.asList("{","ComandoSeq","}")));
        g.put("ComandoSeq", Arrays.asList(
            Arrays.asList("Comando","ComandoSeq"),
            Arrays.asList("ε")));
        g.put("Comando", Arrays.asList(
            Arrays.asList("Se","(","Condicao",")","Bloco"),
            Arrays.asList("Imprima","(","Expressao",")",";"),
            Arrays.asList("Ident","=","Expressao",";"),
            Arrays.asList("Bloco")));
        return g;
    }

    public static void main(String[] args) {
        Map<String, List<List<String>>> g = buildGrammar();
        FirstFollowCalculator_YourGrammar calc = new FirstFollowCalculator_YourGrammar(g, "Programa");
        calc.demonstrate();
    }
}
