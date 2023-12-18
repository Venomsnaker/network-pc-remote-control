package com.example.pcremotecontrol.controllers;

import com.example.pcremotecontrol.MainApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class MenuController extends BaseController{

    private static final String main_txt =
            "Đây là phần mềm dùng điều khiển máy tính từ xa qua email.\n" +
                "- Bắt Đầu/ Tắt để giao tiếp với Server.\n" +
                "- Truy cập Hướng Dẫn để xem cách sử dụng.\n" +
                "- Truy cập Chức Năng để xem các lệnh.\n" +
                "- Truy cập Thư Viện Mails để nhập các mails được cấp quyền.\n" +
                "- Truy cập Thư Viện Apps để nhập các apps cần điều khiển.\n" +
                "- Truy cập Thông Tin để biết thêm về phần mềm này.\n" +
                "Cảnh báo: Phần mềm này sẽ truy cập nhiều thông tin của máy chủ.\n\t"+
                    "Vui lòng xem kĩ phần Hướng Dẫn và Chức Năng trước khi sử dụng\n"+
                "Cảm ơn bạn vì đã sử dụng phần mềm này!";

    @FXML
    private Label mainTxt;

    @FXML
    private Button startButton;

    public void initMenu() {
        mainTxt.setText(main_txt);
        updateServerStageUI();
    }

    @FXML
    protected void onStartButtonClick() {
        if (MainApplication.getInstance().getServerFlag()) {
            MainApplication.getInstance().setServerFlag(false);
            startButton.setText("Bắt Đầu");
            updateServerStageUI();
        } else {
            MainApplication.getInstance().setServerFlag(true);
            startButton.setText("Kết Thúc");
            updateServerStageUI();
        }
    }
}