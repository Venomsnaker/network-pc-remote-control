package com.example.remotecontrolfx;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class BaseController {
    protected SceneManager sceneManager;
    void setSceneManager(SceneManager sceneManager) { // if SceneManager and BaseController are in different packages, change visibility
        this.sceneManager = sceneManager;
    }
}
