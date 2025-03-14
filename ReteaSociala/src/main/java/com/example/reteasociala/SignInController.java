package com.example.reteasociala;

import domain.Utilizator;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import services.ServiceUtilizatori;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.Optional;


public class SignInController {
    HelloApplication app;
    public Utilizator utilizatorCurent;

    public Utilizator getUtilizatorCurent() {
        return utilizatorCurent;
    }

    public void setUtilizatorCurent(Utilizator utilizatorCurent) {
        this.utilizatorCurent = utilizatorCurent;
    }

    @FXML
    private TextField textUsername;
    @FXML
    private PasswordField textPassword;

    @FXML
    protected void handleLogIn(ActionEvent event) {
        try {
            Optional< Utilizator> utilizatorCurent = app.serviceUtilizatori.existaUtilizator(textUsername.getText(), textPassword.getText());
            if (utilizatorCurent.isPresent()){
                System.out.println(utilizatorCurent.get());
                setUtilizatorCurent(utilizatorCurent.get());
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("MainWindow.fxml"));
                Scene newScene = new Scene(fxmlLoader.load());
                MainWindowController mainWindowController = fxmlLoader.getController();
                System.out.println("sda");
                mainWindowController.initializare(app,utilizatorCurent.get());
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.setScene(newScene);
            }
            else{
                MessageAlert.showMessage(null,Alert.AlertType.WARNING,"The user does not exist","The user with the given username and password does not exist");
            }
        }catch (Exception e) {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR,"Error",e.getMessage());
        }
    }

    @FXML
    protected void handleNewUser(ActionEvent event) {
        try {
            if(app.serviceUtilizatori.existaUsername(textUsername.getText()).isPresent()) {
                MessageAlert.showMessage(null, Alert.AlertType.ERROR,"The username already exists","The user with the given username already exists");
                return;
            }
            app.serviceUtilizatori.addUtilizator(textUsername.getText(), textPassword.getText());
            MessageAlert.showMessage(null,Alert.AlertType.CONFIRMATION,"New user created","");
        }catch (Exception e) {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR,"Error",e.getMessage());
        }
    }

    public void setApp(HelloApplication app) {
        this.app = app;
    }


}
