
package com.stockmanager.controller;

import com.stockmanager.model.Article;
import com.stockmanager.service.AlertService;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class AlertController {

    @FXML
    private Label outOfStockCountLabel;

    @FXML
    private Label lowStockCountLabel;

    @FXML
    private TableView<Article> outOfStockTable;

    @FXML
    private TableView<Article> lowStockTable;

    @FXML
    private TableColumn<Article, String> outReferenceColumn;

    @FXML
    private TableColumn<Article, String> outNameColumn;

    @FXML
    private TableColumn<Article, Integer> outQuantityColumn;

    @FXML
    private TableColumn<Article, Integer> outMinimumColumn;

    @FXML
    private TableColumn<Article, String> lowReferenceColumn;

    @FXML
    private TableColumn<Article, String> lowNameColumn;

    @FXML
    private TableColumn<Article, Integer> lowQuantityColumn;

    @FXML
    private TableColumn<Article, Integer> lowMinimumColumn;

    private final AlertService alertService = new AlertService();

    @FXML
    public void initialize() {
        configureTables();
        loadAlerts();
    }

    private void configureTables() {

        outReferenceColumn.setCellValueFactory(
                new PropertyValueFactory<>("reference"));

        outNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name"));

        outQuantityColumn.setCellValueFactory(
                new PropertyValueFactory<>("quantity"));

        outMinimumColumn.setCellValueFactory(
                new PropertyValueFactory<>("minimumStock"));

        lowReferenceColumn.setCellValueFactory(
                new PropertyValueFactory<>("reference"));

        lowNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name"));

        lowQuantityColumn.setCellValueFactory(
                new PropertyValueFactory<>("quantity"));

        lowMinimumColumn.setCellValueFactory(
                new PropertyValueFactory<>("minimumStock"));
    }

    private void loadAlerts() {
        try {
            var outOfStock = alertService.getOutOfStockArticles();
            var lowStock = alertService.getLowStockArticles();

            outOfStockTable.setItems(
                    FXCollections.observableArrayList(outOfStock));

            lowStockTable.setItems(
                    FXCollections.observableArrayList(lowStock));

            outOfStockCountLabel.setText(
                    "Articles en rupture : " + outOfStock.size());

            lowStockCountLabel.setText(
                    "Stocks faibles : " + lowStock.size());

        } catch (SQLException e) {
            showError("Impossible de charger les alertes : " + e.getMessage());
        }
    }

    @FXML
    private void handleRefresh() {
        loadAlerts();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Chargement des alertes impossible");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
