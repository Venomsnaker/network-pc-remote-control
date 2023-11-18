import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try {
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
                    Runtime.getRuntime().exec("shutdown -s -t 3600");
                    writer.println("The computer is being shutdown.");
                    writer.flush();
                } else if (request.equals("restart")) {
                    Runtime.getRuntime().exec("shutdown -r -t 3600");
                    writer.println("The computer is being restarted.");
                    writer.flush();
                }
                else if (request.equals("cancel")) {
                    Runtime.getRuntime().exec("shutdown -a");
                    writer.println("The shutdown command is cancelled.");
                    writer.flush();
                }
                else if (request.equals("screenshot")) {
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
                }
            }
        } catch (Exception e) {

        }
    }
}
