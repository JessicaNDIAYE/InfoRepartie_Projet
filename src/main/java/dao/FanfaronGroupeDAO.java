package dao;

import model.FanfaronGroupe;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FanfaronGroupeDAO {
    private final Connection connection;

    public FanfaronGroupeDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Ajoute une association Fanfaron-Groupe.
     */
    public boolean insert(FanfaronGroupe association) throws SQLException {
        String sql = "INSERT INTO Fanfaron_Groupe (id_fanfaron, id_groupe) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, association.getIdFanfaron());
            stmt.setInt(2, association.getIdGroupe());
            return stmt.executeUpdate() == 1;
        }
    }

    /**
     * Supprime une association Fanfaron-Groupe.
     */
    public boolean delete(FanfaronGroupe association) throws SQLException {
        String sql = "DELETE FROM Fanfaron_Groupe WHERE id_fanfaron = ? AND id_groupe = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, association.getIdFanfaron());
            stmt.setInt(2, association.getIdGroupe());
            return stmt.executeUpdate() == 1;
        }
    }

    /**
     * Supprime toutes les associations pour un fanfaron donné.
     */
    public boolean deleteByFanfaronId(int idFanfaron) throws SQLException {
        String sql = "DELETE FROM Fanfaron_Groupe WHERE id_fanfaron = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idFanfaron);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Récupère tous les groupes associés à un fanfaron.
     */
    public List<FanfaronGroupe> findByFanfaronId(int idFanfaron) throws SQLException {
        List<FanfaronGroupe> result = new ArrayList<>();
        String sql = "SELECT id_fanfaron, id_groupe FROM Fanfaron_Groupe WHERE id_fanfaron = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idFanfaron);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    FanfaronGroupe assoc = new FanfaronGroupe();
                    assoc.setIdFanfaron(rs.getInt("id_fanfaron"));
                    assoc.setIdGroupe(rs.getInt("id_groupe"));
                    result.add(assoc);
                }
            }
        }
        return result;
    }

    /**
     * Récupère toutes les associations fanfaron-groupe.
     */
    public List<FanfaronGroupe> findAll() throws SQLException {
        List<FanfaronGroupe> result = new ArrayList<>();
        String sql = "SELECT id_fanfaron, id_groupe FROM Fanfaron_Groupe";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                FanfaronGroupe assoc = new FanfaronGroupe();
                assoc.setIdFanfaron(rs.getInt("id_fanfaron"));
                assoc.setIdGroupe(rs.getInt("id_groupe"));
                result.add(assoc);
            }
        }
        return result;
    }
}
