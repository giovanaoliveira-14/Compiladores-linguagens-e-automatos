package main;

import lexer.Lexer;
import parser.Parser;
import parser.ASTNode;
import sema.SemanticAnalyzer;
import llvm.LLVMEmitter;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Uso: java main.Main arquivo.gjs [-o saida.ll]");
            return;
        }
        String path = args[0];
        String code = new String(Files.readAllBytes(Paths.get(path))); 

        Lexer lexer = new Lexer(code);
        var tokens = lexer.tokenize();

        Parser parser = new Parser(tokens);
        ASTNode program = parser.parseProgram();

        SemanticAnalyzer sema = new SemanticAnalyzer();
        sema.visit(program);

        LLVMEmitter emitter = new LLVMEmitter();
        String llvm = emitter.emit(program);

        String out = "a.ll";
        for (int i=1;i<args.length;i++) {
            if (args[i].equals("-o") && i+1<args.length) out = args[i+1];
        }
        Files.writeString(Paths.get(out), llvm);
        System.out.println("LLVM IR gerado em: " + out);
    }
}
