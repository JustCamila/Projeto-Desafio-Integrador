package scr;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProdutoDAO {

    public void criarProduto(Produto produto){
        String sql = "INSERT INTO produtos (nome_produto, estoque, preco, categoria) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar(); 
        PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.getNome_produto());
            stmt.setInt(2, produto.getEstoque());
            stmt.setDouble(3, produto.getPreco());
            stmt.setString(4, produto.getCategoria());

            stmt.executeUpdate();
            System.out.println("Produto cadastrado!");

        } catch (SQLException e) {
            System.err.println("Erro aos salvar: " + e.getMessage());
        }
    }
    
}
