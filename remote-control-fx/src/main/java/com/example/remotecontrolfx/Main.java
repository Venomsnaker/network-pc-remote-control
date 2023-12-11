package com.example.remotecontrolfx;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        SceneManager sceneManager = new SceneManager(stage);
        sceneManager.switchScene("menu-view.fxml");
        stage.setTitle("Menu View");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}