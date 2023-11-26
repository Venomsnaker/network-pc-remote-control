public class Main {

    public static void main(String[] args) {
        try {
            Process p = Runtime.getRuntime().exec("powershell -command \"Get-ItemProperty HKLM:\\Software\\Wow6432Node\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\* | Select-Object DisplayName, DisplayVersion, Publisher, InstallDate | Format-Table –AutoSize\"");
            System.out.println(p);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}