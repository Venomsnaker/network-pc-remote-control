package com.example.pcremotecontrol;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Compress {
    public static void zip(String sourcePath, String destinationPath) throws IOException {
        FileOutputStream fos = new FileOutputStream(destinationPath);
        ZipOutputStream zipOut = new ZipOutputStream(fos);

        File fileToZip = new File(sourcePath);
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
        zipOut.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;
        while((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }

        zipOut.close();
        fis.close();
        fos.close();

    }

    public static void multipleZip(String[] sourcePaths, String destinationPath) throws IOException {
        final FileOutputStream fos = new FileOutputStream(destinationPath);
        ZipOutputStream zipOut = new ZipOutputStream(fos);

        for (String sourcePath : sourcePaths) {
            File fileToZip = new File(sourcePath);
            FileInputStream fis = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            fis.close();
        }

        zipOut.close();
        fos.close();
    }

    public static void main(String[] args) throws IOException {
        String[] tmp = {"C:\\Users\\Quynh Chi\\Documents\\MainClass.class", "C:\\Users\\Quynh Chi\\Documents\\MainClass.java"};
        Compress.multipleZip(tmp, "C:\\Users\\Quynh Chi\\Documents\\test.zip");
    }
}
