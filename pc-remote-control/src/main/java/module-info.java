module com.example.pcremotecontrol {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;

    opens com.example.pcremotecontrol to javafx.fxml;
    exports com.example.pcremotecontrol;
    exports com.example.pcremotecontrol.controllers;
    opens com.example.pcremotecontrol.controllers to javafx.fxml;
}