*   Especificação completa usando expressões regulares
    

**1\. Palavras-chave**
----------------------

**Descrição**: Conjunto fixo de termos reservados da linguagem. **Regex**:

(Programa|Armazene|Guarde|mutável|Inteiro|Se|Então|Senão|Fim|ParaCada|Imprima|leia\_entrada|para\_inteiro|E|Ou|Não)

**Observação**: Sempre têm **precedência sobre identificadores**.

**2\. Identificadores**
-----------------------

**Descrição**: Nomes de variáveis e funções. Aceitam camelCase ou snake\_case. **Regex**:

(\[a-z\]\[a-zA-Z0-9\]\*|\_\[a-z0-9\_\]+)

Válidos: pontuacao, vidasRestantes, jogador\_1\_nome.

Inválidos: Pontuacao, vidas-restantes, jogador nome.

**3\. Números inteiros**
------------------------

**Descrição**: Sequência de dígitos. **Regex**:

\\d+

Válidos: 0, 42, 100

Inválidos: 3.14, 1a

**4\. Cadeias de texto (Strings)**
----------------------------------

**Descrição**: Texto entre aspas duplas, com escapes opcionais. **Regex**:

"(?:\[^"\\\\\]|\\\\.)\*"

 Válidos: "Você venceu", "linha\\qlquebrada"

 Inválidos: 'Texto', "Incompleto

**5\. Operadores**

**5.1 Aritméticos**

**Regex:** \[+\\-\*/\]

**5.2 Relacionais (priorizar compostos)**

**Regex:** (== | != | <= | >= | < | >)

**5.3 Lógicos**

Regex: (E|Ou|Não)

**6\. Delimitadores e Símbolos**

**Regex:** \[\] () {} , :

**7\. Espaços e Comentários**
-----------------------------

### **Espaços em branco**

**Ignorados, mas devem manter contagem de linha/coluna:**

**Regex:** 

\\t \\r \\ql

**Comentários (estilo linha)**

**Regex:** 

\\# .\*

*    **ERRO LÉXICO**, exibindo mensagem clara:
    

Erro: caractere inválido '@' na linha 3, coluna 15.

Sugestão: remova ou substitua o caractere.

*   Análise de ambiguidades e regras de resolução
    

**1\. Palavras-chave vs Identificadores**
-----------------------------------------

*   **Problema**: a regex de identificadores (\[a-z\]\[a-zA-Z0-9\]\*|\_\[a-z0-9\_\]+) pode reconhecer também palavras-chave como Se, Fim, Imprima.
    

*   **Exemplo**:
    
    *   Código: Se x > 0 Então
        
    *   Lexer sem precedência poderia gerar IDENTIFICADOR("Se") ao invés de PALAVRA\_CHAVE("Se").
        
*   **Solução**:
    
    *   **Regra de precedência**: palavras-chave sempre têm prioridade sobre identificadores.
        
    *   **Implementação prática**: testar regex de palavras-chave **antes** da de identificadores no analisador léxico.
        

**2\. Operadores relacionais compostos vs simples**
---------------------------------------------------

*   **Problema**: <= poderia ser reconhecido como < seguido de = se não houver prioridade.
    
*   **Exemplo**:
    
    *   Código: a <= b
        
    *   Lexer mal implementado poderia gerar OP\_REL('<') + ATRIB('=').
        
*   **Solução**:
    
    *   **Regra de maximal munch (casamento mais longo)**: sempre reconhecer o token mais longo possível.
        
    *   <= deve ser reconhecido como um único token OP\_REL("<= ").
        

**3\. Strings mal formadas**
----------------------------

*   **Problema**: regex de string ("(?:\[^"\\\\\]|\\\\.)\*") pode falhar se não encontrar aspas de fechamento.
    
*   **Exemplo**:
    
    *   Código: "Texto sem fim
        
    *   Resultado esperado: erro léxico claro.
        
*   **Solução**:
    
    *   Criar **estado especial no analisador léxico**: se chegar ao fim da linha/arquivo sem aspas de fechamento, emitir ERRO\_LEXICO("String não fechada").
        

**4\. Números vs Identificadores iniciados com dígito**
-------------------------------------------------------

*   **Problema**: regex de identificadores não permite dígito inicial, mas alunos podem escrever 2vidas.
    
*   **Exemplo**:
    
    *   Código: 2vidas = 3
        
    *   O lexer poderia quebrar em NUM(2) + IDENT("vidas").
        
*   **Solução**:
    
    *   Aceitar isso como **erro semântico** ("identificador não pode começar com número").
        
    *   Ou, no léxico, sinalizar como ERRO\_LEXICO("identificador inválido").
        

**5\. Comentário vs operador “#” inválido**
-------------------------------------------

*   **Problema**: comentário é #.\*, mas se o usuário escrever só # sem texto, é válido?
    
*   **Exemplo**:
    
    *   Código: #
        
    *   Regex ainda captura, mas semanticamente não é comentário útil.
        
*   **Solução**:
    
    *   Aceitar como comentário válido (vazio), mas documentar.
        
    *   Opcionalmente: emitir aviso (“comentário vazio”).
        

**6\. Espaços em branco**
-------------------------

*   **Problema**: espaço em branco não deve gerar token, mas precisa ser rastreado para linha/coluna.
    
*   **Solução**:
    
    *   O analisador léxico **descarta** espaços/tabs/newlines, mas **conta** para posição de erro.
        

**7\. Caracteres inválidos**
----------------------------

*   **Problema**: qualquer coisa fora da gramática (@, $, ç em identificadores, etc.).
    
*   **Solução**:
    
    *   Regra final de captura (.) classifica como ERRO\_LEXICO.
        
    *   Mensagem útil: Erro Léxico: caractere inválido '@' na linha 4, coluna 7
        

*   Estratégia para tratamento de erros léxicos
    

1.  **Detecção imediata**
    
    *   Se o analisador encontrar um caractere ou sequência que **não corresponde a nenhum token válido**, gera um erro léxico.
        
    *   Exemplo: @, 2abc, "texto sem fim.
        

1.  **Mensagem clara e útil**
    
    *   O erro deve indicar:
        
        *   O **tipo de erro** (caractere inválido, string não fechada, etc.)
            
        *   A **linha e coluna** onde ocorreu.
            

Exemplo de saída: **Erro léxico na linha 3, coluna 8: caractere inválido '@'.**

1.  **Recuperação simples (pânico controlado)**
    
    *   O analisador **ignora o caractere inválido** e continua a leitura do código, para não parar na primeira falha.
        
    *   Em caso de **strings não fechadas**, pode descartar até encontrar aspas ou fim da linha.
        

1.  **Registro de erros**
    
    *   Todos os erros encontrados são armazenados em uma **lista de erros léxicos**.
        

Ao final da análise, o compilador exibe o relatório completo: 2 erros encontrados:

Linha 3, coluna 8: caractere inválido '@'

Linha 7, coluna 15: string não fechada

1.  **Política de continuação**
    
    *   Mesmo com erros léxicos, o analisador tenta seguir em frente para gerar a lista completa de erros.
        
    *   Isso evita frustração do programador, que não precisa corrigir **um erro por vez**.
        

**Detectar → Reportar → Ignorar/Descartar → Continuar → Listar erros no final.**

*   **Primeiros esboços de mensagens de erro para usuários**
    

**vida@ = 100**

**Entrada: \[Erro\] Linha 1, coluna 5: caractere inválido '@'.**

**Dica: use apenas letras, números e '\_' em identificadores.**

**Entrada: mensagem = "Você venceu**

**\[Erro\] Linha 1, coluna 12: string não foi fechada.**

**Dica: adicione aspas no final da string.**

**Entrada: tempo = 12.34.56**

**\[Erro\] Linha 1, coluna 13: número mal formado '12.34.56'.**

**Dica: use apenas um ponto decimal em números reais (ex: 12.34).**

**Entrada: 2vidas = 3**

**\[Erro\] Linha 1, coluna 1: identificador inválido '2vidas'.**

**Dica: identificadores devem começar com letra ou '\_'.**

**Entrada: /\* comentário sem fechar**

**\[Erro\] Linha 1, coluna 1: comentário não foi fechado.**

**Dica: adicione '\*/' para finalizar o comentário.**

**Entrada: resultado = 5 $ 3**

**\[Erro\] Linha 1, coluna 15: símbolo '$' não é reconhecido.**

**Dica: verifique operadores válidos (+, -, \*, /, etc.).**

**Entrada: jogador.vida = 100**

**\[Erro\] Linha 1, coluna 8: caractere '.' inválido em identificador.**

**Dica: use '\_' em vez de '.' (ex: jogador\_vida).**