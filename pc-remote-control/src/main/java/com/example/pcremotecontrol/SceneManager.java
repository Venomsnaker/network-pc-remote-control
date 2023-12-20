package com.example.pcremotecontrol;

import com.example.pcremotecontrol.controllers.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource(u));
            try {
                Pane p = loader.load();
                BaseController controller = loader.getController();
                // Mange Controllers Init
                if (controller instanceof MenuController) {
                    ((MenuController) controller).initMenu();
                }
                else if (controller instanceof GuideController) {
                    ((GuideController) controller).initGuide();
                }
                else if (controller instanceof FunctionController) {
                    ((FunctionController) controller).initFunction();
                }
                else if (controller instanceof CreditController) {
                    ((CreditController) controller).initCredit();
                }
                else if (controller instanceof ServerController) {
                    ((ServerController) controller).initServer();
                }
                else if (controller instanceof MailLibraryController) {
                    ((MailLibraryController) controller).initMailLibrary();
                }
                else if (controller instanceof AppLibraryController) {
                    ((AppLibraryController) controller).initAppLibrary();
                }
                controller.setSceneManager(this);
                Scene newScene = new Scene(p);
                newScene.setUserData(controller);
                return newScene;
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        rootStage.setScene(scene);
        BaseController controller = (BaseController) scene.getUserData();
        if (controller != null) {
            controller.updateServerStageUI();
        }
    }
}