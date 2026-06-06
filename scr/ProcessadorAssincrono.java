package scr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProcessadorAssincrono implements Runnable {
    
    private volatile boolean rodando = true;

    public void parar() {
        this.rodando = false;
    }

    @Override
    public void run() {
        System.out.println("\nThread iniciada!");
        
        while (rodando) {
            try {
                processarProximoPedido();
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println("Thread Interrompida");
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("Thread Desligada");
    }

    private void processarProximoPedido() {

        String sqlBuscar = "SELECT id_pedido FROM pedido WHERE status = 'FILA' LIMIT 1";
        
        String sqlAtualizarProcessando = "UPDATE pedido SET status = 'PROCESSANDO' WHERE id_pedido = ? AND status = 'FILA'";
        
        String sqlAtualizarFinalizado = "UPDATE pedido SET status = 'FINALIZADO' WHERE id_pedido = ? AND status = 'PROCESSANDO'";

        try (Connection conn = Conexao.conectar()) {
            int idPedido = -1;

            try (PreparedStatement stmtBusca = conn.prepareStatement(sqlBuscar);
                 ResultSet rs = stmtBusca.executeQuery()) {
                if (rs.next()) {
                    idPedido = rs.getInt("id_pedido");
                }
            }

            if (idPedido != -1) {
                try (PreparedStatement stmtProc = conn.prepareStatement(sqlAtualizarProcessando)) {
                    stmtProc.setInt(1, idPedido);
                    int linhasAfetadas = stmtProc.executeUpdate();
                    
                    if (linhasAfetadas > 0) {
                        System.out.println("\nPedido nº" + idPedido + " na cozinha! Estado: PROCESSANDO...");
                        
                        Thread.sleep(8000); 

                        try (PreparedStatement stmtFin = conn.prepareStatement(sqlAtualizarFinalizado)) {
                            stmtFin.setInt(1, idPedido);
                            stmtFin.executeUpdate();
                            System.out.println("\n Pedido concluido! Estado: FINALIZADO!");
                            System.out.print("Deseja continuar?Escolha uma opção: ");
                        }
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Thread Erro de banco de dados: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}