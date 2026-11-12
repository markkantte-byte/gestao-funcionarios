package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Modifier;
import java.util.List;
import org.junit.jupiter.api.Test;

public class FuncionarioTest {

    private static final String CPF_VALIDO = "52998224725";
    private static final String CPF_VALIDO_2 = "11144477735";
    private static final String CPF_VALIDO_3 = "16899535009";

    /** Subclasse mínima, só para testar o comportamento da classe abstrata. */
    private static class FuncionarioFake extends Funcionario {

        FuncionarioFake(int codigo, String nome, String cpf, double salarioBase) {
            super(codigo, nome, cpf, salarioBase);
        }

        @Override
        public double calcularRemuneracao() {
            return getSalarioBase();
        }
    }

    private FuncionarioFake criar() {
        return new FuncionarioFake(1, "Maria Souza", CPF_VALIDO, 2500.0);
    }

    @Test
    void classeFuncionarioEAbstrata() {
        assertTrue(Modifier.isAbstract(Funcionario.class.getModifiers()));
    }

    @Test
    void construtorValidoGuardaOsDados() {
        Funcionario f = criar();

        assertEquals(1, f.getCodigo());
        assertEquals("Maria Souza", f.getNome());
        assertEquals(CPF_VALIDO, f.getCpf());
        assertEquals(2500.0, f.getSalarioBase(), 0.001);
    }

    @Test
    void cpfComMascaraEArmazenadoApenasComDigitos() {
        Funcionario f = new FuncionarioFake(1, "Maria Souza", "529.982.247-25", 2500.0);

        assertEquals("52998224725", f.getCpf());
    }

    @Test
    void getCpfFormatadoRetornaCpfComMascara() {
        assertEquals("529.982.247-25", criar().getCpfFormatado());
    }

    @Test
    void codigoZeroLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new FuncionarioFake(0, "Maria Souza", CPF_VALIDO, 2500.0));
    }

    @Test
    void codigoNegativoLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new FuncionarioFake(-5, "Maria Souza", CPF_VALIDO, 2500.0));
    }

    @Test
    void nomeNuloLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new FuncionarioFake(1, null, CPF_VALIDO, 2500.0));
    }

    @Test
    void nomeEmBrancoLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new FuncionarioFake(1, "   ", CPF_VALIDO, 2500.0));
    }

    @Test
    void cpfNuloLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new FuncionarioFake(1, "Maria Souza", null, 2500.0));
    }

    @Test
    void cpfComTamanhoErradoLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new FuncionarioFake(1, "Maria Souza", "123456789", 2500.0));
    }

    @Test
    void cpfComDigitosRepetidosLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new FuncionarioFake(1, "Maria Souza", "11111111111", 2500.0));
    }

    @Test
    void cpfComDigitoVerificadorErradoLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new FuncionarioFake(1, "Maria Souza", "52998224724", 2500.0));
    }

    @Test
    void cpfComLetrasLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new FuncionarioFake(1, "Maria Souza", "5299822472a", 2500.0));
    }

    @Test
    void salarioBaseNegativoLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new FuncionarioFake(1, "Maria Souza", CPF_VALIDO, -1.0));
    }

    @Test
    void salarioBaseZeroEPermitido() {
        Funcionario f = new FuncionarioFake(1, "Maria Souza", CPF_VALIDO, 0.0);

        assertEquals(0.0, f.getSalarioBase(), 0.001);
    }

    @Test
    void setNomeValidoAlteraONome() {
        Funcionario f = criar();

        f.setNome("Maria Oliveira");

        assertEquals("Maria Oliveira", f.getNome());
    }

    @Test
    void setNomeInvalidoLancaExcecaoEMantemOValorAnterior() {
        Funcionario f = criar();

        assertThrows(IllegalArgumentException.class, () -> f.setNome(""));
        assertThrows(IllegalArgumentException.class, () -> f.setNome(null));
        assertEquals("Maria Souza", f.getNome());
    }

    @Test
    void setCpfValidoAlteraOCpf() {
        Funcionario f = criar();

        f.setCpf("111.444.777-35");

        assertEquals(CPF_VALIDO_2, f.getCpf());
    }

    @Test
    void setCpfInvalidoLancaExcecaoEMantemOValorAnterior() {
        Funcionario f = criar();

        assertThrows(IllegalArgumentException.class, () -> f.setCpf("00000000000"));
        assertEquals(CPF_VALIDO, f.getCpf());
    }

    @Test
    void setSalarioBaseValidoAlteraOSalario() {
        Funcionario f = criar();

        f.setSalarioBase(4000.0);

        assertEquals(4000.0, f.getSalarioBase(), 0.001);
    }

    @Test
    void setSalarioBaseNegativoLancaExcecaoEMantemOValorAnterior() {
        Funcionario f = criar();

        assertThrows(IllegalArgumentException.class, () -> f.setSalarioBase(-0.01));
        assertEquals(2500.0, f.getSalarioBase(), 0.001);
    }

    @Test
    void salarioBaseNaNLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new FuncionarioFake(1, "Maria Souza", CPF_VALIDO, Double.NaN));
    }

    @Test
    void cpfValidoAceitaCpfsValidosComEPemMascara() {
        assertTrue(Funcionario.cpfValido("52998224725"));
        assertTrue(Funcionario.cpfValido("529.982.247-25"));
        assertTrue(Funcionario.cpfValido(CPF_VALIDO_2));
        assertTrue(Funcionario.cpfValido("168.995.350-09"));
        assertTrue(Funcionario.cpfValido(CPF_VALIDO_3));
    }

    @Test
    void cpfValidoRejeitaCpfsInvalidos() {
        assertFalse(Funcionario.cpfValido(null));
        assertFalse(Funcionario.cpfValido(""));
        assertFalse(Funcionario.cpfValido("123"));
        assertFalse(Funcionario.cpfValido("11111111111"));
        assertFalse(Funcionario.cpfValido("52998224724"));
        assertFalse(Funcionario.cpfValido("529.982.247-24"));
        assertFalse(Funcionario.cpfValido("529982247255"));
        assertFalse(Funcionario.cpfValido("529-982-247.25"));
    }

    @Test
    void toStringContemOsDadosBasicos() {
        String texto = criar().toString();

        assertTrue(texto.contains("Maria Souza"));
        assertTrue(texto.contains("529.982.247-25"));
        assertTrue(texto.contains("FuncionarioFake"));
    }

    @Test
    void comportamentoPolimorficoDeCalcularRemuneracao() {
        List<Funcionario> funcionarios = List.of(
                new FuncionarioCLT(1, "Ana Lima", CPF_VALIDO, 3000.0, 500.0),
                new FuncionarioPJ(2, "Bruno Costa", CPF_VALIDO_2, 1500.0, 4),
                new FuncionarioComissionado(3, "Carla Dias", CPF_VALIDO_3, 2000.0, 10.0, 10000.0));

        assertEquals(3500.0, funcionarios.get(0).calcularRemuneracao(), 0.001);
        assertEquals(6000.0, funcionarios.get(1).calcularRemuneracao(), 0.001);
        assertEquals(3000.0, funcionarios.get(2).calcularRemuneracao(), 0.001);
    }
}
