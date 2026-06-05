import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

package scr;

public class Pedido {
    private final int id;
    private final Cliente cliente;
    private final List<ItemPedido> itens;
    private StatusPedido status;

// contrutorzin para criar um novo pedido

public Pedido(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("O pedido precisa ter um cliente associado.");
        }
        this.id = 0;
        this.cliente = cliente;
        this.status = StatusPedido.ABERTO;
        this.itens = new ArrayList<>();
    }

//construtor para quando o banco instanciar um pedido já existente
    public Pedido(int id, Cliente cliente, StatusPedido status, List<ItemPedido> itens) {
        this.id = id;
        this.cliente = cliente;
        this.status = status;
        this.itens = itens != null ? itens : new ArrayList<>();
    }

    // método para adicionar um item ao pedido
    public void adicionarItem(ItemPedido item) {
        if (item == null) {
            throw new IllegalArgumentException("Não é possível adicionar um item nulo.");
        }
        this.itens.add(item);
    }

    public int getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }
    //retorna uma lista imutável dos itens do pedido para evitar modificações externas
    public List<ItemPedido> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public StatusPedido getStatus() {
        return status;
    }
   // como o status muda ao longo do ciclo, ele 
    public void setStatus(StatusPedido status) {
        if (status == null) {
            throw new IllegalArgumentException("O status do pedido não pode ser nulo.");
        }
        this.status = status;
    }
    // método para calcular o valor total do pedido somando os itens
    public double getTotalPedido() {
        double total = 0.0;
        for (ItemPedido item : itens) {
            total += item.getSubtotal();
        }
        return total;
    }


}




