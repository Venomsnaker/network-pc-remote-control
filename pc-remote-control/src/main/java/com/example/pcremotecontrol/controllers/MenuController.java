package com.example.pcremotecontrol.controllers;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;

public class MenuController extends BaseController{

    private static final String main_txt =
            "Đây là phần mềm dùng điều khiển máy tính từ xa qua email.\n" +
                "Bắt Đầu/ Tắt để giao tiếp với Server.\n" +
                "Truy cập Hướng Dẫn để xem cách sử dụng.\n" +
                "Truy cập Chức Năng để xem các lệnh.\n" +
                "Truy cập Thư Viện Mails để nhập các mails được cấp quyền.\n" +
                "Truy cập Thư Viện Apps để nhập các apps cần điều khiển.\n" +
                "Truy cập Thông Tin để biết thêm về phần mềm này.\n" +
                "Cảm ơn bạn vì đã sử dụng phần mềm này!";

    @FXML
    private Label mainTxt;

    @FXML
    private Button startButton;

    public void initMenu() {
        mainTxt.setText(main_txt);
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
}