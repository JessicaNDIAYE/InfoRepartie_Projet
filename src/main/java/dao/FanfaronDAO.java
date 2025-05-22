package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Fanfaron;

public class FanfaronDAO {

    private Connection connection;

    // Constructeur qui prend une connexion en paramètre (appelé par DAOFactory)
    public FanfaronDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean checkNomFanfaronExists(String nomFanfaron) {
        boolean exists = false;
        String query = "SELECT COUNT(*) FROM Fanfaron WHERE nom_fanfaron = ?";
        System.out.println("FanfaronDAO: Vérification de l'existence du nom de fanfaron: '" + nomFanfaron + "'");

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, nomFanfaron.trim());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                exists = rs.getInt(1) > 0;
            }
            System.out.println("FanfaronDAO: checkNomFanfaronExists('" + nomFanfaron + "') = " + exists);
        } catch (SQLException e) {
            System.err.println("FanfaronDAO: Erreur SQL lors de checkNomFanfaronExists:");
            e.printStackTrace(); // Affiche la pile d'erreur complète
        }

        return exists;
    }

    public boolean checkEmailExists(String email) {
        boolean exists = false;
        String query = "SELECT COUNT(*) FROM Fanfaron WHERE email = ?";
        System.out.println("FanfaronDAO: Vérification de l'existence de l'email: '" + email + "'");

        if (email == null) email = "";
        email = email.trim();

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                exists = rs.getInt(1) > 0;
            }
            System.out.println("FanfaronDAO: checkEmailExists('" + email + "') = " + exists);
        } catch (SQLException e) {
            System.err.println("FanfaronDAO: Erreur SQL lors de checkEmailExists:");
            e.printStackTrace(); // Affiche la pile d'erreur complète
        }

        return exists;
    }

    public int insertFanfaron(Fanfaron fanfaron) {
        String query = "INSERT INTO Fanfaron (nom_fanfaron, nom, prenom, genre, email, mdp, contraintes_alimentaires, date_creation, admin) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_DATE, FALSE)"; // FALSE par défaut pour 'admin'

        System.out.println("FanfaronDAO: Tentative d'insertion du fanfaron: " + fanfaron.getNomFanfaron());
        System.out.println("Requête d'insertion: " + query); // Log la requête pour vérification

        try (PreparedStatement ps = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, fanfaron.getNomFanfaron().trim());
            ps.setString(2, fanfaron.getNom().trim());
            ps.setString(3, fanfaron.getPrenom().trim());
            ps.setString(4, fanfaron.getGenre().trim());
            ps.setString(5, fanfaron.getEmail().trim());
            ps.setString(6, fanfaron.getMdp().trim());
            ps.setString(7, fanfaron.getContraintesAlimentaires().trim());

            System.out.println("FanfaronDAO: Exécution de la requête d'insertion...");
            int rowsAffected = ps.executeUpdate();
            System.out.println("FanfaronDAO: Lignes insérées : " + rowsAffected);

            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int generatedId = rs.getInt(1);
                        System.out.println("FanfaronDAO: Nouvel ID généré : " + generatedId);
                        return generatedId;
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("FanfaronDAO: Erreur SQL lors de l'insertion de Fanfaron:");
            e.printStackTrace(); // TRÈS IMPORTANT: Affiche l'erreur exacte de la base de données
        } catch (Exception e) {
            System.err.println("FanfaronDAO: Erreur inattendue lors de l'insertion de Fanfaron:");
            e.printStackTrace(); // Pour capturer d'autres types d'erreurs
        }

        return -1; // Indique un échec de l'insertion
    }

    public Fanfaron authenticateFanfaron(String identifiant, String mdp) {
        Fanfaron fanfaron = null;
        String sql = "SELECT * FROM fanfaron WHERE (nom_fanfaron = ? OR email = ?) AND mdp = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, identifiant);
            stmt.setString(2, identifiant);
            stmt.setString(3, mdp);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    fanfaron = new Fanfaron();
                    fanfaron.setId(rs.getInt("id_fanfaron"));
                    fanfaron.setNomFanfaron(rs.getString("nom_fanfaron"));
                    fanfaron.setNom(rs.getString("nom"));
                    fanfaron.setPrenom(rs.getString("prenom"));
                    fanfaron.setGenre(rs.getString("genre"));
                    fanfaron.setEmail(rs.getString("email"));
                    fanfaron.setMdp(rs.getString("mdp"));
                    fanfaron.setContraintesAlimentaires(rs.getString("contraintes_alimentaires"));
                    fanfaron.setDateCreation(rs.getDate("date_creation"));
                    fanfaron.setDerniereConnexion(rs.getDate("derniere_connexion"));
                    fanfaron.setAdmin(rs.getBoolean("admin")); // <- Important

                    // Mettre à jour la dernière connexion après authentification réussie
                    updateDerniereConnexion(fanfaron.getId());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fanfaron;
    }

    private void updateDerniereConnexion(int id) {
        System.out.println("FanfaronDAO: Mise à jour de la dernière connexion pour l'utilisateur ID : " + id);
        String query = "UPDATE Fanfaron SET derniere_connexion = CURRENT_TIMESTAMP WHERE id_fanfaron = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            System.out.println("FanfaronDAO: Lignes affectées lors de la mise à jour de dernière connexion : " + rows);

        } catch (SQLException e) {
            System.err.println("FanfaronDAO: Erreur SQL dans updateDerniereConnexion:");
            e.printStackTrace();
        }
    }

    public List<Fanfaron> getAllFanfarons() {
        System.out.println("FanfaronDAO: Récupération de tous les fanfarons...");
        List<Fanfaron> fanfarons = new ArrayList<>();
        String query = "SELECT * FROM Fanfaron ORDER BY nom_fanfaron"; // Tri par nom de fanfaron pour une meilleure lisibilité

        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Fanfaron fanfaron = new Fanfaron();
                fanfaron.setId(rs.getInt("id_fanfaron"));
                fanfaron.setNomFanfaron(rs.getString("nom_fanfaron"));
                fanfaron.setNom(rs.getString("nom"));
                fanfaron.setPrenom(rs.getString("prenom"));
                fanfaron.setGenre(rs.getString("genre"));
                fanfaron.setEmail(rs.getString("email"));
                fanfaron.setContraintesAlimentaires(rs.getString("contraintes_alimentaires"));
                fanfaron.setDateCreation(rs.getDate("date_creation"));
                fanfaron.setDerniereConnexion(rs.getDate("derniere_connexion"));
                fanfaron.setAdmin(rs.getBoolean("admin"));

                fanfarons.add(fanfaron);
            }

            System.out.println("FanfaronDAO: Nombre total de fanfarons récupérés : " + fanfarons.size());

        } catch (SQLException e) {
            System.err.println("FanfaronDAO: Erreur SQL dans getAllFanfarons:");
            e.printStackTrace();
        }

        return fanfarons;
    }

    /**
     * Récupérer un fanfaron par son ID
     */
    public Fanfaron getFanfaronById(int id) {
        System.out.println("FanfaronDAO: Récupération du fanfaron avec l'ID : " + id);
        String query = "SELECT * FROM Fanfaron WHERE id_fanfaron = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Fanfaron fanfaron = new Fanfaron();
                fanfaron.setId(rs.getInt("id_fanfaron"));
                fanfaron.setNomFanfaron(rs.getString("nom_fanfaron"));
                fanfaron.setNom(rs.getString("nom"));
                fanfaron.setPrenom(rs.getString("prenom"));
                fanfaron.setGenre(rs.getString("genre"));
                fanfaron.setEmail(rs.getString("email"));
                fanfaron.setMdp(rs.getString("mdp"));
                fanfaron.setContraintesAlimentaires(rs.getString("contraintes_alimentaires"));
                fanfaron.setDateCreation(rs.getDate("date_creation"));
                fanfaron.setDerniereConnexion(rs.getDate("derniere_connexion"));
                fanfaron.setAdmin(rs.getBoolean("admin"));

                System.out.println("FanfaronDAO: Fanfaron trouvé: " + fanfaron.getNomFanfaron());
                return fanfaron;
            } else {
                System.out.println("FanfaronDAO: Aucun fanfaron trouvé pour l'ID : " + id);
            }

        } catch (SQLException e) {
            System.err.println("FanfaronDAO: Erreur SQL dans getFanfaronById:");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Vérifier si un nom de fanfaron existe déjà (sauf pour un utilisateur spécifique)
     */
    public boolean checkNomFanfaronExistsExcept(String nomFanfaron, int exceptId) {
        boolean exists = false;
        String query = "SELECT COUNT(*) FROM Fanfaron WHERE nom_fanfaron = ? AND id_fanfaron != ?";
        System.out.println("FanfaronDAO: Vérification de l'existence du nom de fanfaron '" + nomFanfaron + "' (sauf ID " + exceptId + ")");

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, nomFanfaron.trim());
            ps.setInt(2, exceptId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                exists = rs.getInt(1) > 0;
            }
            System.out.println("FanfaronDAO: checkNomFanfaronExistsExcept result: " + exists);
        } catch (SQLException e) {
            System.err.println("FanfaronDAO: Erreur SQL lors de checkNomFanfaronExistsExcept:");
            e.printStackTrace();
        }

        return exists;
    }

    /**
     * Vérifier si un email existe déjà (sauf pour un utilisateur spécifique)
     */
    public boolean checkEmailExistsExcept(String email, int exceptId) {
        boolean exists = false;
        String query = "SELECT COUNT(*) FROM Fanfaron WHERE email = ? AND id_fanfaron != ?";
        System.out.println("FanfaronDAO: Vérification de l'existence de l'email '" + email + "' (sauf ID " + exceptId + ")");

        if (email == null) email = "";
        email = email.trim();

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, email);
            ps.setInt(2, exceptId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                exists = rs.getInt(1) > 0;
            }
            System.out.println("FanfaronDAO: checkEmailExistsExcept result: " + exists);
        } catch (SQLException e) {
            System.err.println("FanfaronDAO: Erreur SQL lors de checkEmailExistsExcept:");
            e.printStackTrace();
        }

        return exists;
    }

    /**
     * Mettre à jour un fanfaron
     */
    public boolean updateFanfaron(Fanfaron fanfaron) {
        // Note: Le mot de passe n'est pas mis à jour ici. S'il doit l'être, il faut une méthode séparée.
        String query = "UPDATE Fanfaron SET nom_fanfaron = ?, nom = ?, prenom = ?, genre = ?, email = ?, contraintes_alimentaires = ? WHERE id_fanfaron = ?";
        System.out.println("FanfaronDAO: Mise à jour du fanfaron ID : " + fanfaron.getId());

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, fanfaron.getNomFanfaron().trim());
            ps.setString(2, fanfaron.getNom().trim());
            ps.setString(3, fanfaron.getPrenom().trim());
            ps.setString(4, fanfaron.getGenre().trim());
            ps.setString(5, fanfaron.getEmail().trim());
            ps.setString(6, fanfaron.getContraintesAlimentaires().trim());
            ps.setInt(7, fanfaron.getId());

            int rowsAffected = ps.executeUpdate();
            System.out.println("FanfaronDAO: Lignes affectées lors de la mise à jour : " + rowsAffected);

            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("FanfaronDAO: Erreur SQL dans updateFanfaron:");
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Supprimer un fanfaron
     */
    public boolean deleteFanfaron(int id) {
        String query = "DELETE FROM Fanfaron WHERE id_fanfaron = ?";
        System.out.println("FanfaronDAO: Suppression du fanfaron ID : " + id);

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();
            System.out.println("FanfaronDAO: Lignes affectées lors de la suppression : " + rowsAffected);

            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("FanfaronDAO: Erreur SQL dans deleteFanfaron:");
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Basculer le statut administrateur d'un utilisateur
     */
    public boolean toggleAdminStatus(int id) {
        String query = "UPDATE Fanfaron SET admin = NOT admin WHERE id_fanfaron = ?";
        System.out.println("FanfaronDAO: Basculement du statut admin pour l'ID : " + id);

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();
            System.out.println("FanfaronDAO: Lignes affectées lors du basculement admin : " + rowsAffected);

            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("FanfaronDAO: Erreur SQL dans toggleAdminStatus:");
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Mettre à jour le mot de passe d'un fanfaron
     */
    public boolean updatePassword(int id, String newPassword) {
        String query = "UPDATE Fanfaron SET mdp = ? WHERE id_fanfaron = ?";
        System.out.println("FanfaronDAO: Mise à jour du mot de passe pour l'ID : " + id);

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, newPassword.trim());
            ps.setInt(2, id);

            int rowsAffected = ps.executeUpdate();
            System.out.println("FanfaronDAO: Lignes affectées lors de la mise à jour du mot de passe : " + rowsAffected);

            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("FanfaronDAO: Erreur SQL dans updatePassword:");
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Rechercher des fanfarons par nom ou nom de fanfaron
     */
    public List<Fanfaron> searchFanfarons(String searchTerm) {
        List<Fanfaron> fanfarons = new ArrayList<>();
        String query = "SELECT * FROM Fanfaron WHERE nom_fanfaron LIKE ? OR nom LIKE ? OR prenom LIKE ? ORDER BY nom_fanfaron";
        System.out.println("FanfaronDAO: Recherche de fanfarons avec le terme : " + searchTerm);

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            String searchPattern = "%" + searchTerm.trim() + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Fanfaron fanfaron = new Fanfaron();
                fanfaron.setId(rs.getInt("id_fanfaron"));
                fanfaron.setNomFanfaron(rs.getString("nom_fanfaron"));
                fanfaron.setNom(rs.getString("nom"));
                fanfaron.setPrenom(rs.getString("prenom"));
                fanfaron.setGenre(rs.getString("genre"));
                fanfaron.setEmail(rs.getString("email"));
                fanfaron.setContraintesAlimentaires(rs.getString("contraintes_alimentaires"));
                fanfaron.setDateCreation(rs.getDate("date_creation"));
                fanfaron.setDerniereConnexion(rs.getDate("derniere_connexion"));
                fanfaron.setAdmin(rs.getBoolean("admin"));

                fanfarons.add(fanfaron);
            }

            System.out.println("FanfaronDAO: Nombre de fanfarons trouvés : " + fanfarons.size());

        } catch (SQLException e) {
            System.err.println("FanfaronDAO: Erreur SQL dans searchFanfarons:");
            e.printStackTrace();
        }

        return fanfarons;
    }

    /**
     * Vérifier si un fanfaron existe
     */
    public boolean fanfaronExists(int id) {
        String query = "SELECT COUNT(*) FROM Fanfaron WHERE id_fanfaron = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("FanfaronDAO: Erreur SQL dans fanfaronExists:");
            e.printStackTrace();
        }

        return false;
    }
}