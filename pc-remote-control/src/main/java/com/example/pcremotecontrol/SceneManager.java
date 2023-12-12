package com.example.pcremotecontrol;

import com.example.pcremotecontrol.controllers.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SceneManager {
    private final Stage rootStage;

    public SceneManager(Stage rootStage) {
        if (rootStage == null) {
            throw new IllegalArgumentException();
        }
        this.rootStage = rootStage;
    }

    private final Map<String, Scene> scenes = new HashMap<>();

    public void switchScene(String url) {
        Scene scene = scenes.computeIfAbsent(url, u -> {
            System.out.println(u);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(u));
            try {
                Pane p = loader.load();
                BaseController controller = loader.getController();
                controller.setSceneManager(this);
                // Mange Controllers Init
                if (controller instanceof MenuController) {
                    ((MenuController) controller).initMenu();
                }
                else if (controller instanceof GuideController) {
                    ((GuideController) controller).initGuideController();
                }
                return new Scene(p);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        rootStage.setScene(scene);
    }
}