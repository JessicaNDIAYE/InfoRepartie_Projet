package dao;

import model.Groupe;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroupeDAO {
    private Connection connection;

    // Constructeur qui reçoit la connexion depuis DAOFactory
    public GroupeDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Récupère tous les groupes de la base de données
     * @return Liste de tous les groupes
     */
    public List<Groupe> getAll() {
        List<Groupe> list = new ArrayList<>();
        String sql = "SELECT * FROM Groupe";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Groupe g = new Groupe();
                g.setIdGroupe(rs.getInt("id_groupe"));
                g.setLibelle(rs.getString("libelle"));
                list.add(g);
            }
        } catch (SQLException e) {
            System.err.println("GroupeDAO: Erreur lors de la récupération de tous les groupes.");
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Alias pour getAll() - méthode standard des DAO
     * @return Liste de tous les groupes
     */
    public List<Groupe> findAll() {
        return getAll();
    }


    /**
     * Insère un nouveau groupe dans la base de données
     * @param groupe Le groupe à insérer
     * @return true si l'insertion a réussi, false sinon
     */
    public boolean insert(Groupe groupe) {
        if (groupe == null || groupe.getLibelle() == null || groupe.getLibelle().trim().isEmpty()) {
            System.err.println("GroupeDAO: Impossible d'insérer un groupe null ou avec un libellé vide.");
            return false;
        }

        String sql = "INSERT INTO Groupe (libelle) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, groupe.getLibelle().trim());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Récupérer l'ID généré
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        groupe.setIdGroupe(generatedKeys.getInt(1));
                    }
                }
                System.out.println("GroupeDAO: Groupe inséré avec succès - ID: " + groupe.getIdGroupe());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("GroupeDAO: Erreur lors de l'insertion du groupe: " + groupe.getLibelle());
            e.printStackTrace();
        }
        return false;
    }



    /**
     * Supprime un groupe par son ID
     * @param id L'identifiant du groupe à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean delete(int id) {
        if (id <= 0) {
            System.err.println("GroupeDAO: ID invalide pour la suppression: " + id);
            return false;
        }

        String sql = "DELETE FROM Groupe WHERE id_groupe = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("GroupeDAO: Groupe supprimé avec succès - ID: " + id);
                return true;
            } else {
                System.out.println("GroupeDAO: Aucun groupe trouvé avec l'ID: " + id);
            }
        } catch (SQLException e) {
            System.err.println("GroupeDAO: Erreur lors de la suppression du groupe - ID: " + id);
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Supprime un groupe
     * @param groupe Le groupe à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean delete(Groupe groupe) {
        if (groupe == null || groupe.getIdGroupe() <= 0) {
            System.err.println("GroupeDAO: Impossible de supprimer un groupe null ou avec un ID invalide.");
            return false;
        }
        return delete(groupe.getIdGroupe());
    }

}