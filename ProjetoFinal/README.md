# Documentação do Compilador da Linguagem Educacional

## 1. Introdução

Este projeto consiste na implementação de um compilador para uma linguagem imperativa simples, desenvolvido com fins educacionais como projeto final da disciplina de Compiladores / Linguagens e Autômatos. O objetivo principal foi compreender, na prática, todas as etapas envolvidas no processo de compilação, desde a leitura do código-fonte até a geração de código intermediário.

O compilador implementado realiza as seguintes fases:

* Análise léxica
* Análise sintática (construção da AST)
* Análise semântica
* Geração de código intermediário no formato LLVM IR

A linguagem criada foi projetada deliberadamente de forma simples, priorizando clareza e correção conceitual em vez de complexidade.

---

## 2. Visão geral da linguagem

A linguagem desenvolvida é uma linguagem imperativa, com tipagem estática e sintaxe inspirada em linguagens tradicionais, porém simplificada.

### 2.1 Características principais

* Programas compostos por funções
* Tipos inteiros
* Função principal `main` obrigatória
* Retorno explícito de valores inteiros
* Expressões aritméticas com constantes

### 2.2 Estrutura básica de um programa

Todo programa válido deve conter a função `main`, que representa o ponto de entrada da execução:

```lang
CRIAR INTEIRO main() {
    RETORNE 0
}
```

---

## 3. Manual de instalação

Esta seção descreve, de forma detalhada, como preparar o ambiente e compilar o projeto, mesmo para usuários sem experiência prévia.

### 3.1 Requisitos do sistema

* Java Development Kit (JDK) versão 17 ou superior
* Sistema operacional Windows, Linux ou macOS
* Terminal de comandos (Prompt de Comando, PowerShell ou Terminal)

### 3.2 Estrutura do projeto

O projeto está organizado da seguinte forma:

```
ProjetoFinal/
├── src/
│   ├── lexer/     # Analisador léxico
│   ├── parser/    # Parser e AST
│   ├── sema/      # Analisador semântico
│   ├── llvm/      # Gerador de código LLVM IR
│   └── main/      # Classe principal do compilador
├── examples/      # Programas de exemplo na linguagem criada
├── out/           # Arquivos .class gerados na compilação
```

### 3.3 Compilação do compilador

Para compilar o compilador, abra o terminal na pasta raiz do projeto e execute o seguinte comando:

```bash
javac -encoding UTF-8 -d out src/main/*.java src/lexer/*.java src/parser/*.java src/sema/*.java src/llvm/*.java
```

Após a execução bem-sucedida do comando, os arquivos compilados (`.class`) estarão disponíveis na pasta `out`.

---

## 4. Manual de utilização

Nesta seção é explicado como utilizar o compilador para compilar um programa escrito na linguagem criada.

### 4.1 Compilando um programa

Para compilar um arquivo `.lang`, execute o comando:

```bash
java -cp out main.Main examples/hello.lang
```

Onde:

* `main.Main` é a classe principal do compilador
* `examples/hello.lang` é o arquivo de entrada escrito na linguagem criada

### 4.2 Arquivo de saída

Após a execução do compilador, será gerado um arquivo chamado `a.ll`, que contém o código intermediário no formato LLVM IR.

Exemplo de saída simplificada:

```llvm
define i32 @main() {
  ret i32 0
}
```

---

## 5. Exemplos de testes disponíveis

O projeto acompanha exemplos funcionais localizados na pasta `examples`, utilizados para validação do compilador:

| Arquivo        | Descrição                               |
| -------------- | --------------------------------------- |
| hello.lang     | Programa mínimo com retorno 0           |
| soma.lang      | Retorno de expressão aritmética simples |
| operacoes.lang | Combinação de operações aritméticas     |

Esses arquivos permitem verificar o funcionamento correto do analisador léxico, sintático, semântico e do gerador de código.

---

## 6. Etapas do compilador

### 6.1 Analisador Léxico

O analisador léxico realiza a leitura do código-fonte e o transforma em uma sequência de tokens, identificando palavras-chave, identificadores, operadores e literais.

### 6.2 Analisador Sintático

O analisador sintático valida a estrutura do programa conforme a gramática definida e constrói a Árvore Sintática Abstrata (AST), que representa o programa de forma hierárquica.

### 6.3 Analisador Semântico

O analisador semântico verifica regras que vão além da gramática, como:

* Existência da função `main`
* Tipos compatíveis
* Uso correto das construções da linguagem

### 6.4 Gerador de Código LLVM IR

A AST validada é traduzida para código LLVM IR. O gerador atual traduz corretamente:

* Função `main`
* Expressões aritméticas com constantes
* Retornos inteiros

---

## 7. Visualização e validação do código LLVM

Para facilitar a visualização e validação do código LLVM IR gerado, recomenda-se o uso do site [https://godbolt.org/](https://godbolt.org/)

Nesse site é possível:

* Colar o conteúdo do arquivo `a.ll`
* Visualizar o LLVM IR de forma destacada
* Compilar e analisar o código gerado
* Comparar com outras representações intermediárias

Essa ferramenta auxilia na compreensão do funcionamento do backend do compilador.

---

## 8. Limitações conhecidas

Na versão atual, o gerador de código LLVM IR não traduz algumas construções reconhecidas nas etapas anteriores do compilador, como:

* Estruturas condicionais (`if`)
* Laços (`while`)
* Chamada de funções

Essas construções são aceitas pelo analisador léxico, sintático e semântico, porém não são emitidas no código LLVM nesta versão.

---

## 9. Conclusão

O projeto atingiu os objetivos propostos pela tarefa, permitindo a implementação completa de um pipeline de compilação funcional. Apesar das limitações no backend LLVM, todas as etapas fundamentais de um compilador foram implementadas e integradas, proporcionando uma visão prática e concreta do funcionamento de um compilador real.
