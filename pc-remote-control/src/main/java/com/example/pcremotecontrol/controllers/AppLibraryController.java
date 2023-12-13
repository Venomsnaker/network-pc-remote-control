package com.example.pcremotecontrol.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class AppLibraryController extends BaseController{
    @FXML
    private Label appsLibraryTxt;

    @FXML
    private Button returnButton;

    @FXML
    protected void onReturnButtonClick() {
        sceneManager.switchScene("menu-view.fxml");
    }
}
