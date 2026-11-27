
package com.stockmanager.controller;

import com.stockmanager.model.Article;
import com.stockmanager.model.Category;
import com.stockmanager.service.ArticleService;
import com.stockmanager.service.CategoryService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class ArticleController {

    @FXML private TextField referenceField;
    @FXML private TextField nameField;
    @FXML private TextArea descriptionField;
    @FXML private TextField purchasePriceField;
    @FXML private TextField sellingPriceField;
    @FXML private TextField minimumStockField;

    @FXML private ComboBox<Category> categoryComboBox;

    @FXML private TableView<Article> articleTable;
    @FXML private TableColumn<Article, Integer> idColumn;
    @FXML private TableColumn<Article, String> referenceColumn;
    @FXML private TableColumn<Article, String> nameColumn;
    @FXML private TableColumn<Article, Double> purchasePriceColumn;
    @FXML private TableColumn<Article, Double> sellingPriceColumn;
    @FXML private TableColumn<Article, Integer> quantityColumn;
    @FXML private TableColumn<Article, Integer> minimumStockColumn;
    @FXML private TableColumn<Article, String> categoryColumn;

    private final ArticleService articleService = new ArticleService();
    private final CategoryService categoryService = new CategoryService();

    private Article selectedArticle;

    @FXML
    public void initialize() {

        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("id"));

        referenceColumn.setCellValueFactory(
                new PropertyValueFactory<>("reference"));

        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name"));

        purchasePriceColumn.setCellValueFactory(
                new PropertyValueFactory<>("purchasePrice"));

        sellingPriceColumn.setCellValueFactory(
                new PropertyValueFactory<>("sellingPrice"));

        quantityColumn.setCellValueFactory(
                new PropertyValueFactory<>("quantity"));

        minimumStockColumn.setCellValueFactory(
                new PropertyValueFactory<>("minimumStock"));

        categoryColumn.setCellValueFactory(
                new PropertyValueFactory<>("categoryName"));

        loadCategories();
        loadArticles();

        articleTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                handleSelect();
            }
        });
    }

    // Charger les catégories
    private void loadCategories() {
        try {
            ObservableList<Category> categories =
                    FXCollections.observableArrayList(
                            categoryService.getAllCategories());

            categoryComboBox.setItems(categories);

        } catch (SQLException e) {
            showError("Erreur de chargement des catégories", e.getMessage());
        }
    }

    // Charger les articles
    private void loadArticles() {
        try {
            ObservableList<Article> articles =
                    FXCollections.observableArrayList(
                            articleService.getAllArticles());

            articleTable.setItems(articles);

        } catch (SQLException e) {
            showError("Erreur de chargement des articles", e.getMessage());
        }
    }

    // Ajouter un article
    @FXML
    private void handleAdd() {
        try {
            Article article = new Article(
                    referenceField.getText(),
                    nameField.getText(),
                    descriptionField.getText(),
                    Double.parseDouble(purchasePriceField.getText()),
                    Double.parseDouble(sellingPriceField.getText()),
                    0,
                    Integer.parseInt(minimumStockField.getText()),
                    getSelectedCategoryId()
            );

            articleService.addArticle(article);

            showInfo("Article ajouté avec succès.");
            loadArticles();
            handleClear();

        } catch (NumberFormatException e) {
            showError("Données invalides",
                    "Veuillez saisir des prix et un seuil valides.");
        } catch (IllegalArgumentException e) {
            showError("Validation", e.getMessage());
        } catch (SQLException e) {
            showError("Erreur SQL", e.getMessage());
        }
    }

    // Modifier un article
    @FXML
    private void handleUpdate() {
        if (selectedArticle == null) {
            showError("Sélection requise",
                    "Veuillez sélectionner un article.");
            return;
        }

        try {
            selectedArticle.setReference(referenceField.getText());
            selectedArticle.setName(nameField.getText());
            selectedArticle.setDescription(descriptionField.getText());
            selectedArticle.setPurchasePrice(
                    Double.parseDouble(purchasePriceField.getText()));
            selectedArticle.setSellingPrice(
                    Double.parseDouble(sellingPriceField.getText()));
            selectedArticle.setMinimumStock(
                    Integer.parseInt(minimumStockField.getText()));
            selectedArticle.setCategoryId(getSelectedCategoryId());

            articleService.updateArticle(selectedArticle);

            showInfo("Article modifié avec succès.");
            loadArticles();
            handleClear();

        } catch (NumberFormatException e) {
            showError("Données invalides",
                    "Veuillez saisir des prix et un seuil valides.");
        } catch (IllegalArgumentException e) {
            showError("Validation", e.getMessage());
        } catch (SQLException e) {
            showError("Erreur SQL", e.getMessage());
        }
    }

    // Supprimer un article
    @FXML
    private void handleDelete() {
        if (selectedArticle == null) {
            showError("Sélection requise",
                    "Veuillez sélectionner un article.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Supprimer cet article ?");
        confirmation.setContentText(selectedArticle.getName());

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    articleService.deleteArticle(selectedArticle.getId());

                    showInfo("Article supprimé avec succès.");
                    loadArticles();
                    handleClear();

                } catch (SQLException | IllegalArgumentException e) {
                    showError("Suppression impossible", e.getMessage());
                }
            }
        });
    }

    // Sélectionner un article
    private void handleSelect() {
        selectedArticle = articleTable.getSelectionModel()
                .getSelectedItem();

        if (selectedArticle == null) {
            return;
        }

        referenceField.setText(selectedArticle.getReference());
        nameField.setText(selectedArticle.getName());
        descriptionField.setText(selectedArticle.getDescription());
        purchasePriceField.setText(
                String.valueOf(selectedArticle.getPurchasePrice()));
        sellingPriceField.setText(
                String.valueOf(selectedArticle.getSellingPrice()));
        minimumStockField.setText(
                String.valueOf(selectedArticle.getMinimumStock()));

        categoryComboBox.getSelectionModel().clearSelection();

        if (selectedArticle.getCategoryId() != null) {
            categoryComboBox.getItems().stream()
                    .filter(category ->
                            category.getId() == selectedArticle.getCategoryId())
                    .findFirst()
                    .ifPresent(category ->
                            categoryComboBox.getSelectionModel()
                                    .select(category));
        }
    }

    // Récupérer l'ID de la catégorie choisie
    private Integer getSelectedCategoryId() {
        Category category = categoryComboBox.getValue();

        return category == null ? null : category.getId();
    }

    // Vider le formulaire
    @FXML
    private void handleClear() {
        referenceField.clear();
        nameField.clear();
        descriptionField.clear();
        purchasePriceField.clear();
        sellingPriceField.clear();
        minimumStockField.setText("5");

        categoryComboBox.getSelectionModel().clearSelection();
        articleTable.getSelectionModel().clearSelection();

        selectedArticle = null;
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("StockManager");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
