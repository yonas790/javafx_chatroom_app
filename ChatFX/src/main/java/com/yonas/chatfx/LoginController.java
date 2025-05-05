package com.yonas.chatfx;

import com.yonas.chatfx.Connection.ConnectionManagement;
import com.yonas.chatfx.Services.Interfaces.ILoginService;
import com.yonas.chatfx.Singletons.LoginServiceSingleton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import com.yonas.chatfx.Entities.User;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    // JavaFX variables
    @FXML
    private TextField txtUser;
    @FXML
    private TextField txtPassword;
    @FXML
    private Label lblUserInfo;
    private final ILoginService loginService;
    private User user;

    public LoginController() {
        this.loginService = LoginServiceSingleton.getLoginService();
    }


    /**
     * Method to navigate to the next page (chats) when the btnLogin is clicked, passing the user
     * @param event
     * @throws IOException
     */
    @FXML
    protected void onBtnLoginClick(ActionEvent event) throws IOException {
        // If the text fields are empty, do nothing
        if (!txtUser.getText().equals("") && !txtPassword.getText().equals("")){
            // Create a user object with the data from the JavaFX text fields
            user = new User(txtUser.getText(), txtPassword.getText());
            try {
                user = loginService.getUserId(user);
                // Check if the credentials are correct
                if (user.getIdUser() != -1  || user.getIdUser() == 0){
                    switchToScene(event); // Call the method to switch scenes
                } else {
                    lblUserInfo.setText("Incorrect username or password");
                }

            } catch (SQLException e) {
                lblUserInfo.setText("Error retrieving verification");
            }

        }
    }

    /**
     * Method to register a user in the database when btnSignUp is clicked
     */
    @FXML
    protected void onBtnSignUpClick(){
        // Check that no fields are left empty
        if (!txtUser.getText().equals("") && !txtPassword.getText().equals("")){
            user = new User(txtUser.getText(), txtPassword.getText());
            try {
                user = loginService.getUserId(user);
                if (user.getIdUser() == -1  || user.getIdUser() == 0){
                    // Insert the user
                    try {
                        loginService.create(user);
                        lblUserInfo.setText("Registration completed successfully");
                    } catch (SQLException e) {
                        lblUserInfo.setText("User already registered");
                    }catch (Exception e){
                        System.out.println("Everything seems fine");
                    }
                } else {
                    lblUserInfo.setText("User already registered");
                }
            } catch (SQLException e) {
                lblUserInfo.setText("Error retrieving verification");
            }
        }
    }

    /**
     * Method to switch scenes
     * @param event
     * @throws IOException
     */
    protected void switchToScene(ActionEvent event) throws IOException {
        // Prepare the loading of the new scene
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("chat-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        // Instantiate ChatController to assign the logged-in user to the new scene
        ChatController chatController = fxmlLoader.getController();
        chatController.setUser(user);
        // Get the user's contact list
        chatController.getContactList();
        // Load the scene
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setTitle("Chats");
        stage.setScene(scene);
        stage.setOnCloseRequest((ae) -> handleClose(stage));
        stage.show();
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

}
