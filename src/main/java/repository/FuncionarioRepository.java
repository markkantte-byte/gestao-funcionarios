package repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import model.Funcionario;

/**
 * Armazena e recupera funcionários em memória, mantendo a ordem de inserção.
 */
public class FuncionarioRepository {

    private final Map<Integer, Funcionario> funcionarios = new LinkedHashMap<>();

    /**
     * Guarda o funcionário usando o seu código como chave.
     *
     * @throws IllegalArgumentException se o funcionário for nulo
     */
    public void salvar(Funcionario funcionario) {
        if (funcionario == null) {
            throw new IllegalArgumentException("O funcionário não pode ser nulo.");
        }
        funcionarios.put(funcionario.getCodigo(), funcionario);
    }

    public Optional<Funcionario> buscarPorCodigo(int codigo) {
        return Optional.ofNullable(funcionarios.get(codigo));
    }

    /**
     * @return uma cópia da lista de funcionários, na ordem de cadastro
     */
    public List<Funcionario> listarTodos() {
        return new ArrayList<>(funcionarios.values());
    }

    /**
     * @return true se existia um funcionário com o código e ele foi removido
     */
    public boolean remover(int codigo) {
        return funcionarios.remove(codigo) != null;
    }

    public boolean existe(int codigo) {
        return funcionarios.containsKey(codigo);
    }

    public int quantidade() {
        return funcionarios.size();
    }
}
