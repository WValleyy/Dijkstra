package com.example.dijkstra;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class mainAppGUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
        Controller controller = new Controller();
        loader.setController(controller);
        Parent root = loader.load();

        // Set up the primary stage
        primaryStage.setTitle("Dijkstra");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setResizable(false);

        // Get the controller


        // Show the stage
        primaryStage.show();

        // Call the generatePress method for testing
        //controller.generatePress();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
