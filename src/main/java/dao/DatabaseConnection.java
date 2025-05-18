package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Configuration de la base de données
    private static final String URL = "jdbc:postgresql://localhost:5432/fanfaron";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Jesthe1520"; // À modifier selon votre configuration

    static {
        try {
            // Charger le pilote JDBC
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Impossible de charger le pilote POSTGRES JDBC");
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}