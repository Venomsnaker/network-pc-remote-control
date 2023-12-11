package com.example.remotecontrolfx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MenuController extends BaseController{
    @FXML
    private Label nameTxt;

    @FXML
    private Button startButton;

    @FXML
    private Button guideButton;

    @FXML
    private Button mailsLibraryButton;

    @FXML
    private Button appsLibraryButton;

    @FXML
    private Button creditButton;

    @FXML
    private Button exitButton;

    @FXML
    protected void onStartButtonClick() {
        if (startButton.getText().equals("Start")) {
            startButton.setText("Stop");
            // Start The Mail Server
        } else {
            startButton.setText("Start");
            // Stop The Mail Server
        }
    }

    @FXML
    protected void onMailsLibraryButtonClick() {
        sceneManager.switchScene("mails-library-view.fxml");
    }

    @FXML
    protected void onAppsLibraryButtonClick() {
        sceneManager.switchScene("apps-library-view.fxml");
    }

    @FXML
    protected void onGuideButtonClick() {
        sceneManager.switchScene("guide-view.fxml");
    }

    @FXML
    protected void onCreditButtonClick() {
        sceneManager.switchScene("credit-view.fxml");
    }

    @FXML
    protected void onExitButtonClick() {
        System.exit(0);
    }
}