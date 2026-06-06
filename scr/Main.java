package scr;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner leitor = new Scanner(System.in);
        ClienteDAO clientedao = new ClienteDAO();
        ProdutoDAO produtodao = new ProdutoDAO();
        PedidoDAO pedidodao = new PedidoDAO(); 

        ProcessadorAssincrono processador = new ProcessadorAssincrono();
        Thread threadProcessamento = new Thread(processador);
        threadProcessamento.start(); 

        int opcao = 0;

        System.out.println("-----------Bem-vindo(a)!-----------");

        while (opcao != 5) {
            System.out.println("\n1- Cadastro de Cliente");
            System.out.println("2- Cadastro de Produtos");
            System.out.println("3- Novo Pedido");
            System.out.println("4- Consultas e Relatórios"); 
            System.out.println("5- Sair do sistema...");       
            System.out.print("Escolha uma opção: ");

            opcao = leitor.nextInt();
            leitor.nextLine(); 
            switch (opcao) {
                case 1:
                    System.out.println("\n-------------------CADASTRO CLIENTE-------------------");
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
                        System.err.println("Erro ao cadastrar cliente: " + e.getMessage());
                    }
                    break;

                case 2:
                    System.out.println("\n-------------------CADASTRO PRODUTO-------------------");
                    System.out.print("Digite o nome do produto: ");
                    String nomeProdDigitado = leitor.nextLine();

                    System.out.print("Digite a quantidade em estoque: ");
                    int estoqueDigitado = leitor.nextInt();

                    System.out.print("Digite o preço: ");
                    double precoDigitado = leitor.nextDouble();
                    leitor.nextLine(); 

                    System.out.println("Escolha a categoria:");
                    System.out.println("1 - CAFES_QUENTES");
                    System.out.println("2 - BEBIDAS_GELADAS");
                    System.out.println("3 - COOKIES_TRADICIONAIS");
                    System.out.println("4 - COOKIES_RECHEADOS");
                    System.out.print("Opção: ");
                    int catOpcao = leitor.nextInt();
                    leitor.nextLine(); 

                    Categoria categoriaSelecionada = Categoria.CAFES_QUENTES;
                    if (catOpcao == 2) categoriaSelecionada = Categoria.BEBIDAS_GELADAS;
                    else if (catOpcao == 3) categoriaSelecionada = Categoria.COOKIES_TRADICIONAIS;
                    else if (catOpcao == 4) categoriaSelecionada = Categoria.COOKIES_RECHEADOS;

                    try {
                        Produto novoProduto = new Produto(nomeProdDigitado, estoqueDigitado, precoDigitado, categoriaSelecionada);
                        produtodao.criarProduto(novoProduto);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Erro ao cadastrar produto: " + e.getMessage());
                    }
                    break;

                case 3:
                    System.out.println("\n-------------------NOVO PEDIDO-------------------");
                    System.out.print("Digite o nome do cliente para o pedido: ");
                    String nomeBuscaCliente = leitor.nextLine();

                    Cliente clienteSelecionado = null;
                    List<Cliente> clientesDoBanco = clientedao.listarTodos();
                    for (Cliente c : clientesDoBanco) {
                        if (c.getNome().equalsIgnoreCase(nomeBuscaCliente)) {
                            clienteSelecionado = c;
                            break;
                        }
                    }

                    if (clienteSelecionado == null) {
                        System.out.println("Aviso: Cliente não cadastrado no banco. Gerando perfil temporário...");
                        clienteSelecionado = new Cliente(nomeBuscaCliente, "cliente@temporario.com", "9999-9999", "Rua Padrão", "Bairro Padrão");
                    }

                    Pedido novoPedido = new Pedido(clienteSelecionado);
                    boolean adicionandoItens = true;

                    while (adicionandoItens) {
                        System.out.print("Nome do produto a adicionar: ");
                        String nomeProdBusca = leitor.nextLine();

                        Produto produtoSelecionado = null;
                        List<Produto> produtosDoBanco = produtodao.listarTodos();
                        for (Produto p : produtosDoBanco) {
                            if (p.getNome_produto().equalsIgnoreCase(nomeProdBusca)) {
                                produtoSelecionado = p;
                                break;
                            }
                        }

                        if (produtoSelecionado == null) {
                            System.out.println("Erro: Produto '" + nomeProdBusca + "' não encontrado no banco!");
                        } else {
                            System.out.print("Digite a quantidade desejada: ");
                            int qtdItem = leitor.nextInt();
                            leitor.nextLine();

                            try {
                                ItemPedido novoItem = new ItemPedido(produtoSelecionado, qtdItem);
                                novoPedido.adicionarItem(novoItem);
                                System.out.println("Item inserido! Subtotal do item: R$ " + novoItem.getSubtotal());
                            } catch (IllegalArgumentException e) {
                                System.err.println("Erro ao inserir item: " + e.getMessage());
                            }
                        }

                        System.out.print("Deseja adicionar outro produto? (S/N): ");
                        String resposta = leitor.nextLine();
                        if (resposta.equalsIgnoreCase("N")) {
                            adicionandoItens = false;
                        }
                    }

                    if (!novoPedido.getItens().isEmpty()) {
                        System.out.println("\nEnviando pedido para o banco...");
                        pedidodao.criarPedido(novoPedido);
                    } else {
                        System.out.println("Pedido descartado: nenhum item foi adicionado.");
                    }
                    break;

                case 4:
                    int opcaoRelatorio = 0;
                    while (opcaoRelatorio != 5) {
                        System.out.println("\n--- Consultas e Relatórios ---");
                        System.out.println("1 - Listar Todos os Clientes");
                        System.out.println("2 - Listar Todos os Produtos");
                        System.out.println("3 - Relatório Estoque Disponível");
                        System.out.println("4 - Relatório Média de Preço por Categoria");
                        System.out.println("5 - Voltar ao Menu Principal");
                        System.out.print("Escolha uma opção: ");
                        
                        opcaoRelatorio = leitor.nextInt();
                        leitor.nextLine();

                        switch (opcaoRelatorio) {
                            case 1:
                                System.out.println("\n --- Lista de Clientes ---");
                                List<Cliente> listaClientes = clientedao.listarTodos();
                                for (Cliente c : listaClientes) {
                                    System.out.println("Nome: " + c.getNome() + " | Email: " + c.getEmail() + " | Tel: " + c.getTelefone());
                                }
                                break;

                            case 2:
                                System.out.println("\n --- Lista de Produtos ---");
                                List<Produto> listaProdutos = produtodao.listarTodos();
                                for (Produto p : listaProdutos) {
                                    System.out.println("-> " + p.getNome_produto() + "\nEstoque: " + p.getEstoque() + "\nPreço: R$ " + p.getPreco() + "\nCategoria: " + p.getCategoria() + "\n");
                                }
                                break;
                            
                            case 3: 
                                System.out.println("\n --- Relatorio Estoque disponível ---");
                                produtodao.relatorioEstoque();
                                break;

                            case 4: 
                                System.out.println("\n --- Relatorio Média de preço por categoria ---");
                                produtodao.relatorioMedia();
                                break;

                            case 5:
                                System.out.println("\nVoltando ao menu principal...");
                                break;

                            default:
                                System.out.println("Opção inválida!");
                                break;
                        }
                    }
                    break;

                case 5:
                    System.out.println("\nEncerrando o sistema...");
                    
                    processador.parar(); 
                    threadProcessamento.interrupt(); 
                    break;

                default:
                    System.out.println("Opção inválida!");
            }
        }

        leitor.close();
    }
}