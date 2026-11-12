package exception;

/**
 * Lançada quando se tenta cadastrar um funcionário com identificador já existente.
 */
public class FuncionarioJaCadastradoException extends Exception {

    private static final long serialVersionUID = 1L;

    public FuncionarioJaCadastradoException(String mensagem) {
        super(mensagem);
    }
}
