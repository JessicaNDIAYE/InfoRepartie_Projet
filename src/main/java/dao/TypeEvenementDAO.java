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

    // Create - Insert a new TypeEvenement
    public boolean create(TypeEvenement typeEvenement) {
        String sql = "INSERT INTO type_evenement (libelle) VALUES (?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, typeEvenement.getLibelle());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        typeEvenement.setIdType(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            // Mieux vaut logger ici ou ré-émmettre l'exception dans une vraie app
            e.printStackTrace();
        }
        return false;
    }

    // Read - Get TypeEvenement by ID
    public TypeEvenement findById(int idType) {
        String sql = "SELECT * FROM type_evenement WHERE id_type = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idType);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new TypeEvenement(
                            rs.getInt("id_type"),
                            rs.getString("libelle")
                    );

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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

    // Read - Find by libelle (case insensitive, partial match)
    public List<TypeEvenement> findByLibelle(String libelle) {
        List<TypeEvenement> typeEvenements = new ArrayList<>();
        String sql = "SELECT * FROM type_evenement WHERE LOWER(libelle) LIKE LOWER(?) ORDER BY libelle";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + libelle + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    typeEvenements.add(new TypeEvenement(
                            rs.getInt("id_type"),
                            rs.getString("libelle")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return typeEvenements;
    }

    // Update - Update an existing TypeEvenement
    public boolean update(TypeEvenement typeEvenement) {
        String sql = "UPDATE type_evenement SET libelle = ? WHERE id_type = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, typeEvenement.getLibelle());
            stmt.setInt(2, typeEvenement.getIdType());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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

    // Utility method - Count total records
    public int count() {
        String sql = "SELECT COUNT(*) FROM type_evenement";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
