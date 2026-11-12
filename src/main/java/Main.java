import java.util.Scanner;
import repository.FuncionarioRepository;
import service.FuncionarioService;
import view.FuncionarioView;

/**
 * Ponto de entrada: monta as camadas (repository -> service -> view) e inicia o menu.
 */
public class Main {

    public static void main(String[] args) {
        FuncionarioRepository repository = new FuncionarioRepository();
        FuncionarioService service = new FuncionarioService(repository);

        try (Scanner scanner = new Scanner(System.in)) {
            new FuncionarioView(service, scanner, System.out).iniciar();
        }
    }
}
