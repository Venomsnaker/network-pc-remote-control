package com.example.remotecontrolfx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MailsLibraryController extends BaseController{
    @FXML
    private Label mailsLibraryTxt;

    @FXML
    private Button returnButton;

    @FXML
    protected void onReturnButtonClick() {
        sceneManager.switchScene("menu-view.fxml");
    }

}
