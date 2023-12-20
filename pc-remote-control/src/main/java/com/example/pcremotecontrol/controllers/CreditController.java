package com.example.pcremotecontrol.controllers;

import com.example.pcremotecontrol.MainApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class CreditController extends BaseController{
    private static final String main_txt =
            "Phần mềm sử dụng:\n" +
                "- Các thư viện Java cơ bản.\n" +
                "- Một số thư viện Java đặc biệt: javafx, javax.activation,\n\tjavax.mail, jnativehook.\n" +
            "Sản phẩm của các thành viên:\n"+
                "- 22120068 - Nguyễn Anh Đức\n" +
                "- 22120128 - Bùi Quốc Huy\n" +
                "- 22120177 - Văn Tuấn Kiệt\n" +
            "Cảm ơn bạn vì đã sử dụng chương trình.";

    @FXML
    private Label mainTxt;

    public void initCredit() {
        mainTxt.setText(main_txt);
        updateServerStageUI();
    }
}
