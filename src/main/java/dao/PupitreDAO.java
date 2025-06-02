package dao;

import model.Pupitre;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PupitreDAO {
    private Connection connection;

    // Constructeur qui reçoit la connexion depuis DAOFactory
    public PupitreDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Récupère tous les pupitres de la base de données
     * @return Liste de tous les pupitres
     */
    public List<Pupitre> getAll() {
        List<Pupitre> list = new ArrayList<>();
        String sql = "SELECT * FROM Pupitre";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Pupitre p = new Pupitre();
                p.setIdPupitre(rs.getInt("id_pupitre"));
                p.setLibelle(rs.getString("libelle"));
                list.add(p);
            }
        } catch (SQLException e) {
            System.err.println("PupitreDAO: Erreur lors de la récupération de tous les pupitres.");
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Alias pour getAll() - méthode standard des DAO
     * @return Liste de tous les pupitres
     */
    public List<Pupitre> findAll() {
        return getAll();
    }





    /**
     * Insère un nouveau pupitre dans la base de données
     * @param pupitre Le pupitre à insérer
     * @return true si l'insertion a réussi, false sinon
     */
    public boolean insert(Pupitre pupitre) {
        if (pupitre == null || pupitre.getLibelle() == null || pupitre.getLibelle().trim().isEmpty()) {
            System.err.println("PupitreDAO: Impossible d'insérer un pupitre null ou avec un libellé vide.");
            return false;
        }

        String sql = "INSERT INTO Pupitre (libelle) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, pupitre.getLibelle().trim());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Récupérer l'ID généré
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        pupitre.setIdPupitre(generatedKeys.getInt(1));
                    }
                }
                System.out.println("PupitreDAO: Pupitre inséré avec succès - ID: " + pupitre.getIdPupitre());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("PupitreDAO: Erreur lors de l'insertion du pupitre: " + pupitre.getLibelle());
            e.printStackTrace();
        }
        return false;
    }



    /**
     * Supprime un pupitre par son ID
     * @param id L'identifiant du pupitre à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean delete(int id) {
        if (id <= 0) {
            System.err.println("PupitreDAO: ID invalide pour la suppression: " + id);
            return false;
        }

        String sql = "DELETE FROM Pupitre WHERE id_pupitre = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("PupitreDAO: Pupitre supprimé avec succès - ID: " + id);
                return true;
            } else {
                System.out.println("PupitreDAO: Aucun pupitre trouvé avec l'ID: " + id);
            }
        } catch (SQLException e) {
            System.err.println("PupitreDAO: Erreur lors de la suppression du pupitre - ID: " + id);
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Supprime un pupitre
     * @param pupitre Le pupitre à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean delete(Pupitre pupitre) {
        if (pupitre == null || pupitre.getIdPupitre() <= 0) {
            System.err.println("PupitreDAO: Impossible de supprimer un pupitre null ou avec un ID invalide.");
            return false;
        }
        return delete(pupitre.getIdPupitre());
    }


}