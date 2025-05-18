package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Fanfaron;

public class FanfaronDAO {

    public boolean checkNomFanfaronExists(String nomFanfaron) {
        boolean exists = false;
        String query = "SELECT COUNT(*) FROM Fanfaron WHERE nom_fanfaron = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, nomFanfaron.trim());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                exists = rs.getInt(1) > 0;
            }
            System.out.println("checkNomFanfaronExists('" + nomFanfaron + "') = " + exists);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exists;
    }

    public boolean checkEmailExists(String email) {
        boolean exists = false;
        String query = "SELECT COUNT(*) FROM Fanfaron WHERE email = ?";

        if (email == null) email = "";
        email = email.trim();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                exists = rs.getInt(1) > 0;
            }
            System.out.println("checkEmailExists('" + email + "') = " + exists);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exists;
    }

    public int insertFanfaron(Fanfaron fanfaron) {
        String query = "INSERT INTO Fanfaron (nom_fanfaron, nom, prenom, genre, email, mdp, contraintes_alimentaires, date_creation) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_DATE)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, fanfaron.getNomFanfaron().trim());
            ps.setString(2, fanfaron.getNom().trim());
            ps.setString(3, fanfaron.getPrenom().trim());
            ps.setString(4, fanfaron.getGenre().trim());
            ps.setString(5, fanfaron.getEmail().trim());
            ps.setString(6, fanfaron.getMdp().trim());
            ps.setString(7, fanfaron.getContraintesAlimentaires().trim());


            System.out.println("Exécution de la requête d'insertion...");
            int rowsAffected = ps.executeUpdate();
            System.out.println("Lignes insérées : " + rowsAffected);

            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int generatedId = rs.getInt(1);
                        System.out.println("Nouvel ID généré : " + generatedId);
                        return generatedId;
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur SQL lors de l'insertion :");
            e.printStackTrace();
        }

        return -1;
    }

    public Fanfaron authenticateFanfaron(String identifiant, String motDePasse) {
        System.out.println("Authentification en cours pour : " + identifiant);
        String query = "SELECT * FROM Fanfaron WHERE (nom_fanfaron = ? OR email = ?) AND mdp = ?";

        if (identifiant == null) identifiant = "";
        if (motDePasse == null) motDePasse = "";

        identifiant = identifiant.trim();
        motDePasse = motDePasse.trim();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, identifiant);
            ps.setString(2, identifiant);
            ps.setString(3, motDePasse);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Utilisateur trouvé : " + identifiant);
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

                updateDerniereConnexion(fanfaron.getId());

                return fanfaron;
            } else {
                System.out.println("Aucun utilisateur trouvé.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur dans authenticateFanfaron: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    private void updateDerniereConnexion(int id) {
        System.out.println("Mise à jour de la dernière connexion pour l'utilisateur ID : " + id);
        String query = "UPDATE Fanfaron SET derniere_connexion = CURRENT_TIMESTAMP WHERE id_fanfaron = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            System.out.println("Lignes affectées lors de la mise à jour de dernière connexion : " + rows);

        } catch (SQLException e) {
            System.err.println("Erreur dans updateDerniereConnexion: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Fanfaron> getAllFanfarons() {
        System.out.println("Récupération de tous les fanfarons...");
        List<Fanfaron> fanfarons = new ArrayList<>();
        String query = "SELECT * FROM Fanfaron ORDER BY nom, prenom";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
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

            System.out.println("Nombre total de fanfarons récupérés : " + fanfarons.size());

        } catch (SQLException e) {
            System.err.println("Erreur dans getAllFanfarons: " + e.getMessage());
            e.printStackTrace();
        }

        return fanfarons;
    }
}
