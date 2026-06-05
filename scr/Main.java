package scr;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner leitor = new Scanner(System.in);
        ClienteDAO clientedao = new ClienteDAO();
        ProdutoDAO produtodao = new ProdutoDAO();

        int opcao = 0;

        System.out.println("-----------Bem-vindo(a)!-----------");

        while (opcao != 4) {
            System.out.println("\n1- Cadastro de Cliente");
            System.out.println("2- Cadastro de Produtos");
            System.out.println("3- Consultas e Relatórios"); 
            System.out.println("4- Sair do sistema...");       
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

                    System.out.print("Digite o telefone: ");
                    String telefoneDigitado = leitor.nextLine();

                    System.out.print("Digite o logradouro: ");
                    String logradouroDigitado = leitor.nextLine();

                    System.out.print("Digite o bairro: ");
                    String bairroDigitado = leitor.nextLine();

                    try {
                        Cliente novoCliente = new Cliente(nomeDigitado, emailDigitado, telefoneDigitado, logradouroDigitado, bairroDigitado);
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
                    System.out.println("\n==================================");
                    System.out.println("CONSULTAS E RELATÓRIOS ");
                    System.out.println("==================================");
                    System.out.println("1 - Listar Todos os Clientes");
                    System.out.println("2 - Listar Todos os Produtos");
                    System.out.println("3 - Relatorio estoque por categoria");
                    System.out.println("4 - Relatorio Media preço por categoria");
                    System.out.println("5 - Voltar ao Menu Principal");
                    System.out.print("Escolha uma opção: ");
                    int opcaoSubmenu = leitor.nextInt();
                    leitor.nextLine(); 

                    switch (opcaoSubmenu) {
                        case 1:
                            System.out.println("\n --- LISTA DE CLIENTES ---");
                            // Chama o método listarTodos() do ClienteDAO
                            List<Cliente> listaClientes = clientedao.listarTodos();
                            
                            // Mostra cada cliente do banco
                            for (Cliente c : listaClientes) {
                                System.out.println("-> Nome: " + c.getNome() + " | Email: " + c.getEmail());
                            }
                            break;

                        case 2:
                            System.out.println("\n --- LISTA DE PRODUTOS ---");
                            // Chama o método listarTodos() do ProdutoDAO
                            List<Produto> listaProdutos = produtodao.listarTodos();

                            // Mostra cada produto do banco
                            for (Produto p : listaProdutos) {
                                System.out.println("-> " + p.getNome_produto() + "\nEstoque: " + p.getEstoque() + "\nPreço: R$ " + p.getPreco() + "\nCategoria: " + p.getCategoria() + "\n");
                            }
                            break;
                            
                        case 3: 
                            System.out.println("\n --- Relatorio Estoque disponivel ---");
                            produtodao.relatorioEstoque();

                        case 4: 
                            System.out.println("\n --- Relatorio Media de preco por categoria ---");
                            produtodao.relatorioMedia();

                        case 5:
                            System.out.println("\nVoltando ao menu principal...");
                            break;

                        default:
                            System.out.println("Opção inválida!");
                            break;
                    }
                    break;

                case 4:
                    System.out.println("Encerrando o sistema...");
                    break;

                default:
                    System.out.println("Opção invalida!");
            }
        }

        leitor.close();
    }
}