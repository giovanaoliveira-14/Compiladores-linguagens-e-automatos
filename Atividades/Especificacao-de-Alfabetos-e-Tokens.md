# 📘 Especificação Inicial da Linguagem — Semana 2  
*(Linguagem Educacional para Jogos)*  

## Alfabeto da Linguagem
O alfabeto da nossa linguagem, inclui: 

- Σletrasminusculas = {a,b,c,d...,z} (26 letras)
- Σletrasmaiusculas = {A,B,C,D...,Z} (26 letras)
- Σdigitos = {0,1,2,3,4,5,6,7,8,9} (10 números)
- Σacentosminusculos = {ã, õ, ç, á, é, í, ó, ú}
- Σacentosmaiusculos = {Ã, Õ, Ç, Á, É, Í, Ó, Ú}
- Σoperadores = {+, -, *, /, =, <, >, !, &, ?, |, ^, %, <=, >=, ==, !=}

Obs: Será aceito todos os tipos de acento para que a linguagem se assemelhe à língua portuguesa.

## Definição de Tokens

**Identificadores**:

Identificadores = (Identificadores = { Letrasminusculas | _ } { Letraminusculas | Letramaiusculas | Digitos | _ }*)

A regra define identificadores válidos como variáveis que podem seguir dois formatos: camelCase, onde a primeira palavra começa com letra minúscula e as subsequentes com letra maiúscula sem underscores, e snake_case, onde todas as palavras são minúsculas e separadas por underscores. O identificador pode conter letras, números e underscores, e deve começar com uma letra minúscula ou um underscore. Caracteres especiais e espaços são proibidos.

**Palavras-chave**:

PalavrasChave = {Talvez se, Então talvez, Então será, Por enquanto, Receba, Criar, Imprima, Falso, Verdadeiro, Chega, Para cada, Escolha, Se for, ... }

**Literais Númericos**:

Inteiros = Digitos+
Decimais = Digitos+ . {.} . Digitos+
NumerosValidos = Inteiros U Decimais

**Comentários**

ComentariosLinha = ``` { -> } · (Σdidatica - {newline})* · {newline} ```
ComentariosBlocos = ``` { -> } · (Σdidatica)* · { <- } ```

**Declaração de Variáveis**

DeclaracaoVar = {Armazene}.Tipos.Identificadores.{recebendo}.Identificadores
Tipos = {Inteiro, Flutuante, Logico, Texto, ...}

**Estruturas de Controle**

EstruturaCondicional = {Talvez Se}.ExpressaoLogica.BlocoComandos.({Então Talvez}.ExpressaoLogica.BlocoComando)?.({Então Será}.BlocoComandos)?

EstruraRepeticao = {Por Enquanto}.ExpressaoLogica.BlocoComandos

EstruturaDecisao = {Escolha}.Identificadores.{newline}.{Se For}.BlocoComandos.{newline}.{Chega}.{newline}...

**Definição de Funções**

DefinicaoFuncao = {Criar}.Tipos.Identificadores.{(}.ParametrosList.{)}.{newline}.BlocoComandos.{newline}.{Fim}

**Formatação**:  
    - Espaço em branco: (` `)
    - Quebra de linha: (`\ql`)

**Movimentação do jogo**:
    - `↑`, `←`, `↓`, `→`
    - `w (cima)`, `a (esquerda)`, `s (baixo)`, `d (direita)`


## Ambiguidades Léxicas e Estratégias de Resolução

**Ambiguidade nas Palavras-chave e Identificadores**

Algumas palavras-chave podem gerar ambiguidade com identificadores, devido ao uso de expressões compostas. Por exemplo, termos como "Talvez se", "Então talvez", "Então será" e "Escolha" (equivalentes a if, else if, else e switch, respectivamente) são formados por mais de uma palavra, o que pode gerar confusão ao serem utilizados no código.

**Solução:** Para evitar essa ambiguidade, pode-se adotar uma convenção de nomenclatura que torne as palavras-chave mais distintas de identificadores, como escrever as palavras-chave sempre em maiúsculas ou prefixá-las com algo como "kw_". Isso facilitaria a identificação das palavras-chave no código e minimizaria o risco de confusão.

**Dificuldade com o Suporte a Acentos e Caracteres Especiais**

A linguagem permite o uso de acentos e caracteres especiais no alfabeto, como ç, ã, é e í, para se aproximar da língua portuguesa. No entanto, isso pode causar problemas ao compilar e interpretar o código, especialmente se a codificação de caracteres não for bem definida ou se o suporte a esses caracteres não for consistente entre diferentes plataformas.

**Solução:** Para garantir a compatibilidade e evitar erros, é importante garantir que a linguagem use uma codificação de caracteres sólida e amplamente suportada, como o padrão UTF-8, que permite o uso de caracteres especiais de forma consistente em diferentes sistemas operacionais.

**Ambiguidade no Controle de Fluxo e Estruturas de Controle**

As palavras-chave usadas para controle de fluxo, como "talvez se", "então talvez" e "então será", embora intuitivas, são muito semelhantes entre si e podem gerar confusão ao serem usadas em expressões complexas de controle de fluxo. Isso pode dificultar a leitura e a compreensão do código, especialmente para desenvolvedores iniciantes.

**Solução:** Uma maneira de melhorar a legibilidade seria simplificar a sintaxe, adotando palavras-chave mais curtas e diretas, como "se", "senão" e "caso contrário", o que tornaria o código mais claro e reduziria o risco de erros.

### Hash Table para Palavras-chave:
```csharp
palavrasReservadas = {
    "Talvez se",
    "Então talvez",
    "Então será",
    "Por enquanto", 
    "Receba", 
    "Criar", 
    "Imprima", 
    "Falso", 
    "Verdadeiro", 
    "Chega", 
    "Para cada", 
    "Escolha",
    "Se for"
};
```

### Conexões com Princípios Arquiteturais

**Verificação de Padrões na Análise Lexical**

Uma característica da nossa linguagem é como padrões arquiteturais influenciam até mesmo a análise lexical. Por exemplo, quando o padrão ECS (Entity-Component-System) é declarado, certas palavras-chave se tornam disponíveis:

- Palavras-chave Condicionais (disponíveis apenas com o padrão ECS):
- PalavrasChaveECS = {Entidade, Componente, Sistema, Atualizar, Processar}
- PalavrasChaveECS={Entidade,Componente,Sistema,Atualizar,Processar}

**Palavras-chave para Clean Architecture (podem se combinar com ECS)**:
- PalavrasChaveClean = {Entidade, CasoDeUso, Gateway, Adaptador}
- PalavrasChaveClean={Entidade,CasoDeUso,Gateway,Adaptador}

Isso significa que o conjunto de palavras reservadas não é fixo, mas depende do contexto arquitetural declarado no projeto. Matematicamente, temos:

- PalavrasReservadasCompletas = PalavrasBase ∪ PalavrasArquiteturais
- PalavrasReservadasCompletas = PalavrasBase ∪ PalavrasArquiteturais

onde **PalavrasArquiteturais** varia de acordo com a configuração do projeto. Essa dependência contextual adiciona complexidade interessante ao analisador léxico — ele precisa ser configurável baseado nas declarações arquiteturais do projeto.




