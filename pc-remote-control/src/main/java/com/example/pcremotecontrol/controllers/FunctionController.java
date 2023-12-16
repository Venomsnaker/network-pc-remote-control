package com.example.pcremotecontrol.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class FunctionController extends BaseController{
    private static final String main_txt =
            "Các lệnh phần mềm này có thể thực hiện:\n" +
                "Cấu trúc [tiêu đề mail] [nội dung mail].\n" +
                    "- Nhận danh sách Apps [get-apps] []\n" +
                    "- Khởi động Apps [start-app] [địa chỉ file exe]\n" +
                    "- Khởi động Apps lưu sẵn [start-app-by-name] [tên app]\n" +
                    "- Tắt Apps [stop-app] [tên app]\n\n" +

                    "- Nhận danh sách Services [get-services] []\n" +
                    "- Khởi động Service [start-service] [tên service]\n" +
                    "- Tắt Service [stop-service] [tên service]\n\n" +

                    "- Lấy File [collect-file] [địa chỉ file]\n" +
                    "- Lấy Screenshot [get-screenshot] []\n" +
                    "- Khởi động Keylogger [start-keylogger] []\n" +
                    "- Tắt Keylogger [stop-keylogger] []\n\n" +

                    "- Tắt máy tính [shutdown-server] [thời gian delay theo s]\n" +
                    "- Tắt máy tính [restart-server] [thời gian delay theo s]\n" +
                    "- Dừng lệnh tắt máy tính [cancel-server-shutdown] []";

    @FXML
    private Label mainTxt;

    public void initFunction() {
        mainTxt.setText(main_txt);
    }
}
