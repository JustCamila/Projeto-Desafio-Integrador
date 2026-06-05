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
}


