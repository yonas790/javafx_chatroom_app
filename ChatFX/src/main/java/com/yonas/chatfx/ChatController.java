package com.yonas.chatfx;


import com.yonas.chatfx.Services.Interfaces.IChatService;
import com.yonas.chatfx.Singletons.ChatServiceSingleton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import com.yonas.chatfx.Entities.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

public class ChatController  {
    // JavaFX elements
    @FXML
    ListView<Contact> contactList;
    @FXML
    ListView<Message> messageList;
    @FXML
    Button btnAddContact;
    @FXML
    Button btnSend;
    @FXML
    CheckBox btnBlocked;
    @FXML
    TextField textField;
    @FXML
    Label pendingMessages;
    @FXML
    Button btnLogout;
    @FXML
    Label lblContactName;
    @FXML
    Label lblUserId;
    @FXML
    Label lblError;
    // Class variables
    private User user;
    private Contact selectedContact;
    private int contactCount = -1;
    private ObservableList<Contact> auxContacts = FXCollections.observableArrayList();
    private int messageCount = -1;
    private int unreadMessages = 0;
    private Timer timerContactsPanel;
    private Timer timerMessages;
    private final IChatService chatService;

    public ChatController() {
        this.chatService = ChatServiceSingleton.getChatService();
    }

    /**
     * Method to get the user's contact list
     */
    public void getContactList() {
        // Update the view to display the user's ID
        lblUserId.setText("ID: " + user.getIdUser());
        try {
            // Create a timer to update the contact list
            timerContactsPanel = new Timer();
            timerContactsPanel.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (user != null) {
                        // Update the variable with the number of unread messages from the database
                        try {
                            unreadMessages = chatService.getUnreadMessageCount(user.getIdUser());

                        } catch (SQLException e) {
                            System.out.println("Error retrieving pending messages for the user");
                        } catch (Exception e) {
                            System.out.println("Something went wrong: " + e.getMessage());

                        }
                        // Update the view with the value of unreadMessages
                        Platform.runLater(() -> pendingMessages.setText(String.valueOf(unreadMessages)));

                        // Update the auxiliary variable with the user's contacts from the database
                        try {
                            auxContacts = chatService.getUserContacts(user.getIdUser());
                        } catch (SQLException e) {
                            System.out.println("Error retrieving user contacts");
                        } catch (Exception e) {
                            System.out.println("Something went wrong: " + e.getMessage());

                        }
                        // If the auxiliary contact list has fewer items than the stored contactCount, refresh the scene's contact list
                        if (auxContacts.size() != contactCount) {
                            // Update the contact list in the view
                            Platform.runLater(() -> {
                                try {
                                    contactList.setItems(chatService.getUserContacts(user.getIdUser()));
                                } catch (SQLException e) {
                                    System.out.println("Error retrieving user contacts");
                                } catch (Exception e) {
                                    System.out.println("Something went wrong: " + e.getMessage());
                                }
                                contactList.refresh();
                                // Assign the value of contactCount to the number of items in the scene's list
                                contactCount = contactList.getItems().size();
                            });
                        }
                    }
                }
            }, 0, 1000);

            // Add a listener to the contact list so that when a contact is clicked, different data is obtained
            contactList.getSelectionModel().selectedItemProperty().addListener((observableValue, oldContact, newContact) -> {
                btnBlocked.setVisible(true);
                btnSend.setVisible(true);
                textField.setVisible(true);

                // Assign the selected contact with the data of the clicked contact
                selectedContact = contactList.getSelectionModel().getSelectedItem();

                // Update contactNameLabel to display the contact's ID and username
                try {
                    lblContactName.setText(selectedContact.getIdContact() + ": " + selectedContact.getUserName());
                    // Check if the contact is blocked in the database to set the checkbox active or inactive and disable the entry if it is blocked
                    if (selectedContact.getBlocked()) {
                        btnBlocked.setSelected(true);
                        textField.setDisable(true);
                    } else {
                        btnBlocked.setSelected(false);
                        textField.setDisable(false);
                    }
                } catch (NullPointerException nullPointerException) {
                    System.out.println("Everything is working fine, but there is no contact ID");
                } catch (Exception e) {
                    System.out.println("Something went wrong: " + e.getMessage());

                }
            });

            // Create a timer to update the message list
            timerMessages = new Timer();
            timerMessages.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> updateMessages());
                }
            }, 0, 500);
        } catch (NullPointerException nullPointerException) {
            System.out.println("It works, but no contact is selected");
        } catch (IllegalStateException illegalStateException) {
            System.out.println("It works, but nothing is updated");
        } catch (Exception e) {
            System.out.println("Something went wrong: " + e.getMessage());

        }
    }


    /**
     * Method to update the messages in the view
     */
    private void updateMessages() {
            if(selectedContact != null && user != null){
                try {
                // Update the message list
                messageList.setItems(chatService.getChatMessages(user.getIdUser(), selectedContact.getIdContact()));
                messageList.refresh();
                // Update read messages
                for (Message m : messageList.getItems()) {
                    if (m.getIdUser() != user.getIdUser()) {
                        m.setReaded(true);
                    }
                }
                // Update read messages in the database
                    chatService.updateReadMessages(selectedContact.getIdContact(), user.getIdUser());
                // Assign the value of messageCount to the number of messages in the message list
                messageCount = messageList.getItems().size();
            } catch (NullPointerException nullPointerException) {
                System.out.println("It works, but no user is selected");
            } catch (IllegalStateException illegalStateException) {
                System.out.println("It works, but nothing is updated");
            } catch (SQLException e) {
                System.out.println("Error retrieving user contacts");
            } catch (Exception e) {
                    System.out.println("Something went wrong: " + e.getMessage());

            }
            }

    }

    /**
     * Method to assign the user used in the class
     *
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Method to add a contact when addContactButton is clicked
     */
    public void addContactButtonOnClick() {
        // Create and display a popup window with an entry field
        TextInputDialog tiDialog = new TextInputDialog();
        tiDialog.setTitle("Add Contact");
        tiDialog.setHeaderText("Enter the user ID of the contact");
        tiDialog.setContentText("ID: ");

        // Store the text entered on the field
        Optional<String> result = tiDialog.showAndWait();
        // If there is text and the user ID to beaded is different, add the contact
        if (result.isPresent() && user.getIdUser() != Integer.parseInt(result.get())) {
            String contactIdString = result.get();
            int contactId = Integer.parseInt(contactIdString);
            try {
                chatService.addContact(user.getIdUser(), contactId);
            } catch (SQLException e) {
                System.out.println("Error adding contact");
            } catch (Exception e) {
                    System.out.println("Something went wrong: " + e.getMessage());

            }
            try {
                chatService.createChat(user.getIdUser(), contactId);
            } catch (SQLException e) {
                System.out.println("Error creating chat");
            } catch (Exception e) {
                    System.out.println("Something went wrong: " + e.getMessage());

            }

        }
    }

    /**
     * Method to block or unblock a contact using a checkbox
     */
    public void blockedButtonOnClick() {
        boolean block = btnBlocked.isSelected();
        try {
            chatService.blockUnblockContact(user.getIdUser(), selectedContact.getIdContact(), block);
        } catch (SQLException e) {
                System.out.println("Error blocking contact");
        } catch (Exception e) {
                System.out.println("Something went wrong: " + e.getMessage());

        }
        selectedContact.setBlocked(block);
        textField.setDisable(block);
    }
    /**
     * Method to send a message
     */
    public void sendButtonOnClick() {
        if(textField.getText() != null){
            if (!textField.getText().isEmpty() && textField.getText().length() < 100) {
                lblError.setVisible(false);
                try {
                    // Insert the message in the database
                    chatService.insertMessage(user.getIdUser(), textField.getText(),selectedContact.getIdContact());
                } catch (SQLException e) {
                    System.out.println("Error inserting message");
                } catch (Exception e) {
                        System.out.println("Something went wrong: " + e.getMessage());

                }
                try {
                    // Update the message list
                    messageList.setItems(chatService.getChatMessages(user.getIdUser(), selectedContact.getIdContact()));
                } catch (SQLException e) {
                    System.out.println("Error updating message list");
                } catch (Exception e) {
                        System.out.println("Something went wrong: " + e.getMessage());

                }
                // Mark the messages as read
                for (Message m : messageList.getItems()) {
                    if (m.getIdUser() != user.getIdUser()) {
                        m.setReaded(true);
                    }
                }
                // Clear the entry
                textField.setText(null);
            } else {
                lblError.setVisible(true);
            }
        }
    }

    // Method to log out and return to the main scene
    public void logoutButtonOnClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Login");
        user = null;
        selectedContact = null;
        stage.setScene(scene);
        stage.show();
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }
}