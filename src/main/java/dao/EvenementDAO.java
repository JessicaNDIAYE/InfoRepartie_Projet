package dao;

import model.Evenement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EvenementDAO {
    private Connection connection;

    // Constructeur qui utilise DAOFactory pour obtenir la connexion
    public EvenementDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Méthode utilitaire pour obtenir une nouvelle connexion si nécessaire
     */
    private Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DAOFactory.getInstance().getConnection();
        }
        return connection;
    }

    /**
     * Récupère tous les événements de la base de données
     * @return Liste de tous les événements
     */
    public List<Evenement> getAll() {
        List<Evenement> list = new ArrayList<>();
        String sql = "SELECT * FROM Evenement ORDER BY horodatage DESC";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Evenement e = mapResultSetToEvenement(rs);
                list.add(e);
            }
        } catch (SQLException e) {
            System.err.println("EvenementDAO: Erreur lors de la récupération de tous les événements.");
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Alias pour getAll() - méthode standard des DAO
     * @return Liste de tous les événements
     */
    public List<Evenement> findAll() {
        return getAll();
    }

    /**
     * Trouve un événement par son ID
     * @param id L'identifiant de l'événement
     * @return L'événement trouvé ou null si non trouvé
     */
    public Evenement findById(int id) {
        String sql = "SELECT * FROM Evenement WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEvenement(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("EvenementDAO: Erreur lors de la recherche de l'événement avec ID: " + id);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Trouve les événements par nom (recherche partielle)
     * @param nom Le nom à rechercher
     * @return Liste des événements trouvés
     */
    public List<Evenement> findByNomLike(String nom) {
        List<Evenement> list = new ArrayList<>();
        String sql = "SELECT * FROM Evenement WHERE nom ILIKE ? ORDER BY horodatage DESC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nom + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToEvenement(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("EvenementDAO: Erreur lors de la recherche des événements avec nom contenant: " + nom);
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Trouve les événements par lieu
     * @param lieu Le lieu à rechercher
     * @return Liste des événements trouvés
     */
    public List<Evenement> findByLieu(String lieu) {
        List<Evenement> list = new ArrayList<>();
        String sql = "SELECT * FROM Evenement WHERE lieu ILIKE ? ORDER BY horodatage DESC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + lieu + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToEvenement(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("EvenementDAO: Erreur lors de la recherche des événements par lieu: " + lieu);
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Trouve les événements créés par un utilisateur spécifique
     * @param idCreateur L'ID du créateur
     * @return Liste des événements créés par cet utilisateur
     */
    public List<Evenement> findByCreateur(int idCreateur) {
        List<Evenement> list = new ArrayList<>();
        String sql = "SELECT * FROM Evenement WHERE id_createur = ? ORDER BY horodatage DESC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCreateur);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToEvenement(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("EvenementDAO: Erreur lors de la recherche des événements par créateur: " + idCreateur);
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Trouve les événements dans une plage de dates
     * @param dateDebut Date de début (format: YYYY-MM-DD HH:MM:SS)
     * @param dateFin Date de fin (format: YYYY-MM-DD HH:MM:SS)
     * @return Liste des événements dans cette plage
     */
    public List<Evenement> findByDateRange(Timestamp dateDebut, Timestamp dateFin) {
        List<Evenement> list = new ArrayList<>();
        String sql = "SELECT * FROM Evenement WHERE horodatage BETWEEN ? AND ? ORDER BY horodatage ASC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, dateDebut);
            stmt.setTimestamp(2, dateFin);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToEvenement(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("EvenementDAO: Erreur lors de la recherche des événements par plage de dates.");
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Insère un nouvel événement dans la base de données
     * @param evenement L'événement à insérer
     * @return true si l'insertion a réussi, false sinon
     */
    public boolean insert(Evenement evenement) {
        if (evenement == null || evenement.getNom() == null || evenement.getNom().trim().isEmpty()) {
            System.err.println("EvenementDAO: Impossible d'insérer un événement null ou avec un nom vide.");
            return false;
        }

        String sql = "INSERT INTO Evenement (nom, horodatage, duree, lieu, description, id_createur) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, evenement.getNom().trim());
            stmt.setTimestamp(2, new Timestamp(evenement.getHorodatage().getTime()));
            stmt.setTime(3, evenement.getDuree());
            stmt.setString(4, evenement.getLieu());
            stmt.setString(5, evenement.getDescription());
            stmt.setInt(6, evenement.getIdCreateur());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Récupérer l'ID généré
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        evenement.setId(generatedKeys.getInt(1));
                    }
                }
                System.out.println("EvenementDAO: Événement inséré avec succès - ID: " + evenement.getId());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("EvenementDAO: Erreur lors de l'insertion de l'événement: " + evenement.getNom());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Met à jour un événement existant
     * @param evenement L'événement à mettre à jour
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean update(Evenement evenement) {
        if (evenement == null || evenement.getId() <= 0 ||
                evenement.getNom() == null || evenement.getNom().trim().isEmpty()) {
            System.err.println("EvenementDAO: Impossible de mettre à jour un événement invalide.");
            return false;
        }

        String sql = "UPDATE Evenement SET nom = ?, horodatage = ?, duree = ?, lieu = ?, description = ?, id_createur = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, evenement.getNom().trim());
            stmt.setTimestamp(2, new Timestamp(evenement.getHorodatage().getTime()));
            stmt.setTime(3, evenement.getDuree());
            stmt.setString(4, evenement.getLieu());
            stmt.setString(5, evenement.getDescription());
            stmt.setInt(6, evenement.getIdCreateur());
            stmt.setInt(7, evenement.getId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("EvenementDAO: Événement mis à jour avec succès - ID: " + evenement.getId());
                return true;
            } else {
                System.out.println("EvenementDAO: Aucun événement trouvé avec l'ID: " + evenement.getId());
            }
        } catch (SQLException e) {
            System.err.println("EvenementDAO: Erreur lors de la mise à jour de l'événement - ID: " + evenement.getId());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Supprime un événement par son ID
     * @param id L'identifiant de l'événement à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean delete(int id) {
        if (id <= 0) {
            System.err.println("EvenementDAO: ID invalide pour la suppression: " + id);
            return false;
        }

        String sql = "DELETE FROM Evenement WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("EvenementDAO: Événement supprimé avec succès - ID: " + id);
                return true;
            } else {
                System.out.println("EvenementDAO: Aucun événement trouvé avec l'ID: " + id);
            }
        } catch (SQLException e) {
            System.err.println("EvenementDAO: Erreur lors de la suppression de l'événement - ID: " + id);
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Supprime un événement
     * @param evenement L'événement à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean delete(Evenement evenement) {
        if (evenement == null || evenement.getId() <= 0) {
            System.err.println("EvenementDAO: Impossible de supprimer un événement null ou avec un ID invalide.");
            return false;
        }
        return delete(evenement.getId());
    }

    /**
     * Vérifie si un événement existe par son ID
     * @param id L'identifiant de l'événement
     * @return true si l'événement existe, false sinon
     */
    public boolean exists(int id) {
        String sql = "SELECT COUNT(*) FROM Evenement WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("EvenementDAO: Erreur lors de la vérification d'existence - ID: " + id);
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Compte le nombre total d'événements
     * @return Le nombre d'événements
     */
    public int count() {
        String sql = "SELECT COUNT(*) FROM Evenement";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("EvenementDAO: Erreur lors du comptage des événements.");
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Compte le nombre d'événements créés par un utilisateur
     * @param idCreateur L'ID du créateur
     * @return Le nombre d'événements créés par cet utilisateur
     */
    public int countByCreateur(int idCreateur) {
        String sql = "SELECT COUNT(*) FROM Evenement WHERE id_createur = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCreateur);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("EvenementDAO: Erreur lors du comptage des événements par créateur: " + idCreateur);
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Récupère les événements avec pagination
     * @param offset Le décalage (nombre d'enregistrements à ignorer)
     * @param limit Le nombre maximum d'enregistrements à retourner
     * @return Liste paginée des événements
     */
    public List<Evenement> findWithPagination(int offset, int limit) {
        List<Evenement> list = new ArrayList<>();
        String sql = "SELECT * FROM Evenement ORDER BY horodatage DESC LIMIT ? OFFSET ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);
            stmt.setInt(2, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToEvenement(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("EvenementDAO: Erreur lors de la récupération paginée des événements.");
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Récupère les événements à venir (horodatage futur)
     * @return Liste des événements à venir
     */
    public List<Evenement> findUpcomingEvents() {
        List<Evenement> list = new ArrayList<>();
        String sql = "SELECT * FROM Evenement WHERE horodatage > NOW() ORDER BY horodatage ASC";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapResultSetToEvenement(rs));
            }
        } catch (SQLException e) {
            System.err.println("EvenementDAO: Erreur lors de la récupération des événements à venir.");
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Récupère les événements passés (horodatage passé)
     * @return Liste des événements passés
     */
    public List<Evenement> findPastEvents() {
        List<Evenement> list = new ArrayList<>();
        String sql = "SELECT * FROM Evenement WHERE horodatage < NOW() ORDER BY horodatage DESC";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapResultSetToEvenement(rs));
            }
        } catch (SQLException e) {
            System.err.println("EvenementDAO: Erreur lors de la récupération des événements passés.");
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Méthode utilitaire pour mapper un ResultSet vers un objet Evenement
     * @param rs Le ResultSet à mapper
     * @return L'objet Evenement créé
     * @throws SQLException Si une erreur SQL se produit
     */
    private Evenement mapResultSetToEvenement(ResultSet rs) throws SQLException {
        Evenement evenement = new Evenement();
        evenement.setId(rs.getInt("id"));
        evenement.setNom(rs.getString("nom"));
        evenement.setHorodatage(rs.getTimestamp("horodatage"));
        evenement.setDuree(rs.getTime("duree"));
        evenement.setLieu(rs.getString("lieu"));
        evenement.setDescription(rs.getString("description"));
        evenement.setIdCreateur(rs.getInt("id_createur"));
        return evenement;
    }
}