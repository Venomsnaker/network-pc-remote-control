import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerClean {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            Socket socket = serverSocket.accept();
            System.out.println("Client connected: " + socket.getInetAddress().getHostAddress());

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            String line;
            while((line = reader.readLine()) != null) {
                if (line.equals("shutdown")) {
                    System.out.println("Shutdown command received");
                    // Implement your shutdown logic here
                    break;
                } else if (line.equals("cancel-shutdown")) {
                    System.out.println("Cancel shutdown command received");
                    // Implement your cancel shutdown logic here
                    break;
                }
            }

            socket.close();
            serverSocket.close();

        } catch(Exception e) {
            e.printStackTrace();
        }


    }
}
