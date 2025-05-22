package dao;

import java.sql.Connection;
import java.sql.SQLException;

import model.*;

public class DAOFactory {
    private static DAOFactory instance;
    private Connection connection;

    // Constructeur privé pour le pattern Singleton
    private DAOFactory() {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            System.err.println("DAOFactory: Erreur lors de l'initialisation de la connexion à la base de données.");
            e.printStackTrace();
            throw new RuntimeException("Impossible d'initialiser DAOFactory", e);
        }
    }

    /**
     * Méthode pour obtenir l'instance unique de DAOFactory (Singleton)
     * @return l'instance unique de DAOFactory
     */
    public static synchronized DAOFactory getInstance() {
        if (instance == null) {
            instance = new DAOFactory();
        }
        return instance;
    }

    /**
     * Méthode pour obtenir une nouvelle connexion si nécessaire
     * @return une nouvelle connexion à la base de données
     * @throws SQLException si une erreur de connexion se produit
     */
    public Connection getConnection() throws SQLException {
        // Vérifier si la connexion est fermée ou nulle
        if (connection == null || connection.isClosed()) {
            System.out.println("DAOFactory: Reconnexion à la base de données...");
            connection = DatabaseConnection.getConnection();
        }
        return connection;
    }

    /**
     * Ferme la connexion à la base de données
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("DAOFactory: Connexion fermée avec succès.");
            }
        } catch (SQLException e) {
            System.err.println("DAOFactory: Erreur lors de la fermeture de la connexion.");
            e.printStackTrace();
        }
    }

    public FanfaronDAO getFanfaronDAO() {
        try {
            return new FanfaronDAO(getConnection());
        } catch (SQLException e) {
            System.err.println("DAOFactory: Erreur lors de la création de FanfaronDAO.");
            e.printStackTrace();
            throw new RuntimeException("Impossible de créer FanfaronDAO", e);
        }
    }

    // Méthodes pour obtenir les différents DAO
    public PupitreDAO getPupitreDAO() {
        try {
            return new PupitreDAO(getConnection());
        } catch (SQLException e) {
            System.err.println("DAOFactory: Erreur lors de la création de PupitreDAO.");
            e.printStackTrace();
            throw new RuntimeException("Impossible de créer PupitreDAO", e);
        }
    }

    public GroupeDAO getGroupeDAO() {
        try {
            return new GroupeDAO(getConnection());
        } catch (SQLException e) {
            System.err.println("DAOFactory: Erreur lors de la création de GroupeDAO.");
            e.printStackTrace();
            throw new RuntimeException("Impossible de créer GroupeDAO", e);
        }
    }

    public TypeEvenementDAO getTypeEvenementDAO() {
        try {
            return new TypeEvenementDAO(getConnection());
        } catch (SQLException e) {
            System.err.println("DAOFactory: Erreur lors de la création de TypeEvenementDAO.");
            e.printStackTrace();
            throw new RuntimeException("Impossible de créer TypeEvenementDAO", e);
        }
    }

    public EvenementDAO getEvenementDAO() {
        try {
            return new EvenementDAO(getConnection());
        } catch (SQLException e) {
            System.err.println("DAOFactory: Erreur lors de la création de EvenementDAO.");
            e.printStackTrace();
            throw new RuntimeException("Impossible de créer EvenementDAO", e);
        }
    }

    public StatutParticipationDAO getStatutParticipationDAO() {
        try {
            return new StatutParticipationDAO(getConnection());
        } catch (SQLException e) {
            System.err.println("DAOFactory: Erreur lors de la création de StatutParticipationDAO.");
            e.printStackTrace();
            throw new RuntimeException("Impossible de créer StatutParticipationDAO", e);
        }
    }

    public FanfaronPupitreDAO getFanfaronPupitreDAO() {
        try {
            return new FanfaronPupitreDAO(getConnection());
        } catch (SQLException e) {
            System.err.println("DAOFactory: Erreur lors de la création de FanfaronPupitreDAO.");
            e.printStackTrace();
            throw new RuntimeException("Impossible de créer FanfaronPupitreDAO", e);
        }
    }

    public FanfaronGroupeDAO getFanfaronGroupeDAO() {
        try {
            return new FanfaronGroupeDAO(getConnection());
        } catch (SQLException e) {
            System.err.println("DAOFactory: Erreur lors de la création de FanfaronGroupeDAO.");
            e.printStackTrace();
            throw new RuntimeException("Impossible de créer FanfaronGroupeDAO", e);
        }
    }

    public FanfaronEvenementParticipationDAO getFanfaronEvenementParticipationDAO() {
        try {
            return new FanfaronEvenementParticipationDAO(getConnection());
        } catch (SQLException e) {
            System.err.println("DAOFactory: Erreur lors de la création de FanfaronEvenementParticipationDAO.");
            e.printStackTrace();
            throw new RuntimeException("Impossible de créer FanfaronEvenementParticipationDAO", e);
        }
    }

    /**
     * Méthode pour nettoyer les ressources lors de l'arrêt de l'application
     */
    public static void cleanup() {
        if (instance != null) {
            instance.closeConnection();
            instance = null;
        }
    }
}