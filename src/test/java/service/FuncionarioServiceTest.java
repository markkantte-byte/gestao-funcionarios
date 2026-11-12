package service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import exception.FuncionarioJaCadastradoException;
import exception.FuncionarioNaoEncontradoException;
import java.util.List;
import model.Funcionario;
import model.FuncionarioCLT;
import model.FuncionarioComissionado;
import model.FuncionarioPJ;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.FuncionarioRepository;

public class FuncionarioServiceTest {

    private static final String CPF_1 = "52998224725";
    private static final String CPF_2 = "11144477735";
    private static final String CPF_3 = "16899535009";

    private FuncionarioService service;
    private Funcionario clt;
    private Funcionario pj;
    private Funcionario comissionado;

    @BeforeEach
    void setUp() {
        service = new FuncionarioService(new FuncionarioRepository());
        clt = new FuncionarioCLT(1, "Ana Lima", CPF_1, 3000.0, 500.0);               // 3500
        pj = new FuncionarioPJ(2, "Bruno Costa", CPF_2, 1500.0, 4);                  // 6000
        comissionado = new FuncionarioComissionado(3, "Carla Dias", CPF_3, 2000.0, 10.0, 10000.0); // 3000
    }

    private void cadastrarTodos() throws FuncionarioJaCadastradoException {
        service.cadastrar(clt);
        service.cadastrar(pj);
        service.cadastrar(comissionado);
    }

    // ---------- construtor ----------

    @Test
    void construtorComRepositorioNuloLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> new FuncionarioService(null));
    }

    // ---------- cadastrar / listar ----------

    @Test
    void cadastraEListaFuncionarios() throws Exception {
        service.cadastrar(clt);
        service.cadastrar(pj);

        List<Funcionario> lista = service.listar();

        assertEquals(2, lista.size());
        assertSame(clt, lista.get(0));
        assertSame(pj, lista.get(1));
    }

    @Test
    void listarSemFuncionariosRetornaListaVazia() {
        assertTrue(service.listar().isEmpty());
    }

    @Test
    void cadastrarFuncionarioNuloLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(null));
    }

    @Test
    void cadastrarCodigoDuplicadoLancaFuncionarioJaCadastradoException() throws Exception {
        service.cadastrar(clt);
        Funcionario outroComMesmoCodigo = new FuncionarioPJ(1, "Outra Pessoa", CPF_2, 1000.0, 1);

        assertThrows(FuncionarioJaCadastradoException.class,
                () -> service.cadastrar(outroComMesmoCodigo));

        assertEquals(1, service.listar().size());
        assertSame(clt, service.listar().get(0));
    }

    // ---------- buscar por código ----------

    @Test
    void buscaFuncionarioPorCodigo() throws Exception {
        cadastrarTodos();

        assertSame(pj, service.buscarFuncionario(2));
    }

    @Test
    void buscarPorCodigoInexistenteLancaFuncionarioNaoEncontradoException() throws Exception {
        cadastrarTodos();

        assertThrows(FuncionarioNaoEncontradoException.class, () -> service.buscarFuncionario(99));
    }

    // ---------- buscar por CPF ----------

    @Test
    void buscaFuncionarioPorCpfSemMascara() throws Exception {
        cadastrarTodos();

        assertSame(comissionado, service.buscarFuncionario(CPF_3));
    }

    @Test
    void buscaFuncionarioPorCpfComMascara() throws Exception {
        cadastrarTodos();

        assertSame(clt, service.buscarFuncionario("529.982.247-25"));
    }

    @Test
    void buscarPorCpfInexistenteLancaFuncionarioNaoEncontradoException() throws Exception {
        cadastrarTodos();

        assertThrows(FuncionarioNaoEncontradoException.class,
                () -> service.buscarFuncionario("00000000000"));
    }

    @Test
    void buscarPorCpfNuloLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> service.buscarFuncionario((String) null));
    }

    // ---------- buscar por nome ----------

    @Test
    void buscaPorNomeExatoIgnorandoMaiusculasEMinusculas() throws Exception {
        cadastrarTodos();

        List<Funcionario> encontrados = service.buscarFuncionario("ana lima", true);

        assertEquals(1, encontrados.size());
        assertSame(clt, encontrados.get(0));
    }

    @Test
    void buscaExataNaoEncontraNomeParcial() throws Exception {
        cadastrarTodos();

        assertThrows(FuncionarioNaoEncontradoException.class,
                () -> service.buscarFuncionario("Ana", true));
    }

    @Test
    void buscaPorNomeParcialEncontraTodosQueContem() throws Exception {
        cadastrarTodos();
        service.cadastrar(new FuncionarioCLT(4, "Mariana Souza", "39053344705", 2000.0, 0.0));

        List<Funcionario> encontrados = service.buscarFuncionario("ana", false);

        assertEquals(2, encontrados.size());
        assertSame(clt, encontrados.get(0));
        assertEquals("Mariana Souza", encontrados.get(1).getNome());
    }

    @Test
    void buscarPorNomeInexistenteLancaFuncionarioNaoEncontradoException() throws Exception {
        cadastrarTodos();

        assertThrows(FuncionarioNaoEncontradoException.class,
                () -> service.buscarFuncionario("Zeca", false));
    }

    @Test
    void buscarPorNomeNuloOuEmBrancoLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> service.buscarFuncionario(null, true));
        assertThrows(IllegalArgumentException.class, () -> service.buscarFuncionario("  ", false));
    }

    // ---------- remover ----------

    @Test
    void removeFuncionarioCadastrado() throws Exception {
        cadastrarTodos();

        service.remover(2);

        assertEquals(2, service.listar().size());
        assertThrows(FuncionarioNaoEncontradoException.class, () -> service.buscarFuncionario(2));
    }

    @Test
    void removerFuncionarioInexistenteLancaFuncionarioNaoEncontradoException() {
        assertThrows(FuncionarioNaoEncontradoException.class, () -> service.remover(99));
    }

    @Test
    void aposRemoverEPossivelCadastrarOMesmoCodigoNovamente() throws Exception {
        service.cadastrar(clt);
        service.remover(1);

        assertDoesNotThrow(() -> service.cadastrar(clt));
    }

    // ---------- remuneração (polimorfismo) ----------

    @Test
    void calculaARemuneracaoDeCadaCategoriaPeloCodigo() throws Exception {
        cadastrarTodos();

        assertEquals(3500.0, service.calcularRemuneracao(1), 0.001);
        assertEquals(6000.0, service.calcularRemuneracao(2), 0.001);
        assertEquals(3000.0, service.calcularRemuneracao(3), 0.001);
    }

    @Test
    void calcularRemuneracaoDeFuncionarioInexistenteLancaExcecao() {
        assertThrows(FuncionarioNaoEncontradoException.class,
                () -> service.calcularRemuneracao(99));
    }

    // ---------- dados completos ----------

    @Test
    void exibeOsDadosCompletosDeUmFuncionario() throws Exception {
        cadastrarTodos();

        String dados = service.exibirDados(1);

        assertTrue(dados.contains("Ana Lima"));
        assertTrue(dados.contains("529.982.247-25"));
        assertTrue(dados.contains("CLT"));
    }

    @Test
    void exibirDadosDeFuncionarioInexistenteLancaExcecao() {
        assertThrows(FuncionarioNaoEncontradoException.class, () -> service.exibirDados(99));
    }

    // ---------- folha de pagamento ----------

    @Test
    void calculaOTotalDaFolhaDePagamento() throws Exception {
        cadastrarTodos();

        // 3500 + 6000 + 3000
        assertEquals(12500.0, service.calcularTotalFolha(), 0.001);
    }

    @Test
    void totalDaFolhaSemFuncionariosEZero() {
        assertEquals(0.0, service.calcularTotalFolha(), 0.001);
    }

    @Test
    void totalDaFolhaAcompanhaRemocoes() throws Exception {
        cadastrarTodos();

        service.remover(2);

        assertEquals(6500.0, service.calcularTotalFolha(), 0.001);
    }

    // ---------- relatório ----------

    @Test
    void relatorioContemTodosOsFuncionariosEOTotal() throws Exception {
        cadastrarTodos();

        String relatorio = service.gerarRelatorio();

        assertTrue(relatorio.contains("Ana Lima"));
        assertTrue(relatorio.contains("Bruno Costa"));
        assertTrue(relatorio.contains("Carla Dias"));
        assertTrue(relatorio.contains("3.500,00"));
        assertTrue(relatorio.contains("6.000,00"));
        assertTrue(relatorio.contains("3.000,00"));
        assertTrue(relatorio.contains("TOTAL"));
        assertTrue(relatorio.contains("12.500,00"));
    }

    @Test
    void relatorioSemFuncionariosInformaQueEstaVazio() {
        String relatorio = service.gerarRelatorio();

        assertTrue(relatorio.contains("Nenhum funcionário cadastrado"));
    }
}
