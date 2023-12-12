package com.example.pcremotecontrol.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class GuideController extends BaseController{
    @FXML
    private Label guideTxt;

    @FXML
    private Button returnButton;

    public void initGuideController() {
        guideTxt.setText("Lol");
    }

    @FXML
    protected void onReturnButtonClick() {
        sceneManager.switchScene("menu-view.fxml");
    }
}
