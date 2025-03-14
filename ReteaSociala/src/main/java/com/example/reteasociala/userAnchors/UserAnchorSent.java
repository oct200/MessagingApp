package com.example.reteasociala.userAnchors;

import com.example.reteasociala.HelloApplication;
import com.example.reteasociala.SignInController;
import domain.Utilizator;

public class UserAnchorSent extends GenericUserAnchor{
    public UserAnchorSent(Utilizator utilizator, HelloApplication app, Utilizator utilizatorCurent) {
        super(utilizator,app, utilizatorCurent);
        stButton.setVisible(false);
        drButton.setOnAction(event -> handleDeleteClick());
        drButton.setStyle("-fx-background-radius: 25;     -fx-border-radius: 25;     -fx-background-color: #4CAF50;     -fx-border-color: #2C6B1F;     -fx-border-width: 3;     -fx-pref-width: 25px;     -fx-pref-height: 25px;     -fx-alignment: center;     -fx-padding: 0; -fx-background-image: url(user-x.png); -fx-background-position: center center; -fx-background-size: 15px 15px; -fx-background-repeat: no-repeat;");

    }

    public void handleDeleteClick() {
        application.serviceFriendships.deleteFriendship(utilizator.getId(),utilizatorCurent.getId());
    }
}
