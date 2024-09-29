package py.una.server.tcp;

import java.net.*;
import java.util.List;
import java.io.*;
import py.una.bd.PersonaDAO;

//En TCPServerHilo.java se manejan las solicitudes de login, logout, registro de usuarios
public class TCPServerHilo extends Thread {	//Extiende Thread para el manejo de multiples clientes simultaneamente
	
	private Socket socket = null;		//representa el socket del cliente
	
    TCPMultiServer servidor;			//como este es un hilo, se hace referencia al servidor de tipo TCPMultiServer
    PersonaDAO personaDAO;				//personaDAO maneja la base de datos
    private String username; 			// Para almacenar el nombre de usuario

    public TCPServerHilo(Socket socket, TCPMultiServer servidor) {	//constructor que inicializa el socket del cliente y hace referencia al servidor TCOMultiServer
    	super("TCPServerHilo");
        this.socket = socket;
        this.servidor = servidor;
        this.personaDAO = servidor.personaDAO;
        this.username = null; 										// username se inicializa como null para evitar que servidor no lea usuario en cuestion
    }

    public void run() { 											//el metodo run se encarga del paso de mensajes con el cliente
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);  					//Clase PrintWriter para enviar
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); //Clase BufferedReader paar recibir
            out.println("Bienvenido!"); 															// Se envia Mesnaje de bienvenida al cliente con out
            String inputLine, outputLine;
            
            
            //El login se produce estableciendo el valor de conectado en true, por defecto esta en false
            //Para el logout se establece el valor de conectado en false
            
            while ((inputLine = in.readLine()) != null) {   	//vemos si hay usuarios conectados
                if (inputLine.startsWith("LOGIN:")) {			//COMANDO LOGIN
                    String[] partes = inputLine.split(":");
                    //System.out.println("inputLine: "+inputLine);
                    username = partes[1];
                    //System.out.println("username: "+username);
                    String password = partes[2];
                    //System.out.println("password: "+password);

                    if (personaDAO.loginUsuario(username, password)) {
                    	System.out.println("\nUsuario " + username + " conectado");
                        out.println("Usuario " + username + " conectado.");
                        servidor.usuarios.add(username);
                        personaDAO.conectarUsuario(username, true); 	// Marca al usuario como conectado
                    } else {
                        out.println("Error al iniciar sesion. No existe el usuario.");
                        out.println("error");
                    }
                } else if (inputLine.equals("LOGOUT")) {				//COMANDO LOGOUT
                   
                	if (username != null) { 
                		System.out.println("Se cierra la sesion del usuario: " + username);
                        personaDAO.conectarUsuario(username, false); 	// Marca al usuario como desconectado
                        out.println("Usuario desconectado");
                        username = null; 								// Limpia el nombre de usuario
                	}else {
                        out.println("Usuarios conectados: 0");
                    }
                	
                    //break;
                } else if (inputLine.startsWith("SIGNIN:")) {		//COMANDO SIGNIN
                    String[] partes = inputLine.split(":");
                    String newUsername = partes[1];
                    String newPassword = partes[2];

                    if (personaDAO.signinUsuario(newUsername, newPassword)) {
                        out.println("Se ha registrado el usuario " + newUsername + " .Por favor, inicie sesion");
                    } else {
                        out.println("Error de registro de usuario. El nombre esta en uso");
                    }
                    out.println("error");
                } else if (inputLine.equals("LISTA_USUARIOS")) {
       
                
                	List<String> usuariosConectados = personaDAO.listarUsuariosConectados();
                	if (usuariosConectados.isEmpty()) {
                        out.println("Usuarios conectados: 0");
                        out.println("error");
                    } else {
                        out.println("Usuarios conectados: " + String.join(", ", usuariosConectados));
                    }
                	out.println("error");
                } else if (inputLine.equals("TERMINAR PROCESO")) {
       
                	out.println("Proceso terminado");
                	break;
                    
                }
                else {
                    
                    out.println("error");
                }
            }
            out.close();
            in.close();
        } catch (IOException e) {
            personaDAO.conectarUsuario(username, false); 	// Marca al usuario como desconectado
            servidor.usuarios.remove(username);
            System.out.println("Usuario desconectado: " + username);
            
            // e.printStackTrace();
        } finally {
            try {
                socket.close();
                System.out.println("Finalizando Hilo");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}