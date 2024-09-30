package py.una.bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bd {
    private static final String URL = "jdbc:postgresql://localhost:5432/tarea_sd"; //Se debe configurar la base de datos
    private static final String USER = "postgres"; //se debe configurar el usuario de la base de datos
    private static final String PASSWORD = "postgres"; // cambiar por contraseña
    private static final Logger LOGGER = Logger.getLogger(Bd.class.getName());

    //
    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Connection failed", e);
            return null; 
        }
    }
}

//CREAR BASE DE DATOS LLAMADA "tarea_sd"
/*
CREATE TABLE IF NOT EXISTS public.usuarios
(
    id serial NOT NULL,
    nombre character varying(30) COLLATE pg_catalog."default" NOT NULL,
    contrasena character varying(100) COLLATE pg_catalog."default" NOT NULL,
    conectado boolean DEFAULT false,
    CONSTRAINT usuarios_pkey PRIMARY KEY (id),
    CONSTRAINT usuarios_nombre_key UNIQUE (nombre)
);
 */