package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Informations de connexion à votre base de données PostgreSQL
    private static final String DB_URL = "jdbc:postgresql://localhost:5433/fanfaron";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "kawthar";

    // Bloc statique pour charger le pilote JDBC une seule fois
    static {
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("DatabaseConnection: Pilote PostgreSQL JDBC chargé avec succès.");
        } catch (ClassNotFoundException e) {
            System.err.println("DatabaseConnection: Erreur: Le pilote PostgreSQL JDBC n'a pas été trouvé dans le classpath.");
            System.err.println("Assurez-vous que le fichier JAR du pilote est dans WEB-INF/lib ou le classpath de Tomcat.");
            e.printStackTrace();
            throw new RuntimeException("Impossible de charger le pilote JDBC PostgreSQL.", e);
        }
    }

    /**
     * Fournit une nouvelle connexion à la base de données.
     * @return une instance de Connection
     * @throws SQLException si une erreur de connexion à la base de données se produit
     */
    public static Connection getConnection() throws SQLException {
        System.out.println("DatabaseConnection: Tentative de connexion à la base de données: " + DB_URL);
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("DatabaseConnection: Connexion à la base de données établie avec succès.");
            return connection;
        } catch (SQLException e) {
            System.err.println("DatabaseConnection: Erreur lors de l'établissement de la connexion à la base de données.");
            System.err.println("URL: " + DB_URL + ", User: " + DB_USER);
            e.printStackTrace();
            throw e; // Relaunce l'exception pour qu'elle soit gérée plus haut
        }
    }
}