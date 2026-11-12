package repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import model.Funcionario;
import model.FuncionarioCLT;
import model.FuncionarioComissionado;
import model.FuncionarioPJ;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FuncionarioRepositoryTest {

    private FuncionarioRepository repository;
    private Funcionario clt;
    private Funcionario pj;
    private Funcionario comissionado;

    @BeforeEach
    void setUp() {
        repository = new FuncionarioRepository();
        clt = new FuncionarioCLT(1, "Ana Lima", "52998224725", 3000.0, 500.0);
        pj = new FuncionarioPJ(2, "Bruno Costa", "11144477735", 1500.0, 4);
        comissionado = new FuncionarioComissionado(3, "Carla Dias", "16899535009", 2000.0, 10.0, 10000.0);
    }

    @Test
    void repositorioNovoComecaVazio() {
        assertEquals(0, repository.quantidade());
        assertTrue(repository.listarTodos().isEmpty());
    }

    @Test
    void salvaEBuscaPorCodigo() {
        repository.salvar(clt);

        Optional<Funcionario> encontrado = repository.buscarPorCodigo(1);

        assertTrue(encontrado.isPresent());
        assertSame(clt, encontrado.get());
    }

    @Test
    void buscarPorCodigoInexistenteRetornaVazio() {
        assertFalse(repository.buscarPorCodigo(99).isPresent());
    }

    @Test
    void salvarFuncionarioNuloLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> repository.salvar(null));
    }

    @Test
    void existeInformaSeOCodigoEstaCadastrado() {
        repository.salvar(clt);

        assertTrue(repository.existe(1));
        assertFalse(repository.existe(2));
    }

    @Test
    void quantidadeAcompanhaOsFuncionariosSalvos() {
        repository.salvar(clt);
        repository.salvar(pj);

        assertEquals(2, repository.quantidade());
    }

    @Test
    void listarTodosMantemAOrdemDeInsercao() {
        repository.salvar(comissionado);
        repository.salvar(clt);
        repository.salvar(pj);

        List<Funcionario> lista = repository.listarTodos();

        assertEquals(3, lista.size());
        assertSame(comissionado, lista.get(0));
        assertSame(clt, lista.get(1));
        assertSame(pj, lista.get(2));
    }

    @Test
    void listarTodosRetornaUmaCopiaProtegida() {
        repository.salvar(clt);

        List<Funcionario> lista = repository.listarTodos();
        lista.clear();

        assertEquals(1, repository.quantidade());
    }

    @Test
    void removeFuncionarioExistente() {
        repository.salvar(clt);
        repository.salvar(pj);

        boolean removido = repository.remover(1);

        assertTrue(removido);
        assertFalse(repository.existe(1));
        assertEquals(1, repository.quantidade());
    }

    @Test
    void removerFuncionarioInexistenteRetornaFalse() {
        assertFalse(repository.remover(99));
    }

    @Test
    void armazenaFuncionariosDeCategoriasDiferentes() {
        repository.salvar(clt);
        repository.salvar(pj);
        repository.salvar(comissionado);

        assertTrue(repository.buscarPorCodigo(1).get() instanceof FuncionarioCLT);
        assertTrue(repository.buscarPorCodigo(2).get() instanceof FuncionarioPJ);
        assertTrue(repository.buscarPorCodigo(3).get() instanceof FuncionarioComissionado);
    }
}
