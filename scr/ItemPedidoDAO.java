package scr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemPedidoDAO {

    // salvaria os itens de um pedido no banco de daduss
    public void salvarItens(List<ItemPedido> itens, int idPedido, Connection conn) throws SQLException {
        String sql = "INSERT INTO itens_pedido (pedido_id, produto_nome, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (ItemPedido item : itens) {
                stmt.setInt(1, idPedido);
                stmt.setString(2, item.getProduto().getNome_produto()); 
                stmt.setInt(3, item.getQuantidade());
                stmt.setDouble(4, item.getPrecoUnitario());
                stmt.addBatch(); 
            }
            stmt.executeBatch();
        }
    }

    // buscaria todos os itens de um pedido específico usado nos relatórios
    public List<ItemPedido> buscarPorPedido(int idPedido, Connection conn) throws SQLException {
        List<ItemPedido> itens = new ArrayList<>();
        String sql = "SELECT ip.id, ip.quantidade, ip.preco_unitario, p.estoque, p.preco, p.categoria " +
                     "FROM itens_pedido ip " +
                     "JOIN produto p ON ip.produto_nome = p.nome_produto " +
                     "WHERE ip.pedido_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPedido);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // ele reconstruiria a categoria baseada no enum que você criou
                    Categoria cat = Categoria.valueOf(rs.getString("categoria"));
                    // cria o objeto produto com os dados atuais do banco
                    Produto prod = new Produto(rs.getString("produto_nome"), rs.getInt("estoque"), rs.getDouble("preco"), cat);
                    
                    // cria o item com o preço e o histórico da da compra na loja xamito delicias
                    ItemPedido item = new ItemPedido(
                        rs.getInt("id"),
                        prod,
                        rs.getInt("quantidade"),
                        rs.getDouble("preco_unitario")
                    );
                    itens.add(item);
                }
            }
        }
        return itens;
    }
}