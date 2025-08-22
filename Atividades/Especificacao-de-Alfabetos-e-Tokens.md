# 📘 Especificação Inicial da Linguagem — Semana 2  
*(Linguagem Educacional para Jogos)*  

## Alfabeto da Linguagem
O alfabeto da nossa linguagem, inclui:  
- **Letras**: a–z, A–Z (inclui acentuação para permitir palavras em português: á, é, í, ó, ú, ç).  
- **Dígitos**:
    - Inteiros: 0 – 9
    - Flutuantes: [0-9]+\.[0-9]+

- **Símbolos especiais**:  
    - Operadores: `+ - * / % = == != < > <= >=`
    - Caracteres especiais: 
    `{ } ( ) [ ] ; , . " '` 

- **Formatação**:  
    - Espaço em branco: (` `)
    - Quebra de linha: (`\ql`)

- **Movimentação do jogo**:
    - `↑`, `←`, `↓`, `→`
    - `w (cima)`, `a (esquerda)`, `s (baixo)`, `d (direita)`

- **Case-sensitive**:  **SIM**, nosso alfabeto terá validação para identificar letras maiúsculas e minúsculas, permitindo que se assemelhe ao máximo com uma linguagem de fácil entedimento.

- **Comentários**:
    - Comentário de uma linha:
        - `–> comentário`
    - Comentário de várias linhas (em bloco):
        - ```
            –> comentário
            comentário <--
            ```
---

## Definição de Tokens  

### ➿ Identificadores  
- Forma: **letra** ou `_` inicial, seguidos de letras, dígitos ou `_`.  
- Exemplos: `jogador1`, `vida_total`, `Pontuacao`.  
- Case-sensitive (ou seja, `vida` ≠ `Vida`).  

### ➿ Literais Numéricos  
- **Inteiros**: `0 – 9`, permitindo números com diversas casas (exemplo: 0, 30, 100, 4000).  
- **Decimais**: `3.14`, `0.5`.

### ➿ Literais de Texto (strings)  
- As strings serão identicadas quando a palavra se encontrar entre um conjunto de aspas duplas (`"Isso é uma string!"`).
- Os textos suportam o uso de caracteres especiais.
- Suporta comentários.
- Suporta o uso de caracteres especiais para criação de nova linha, espaços, etc. (`\ql`)

### ➿ Operadores  
- Aritméticos: `+`, `-`, `*`, `/`, `%`.  
- Relacionais: `==`, `!=`, `<`, `<=`, `>`, `>=`.  
- Lógicos: `&&`, `||`, `!`.  

### ➿ Palavras reservadas  
As palavras reservadas da nossa linguagem adotarão uma estrutura de fácil compreensão, com o objetivo de atender desenvolvedores iniciantes voltados ao desenvolvimento de jogos.

- Controle de fluxo e estrutura:
```
talvez se -> if
então talvez   seja -> else if
então será -> else
por enquanto -> while
receba -> return
criar -> function
inteiro -> int
flutuante -> float
boleano -> boolean
texto -> string
falso -> false
verdadeiro -> true
imprima -> print
chega -> break
para cada -> for
switch -> escolha
case -> se for
```  

### Exemplo de uso

#### Exemplo 1 — Texto  

```csharp
inicio

    texto mensagem = "Bem-vindo ao nosso jogo!";
    imprima(mensagem);
fim

```

#### Exemplo 2 — Soma

```csharp
inicio

    criar maiorNumero (inteiro a, inteiro b) {
        talvez se (a > b) {
            mensagem = "O número " + a + "é maior";
        } então talvez (b > a) {
            mensagem = "O número " + b + "é maior";
        } então será {
            mensagem = "Os números são iguais!";
        }

        receba mensagem;
    }

    imprima(maiorNumero(10, 9));

fim
```

#### Exemplo 3 — Sistema simples de colisão e pontuação 

```csharp

inteiro soma = 200;

por enquanto (verdadeiro) {
  talvez se (soma > 100) {
	imprima(soma);
  } então será {
	soma = soma + 1;
  }
}

```
