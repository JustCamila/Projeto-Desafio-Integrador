package scr;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ClienteDAO {

    public void salvar(Cliente cliente) {
        String sql = "INSERT INTO clientes (nome, email, telefone, logradouro, bairro) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(2, cliente.getTelefone());
            stmt.setString(2, cliente.getLogradouro());
            stmt.setString(2, cliente.getBairro());

            stmt.executeUpdate();
            System.out.println("Cliente salvo!");

        } catch (SQLException e) {
            System.err.println("Erro ao salvar: " + e.getMessage());
        }
    }

public List<Cliente> listarTodos() {
    List<Cliente> clientes = new ArrayList<>();
    String sql = "SELECT nome, email, telefone, logradouro, bairro FROM clientes"; // <-- puxa todos os dados do banco 

    try (Connection conn = Conexao.conectar();  // o try está aqui pra garantir que seja fechado corretamente mesmo ouvendo um erro na execução
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            String nome = rs.getString("nome");
            String email = rs.getString("email");
            String telefone = rs.getString("telefone");
            String logradouro = rs.getString("logradouro");
            String bairro = rs.getString("bairro");

            Cliente cliente = new Cliente(nome, email, telefone, logradouro, bairro);
            clientes.add(cliente);
        }

    } catch (SQLException e) {
        System.err.println("Erro ao listar clientes: " + e.getMessage());
    }

    return clientes;
}


}
