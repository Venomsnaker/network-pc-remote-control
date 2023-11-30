import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientClean {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 5000);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            Scanner sc = new Scanner(System.in);
            boolean exit = false;

            while (!exit) {
                int choice = sc.nextInt();

                if (choice == 0) {
                    exit = true;
                    break;
                } else if (choice == 1) {
                    writer.println("shutdown");
                    writer.flush();
                    break;
                } else if (choice == 2) {
                    writer.println("cancel-shutdown");
                    writer.flush();
                    break;
                }
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
