package py.una.bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Bd {
    private static final String URL = "jdbc:postgresql://localhost:5432/tarea_sd"; // Cambia esto por tu base de datos
    private static final String USER = "postgres"; // Cambia esto por tu usuario
    private static final String PASSWORD = "postgres"; // Cambia esto por tu contraseña

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}