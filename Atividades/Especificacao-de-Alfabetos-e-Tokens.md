# 📘 Especificação Inicial da Linguagem — Semana 2  
*(Linguagem Educacional para Jogos)*  

## 🔤 1. Alfabeto da Linguagem (Σ)  
O alfabeto inclui:  
- **Letras**: a–z, A–Z (inclui acentuação para permitir palavras em português: á, é, í, ó, ú, ç).  
- **Dígitos**: 0–9.  
- **Símbolos de pontuação e operadores**:  
  `{ } ( ) [ ] ; , . " ' + - * / % = == != < <= > >= && || !`  
- **Espaços em branco**: espaço, tabulação, quebra de linha.  
- **Símbolos especiais para jogos**: `→` (movimento), `↑`, `↓`, `←`.  

👉 **Decisão pedagógica**: permitir acentos e nomes em português (como `personagem`, `inimigo`, `pontuação`) para aproximar da ideia do **Portugol**.  

---

## 📝 2. Definição de Tokens  

### 🔹 Identificadores  
- Forma: **letra** ou `_` inicial, seguidos de letras, dígitos ou `_`.  
- Exemplos: `jogador1`, `vida_total`, `Pontuacao`.  
- Case-sensitive (ou seja, `vida` ≠ `Vida`).  

### 🔹 Literais Numéricos  
- **Inteiros**: `0`, `42`, `1000`.  
- **Decimais**: `3.14`, `0.5`.  
- **Notação científica**: `1.5e3`.  

### 🔹 Literais de Texto (strings)  
- Entre aspas duplas: `"Você venceu!"`.  
- Suporta caracteres especiais: `\n` (nova linha), `\t` (tabulação).  

### 🔹 Operadores  
- Aritméticos: `+`, `-`, `*`, `/`, `%`.  
- Relacionais: `==`, `!=`, `<`, `<=`, `>`, `>=`.  
- Lógicos: `&&`, `||`, `!`.  
- Especiais para jogos:  
  - `→` (mover para direita), `↑`, `↓`, `←`.  
  - Ex.: `mover jogador → 10`.  

### 🔹 Palavras-chave  
Inspiradas em Portugol e C#, mas simplificadas para clareza:  
- Controle de fluxo: `se`, `senao`, `enquanto`, `para`.  
- Estruturas: `funcao`, `inicio`, `fim`, `classe`, `objeto`.  
- Jogos: `criar`, `mover`, `colidir`, `pontuar`, `som`.  
- Outros: `inteiro`, `decimal`, `texto`, `retorne`.  

### 🔹 Comentários  
- Linha única: `// comentário`.  
- Bloco: `/* comentário */`.  

---

## 🌐 3. Estrutura Léxica Geral  
- **Case-sensitive** para maior precisão.  
- **Espaços em branco** e quebras de linha são ignorados, exceto dentro de strings.  
- **Tokens de jogos** dão um diferencial pedagógico: comandos curtos e diretos para ações.  

---

## 📊 4. Exemplos Concretos de Programa  

### Exemplo 1 — Olá, jogo!  

```csharp
inicio
    texto mensagem = "Bem-vindo ao jogo!";
    mostrar(mensagem);
fim
```

### Exemplo 2 — Movimento de personagem  

```csharp
inicio
    inteiro x = 0;
    mover jogador → 5; // jogador anda 5 unidades para a direita
    mover jogador ↑ 3; // jogador sobe 3 unidades
fim
```

### Exemplo 3 — Sistema simples de colisão e pontuação 

```csharp
inicio
    inteiro pontos = 0;
    criar jogador em (0,0);
    criar inimigo em (5,0);

    se colidir(jogador, inimigo) entao
        pontos = pontos + 10;
        mostrar("Você ganhou " + pontos + " pontos!");
    fimse
fim
```


---

## 📔 5. Diário de Decisões de Design  
1. **Acentos permitidos** para aproximar da língua nativa (ex.: `pontuação`).  
2. **Tokens de jogos pré-configurados** (como `mover`, `colidir`, `pontuar`) para resultados rápidos em simulações visuais.  
3. **Case-sensitive** para preparar alunos para linguagens de mercado como C#.  
4. **Operadores gráficos (→, ↑, ↓, ←)** como “açúcar sintático”, facilitando a intuição espacial em jogos.  
5. **Strings simples**, sem interpolação inicial, para manter a curva de aprendizado suave.  
6. **Identificadores não podem começar por número**, para evitar ambiguidade com literais.  

---
