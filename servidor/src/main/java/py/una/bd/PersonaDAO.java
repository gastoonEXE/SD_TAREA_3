package py.una.bd;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


//En PersonaDAO.java manejamos la comunicacion con la base de datos relacionadas a operaciones para registrar, iniciar y listar usuarios conectados
public class PersonaDAO {
    private static Connection conn = Bd.connect();
	
	//al hacer SIGNIN se inserta un nuevo usuario en la base de datos
	public boolean signinUsuario(String nombre, String contrasena) {
        String SQL = "INSERT INTO usuarios (nombre, contrasena) VALUES (?, ?)";

        try {
            PreparedStatement pstmt = conn.prepareStatement(SQL);
            pstmt.setString(1, nombre);
            pstmt.setString(2, encryptPassword(contrasena)); 
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
        
        try {
            PreparedStatement pstmt = conn.prepareStatement(SQL);
            pstmt.setString(1, nombre);
            pstmt.setString(2, encryptPassword(contrasena)); 
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
        
        try {
            PreparedStatement pstmt = conn.prepareStatement(SQL);
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
        try {
            PreparedStatement pstmt = conn.prepareStatement(SQL);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                usuariosConectados.add(rs.getString("nombre"));
            }
        } catch (SQLException ex) {
            System.out.println("Error al obtener usuarios conectados: " + ex.getMessage());
        }
        return usuariosConectados;
    }

    public static void desconectarUsuarios() {
        String SQL = "UPDATE usuarios SET conectado = false";
        try {
            PreparedStatement pstmt = conn.prepareStatement(SQL);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error al desconectar usuarios: " + ex.getMessage());
        }
    }
    
    // Método para encriptar la contraseña
    private String encryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256"); // Usa SHA-256 como ejemplo
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();

            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b)); // Convierte a formato hexadecimal
            }

            return sb.toString(); // Devuelve la contraseña encriptada
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al encriptar la contraseña", e);
        }
    }
}
