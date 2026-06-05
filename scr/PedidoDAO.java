package scr;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {
    private final ItemPedidoDAO itemPedidoDAO = new ItemPedidoDAO();

    // cria o pedido, valida estoque de forma bem segura e diminui o estoque no banco
    public boolean criarPedido(Pedido pedido) {
        String sqlPedido = "INSERT INTO pedido (cliente_email, status) VALUES (?, ?)";
        String sqlSubtraiEstoque = "UPDATE produto SET estoque = estoque - ? WHERE nome_produto = ? AND estoque >= ?";

        Connection conn = null;
        try {
            conn = Conexao.conectar();
            conn.setAutoCommit(false); // aqui começa a transação

            // ele verifica e atualiza o estoque de todos os itens condicionalmente
            for (ItemPedido item : pedido.getItens()) {
                try (PreparedStatement stmtEstoque = conn.prepareStatement(sqlSubtraiEstoque)) {
                    stmtEstoque.setInt(1, item.getQuantidade());
                    stmtEstoque.setString(2, item.getProduto().getNome_produto());
                    stmtEstoque.setInt(3, item.getQuantidade()); // o estoque deve tenq ser maior ou igual a quantidade do pedido

                    int linhasAfetadas = stmtEstoque.executeUpdate();
                    if (linhasAfetadas == 0) {
                        // se der 0 significa que o estoque era insuficiente
                        conn.rollback(); // cancela tudo feito ate agora
                        return false; // pedido recusado por falta de estoque
                    }
                }
            }

            // se todos tinham estoque ai insere o pedido
            int idPedidoGerado = 0;
            try (PreparedStatement stmtPedido = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                stmtPedido.setString(1, pedido.getCliente().getEmail());
                stmtPedido.setString(2, pedido.getStatus().name());
                stmtPedido.executeUpdate();

                try (ResultSet rs = stmtPedido.getGeneratedKeys()) {
                    if (rs.next()) {
                        idPedidoGerado = rs.getInt(1);
                    }
                }
            }

            // salvaria os itens vinculados a esse id de pedido gerado
            itemPedidoDAO.salvarItens(pedido.getItens(), idPedidoGerado, conn);

            conn.commit(); // confirma tudo no banco de dados com segurança
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    // pega um pedido na fila,e muda para processando de forma atômica
    public Pedido buscarProximoDaFila() {
        String sqlBuscar = "SELECT id, cliente_email FROM pedido WHERE status = 'FILA' LIMIT 1 FOR UPDATE";
        String sqlAtualizar = "UPDATE pedido SET status = 'PROCESSANDO' WHERE id = ?";

        Connection conn = null;
        try {
            conn = Conexao.conectar();
            conn.setAutoCommit(false); 

            try (PreparedStatement stmtBuscar = conn.prepareStatement(sqlBuscar);
                 ResultSet rs = stmtBuscar.executeQuery()) {
                
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String emailCliente = rs.getString("cliente_email");

                    // ele altera imediatamente para processando para que nenhuma outra thread pegue o mesmo pedido
                    try (PreparedStatement stmtAtualizar = conn.prepareStatement(sqlAtualizar)) {
                        stmtAtualizar.setInt(1, id);
                        stmtAtualizar.executeUpdate();
                    }

                    // busca os dados e simula, vai utilizar o email do cliente tbm
                    Cliente cliente = new Cliente("Cliente do Banco", emailCliente); 
                    
                    // agora busca os itens do pedido para montar o objeto completo do pedido
                    List<ItemPedido> itens = itemPedidoDAO.buscarPorPedido(id, conn);

                    Pedido pedido = new Pedido(id, cliente, StatusPedido.PROCESSANDO, itens);
                    conn.commit();
                    return pedido;
                }
            }
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return null;
    }

    // alteraria o status do pedido, e é usado pela thread para finalizar o pedido
    public void atualizarStatus(int idPedido, StatusPedido novoStatus) {
        String sql = "UPDATE pedido SET status = ? WHERE id = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, novoStatus.name());
            stmt.setInt(2, idPedido);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
