package com.example.pcremotecontrol.controllers;

import com.example.pcremotecontrol.MainApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class AppLibraryController extends BaseController{
    @FXML
    private Label mainTxt;

    @FXML
    private Label appListTxt;

    @FXML
    private TextField appNameTextField;

    @FXML
    private TextField appAddressTextField;

    private static final String main_txt =
            "Ở trên là các apps được thiết lập từ trước.\n" +
                    "Vui lòng nhập các apps mới phía dưới.";

    public void initAppLibrary() {
        mainTxt.setText(main_txt);
        updateServerStageUI();
    }

    @FXML
    protected void updateUI(Map<String, String> appsSaved) {
        String output = String.join("\n", appsSaved.keySet());
        appListTxt.setText(output);
    }

    @FXML
    protected void onAppAddButtonClick() {
        MainApplication.getInstance().addAppAddress(appNameTextField.getText(), appAddressTextField.getText());
        appNameTextField.setText("");
        appAddressTextField.setText("");
        updateUI(MainApplication.getInstance().getAppsLibrary());
    }

    @FXML protected void onAppRemoveButtonClick() {
        MainApplication.getInstance().removeAppAddress(appNameTextField.getText());
        appNameTextField.setText("");
        appAddressTextField.setText("");
        updateUI(MainApplication.getInstance().getAppsLibrary());
    }
}
