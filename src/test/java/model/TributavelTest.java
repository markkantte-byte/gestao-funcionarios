package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TributavelTest {

    private static final String CPF = "52998224725";

    @Test
    void implementacaoAnonimaCalculaOsTributos() {
        Tributavel tributavel = () -> 123.45;

        assertEquals(123.45, tributavel.calcularTributos(), 0.001);
    }

    @Test
    void funcionarioCLTETributavel() {
        Object funcionario = new FuncionarioCLT(1, "Ana Lima", CPF, 3000.0, 500.0);

        assertTrue(funcionario instanceof Tributavel);
    }

    @Test
    void funcionarioComissionadoETributavel() {
        Object funcionario = new FuncionarioComissionado(3, "Carla Dias", CPF, 2000.0, 10.0, 10000.0);

        assertTrue(funcionario instanceof Tributavel);
    }

    @Test
    void funcionarioPJNaoETributavelPoisRecolheOsProprioImpostos() {
        Object funcionario = new FuncionarioPJ(2, "Bruno Costa", CPF, 1500.0, 4);

        assertFalse(funcionario instanceof Tributavel);
    }

    @Test
    void tributosNuncaSuperamARemuneracao() {
        FuncionarioCLT clt = new FuncionarioCLT(1, "Ana Lima", CPF, 3000.0, 500.0);
        FuncionarioComissionado comissionado =
                new FuncionarioComissionado(3, "Carla Dias", CPF, 2000.0, 10.0, 10000.0);

        assertTrue(clt.calcularTributos() <= clt.calcularRemuneracao());
        assertTrue(comissionado.calcularTributos() <= comissionado.calcularRemuneracao());
    }
}
