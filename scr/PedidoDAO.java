package scr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class PedidoDAO {

    public void criarPedido(Pedido pedido) {

        String sqlPedido = "INSERT INTO pedido (cliente_id, status) VALUES ((SELECT id_cliente FROM clientes WHERE nome = ? LIMIT 1), ?)";
        
        String sqlItem = "INSERT INTO itens_pedidos (pedido_id, produto_id, quantidade_itens) VALUES (?, (SELECT id_produto FROM produtos WHERE nome_produto = ? LIMIT 1), ?)";
        
        String sqlEstoque = "UPDATE produtos SET estoque = estoque - ? WHERE nome_produto = ? AND estoque >= ?";

        Connection conn = null;

        try {
            conn = Conexao.conectar();
            conn.setAutoCommit(false);

            int idPedidoGerado = 0;
            try (PreparedStatement stmtPedido = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                stmtPedido.setString(1, pedido.getCliente().getNome());
                stmtPedido.setString(2, StatusPedido.FILA.name()); 
                stmtPedido.executeUpdate();

                try (ResultSet rs = stmtPedido.getGeneratedKeys()) {
                    if (rs.next()) {
                        idPedidoGerado = rs.getInt(1);
                    }
                }
            }

            if (idPedidoGerado == 0) {
                throw new SQLException("Cliente não encontrado no banco de dados para gerar a chave estrangeira.");
            }

            try (PreparedStatement stmtItem = conn.prepareStatement(sqlItem);
                 PreparedStatement stmtEstoque = conn.prepareStatement(sqlEstoque)) {

                for (ItemPedido item : pedido.getItens()) {
                    
                    stmtEstoque.setInt(1, item.getQuantidade());
                    stmtEstoque.setString(2, item.getProduto().getNome_produto());
                    stmtEstoque.setInt(3, item.getQuantidade());
                    
                    int linhasAfetadas = stmtEstoque.executeUpdate();

                    if (linhasAfetadas == 0) {
                        throw new SQLException("Estoque insuficiente para o produto: " + item.getProduto().getNome_produto());
                    }

                    stmtItem.setInt(1, idPedidoGerado);
                    stmtItem.setString(2, item.getProduto().getNome_produto());
                    stmtItem.setInt(3, item.getQuantidade());
                    stmtItem.executeUpdate();
                }
            }

            conn.commit(); 
            pedido.setStatus(StatusPedido.FILA); 
            System.out.println("Pedido gerado no banco! Status inicial: FILA. (Aguardando Thread de processamento...)");

        } catch (SQLException e) {
            System.err.println("Erro ao processar pedido, cancelando operação (Rollback)... Motivo: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback(); 
                } catch (SQLException ex) {
                    System.err.println("Erro crítico ao fazer o rollback: " + ex.getMessage());
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Erro ao fechar conexão: " + e.getMessage());
                }
            }
        }
    }
}