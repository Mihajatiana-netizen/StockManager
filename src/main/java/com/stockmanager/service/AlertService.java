
package com.stockmanager.service;

import com.stockmanager.model.Article;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class AlertService {

    private final ArticleService articleService;

    public AlertService() {
        this.articleService = new ArticleService();
    }

    public List<Article> getOutOfStockArticles() throws SQLException {
        return articleService.getAllArticles()
                .stream()
                .filter(article -> article.getQuantity() == 0)
                .collect(Collectors.toList());
    }

    public List<Article> getLowStockArticles() throws SQLException {
        return articleService.getAllArticles()
                .stream()
                .filter(article ->
                        article.getQuantity() > 0
                        && article.getQuantity() <= article.getMinimumStock())
                .collect(Collectors.toList());
    }

    public int getOutOfStockCount() throws SQLException {
        return getOutOfStockArticles().size();
    }

    public int getLowStockCount() throws SQLException {
        return getLowStockArticles().size();
    }
}
