
package com.stockmanager.repository;

import com.stockmanager.database.Database;
import com.stockmanager.model.Movement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovementRepository {

    // Enregistrer un mouvement et actualiser le stock
    public void save(Movement movement) throws SQLException {

        if (movement.getQuantity() <= 0) {
            throw new IllegalArgumentException(
                    "La quantité doit être supérieure à zéro."
            );
        }

        if (!"ENTRY".equals(movement.getType())
                && !"EXIT".equals(movement.getType())) {
            throw new IllegalArgumentException(
                    "Type de mouvement invalide."
            );
        }

        String updateSql;

        if ("ENTRY".equals(movement.getType())) {
            updateSql = """
                    UPDATE articles
                    SET quantity = quantity + ?
                    WHERE id = ?
                    """;
        } else {
            updateSql = """
                    UPDATE articles
                    SET quantity = quantity - ?
                    WHERE id = ? AND quantity >= ?
                    """;
        }

        String insertSql = """
                INSERT INTO movements (
                    article_id, type, quantity, description
                )
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = Database.getConnection()) {

            connection.setAutoCommit(false);

            try {
                // 1. Mettre à jour la quantité de l'article
                try (PreparedStatement update =
                             connection.prepareStatement(updateSql)) {

                    update.setInt(1, movement.getQuantity());
                    update.setInt(2, movement.getArticleId());

                    if ("EXIT".equals(movement.getType())) {
                        update.setInt(3, movement.getQuantity());
                    }

                    int rows = update.executeUpdate();

                    if (rows == 0) {
                        throw new SQLException(
                                "Article introuvable ou stock insuffisant."
                        );
                    }
                }

                // 2. Enregistrer le mouvement
                try (PreparedStatement insert =
                             connection.prepareStatement(
                                     insertSql,
                                     Statement.RETURN_GENERATED_KEYS)) {

                    insert.setInt(1, movement.getArticleId());
                    insert.setString(2, movement.getType());
                    insert.setInt(3, movement.getQuantity());
                    insert.setString(4, movement.getDescription());

                    insert.executeUpdate();

                    try (ResultSet keys = insert.getGeneratedKeys()) {
                        if (keys.next()) {
                            movement.setId(keys.getInt(1));
                        }
                    }
                }

                // 3. Valider les deux opérations
                connection.commit();

            } catch (SQLException | RuntimeException e) {

                // Annuler les opérations en cas d'erreur
                connection.rollback();

                throw e;
            }
        }
    }

    // Récupérer tous les mouvements
    public List<Movement> findAll() throws SQLException {

        List<Movement> movements = new ArrayList<>();

        String sql = """
        SELECT m.*, a.name AS articleName
        FROM movements m
        JOIN articles a ON m.article_id = a.id
        ORDER BY m.movement_date DESC
        """;

        try (Connection connection = Database.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                Movement movement = new Movement(
                        result.getInt("id"),
                        result.getInt("article_id"),
                        result.getString("type"),
                        result.getInt("quantity"),
                        result.getString("movement_date"),
                        result.getString("description")
                );
                movement.setArticleName(result.getString("articleName"));
                movements.add(movement);
            }
        }

        return movements;
    }

public List<Movement> findByFilters(Integer articleId, String type)
        throws SQLException {

    List<Movement> movements = new ArrayList<>();

    StringBuilder sql = new StringBuilder("""
        SELECT m.*, a.name AS articleName
        FROM movements m
        JOIN articles a ON m.article_id = a.id
        WHERE 1 = 1
        """);

    if (articleId != null) {
        sql.append(" AND m.article_id = ?");
    }

    if (type != null && !type.isBlank()) {
        sql.append(" AND m.type = ?");
    }

    sql.append(" ORDER BY m.movement_date DESC");

    try (Connection connection = Database.getConnection();
         PreparedStatement statement =
                 connection.prepareStatement(sql.toString())) {

        int index = 1;

        if (articleId != null) {
            statement.setInt(index++, articleId);
        }

        if (type != null && !type.isBlank()) {
            statement.setString(index, type);
        }

        try (ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                Movement movement = new Movement(
                        result.getInt("id"),
                        result.getInt("article_id"),
                        result.getString("type"),
                        result.getInt("quantity"),
                        result.getString("movement_date"),
                        result.getString("description")
                );

                movement.setArticleName(
                        result.getString("articleName"));

                movements.add(movement);
            }
        }
    }

    return movements;
}

}
