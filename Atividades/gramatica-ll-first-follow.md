## Gramática

```
Programa -> DeclaracaoSeq Fim
DeclaracaoSeq -> Declaracao DeclaracaoSeq | ε
Declaracao -> inteiro Ident ; | texto Ident ; | Bloco
Bloco -> { ComandoSeq }
ComandoSeq -> Comando ComandoSeq | ε
Comando -> Se ( Condicao ) Bloco | Imprima ( Expressao ) ; | Ident = Expressao ; | Bloco
```

## Conjuntos FIRST

- FIRST(Programa) = {Fim, inteiro, texto, {}
- FIRST(DeclaracaoSeq) = {inteiro, texto, {, ε}
- FIRST(Declaracao) = {inteiro, texto, {}
- FIRST(Bloco) = {{}
- FIRST(ComandoSeq) = {Ident, Imprima, Se, {, ε}
- FIRST(Comando) = {Ident, Imprima, Se, {}

## Conjuntos FOLLOW

- FOLLOW(Programa) = {$}
- FOLLOW(DeclaracaoSeq) = {Fim}
- FOLLOW(Declaracao) = {Fim, inteiro, texto, {}
- FOLLOW(Bloco) = {Fim, Ident, Imprima, Se, inteiro, texto, {, }}
- FOLLOW(ComandoSeq) = {}}
- FOLLOW(Comando) = {Ident, Imprima, Se, {, }}
