package dao;

import model.FanfaronEvenementParticipation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FanfaronEvenementParticipationDAO {
    private final Connection connection;

    public FanfaronEvenementParticipationDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Ajoute une participation à un événement.
     */
    public boolean insert(FanfaronEvenementParticipation participation) throws SQLException {
        String sql = "INSERT INTO Fanfaron_Evenement_Participation (id_fanfaron, id_event, id_pupitre, id_statut_participation) " +
                "VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, participation.getIdFanfaron());
            stmt.setInt(2, participation.getIdEvent());
            stmt.setInt(3, participation.getIdPupitre());
            stmt.setInt(4, participation.getIdStatutParticipation());
            return stmt.executeUpdate() == 1;
        }
    }

    /**
     * Met à jour le statut de participation d'un fanfaron pour un événement.
     */
    public boolean update(FanfaronEvenementParticipation participation) throws SQLException {
        String sql = "UPDATE Fanfaron_Evenement_Participation SET id_pupitre = ?, id_statut_participation = ? " +
                "WHERE id_fanfaron = ? AND id_event = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, participation.getIdPupitre());
            stmt.setInt(2, participation.getIdStatutParticipation());
            stmt.setInt(3, participation.getIdFanfaron());
            stmt.setInt(4, participation.getIdEvent());
            return stmt.executeUpdate() == 1;
        }
    }

    /**
     * Supprime une participation à un événement.
     */
    public boolean delete(int idFanfaron, int idEvent) throws SQLException {
        String sql = "DELETE FROM Fanfaron_Evenement_Participation WHERE id_fanfaron = ? AND id_event = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idFanfaron);
            stmt.setInt(2, idEvent);
            return stmt.executeUpdate() == 1;
        }
    }

    /**
     * Récupère une participation spécifique.
     */
    public FanfaronEvenementParticipation find(int idFanfaron, int idEvent) throws SQLException {
        String sql = "SELECT * FROM Fanfaron_Evenement_Participation WHERE id_fanfaron = ? AND id_event = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idFanfaron);
            stmt.setInt(2, idEvent);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    FanfaronEvenementParticipation p = new FanfaronEvenementParticipation();
                    p.setIdFanfaron(rs.getInt("id_fanfaron"));
                    p.setIdEvent(rs.getInt("id_event"));
                    p.setIdPupitre(rs.getInt("id_pupitre"));
                    p.setIdStatutParticipation(rs.getInt("id_statut_participation"));
                    return p;
                }
                return null;
            }
        }
    }

    /**
     * Liste toutes les participations d'un fanfaron.
     */
    public List<FanfaronEvenementParticipation> findByFanfaron(int idFanfaron) throws SQLException {
        List<FanfaronEvenementParticipation> participations = new ArrayList<>();
        String sql = "SELECT * FROM Fanfaron_Evenement_Participation WHERE id_fanfaron = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idFanfaron);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    FanfaronEvenementParticipation p = new FanfaronEvenementParticipation();
                    p.setIdFanfaron(rs.getInt("id_fanfaron"));
                    p.setIdEvent(rs.getInt("id_event"));
                    p.setIdPupitre(rs.getInt("id_pupitre"));
                    p.setIdStatutParticipation(rs.getInt("id_statut_participation"));
                    participations.add(p);
                }
            }
        }
        return participations;
    }

    /**
     * Liste toutes les participations pour un événement.
     */
    public List<FanfaronEvenementParticipation> findByEvent(int idEvent) throws SQLException {
        List<FanfaronEvenementParticipation> participations = new ArrayList<>();
        String sql = "SELECT * FROM Fanfaron_Evenement_Participation WHERE id_event = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idEvent);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    FanfaronEvenementParticipation p = new FanfaronEvenementParticipation();
                    p.setIdFanfaron(rs.getInt("id_fanfaron"));
                    p.setIdEvent(rs.getInt("id_event"));
                    p.setIdPupitre(rs.getInt("id_pupitre"));
                    p.setIdStatutParticipation(rs.getInt("id_statut_participation"));
                    participations.add(p);
                }
            }
        }
        return participations;
    }
    public void saveOrUpdate(FanfaronEvenementParticipation participation) throws SQLException {
        FanfaronEvenementParticipation existing = find(participation.getIdFanfaron(), participation.getIdEvent());

        if (existing == null) {
            insert(participation);
        } else {
            update(participation);
        }
    }

}
