package com.yonas.chat.javafx_chatroom_app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Simple UI for testing
        StackPane root = new StackPane();
        root.getChildren().add(new javafx.scene.control.Label("Hello, JavaFX!"));

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("JavaFX Chatroom App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
