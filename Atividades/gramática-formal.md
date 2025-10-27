**Definição formal da gramática**

G = (V, Σ, P, S)

onde:

**V (variáveis / não-terminais):**

{Programa, Declaracao, Bloco, Comando, Expressao, Termo, Fator, Condicao, Tipo, Ident, Numero, StringLiteral, OperadorRelacional, OperadorLogico, OperadorAritmetico}

**Σ (terminais):**

Palavras reservadas, símbolos e identificadores:

{Se, Fim, Imprima, E, Ou, Não, inteiro, texto, verdadeiro, falso, "(", ")", "{", "}", ",", ";", "=", "+", "-", "\*", "/", ">", "<", ">=", "<=", "==", "!=", Ident, Numero, String}

**P (Conjunto de Produções):**

São as regras de substituição que definem como as sentenças da linguagem podem ser formadas. Cada produção tem a forma A → α, onde A ∈ V e α ∈ (V ∪ Σ)\*.

**S (símbolo inicial):**

Programa

**Gramática em EBNF (Extended Backus–Naur Form)**

Programa        = { Declaracao } "Fim" .

Declaracao      = "inteiro" Ident ";"

                 | "texto" Ident ";"

                 | Bloco .

Bloco           = "{" { Comando } "}" .

Comando         = "Se" "(" Condicao ")" Bloco

                 | "Imprima" "(" Expressao ")" ";"

                 | Ident "=" Expressao ";"

                 | Bloco .

Condicao        = Expressao OperadorRelacional Expressao

                 | "Não" "(" Condicao ")"

                 | Condicao OperadorLogico Condicao .

OperadorRelacional = ">" | "<" | ">=" | "<=" | "==" | "!=" .

OperadorLogico     = "E" | "Ou" .

OperadorAritmetico = "+" | "-" | "\*" | "/" .

Expressao       = Termo { ("+" | "-") Termo } .

Termo           = Fator { ("\*" | "/") Fator } .

Fator           = Ident

                 | Numero

                 | StringLiteral

                 | "verdadeiro"

                 | "falso"

                 | "(" Expressao ")" .

Ident           = letra { letra | digito | "\_" } .

Numero          = digito { digito } .

StringLiteral   = '"' { caractere } '"' .

letra           = "A" | "B" | ... | "Z" | "a" | "b" | ... | "z" .

digito          = "0" | "1" | ... | "9" .

caractere       = qualquer caractere imprimível exceto aspas duplas.

**Gramática em BNF (Backus–Naur Form)**

          ::= "Fim"

      ::= | ε

        ::= "inteiro" ";"

                       | "texto" ";"

                       |

              ::= "{" "}"

        ::= | ε

            ::= "Se" "(" ")"

                       | "Imprima" "(" ")" ";"

                       | "=" ";"

                       |

          ::=

                       | "Não" "(" ")"

                       |

::= ">" | "<" | ">=" | "<=" | "==" | "!="

    ::= "E" | "Ou"

::= "+" | "-" | "\*" | "/"

          ::=

                       | "-"

      ::= | ε

              ::=

          ::= ("\*" | "/") | ε

              ::=

                       |

                       |

                       | "verdadeiro"

                       | "falso"

                       | "(" ")"

              ::=

          ::= ( | | "\_") | ε

            ::=

        ::= | ε

      ::= '"' '"'

        ::= | ε

              ::= "A" | ... | "Z" | "a" | ... | "z"

            ::= "0" | ... | "9"

          ::= qualquer caractere imprimível exceto aspas duplas