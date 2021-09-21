package main.Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.Models.DBModels.DBModel;
import main.Models.DBModels.WriteToDBModel;

import java.io.*;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class BackupArchiveModel {
    public static int backupCreationIntervalInSeconds = 3600;
    //if adding a backup makes backup list grow to more than this, delete the oldest backup
    public static int maxBackupNum = 6;
    //a list of available archive backup names
    public static ObservableList<String> availableBackupsObservableList = FXCollections.observableArrayList();

    public static String parsedDateFormat = "EEE MMM dd hh:mm:ss Z yyyy";
    public static String formattedDateFormat = "yyyy,MM,dd,hh,mm,ss";


    public static Runnable createBackupArchiveDBRegularly = () -> {

        //run while app is on
        while (true){


            try {

                if (WriteToDBModel.dataBackupProcessAtRest){
                    //just create backup
                    createBackup();
                } else {
                    //wait until data backup process finished then create backup to prevent DB corruption
                    while (!WriteToDBModel.dataBackupProcessAtRest){
                        Thread.sleep(10);
                    }
                    createBackup();
                }


                //wait until it's time to create the next backup
                for (int i = backupCreationIntervalInSeconds; i >= 0; i--){

                    Thread.sleep(1000);

                }

                //catch any exception from Thread.sleep()
            } catch (InterruptedException e){
                e.printStackTrace();
            }

        }

    };


    private static void copyPasteFile(String source, String destination) {

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


    public static void createBackup() {
        String backupName = formatBackupName(new Date());
        copyPasteFile(DBModel.dbName, "Backups/" + backupName);
        System.out.println("(from createBackup) successfully created backup: " + backupName);



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


    public static void loadBackup(String backupName) {
        createBackup();
        Date parsedBackupName = parseBackupName(backupName, formattedDateFormat);
        String formattedBackupName = formatBackupName(parsedBackupName);
        copyPasteFile("Backups/" + formattedBackupName, DBModel.dbName);
        System.out.println("(from loadBackup) successfully loaded backup: " + backupName);

    }


    public static ObservableList<String> listBackups() {
        ObservableList<String> parsedBackupList = FXCollections.observableArrayList();

        File backupDirectory = new File("Backups");
        String[] backupList = backupDirectory.list();

        for (int index = 0; index < backupList.length; index++) {
            String backupName = backupList[index];
            Date parsedBackupName = parseBackupName(backupName, formattedDateFormat);
            parsedBackupList.add(parsedBackupName.toString());
        }

        return parsedBackupList;

    }



    public static String formatBackupName(Date parsedBackupName) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy,MM,dd,hh,mm,ss");
        String formattedBackupName = dateFormat.format(parsedBackupName);
        return formattedBackupName;
    }

    public static Date parseBackupName(String formattedBackupName, String pattern) {

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            Date parsedBackupName = dateFormat.parse(formattedBackupName);
            return parsedBackupName;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;

    }

}
