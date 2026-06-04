package scr;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
 
    public List<Produto> listarTodos() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT nome_produto, estoque, preco, categoria FROM produtos"; 

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {


            while (rs.next()) {
                String nome = rs.getString("nome_produto");
                int estoque = rs.getInt("estoque");
                double preco = rs.getDouble("preco");
                String categoriaTexto = rs.getString("categoria");

                Categoria categoriaEnum = Categoria.valueOf(categoriaTexto);

                // Cria o objeto Produto com os dados que vieram do banco
                Produto produto = new Produto(nome, estoque, preco, categoriaEnum);

                produtos.add(produto);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar produtos: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Erro na categoria do produto: " + e.getMessage());
        }

        return produtos;
    }
}




