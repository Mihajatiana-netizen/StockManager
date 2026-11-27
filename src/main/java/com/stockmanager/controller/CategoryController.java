
package com.stockmanager.controller;

import com.stockmanager.model.Category;
import com.stockmanager.service.CategoryService;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class CategoryController {

    @FXML
    private TextField nameField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private TableView<Category> categoryTable;

    @FXML
    private TableColumn<Category, Integer> idColumn;

    @FXML
    private TableColumn<Category, String> nameColumn;

    @FXML
    private TableColumn<Category, String> descriptionColumn;

    private final CategoryService categoryService =
            new CategoryService();

    @FXML
    public void initialize() {

        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("id"));

        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name"));

        descriptionColumn.setCellValueFactory(
                new PropertyValueFactory<>("description"));

        loadCategories();
    }

    // Charger les catégories depuis SQLite
    private void loadCategories() {

        try {
            categoryTable.setItems(
                    FXCollections.observableArrayList(
                            categoryService.getAllCategories()
                    )
            );

        } catch (SQLException e) {
            showError("Erreur de chargement", e.getMessage());
        }
    }

    // Ajouter une catégorie
    @FXML
    private void handleAdd() {

        try {
            Category category = new Category(
                    nameField.getText(),
                    descriptionField.getText()
            );

            categoryService.addCategory(category);

            loadCategories();
            clearFields();

            showInformation(
                    "Succès",
                    "Catégorie ajoutée avec succès."
            );

        } catch (IllegalArgumentException | SQLException e) {
            showError("Erreur", e.getMessage());
        }
    }

    // Modifier une catégorie
    @FXML
    private void handleUpdate() {

        Category selected = categoryTable.getSelectionModel()
                .getSelectedItem();

        if (selected == null) {
            showError("Attention",
                    "Sélectionnez une catégorie à modifier.");
            return;
        }

        try {
            selected.setName(nameField.getText());
            selected.setDescription(descriptionField.getText());

            categoryService.updateCategory(selected);

            loadCategories();
            clearFields();

            showInformation(
                    "Succès",
                    "Catégorie modifiée avec succès."
            );

        } catch (IllegalArgumentException | SQLException e) {
            showError("Erreur", e.getMessage());
        }
    }

    // Supprimer une catégorie
    @FXML
    private void handleDelete() {

        Category selected = categoryTable.getSelectionModel()
                .getSelectedItem();

        if (selected == null) {
            showError("Attention",
                    "Sélectionnez une catégorie à supprimer.");
            return;
        }

        Alert confirmation = new Alert(
                Alert.AlertType.CONFIRMATION
        );

        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Supprimer cette catégorie ?");
        confirmation.setContentText(selected.getName());

        confirmation.showAndWait().ifPresent(response -> {

            if (response == ButtonType.OK) {
                try {
                    categoryService.deleteCategory(
                            selected.getId()
                    );

                    loadCategories();
                    clearFields();

                } catch (IllegalArgumentException | SQLException e) {
                    showError("Erreur", e.getMessage());
                }
            }
        });
    }

    // Sélectionner une catégorie dans le tableau
    @FXML
    private void handleSelect() {
        Category selected = categoryTable.getSelectionModel()
                .getSelectedItem();

        if (selected != null) {
            nameField.setText(selected.getName());
            descriptionField.setText(selected.getDescription());
        }
    }

    // Vider les champs
    @FXML
    private void handleClear() {
        clearFields();
        categoryTable.getSelectionModel().clearSelection();
    }

    private void clearFields() {
        nameField.clear();
        descriptionField.clear();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInformation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
