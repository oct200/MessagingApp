package com.example.reteasociala.userAnchors;

import com.almasb.fxgl.app.MainWindow;
import com.example.reteasociala.MainWindowController;
import com.sun.tools.javac.Main;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class PanelProfil extends AnchorPane {
    private MainWindowController mainWindow;
    private int idProfil;
    private Button pozaProfil;
    private Button actiune;
    private Label info;

    public PanelProfil(MainWindowController mainWindow, int idProfil) {
        this.mainWindow = mainWindow;
        this.idProfil = idProfil;
        loadGUI();
    }

    private void loadGUI(){

    }

}
