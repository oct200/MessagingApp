package com.example.reteasociala;

import ObserverClasses.Observable;
import ObserverClasses.Observer;
import ObserverClasses.ObserverAction;
import com.example.reteasociala.userAnchors.*;
import domain.Friendship;
import domain.Mesaj;
import domain.Utilizator;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderRepeat;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.ServiceUtilizatori;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;


import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;



public class MainWindowController implements Observer {
    HelloApplication app;
    Utilizator utilizatorCurent;
    int selectedButton;
    Utilizator cautat, utilizatorMesaje;

    @FXML
    protected Label labelUsername;
    @FXML
    protected Button buttonReceivedRequests;
    @FXML
    protected Button buttonSentRequests;
    @FXML
    protected Button buttonFriends;
    @FXML
    protected VBox vBoxDreapta;
    @FXML
    protected TextField textSearch;
    @FXML
    protected Button buttonRezultat;
    @FXML
    protected AnchorPane paneCautare;
    @FXML
    protected BorderPane paneMesaje;
    @FXML
    protected VBox vBoxMesaje;
    @FXML
    protected TextField textMesaj;
    @FXML
    protected TextField textReplyId;


    public void initializare(HelloApplication app, Utilizator utilizator) {
        this.app = app;
        app.serviceFriendships.addObserver(this);
        app.serviceMesaje.addObserver(this);
        this.utilizatorCurent = utilizator;
        labelUsername.setText(utilizatorCurent.getUsername());
        selectedButton = 1;
        updateRightPane();
    }

    @FXML
    private void searchCurrentUser(){
        searchUserByUsername(utilizatorCurent.getUsername());
    }

    @FXML
    private void searchUsername() {
        if (textSearch.getText().isEmpty())
            return;
        searchUserByUsername(textSearch.getText());
    }


    private void searchUserByUsername(String username){
        Optional<Utilizator> rez = app.serviceUtilizatori.existaUsername(username);
        buttonRezultat.setVisible(true);
        if(rez.isEmpty()){
            buttonRezultat.setStyle("-fx-background-color: red");
            buttonRezultat.setDisable(true);
            buttonRezultat.setText("This username does not exist");
            return;
        }
        cautat = rez.get();
        Optional<Friendship> friendship = app.serviceFriendships.getFriendship(utilizatorCurent.getId(), rez.get().getId());
        if(friendship.isEmpty()){
            buttonRezultat.setStyle("-fx-background-color: green");
            buttonRezultat.setDisable(false);
            buttonRezultat.setText("Send friend request");
            return;
        }
        if(friendship.get().getStatus() == 1){
            buttonRezultat.setStyle("-fx-background-color: green");
            buttonRezultat.setDisable(true);
            buttonRezultat.setText("Friends since " + friendship.get().getDataPrietenie().toString());
            return;
        }
        if(Objects.equals(friendship.get().getId().getFirst(), utilizatorCurent.getId())){
            buttonRezultat.setStyle("-fx-background-color: green");
            buttonRezultat.setDisable(true);
            buttonRezultat.setText("You sent a request on " + friendship.get().getDataPrietenie().toString());
            return;
        }
        buttonRezultat.setStyle("-fx-background-color: green");
        buttonRezultat.setDisable(true);
        buttonRezultat.setText("Request received on " + friendship.get().getDataPrietenie().toString());

    }

    @FXML
    public void functieButonRezultat(){
        if(Objects.equals(buttonRezultat.getText(), "Send friend request")){
            app.serviceFriendships.addRequest(utilizatorCurent.getId(), cautat.getId());
        }
    }

    private void fillSentRequests(){
        List<Friendship> req = app.serviceFriendships.getSentRequests(utilizatorCurent.getId());
        vBoxDreapta.getChildren().clear();
        req.forEach(friendship ->{
            vBoxDreapta.getChildren().add(new UserAnchorSent(app.serviceUtilizatori.getUtilizator(friendship.getId().getSecond()),app,utilizatorCurent));
        });
    }

    private void fillReceivedRequests(){
        List<Friendship> req = app.serviceFriendships.getReceivedRequests(utilizatorCurent.getId());
        vBoxDreapta.getChildren().clear();
        req.forEach(friendship ->{
            vBoxDreapta.getChildren().add(new UserAnchorReceived(app.serviceUtilizatori.getUtilizator(friendship.getId().getFirst()),app,utilizatorCurent));
        });
    }

    private void fillFriends(){
        List<Friendship> req = app.serviceFriendships.getFriends(utilizatorCurent.getId());
        vBoxDreapta.getChildren().clear();
        req.forEach(friendship ->{
            if (utilizatorCurent.equals(app.serviceUtilizatori.getUtilizator(friendship.getId().getFirst())))
                vBoxDreapta.getChildren().add(new UserAnchorFriend(app.serviceUtilizatori.getUtilizator(friendship.getId().getSecond()),app,utilizatorCurent,this));
            else
                vBoxDreapta.getChildren().add(new UserAnchorFriend(app.serviceUtilizatori.getUtilizator(friendship.getId().getFirst()),app,utilizatorCurent,this));
        });
    }

    @FXML
    protected void deleteMyAcoount(){
        try{
            app.serviceUtilizatori.deleteAccount(utilizatorCurent.getId());
            MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION,"Deleted","Your account was deleted successfully");
            System.exit(0);
        }
        catch (RuntimeException e){
            MessageAlert.showMessage(null,Alert.AlertType.ERROR,"eroare",e.getMessage());
        }
    }

    public void updateRightPane(){
        switch (selectedButton){
            case 0: fillSentRequests(); break;
            case 1: fillReceivedRequests(); break;
            case 2: fillFriends(); break;
        }
    }

    @FXML
    protected void setPaneCautareVisible(){
        paneCautare.setVisible(true);
        paneMesaje.setVisible(false);
    }

    protected void setPaneMesajeVisible(){
        paneMesaje.setVisible(true);
        paneCautare.setVisible(false);
    }

    @FXML
    protected void sendMessage(){
        Long idReply = null;
        if(!Objects.equals(textReplyId.getText(), "")) {
            try {
                idReply = Long.parseLong(textReplyId.getText());
            } catch (Exception e) {
                MessageAlert.showMessage(null, Alert.AlertType.ERROR, "eroare", "eroare la idReply");
            }
        }
        String mesaj = textMesaj.getText();
        if(mesaj.isEmpty()){
            MessageAlert.showMessage(null, Alert.AlertType.WARNING,"Empty message","The message cannot be empty");
            return;
        }
        app.serviceMesaje.addMesaj(mesaj,utilizatorCurent.getId(),utilizatorMesaje.getId(),idReply);
    }

    public void loadMesaje(){
        setPaneMesajeVisible();
        vBoxMesaje.getChildren().clear();
        List<Mesaj> mesaje = app.serviceMesaje.getMesajeUseri(utilizatorCurent.getId(),utilizatorMesaje.getId());
        mesaje.forEach((mesaj) ->{
            Label l1 = new Label(String.valueOf(mesaj.getId()) + ". " + mesaj.getMesaj());
            if (mesaj.getReplyTo() != null){
                l1.setText(l1.getText() + " RASPUNS LA  " + mesaj.getReplyTo().getMesaj());
            }
            vBoxMesaje.getChildren().add(l1);
        });
    }

    @FXML
    protected void buttonSent(){
        if (selectedButton == 0){
            return;
        }
        /*buttonFriends.setStyle(buttonFriends.getStyle() + " -fx-background-color: grey ; ");
        buttonReceivedRequests.setStyle(buttonReceivedRequests.getStyle() + " -fx-background-color: grey ; ");
        buttonSentRequests.setStyle(buttonSentRequests.getStyle() + " -fx-background-color: blue ; " );
        */selectedButton = 0;
        fillSentRequests();
    }

    @FXML
    protected void buttonReceived(){
        if (selectedButton == 1){
            return;
        }
        /*buttonFriends.setStyle(buttonFriends.getStyle() + " -fx-background-color: grey ; ");
        buttonReceivedRequests.setStyle(buttonReceivedRequests.getStyle() + " -fx-background-color: blue ; ");
        buttonSentRequests.setStyle(buttonSentRequests.getStyle() + " -fx-background-color: grey ; ");
        */selectedButton = 1;
        fillReceivedRequests();
    }

    @FXML
    protected void buttonFriends(){
        if (selectedButton == 2){
            return;
        }
        /*buttonFriends.setStyle(buttonFriends.getStyle() + " -fx-background-color: blue ; ");
        buttonReceivedRequests.setStyle(buttonReceivedRequests.getStyle() + " -fx-background-color: grey ; ");
        buttonSentRequests.setStyle(buttonSentRequests.getStyle() + " -fx-background-color: grey ; ");
        */selectedButton = 2;
        fillFriends();
    }

    public Utilizator getUtilizatorMesaje() {
        return utilizatorMesaje;
    }

    public void setUtilizatorMesaje(Utilizator utilizatorMesaje) {
        this.utilizatorMesaje = utilizatorMesaje;
    }

    @Override
    public void update(ObserverAction action) {
        switch (action.getActionType()){
            case NEWREQUEST -> {
                if(Objects.equals(action.getFrom(), utilizatorCurent.getId()) || Objects.equals(action.getTo(), utilizatorCurent.getId())) {
                    updateRightPane();
                    searchUsername();
                }
                if (Objects.equals(action.getTo(), utilizatorCurent.getId())) {
                    MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"new friend request","You have a new friend request from " + app.serviceUtilizatori.getUtilizator(action.getFrom()).getUsername());
                }
            }
            case NEWFRIEND, DELETEFRIEND -> {
                if(Objects.equals(action.getFrom(), utilizatorCurent.getId()) || Objects.equals(action.getTo(), utilizatorCurent.getId())) {
                    updateRightPane();
                    searchUsername();
                }
            }
            case NEWMESSAGE -> {
                if (utilizatorMesaje != null)
                    if(Objects.equals(action.getFrom(), utilizatorCurent.getId()) || Objects.equals(action.getTo(), utilizatorCurent.getId()))
                        loadMesaje();
            }
        }
    }
}