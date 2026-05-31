package scr;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner leitor = new Scanner(System.in);
        ClienteDAO clientedao = new ClienteDAO();
        ProdutoDAO produtodao = new ProdutoDAO();

        int opcao = 0;

        System.out.println("-----------Bem-vindo(a)!-----------");

        while (opcao != 3) {
            System.out.println("1- Cadastro de Cliente");
            System.out.println("2- Cadastro de Produtos");
            System.out.println("3- Sair do sistema...");
            System.out.print("Escolha uma opção: ");

            opcao = leitor.nextInt();
            leitor.nextLine();

            switch (opcao) {
                case 1:
                    System.out.println(
        "\n-------------------CADASTRO CLIENTE-------------------");

                    System.out.print("Digite o nome: ");
                    String nomeDigitado = leitor.nextLine();

                    System.out.print("Digite o email: ");
                    String emailDigitado = leitor.nextLine();

                    try {

                        Cliente novoCliente = new Cliente(nomeDigitado, emailDigitado);
                        clientedao.salvar(novoCliente);

                    } catch (IllegalArgumentException e) {
                        System.err.println("Erro ao criar cliente: " + e.getMessage());
                    }
                    break;

                case 2:
                    System.out.println(
    "\n--------------------CADASTRO DE PRODUTO--------------------");

                    System.out.print("Nome do produto: ");
                    String nome = leitor.nextLine();

                    System.out.print("Estoque: ");
                    int estoque = leitor.nextInt();

                    System.out.print("Preço (R$): ");
                    double preco = leitor.nextDouble();

                    System.out.println("\nEscolha a Categoria do Produto:");
                    System.out.println("1 - Cafés Quentes");
                    System.out.println("2 - Bebidas Geladas");
                    System.out.println("3 - Cookies Tradicionais");
                    System.out.println("4 - Cookies Recheados");
                    System.out.print("Digite o número da opção: ");
                    int opcaoCategoria = leitor.nextInt();

                    Categoria categoriaEscolhida = null;
                    if (opcaoCategoria == 1) {
                        categoriaEscolhida = Categoria.CAFES_QUENTES;
                    } else if (opcaoCategoria == 2) {
                        categoriaEscolhida = Categoria.BEBIDAS_GELADAS;
                    } else if (opcaoCategoria == 3) {
                        categoriaEscolhida = Categoria.COOKIES_TRADICIONAIS;
                    } else if (opcaoCategoria == 4) {
                        categoriaEscolhida = Categoria.COOKIES_RECHEADOS;
                    }

                    try {
                        Produto novoProduto = new Produto(nome, estoque, preco, categoriaEscolhida);
                        produtodao.criarProduto(novoProduto);

                    } catch (IllegalArgumentException e) {
                        System.err.println("\n Erro de Validação: " + e.getMessage());
                    }
                break;

                case 3:
                    System.out.println("Encerrando o sistema...");
                break;

                default:
                    System.out.println("Opção invalida!");

            }

        }

        leitor.close();
    }
}
