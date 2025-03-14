package com.example.reteasociala.userAnchors;

import com.example.reteasociala.HelloApplication;
import com.example.reteasociala.SignInController;
import domain.Utilizator;

public class UserAnchorReceived extends GenericUserAnchor{
    public UserAnchorReceived(Utilizator utilizator, HelloApplication app, Utilizator utilizatorCurent) {
        super(utilizator, app, utilizatorCurent);
        stButton.setOnAction(handler -> {handlePlusClick();});
        drButton.setOnAction(handler -> {handleXClick();});
        stButton.setStyle("-fx-background-radius: 25;     -fx-border-radius: 25;     -fx-background-color: #4CAF50;     -fx-border-color: #2C6B1F;     -fx-border-width: 3;     -fx-pref-width: 25px;     -fx-pref-height: 25px;     -fx-alignment: center;     -fx-padding: 0; -fx-background-image: url(user-plus.png); -fx-background-position: center center; -fx-background-size: 15px 15px; -fx-background-repeat: no-repeat;");
        drButton.setStyle("-fx-background-radius: 25;     -fx-border-radius: 25;     -fx-background-color: #4CAF50;     -fx-border-color: #2C6B1F;     -fx-border-width: 3;     -fx-pref-width: 25px;     -fx-pref-height: 25px;     -fx-alignment: center;     -fx-padding: 0; -fx-background-image: url(user-x.png); -fx-background-position: center center; -fx-background-size: 15px 15px; -fx-background-repeat: no-repeat;");

    }

    public void handleXClick(){
        application.serviceFriendships.deleteFriendship(utilizator.getId(),utilizatorCurent.getId());
    }

    public void handlePlusClick(){
        application.serviceFriendships.updateFriendship(utilizator.getId(),utilizatorCurent.getId());
    }
}
