package view;

import exception.FuncionarioJaCadastradoException;
import exception.FuncionarioNaoEncontradoException;
import java.io.PrintStream;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;
import model.Funcionario;
import model.FuncionarioCLT;
import model.FuncionarioComissionado;
import model.FuncionarioPJ;
import service.FuncionarioService;

/**
 * Interface de console: lê as opções do usuário, chama o service e apresenta os resultados.
 * As exceções das camadas inferiores são tratadas aqui e exibidas como mensagens de erro.
 */
public class FuncionarioView {

    private static final Locale PT_BR = Locale.forLanguageTag("pt-BR");

    private final FuncionarioService service;
    private final Scanner scanner;
    private final PrintStream out;

    public FuncionarioView(FuncionarioService service, Scanner scanner, PrintStream out) {
        if (service == null || scanner == null || out == null) {
            throw new IllegalArgumentException("Service, scanner e saída são obrigatórios.");
        }
        this.service = service;
        this.scanner = scanner;
        this.out = out;
    }

    /**
     * Executa o menu principal até o usuário escolher sair (ou a entrada acabar).
     */
    public void iniciar() {
        boolean continuar = true;
        try {
            while (continuar) {
                exibirMenu();
                String opcao = lerTexto("Escolha uma opção: ");
                continuar = executarOpcao(opcao.trim());
            }
        } catch (NoSuchElementException e) {
            out.println("Entrada encerrada.");
            return;
        }
        out.println("Encerrando o sistema...");
    }

    private void exibirMenu() {
        out.println();
        out.println("===== SISTEMA DE FUNCIONÁRIOS E FOLHA DE PAGAMENTO =====");
        out.println("1  - Cadastrar funcionário CLT");
        out.println("2  - Cadastrar funcionário PJ");
        out.println("3  - Cadastrar funcionário comissionado");
        out.println("4  - Listar funcionários");
        out.println("5  - Buscar funcionário por código");
        out.println("6  - Buscar funcionário por CPF");
        out.println("7  - Buscar funcionário por nome");
        out.println("8  - Remover funcionário");
        out.println("9  - Exibir dados completos de um funcionário");
        out.println("10 - Calcular remuneração de um funcionário");
        out.println("11 - Relatório de funcionários e salários");
        out.println("12 - Total da folha de pagamento");
        out.println("0  - Sair");
    }

    /**
     * @return false quando o usuário pediu para sair; true caso contrário
     */
    private boolean executarOpcao(String opcao) {
        try {
            switch (opcao) {
                case "1":
                    cadastrarCLT();
                    break;
                case "2":
                    cadastrarPJ();
                    break;
                case "3":
                    cadastrarComissionado();
                    break;
                case "4":
                    listar();
                    break;
                case "5":
                    buscarPorCodigo();
                    break;
                case "6":
                    buscarPorCpf();
                    break;
                case "7":
                    buscarPorNome();
                    break;
                case "8":
                    remover();
                    break;
                case "9":
                    exibirDados();
                    break;
                case "10":
                    calcularRemuneracao();
                    break;
                case "11":
                    out.println(service.gerarRelatorio());
                    break;
                case "12":
                    out.println(String.format(PT_BR, "Total da folha de pagamento: R$ %,.2f",
                            service.calcularTotalFolha()));
                    break;
                case "0":
                    return false;
                default:
                    out.println("Opção inválida. Tente novamente.");
            }
        } catch (FuncionarioJaCadastradoException | FuncionarioNaoEncontradoException
                 | IllegalArgumentException e) {
            out.println("Erro: " + e.getMessage());
        }
        return true;
    }

    // ---------- ações do menu ----------

    private void cadastrarCLT() throws FuncionarioJaCadastradoException {
        int codigo = lerInteiro("Código: ");
        String nome = lerTexto("Nome: ");
        String cpf = lerTexto("CPF: ");
        double salarioBase = lerDecimal("Salário-base: ");
        double adicional = lerDecimal("Adicional de benefícios: ");

        service.cadastrar(new FuncionarioCLT(codigo, nome, cpf, salarioBase, adicional));
        out.println("Funcionário cadastrado com sucesso.");
    }

    private void cadastrarPJ() throws FuncionarioJaCadastradoException {
        int codigo = lerInteiro("Código: ");
        String nome = lerTexto("Nome: ");
        String cpf = lerTexto("CPF: ");
        double valorPorProjeto = lerDecimal("Valor por projeto: ");
        int quantidadeProjetos = lerInteiro("Quantidade de projetos: ");

        service.cadastrar(new FuncionarioPJ(codigo, nome, cpf, valorPorProjeto, quantidadeProjetos));
        out.println("Funcionário cadastrado com sucesso.");
    }

    private void cadastrarComissionado() throws FuncionarioJaCadastradoException {
        int codigo = lerInteiro("Código: ");
        String nome = lerTexto("Nome: ");
        String cpf = lerTexto("CPF: ");
        double salarioBase = lerDecimal("Salário-base: ");
        double percentual = lerDecimal("Percentual de comissão (0 a 100): ");
        double totalVendas = lerDecimal("Total de vendas: ");

        service.cadastrar(new FuncionarioComissionado(
                codigo, nome, cpf, salarioBase, percentual, totalVendas));
        out.println("Funcionário cadastrado com sucesso.");
    }

    private void listar() {
        List<Funcionario> funcionarios = service.listar();
        if (funcionarios.isEmpty()) {
            out.println("Nenhum funcionário cadastrado.");
            return;
        }
        for (Funcionario f : funcionarios) {
            out.println(f);
        }
    }

    private void buscarPorCodigo() throws FuncionarioNaoEncontradoException {
        int codigo = lerInteiro("Código: ");
        out.println(service.buscarFuncionario(codigo));
    }

    private void buscarPorCpf() throws FuncionarioNaoEncontradoException {
        String cpf = lerTexto("CPF: ");
        out.println(service.buscarFuncionario(cpf));
    }

    private void buscarPorNome() throws FuncionarioNaoEncontradoException {
        String nome = lerTexto("Nome (ou parte do nome): ");
        boolean buscaExata = lerTexto("Busca exata? (s/n): ").trim().equalsIgnoreCase("s");

        for (Funcionario f : service.buscarFuncionario(nome, buscaExata)) {
            out.println(f);
        }
    }

    private void remover() throws FuncionarioNaoEncontradoException {
        int codigo = lerInteiro("Código: ");
        service.remover(codigo);
        out.println("Funcionário removido com sucesso.");
    }

    private void exibirDados() throws FuncionarioNaoEncontradoException {
        int codigo = lerInteiro("Código: ");
        out.println(service.exibirDados(codigo));
    }

    private void calcularRemuneracao() throws FuncionarioNaoEncontradoException {
        int codigo = lerInteiro("Código: ");
        double remuneracao = service.calcularRemuneracao(codigo);
        out.println(String.format(PT_BR, "Remuneração do funcionário %d: R$ %,.2f", codigo, remuneracao));
    }

    // ---------- leitura de dados ----------

    private String lerTexto(String mensagem) {
        out.print(mensagem);
        return scanner.nextLine();
    }

    private int lerInteiro(String mensagem) {
        String texto = lerTexto(mensagem).trim();
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Entrada numérica inválida: \"" + texto + "\".");
        }
    }

    /** Aceita vírgula ou ponto como separador decimal (ex.: 1500,50 ou 1500.50). */
    private double lerDecimal(String mensagem) {
        String texto = lerTexto(mensagem).trim();
        try {
            return Double.parseDouble(texto.replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Entrada numérica inválida: \"" + texto + "\".");
        }
    }
}
