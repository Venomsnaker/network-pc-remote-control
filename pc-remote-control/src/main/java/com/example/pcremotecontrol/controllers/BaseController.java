package com.example.pcremotecontrol.controllers;

import com.example.pcremotecontrol.SceneManager;
import javafx.application.Application;
import javafx.fxml.FXML;

public abstract class BaseController {
    protected SceneManager sceneManager;
    public void setSceneManager(SceneManager sceneManager) { // if SceneManager and BaseController are in different packages, change visibility
        this.sceneManager = sceneManager;
    }

    @FXML
    public void onGuideButtonClick() {
        sceneManager.switchScene("guide-view.fxml");
    }

    @FXML
    public void onFunctionButtonClick() {
        sceneManager.switchScene("function-view.fxml");
    }

    @FXML
    public void onMailLibraryButtonClick() {
        sceneManager.switchScene("mail-library-view.fxml");
    }

    @FXML
    public void onAppLibraryButtonClick() {
        sceneManager.switchScene("app-library-view.fxml");
    }

    @FXML
    public void onCreditButtonClick() {
        sceneManager.switchScene("credit-view.fxml");
    }

    @FXML
    public void onMenuButtonClick() {
        sceneManager.switchScene("menu-view.fxml");
    }

    @FXML
    public void onExitButtonClick() {
        System.exit(0);
    }
}
