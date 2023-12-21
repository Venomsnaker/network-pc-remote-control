package com.example.pcremotecontrol.controllers;

import com.example.pcremotecontrol.MainApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class MailLibraryController extends BaseController{
    @FXML
    private Label mainTxt;

    @FXML
    private Label mailListTxt;

    @FXML
    private TextField mailAddressTextField;

    private static final String main_txt =
        "Ở trên là các mails được ủy quyền.\n" +
            "Vui lòng nhập các mails mới ở phía dưới.";

    public void initMailLibrary() {
        mainTxt.setText(main_txt);
        updateServerStageUI();
    }

    private void updateUI(List<String> mailsSaved) {
        String output = String.join("\n", mailsSaved);
        mailListTxt.setText(output);
    }

    @FXML
    protected void onMailAddButtonClick() {
        MainApplication.getInstance().addMailAddress(mailAddressTextField.getText());
        mailAddressTextField.setText("");
        updateUI(MainApplication.getInstance().getMailsLibrary());
    }

    @FXML
    protected void onMailRemoveButtonClick() {
        MainApplication.getInstance().removeMailAddress(mailAddressTextField.getText());
        mailAddressTextField.setText("");
        updateUI(MainApplication.getInstance().getMailsLibrary());
    }
}
