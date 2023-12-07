package com.example.laborator7.Gui;

import com.example.laborator7.Domain.Message;
import com.example.laborator7.Service.MessageService;
import com.example.laborator7.Validators.ValidationException;
import com.example.laborator7.Domain.User;
import com.example.laborator7.Service.Service;
import com.example.laborator7.Validators.UserValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class SignUpController {

    @FXML
    private TextField first_name;
    @FXML
    private TextField last_name;
    @FXML
    private TextField email;
    @FXML
    private TextField age;
    @FXML
    private TextField password;
    @FXML
    private TextField password_confirm;

    @FXML
    private Text firstnameErrorText;
    @FXML
    private Text lastnameErrorText;
    @FXML
    private Text emailErrorText;
    @FXML
    private Text passwordErrorText;

    private UserValidator userValidator = new UserValidator();
    private Service service;
    private MessageService messageService;

    public Service getService() {
        return service;
    }

    @FXML
    protected void onCreateAccountClick(ActionEvent event) throws IOException {
        User newUser = new User(first_name.getText(), last_name.getText(),Integer.parseInt(age.getText()), email.getText(), password.getText());
        try{
            userValidator.validate(newUser);
        }
        catch (ValidationException exception) {
            String err = exception.toString().split(":")[1];
            switch (err.charAt(1)) {
                case '1' -> {
                    firstnameErrorText.setText(err.substring(1));
                    firstnameErrorText.setVisible(true);
                }
                case '2' -> {
                    lastnameErrorText.setText(err.substring(1));
                    lastnameErrorText.setVisible(true);
                }
                default -> {
                    emailErrorText.setText(err);
                    System.out.println(err);
                    emailErrorText.setVisible(true);
                }
            }
            return;
        }

        if(!password.getText().equals(password_confirm.getText()))
            passwordErrorText.setVisible(true);
        else { // adaugam utilizator
            service.addUser(newUser);

            FXMLLoader stageLoader = new FXMLLoader();
            stageLoader.setLocation(getClass().getResource("/com/example/laborator7/LogIn-view.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

            GridPane appLayout = stageLoader.load();
            Scene scene = new Scene(appLayout);
            stage.setScene(scene);

            LogInController logInController = stageLoader.getController();
            logInController.setService(this.service);
            logInController.setMessageService(this.messageService);

            stage.show();
        }
    }

    public void goBackToLogIn(ActionEvent actionEvent) throws IOException {
        FXMLLoader stageLoader = new FXMLLoader();
        stageLoader.setLocation(getClass().getResource("/com/example/laborator7/LogIn-view.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();

        GridPane logInLayout = stageLoader.load();
        Scene scene = new Scene(logInLayout);
        stage.setScene(scene);

        LogInController logInController = stageLoader.getController();
        logInController.setService(this.service);
        logInController.setMessageService(this.messageService);
        stage.show();
    }

    public void onFirstnameTextChanged() {
        firstnameErrorText.setVisible(false);
    }

    public void onLastnameTextChanged() {
        lastnameErrorText.setVisible(false);
    }

    public void onEmailTextChanged() {
        emailErrorText.setVisible(false);
    }

    public void onPasswordTextChanged() {
        passwordErrorText.setVisible(false);
    }

    public void onConfirmPasswordTextChanged() {
        passwordErrorText.setVisible(false);
    }

    public void setService(Service service) {
        this.service = service;
    }
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }
}