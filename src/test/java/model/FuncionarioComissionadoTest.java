package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class FuncionarioComissionadoTest {

    private static final String CPF = "52998224725";

    private FuncionarioComissionado criar() {
        return new FuncionarioComissionado(3, "Carla Dias", CPF, 2000.0, 10.0, 10000.0);
    }

    @Test
    void estendeFuncionarioEImplementaTributavel() {
        assertTrue(Funcionario.class.isAssignableFrom(FuncionarioComissionado.class));
        assertTrue(Tributavel.class.isAssignableFrom(FuncionarioComissionado.class));
    }

    @Test
    void calculaRemuneracaoSomandoSalarioBaseEComissao() {
        // 2000 + 10% de 10000 = 3000
        assertEquals(3000.0, criar().calcularRemuneracao(), 0.001);
    }

    @Test
    void semVendasARemuneracaoESoOSalarioBase() {
        FuncionarioComissionado f =
                new FuncionarioComissionado(3, "Carla Dias", CPF, 2000.0, 10.0, 0.0);

        assertEquals(2000.0, f.calcularRemuneracao(), 0.001);
    }

    @Test
    void percentualZeroNaoGeraComissao() {
        FuncionarioComissionado f =
                new FuncionarioComissionado(3, "Carla Dias", CPF, 2000.0, 0.0, 10000.0);

        assertEquals(2000.0, f.calcularRemuneracao(), 0.001);
    }

    @Test
    void percentualCemPorCentoEPermitido() {
        FuncionarioComissionado f =
                new FuncionarioComissionado(3, "Carla Dias", CPF, 1000.0, 100.0, 500.0);

        assertEquals(1500.0, f.calcularRemuneracao(), 0.001);
    }

    @Test
    void guardaOsDadosInformados() {
        FuncionarioComissionado f = criar();

        assertEquals(3, f.getCodigo());
        assertEquals("Carla Dias", f.getNome());
        assertEquals(CPF, f.getCpf());
        assertEquals(2000.0, f.getSalarioBase(), 0.001);
        assertEquals(10.0, f.getPercentualComissao(), 0.001);
        assertEquals(10000.0, f.getTotalVendas(), 0.001);
    }

    @Test
    void percentualNegativoLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new FuncionarioComissionado(3, "Carla Dias", CPF, 2000.0, -1.0, 10000.0));
    }

    @Test
    void percentualAcimaDeCemLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new FuncionarioComissionado(3, "Carla Dias", CPF, 2000.0, 100.01, 10000.0));
    }

    @Test
    void percentualNaNLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new FuncionarioComissionado(3, "Carla Dias", CPF, 2000.0, Double.NaN, 10000.0));
    }

    @Test
    void totalDeVendasNegativoLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new FuncionarioComissionado(3, "Carla Dias", CPF, 2000.0, 10.0, -1.0));
    }

    @Test
    void salarioBaseNegativoLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new FuncionarioComissionado(3, "Carla Dias", CPF, -2000.0, 10.0, 10000.0));
    }

    @Test
    void dadosBaseInvalidosLancamExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new FuncionarioComissionado(3, " ", CPF, 2000.0, 10.0, 10000.0));
        assertThrows(IllegalArgumentException.class,
                () -> new FuncionarioComissionado(3, "Carla Dias", "52998224724", 2000.0, 10.0, 10000.0));
    }

    @Test
    void setPercentualComissaoValidoAlteraARemuneracao() {
        FuncionarioComissionado f = criar();

        f.setPercentualComissao(20.0);

        assertEquals(20.0, f.getPercentualComissao(), 0.001);
        assertEquals(4000.0, f.calcularRemuneracao(), 0.001);
    }

    @Test
    void setPercentualComissaoInvalidoLancaExcecaoEMantemOValorAnterior() {
        FuncionarioComissionado f = criar();

        assertThrows(IllegalArgumentException.class, () -> f.setPercentualComissao(-5.0));
        assertThrows(IllegalArgumentException.class, () -> f.setPercentualComissao(101.0));
        assertEquals(10.0, f.getPercentualComissao(), 0.001);
    }

    @Test
    void setTotalVendasValidoAlteraARemuneracao() {
        FuncionarioComissionado f = criar();

        f.setTotalVendas(20000.0);

        assertEquals(20000.0, f.getTotalVendas(), 0.001);
        assertEquals(4000.0, f.calcularRemuneracao(), 0.001);
    }

    @Test
    void setTotalVendasNegativoLancaExcecaoEMantemOValorAnterior() {
        FuncionarioComissionado f = criar();

        assertThrows(IllegalArgumentException.class, () -> f.setTotalVendas(-1.0));
        assertEquals(10000.0, f.getTotalVendas(), 0.001);
    }

    @Test
    void calculaTributosComoQuinzePorCentoDaRemuneracao() {
        // 15% de 3000 = 450
        assertEquals(450.0, criar().calcularTributos(), 0.001);
    }

    @Test
    void toStringContemTipoNomeEPercentual() {
        String texto = criar().toString();

        assertTrue(texto.contains("Comissionado"));
        assertTrue(texto.contains("Carla Dias"));
        assertTrue(texto.contains("10,00%"));
    }
}
