package com.example.pcremotecontrol.servers;

import com.example.pcremotecontrol.MainApplication;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MailServerHelpers {
    public static String getAppsList() {
        List<String> apps = new ArrayList<>();
        try {
            // Get apps
            Process p = Runtime.getRuntime().exec
                    (System.getenv("windir") +"\\system32\\"+"tasklist.exe");
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) apps.add(line);
            reader.close();

            // Write to File
            String fileAddressApps = "/apps.txt";
            FileWriter writer = new FileWriter(fileAddressApps);
            for(String str: apps) {
                writer.write(str + System.lineSeparator());
            }
            writer.close();
            return fileAddressApps;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getServicesList() {
        List<String> services = new ArrayList<>();
        try {
            // Get services
            Process p = Runtime.getRuntime().exec("sc query type= service state= all");
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("DISPLAY_NAME:")) {
                    services.add(line.substring(14).trim());
                }
            }
            reader.close();

            // Write to File
            String fileAddressServices = "/services.txt";
            FileWriter writer = new FileWriter(fileAddressServices);
            for(String str: services) {
                writer.write(str + System.lineSeparator());
            }
            writer.close();
            return fileAddressServices;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String startApp(String appAddress) {
        try {
            Process p = Runtime.getRuntime().exec(appAddress);
            int exitCode = p.waitFor();
            if (exitCode == 0) {
                return "Successfully start: " + appAddress;
            } else {
                return "Can't start: " + appAddress;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Fail to start: " + appAddress;
        }
    }

    public static String stopApp(String appName) {
        try {
            Runtime.getRuntime().exec("taskkill /F /IM " + appName + ".exe");
            return "Successfully stop: " + appName;
        } catch (IOException e) {
            e.printStackTrace();
            return "Fail to stop: " + appName;
        }
    }

    public static String startService(String serviceName) {
        try {
            Process p = Runtime.getRuntime().exec("net start " + serviceName);
            int exitCode = p.waitFor();
            if (exitCode == 0) {
                return "Successfully start: " + serviceName;
            } else {
                return "Can't start: " + serviceName;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Fail to start: " + serviceName;
        }
    }

    public static String stopService(String serviceName) {
        try {
            Process p = Runtime.getRuntime().exec("net stop " + serviceName);
            int exitCode = p.waitFor();
            if (exitCode == 0) {
                return "Successfully stop: " + serviceName;
            } else {
                return "Can't stop: " + serviceName;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Fail to stop: " + serviceName;
        }
    }

    public static String getScreenshot() {
        try {
            // Capture the Screenshot
            Robot robot = new Robot();
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenshot = robot.createScreenCapture(screenRect);

            // Save the Screenshot to a File
            String fileAddressScreenshot = "/screenshot.jpeg";
            File bufferOutput = new File(fileAddressScreenshot);
            ImageIO.write(screenshot, "jpeg", bufferOutput);
            return fileAddressScreenshot;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String shutdownServer(String delayTime) {
        try {
            Runtime.getRuntime().exec("shutdown -s -t " + delayTime);
            return "Shutdown the server machine after: " + delayTime;
        } catch (Exception e) {
            e.printStackTrace();
            return "Fail to shutdown.";
        }
    }

    public static String restartServer(String delayTime) {
        try {
            Runtime.getRuntime().exec("shutdown -r -t " + delayTime);
            return "Restart the server machine after: " + delayTime;
        } catch (Exception e) {
            e.printStackTrace();
            return "Fail to restart.";
        }
    }

    public static String cancelServerShutdown() {
        try {
            Runtime.getRuntime().exec("shutdown -a");
            return "Successfully stop previous shutdown attempts.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Fail to stop previous shutdown attempts.";
        }
    }

    public static void logoutServer() {
        System.exit(0);
    }

    public static String startKeylogger() {
        try {
            MailServerKeylogger.getInstance().startKeylogger();
            return "Successfully start a keylogger.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Fail to start a keylogger";
        }
    }

    public static String stopKeylogger() {
        try {
            List<String> keylogger = MailServerKeylogger.getInstance().getKeyloggerResult();

            // Write to File
            String fileAddressKeylogger = "/keylogger.txt";
            FileWriter writer = new FileWriter(fileAddressKeylogger);
            for(String str: keylogger) {
                writer.write(str + System.lineSeparator());
            }
            writer.close();
            return fileAddressKeylogger;

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String collectDirectory(String folderAddress) {
        List<String> directoryChildren = new ArrayList<>();

        try {
            File folder = new File(folderAddress);
            File[] listOfFiles = folder.listFiles();

            if (listOfFiles != null) {
                for (File file: listOfFiles) {
                    directoryChildren.add(file.getPath());
                }
            }

            // Write to File
            String fileAddressDirectory = "/directory.txt";
            FileWriter writer = new FileWriter(fileAddressDirectory);
            for(String str: directoryChildren) {
                writer.write(str + System.lineSeparator());
            }
            writer.close();
            return fileAddressDirectory;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String collectFile(String fileAddress) {
        try {
            File file = new File(fileAddress);

            if (file.exists()) {
                return fileAddress;
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getFunctionsImage() throws IOException {
        String output = "/functions.jpg";
        File f = new File(output);
        if(!f.exists()) {
            initializeFunctionsImage();
        }
        return output;
    }

    private static void initializeFunctionsImage() throws IOException {
        BufferedImage image = ImageIO.read(MainApplication.class.getResourceAsStream("functions.jpg"));
        String fileAddressFunction = "/functions.jpg";
        File bufferOutput = new File(fileAddressFunction);
        ImageIO.write(image, "jpg", bufferOutput);
    }


}

