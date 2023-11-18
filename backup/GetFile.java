package features.getfile;
import java.io.*;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.Scanner;

public class GetFile {public ArrayList<String> diskList;
    private Process proc = null;
    private Runtime rt = Runtime.getRuntime();
    private String disk;
    private String path;
    Scanner sc;
    public GetFile(){
        this.sc = new Scanner(System.in);
    }

    public String getPath(){
        return path;
    }

    public void getDiskList(){
        System.out.println(diskList);
    }

    private ArrayList<String> driveList(){
        File[] computerDrives = File.listRoots();
        ArrayList<String> output = new ArrayList<String>();
        for (File drive : computerDrives) {
            output.add(drive.toString());
        }
        return output;
    }
    public void chooseDrive(){
        System.out.println(driveList());
        System.out.println("Chon o dia can lay: ");
        String pos = sc.nextLine();
        path = pos;
    }
    public ArrayList<String> fileList(String path){
        ArrayList<String> output = new ArrayList<String>();
        File folder = new File(path);
        for (File x: folder.listFiles()){
            if (x.isFile() || x.isDirectory()) {
                int pos = x.toString().lastIndexOf("\\");
                output.add(x.toString().substring(pos + 1));
            }
        }
        return output;
    }

    private void listFile(){
        for (String x:fileList(this.path)){
            System.out.print(x+" ");
        }
        System.out.println();
    }

    private String removePath(String path){
        int pos = path.lastIndexOf("\\");
        return path.substring(0,pos);
    }
    private void choosePath(String pathName) {
        if (pathName=="..") {
            path = removePath(path);
        }
        else {
            if (path.length()>3){
                path+= "\\" + pathName;
            }
            else {
                path += pathName;
            }
        }
    }

    public File getFile(){
        listFile();
        System.out.println("Chon file can lay: ");
        String pathName = sc.nextLine();
        choosePath(pathName);
        File file = new File(path);
        while (file.isDirectory()){
            System.out.println("Chon file can lay: ");
            listFile();
            pathName = sc.nextLine();
            choosePath(pathName);
            file = new File(path);
        }
        sc.close();
        return file;
    }
}
