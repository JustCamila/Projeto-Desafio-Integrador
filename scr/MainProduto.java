package scr;
import java.util.Scanner;

public class MainProduto {
    public static void main(String[] args) {
        Scanner leitor = new Scanner(System.in);
        ProdutoDAO produtoDAO = new ProdutoDAO();

        System.out.println("--------------------CADASTRO DE PRODUTO--------------------");
        
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
            produtoDAO.criarProduto(novoProduto);

        } catch (IllegalArgumentException e) {
            System.err.println("\n Erro de Validação: " + e.getMessage());
        }
        
        leitor.close();
    }
}