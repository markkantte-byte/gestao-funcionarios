package exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class FuncionarioNaoEncontradoExceptionTest {

    @Test
    void guardaAMensagemInformada() {
        FuncionarioNaoEncontradoException e =
                new FuncionarioNaoEncontradoException("Funcionário 7 não encontrado");

        assertEquals("Funcionário 7 não encontrado", e.getMessage());
    }

    @Test
    void eUmaExcecaoVerificada() {
        assertTrue(Exception.class.isAssignableFrom(FuncionarioNaoEncontradoException.class));
        assertFalse(RuntimeException.class.isAssignableFrom(FuncionarioNaoEncontradoException.class));
    }

    @Test
    void podeSerLancadaECapturada() {
        FuncionarioNaoEncontradoException e = assertThrows(
                FuncionarioNaoEncontradoException.class,
                () -> {
                    throw new FuncionarioNaoEncontradoException("não achei");
                });

        assertEquals("não achei", e.getMessage());
    }
}
