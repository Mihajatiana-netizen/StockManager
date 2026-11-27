
package com.stockmanager.repository;

import com.stockmanager.database.Database;
import com.stockmanager.model.Article;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArticleRepository {

    // Ajouter un nouvel article
    public void save(Article article) throws SQLException {

        String sql = """
                INSERT INTO articles (
                    reference, name, description,
                    purchase_price, selling_price,
                    minimum_stock, category_id
                )
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, article.getReference());
            statement.setString(2, article.getName());
            statement.setString(3, article.getDescription());
            statement.setDouble(4, article.getPurchasePrice());
            statement.setDouble(5, article.getSellingPrice());
            statement.setInt(6, article.getMinimumStock());

            if (article.getCategoryId() != null) {
                statement.setInt(7, article.getCategoryId());
            } else {
                statement.setNull(7, Types.INTEGER);
            }

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    article.setId(keys.getInt(1));
                }
            }
        }
    }

    // Récupérer tous les articles
    
public List<Article> findAll() throws SQLException {

    List<Article> articles = new ArrayList<>();

    String sql = """
            SELECT a.*, c.name AS categoryName
            FROM articles a
            LEFT JOIN categories c
                ON a.category_id = c.id
            ORDER BY a.name
            """;

    try (Connection connection = Database.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql);
         ResultSet result = statement.executeQuery()) {

        while (result.next()) {
            articles.add(mapResult(result));
        }
    }

    return articles;
}


    // Rechercher un article par son ID
    public Article findById(int id) throws SQLException {

        String sql = "SELECT * FROM articles WHERE id = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return mapResult(result);
                }
            }
        }

        return null;
    }

    // Modifier les informations d'un article
    public boolean update(Article article) throws SQLException {

        String sql = """
                UPDATE articles
                SET reference = ?,
                    name = ?,
                    description = ?,
                    purchase_price = ?,
                    selling_price = ?,
                    minimum_stock = ?,
                    category_id = ?
                WHERE id = ?
                """;

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, article.getReference());
            statement.setString(2, article.getName());
            statement.setString(3, article.getDescription());
            statement.setDouble(4, article.getPurchasePrice());
            statement.setDouble(5, article.getSellingPrice());
            statement.setInt(6, article.getMinimumStock());

            if (article.getCategoryId() != null) {
                statement.setInt(7, article.getCategoryId());
            } else {
                statement.setNull(7, Types.INTEGER);
            }

            statement.setInt(8, article.getId());

            return statement.executeUpdate() > 0;
        }
    }

    // Supprimer un article
    public boolean delete(int id) throws SQLException {

        String sql = "DELETE FROM articles WHERE id = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;
        }
    }

    // Transformer un résultat SQL en objet Article
    
private Article mapResult(ResultSet result) throws SQLException {

    int categoryValue = result.getInt("category_id");

    Integer categoryId = result.wasNull()
            ? null
            : categoryValue;

    Article article = new Article(
            result.getInt("id"),
            result.getString("reference"),
            result.getString("name"),
            result.getString("description"),
            result.getDouble("purchase_price"),
            result.getDouble("selling_price"),
            result.getInt("quantity"),
            result.getInt("minimum_stock"),
            categoryId,
            result.getString("created_at")
    );

    article.setCategoryName(result.getString("categoryName"));

    return article;
}

}
