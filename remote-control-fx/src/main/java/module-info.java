module com.example.remotecontrolfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;

    opens com.example.remotecontrolfx to javafx.fxml;
    exports com.example.remotecontrolfx;
}