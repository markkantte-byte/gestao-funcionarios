package exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class FuncionarioJaCadastradoExceptionTest {

    @Test
    void guardaAMensagemInformada() {
        FuncionarioJaCadastradoException e =
                new FuncionarioJaCadastradoException("Código 3 já cadastrado");

        assertEquals("Código 3 já cadastrado", e.getMessage());
    }

    @Test
    void eUmaExcecaoVerificada() {
        assertTrue(Exception.class.isAssignableFrom(FuncionarioJaCadastradoException.class));
        assertFalse(RuntimeException.class.isAssignableFrom(FuncionarioJaCadastradoException.class));
    }

    @Test
    void podeSerLancadaECapturada() {
        FuncionarioJaCadastradoException e = assertThrows(
                FuncionarioJaCadastradoException.class,
                () -> {
                    throw new FuncionarioJaCadastradoException("duplicado");
                });

        assertEquals("duplicado", e.getMessage());
    }
}
