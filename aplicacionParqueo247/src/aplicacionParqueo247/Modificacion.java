package aplicacionParqueo247;

import java.sql.*;

public class Modificacion {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/parqueo_24_7_evidencia";
        String usuario = "root";
        String password = "My$qlR00tP@ss2024!";
        
        System.out.println("🔌 Conectando a la base de datos...");
        System.out.println("Base de datos: parqueo_24_7_evidencia");
        
        try (Connection conexion = DriverManager.getConnection(url, usuario, password)) {
            System.out.println("¡CONEXIÓN EXITOSA!");
            
            // Mostrar usuarios existentes antes de las operaciones
            System.out.println("\n👥 Usuarios existentes antes de las operaciones:");
            mostrarUsuarios(conexion);
            
            // 1. Actualizar contraseñas de los registros 10 y 12
            System.out.println("\n🔄 ACTUALIZANDO CONTRASEÑAS...");
            
            // Actualizar contraseña del registro 10 (felipe_ruiz)
            boolean actualizado55 = actualizarContrasena(conexion, 55, "nuevaPassFelipe2024");
            if (actualizado55) {
                System.out.println("✅ Contraseña actualizada para usuario ID 10 (felipe_ruiz)");
            } else {
                System.out.println("❌ Error al actualizar contraseña para usuario ID 10");
            }
            
            // Actualizar contraseña del registro 12 (andres_vargas)
            boolean actualizado57 = actualizarContrasena(conexion, 57, "andresSecure2024!");
            if (actualizado57) {
                System.out.println("Contraseña actualizada para usuario ID 57 (andres_vargas)");
            } else {
                System.out.println("Error al actualizar contraseña para usuario ID 12");
            }
            
            // 2. Eliminar el registro 14 (pedro_ramos)
            System.out.println("\n ELIMINANDO REGISTRO...");
            boolean eliminado59 = eliminarUsuario(conexion, 59);
            if (eliminado59) {
                System.out.println("Usuario ID 14 (pedro_ramos) eliminado correctamente");
            } else {
                System.out.println("Error al eliminar usuario ID 59");
            }
            
            // Mostrar usuarios después de las operaciones
            System.out.println("\n👥 Usuarios después de las operaciones:");
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
                String contrasena = rs.getString("contrasena");
                String contrasenaMostrar = (contrasena != null && contrasena.length() > 5) 
                    ? contrasena.substring(0, 5) + "..." 
                    : contrasena;
                
                System.out.printf("%-5d %-20s %-15s %-20s %-12s %-10d%n", 
                    rs.getInt("idusuario"),
                    rs.getString("nombre_usuario"),
                    contrasenaMostrar,
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
    
    // Método para actualizar contraseña de un usuario
    public static boolean actualizarContrasena(Connection conexion, int idUsuario, String nuevaContrasena) {
        String sql = "UPDATE usuario SET contrasena = ? WHERE idusuario = ?";
        
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setString(1, nuevaContrasena);
            pstmt.setInt(2, idUsuario);
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException ex) {
            System.err.println("Error al actualizar contraseña para usuario ID " + idUsuario + ": " + ex.getMessage());
            return false;
        }
    }
    
    // Método para eliminar un usuario por ID
    public static boolean eliminarUsuario(Connection conexion, int idUsuario) {
        String sql = "DELETE FROM usuario WHERE idusuario = ?";
        
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException ex) {
            System.err.println("Error al eliminar usuario ID " + idUsuario + ": " + ex.getMessage());
            
            // Si hay error de clave foránea, mostrar mensaje específico
            if (ex.getSQLState().equals("23000")) {
                System.err.println("No se puede eliminar el usuario porque tiene registros relacionados en otras tablas.");
            }
            return false;
        }
    }
    
    // Método adicional para obtener nombre de usuario por ID
    public static String obtenerNombreUsuario(Connection conexion, int idUsuario) {
        String sql = "SELECT nombre_usuario FROM usuario WHERE idusuario = ?";
        
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("nombre_usuario");
            }
        } catch (SQLException ex) {
            System.err.println("Error al obtener nombre de usuario: " + ex.getMessage());
        }
        
        return "Desconocido";
    }
}
