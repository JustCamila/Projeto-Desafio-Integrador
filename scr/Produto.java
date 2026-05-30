package scr;

public class Produto {
    private final String nome_produto;
    private final int estoque;
    private final double preco;
	private final Categoria categoria;


    public Produto(String nome_produto, int estoque, double preco, Categoria categoria) {

        if (nome_produto == null || nome_produto.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do produto não pode ser vazio.");
        }
        if (preco <= 0) { 
            throw new IllegalArgumentException("Preço invalido.");
        }
        if (estoque < 0) { 
            throw new IllegalArgumentException("Estoque invalido.");
        }
        if (categoria == null) {
            throw new IllegalArgumentException("A categoria é obrigatória.");
        }

        this.nome_produto = nome_produto;
        this.estoque = estoque;
        this.preco = preco;
        this.categoria = categoria;
    }

    public String getNome_produto() {
        return nome_produto;
    }

    public int getEstoque() {
        return estoque;
    }

    public double getPreco() {
        return preco;
    }

    public String getCategoria() {
        return categoria.name();
    }
}
