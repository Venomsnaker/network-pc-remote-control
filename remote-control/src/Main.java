import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            String serviceName = "MySQL82";
            Process process = Runtime.getRuntime().exec("net start " + serviceName);
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Service started successfully.");
            } else {
                System.out.println("Failed to start service. Exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}