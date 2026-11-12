package exception;

/**
 * Lançada quando se tenta buscar ou remover um funcionário que não existe.
 */
public class FuncionarioNaoEncontradoException extends Exception {

    private static final long serialVersionUID = 1L;

    public FuncionarioNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
