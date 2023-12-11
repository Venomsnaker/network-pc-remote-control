package com.example.pcremotecontrol.controllers;

import com.example.pcremotecontrol.SceneManager;

public abstract class BaseController {
    protected SceneManager sceneManager;
    public void setSceneManager(SceneManager sceneManager) { // if SceneManager and BaseController are in different packages, change visibility
        this.sceneManager = sceneManager;
    }
}
