package com.example.pcremotecontrol.controllers;

import com.example.pcremotecontrol.MainApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Pair;

public class ServerController extends BaseController{
    @FXML
    private Label mainTxt;

    @FXML
    private Label serverTxt;

    private static final String main_txt =
            "Vui lòng nhập thông tin Server.";

    @FXML
    private TextField mailAddressTextField;

    @FXML
    private TextField mailPasswordTextField;

    public void initServer() {
        mainTxt.setText(main_txt);
        updateUI();
        updateServerStageUI();
    }

    @FXML
    protected void onServerChangeButtonClick() {
        MainApplication.getInstance().updateServerInfo(mailAddressTextField.getText(), mailPasswordTextField.getText());
        mailAddressTextField.setText("");
        mailPasswordTextField.setText("");
        updateUI();
    }

    private void updateUI() {
        Pair<String, String> serverContent = MainApplication.getInstance().getServerInfo();
        serverTxt.setText(serverContent.getKey());
    }
}
