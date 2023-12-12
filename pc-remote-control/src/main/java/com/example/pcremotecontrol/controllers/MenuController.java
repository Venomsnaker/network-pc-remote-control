package com.example.pcremotecontrol.controllers;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController extends BaseController {
    private static final String main_txt =
            "Đây là phần mềm điều khiển máy tính từ xa.\n" +
            "Để biết thêm về các chức năng -> Hướng Dẫn.\n" +
            "Để biết thêm về thông tin phần mềm -> Thông Tin.\n" +
            "Để nhập các Mails được ủy quyền -> Thư Viện Mails.\n" +
            "Để nhập các Apps cần bắt đầu từ xa -> Thư Viện Apps.\n" +
            "Vui lòng khởi động phần mềm này với quyền administrator \n để thực hiện các chức năng services.\n" +
            "Bắt Đầu/ Tắt để giao tiếp với Server. \n Cảm ơn bạn vì đã sử dụng phần mềm này!";

    @FXML
    private Label mainTxt;

    @FXML
    private Button startButton;

    public void initMenu() {
        mainTxt.setText(main_txt);
        mainTxt.setWrapText(true);
    }

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
    protected void onGuideButtonClick() {
        sceneManager.switchScene("guide-view.fxml");
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
    protected void onCreditButtonClick() {
        sceneManager.switchScene("credit-view.fxml");
    }

    @FXML
    protected void onExitButtonClick() {
        System.exit(0);
    }
}
