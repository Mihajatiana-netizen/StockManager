
package com.stockmanager.service;

import com.stockmanager.model.Category;
import com.stockmanager.repository.CategoryRepository;

import java.sql.SQLException;
import java.util.List;

public class CategoryService {

    private final CategoryRepository categoryRepository;

    // Injection du Repository
    public CategoryService() {
        this.categoryRepository = new CategoryRepository();
    }

    // CREATE : ajouter une catégorie
    public void addCategory(Category category) throws SQLException {

        validateCategory(category);

        category.setName(category.getName().trim());
        category.setDescription(normalizeDescription(
                category.getDescription()
        ));

        categoryRepository.save(category);
    }

    // READ : récupérer toutes les catégories
    public List<Category> getAllCategories() throws SQLException {
        return categoryRepository.findAll();
    }

    // READ : récupérer une catégorie par ID
    public Category getCategoryById(int id) throws SQLException {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Identifiant de catégorie invalide."
            );
        }

        return categoryRepository.findById(id);
    }

    // UPDATE : modifier une catégorie
    public void updateCategory(Category category) throws SQLException {

        validateCategory(category);

        if (category.getId() <= 0) {
            throw new IllegalArgumentException(
                    "Identifiant de catégorie invalide."
            );
        }

        category.setName(category.getName().trim());
        category.setDescription(normalizeDescription(
                category.getDescription()
        ));

        boolean updated = categoryRepository.update(category);

        if (!updated) {
            throw new IllegalArgumentException(
                    "Catégorie introuvable."
            );
        }
    }

    // DELETE : supprimer une catégorie
    public void deleteCategory(int id) throws SQLException {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Identifiant de catégorie invalide."
            );
        }

        boolean deleted = categoryRepository.delete(id);

        if (!deleted) {
            throw new IllegalArgumentException(
                    "Catégorie introuvable."
            );
        }
    }

    // VALIDATION : vérifier les données
    private void validateCategory(Category category) {

        if (category == null) {
            throw new IllegalArgumentException(
                    "La catégorie ne peut pas être vide."
            );
        }

        if (category.getName() == null
                || category.getName().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Le nom de la catégorie est obligatoire."
            );
        }
    }

    // Normaliser la description
    private String normalizeDescription(String description) {

        if (description == null || description.isBlank()) {
            return null;
        }

        return description.trim();
    }
}
