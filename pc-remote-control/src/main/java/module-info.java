module com.example.pcremotecontrol {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    requires java.activation;
    requires java.mail;
    requires com.github.kwhat.jnativehook;

    opens com.example.pcremotecontrol to javafx.fxml;
    exports com.example.pcremotecontrol;
    exports com.example.pcremotecontrol.controllers;
    opens com.example.pcremotecontrol.controllers to javafx.fxml;
    exports com.example.pcremotecontrol.servers;
    opens com.example.pcremotecontrol.servers to javafx.fxml;
}