//package aplicacionParqueo247;

import java.sql.*;
import java.util.UUID;

public class INSERT {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/parqueo_24_7_evidencia";
        String usuario = "root";
        String password = "My$qlR00tP@ss2024!";
        
        System.out.println("🔌 Conectando a la base de datos...");
        System.out.println("Base de datos: parqueo_24_7_evidencia");
        
        try (Connection conexion = DriverManager.getConnection(url, usuario, password)) {
            System.out.println("¡CONEXIÓN EXITOSA!");
            
            // Mostrar usuarios existentes antes de la inserción
            System.out.println("\n👥 Usuarios existentes antes de la inserción:");
            mostrarUsuarios(conexion);
            
            // Insertar los nuevos usuarios
            System.out.println("\n➕ INSERTANDO NUEVOS USUARIOS...");
            
            // Datos de los usuarios a insertar
            String[][] usuarios = {
                {"b8a1c7d2-a0e4-4f5b-8c2d-1d6e8b4c3e9f", "alejandra_mora", "passAlejandra", "16"},
                {"c4d9e8f1-b5c6-4a7d-9b0e-2e8f1c7d9a0b", "ricardo_vidal", "ricardo.v", "17"},
                {"d5e1f2g3-c6d7-4b8e-8c9f-3f2g1h0i9j8k", "paola_osorio", "paolaOsorio", "18"}
            };
            
            int usuariosInsertados = insertarMultiplesUsuarios(conexion, usuarios);
            
            System.out.println("✅ Se insertaron " + usuariosInsertados + " usuarios correctamente");
            
            // Mostrar usuarios después de la inserción
            System.out.println("\n👥 Usuarios después de la inserción:");
            mostrarUsuarios(conexion);
            
        } catch (SQLException ex) {
            System.err.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    // Método para mostrar todos los usuarios
    public static void mostrarUsuarios(Connection conexion) {
        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM usuario ORDER BY idusuario")) {
            
            System.out.println("==================================================================================");
            System.out.printf("%-5s %-20s %-15s %-20s %-12s %-10s%n", 
                "ID", "Usuario", "Contraseña", "Fecha Creación", "Creado Por", "Persona ID");
            System.out.println("==================================================================================");
            
            int contador = 0;
            while(rs.next()) {
                contador++;
                System.out.printf("%-5d %-20s %-15s %-20s %-12s %-10d%n", 
                    rs.getInt("idusuario"),
                    rs.getString("nombre_usuario"),
                    // Mostrar solo los primeros 5 caracteres de la contraseña por seguridad
                    rs.getString("contrasena").substring(0, Math.min(5, rs.getString("contrasena").length())) + "...",
                    rs.getTimestamp("fecha_creacion").toString().substring(0, 16),
                    rs.getString("creado_por"),
                    rs.getInt("persona_id"));
            }
            
            System.out.println("==================================================================================");
            System.out.println("Total de usuarios: " + contador);
            
        } catch (SQLException ex) {
            System.err.println("Error al mostrar usuarios: " + ex.getMessage());
        }
    }
    
    // Método para insertar múltiples usuarios (con la estructura correcta)
    public static int insertarMultiplesUsuarios(Connection conexion, String[][] usuarios) {
        // SQL corregido con la estructura exacta de la tabla
        String sql = "INSERT INTO usuario (uuid, nombre_usuario, contrasena, fecha_creacion, creado_por, persona_id) " +
                     "VALUES (?, ?, ?, NOW(), ?, ?)";
        
        int contador = 0;
        
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            // Desactivar auto-commit para realizar una transacción
            conexion.setAutoCommit(false);
            
            for (String[] usuario : usuarios) {
                try {
                    pstmt.setString(1, usuario[0]); // uuid
                    pstmt.setString(2, usuario[1]); // nombre_usuario
                    pstmt.setString(3, usuario[2]); // contrasena
                    pstmt.setString(4, "system");   // creado_por
                    pstmt.setInt(5, Integer.parseInt(usuario[3])); // persona_id
                    
                    int filasAfectadas = pstmt.executeUpdate();
                    if (filasAfectadas > 0) {
                        contador++;
                        System.out.println("✅ Insertado: " + usuario[1]);
                    }
                } catch (SQLException e) {
                    System.err.println("❌ Error al insertar usuario " + usuario[1] + ": " + e.getMessage());
                    // Mostrar el error específico de SQL
                    System.err.println("SQL State: " + e.getSQLState());
                    System.err.println("Error Code: " + e.getErrorCode());
                } catch (NumberFormatException e) {
                    System.err.println("❌ Error en formato de número para persona_id: " + usuario[3]);
                }
            }
            
            // Confirmar la transacción
            conexion.commit();
            // Reactivar auto-commit
            conexion.setAutoCommit(true);
            
        } catch (SQLException ex) {
            System.err.println("Error en la transacción: " + ex.getMessage());
            try {
                // Revertir la transacción en caso de error
                conexion.rollback();
                conexion.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error al revertir transacción: " + e.getMessage());
            }
        }
        
        return contador;
    }
    
    // Método adicional para verificar si los persona_id existen
    public static boolean verificarPersonaId(Connection conexion, int personaId) {
        String sql = "SELECT COUNT(*) FROM persona WHERE idpersona = ?";
        
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, personaId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar persona_id: " + e.getMessage());
        }
        
        return false;
    }
}