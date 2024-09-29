package py.una.bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bd {
    private static final String URL = "jdbc:postgresql://localhost:5432/tarea_sd"; // Cambia esto por tu base de datos
    private static final String USER = "postgres"; // Cambia esto por tu usuario
    private static final String PASSWORD = "postgres"; // Cambia esto por tu contraseña
    private static final Logger LOGGER = Logger.getLogger(Bd.class.getName());

    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Connection failed", e);
            return null; // or throw a custom exception
        }
    }
}