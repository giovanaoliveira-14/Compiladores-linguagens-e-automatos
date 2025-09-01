# Gramáticas Formais e Hierarquia de Chomsky

### Definição Completa G = (V, T, P, S)

### Conjunto de Variáveis(V):

V= { Programa, Declarações, DeclaraçãoVariáveis, Comandos, Comando, Condicional, LaçoRepetição, Decisão, Função, BlocoComandos, Expressão, Termo, Fator, ExpressãoLógica, OperadorLógico, Tipo, Identificador, Numérico, ParametrosLista, Movimento, EventoJogo }

### Conjunto de Terminais(T):

T = { Armazene, recebendo, Imprima, Criar, Fim, Talvez, Se, Então, talvez, Então, será, Por, enquanto, Escolha, Se, for, Chega, Verdadeiro, Falso, Inteiro, Flutuante, Logico, Texto, +, -, \*, /, =, ==, !=, <, >, <=, >=, (, ), {, }, ",", ;, identificador, literal\_inteiro, literal\_flutuante, literal\_texto, literal\_booleano }

### Produções(P):

*   Programa → BlocoDeclaracoes BlocoComandosPrincipal
    
*   BlocoDeclaracoes → Declaracao BlocoDeclaracoes | ε
    
*   Declaracao → DeclaracaoVariavel | DeclaracaoFuncao | DeclaracaoClasse | DeclaracaoEntidade
    
*   DeclaracaoVariavel → Armazene Tipo identificador | Armazene mutável Tipo identificador
    
*   Tipo → Inteiro | Flutuante | Texto | Booleano | Vetor | Sprite | Som
    
*   DeclaracaoFuncao → Funcao Tipo identificador ( ListaParametros ) BlocoComandos Fim
    
*   DeclaracaoClasse → Classe identificador BlocoClasse Fim | Classe identificador herda identificador BlocoClasse Fim

*   DeclaracaoEntidade → Entidade identificador BlocoEntidade Fim
    
*   BlocoComandos → Comando BlocoComandos | ε
    
*   BlocoComandosPrincipal → Comando BlocoComandosPrincipal | ε
    
*   Comando → ComandoAtribuicao | ComandoCondicional | ComandoLaco | ComandoEscrita  | ComandoEntrada | ComandoMovimento | ComandoEvento
    
*   ComandoCondicional → Se Expressao Então BlocoComandos Fim | Se Expressao Então BlocoComandos Senao BlocoComandos Fim

*   ComandoLaco → Enquanto Expressao BlocoComandos Fim | ParaCada Tipo identificador de literal até literal BlocoComandos Fim
    
*   ComandoEscrita → Imprima ListaArgumentos
    
*   ComandoEntrada → Leia identificador
    
*   ComandoMovimento → Mova identificador para ( Expressao , Expressao )| Rotacione identificador para Expressao graus

*   ComandoEvento → Quando EventoJogo faça BlocoComandos Fim
    
*   EventoJogo → Colisao(identificador, identificador) | TeclaPressionada(tecla) | CliqueMouse | Atualizar

*   Expressao → ExpressaoLogica
    
*   ExpressaoLogica → ExpressaoRelacional | ExpressaoLogica E ExpressaoRelaciona l| ExpressaoLogica Ou ExpressaoRelacional | Não ExpressaoLogica
    

*   ExpressaoRelacional → ExpressaoAritmetica | ExpressaoAritmetica == ExpressaoAritmetica | ExpressaoAritmetica != ExpressaoAritmetica | ExpressaoAritmetica > ExpressaoAritmetica | ExpressaoAritmetica < | ExpressaoAritmetica >= ExpressaoAritmetica | ExpressaoAritmetica <= ExpressaoAritmetica

*   ExpressaoAritmetica → Termo | ExpressaoAritmetica + Termo | ExpressaoAritmetica - Termo

*   Termo → Fator | Termo \* Fator | Termo / Fator
    
*   Fator → identificador | literal\_inteiro | literal\_flutuante | literal\_texto | literal\_booleano | ( Expressao ) | identificador ( ListaArgumentos )

*   ListaParametros → Parametro | Parametro , ListaParametros
    
*   Parametro → Tipo identificador
    
*   ListaArgumentos → Expressao | Expressao , ListaArgumentos
    
S = < programa >


## Classificação na Hierarquia de Chomsky

### Análise Formal da Classificação

Ao analisarmos a gramática da linguagem de programação a ser criada classificamos como Tipo 2 (Livre de Contexto) na hierarquia de Chomsky.

### Justificativa técnica

*   Produções: todas seguem A → α.
    
*   Não-terminais aninhados: permitem expressões e comandos recursivos, o que não é regular, mas é livre de contexto.
    
*   Palavras-chave compostas (ex: "Talvez se", "Então talvez") e uso de acentos não alteram a classificação, pois são tratados no nível lexical, antes da análise sintática.
    
*   Estruturas de controle e ECS/Clean Architecture: dependem do contexto de projeto, mas não mudam a gramática formal, só influenciam o léxico e o conjunto de palavras-chave disponíveis.
    

### Verificação

*   Não precisa ser do tipo 0 (irrestrita), pois a gramática é bem estruturada, sem necessidade de regras arbitrárias.
    
*   Não precisa ser do tipo 1(sensível ao contexto), como **αAβ → αγβ**, porque a substituição de símbolos não depende do contexto.
    
*   A gramática possui estruturas aninhadas, como **Se ... Fim** dentro de blocos, laços e funções, que não podem ser expressas por gramáticas regulares (tipo 3)
    


## Limitações da Gramática e Análise Semântica

O compilador que estamos desenvolvendo para a nossa nova linguagem de programação tem limitações, mas são desafios que podem ser superados. Nossa gramática, por ser livre de contexto, é ótima para validar a estrutura básica do código.Mas, ela não consegue "lembrar" do que já foi declarado antes.

É aí que entra a **análise semântica**. Essa etapa funciona como um assistente de memória para o compilador. Ela constrói uma **tabela de símbolos** para manter um registro de tudo que foi declarado no código, como variáveis e funções. Com isso, o compilador consegue garantir que você declare algo antes de usá-lo, evitando erros.

### Desafios e soluções de design

#### Ambiguidade em palavras-chave

Palavras-chave compostas como "**Talvez se**" ou "**Então será**" podem causar confusão para o analisador léxico. A solução que estamos adotando é diferenciá-las visualmente ou usar prefixos, como **KW\_TalvezSe**, ou simplesmente garantir que as palavras-chave nunca coincidam com os identificadores que você pode usar para suas variáveis.

**Acentos e caracteres especiais**

A linguagem foi pensada para se aproximar do português, aceitando caracteres como **ç**, **ã**, e **é**. Para evitar problemas de compatibilidade entre diferentes sistemas operacionais, a gente garante que o compilador use sempre a codificação **UTF-8**, que é um padrão global e mantém a consistência.

**Estruturas de controle complexas**

As palavras-chave para o controle de fluxo podem ser parecidas, dificultando a leitura. Uma alternativa para melhorar a clareza seria simplificar a sintaxe para algo como "**Se**", "**Senão**", ou "**Caso contrário**". Além disso, podemos encorajar o uso de indentação para deixar a estrutura do código mais clara visualmente.

**Dependência de contexto arquitetural**

Palavras-chave como "**Entidade**" ou "**Componente**" são comuns em arquiteturas específicas, como a **ECS**. Para que a linguagem seja flexível, a análise léxica é configurável, e pode se adaptar ao contexto do seu projeto. Assim, a linguagem pode ser útil tanto para um game dev usando ECS quanto para um engenheiro de software usando **Clean Architecture**.

### **O que a gramática faz e o que a semântica faz**

A divisão entre o que é de responsabilidade da **sintaxe** e o que é de responsabilidade da **semântica** é uma decisão de design. A gramática cuida da **estrutura** do código, enquanto a análise semântica foca nas **regras** contextuais, como:

*   **Declaração antes do uso**: Uma variável tem que ser declarada antes de ser utilizada.
    
*   **Tipagem de variáveis**: Cada variável tem um tipo específico.
    
*   **Escopo de funções**: Variáveis declaradas em uma função só existem dentro dela.
    
*   **Parâmetros de funções**: A quantidade de argumentos passados em uma chamada de função tem que ser a mesma que a função espera.
    


**Ambiguidades Tratadas na Linguagem Educacional para Jogos**

**1\. Problema do “Dangling-Else”**

Se condicao1 Então

    Talvez Se condicao2 Então

        comando1

Então Será   # A qual Se/Talvez Se pertence?

    comando2

Fim

**Solução:** Regra de associação — Então Será sempre se associa ao Se ou Talvez Se mais próximo não pareado.

**2\. Precedência de Operadores:** Hierarquia clara para a linguagem:

1.  () - Parênteses
    
2.  Não - Negação lógica
    
3.  \* / - Multiplicação e divisão
    
4.  \+ - - Adição e subtração
    
5.  \== != > < >= <= - Comparações
    
6.  E - AND lógico
    
7.  Ou - OR lógico
    

**3\. Associatividade:**

*   Operadores aritméticos: associativos à esquerda
    
*   Operadores lógicos: associativos à esquerda
    
*   Comparações: não associativas 
    



**Exemplos de Derivação**

**Programa:**

Armazene Inteiro pontuacao recebendo 85

Se pontuacao >= 100 Então

    Imprima "Você venceu"

Senao

    Imprima "Continue tentando"

Fim

**Derivação:**

Programa

→ BlocoDeclaracoes BlocoComandosPrincipal

→ Declaracao BlocoDeclaracoes BlocoComandosPrincipal

→ DeclaracaoVariavel BlocoDeclaracoes BlocoComandosPrincipal

→ Armazene Tipo identificador recebendo literal\_inteiro BlocoDeclaracoes BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 BlocoDeclaracoes BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 Comando BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 ComandoCondicional BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 Se Expressao Então BlocoComandos Senao BlocoComandos Fim BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 Se ExpressaoRelacional Então BlocoComandos Senao BlocoComandos Fim BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 Se ExpressaoAritmetica >= ExpressaoAritmetica Então BlocoComandos Senao BlocoComandos Fim BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 Se identificador >= literal\_inteiro Então BlocoComandos Senao BlocoComandos Fim BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 Se pontuacao >= 100 Então Comando Senao BlocoComandos Fim BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 Se pontuacao >= 100 Então ComandoEscrita Senao BlocoComandos Fim BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 Se pontuacao >= 100 Então Imprima ListaArgumentos Senao BlocoComandos Fim BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 Se pontuacao >= 100 Então Imprima "Você venceu" Senao Comando BlocoComandos Fim BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 Se pontuacao >= 100 Então Imprima "Você venceu" Senao Imprima "Continue tentando" Fim

**Derivação Expandida:**

Programa

Programa → BlocoDeclaracoes BlocoComandosPrincipal

→ Declaracao BlocoDeclaracoes BlocoComandosPrincipal

→ DeclaracaoVariavel BlocoDeclaracoes BlocoComandosPrincipal

→ Armazene Tipo identificador recebendo literal\_inteiro BlocoDeclaracoes BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 BlocoDeclaracoes BlocoComandosPrincipal

(termina as declarações) ⇒ Armazene Inteiro pontuacao recebendo 85 Comando BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 ComandoCondicional BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 Se Expressao Então BlocoComandos Senao BlocoComandos Fim BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 Se ExpressaoRelacional Então BlocoComandos Senao BlocoComandos Fim BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 Se ExpressaoAritmetica >= ExpressaoAritmetica Então BlocoComandos Senao BlocoComandos Fim BlocoComandosPrincipal→ Armazene Inteiro pontuacao recebendo 85 Se identificador >= literal\_inteiro Então BlocoComandos Senao BlocoComandos Fim BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 Se pontuacao >= 100 Então Comando Senao BlocoComandos Fim BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 Se pontuacao >= 100 Então ComandoEscrita Senao BlocoComandos Fim BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 Se pontuacao >= 100 Então Imprima ListaArgumentos Senao BlocoComandos Fim BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 Se pontuacao >= 100 Então Imprima Expressao Senao BlocoComandos Fim BlocoComandosPrincipal


→ Armazene Inteiro pontuacao recebendo 85 Se pontuacao >= 100 Então Imprima "Você venceu" Senao BlocoComandos Fim BlocoComandosPrincipal

Agora desenvolvendo o bloco Senao BlocoComandos:

→ Armazene Inteiro pontuacao recebendo 85 Se pontuacao >= 100 Então Imprima "Você venceu" Senao Comando BlocoComandos Fim BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 Se pontuacao >= 100 Então Imprima "Você venceu" Senao ComandoEscrita BlocoComandos Fim BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 Se pontuacao >= 100 Então Imprima "Você venceu" Senao Imprima ListaArgumentos Fim BlocoComandosPrincipal

→Armazene Inteiro pontuacao recebendo 85 Se pontuacao >= 100 Então Imprima "Você venceu" Senao Imprima "Continue tentando" Fim


**Características Específicas da Linguagem**

1\. Sintaxe Natural em Português

A linguagem adota comandos claros em português, para que o aluno foque na lógica e não em termos técnicos em inglês.

*   Exemplo:
    
```csharp

Guarde mutável Inteiro 85 como pontuacao  

Se pontuacao >= 100 Então  

    Imprima "Você venceu"  

Senão  

    Imprima "Continue tentando"  

Fim  

```

2\. Foco em Jogos Digitais


Inclui recursos prontos para jogos, permitindo resultados visuais imediatos:

Mover personagem para cima  

Se personagem colidir com inimigo Então  

    Imprima "Game Over"  

Fim  

3\. Sistema de Mutabilidade Explícita

Por padrão, variáveis são constantes (imutáveis). O programador precisa declarar explicitamente quando algo pode mudar.

Guarde Inteiro 10 como constante      # imutável  

Guarde mutável Inteiro 3 como vidas   # mutável  

4\. Regras de Identificadores


A linguagem define identificadores válidos como variáveis que podem seguir dois formatos:

*   camelCase → primeira palavra com minúscula, subsequentes com inicial maiúscula, sem underscores.
    
*   snake\_case → todas as letras minúsculas, separadas por underscores.
    

Regras adicionais:

*   Devem começar com letra minúscula ou underscore.
    
*   Podem conter letras, números e underscores.
    
*   Caracteres especiais e espaços são proibidos.
    

Exemplos válidos:

pontuacao

vidasRestantes

jogador\_1\_nome\_temporario

Exemplos inválidos:

Pontuacao         # começa com maiúscula  

vidas-restantes   # contém caractere especial (-)  

jogador nome      # contém espaço  

5\. Estruturas Verbosas, mas Claras

Comandos são escritos de forma detalhada para aumentar a compreensão.

ParaCada Inteiro i de 1 até 5  

    Imprima "Rodada:", i  

Fim  

6\. Funções Nativas Educacionais

A linguagem oferece funções simples para entrada e conversão de dados:

nome = leia\_entrada()  

idade = para\_inteiro(leia\_entrada())  


**Validação/Teste e Análise de Erros Sintáticos**

 **Exemplos Corretos:**

**Uso correto do condicional**
------------------------------

Guarde mutável Inteiro 85 como pontuacao  

Se pontuacao >= 100 Então  

    Imprima "Você venceu"  

Senão  

    Imprima "Continue tentando"  

Fim  

1.  **Laço com fechamento adequado**
    
```csharp
ParaCada Inteiro i de 1 até 3  

    Imprima "Rodada:", i  

Fim  
```


1.  **Identificadores válidos**
    
```cscharp
vidasRestantes = 3  

pontuacao\_maxima = 100  
```


**Exemplos com Erro e Mensagens Explicativas**

1.  **Erro em condicional (faltou o Então)**
    
```csharp
Se pontuacao >= 100  

    Imprima "Você venceu"  
``` 

Mensagem:

```csharp

Erro de Sintaxe: após a condição de 'Se', você deve escrever 'Então'.  

Exemplo correto: Se pontuacao >= 100 Então  

```

1.  **Erro em laço (faltou o Fim)**
    
```csharp

ParaCada Inteiro i de 1 até 3  

    Imprima i  

```

Mensagem:

```csharp

Erro de Sintaxe: bloco 'ParaCada' precisa terminar com 'Fim'. 

```
 

1.  **Erro em identificador (uso de caractere inválido)**
    
```csharp
Guarde Inteiro 10 como Vidas-Restantes  
```

Mensagem:

```csharp
Erro de Identificador: 'Vidas-Restantes' não é válido.  

Use apenas letras minúsculas, números e underscores.  

Exemplo correto: vidas\_restantes 
``` 

1.  **Erro em identificador (começando com maiúscula)**
    
```csharp

Guarde mutável Inteiro 3 como Pontuacao  

```
Mensagem:

```csharp
Erro de Identificador: 'Pontuacao' não é válido.  

Identificadores devem começar com letra minúscula ou underscore.  

Exemplo correto: pontuacao  
```


**Reflexão:**

**Pergunta:**

Considerem diferentes paradigmas de programação. A linguagem será puramente imperativa? Oferecerá recursos funcionais? Como estes paradigmas afetam a estrutura da gramática? Não tenham medo de experimentar com construtos não convencionais, desde que consigam justificar suas escolhas.

Pensem sobre precedência de operadores e associatividade. Como garantir que expressões como $a + b \* c$ sejam interpretadas corretamente? Como permitir que usuários controlem a ordem de avaliação quando necessário?

**Resposta:**

Nossa linguagem é **principalmente imperativa**, com comandos sequenciais, laços e condicionais, mas também permite o uso de **funções**, dando um toque de programação funcional ao possibilitar retornos de valores e escopo local. Isso influencia a gramática, que precisa lidar com blocos de comandos e expressões de forma recursiva, tornand ela **livre de contexto**.

Para evitar ambiguidades, definimos **precedência e associatividade de operadores**:

*   () — sempre avaliado primeiro, permitindo controlar a ordem.
    
*   Não — negação lógica.
    
*   \* / — multiplicação e divisão.
    
*   \+ - — adição e subtração.
    
*   Comparações (==, !=, <, >, <=, >=).
    
*   E — AND lógico.
    
*   Ou — OR lógico.
    

Os operadores aritméticos e lógicos são **associativos à esquerda**, e comparações não são associativas. Quem programa pode usar **parênteses** para forçar a avaliação na ordem que quiser, garantindo que expressões como a + b \* c funcionem corretamente.

**Definição Completa G = (V, T, P, S)**

**Conjunto de Variáveis(V):**

V={ Programa, Declarações, DeclaraçãoVariáveis, Comandos, Comando, Condicional, LaçoRepetição, Decisão, Função, BlocoComandos, Expressão, Termo, Fator, ExpressãoLógica, OperadorLógico, Tipo, Identificador, Numérico, ParametrosLista, Movimento, EventoJogo }

**Conjunto de Terminais(T):**

T = { Armazene, recebendo, Imprima, Criar, Fim, Talvez, Se, Então, talvez, Então, será, Por, enquanto, Escolha, Se, for, Chega, Verdadeiro, Falso, Inteiro, Flutuante, Logico, Texto, +, -, \*, /, =, ==, !=, <, >, <=, >=, (, ), {, }, ",", ;, identificador, literal\_inteiro, literal\_flutuante, literal\_texto, literal\_booleano }

**Produções(P):**

*   Programa → BlocoDeclaracoes BlocoComandosPrincipal
    
*   BlocoDeclaracoes → Declaracao BlocoDeclaracoes | ε
    
*   Declaracao → DeclaracaoVariavel | DeclaracaoFuncao | DeclaracaoClasse | DeclaracaoEntidade
    
*   DeclaracaoVariavel → Armazene Tipo identificador | Armazene mutável Tipo identificador
    
*   Tipo → Inteiro | Flutuante | Texto | Booleano | Vetor | Sprite | Som
    
*   DeclaracaoFuncao → Funcao Tipo identificador ( ListaParametros ) BlocoComandos Fim
    
*   DeclaracaoClasse → Classe identificador BlocoClasse Fim 
    

                 | Classe identificador herda identificador BlocoClasse Fim

*   DeclaracaoEntidade → Entidade identificador BlocoEntidade Fim
    
*   BlocoComandos → Comando BlocoComandos | ε
    
*   BlocoComandosPrincipal → Comando BlocoComandosPrincipal | ε
    
*   Comando → ComandoAtribuicao | ComandoCondicional | ComandoLaco | ComandoEscrita  | ComandoEntrada | ComandoMovimento | ComandoEvento
    
*   ComandoCondicional → Se Expressao Então BlocoComandos Fim 
    

                   | Se Expressao Então BlocoComandos Senao BlocoComandos Fim

*   ComandoLaco → Enquanto Expressao BlocoComandos Fim | ParaCada Tipo identificador de literal até literal BlocoComandos Fim
    
*   ComandoEscrita → Imprima ListaArgumentos
    
*   ComandoEntrada → Leia identificador
    
*   ComandoMovimento → Mova identificador para ( Expressao , Expressao ) 
    

                  | Rotacione identificador para Expressao graus

*   ComandoEvento → Quando EventoJogo faça BlocoComandos Fim
    
*   EventoJogo → Colisao(identificador, identificador) | TeclaPressionada(tecla) 
    

   | CliqueMouse | Atualizar

*   Expressao → ExpressaoLogica
    
*   ExpressaoLogica → ExpressaoRelacional | ExpressaoLogica E ExpressaoRelaciona l| ExpressaoLogica Ou ExpressaoRelacional | Não ExpressaoLogica
    

*   ExpressaoRelacional → ExpressaoAritmetica 
    

                     | ExpressaoAritmetica == ExpressaoAritmetica 

                     | ExpressaoAritmetica != ExpressaoAritmetica 

                     | ExpressaoAritmetica > ExpressaoAritmetica 

                     | ExpressaoAritmetica < ExpressaoAritmetica 

                     | ExpressaoAritmetica >= ExpressaoAritmetica 

                     | ExpressaoAritmetica <= ExpressaoAritmetica

*   ExpressaoAritmetica → Termo 
    

                     | ExpressaoAritmetica + Termo 

                     | ExpressaoAritmetica - Termo

*   Termo → Fator | Termo \* Fator | Termo / Fator
    
*   Fator → identificador 
    

       | literal\_inteiro 

       | literal\_flutuante

       | literal\_texto 

       | literal\_booleano 

       | ( Expressao ) 

       | identificador ( ListaArgumentos )

*   ListaParametros → Parametro | Parametro , ListaParametros
    
*   Parametro → Tipo identificador
    
*   ListaArgumentos → Expressao | Expressao , ListaArgumentos
    

S =



**Classificação na Hierarquia de Chomsky**

**Análise Formal da Classificação**

Ao analisarmos a gramática da linguagem de programação a ser criada classificamos como Tipo 2 (Livre de Contexto) na hierarquia de Chomsky.

**Justificativa técnica**

*   Produções: todas seguem A → α.
    
*   Não-terminais aninhados: permitem expressões e comandos recursivos, o que não é regular, mas é livre de contexto.
    
*   Palavras-chave compostas (ex: "Talvez se", "Então talvez") e uso de acentos não alteram a classificação, pois são tratados no nível lexical, antes da análise sintática.
    
*   Estruturas de controle e ECS/Clean Architecture: dependem do contexto de projeto, mas não mudam a gramática formal, só influenciam o léxico e o conjunto de palavras-chave disponíveis.
    

**Verificação**

*   Não precisa ser do tipo 0 (irrestrita), pois a gramática é bem estruturada, sem necessidade de regras arbitrárias.
    
*   Não precisa ser do tipo 1(sensível ao contexto), como **αAβ → αγβ**, porque a substituição de símbolos não depende do contexto.
    
*   A gramática possui estruturas aninhadas, como **Se ... Fim** dentro de blocos, laços e funções, que não podem ser expressas por gramáticas regulares (tipo 3)
    



**Limitações da Gramática e Análise Semântica**

O compilador que estamos desenvolvendo para a nossa nova linguagem de programação tem limitações, mas são desafios que podem ser superados. Nossa gramática, por ser livre de contexto, é ótima para validar a estrutura básica do código.Mas, ela não consegue "lembrar" do que já foi declarado antes.

É aí que entra a **análise semântica**. Essa etapa funciona como um assistente de memória para o compilador. Ela constrói uma **tabela de símbolos** para manter um registro de tudo que foi declarado no código, como variáveis e funções. Com isso, o compilador consegue garantir que você declare algo antes de usá-lo, evitando erros.

### Desafios e soluções de design

**Ambiguidade em palavras-chave**

Palavras-chave compostas como "**Talvez se**" ou "**Então será**" podem causar confusão para o analisador léxico. A solução que estamos adotando é diferenciá-las visualmente ou usar prefixos, como **KW\_TalvezSe**, ou simplesmente garantir que as palavras-chave nunca coincidam com os identificadores que você pode usar para suas variáveis.

**Acentos e caracteres especiais**

A linguagem foi pensada para se aproximar do português, aceitando caracteres como **ç**, **ã**, e **é**. Para evitar problemas de compatibilidade entre diferentes sistemas operacionais, a gente garante que o compilador use sempre a codificação **UTF-8**, que é um padrão global e mantém a consistência.

**Estruturas de controle complexas**

As palavras-chave para o controle de fluxo podem ser parecidas, dificultando a leitura. Uma alternativa para melhorar a clareza seria simplificar a sintaxe para algo como "**Se**", "**Senão**", ou "**Caso contrário**". Além disso, podemos encorajar o uso de indentação para deixar a estrutura do código mais clara visualmente.

**Dependência de contexto arquitetural**

Palavras-chave como "**Entidade**" ou "**Componente**" são comuns em arquiteturas específicas, como a **ECS**. Para que a linguagem seja flexível, a análise léxica é configurável, e pode se adaptar ao contexto do seu projeto. Assim, a linguagem pode ser útil tanto para um game dev usando ECS quanto para um engenheiro de software usando **Clean Architecture**.

### O que a gramática faz e o que a semântica faz

A divisão entre o que é de responsabilidade da **sintaxe** e o que é de responsabilidade da **semântica** é uma decisão de design. A gramática cuida da **estrutura** do código, enquanto a análise semântica foca nas **regras** contextuais, como:

*   **Declaração antes do uso**: Uma variável tem que ser declarada antes de ser utilizada.
    
*   **Tipagem de variáveis**: Cada variável tem um tipo específico.
    
*   **Escopo de funções**: Variáveis declaradas em uma função só existem dentro dela.
    
*   **Parâmetros de funções**: A quantidade de argumentos passados em uma chamada de função tem que ser a mesma que a função espera.
    


**Ambiguidades Tratadas na Linguagem Educacional para Jogos**

**1\. Problema do “Dangling-Else”**

```csharp
Se condicao1 Então

    Talvez Se condicao2 Então

        comando1

Então Será   # A qual Se/Talvez Se pertence?

    comando2

Fim
```

**Solução:** Regra de associação — Então Será sempre se associa ao Se ou Talvez Se mais próximo não pareado.

### 2\. Precedência de Operadores: Hierarquia clara para a linguagem:

1.  () - Parênteses
    
2.  Não - Negação lógica
    
3.  \* / - Multiplicação e divisão
    
4.  \+ - - Adição e subtração
    
5.  \== != > < >= <= - Comparações
    
6.  E - AND lógico
    
7.  Ou - OR lógico
    

### 3\. Associatividade:

*   Operadores aritméticos: associativos à esquerda
    
*   Operadores lógicos: associativos à esquerda
    
*   Comparações: não associativas 
    


## Exemplos de Derivação

### Programa:

```csharp
Armazene Inteiro pontuacao recebendo 85

Se pontuacao >= 100 Então

    Imprima "Você venceu"

Senao

    Imprima "Continue tentando"

Fim
```

### Derivação:

Programa

→ BlocoDeclaracoes BlocoComandosPrincipal

→ Declaracao BlocoDeclaracoes BlocoComandosPrincipal

→ DeclaracaoVariavel BlocoDeclaracoes BlocoComandosPrincipal

→ Armazene Tipo identificador recebendo literal\_inteiro BlocoDeclaracoes BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 BlocoDeclaracoes BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 Comando BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 ComandoCondicional BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 Se Expressao Então BlocoComandos Senao BlocoComandos Fim BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 Se ExpressaoRelacional Então BlocoComandos Senao BlocoComandos Fim BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 Se ExpressaoAritmetica >= ExpressaoAritmetica Então BlocoComandos Senao BlocoComandos Fim BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 Se identificador >= literal\_inteiro Então BlocoComandos Senao BlocoComandos Fim BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 Se pontuacao >= 100 Então Comando Senao BlocoComandos Fim BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 Se pontuacao >= 100 Então ComandoEscrita Senao BlocoComandos Fim BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 Se pontuacao >= 100 Então Imprima ListaArgumentos Senao BlocoComandos Fim BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 Se pontuacao >= 100 Então Imprima "Você venceu" Senao Comando BlocoComandos Fim BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 Se pontuacao >= 100 Então Imprima "Você venceu" Senao Imprima "Continue tentando" Fim

### Derivação Expandida:

Programa

Programa → BlocoDeclaracoes BlocoComandosPrincipal

→ Declaracao BlocoDeclaracoes BlocoComandosPrincipal

→ DeclaracaoVariavel BlocoDeclaracoes BlocoComandosPrincipal

→ Armazene Tipo identificador recebendo literal\_inteiro BlocoDeclaracoes BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 BlocoDeclaracoes BlocoComandosPrincipal

(termina as declarações) ⇒ Armazene Inteiro pontuacao recebendo 85 Comando BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 ComandoCondicional BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 Se Expressao Então BlocoComandos Senao BlocoComandos Fim BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 Se ExpressaoRelacional Então BlocoComandos Senao BlocoComandos Fim BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 Se ExpressaoAritmetica >= ExpressaoAritmetica Então BlocoComandos Senao BlocoComandos Fim BlocoComandosPrincipal→ Armazene Inteiro pontuacao recebendo 85 Se identificador >= literal\_inteiro Então BlocoComandos Senao BlocoComandos Fim BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 Se pontuacao >= 100 Então Comando Senao BlocoComandos Fim BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 Se pontuacao >= 100 Então ComandoEscrita Senao BlocoComandos Fim BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 Se pontuacao >= 100 Então Imprima ListaArgumentos Senao BlocoComandos Fim BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 Se pontuacao >= 100 Então Imprima Expressao Senao BlocoComandos Fim BlocoComandosPrincipal

\-> Armazene Inteiro pontuacao recebendo 85 Se pontuacao >= 100 Então Imprima "Você venceu" Senao BlocoComandos Fim BlocoComandosPrincipal

Agora desenvolvendo o bloco Senao BlocoComandos:→ Armazene Inteiro pontuacao recebendo 85 Se pontuacao >= 100 Então Imprima "Você venceu" Senao Comando BlocoComandos Fim BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 Se pontuacao >= 100 Então Imprima "Você venceu" Senao ComandoEscrita BlocoComandos Fim BlocoComandosPrincipal

→ Armazene Inteiro pontuacao recebendo 85 Se pontuacao >= 100 Então Imprima "Você venceu" Senao Imprima ListaArgumentos Fim BlocoComandosPrincipal

→Armazene Inteiro pontuacao recebendo 85 Se pontuacao >= 100 Então Imprima "Você venceu" Senao Imprima "Continue tentando" Fim


## Características Específicas da Linguagem

1\. Sintaxe Natural em Português

A linguagem adota comandos claros em português, para que o aluno foque na lógica e não em termos técnicos em inglês.

*   Exemplo:
    
```csharp

Guarde mutável Inteiro 85 como pontuacao  

Se pontuacao >= 100 Então  

    Imprima "Você venceu"  

Senão  

    Imprima "Continue tentando"  

Fim  
```

### 2\. Foco em Jogos Digitais

Inclui recursos prontos para jogos, permitindo resultados visuais imediatos:

```csharp
Mover personagem para cima  

Se personagem colidir com inimigo Então  

    Imprima "Game Over"  

Fim  
```

### 3\. Sistema de Mutabilidade Explícita

Por padrão, variáveis são constantes (imutáveis). O programador precisa declarar explicitamente quando algo pode mudar.

```csharp
Guarde Inteiro 10 como constante      # imutável  

Guarde mutável Inteiro 3 como vidas   # mutável 
``` 

### 4\. Regras de Identificadores


A linguagem define identificadores válidos como variáveis que podem seguir dois formatos:

*   camelCase → primeira palavra com minúscula, subsequentes com inicial maiúscula, sem underscores.
    
*   snake\_case → todas as letras minúsculas, separadas por underscores.
    

Regras adicionais:

*   Devem começar com letra minúscula ou underscore.
    
*   Podem conter letras, números e underscores.
    
*   Caracteres especiais e espaços são proibidos.
    

Exemplos válidos:

```csharp
pontuacao

vidasRestantes

jogador\_1\_nome\_temporario

```

Exemplos inválidos:
```csharp
Pontuacao         # começa com maiúscula  

vidas-restantes   # contém caractere especial (-)  

jogador nome      # contém espaço 

```

### 5\. Estruturas Verbosas, mas Claras


Comandos são escritos de forma detalhada para aumentar a compreensão.

```csharp

ParaCada Inteiro i de 1 até 5  

    Imprima "Rodada:", i  

Fim  
```


###  6\. Funções Nativas Educacionais

A linguagem oferece funções simples para entrada e conversão de dados:


nome = leia\_entrada()  

idade = para\_inteiro(leia\_entrada())  

***

## Validação/Teste e Análise de Erros Sintáticos**


 **Exemplos Corretos:**

**Uso correto do condicional**

```csharp
Guarde mutável Inteiro 85 como pontuacao  

Se pontuacao >= 100 Então  

    Imprima "Você venceu"  

Senão  

    Imprima "Continue tentando"  

Fim  
```

1.  **Laço com fechamento adequado**
    
```Csharp
ParaCada Inteiro i de 1 até 3  

    Imprima "Rodada:", i  

Fim  
```

2.  **Identificadores válidos**
    
```Csharp
vidasRestantes = 3  

pontuacao\_maxima = 100  
```

## Exemplos com Erro e Mensagens Explicativas

1.  **Erro em condicional (faltou o Então)**
    
```Csharp
Se pontuacao >= 100  

    Imprima "Você venceu"  
```


Mensagem:

```Csharp
Erro de Sintaxe: após a condição de 'Se', você deve escrever 'Então'.  

Exemplo correto: Se pontuacao >= 100 Então  
```

2.  **Erro em laço (faltou o Fim)**
    
```Csharp
ParaCada Inteiro i de 1 até 3  

    Imprima i  
```

Mensagem:

```Csharp
Erro de Sintaxe: bloco 'ParaCada' precisa terminar com 'Fim'.  
```

3.  **Erro em identificador (uso de caractere inválido)**
    
```Csharp
Guarde Inteiro 10 como Vidas-Restantes  
```
Mensagem:

```Csharp
Erro de Identificador: 'Vidas-Restantes' não é válido.  

Use apenas letras minúsculas, números e underscores.  

Exemplo correto: vidas\_restantes  
```

4.  **Erro em identificador (começando com maiúscula)**
    
```Csharp
Guarde mutável Inteiro 3 como Pontuacao  
```

Mensagem:

```Csharp
Erro de Identificador: 'Pontuacao' não é válido.  

Identificadores devem começar com letra minúscula ou underscore.  

Exemplo correto: pontuacao  
```

***

**Reflexão:**

**Pergunta:**

Considerem diferentes paradigmas de programação. A linguagem será puramente imperativa? Oferecerá recursos funcionais? Como estes paradigmas afetam a estrutura da gramática? Não tenham medo de experimentar com construtos não convencionais, desde que consigam justificar suas escolhas.

Pensem sobre precedência de operadores e associatividade. Como garantir que expressões como $a + b \* c$ sejam interpretadas corretamente? Como permitir que usuários controlem a ordem de avaliação quando necessário?

**Resposta:**

Nossa linguagem é **principalmente imperativa**, com comandos sequenciais, laços e condicionais, mas também permite o uso de **funções**, dando um toque de programação funcional ao possibilitar retornos de valores e escopo local. Isso influencia a gramática, que precisa lidar com blocos de comandos e expressões de forma recursiva, tornand ela **livre de contexto**.

Para evitar ambiguidades, definimos **precedência e associatividade de operadores**:

*   () — sempre avaliado primeiro, permitindo controlar a ordem.
    
*   Não — negação lógica.
    
*   \* / — multiplicação e divisão.
    
*   \+ - — adição e subtração.
    
*   Comparações (==, !=, <, >, <=, >=).
    
*   E — AND lógico.
    
*   Ou — OR lógico.
    

Os operadores aritméticos e lógicos são **associativos à esquerda**, e comparações não são associativas. Quem programa pode usar **parênteses** para forçar a avaliação na ordem que quiser, garantindo que expressões como a + b \* c funcionem corretamente.