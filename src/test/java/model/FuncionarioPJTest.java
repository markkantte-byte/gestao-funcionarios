package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class FuncionarioPJTest {

    private static final String CPF = "52998224725";

    private FuncionarioPJ criar() {
        return new FuncionarioPJ(2, "Bruno Costa", CPF, 1500.0, 4);
    }

    @Test
    void estendeFuncionarioENaoImplementaTributavel() {
        assertTrue(Funcionario.class.isAssignableFrom(FuncionarioPJ.class));
        assertFalse(Tributavel.class.isAssignableFrom(FuncionarioPJ.class));
    }

    @Test
    void calculaRemuneracaoPorProjetos() {
        assertEquals(6000.0, criar().calcularRemuneracao(), 0.001);
    }

    @Test
    void semProjetosARemuneracaoEZero() {
        FuncionarioPJ f = new FuncionarioPJ(2, "Bruno Costa", CPF, 1500.0, 0);

        assertEquals(0.0, f.calcularRemuneracao(), 0.001);
    }

    @Test
    void guardaOsDadosInformados() {
        FuncionarioPJ f = criar();

        assertEquals(2, f.getCodigo());
        assertEquals("Bruno Costa", f.getNome());
        assertEquals(CPF, f.getCpf());
        assertEquals(1500.0, f.getValorPorProjeto(), 0.001);
        assertEquals(4, f.getQuantidadeProjetos());
    }

    @Test
    void salarioBaseDoPJEZeroPoisSoRecebePorProjeto() {
        assertEquals(0.0, criar().getSalarioBase(), 0.001);
    }

    @Test
    void valorPorProjetoNegativoLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new FuncionarioPJ(2, "Bruno Costa", CPF, -1.0, 4));
    }

    @Test
    void quantidadeDeProjetosNegativaLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new FuncionarioPJ(2, "Bruno Costa", CPF, 1500.0, -1));
    }

    @Test
    void dadosBaseInvalidosLancamExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new FuncionarioPJ(2, null, CPF, 1500.0, 4));
        assertThrows(IllegalArgumentException.class,
                () -> new FuncionarioPJ(2, "Bruno Costa", "11111111111", 1500.0, 4));
    }

    @Test
    void setValorPorProjetoValidoAlteraARemuneracao() {
        FuncionarioPJ f = criar();

        f.setValorPorProjeto(2000.0);

        assertEquals(2000.0, f.getValorPorProjeto(), 0.001);
        assertEquals(8000.0, f.calcularRemuneracao(), 0.001);
    }

    @Test
    void setValorPorProjetoNegativoLancaExcecaoEMantemOValorAnterior() {
        FuncionarioPJ f = criar();

        assertThrows(IllegalArgumentException.class, () -> f.setValorPorProjeto(-5.0));
        assertEquals(1500.0, f.getValorPorProjeto(), 0.001);
    }

    @Test
    void setQuantidadeProjetosValidaAlteraARemuneracao() {
        FuncionarioPJ f = criar();

        f.setQuantidadeProjetos(6);

        assertEquals(6, f.getQuantidadeProjetos());
        assertEquals(9000.0, f.calcularRemuneracao(), 0.001);
    }

    @Test
    void setQuantidadeProjetosNegativaLancaExcecaoEMantemOValorAnterior() {
        FuncionarioPJ f = criar();

        assertThrows(IllegalArgumentException.class, () -> f.setQuantidadeProjetos(-2));
        assertEquals(4, f.getQuantidadeProjetos());
    }

    @Test
    void toStringContemTipoNomeEProjetos() {
        String texto = criar().toString();

        assertTrue(texto.contains("PJ"));
        assertTrue(texto.contains("Bruno Costa"));
        assertTrue(texto.contains("1.500,00"));
    }
}
