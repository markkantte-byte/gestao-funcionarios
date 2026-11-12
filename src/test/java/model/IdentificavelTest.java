package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class IdentificavelTest {

    private static final String CPF = "52998224725";

    @Test
    void implementacaoAnonimaRetornaOIdentificador() {
        Identificavel identificavel = () -> 42;

        assertEquals(42, identificavel.getCodigo());
    }

    @Test
    void funcionarioCLTEIdentificavel() {
        Identificavel identificavel = new FuncionarioCLT(10, "Ana Lima", CPF, 3000.0, 500.0);

        assertEquals(10, identificavel.getCodigo());
    }

    @Test
    void funcionarioPJEIdentificavel() {
        Identificavel identificavel = new FuncionarioPJ(11, "Bruno Costa", CPF, 1500.0, 4);

        assertEquals(11, identificavel.getCodigo());
    }

    @Test
    void funcionarioComissionadoEIdentificavel() {
        Identificavel identificavel =
                new FuncionarioComissionado(12, "Carla Dias", CPF, 2000.0, 10.0, 10000.0);

        assertEquals(12, identificavel.getCodigo());
    }

    @Test
    void funcionarioImplementaIdentificavelEAsSubclassesHerdam() {
        assertTrue(Identificavel.class.isAssignableFrom(Funcionario.class));
        assertTrue(Identificavel.class.isAssignableFrom(FuncionarioCLT.class));
        assertTrue(Identificavel.class.isAssignableFrom(FuncionarioPJ.class));
        assertTrue(Identificavel.class.isAssignableFrom(FuncionarioComissionado.class));
    }
}
