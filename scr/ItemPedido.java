package scr;

public class ItemPedido {
    private final int id; 
    private final Produto produto;
    private final int quantidade;
    private final double precoUnitario;

    //construtorzinho para quando criarmos um novo item
    public ItemPedido(Produto produto, int quantidade) {
        if (produto == null) {
            throw new IllegalArgumentException("É obrigatório o produto no item do pedido.");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade do item deve ser maior que zero.");
        }
        
        this.id = 0; 
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = produto.getPreco();
    }

    //construtorzinho para quando formos ler um item do banquin de dados
    public ItemPedido(int id, Produto produto, int quantidade, double precoUnitario) {
        this.id = id;
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }


public int getId() {
        return id;
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    // novo método para calcular o subtotal do item
    public double getSubtotal() {
        return this.quantidade * this.precoUnitario;
    }
}
