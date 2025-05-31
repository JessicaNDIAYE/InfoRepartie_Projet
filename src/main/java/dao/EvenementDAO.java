package dao;

import model.Evenement;
import model.TypeEvenement;
import org.postgresql.util.PGInterval;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EvenementDAO {
    private Connection connection;

    // Constructeur avec injection de la connexion
    public EvenementDAO(Connection connection) {
        this.connection = connection;
    }

    // Méthode utilitaire pour obtenir une connexion valide
    private Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DAOFactory.getInstance().getConnection();
        }
        return connection;
    }

    // Méthode utilitaire pour convertir Time en PGInterval
    private PGInterval timeToInterval(Time time) {
        if (time == null) return null;

        // Extraire les heures, minutes et secondes
        int hours = time.getHours();
        int minutes = time.getMinutes();
        int seconds = time.getSeconds();

        // Créer un PGInterval (years, months, days, hours, minutes, seconds)
        return new PGInterval(0, 0, 0, hours, minutes, seconds);
    }

    // Méthode utilitaire pour convertir PGInterval en Time
    private Time intervalToTime(PGInterval interval) {
        if (interval == null) return null;

        // Convertir en millisecondes totales
        int totalSeconds = interval.getHours() * 3600 +
                interval.getMinutes() * 60 +
                (int) interval.getSeconds();

        // Créer un Time à partir des millisecondes
        return new Time(totalSeconds * 1000L);
    }

    // Récupère tous les événements
    public List<Evenement> getAll(String search) {
        List<Evenement> list = new ArrayList<>();

        String sql = "SELECT e.*, t.libelle AS type_libelle " +
                "FROM evenement e " +
                "JOIN type_evenement t ON e.id_type = t.id_type ";

        if (search != null && !search.trim().isEmpty()) {
            sql += "WHERE LOWER(e.nom) LIKE ? OR LOWER(t.libelle) LIKE ? ";
        }

        sql += "ORDER BY e.horodatage DESC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (search != null && !search.trim().isEmpty()) {
                String searchTerm = "%" + search.toLowerCase() + "%";
                stmt.setString(1, searchTerm);
                stmt.setString(2, searchTerm);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToEvenement(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


    // Alias de getAll()
    public List<Evenement> findAll() {
        return getAll(null);
    }

    // Trouve un événement par son ID
    public Evenement findById(int id) {
        String sql = "SELECT * FROM Evenement WHERE id_event = ?";

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

    // Vérifie si un fanfaron est dans la commission prestation
    public boolean estDansCommissionPrestation(int idFanfaron) {
        String sql = "SELECT 1 FROM fanfaron_groupe WHERE id_fanfaron = ? AND id_groupe = 1 LIMIT 1";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFanfaron);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification du groupe du fanfaron " + idFanfaron);
            e.printStackTrace();
        }

        return false;
    }





    // Insère un nouvel événement
    public boolean insert(Evenement evenement) {
        if (evenement == null || evenement.getNom() == null || evenement.getNom().trim().isEmpty()) {
            System.err.println("EvenementDAO: Impossible d'insérer un événement null ou avec un nom vide.");
            return false;
        }

        // Vérification du droit de créer
        if (!estDansCommissionPrestation(evenement.getIdCreateur())) {
            System.err.println("EvenementDAO: Ce fanfaron n'a pas le droit de créer un événement.");
            return false;
        }

        String sql = "INSERT INTO Evenement (nom, horodatage, duree, lieu, description, id_type, id_createur) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, evenement.getNom().trim());
            stmt.setTimestamp(2, new Timestamp(evenement.getHorodatage().getTime()));

            // Conversion Time vers PGInterval pour PostgreSQL
            PGInterval interval = timeToInterval(evenement.getDuree());
            stmt.setObject(3, interval);

            stmt.setString(4, evenement.getLieu());
            stmt.setString(5, evenement.getDescription());
            stmt.setInt(6, evenement.getIdType());
            stmt.setInt(7, evenement.getIdCreateur());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
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


    // Supprime par ID
    public boolean delete(int id) {
        if (id <= 0) {
            System.err.println("EvenementDAO: ID invalide pour la suppression: " + id);
            return false;
        }

        String sql = "DELETE FROM Evenement WHERE id_event = ?";
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

    // Supprime un événement
    public boolean delete(Evenement evenement) {
        if (evenement == null || evenement.getId() <= 0) {
            System.err.println("EvenementDAO: Impossible de supprimer un événement null ou avec un ID invalide.");
            return false;
        }
        return delete(evenement.getId());
    }



    // Méthode privée pour mapper le ResultSet vers un objet Evenement avec TypeEvenement
    private Evenement mapResultSetToEvenement(ResultSet rs) throws SQLException {
        Evenement evenement = new Evenement();
        evenement.setId(rs.getInt("id_event"));
        evenement.setNom(rs.getString("nom"));
        evenement.setHorodatage(rs.getTimestamp("horodatage"));

        // Gestion de l'INTERVAL PostgreSQL
        Object dureeObj = rs.getObject("duree");
        if (dureeObj != null && dureeObj instanceof PGInterval) {
            PGInterval interval = (PGInterval) dureeObj;
            Time duree = intervalToTime(interval);
            evenement.setDuree(duree);
        }

        evenement.setLieu(rs.getString("lieu"));
        evenement.setDescription(rs.getString("description"));
        evenement.setIdType(rs.getInt("id_type"));
        evenement.setIdCreateur(rs.getInt("id_createur"));

        TypeEvenement type = new TypeEvenement(
                rs.getInt("id_type"),
                rs.getString("type_libelle")
        );

        evenement.setTypeEvenement(type);

        return evenement;
    }

}