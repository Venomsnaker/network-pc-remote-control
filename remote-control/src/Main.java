import java.io.File;

public class Main {
    public static void main(String[] args) {
        // Specify the directory
        File folder = new File("D:\\DarkSoulsIII");

        // Get a list of all items in the directory
        File[] listOfFiles = folder.listFiles();

        // Print the name of each item
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                System.out.println(file.getName());
            }
        } else {
            System.out.println("The specified directory does not exist or an I/O error occurred.");
        }
    }
}
