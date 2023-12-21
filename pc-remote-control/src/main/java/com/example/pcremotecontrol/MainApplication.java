package com.example.pcremotecontrol;

import com.example.pcremotecontrol.servers.MailServer;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Pair;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.*;

public class MainApplication extends Application {
    static private Map<String, String> appsSaved = new HashMap<>();
    static private List<String> mailSaved = new ArrayList<>();
    private static Queue<String[]> requests = new LinkedList<>();
    private boolean serverFlag = false;
    private Pair<String, String> serverInfo = new Pair<>("g4.22tnt1.hcmus@gmail.com", "xpfabvasrrgbqmta");
    private static MainApplication instance;

    public static MainApplication getInstance() {
        return instance;
    }

    private void setServerFlag(boolean flag) {
        serverFlag = flag;
    }
    public void startServer() {
        setServerFlag(true);

        new Thread(() -> {
            while(getServerFlag()) {
                try {
                    requests = MailServer.downloadEmails();
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
                if (!requests.isEmpty()) {
                    String[] tmp = requests.poll();
                    if (tmp != null) {
                        String[] respondContent = new String[0];
                        try {
                            respondContent = MailServer.processMail(tmp);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        MailServer.sendMail(respondContent);
                    }
                }
            }
        }).start();
    }
    public void stopServer() {
        setServerFlag(false);
    }
    public boolean getServerFlag() {
        return serverFlag;
    }

    public void updateServerInfo(String mail, String password) {
        serverInfo = new Pair<>(mail, password);
    }
    public Pair<String, String> getServerInfo() {
        return serverInfo;
    }

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

    @Override
    public void start(Stage stage) {
        SceneManager sceneManager = new SceneManager(stage);
        sceneManager.switchScene("menu-view.fxml");
        stage.setTitle("PC Remote Control");
        stage.setResizable(false);
        stage.getIcons().add(new Image(MainApplication.class.getResourceAsStream("logo.png")));
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