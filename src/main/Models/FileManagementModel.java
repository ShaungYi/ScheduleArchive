package main.Models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileManagementModel {

    public static void deleteFile(String path) {
        File backup = new File(path);

        if (!backup.exists()) {
            System.out.println("(from deleteFile) file not found, name: " + path);
        }

        else if(backup.delete()) {
            System.out.println("(from deleteFile) successfully deleted file: " + path);
        }
    }


    public static void createFolder(String path) {
        File newFile = new File(path);

        if (newFile.mkdir()) {
            System.out.println("(from createFolder) successfully created folder: " + path);

        } else {
            System.out.println("(from createFolder) failed to create folder");
        }
    }


    public static void copyPasteFile(String source, String destination) {

        try {
            FileChannel sourceChannel = new FileInputStream(source).getChannel();
            FileChannel destChannel = new FileOutputStream(destination).getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
            sourceChannel.close();
            destChannel.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
