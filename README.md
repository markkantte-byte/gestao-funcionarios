# Sistema de Gerenciamento de Funcionários e Folha de Pagamento

Atividade avaliativa de Programação Orientada a Objetos em Java.
Sistema de console para cadastrar funcionários de diferentes categorias, consultar dados,
calcular remunerações e gerar o relatório/total da folha de pagamento.

## Tecnologias

- Java 17 ou superior
- Maven
- JUnit 5
- Armazenamento em memória (sem banco de dados)

## Funcionalidades

- Cadastrar, listar, buscar (por código, CPF ou nome) e remover funcionários
- Calcular a remuneração de cada funcionário
- Exibir os dados completos de um funcionário
- Gerar relatório com todos os funcionários e suas remunerações
- Calcular o total da folha de pagamento

## Arquitetura (camadas)

```
src/main/java/
 ├── Main.java
 ├── model/        Funcionario (abstrata), FuncionarioCLT, FuncionarioPJ,
 │                 FuncionarioComissionado, Tributavel, Identificavel
 ├── repository/   FuncionarioRepository (armazenamento em memória)
 ├── service/      FuncionarioService (regras de negócio)
 ├── exception/    FuncionarioNaoEncontradoException, FuncionarioJaCadastradoException
 └── view/         FuncionarioView (menu de console)

src/test/java/     uma classe de teste para cada classe de produção
```

| Camada     | Responsabilidade                                              |
|------------|---------------------------------------------------------------|
| model      | Entidades e regras de remuneração de cada categoria           |
| repository | Guarda e recupera funcionários (`LinkedHashMap`)              |
| service    | Regras de negócio e coordenação das operações                 |
| view       | Interação com o usuário; trata e exibe as exceções            |
| exception  | Exceções personalizadas do sistema                            |

## Regras de remuneração

| Categoria        | Dados                                           | Remuneração                                   | Tributos (`Tributavel`)   |
|------------------|-------------------------------------------------|-----------------------------------------------|---------------------------|
| `FuncionarioCLT` | salário-base, adicional de benefícios           | salário-base + adicional                      | 11% da remuneração        |
| `FuncionarioPJ`  | valor por projeto, quantidade de projetos       | valor por projeto × quantidade                | não implementa            |
| `FuncionarioComissionado` | salário-base, percentual (0 a 100), total de vendas | salário-base + (vendas × percentual / 100) | 15% da remuneração |

Observações sobre decisões de projeto:

- O enunciado não define alíquotas; os percentuais de tributos acima são uma simplificação
  (constantes privadas nas classes) e podem ser alterados facilmente.
- `FuncionarioPJ` não implementa `Tributavel`, pois o prestador PJ recolhe os próprios impostos.
- O PJ não possui salário-base; internamente o valor fica em `0.0`.
- O comissionado recebe também o **total de vendas** do período, necessário para aplicar o percentual.
- O CPF é validado (formato e dígitos verificadores) e armazenado somente com os 11 dígitos;
  aceita entrada com ou sem máscara (`529.982.247-25` ou `52998224725`).

## Conceitos de POO aplicados

- **Encapsulamento:** atributos privados, getters/setters com validação, sem `setCodigo` (identificador imutável).
- **Herança e abstração:** `Funcionario` é abstrata e declara `calcularRemuneracao()`.
- **Interfaces:** `Tributavel` e `Identificavel` (esta implementada por `Funcionario`, e herdada pelas subclasses).
- **Polimorfismo por sobreposição:** cada subclasse sobrescreve `calcularRemuneracao()`.
- **Polimorfismo por sobrecarga:** `FuncionarioService.buscarFuncionario(int)`,
  `buscarFuncionario(String cpf)` e `buscarFuncionario(String nome, boolean buscaExata)`.

## Tratamento de exceções

| Situação                                        | Exceção                                |
|-------------------------------------------------|----------------------------------------|
| Identificador já existente                      | `FuncionarioJaCadastradoException`     |
| Buscar/remover funcionário inexistente          | `FuncionarioNaoEncontradoException`    |
| CPF inválido, valores negativos, percentual inválido, dados obrigatórios ausentes | `IllegalArgumentException` |

As exceções são lançadas no model/service e tratadas na `FuncionarioView`, que exibe a mensagem ao usuário.

## Como executar o projeto

Pré-requisitos: JDK 17+ e Maven instalados.

```bash
mvn compile
mvn exec:java -Dexec.mainClass=Main
```

Ou, depois de `mvn compile`:

```bash
java -cp target/classes Main
```

No **Eclipse**: `File > Import > Maven > Existing Maven Projects`, selecione a pasta do projeto,
abra `src/main/java/Main.java` e use `Run As > Java Application`.

Valores decimais podem ser digitados com vírgula ou ponto (`1500,50` ou `1500.50`).

## Como executar os testes

```bash
mvn test
```

No **Eclipse**: clique com o botão direito no projeto e escolha `Run As > JUnit Test`
(ou `Run As > Maven test`).

## Desenvolvimento com TDD

O projeto foi desenvolvido seguindo o ciclo **Red → Green → Refactor**. O histórico do Git
mostra, para cada camada, primeiro o commit dos testes falhando (`[RED]`) e depois o commit
da implementação (`[GREEN]`):

```
exception -> model -> repository -> service -> view
```

Cada classe de produção possui sua respectiva classe de teste (exceto `Main`).
