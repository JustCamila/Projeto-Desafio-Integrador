package scr;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}
