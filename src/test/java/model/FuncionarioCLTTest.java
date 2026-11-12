package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class FuncionarioCLTTest {

    private static final String CPF = "52998224725";

    private FuncionarioCLT criar() {
        return new FuncionarioCLT(1, "Ana Lima", CPF, 3000.0, 500.0);
    }

    @Test
    void estendeFuncionarioEImplementaTributavel() {
        assertTrue(Funcionario.class.isAssignableFrom(FuncionarioCLT.class));
        assertTrue(Tributavel.class.isAssignableFrom(FuncionarioCLT.class));
    }

    @Test
    void calculaRemuneracaoSomandoSalarioBaseEBeneficios() {
        assertEquals(3500.0, criar().calcularRemuneracao(), 0.001);
    }

    @Test
    void semBeneficiosARemuneracaoESoOSalarioBase() {
        FuncionarioCLT f = new FuncionarioCLT(1, "Ana Lima", CPF, 3000.0, 0.0);

        assertEquals(3000.0, f.calcularRemuneracao(), 0.001);
    }

    @Test
    void guardaOsDadosInformados() {
        FuncionarioCLT f = criar();

        assertEquals(1, f.getCodigo());
        assertEquals("Ana Lima", f.getNome());
        assertEquals(CPF, f.getCpf());
        assertEquals(3000.0, f.getSalarioBase(), 0.001);
        assertEquals(500.0, f.getAdicionalBeneficios(), 0.001);
    }

    @Test
    void adicionalNegativoNoConstrutorLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new FuncionarioCLT(1, "Ana Lima", CPF, 3000.0, -1.0));
    }

    @Test
    void salarioBaseNegativoNoConstrutorLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new FuncionarioCLT(1, "Ana Lima", CPF, -3000.0, 500.0));
    }

    @Test
    void dadosBaseInvalidosLancamExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new FuncionarioCLT(1, null, CPF, 3000.0, 500.0));
        assertThrows(IllegalArgumentException.class,
                () -> new FuncionarioCLT(1, "Ana Lima", "123", 3000.0, 500.0));
        assertThrows(IllegalArgumentException.class,
                () -> new FuncionarioCLT(0, "Ana Lima", CPF, 3000.0, 500.0));
    }

    @Test
    void setAdicionalBeneficiosValidoAlteraARemuneracao() {
        FuncionarioCLT f = criar();

        f.setAdicionalBeneficios(800.0);

        assertEquals(800.0, f.getAdicionalBeneficios(), 0.001);
        assertEquals(3800.0, f.calcularRemuneracao(), 0.001);
    }

    @Test
    void setAdicionalBeneficiosNegativoLancaExcecaoEMantemOValorAnterior() {
        FuncionarioCLT f = criar();

        assertThrows(IllegalArgumentException.class, () -> f.setAdicionalBeneficios(-10.0));
        assertEquals(500.0, f.getAdicionalBeneficios(), 0.001);
    }

    @Test
    void remuneracaoAcompanhaAlteracaoDoSalarioBase() {
        FuncionarioCLT f = criar();

        f.setSalarioBase(4000.0);

        assertEquals(4500.0, f.calcularRemuneracao(), 0.001);
    }

    @Test
    void calculaTributosComoOnzePorCentoDaRemuneracao() {
        assertEquals(385.0, criar().calcularTributos(), 0.001);
    }

    @Test
    void toStringContemTipoNomeEAdicional() {
        String texto = criar().toString();

        assertTrue(texto.contains("CLT"));
        assertTrue(texto.contains("Ana Lima"));
        assertTrue(texto.contains("500,00"));
    }
}
