package scr;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner leitor = new Scanner(System.in);
        ClienteDAO dao = new ClienteDAO();

        System.out.println("-------------------Cadastro Cliente-------------------");

        System.out.print("Digite o nome: ");
        String nomeDigitado = leitor.nextLine();

        System.out.print("Digite o email: ");
        String emailDigitado = leitor.nextLine();

        try {
        
            Cliente novoCliente = new Cliente(nomeDigitado, emailDigitado);
            dao.salvar(novoCliente);

        } catch (IllegalArgumentException e) {
            System.err.println("Erro: " + e.getMessage());
        }

        leitor.close();
    }
}
 

