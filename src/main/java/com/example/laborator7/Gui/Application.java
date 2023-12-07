package com.example.laborator7.Gui;

import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.IOException;
import com.example.laborator7.Domain.*;
import com.example.laborator7.Validators.*;
import com.example.laborator7.Repository.*;
import com.example.laborator7.Service.*;

import java.util.UUID;

public class Application extends javafx.application.Application {
    private Service networkService;
    private MessageService messageService;
    private User currentUser;

    @Override
    public void start(Stage stage) throws IOException{

        Validator<User> validator = new UserValidator();
        Validator<Friendship> val = new FriendshipValidator();
        Repository<UUID, User> userRepositoryDB = new UserDBRepository("jdbc:postgresql://localhost:5432/postgres",
                "postgres", "clujvasi21", validator);
        Repository<UUID, Friendship> friendshipRepositoryDB = new FriendshipDBRepository("jdbc:postgresql://localhost:5432/postgres",
                "postgres", "clujvasi21", val, userRepositoryDB);
        Repository<UUID,Message> messageRepositoryDB = new MessageDBRepository("jdbc:postgresql://localhost:5432/postgres",
                "postgres", "clujvasi21", userRepositoryDB);
        networkService = new Service(userRepositoryDB, friendshipRepositoryDB);
        messageService = new MessageService(messageRepositoryDB);
        initView(stage);
        stage.setWidth(639);
        stage.setHeight(450);
        stage.show();
    }
    private void initView(Stage primaryStage) throws IOException{
        FXMLLoader userLoader = new FXMLLoader();
        userLoader.setLocation(getClass().getResource("/com/example/laborator7/LogIn-view.fxml"));
        GridPane userLayout = userLoader.load();
        primaryStage.setScene(new Scene(userLayout));

        LogInController logInController = userLoader.getController();
        logInController.setService(networkService);
        logInController.setMessageService(this.messageService);
    }
    public static void main(String[] args) {
        launch(args);
    }
}