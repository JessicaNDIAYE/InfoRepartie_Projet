package dao;

import model.FanfaronPupitre;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FanfaronPupitreDAO {
    private final Connection connection;

    public FanfaronPupitreDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Ajoute une association Fanfaron-Pupitre.
     */
    public boolean insert(FanfaronPupitre association) throws SQLException {
        String sql = "INSERT INTO Fanfaron_Pupitre (id_fanfaron, id_pupitre) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, association.getIdFanfaron());
            stmt.setInt(2, association.getIdPupitre());
            return stmt.executeUpdate() == 1;
        }
    }

    /**
     * Supprime une association Fanfaron-Pupitre.
     */
    public boolean delete(FanfaronPupitre association) throws SQLException {
        String sql = "DELETE FROM Fanfaron_Pupitre WHERE id_fanfaron = ? AND id_pupitre = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, association.getIdFanfaron());
            stmt.setInt(2, association.getIdPupitre());
            return stmt.executeUpdate() == 1;
        }
    }

    /**
     * Supprime toutes les associations pour un fanfaron donné.
     */
    public boolean deleteByFanfaronId(int idFanfaron) throws SQLException {
        String sql = "DELETE FROM Fanfaron_Pupitre WHERE id_fanfaron = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idFanfaron);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Récupère tous les pupitres associés à un fanfaron.
     */
    public List<FanfaronPupitre> findByFanfaronId(int idFanfaron) throws SQLException {
        List<FanfaronPupitre> result = new ArrayList<>();
        String sql = "SELECT id_fanfaron, id_pupitre FROM Fanfaron_Pupitre WHERE id_fanfaron = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idFanfaron);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    FanfaronPupitre assoc = new FanfaronPupitre();
                    assoc.setIdFanfaron(rs.getInt("id_fanfaron"));
                    assoc.setIdPupitre(rs.getInt("id_pupitre"));
                    result.add(assoc);
                }
            }
        }
        return result;
    }

    /**
     * Récupère toutes les associations fanfaron-pupitre.
     */
    public List<FanfaronPupitre> findAll() throws SQLException {
        List<FanfaronPupitre> result = new ArrayList<>();
        String sql = "SELECT id_fanfaron, id_pupitre FROM Fanfaron_Pupitre";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                FanfaronPupitre assoc = new FanfaronPupitre();
                assoc.setIdFanfaron(rs.getInt("id_fanfaron"));
                assoc.setIdPupitre(rs.getInt("id_pupitre"));
                result.add(assoc);
            }
        }
        return result;
    }
}
