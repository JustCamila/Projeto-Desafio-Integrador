package scr;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
private static final String URL = "jdbc:mysql://localhost:3306/loja_toptop";
private static final String USER = "root";
private static final String PASS = ""; 

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}