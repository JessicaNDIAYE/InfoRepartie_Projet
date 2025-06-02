package dao;

import model.StatutParticipation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StatutParticipationDAO {
    private final Connection connection;

    public StatutParticipationDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Récupère tous les statuts de participation.
     */
    public List<StatutParticipation> findAll() throws SQLException {
        List<StatutParticipation> statuts = new ArrayList<>();
        String sql = "SELECT id_statut, libelle, couleur FROM Statut_Participation";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                StatutParticipation statut = new StatutParticipation();
                statut.setIdStatut(rs.getInt("id_statut"));
                statut.setLibelle(rs.getString("libelle"));
                statut.setCouleur(rs.getString("couleur"));
                statuts.add(statut);
            }
        }
        return statuts;
    }



    /**
     * Insère un nouveau statut de participation.
     */
    public boolean insert(StatutParticipation statut) throws SQLException {
        String sql = "INSERT INTO Statut_Participation (libelle, couleur) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, statut.getLibelle());
            stmt.setString(2, statut.getCouleur());
            return stmt.executeUpdate() == 1;
        }
    }



    /**
     * Supprime un statut de participation par ID.
     */
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM Statut_Participation WHERE id_statut = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() == 1;
        }
    }
}
