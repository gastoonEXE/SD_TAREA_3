package py.una.bd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


//En PersonaDAO.java manejamos la comunicacion con la base de datos relacionadas a operaciones para registrar, iniciar y listar usuarios conectados
public class PersonaDAO {
	
	//al hacer SIGNIN se inserta un nuevo usuario en la base de datos
	public boolean signinUsuario(String nombre, String contrasena) {
        String SQL = "INSERT INTO usuarios (nombre, contrasena) VALUES (?, ?)";

        try (Connection conn = Bd.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, contrasena); 
            pstmt.executeUpdate();
            //imprimir el siguiente mensaje cuando sea la primera vez que ingresa
            System.out.println("Usuario " + nombre + " agregado a la base de datos.");
            return true;
        } catch (SQLException ex) {
            System.out.println("Error en el registro: " + ex.getMessage());
            return false;
        }
    }
	
	//verificacion de existencia de usuario en base de datos
	public boolean loginUsuario(String nombre, String contrasena) {
        String SQL = "SELECT * FROM usuarios WHERE nombre = ? AND contrasena = ?";
        
        try (Connection conn = Bd.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, contrasena); 
            ResultSet rs = pstmt.executeQuery();
            
            return rs.next(); // Devuelve true si se encuentra el usuario
            
        } catch (SQLException ex) {
            System.out.println("Error en la autenticación: " + ex.getMessage());
            return false;
        }
    }
	public void conectarUsuario(String nombre, boolean conectado) {
        String SQL = "UPDATE usuarios SET conectado = ? WHERE nombre = ?";
        //System.out.println("Actualiza para: " + nombre + " a " + conectado);  
        
        try (Connection conn = Bd.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setBoolean(1, conectado);
            pstmt.setString(2, nombre);
            int rowsAffected = pstmt.executeUpdate();
            
        } catch (SQLException ex) {
            System.out.println("Error al actualizar estado de conexión: " + ex.getMessage());
        }
    }

    public List<String> listarUsuariosConectados() {
        List<String> usuariosConectados = new ArrayList<>();
        String SQL = "SELECT nombre FROM usuarios WHERE conectado = true";
        try (Connection conn = Bd.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                usuariosConectados.add(rs.getString("nombre"));
            }
        } catch (SQLException ex) {
            System.out.println("Error al obtener usuarios conectados: " + ex.getMessage());
        }
        return usuariosConectados;
    }
    
}
