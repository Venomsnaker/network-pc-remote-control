package features.power;
import java.io.IOException;

public class SRS {
    public static void shutdown() {
        try {
            String os = System.getProperty("os.name").toLowerCase();

            Runtime.getRuntime().exec("shutdown -s");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void restart() {
        try {
            String os = System.getProperty("os.name").toLowerCase();

            Runtime.getRuntime().exec("shutdown -r");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void logout(){
        try {
            String os = System.getProperty("os.name").toLowerCase();

            Runtime.getRuntime().exec("shutdown -l");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

