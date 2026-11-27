
package com.stockmanager.controller;

import com.stockmanager.model.Article;
import com.stockmanager.model.Movement;
import com.stockmanager.service.ArticleService;
import com.stockmanager.service.MovementService;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HistoryController {

    @FXML private ComboBox<Article> articleComboBox;
    @FXML private ComboBox<String> typeComboBox;

    @FXML private TableView<Movement> historyTable;

    @FXML private TableColumn<Movement, Integer> idColumn;
    @FXML private TableColumn<Movement, String> articleColumn;
    @FXML private TableColumn<Movement, String> typeColumn;
    @FXML private TableColumn<Movement, Integer> quantityColumn;
    @FXML private TableColumn<Movement, String> dateColumn;
    @FXML private TableColumn<Movement, String> descriptionColumn;

    @FXML private Label resultCountLabel;

    private final ArticleService articleService = new ArticleService();
    private final MovementService movementService = new MovementService();

    @FXML
    public void initialize() {
        configureTable();
        loadArticles();

        typeComboBox.setItems(FXCollections.observableArrayList(
                "Tous", "Entrée", "Sortie"
        ));

        typeComboBox.setValue("Tous");

        handleSearch();
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

    private void loadArticles() {
        try {
            List<Article> articles = new ArrayList<>();
            articles.add(null);
            articles.addAll(articleService.getAllArticles());

            articleComboBox.setItems(
                    FXCollections.observableArrayList(articles));

            articleComboBox.setValue(null);

        } catch (SQLException e) {
            showError("Erreur de chargement des articles : " + e.getMessage());
        }
    }

    @FXML
    private void handleSearch() {
        try {
            Article article = articleComboBox.getValue();
            String selectedType = typeComboBox.getValue();

            Integer articleId = article == null
                    ? null
                    : article.getId();

            String type = null;

            if ("Entrée".equals(selectedType)) {
                type = "ENTRY";
            } else if ("Sortie".equals(selectedType)) {
                type = "EXIT";
            }

            List<Movement> movements =
                    movementService.getMovementsByFilters(articleId, type);

            historyTable.setItems(
                    FXCollections.observableArrayList(movements));

            resultCountLabel.setText(
                    "Nombre de mouvements : " + movements.size());

        } catch (SQLException e) {
            showError("Erreur de recherche : " + e.getMessage());
        }
    }

    @FXML
    private void handleReset() {
        articleComboBox.setValue(null);
        typeComboBox.setValue("Tous");
        handleSearch();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Opération impossible");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
