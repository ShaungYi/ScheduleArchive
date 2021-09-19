package main.Models.DBModels;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.*;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BackupArchiveModel {
    public static int backupCreationIntervalInSeconds = 3600;
    //if adding a backup makes backup list grow to more than this, delete the oldest backup
    public static int maxBackupNum = 6;
    //a list of available archive backup names
    public static ObservableList<String> availableBackupsObservableList = FXCollections.observableArrayList("12:23 P.M. 1248-03-04", "04:23 P.M. 1776-02-05", "01:35 P.M. 1996-11-04");


    public static void createBackup() {
        String pattern = "(dd MMMM yyyy hh;mm)";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String backupName = simpleDateFormat.format(new Date());

        try {
            FileChannel sourceChannel = new FileInputStream(DBModel.dbName).getChannel();
            FileChannel destChannel = new FileOutputStream("Backups/" + backupName + ".db").getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
            sourceChannel.close();
            destChannel.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeBackup(String name) {
        File backup = new File("Backups/" + name);

        if (!backup.exists()) {
            System.out.println("(from removeBackup) backup not found, name: " + name);
        }

        else if(backup.delete()) {
            System.out.println("(from removeBackup) backup deleted, name: " + name);
        }
    }
}
