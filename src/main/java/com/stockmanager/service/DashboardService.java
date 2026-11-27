
package com.stockmanager.service;

import com.stockmanager.database.Database;
import com.stockmanager.model.DashboardStats;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardService {

    public DashboardStats getStatistics() throws SQLException {

        String sql = """
                SELECT
                    (SELECT COUNT(*) FROM articles) AS total_articles,

                    (SELECT COUNT(*) FROM categories) AS total_categories,

                    (SELECT COUNT(*)
                     FROM articles
                     WHERE quantity > 0
                     AND quantity <= minimum_stock) AS low_stock_count,

                    (SELECT COUNT(*)
                     FROM articles
                     WHERE quantity = 0) AS out_of_stock_count,

                    (SELECT COUNT(*) FROM movements) AS total_movements,

                    (SELECT COALESCE(SUM(quantity * purchase_price), 0)
                     FROM articles) AS total_stock_value
                """;

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            if (result.next()) {
                return new DashboardStats(
                        result.getInt("total_articles"),
                        result.getInt("total_categories"),
                        result.getInt("low_stock_count"),
                        result.getInt("out_of_stock_count"),
                        result.getInt("total_movements"),
                        result.getDouble("total_stock_value")
                );
            }

            throw new SQLException(
                    "Impossible de récupérer les statistiques du tableau de bord."
            );
        }
    }
}
