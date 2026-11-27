
package com.stockmanager.service;

import com.stockmanager.model.Article;
import com.stockmanager.repository.ArticleRepository;

import java.sql.SQLException;
import java.util.List;

public class ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleService() {
        this.articleRepository = new ArticleRepository();
    }

    // Ajouter un article
    public void addArticle(Article article) throws SQLException {

        validateArticle(article);

        article.setReference(article.getReference().trim());
        article.setName(article.getName().trim());

        if (article.getDescription() != null) {
            article.setDescription(
                    article.getDescription().trim()
            );

            if (article.getDescription().isEmpty()) {
                article.setDescription(null);
            }
        }

        articleRepository.save(article);
    }

    // Récupérer tous les articles
    public List<Article> getAllArticles() throws SQLException {
        return articleRepository.findAll();
    }

    // Rechercher un article par ID
    public Article getArticleById(int id) throws SQLException {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Identifiant d'article invalide."
            );
        }

        return articleRepository.findById(id);
    }

    // Modifier un article
    public void updateArticle(Article article) throws SQLException {

        validateArticle(article);

        if (article.getId() <= 0) {
            throw new IllegalArgumentException(
                    "Identifiant d'article invalide."
            );
        }

        article.setReference(article.getReference().trim());
        article.setName(article.getName().trim());

        if (article.getDescription() != null) {
            article.setDescription(
                    article.getDescription().trim()
            );

            if (article.getDescription().isEmpty()) {
                article.setDescription(null);
            }
        }

        boolean updated = articleRepository.update(article);

        if (!updated) {
            throw new IllegalArgumentException(
                    "Article introuvable."
            );
        }
    }

    // Supprimer un article
    public void deleteArticle(int id) throws SQLException {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Identifiant d'article invalide."
            );
        }

        boolean deleted = articleRepository.delete(id);

        if (!deleted) {
            throw new IllegalArgumentException(
                    "Article introuvable."
            );
        }
    }

    // Vérifier les données d'un article
    private void validateArticle(Article article) {

        if (article == null) {
            throw new IllegalArgumentException(
                    "L'article ne peut pas être vide."
            );
        }

        if (article.getReference() == null
                || article.getReference().isBlank()) {
            throw new IllegalArgumentException(
                    "La référence est obligatoire."
            );
        }

        if (article.getName() == null
                || article.getName().isBlank()) {
            throw new IllegalArgumentException(
                    "Le nom de l'article est obligatoire."
            );
        }

        if (article.getPurchasePrice() < 0
                || article.getSellingPrice() < 0) {
            throw new IllegalArgumentException(
                    "Les prix ne peuvent pas être négatifs."
            );
        }

        if (article.getMinimumStock() < 0) {
            throw new IllegalArgumentException(
                    "Le seuil minimum ne peut pas être négatif."
            );
        }

        if (article.getCategoryId() != null
                && article.getCategoryId() <= 0) {
            throw new IllegalArgumentException(
                    "Catégorie invalide."
            );
        }
    }
}
