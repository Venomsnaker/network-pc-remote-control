import java.awt.*;
import java.io.File;

public class Main {

    public static void main(String[] args) {
        try {
            // Change the path to the path of your .exe file
            Desktop desktop = Desktop.getDesktop();
            desktop.open(new File("C:\\Program Files\\HWiNFO\\HWiNFO64.exe"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}