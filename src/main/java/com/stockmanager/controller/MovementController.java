
package com.stockmanager.controller;

import com.stockmanager.model.Article;
import com.stockmanager.model.Movement;
import com.stockmanager.service.ArticleService;
import com.stockmanager.service.MovementService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class MovementController {

    @FXML
    private ComboBox<Article> articleComboBox;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private TextField quantityField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private TableView<Movement> movementTable;

    @FXML
    private TableColumn<Movement, Integer> idColumn;

    @FXML
    private TableColumn<Movement, String> articleColumn;

    @FXML
    private TableColumn<Movement, String> typeColumn;

    @FXML
    private TableColumn<Movement, Integer> quantityColumn;

    @FXML
    private TableColumn<Movement, String> dateColumn;

    @FXML
    private TableColumn<Movement, String> descriptionColumn;

    private final ArticleService articleService = new ArticleService();
    private final MovementService movementService = new MovementService();

    @FXML
    public void initialize() {
        configureTable();
        configureTypes();
        loadArticles();
        loadMovements();
    }

    private void configureTable() {
        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("id"));

        articleColumn.setCellValueFactory(
                new PropertyValueFactory<>("articleName"));

        typeColumn.setCellValueFactory(
                new PropertyValueFactory<>("typeLabel"));

        quantityColumn.setCellValueFactory(
                new PropertyValueFactory<>("quantity"));

        dateColumn.setCellValueFactory(
                new PropertyValueFactory<>("movementDate"));

        descriptionColumn.setCellValueFactory(
                new PropertyValueFactory<>("description"));
    }

    private void configureTypes() {
        typeComboBox.setItems(
                FXCollections.observableArrayList("Entrée", "Sortie"));
    }

    private void loadArticles() {
        try {
            articleComboBox.setItems(
                    FXCollections.observableArrayList(
                            articleService.getAllArticles()));
        } catch (SQLException e) {
            showError("Impossible de charger les articles : " + e.getMessage());
        }
    }

    private void loadMovements() {
        try {
            movementTable.setItems(
                    FXCollections.observableArrayList(
                            movementService.getAllMovements()));
        } catch (SQLException e) {
            showError("Impossible de charger les mouvements : " + e.getMessage());
        }
    }

    @FXML
    private void handleSave() {
        try {
            Article article = articleComboBox.getValue();
            String selectedType = typeComboBox.getValue();
            String quantityText = quantityField.getText().trim();

            if (article == null) {
                showError("Veuillez sélectionner un article.");
                return;
            }

            if (selectedType == null) {
                showError("Veuillez sélectionner un type de mouvement.");
                return;
            }

            int quantity = Integer.parseInt(quantityText);

            String type = selectedType.equals("Entrée")
                    ? "ENTRY"
                    : "EXIT";

            Movement movement = new Movement(
                    article.getId(),
                    type,
                    quantity,
                    descriptionField.getText()
            );

            movementService.addMovement(movement);

            showInfo("Mouvement enregistré avec succès.");

            handleClear();
            loadMovements();
            loadArticles();

        } catch (NumberFormatException e) {
            showError("Veuillez saisir une quantité entière valide.");
        } catch (IllegalArgumentException | SQLException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void handleClear() {
        articleComboBox.getSelectionModel().clearSelection();
        typeComboBox.getSelectionModel().clearSelection();
        quantityField.clear();
        descriptionField.clear();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Opération impossible");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
