package com.example.pcremotecontrol.controllers;

import com.example.pcremotecontrol.MainApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GuideController extends BaseController{
    private static final String main_txt =
            "Hướng dẫn sử dụng phần mềm cụ thể:\n" +
                "Lưu ý: Để có thể sử dụng được các lệnh services,\n\tvui lòng khởi động chương trình dưới quyền admin.\n" +
                "- Bước 1: Nhập các mails được cấp quyền trong Thư Viện Mails.\n" +
                "- Bước 2: Xem các câu lệnh điều khiển ở mục 'Chức Năng'.\n\t(có thể dùng lệnh help để xem từ mail)\n" +
                "- Bước 3: Bạn đã có thể điều khiển được máy tính từ xa qua mail.\n" +
                "Chú ý, phần mềm này có thể thực hiện mọi hành động trong 'Chức Năng'.\n" +
                "Vui lòng tự đảm bảo an toàn cho email cá nhân khi sử dụng chương trình này.\n";

    @FXML
    private Label mainTxt;

    public void initGuide() {
        mainTxt.setText(main_txt);
        updateServerStageUI();
    }
}
