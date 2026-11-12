package service;

import exception.FuncionarioJaCadastradoException;
import exception.FuncionarioNaoEncontradoException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import model.Funcionario;
import repository.FuncionarioRepository;

/**
 * Regras de negócio do sistema: cadastro, consultas, remoção, remuneração e folha de pagamento.
 */
public class FuncionarioService {

    private static final Locale PT_BR = Locale.forLanguageTag("pt-BR");

    private final FuncionarioRepository repository;

    public FuncionarioService(FuncionarioRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("O repositório é obrigatório.");
        }
        this.repository = repository;
    }

    /**
     * Cadastra um funcionário.
     *
     * @throws IllegalArgumentException         se o funcionário for nulo
     * @throws FuncionarioJaCadastradoException se já existir funcionário com o mesmo código
     */
    public void cadastrar(Funcionario funcionario) throws FuncionarioJaCadastradoException {
        if (funcionario == null) {
            throw new IllegalArgumentException("O funcionário não pode ser nulo.");
        }
        if (repository.existe(funcionario.getCodigo())) {
            throw new FuncionarioJaCadastradoException(
                    "Já existe um funcionário cadastrado com o código " + funcionario.getCodigo() + ".");
        }
        repository.salvar(funcionario);
    }

    public List<Funcionario> listar() {
        return repository.listarTodos();
    }

    /**
     * Busca pelo código identificador.
     *
     * @throws FuncionarioNaoEncontradoException se não existir
     */
    public Funcionario buscarFuncionario(int codigo) throws FuncionarioNaoEncontradoException {
        return repository.buscarPorCodigo(codigo).orElseThrow(() ->
                new FuncionarioNaoEncontradoException(
                        "Funcionário não encontrado com o código " + codigo + "."));
    }

    /**
     * Busca pelo CPF (com ou sem máscara).
     *
     * @throws IllegalArgumentException          se o CPF for nulo
     * @throws FuncionarioNaoEncontradoException se não existir
     */
    public Funcionario buscarFuncionario(String cpf) throws FuncionarioNaoEncontradoException {
        if (cpf == null) {
            throw new IllegalArgumentException("O CPF é obrigatório para a busca.");
        }
        String digitos = cpf.replaceAll("\\D", "");
        for (Funcionario f : repository.listarTodos()) {
            if (f.getCpf().equals(digitos)) {
                return f;
            }
        }
        throw new FuncionarioNaoEncontradoException("Funcionário não encontrado com o CPF " + cpf + ".");
    }

    /**
     * Busca pelo nome, ignorando maiúsculas e minúsculas.
     *
     * @param buscaExata true para nome idêntico; false para nomes que contenham o texto
     * @throws IllegalArgumentException          se o nome for nulo ou em branco
     * @throws FuncionarioNaoEncontradoException se ninguém corresponder
     */
    public List<Funcionario> buscarFuncionario(String nome, boolean buscaExata)
            throws FuncionarioNaoEncontradoException {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome é obrigatório para a busca.");
        }
        String termo = nome.toLowerCase(PT_BR);
        List<Funcionario> encontrados = new ArrayList<>();
        for (Funcionario f : repository.listarTodos()) {
            String nomeFuncionario = f.getNome().toLowerCase(PT_BR);
            boolean corresponde = buscaExata ? nomeFuncionario.equals(termo)
                                             : nomeFuncionario.contains(termo);
            if (corresponde) {
                encontrados.add(f);
            }
        }
        if (encontrados.isEmpty()) {
            throw new FuncionarioNaoEncontradoException(
                    "Nenhum funcionário encontrado com o nome \"" + nome + "\".");
        }
        return encontrados;
    }

    /**
     * @throws FuncionarioNaoEncontradoException se o código não existir
     */
    public void remover(int codigo) throws FuncionarioNaoEncontradoException {
        if (!repository.remover(codigo)) {
            throw new FuncionarioNaoEncontradoException(
                    "Funcionário não encontrado com o código " + codigo + ".");
        }
    }

    /**
     * Calcula a remuneração usando a regra da categoria do funcionário (polimorfismo).
     *
     * @throws FuncionarioNaoEncontradoException se o código não existir
     */
    public double calcularRemuneracao(int codigo) throws FuncionarioNaoEncontradoException {
        return buscarFuncionario(codigo).calcularRemuneracao();
    }

    /**
     * @return texto com todos os dados do funcionário
     * @throws FuncionarioNaoEncontradoException se o código não existir
     */
    public String exibirDados(int codigo) throws FuncionarioNaoEncontradoException {
        return buscarFuncionario(codigo).toString();
    }

    /**
     * @return soma da remuneração de todos os funcionários cadastrados
     */
    public double calcularTotalFolha() {
        double total = 0.0;
        for (Funcionario f : repository.listarTodos()) {
            total += f.calcularRemuneracao();
        }
        return total;
    }

    /**
     * Gera o relatório com todos os funcionários, suas remunerações e o total da folha.
     */
    public String gerarRelatorio() {
        List<Funcionario> funcionarios = repository.listarTodos();
        if (funcionarios.isEmpty()) {
            return "Nenhum funcionário cadastrado.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("===== RELATÓRIO DE FUNCIONÁRIOS =====\n");
        for (Funcionario f : funcionarios) {
            sb.append(String.format(PT_BR, "[%d] %s (%s) - R$ %,.2f%n",
                    f.getCodigo(), f.getNome(), f.getClass().getSimpleName(), f.calcularRemuneracao()));
        }
        sb.append("-------------------------------------\n");
        sb.append(String.format(PT_BR, "TOTAL DA FOLHA: R$ %,.2f", calcularTotalFolha()));
        return sb.toString();
    }
}
