package dao;

import model.TypeEvenement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TypeEvenementDAO {

    private final Connection connection;

    // Constructor
    public TypeEvenementDAO(Connection connection) {
        this.connection = connection;
    }



    // Read - Get all TypeEvenement
    public List<TypeEvenement> findAll() {
        List<TypeEvenement> typeEvenements = new ArrayList<>();
        String sql = "SELECT * FROM type_evenement ORDER BY libelle";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                typeEvenements.add(new TypeEvenement(
                        rs.getInt("id_type"),
                        rs.getString("libelle")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return typeEvenements;
    }



    // Delete - Delete TypeEvenement by ID
    public boolean delete(int idType) {
        String sql = "DELETE FROM type_evenement WHERE id_type = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idType);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete - Delete TypeEvenement object
    public boolean delete(TypeEvenement typeEvenement) {
        return delete(typeEvenement.getIdType());
    }

    // Utility method - Check if TypeEvenement exists by ID
    public boolean exists(int idType) {
        String sql = "SELECT COUNT(*) FROM type_evenement WHERE id_type = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idType);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}
