package com.example.laborator7.Gui;

import com.example.laborator7.Domain.FriendRequest;
import com.example.laborator7.Domain.Friendship;
import com.example.laborator7.Domain.Message;
import com.example.laborator7.Domain.User;
import com.example.laborator7.Service.MessageService;
import com.example.laborator7.Service.Service;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.List;
import java.util.stream.StreamSupport;

public class AppController implements Initializable {
    @FXML
    public VBox messageVBox;
    public ScrollPane messageScrollPane;
    @FXML
    private Text username;
    @FXML
    private TextField message;

    @FXML
    private ListView<String> friendRequestsSent;
    @FXML
    private ListView<String> friendsList;

    @FXML
    private ListView<String> friendsRequestList;

    @FXML
    private ListView<String> userList;

    @FXML
    private ListView<String> messagesFriendList;

    private final ObservableList<String> userObserver = FXCollections.observableArrayList();

    private final ObservableList<String> friendsObserver = FXCollections.observableArrayList();

    private final ObservableList<String> friendsRequestObserver = FXCollections.observableArrayList();

    private final ObservableList<String> friendsRequestSentObserver = FXCollections.observableArrayList();

    Service networkService;
    User currentuser;
    MessageService messageService;

    public void setUser(User user){
        this.currentuser=user;
    }

    User getCurrentuser(){
        return currentuser;
    }

    @FXML
    public void initialize(){
    }
    private void initModel(){

    }
    public void setNetworkService(Service networkService){
        this.networkService=networkService;
        initModel();
    }

    public void initApp(User user) {
        messagesFriendList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.currentuser = user;
        username.setText(user.getFirstName() + " " + user.getLastName());

        for (Friendship friendship : networkService.getAllFriendships()) {
            Friendship friendShip = (Friendship) friendship;

            if ((friendShip.getUser2().getId().equals(user.getId()) ||
                    friendShip.getUser1().getId().equals(user.getId())) &&
                    friendShip.getAcceptance().equals(FriendRequest.ACCEPTED.toString())) {

                User friendUser = (friendShip.getUser1().getId().equals(user.getId())) ?
                        friendShip.getUser2() : friendShip.getUser1();
                friendsObserver.add(
                        friendUser.getFirstName() + " " +
                                friendUser.getLastName() + " " +
                                friendUser.getEmail()
                );
            }
        }
        networkService.getAllFriendships().forEach(f -> {
            Friendship friendShip = (Friendship) f;
            if(friendShip.getUser2().getId().equals(user.getId()))
                friendsRequestObserver
                        .add(friendShip.getUser1().getFirstName() + " " + friendShip.getUser1().getLastName() +
                                " " + friendShip.getUser1().getEmail() + " " + friendShip.getDate() + " "
                                + friendShip.getAcceptance());
        });

        networkService.getAllUsers().forEach(u -> {
            User user1 = u;
            AtomicReference<String> additionalMessage = new AtomicReference<>("");
            if(user1.getEmail().equals(user.getEmail()))
                additionalMessage.set("YOU");

            userObserver.add(user1.getFirstName() + " " + user1.getLastName() + " " + user1.getEmail() + " " + additionalMessage);
        });

        networkService.getAllFriendships().forEach(f -> {
            Friendship friendShip = (Friendship) f;
            if(friendShip.getUser1().getId().equals(user.getId()))
                friendsRequestSentObserver
                        .add(friendShip.getUser2().getFirstName() + " " + friendShip.getUser2().getLastName() +
                                " " + friendShip.getUser2().getEmail() + " " + friendShip.getDate() + " "
                                + friendShip.getAcceptance());
        });
        messagesFriendList.setOnMouseClicked(event -> {
            if (messagesFriendList.getSelectionModel().getSelectedItem() != null) {
                String userInfo = messagesFriendList.getSelectionModel().getSelectedItem().toString();
                String email = userInfo.split(" ")[2];
                User selectedUser = networkService.getUserByEmail(email).orElse(null);
                if (selectedUser != null) {
                    // Afișează mesajele între utilizatori
                    Platform.runLater(() -> showMessages(currentuser, selectedUser));
                }
            }
        });
    }

    public void initialize(URL location, ResourceBundle resources) {
        friendsList.setItems(friendsObserver);
        friendsRequestList.setItems(friendsRequestObserver);
        friendRequestsSent.setItems(friendsRequestSentObserver);
        userList.setItems(userObserver);
        messagesFriendList.setItems(friendsObserver);
    }

    public void removeFriend() {
        if(friendsList.getSelectionModel().getSelectedItem() == null)
            return;

        String userInfo = friendsList.getSelectionModel().getSelectedItem().toString();
        String email = userInfo.split(" ")[2];
        System.out.println(email);
        networkService.deleteFriendship(email, currentuser.getEmail());

        friendsRequestSentObserver.removeAll(friendsRequestSentObserver.stream().toList());
        userObserver.removeAll(userObserver.stream().toList());
        friendsObserver.removeAll(friendsObserver.stream().toList());
        friendsRequestObserver.removeAll(friendsRequestObserver.stream().toList());
        initApp(this.currentuser);
    }

    public void acceptFriendRequest(ActionEvent actionEvent) {
        if(friendsRequestList.getSelectionModel().getSelectedItem() == null)
            return;

        String userInfo = friendsRequestList.getSelectionModel().getSelectedItem();
        String email = userInfo.split(" ")[2];
        String status = userInfo.split(" ")[4];

        if(!status.equals("PENDING"))
            return;

        networkService.acceptFriendship(email, currentuser.getEmail());

        System.out.println(email);

        friendsRequestSentObserver.removeAll(friendsRequestSentObserver.stream().toList());
        userObserver.removeAll(userObserver.stream().toList());
        friendsObserver.removeAll(friendsObserver.stream().toList());
        friendsRequestObserver.removeAll(friendsRequestObserver.stream().toList());
        initApp(this.currentuser);
    }

    public void declineFriendRequest(ActionEvent actionEvent) {
        if(friendsRequestList.getSelectionModel().getSelectedItem() == null)
            return;

        String userInfo = friendsRequestList.getSelectionModel().getSelectedItem().toString();
        String email = userInfo.split(" ")[2];
        //String status = userInfo.split(" ")[4];

        //if(!status.equals("PENDING"))
         //   return;

        networkService.declineFriendRequest(email,currentuser.getEmail());

        friendsRequestSentObserver.removeAll(friendsRequestSentObserver.stream().toList());
        userObserver.removeAll(userObserver.stream().toList());
        friendsObserver.removeAll(friendsObserver.stream().toList());
        friendsRequestObserver.removeAll(friendsRequestObserver.stream().toList());
        initApp(this.currentuser);
    }
    public void logOut(ActionEvent actionEvent) throws IOException {
        FXMLLoader stageLoader = new FXMLLoader();
        stageLoader.setLocation(getClass().getResource("/com/example/laborator7/LogIn-view.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();

        GridPane logInLayout = stageLoader.load();
        Scene scene = new Scene(logInLayout);
        stage.setScene(scene);

        LogInController logInController = stageLoader.getController();
        logInController.setService(this.networkService);
        logInController.setMessageService(this.messageService);
        stage.setWidth(639);
        stage.setHeight(400);
        stage.show();
    }

    public void deleteAccount(ActionEvent actionEvent) throws IOException {
        // delete user from db
        networkService.deleteUser(currentuser.getEmail());
        // change scene
        FXMLLoader stageLoader = new FXMLLoader();
        stageLoader.setLocation(getClass().getResource(".com/example/laborator7/LogIn-view.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();

        GridPane logInLayout = stageLoader.load();
        Scene scene = new Scene(logInLayout);
        stage.setScene(scene);

        LogInController logInController = stageLoader.getController();
        logInController.setService(this.networkService);

        stage.show();
    }

    public void sendRequest(ActionEvent actionEvent) {
        if(userList.getSelectionModel().getSelectedItem() == null)
            return;

        String userInfo = userList.getSelectionModel().getSelectedItem();
        String email = userInfo.split(" ")[2];

        networkService.createFriendRequest(currentuser.getEmail(), email);
        friendsObserver.removeAll(friendsObserver.stream().toList());
        friendsRequestObserver.removeAll(friendsRequestObserver.stream().toList());
        friendsRequestSentObserver.removeAll(friendsRequestSentObserver.stream().toList());
        userObserver.removeAll(userObserver.stream().toList());
        initApp(this.currentuser);
    }

    public void cancelFriendRequest(ActionEvent actionEvent) {
        if(friendRequestsSent.getSelectionModel().getSelectedItem() == null)
            return;

        String userInfo = friendRequestsSent.getSelectionModel().getSelectedItem().toString();
        String email = userInfo.split(" ")[2];

        friendsObserver.removeAll(friendsObserver.stream().toList());
        networkService.deleteFriendship(currentuser.getEmail(), email);
        friendsRequestSentObserver.removeAll(friendsRequestSentObserver.stream().toList());
        userObserver.removeAll(userObserver.stream().toList());
        initApp(this.currentuser);
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public void showMessages(User user1, User user2) {

        List<Message> messagesBetweenUsers = messageService.getMessagesBetweenUsers(user1, user2);

        messagesBetweenUsers.sort(Comparator.comparing(Message::getData));

        messageVBox.getChildren().clear();

        for (Message message : messagesBetweenUsers) {
            System.out.println(message.getMessage()+ " "+ message.getFrom().getFirstName()+ message.getTo().getFirstName());
            String senderName = message.getFrom().equals(currentuser) ? "You" : message.getFrom().getFirstName();
            Label messageLabel = new Label(senderName + ": " + message.getMessage());

            Pos alignment = message.getFrom().equals(currentuser) ? Pos.CENTER_LEFT : Pos.CENTER_RIGHT;

            HBox messageContainer = new HBox();
            messageContainer.getChildren().add(messageLabel);
            messageContainer.setAlignment(alignment);

            messageVBox.getChildren().addAll(messageContainer, new Label(""));
        }

        messageScrollPane.setVvalue(1.0);
    }

    public void sendMessage(KeyEvent event) {
        if (messagesFriendList.getSelectionModel().getSelectedItems().isEmpty() || event.getCode() != KeyCode.ENTER)
            return;

        String textFromTextField = message.getText();
        if (textFromTextField.isEmpty())
            return;

        List<User> selectedUsers = messagesFriendList.getSelectionModel().getSelectedItems().stream()
                .map(email -> networkService.getUserByEmail(email.split(" ")[2]).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        for (User sent : selectedUsers) {
            Message mesaj = new Message(currentuser, sent, textFromTextField, LocalDateTime.now());
            this.messageService.addMessage(mesaj);
            Platform.runLater(() -> showMessages(currentuser, sent));
        }

        message.clear();
    }


    public void enterMessages(Event event) {
    }
}