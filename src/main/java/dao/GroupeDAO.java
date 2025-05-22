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
     * Trouve un groupe par son ID
     * @param id L'identifiant du groupe
     * @return Le groupe trouvé ou null si non trouvé
     */
    public Groupe findById(int id) {
        String sql = "SELECT * FROM Groupe WHERE id_groupe = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Groupe g = new Groupe();
                    g.setIdGroupe(rs.getInt("id_groupe"));
                    g.setLibelle(rs.getString("libelle"));
                    return g;
                }
            }
        } catch (SQLException e) {
            System.err.println("GroupeDAO: Erreur lors de la recherche du groupe avec ID: " + id);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Trouve un groupe par son libellé
     * @param libelle Le libellé du groupe
     * @return Le groupe trouvé ou null si non trouvé
     */
    public Groupe findByLibelle(String libelle) {
        String sql = "SELECT * FROM Groupe WHERE libelle = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, libelle);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Groupe g = new Groupe();
                    g.setIdGroupe(rs.getInt("id_groupe"));
                    g.setLibelle(rs.getString("libelle"));
                    return g;
                }
            }
        } catch (SQLException e) {
            System.err.println("GroupeDAO: Erreur lors de la recherche du groupe avec libellé: " + libelle);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Recherche des groupes par libellé (recherche partielle)
     * @param libelle Le libellé à rechercher (recherche LIKE)
     * @return Liste des groupes trouvés
     */
    public List<Groupe> findByLibelleLike(String libelle) {
        List<Groupe> list = new ArrayList<>();
        String sql = "SELECT * FROM Groupe WHERE libelle ILIKE ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + libelle + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Groupe g = new Groupe();
                    g.setIdGroupe(rs.getInt("id_groupe"));
                    g.setLibelle(rs.getString("libelle"));
                    list.add(g);
                }
            }
        } catch (SQLException e) {
            System.err.println("GroupeDAO: Erreur lors de la recherche des groupes avec libellé contenant: " + libelle);
            e.printStackTrace();
        }
        return list;
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
     * Met à jour un groupe existant
     * @param groupe Le groupe à mettre à jour
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean update(Groupe groupe) {
        if (groupe == null || groupe.getIdGroupe() <= 0 ||
                groupe.getLibelle() == null || groupe.getLibelle().trim().isEmpty()) {
            System.err.println("GroupeDAO: Impossible de mettre à jour un groupe invalide.");
            return false;
        }

        String sql = "UPDATE Groupe SET libelle = ? WHERE id_groupe = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, groupe.getLibelle().trim());
            stmt.setInt(2, groupe.getIdGroupe());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("GroupeDAO: Groupe mis à jour avec succès - ID: " + groupe.getIdGroupe());
                return true;
            } else {
                System.out.println("GroupeDAO: Aucun groupe trouvé avec l'ID: " + groupe.getIdGroupe());
            }
        } catch (SQLException e) {
            System.err.println("GroupeDAO: Erreur lors de la mise à jour du groupe - ID: " + groupe.getIdGroupe());
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

    /**
     * Vérifie si un groupe existe par son ID
     * @param id L'identifiant du groupe
     * @return true si le groupe existe, false sinon
     */
    public boolean exists(int id) {
        String sql = "SELECT COUNT(*) FROM Groupe WHERE id_groupe = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("GroupeDAO: Erreur lors de la vérification d'existence - ID: " + id);
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Vérifie si un groupe existe par son libellé
     * @param libelle Le libellé du groupe
     * @return true si le groupe existe, false sinon
     */
    public boolean existsByLibelle(String libelle) {
        if (libelle == null || libelle.trim().isEmpty()) {
            return false;
        }

        String sql = "SELECT COUNT(*) FROM Groupe WHERE libelle = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, libelle.trim());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("GroupeDAO: Erreur lors de la vérification d'existence par libellé: " + libelle);
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Compte le nombre total de groupes
     * @return Le nombre de groupes
     */
    public int count() {
        String sql = "SELECT COUNT(*) FROM Groupe";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("GroupeDAO: Erreur lors du comptage des groupes.");
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Récupère les groupes avec pagination
     * @param offset Le décalage (nombre d'enregistrements à ignorer)
     * @param limit Le nombre maximum d'enregistrements à retourner
     * @return Liste paginée des groupes
     */
    public List<Groupe> findWithPagination(int offset, int limit) {
        List<Groupe> list = new ArrayList<>();
        String sql = "SELECT * FROM Groupe ORDER BY libelle LIMIT ? OFFSET ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Groupe g = new Groupe();
                    g.setIdGroupe(rs.getInt("id_groupe"));
                    g.setLibelle(rs.getString("libelle"));
                    list.add(g);
                }
            }
        } catch (SQLException e) {
            System.err.println("GroupeDAO: Erreur lors de la récupération paginée des groupes.");
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Récupère les groupes triés par libellé
     * @param ascending true pour tri ascendant, false pour descendant
     * @return Liste triée des groupes
     */
    public List<Groupe> findAllSorted(boolean ascending) {
        List<Groupe> list = new ArrayList<>();
        String sql = "SELECT * FROM Groupe ORDER BY libelle " + (ascending ? "ASC" : "DESC");

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Groupe g = new Groupe();
                g.setIdGroupe(rs.getInt("id_groupe"));
                g.setLibelle(rs.getString("libelle"));
                list.add(g);
            }
        } catch (SQLException e) {
            System.err.println("GroupeDAO: Erreur lors de la récupération triée des groupes.");
            e.printStackTrace();
        }
        return list;
    }
}