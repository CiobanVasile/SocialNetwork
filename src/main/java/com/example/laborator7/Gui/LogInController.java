package com.example.laborator7.Gui;
import com.example.laborator7.Domain.User;
import com.example.laborator7.Service.MessageService;
import com.example.laborator7.Service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
public class LogInController {

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    private Text emailErrorText;

    @FXML
    private Text passwordErrorText;

    private Service service;
    private MessageService messageService;

    public void setService(Service service) {
        this.service = service;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public Service getService() {
        return service;
    }

    @FXML
    protected void onLogInButtonCLick(ActionEvent event) throws IOException {
        User user = service.getUserByEmail(email.getText()).orElse(null);
        System.out.println(user);

        if(user == null)
        {
            emailErrorText.setVisible(true);
            passwordErrorText.setVisible(false);
        }
        else if(!password.getText().equals(user.getPassword())) {
            passwordErrorText.setVisible(true);
            emailErrorText.setVisible(false);
        }
        else {
            emailErrorText.setVisible(false);
            passwordErrorText.setVisible(false);

            FXMLLoader stageLoader = new FXMLLoader();
            stageLoader.setLocation(getClass().getResource("/com/example/laborator7/App-view2.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

            AnchorPane appLayout = stageLoader.load();
            Scene scene = new Scene(appLayout);
            stage.setScene(scene);

            AppController appController = stageLoader.getController();
            appController.setNetworkService(this.service);
            appController.setMessageService(this.messageService);
            appController.initApp(user);

            stage.show();
        }
    }
    public void onSignInClick(ActionEvent event) throws IOException {
        FXMLLoader stageLoader = new FXMLLoader();
        stageLoader.setLocation(getClass().getResource("/com/example/laborator7/SignUp-view.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        AnchorPane singUpLayout = stageLoader.load();
        Scene scene = new Scene(singUpLayout);
        stage.setScene(scene);

        SignUpController signUpController = stageLoader.getController();
        signUpController.setService(this.service);
        signUpController.setMessageService(this.messageService);
        stage.show();
    }

    public void onTextChanged(KeyEvent evt) {
        emailErrorText.setVisible(false);
        passwordErrorText.setVisible(false);
    }

    public void onPasswordChanged(KeyEvent evt) {
        passwordErrorText.setVisible(false);
    }

}
