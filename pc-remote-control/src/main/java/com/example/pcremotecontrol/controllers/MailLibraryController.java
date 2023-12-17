package com.example.pcremotecontrol.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class MailLibraryController extends BaseController{
    static private List<String> mailSaved = new ArrayList<String>();
    private static void addMailAddress(String mail) {
        mailSaved.add(mail);
    }

    private static void removeMailAddress(String mail) {
        mailSaved.remove(mail);
    }

    private static final String main_txt =
            "Ở trên là các mails được ủy quyền.\n" +
                "Cấu trúc: <tên người gửi> địa chỉ mail";

    @FXML
    private Label mainTxt;

    @FXML
    private Label mailListTxt;

    @FXML
    private TextField mailTextField;


    public void initMailLibrary() {
        mainTxt.setText(main_txt);
    }

    @FXML
    protected void onMailAddButtonClick() {
        addMailAddress(mailTextField.getText());
        String output = String.join("\n", mailSaved);
        mailListTxt.setText(output);
    }

    @FXML
    protected void onMailRemoveButtonClick() {
        removeMailAddress(mailTextField.getText());
        String output = String.join("\n", mailSaved);
        mailListTxt.setText(output);
    }
}
