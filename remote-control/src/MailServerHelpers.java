import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MailServerHelpers {
    private static String fileAddress = Paths.get("").toAbsolutePath().toString() + "/";
    private static Path filePathChecker = Paths.get(fileAddress);

    public static String getAppsList() {
        List<String> apps = new ArrayList<>();
        try {
            // Get apps
            Process p = Runtime.getRuntime().exec
                    (System.getenv("windir") +"\\system32\\"+"tasklist.exe");
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) apps.add(line);
            reader.close();

            // Write to File
            String fileAddressApps = fileAddress + "/apps.txt";
            FileWriter writer = new FileWriter(fileAddressApps);
            for(String str: apps) {
                writer.write(str + System.lineSeparator());
            }
            writer.close();
            return fileAddressApps;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
