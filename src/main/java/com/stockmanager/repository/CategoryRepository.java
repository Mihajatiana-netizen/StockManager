package com.stockmanager.repository;

import com.stockmanager.database.Database;
import com.stockmanager.model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {

    // CREATE : enregistrer une catégorie
    public void save(Category category) throws SQLException {

        String sql = """
                INSERT INTO categories (name, description)
                VALUES (?, ?)
                """;

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    category.setId(keys.getInt(1));
                }
            }
        }
    }

    // READ : récupérer toutes les catégories
    public List<Category> findAll() throws SQLException {

        List<Category> categories = new ArrayList<>();

        String sql = """
                SELECT id, name, description
                FROM categories
                ORDER BY name
                """;

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                Category category = new Category(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("description")
                );

                categories.add(category);
            }
        }

        return categories;
    }

    // READ : récupérer une catégorie par son ID
    public Category findById(int id) throws SQLException {

        String sql = """
                SELECT id, name, description
                FROM categories
                WHERE id = ?
                """;

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {
                    return new Category(
                            result.getInt("id"),
                            result.getString("name"),
                            result.getString("description")
                    );
                }
            }
        }

        return null;
    }

    // UPDATE : modifier une catégorie
    public boolean update(Category category) throws SQLException {

        String sql = """
                UPDATE categories
                SET name = ?, description = ?
                WHERE id = ?
                """;

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.setInt(3, category.getId());

            return statement.executeUpdate() > 0;
        }
    }

    // DELETE : supprimer une catégorie
    public boolean delete(int id) throws SQLException {

        String sql = """
                DELETE FROM categories
                WHERE id = ?
                """;

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;
        }
    }
}
