package com.yonas.chatfx;

import com.yonas.chatfx.Connection.ConnectionManagement;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;


public class LoginApplication extends Application {
    /**
     * Method that prepares the scene to be displayed initially by the program
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        stage.setResizable(false);
        try {
            // Load the FXML file
            FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("login-view.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            // Get the controller from the FXML
            LoginController loginController = fxmlLoader.getController();
            // Configure the stage
            stage.setTitle("Login");
            stage.setScene(scene);
            // Configure the application close event
            stage.setOnCloseRequest((ae) -> handleClose(stage));
            // Show the stage
            stage.show();
        } catch (IOException e) {
            System.out.println("Error loading login-view.fxml: " + e.getMessage());
        }

    }
    private void handleClose(Stage stage) {
        try {
            ConnectionManagement.closeConnection();
        } catch (SQLException e) {
            System.out.println("Error closing the connection: " + e.getMessage());
        }
        stage.close();
        System.exit(0);
    }
    /**
     * Obtaining the database connection and running the program
     * @param args
     */
    public static void main(String[] args) {
        try {
            ConnectionManagement.getConnection();
        } catch (SQLException e) {
            System.out.println("Error attempting to connect");
        }catch (Exception e){
            System.out.println("Something went wrong: " + e.getMessage());
        }finally {
            launch();
        }
    }
}