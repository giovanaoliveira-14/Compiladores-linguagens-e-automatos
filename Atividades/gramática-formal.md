# Definição formal da gramática

**G = (V, Σ, P, S)**

---

## Variáveis / Não-terminais (V)

```
{ Programa, Declaracao, Bloco, Comando, Expressao, Termo, Fator, Condicao, Tipo, Ident, Numero, StringLiteral, OperadorRelacional, OperadorLogico, OperadorAritmetico }
```

---

## Terminais (Σ)

Palavras reservadas, símbolos e identificadores:

```
{ Se, Fim, Imprima, E, Ou, Não, inteiro, texto, verdadeiro, falso, "(", ")", "{", "}", ",", ";", "=", "+", "-", "*", "/", ">", "<", ">=", "<=", "==", "!=", Ident, Numero, String }
```

---

## Conjunto de Produções (P)

São as regras de substituição que definem como as sentenças da linguagem podem ser formadas.  
Cada produção tem a forma **A → α**, onde **A ∈ V** e **α ∈ (V ∪ Σ)\***.

---

## Símbolo inicial (S)

```
Programa
```

---

# Gramática em EBNF (Extended Backus–Naur Form)

```ebnf
Programa        = { Declaracao } "Fim" .

Declaracao      = "inteiro" Ident ";"
                 | "texto" Ident ";"
                 | Bloco .

Bloco           = "{" { Comando } "}" .

Comando         = "Se" "(" Condicao ")" Bloco
                 | "Imprima" "(" Expressao ")" ";"
                 | Ident "=" Expressao ";"
                 | Bloco .

Condicao        = Expressao OperadorRelacional Expressao
                 | "Não" "(" Condicao ")"
                 | Condicao OperadorLogico Condicao .

OperadorRelacional = ">" | "<" | ">=" | "<=" | "==" | "!=" .
OperadorLogico     = "E" | "Ou" .
OperadorAritmetico = "+" | "-" | "*" | "/" .

Expressao       = Termo { ("+" | "-") Termo } .
Termo           = Fator { ("*" | "/") Fator } .

Fator           = Ident
                 | Numero
                 | StringLiteral
                 | "verdadeiro"
                 | "falso"
                 | "(" Expressao ")" .

Ident           = letra { letra | digito | "_" } .
Numero          = digito { digito } .
StringLiteral   = '"' { caractere } '"' .

letra           = "A" | "B" | ... | "Z" | "a" | "b" | ... | "z" .
digito          = "0" | "1" | ... | "9" .
caractere       = qualquer caractere imprimível exceto aspas duplas.
```

---

# Gramática em BNF (Backus–Naur Form)

```bnf
<Programa>           ::= <DeclaracaoSeq> "Fim"

<DeclaracaoSeq>      ::= <Declaracao> <DeclaracaoSeq> | ε

<Declaracao>         ::= "inteiro" <Ident> ";"
                       | "texto" <Ident> ";"
                       | <Bloco>

<Bloco>              ::= "{" <ComandoSeq> "}"

<ComandoSeq>         ::= <Comando> <ComandoSeq> | ε

<Comando>            ::= "Se" "(" <Condicao> ")" <Bloco>
                       | "Imprima" "(" <Expressao> ")" ";"
                       | <Ident> "=" <Expressao> ";"
                       | <Bloco>

<Condicao>           ::= <Expressao> <OperadorRelacional> <Expressao>
                       | "Não" "(" <Condicao> ")"
                       | <Condicao> <OperadorLogico> <Condicao>

<OperadorRelacional> ::= ">" | "<" | ">=" | "<=" | "==" | "!="
<OperadorLogico>     ::= "E" | "Ou"
<OperadorAritmetico> ::= "+" | "-" | "*" | "/"

<Expressao>          ::= <Termo> <ExpressaoTail>
                       | "-" <Termo> <ExpressaoTail>

<ExpressaoTail>      ::= <OperadorAritmetico> <Termo> <ExpressaoTail> | ε

<Termo>              ::= <Fator> <TermoTail>
<TermoTail>          ::= ("*" | "/") <Fator> <TermoTail> | ε

<Fator>              ::= <Ident>
                       | <Numero>
                       | <StringLiteral>
                       | "verdadeiro"
                       | "falso"
                       | "(" <Expressao> ")"

<Ident>              ::= <letra> <IdentTail>
<IdentTail>          ::= (<letra> | <digito> | "_") <IdentTail> | ε

<Numero>             ::= <digito> <NumeroTail>
<NumeroTail>         ::= <digito> <NumeroTail> | ε

<StringLiteral>      ::= '"' <Caracteres> '"'
<Caracteres>         ::= <caractere> <Caracteres> | ε

<letra>              ::= "A" | ... | "Z" | "a" | ... | "z"
<digito>             ::= "0" | ... | "9"
<caractere>          ::= qualquer caractere imprimível exceto aspas duplas
```
