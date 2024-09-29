package py.una.client;

import java.io.*;
import java.net.*;


public class TCPClient {
	
    public static void main(String[] args) throws IOException {
    	
    	Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;
    	
        try {
        	socket = new Socket("localhost", 4444);	//se establece la conexion con el servidor en el puerto 4444
        	//enviamos nosotros, para enviar
            out = new PrintWriter(socket.getOutputStream(), true);	//con PrintWriter enviamos datos al servidor
            
            //viene del servidor, para recibir
            in = new BufferedReader(new InputStreamReader(socket.getInputStream())); //con BufferedReader recibimos datos del servidor
        }catch (UnknownHostException e) {
            System.err.println("Host desconocido");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Error de I/O en la conexion al host");
            System.exit(1);
        }
        
        
        BufferedReader	stdIn = new BufferedReader(new InputStreamReader(System.in));
        String fromServer; 	//mensajes desde el servidor
        String fromUser;	//mensajes desde el cliente
        String username = ""; // se inicializa el username del cliente
        String password = "";	//se inicializa password del cliente
        

     // Bienvenida del servidor
        System.out.println(in.readLine());    
        
     // Menú al iniciar el programa
        boolean flag = true;
        while (flag) {
        	// Menú de opciones
            if (username.isEmpty()) {
            	
                System.out.println("\n<<<<<MENU DE OPCIONES>>>>>\n  -LOGIN \n  -SIGNIN \n  -LISTA_USUARIOS\n -TERMINAR PROCESO\nIngrese un comando: ");
            } else {
                System.out.println("\n<<<<<MENU DE OPCIONES>>>>>\n  -LOGOUT \n  -LISTA_USUARIOS\n  Ingrese un comando: ");
            }
            fromUser = stdIn.readLine();
            
            if (fromUser.equals("LOGIN")) {
                System.out.println("Ingrese su nombre de usuario: ");
                username = stdIn.readLine(); // Se asigna nombre a username
                System.out.println("Ingrese su contraseña: ");
                password = stdIn.readLine();
                out.println("LOGIN:" + username + ":" + password); // Enviar el login

                fromServer = in.readLine(); // Esperar respuesta del servidor
                
                if (fromServer.contains("conectado.")) {
                    System.out.println("Servidor: Sesion iniciada " + fromServer);
                    continue; // El bucle termina si el cleinte ingresa exitosamente
                } else {
                    username = ""; // Reiniciar el nombre de usuario al fallar el inicio de sesión
                    password = "";
                    System.out.println("Servidor: " + fromServer);
                }

            } else if (fromUser.equals("SIGNIN")) {
                System.out.println("Ingrese un nuevo nombre de usuario: ");
                username = stdIn.readLine(); 
                System.out.println("Ingrese una nueva contraseña: ");
                password = stdIn.readLine();
                out.println("SIGNIN:" + username + ":" + password); // Enviar el registro

                fromServer = in.readLine(); // Esperar respuesta del servidor
                System.out.println("Servidor: " + fromServer);
                username = "";
                password = "";
            }  else if (fromUser.equals("LOGOUT")) {
                out.println("LOGOUT");
                username = ""; // Reiniciar el nombre de usuario al cerrar sesión, cuando el usuario no esta conectado
            
            }
            
            else if (fromUser.equals("LISTA_USUARIOS")) {
                out.println("LISTA_USUARIOS");
                fromServer = in.readLine(); // Espera la respuesta del servidor
                System.out.println("Servidor: " + fromServer);
            } else if (fromUser.equals("TERMINAR PROCESO")) {
            	out.println("TERMINAR PROCESO");
            	flag = false;
            	
            	
            } else {
            	out.println("cualquiera");
                System.out.println("\n---Error. Por favor, ingrese una opcion correcta---");
                //fromServer = in.readLine(); // Espera la respuesta del servidor
                //System.out.println("Servidor: " + fromServer);
            }
         
            fromServer = in.readLine();			// Espera y lee respuesta del servidor
            //System.out.println("Servidor: " + fromServer);
            
            // Repuesta del servidor
            if (fromServer.contains("conectado.") && !fromServer.contains("desconectado.")) {
                // Conexion de usuario
                continue; // El bucle continuará y mostrará el menú correcto
            } else if (fromServer.contains("registrado")) {
                // Registro de usuario
            	out.println("LOGIN:");
                //continue; // El bucle continuará para iniciar sesión
            } else if (fromServer.contains("desconectado.")) {
                // Deconexion de usuario
                username = ""; // Reiniciar el nombre de usuario
            }
            else {
            	continue;
            }
        }
        
        
        out.close();
        in.close();
        stdIn.close();
        socket.close();
    }
    
}