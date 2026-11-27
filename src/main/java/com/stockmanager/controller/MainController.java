
package com.stockmanager.controller;

import com.stockmanager.model.Movement;
import com.stockmanager.service.DashboardService;
import com.stockmanager.service.MovementService;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class MainController {

    @FXML
    private BorderPane rootPane;

    @FXML
    private Label totalArticlesLabel;

    @FXML
    private Label totalCategoriesLabel;

    @FXML
    private Label lowStockLabel;

    @FXML
    private Label outOfStockLabel;

    @FXML
    private Label totalMovementsLabel;

    @FXML
    private Label stockValueLabel;

    @FXML
    private TableView<Movement> recentMovementsTable;

    @FXML
    private TableColumn<Movement, String> recentArticleColumn;

    @FXML
    private TableColumn<Movement, String> recentTypeColumn;

    @FXML
    private TableColumn<Movement, Integer> recentQuantityColumn;

    @FXML
    private TableColumn<Movement, String> recentDateColumn;

    private final DashboardService dashboardService =
            new DashboardService();

    private final MovementService movementService =
            new MovementService();

    @FXML
    public void initialize() {
        configureRecentMovementsTable();
        loadDashboard();
    }

    private void configureRecentMovementsTable() {

        recentArticleColumn.setCellValueFactory(
                new PropertyValueFactory<>("articleName"));

        recentTypeColumn.setCellValueFactory(
                new PropertyValueFactory<>("typeLabel"));

        recentQuantityColumn.setCellValueFactory(
                new PropertyValueFactory<>("quantity"));

        recentDateColumn.setCellValueFactory(
                new PropertyValueFactory<>("movementDate"));
    }

    private void loadDashboard() {

        try {
            totalArticlesLabel.setText(
                    String.valueOf(dashboardService.getTotalArticles()));

            totalCategoriesLabel.setText(
                    String.valueOf(dashboardService.getTotalCategories()));

            lowStockLabel.setText(
                    String.valueOf(dashboardService.getLowStockCount()));

            outOfStockLabel.setText(
                    String.valueOf(dashboardService.getOutOfStockCount()));

            totalMovementsLabel.setText(
                    String.valueOf(movementService.getAllMovements().size()));

            double stockValue = dashboardService.getTotalStockValue();

            NumberFormat formatter =
                    NumberFormat.getNumberInstance(Locale.FRANCE);

            stockValueLabel.setText(
                    formatter.format(stockValue) + " Ar");

            List<Movement> movements =
                    movementService.getAllMovements();

            List<Movement> recentMovements =
                    movements.stream()
                            .limit(5)
                            .toList();

            recentMovementsTable.setItems(
                    FXCollections.observableArrayList(recentMovements));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   
@FXML
private void showDashboard() {
    loadDashboard();
    rootPane.setCenter(rootPane.getCenter());
}


    @FXML
    private void showCategories() {
        loadView("/fxml/CategoryView.fxml");
    }

    @FXML
    private void showArticles() {
        loadView("/fxml/ArticleView.fxml");
    }

    @FXML
    private void showMovements() {
        loadView("/fxml/MovementView.fxml");
    }

    @FXML
    private void showAlerts() {
        loadView("/fxml/AlertView.fxml");
    }

    @FXML
    private void showHistory() {
        loadView("/fxml/HistoryView.fxml");
    }

    private void loadView(String path) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(path)
            );

            Parent view = loader.load();
            rootPane.setCenter(view);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
