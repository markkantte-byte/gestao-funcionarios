package view;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import model.Funcionario;
import model.FuncionarioCLT;
import model.FuncionarioComissionado;
import model.FuncionarioPJ;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.FuncionarioRepository;
import service.FuncionarioService;

public class FuncionarioViewTest {

    private static final String CPF_1 = "52998224725";
    private static final String CPF_2 = "11144477735";
    private static final String CPF_3 = "16899535009";

    private FuncionarioService service;

    @BeforeEach
    void setUp() {
        service = new FuncionarioService(new FuncionarioRepository());
    }

    /** Executa a view com a entrada simulada e devolve tudo que foi impresso. */
    private String executar(String entrada) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(buffer, true, StandardCharsets.UTF_8);
        FuncionarioView view = new FuncionarioView(service, new Scanner(entrada), out);
        view.iniciar();
        return buffer.toString(StandardCharsets.UTF_8);
    }

    private void cadastrarCltEPj() throws Exception {
        service.cadastrar(new FuncionarioCLT(1, "Ana Lima", CPF_1, 3000.0, 500.0));      // 3500
        service.cadastrar(new FuncionarioPJ(2, "Bruno Costa", CPF_2, 1500.0, 4));        // 6000
    }

    // ---------- construtor ----------

    @Test
    void construtorComDependenciasNulasLancaExcecao() {
        Scanner scanner = new Scanner("");
        PrintStream out = new PrintStream(new ByteArrayOutputStream());

        assertThrows(IllegalArgumentException.class, () -> new FuncionarioView(null, scanner, out));
        assertThrows(IllegalArgumentException.class, () -> new FuncionarioView(service, null, out));
        assertThrows(IllegalArgumentException.class, () -> new FuncionarioView(service, scanner, null));
    }

    // ---------- menu ----------

    @Test
    void opcaoZeroEncerraOSistema() {
        String saida = executar("0\n");

        assertTrue(saida.contains("Encerrando"));
    }

    @Test
    void menuExibeAsOpcoesPrincipais() {
        String saida = executar("0\n");

        assertTrue(saida.contains("Cadastrar funcionário CLT"));
        assertTrue(saida.contains("Cadastrar funcionário PJ"));
        assertTrue(saida.contains("Cadastrar funcionário comissionado"));
        assertTrue(saida.contains("Listar funcionários"));
        assertTrue(saida.contains("Relatório"));
        assertTrue(saida.contains("Sair"));
    }

    @Test
    void opcaoInvalidaMostraMensagemEVoltaAoMenu() {
        String saida = executar("99\n0\n");

        assertTrue(saida.contains("Opção inválida"));
        assertTrue(saida.contains("Encerrando"));
    }

    @Test
    void fimDaEntradaNaoLancaExcecao() {
        assertDoesNotThrow(() -> executar(""));
        assertTrue(executar("").contains("Entrada encerrada"));
    }

    // ---------- cadastro ----------

    @Test
    void cadastraFuncionarioCLT() {
        String saida = executar("1\n1\nJoao Silva\n52998224725\n3000\n500\n0\n");

        assertTrue(saida.contains("cadastrado com sucesso"));
        assertEquals(1, service.listar().size());
        Funcionario f = service.listar().get(0);
        assertTrue(f instanceof FuncionarioCLT);
        assertEquals(3500.0, f.calcularRemuneracao(), 0.001);
    }

    @Test
    void cadastraFuncionarioPJ() {
        String saida = executar("2\n2\nMaria Souza\n11144477735\n1500\n4\n0\n");

        assertTrue(saida.contains("cadastrado com sucesso"));
        Funcionario f = service.listar().get(0);
        assertTrue(f instanceof FuncionarioPJ);
        assertEquals(6000.0, f.calcularRemuneracao(), 0.001);
    }

    @Test
    void cadastraFuncionarioComissionado() {
        String saida = executar("3\n3\nCarla Dias\n16899535009\n2000\n10\n10000\n0\n");

        assertTrue(saida.contains("cadastrado com sucesso"));
        Funcionario f = service.listar().get(0);
        assertTrue(f instanceof FuncionarioComissionado);
        assertEquals(3000.0, f.calcularRemuneracao(), 0.001);
    }

    @Test
    void aceitaVirgulaComoSeparadorDecimal() {
        executar("1\n1\nJoao Silva\n52998224725\n3000,50\n500,25\n0\n");

        assertEquals(3500.75, service.listar().get(0).calcularRemuneracao(), 0.001);
    }

    @Test
    void cadastroComCodigoDuplicadoMostraErro() throws Exception {
        cadastrarCltEPj();

        String saida = executar("1\n1\nOutro Nome\n39053344705\n1000\n0\n0\n");

        assertTrue(saida.contains("Já existe"));
        assertEquals(2, service.listar().size());
    }

    @Test
    void cadastroComCpfInvalidoMostraErroENaoCadastra() {
        String saida = executar("1\n1\nJoao Silva\n11111111111\n3000\n500\n0\n");

        assertTrue(saida.contains("CPF inválido"));
        assertEquals(0, service.listar().size());
    }

    @Test
    void cadastroComValorNegativoMostraErroENaoCadastra() {
        String saida = executar("1\n1\nJoao Silva\n52998224725\n-100\n500\n0\n");

        assertTrue(saida.contains("Valor inválido"));
        assertEquals(0, service.listar().size());
    }

    @Test
    void cadastroComPercentualInvalidoMostraErro() {
        String saida = executar("3\n3\nCarla Dias\n16899535009\n2000\n150\n10000\n0\n");

        assertTrue(saida.contains("Percentual de comissão inválido"));
        assertEquals(0, service.listar().size());
    }

    @Test
    void cadastroSemNomeMostraErroDeDadoObrigatorio() {
        String saida = executar("1\n1\n   \n52998224725\n3000\n500\n0\n");

        assertTrue(saida.contains("obrigatório"));
        assertEquals(0, service.listar().size());
    }

    @Test
    void entradaNumericaInvalidaMostraErroEVoltaAoMenu() {
        String saida = executar("1\nabc\n0\n");

        assertTrue(saida.contains("Entrada numérica inválida"));
        assertTrue(saida.contains("Encerrando"));
    }

    // ---------- listar ----------

    @Test
    void listaOsFuncionariosCadastrados() throws Exception {
        cadastrarCltEPj();

        String saida = executar("4\n0\n");

        assertTrue(saida.contains("Ana Lima"));
        assertTrue(saida.contains("Bruno Costa"));
    }

    @Test
    void listarSemFuncionariosInformaQueEstaVazio() {
        String saida = executar("4\n0\n");

        assertTrue(saida.contains("Nenhum funcionário cadastrado"));
    }

    // ---------- buscas ----------

    @Test
    void buscaFuncionarioPorCodigo() throws Exception {
        cadastrarCltEPj();

        String saida = executar("5\n1\n0\n");

        assertTrue(saida.contains("Ana Lima"));
    }

    @Test
    void buscaPorCodigoInexistenteMostraErro() {
        String saida = executar("5\n99\n0\n");

        assertTrue(saida.contains("não encontrado"));
    }

    @Test
    void buscaFuncionarioPorCpf() throws Exception {
        cadastrarCltEPj();

        String saida = executar("6\n529.982.247-25\n0\n");

        assertTrue(saida.contains("Ana Lima"));
    }

    @Test
    void buscaPorCpfInexistenteMostraErro() {
        String saida = executar("6\n00000000000\n0\n");

        assertTrue(saida.contains("não encontrado"));
    }

    @Test
    void buscaPorNomeParcial() throws Exception {
        cadastrarCltEPj();

        String saida = executar("7\nana\nn\n0\n");

        assertTrue(saida.contains("Ana Lima"));
    }

    @Test
    void buscaPorNomeExato() throws Exception {
        cadastrarCltEPj();

        String saida = executar("7\nana lima\ns\n0\n");

        assertTrue(saida.contains("Ana Lima"));
    }

    @Test
    void buscaExataComNomeParcialMostraErro() throws Exception {
        cadastrarCltEPj();

        String saida = executar("7\nana\ns\n0\n");

        assertTrue(saida.contains("Nenhum funcionário encontrado"));
    }

    // ---------- remover ----------

    @Test
    void removeFuncionarioCadastrado() throws Exception {
        cadastrarCltEPj();

        String saida = executar("8\n1\n0\n");

        assertTrue(saida.contains("removido com sucesso"));
        assertEquals(1, service.listar().size());
    }

    @Test
    void removerFuncionarioInexistenteMostraErro() {
        String saida = executar("8\n99\n0\n");

        assertTrue(saida.contains("não encontrado"));
    }

    // ---------- dados completos / remuneração ----------

    @Test
    void exibeOsDadosCompletosDoFuncionario() throws Exception {
        cadastrarCltEPj();

        String saida = executar("9\n1\n0\n");

        assertTrue(saida.contains("529.982.247-25"));
        assertTrue(saida.contains("Adicional de benefícios"));
    }

    @Test
    void exibirDadosDeFuncionarioInexistenteMostraErro() {
        String saida = executar("9\n99\n0\n");

        assertTrue(saida.contains("não encontrado"));
    }

    @Test
    void calculaARemuneracaoDoFuncionario() throws Exception {
        cadastrarCltEPj();

        String saida = executar("10\n1\n0\n");

        assertTrue(saida.contains("3.500,00"));
    }

    @Test
    void calcularRemuneracaoDeFuncionarioInexistenteMostraErro() {
        String saida = executar("10\n99\n0\n");

        assertTrue(saida.contains("não encontrado"));
    }

    // ---------- relatório / folha ----------

    @Test
    void geraORelatorioDeFuncionarios() throws Exception {
        cadastrarCltEPj();

        String saida = executar("11\n0\n");

        assertTrue(saida.contains("RELATÓRIO"));
        assertTrue(saida.contains("Ana Lima"));
        assertTrue(saida.contains("Bruno Costa"));
        assertTrue(saida.contains("TOTAL DA FOLHA"));
    }

    @Test
    void calculaOTotalDaFolhaDePagamento() throws Exception {
        cadastrarCltEPj();

        String saida = executar("12\n0\n");

        assertTrue(saida.contains("9.500,00"));
    }

    @Test
    void fluxoCompletoCadastraTresCategoriasECalculaATotalDaFolha() {
        String entrada = "1\n1\nAna Lima\n52998224725\n3000\n500\n"
                + "2\n2\nBruno Costa\n11144477735\n1500\n4\n"
                + "3\n3\nCarla Dias\n16899535009\n2000\n10\n10000\n"
                + "12\n0\n";

        String saida = executar(entrada);

        assertEquals(3, service.listar().size());
        assertTrue(saida.contains("12.500,00"));
    }
}
