
package com.stockmanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.stockmanager.database.Database;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Database.initialize();

        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/fxml/MainView.fxml")
        );

        Scene scene = new Scene(loader.load(), 1100, 700);

        primaryStage.setTitle("StockManager");
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
