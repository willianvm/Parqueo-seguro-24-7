package aplicacionParqueo247;

import java.sql.*;

public class aplicacionParqueo247 {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/parqueo_24_7_evidencia";
        String usuario = "root";
        String password = "My$qlR00tP@ss2024!";
        
        System.out.println("🔌Conectando a la base de datos...");
        System.out.println("Base de datos: parqueo_24_7_evidencia");
        
        try (Connection conexion = DriverManager.getConnection(url, usuario, password);
             Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM usuario")) {
            
            System.out.println("¡CONEXIÓN EXITOSA!");
            System.out.println("👥 Listado de usuarios:");
            System.out.println("========================");
            
            int contador = 0;
            while(rs.next()) {
                contador++;
                System.out.println("ID: " + rs.getInt("idusuario") + 
                                 " | Usuario: " + rs.getString("nombre_usuario") +
                                 " | Persona ID: " + rs.getInt("persona_id"));
            }
            
            System.out.println("========================");
            System.out.println("Total de usuarios: " + contador);
            

            
        } catch (SQLException ex) {
            System.err.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}