package com.example.pcremotecontrol;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainApplication extends Application {
    private boolean serverFlag = false;
    static private Map<String, String> appsSaved = new HashMap<String, String>();
    static private List<String> mailSaved = new ArrayList<String>();

    private static MainApplication instance;

    public void addAppAddress(String appName, String appPath) {
        appsSaved.put(appName, appPath);
    }

    public void removeAppAddress(String appName) {
        appsSaved.remove(appName);
    }

    public Map<String, String> getAppsLibrary() {
        return appsSaved;
    }

    public void addMailAddress(String mail) {
        mailSaved.add(mail);
    }

    public void removeMailAddress(String mail) {
        mailSaved.remove(mail);
    }

    public List<String> getMailsLibrary() {return mailSaved;}

    public static MainApplication getInstance() {
        return instance;
    }

    public void setServerFlag(boolean flag) {
        serverFlag = flag;
    }

    public boolean getServerFlag() {
        return serverFlag;
    }

    @Override
    public void start(Stage stage) throws IOException {
        SceneManager sceneManager = new SceneManager(stage);
        sceneManager.switchScene("menu-view.fxml");
        stage.setTitle("PC Remote Control");
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void init() {
        instance = this;
    }

    public static void main(String[] args) {
        launch(args);
    }
}