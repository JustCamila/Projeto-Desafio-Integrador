package scr;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ClienteDAO {

    public void salvar(Cliente cliente) {
        String sql = "INSERT INTO clientes (nome, email) VALUES (?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEmail());

            stmt.executeUpdate();
            System.out.println("Cliente salvo!");

        } catch (SQLException e) {
            System.err.println("Erro ao salvar: " + e.getMessage());
        }
    }

public List<Cliente> listarTodos() {
    List<Cliente> clientes = new ArrayList<>();
    String sql = "SELECT nome, email FROM clientes"; // <-- puxa os nomes e emails do banco 

    try (Connection conn = Conexao.conectar();  // o try está aqui pra garantir que seja fechado corretamente mesmo ouvendo um erro na execução
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            String nome = rs.getString("nome");
            String email = rs.getString("email");

            Cliente cliente = new Cliente(nome, email);
            clientes.add(cliente);
        }

    } catch (SQLException e) {
        System.err.println("Erro ao listar clientes: " + e.getMessage());
    }

    return clientes;
}


}
