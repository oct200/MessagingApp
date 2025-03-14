package com.example.reteasociala;

import domain.Utilizator;
import domain.validators.FriendshipValidator;
import domain.validators.MesajValidator;
import domain.validators.UtilizatorValidator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repository.Repository;
import repository.database.FriendshipDatabaseRepository;
import repository.database.MesajeDatabaseRepository;
import repository.database.UtilizatoriDatabaseRepository;
import services.ServiceFriendships;
import services.ServiceMesaje;
import services.ServiceUtilizatori;

import java.io.IOException;

public class HelloApplication extends Application {
    public ServiceUtilizatori serviceUtilizatori;
    public ServiceFriendships serviceFriendships;
    public ServiceMesaje serviceMesaje;
    @Override
    public void start(Stage stage) throws IOException {
        System.out.println(10 + " " + "asd");
        initVars();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("signin.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        SignInController signincontroller = fxmlLoader.getController();
        signincontroller.setApp(this);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        Stage stage2 = new Stage();
        FXMLLoader fxmlLoader2 = new FXMLLoader(HelloApplication.class.getResource("signin.fxml"));
        stage2.setScene(new Scene(fxmlLoader2.load()));
        SignInController signincontroller2 = fxmlLoader2.getController();
        signincontroller2.setApp(this);
        stage2.show();
    }

    private void initVars() {
        UtilizatorValidator uValidator = new UtilizatorValidator();
        UtilizatoriDatabaseRepository repo = new UtilizatoriDatabaseRepository(uValidator,"Utilizatori");
        serviceUtilizatori = new ServiceUtilizatori(repo);
        FriendshipValidator fv = new FriendshipValidator();
        FriendshipDatabaseRepository fdbr = new FriendshipDatabaseRepository(fv,"prietenii");
        serviceFriendships = new ServiceFriendships(fdbr);
        MesajValidator mv = new MesajValidator();
        MesajeDatabaseRepository mdbr = new MesajeDatabaseRepository(mv,"mesaje");
        serviceMesaje = new ServiceMesaje(mdbr);

    }

    public static void main(String[] args) {
        launch();
    }
}