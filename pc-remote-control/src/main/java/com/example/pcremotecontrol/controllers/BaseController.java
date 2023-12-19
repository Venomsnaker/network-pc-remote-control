package com.example.pcremotecontrol.controllers;

import com.example.pcremotecontrol.MainApplication;
import com.example.pcremotecontrol.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public abstract class BaseController {

    protected SceneManager sceneManager;
    public void setSceneManager(SceneManager sceneManager) { // if SceneManager and BaseController are in different packages, change visibility
        this.sceneManager = sceneManager;
    }

    @FXML
    public Label serverStageTxt;

    @FXML
    public void onGuideButtonClick() {
        sceneManager.switchScene("guide-view.fxml");
    }

    @FXML public void updateServerStageUI() {
        if (MainApplication.getInstance().getServerFlag()) {
            serverStageTxt.setText("Server đang chạy");
            serverStageTxt.setTextFill(Color.web("#50C878"));
        } else {
            serverStageTxt.setText("Server đang tắt");
            serverStageTxt.setTextFill(Color.web("#FF3800"));
        }
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
    public void onServerButtonClick() {sceneManager.switchScene("server-view.fxml");}

    @FXML
    public void onExitButtonClick() {
        System.exit(0);
    }
}
