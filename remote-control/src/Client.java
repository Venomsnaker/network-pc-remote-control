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

            String fileAddress = Paths.get("").toAbsolutePath().toString() + "/";
            Path filePathChecker = Paths.get(fileAddress);

            int totalBytesRead = 0;
            int bytesReadThisTime = 0;
            String response;

            Scanner sc = new Scanner(System.in);
            boolean exit = false;

            while (!exit) {
                System.out.println("\n MENU");
                System.out.println("0. Exit");
                System.out.println("1. Get Apps List");
                System.out.println("2. Get Services List");
                System.out.println("3. Start App");
                System.out.println("4. Stop App");
                System.out.println("5. Start Service");
                System.out.println("6. Stop Service");
                System.out.println("7. Take Screenshot");
                System.out.println("8. Start Keylogger");
                System.out.println("9. Stop and Return Keylogger");
                System.out.println("10. Shutdown");
                System.out.println("11. Restart");
                System.out.println("12. Cancel Shutdown/ Restart");
                System.out.println("13. Collect File via Location");
                System.out.println("\nEnter your choice: ");

                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 0:
                        exit = true;
                        break;

                    case 1:
                        writer.println("get-apps");
                        writer.flush();

                        response = reader.readLine();
                        System.out.println("List of apps on the server:");
                        System.out.println(response);
                        break;

                    case 2:
                        writer.println("get-services");
                        writer.flush();

                        response = reader.readLine();
                        System.out.println("List of service on the server:");
                        System.out.println(response);
                        break;

                    case 3:
                        System.out.println("Enter the name of the app you want to start: ");
                        String appNameStart = sc.nextLine();

                        writer.println("start-app-" + appNameStart);
                        writer.flush();

                        response = reader.readLine();
                        System.out.println(response);
                        break;

                    case 4:
                        System.out.println("Enter the name of the app you want to stop: ");
                        String appNameStop = sc.nextLine();

                        writer.println("stop-app-" + appNameStop);
                        writer.flush();

                        response = reader.readLine();
                        System.out.println(response);
                        break;

                    case 5:
                        writer.println("start-service");
                        writer.flush();

                        response = reader.readLine();
                        System.out.println(response);
                        break;

                    case 6:
                        writer.println("stop-service");
                        writer.flush();

                        response = reader.readLine();
                        System.out.println(response);
                        break;

                    case 7:
                        writer.println("screenshot");
                        writer.flush();

                        int screenshotSize = Integer.parseInt(reader.readLine());
                        byte[] screenshotBytes = new byte[screenshotSize];
                        totalBytesRead = 0;
                        bytesReadThisTime = 0;

                        while (totalBytesRead < screenshotSize) {
                            bytesReadThisTime = socket.getInputStream().read(screenshotBytes, totalBytesRead, screenshotBytes.length - totalBytesRead);
                            if (bytesReadThisTime == -1) {
                                System.out.println("End of stream reached before all bytes were read.");
                                break;
                            }
                            totalBytesRead += bytesReadThisTime;
                        }

                        if (totalBytesRead >= screenshotSize) {
                            System.out.println("Enter screenshot file name: ");
                            String screenshotFileName = sc.nextLine();

                            Path screenshotFilePath = Paths.get(fileAddress + screenshotFileName);

                            if (Files.isDirectory(filePathChecker.getParent()) && Files.isWritable(filePathChecker)) {
                                Files.write(screenshotFilePath, screenshotBytes);
                                System.out.println("Server screenshot saved to: " + screenshotFilePath);
                            } else {
                                System.out.println("Invalid file address or write permissions denied.");
                            }
                        } else {
                            System.out.println("Incomplete screenshot transfer.");
                        }
                        break;

                    case 8:
                        writer.println("start-keylogger");
                        writer.flush();
                        System.out.println(reader.readLine());
                        break;

                    case 9:
                        writer.println("stop-keylogger");
                        writer.flush();

                        response = reader.readLine();
                        byte[] bytes = response.getBytes();

                        Path filePathKeylogger = Paths.get(fileAddress + "/keylogger.txt");

                        if (Files.isDirectory(filePathChecker.getParent()) && Files.isWritable(filePathChecker)) {
                            Files.write(filePathKeylogger, bytes);
                                System.out.println("A new file has been added to: " + fileAddress);
                        } else {
                            System.out.println("Invalid file address or write permissions denied.");
                        }

                        break;

                    case 10:
                        writer.println("shutdown");
                        writer.flush();
                        System.out.println(reader.readLine());
                        break;

                    case 11:
                        writer.println("restart");
                        writer.flush();
                        System.out.println(reader.readLine());
                        break;

                    case 12:
                        writer.println("cancel-shutdown");
                        writer.flush();
                        System.out.println(reader.readLine());
                        break;

                    case 13:
                        System.out.println("Insert the server path to the file: ");
                        String filePathServer = sc.nextLine();
                        writer.println("collect-" + filePathServer);
                        writer.flush();

                        int fileSize = Integer.parseInt(reader.readLine());
                        byte[] fileBytes = new byte[fileSize];
                        totalBytesRead = 0;
                        bytesReadThisTime = 0;

                        while (totalBytesRead < fileSize) {
                            bytesReadThisTime = socket.getInputStream().read(fileBytes, totalBytesRead, fileBytes.length - totalBytesRead);
                            if (bytesReadThisTime == -1) {
                                System.out.println("End of stream reached before all bytes were read.");
                                break;
                            }
                            totalBytesRead += bytesReadThisTime;
                        }

                        if (totalBytesRead == fileSize) {
                            System.out.println("Insert file name: ");
                            String fileName = sc.nextLine();

                            Path filePathUser = Paths.get(fileAddress + fileName);

                            if (Files.isDirectory(filePathChecker.getParent()) && Files.isWritable(filePathChecker)) {
                                Files.write(filePathUser, fileBytes);
                                System.out.println("A new file has been added to: " + fileAddress);
                            } else {
                                System.out.println("Invalid file address or write permissions denied.");
                            }
                        } else {
                            System.out.println("Incomplete file transfer.");
                        }
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
