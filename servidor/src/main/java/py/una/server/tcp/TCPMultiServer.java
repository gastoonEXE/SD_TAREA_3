package py.una.server.tcp;

import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import py.una.bd.PersonaDAO;



public class TCPMultiServer {
	//variables compartidas
    boolean listening = true; 	//variable para controlar que el servidor escuche conexiones
    List<TCPServerHilo> hilosClientes; //almacenar los hilos de cada cliente
    List<String> usuarios;	//almacenar una lista de usuarios conectados
    PersonaDAO personaDAO;  //para comunicarse con la base de datos
    
    
    public void ejecutar() throws IOException {
        ServerSocket serverSocket = null;   //se inicializa el socket del servidor en null

        try {
            serverSocket = new ServerSocket(4444);  //se crea un socket para escuchar peticiones en el puerto 4444
        } catch (IOException e) {
            System.err.println("No se puede abrir el puerto: 4444.");
            System.exit(1);
        }
        //El puerto se muestra en el servidor
        System.out.println("Puerto abierto: 4444.");

        //mientras este escuchando, los clientes se conectan y crean nuevos hilos para iniciar
        while (listening) {
            TCPServerHilo hilo = new TCPServerHilo(serverSocket.accept(), this);
            hilosClientes.add(hilo);    //se agreaga el hilo a la lista de hilos creada
            hilo.start();	//con start corremos el run que esta en TCPServerHilo()
        }

        //Se cierra socket de servidor al salir del bucle
        serverSocket.close();
    }
    
    //Al iniciar el servidor, no hay ningun usuario conectado
    //Se instancia TCPMultiServer para correr programa
    public static void main(String[] args) throws IOException {
        PersonaDAO.desconectarUsuarios(); //se desconectan todos los usuarios al iniciar el servidor
    	TCPMultiServer tms = new TCPMultiServer();  //Se inicializa servidor
        tms.hilosClientes = new ArrayList<>();	//se inicializa la lista de hilosClientes
        tms.usuarios = new ArrayList<>();  //Se inicializa lista de usuarios conectados 
        tms.personaDAO = new PersonaDAO();	//se inicializa objeto personaDAO para manejar comunicacion con base de datos
        tms.ejecutar();	//se utiliza este metodo para escuchar las solicitudes de conexion de clientes
    }
}



