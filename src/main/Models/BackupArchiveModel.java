package main.Models;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.App;
import main.Models.DBModels.ArchiveDBModel;
import main.Models.DBModels.ReadFromDBModel;
import main.Models.DBModels.SettingsDBModel;
import main.Models.DBModels.WriteToDBModel;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class BackupArchiveModel {

    public static String backupFolderName = "/backups/";
    public static String pathToBackupsFolder = App.appDataDirectory + backupFolderName;

    // the amount of delay before create a backup
    public static int backupCreationIntervalInSeconds;

    // the maximum number of backups that can exist at the same time
    public static int maxBackupNum;

    //a list of available archive backup names
    public static ObservableList<String> availableBackupsObservableList = FXCollections.observableArrayList();

    public static String parsedDateFormat = "EEE MMM dd hh:mm:ss Z yyyy";
    public static String formattedDateFormat = "yyyy,MM,dd,hh,mm,ss";


    public static void loadSettings() {

        // loading saved settings from the settings database
        backupCreationIntervalInSeconds = ReadFromDBModel.getBackupSettings(SettingsDBModel.backupCreationInterval);
        maxBackupNum = ReadFromDBModel.getBackupSettings(SettingsDBModel.maxBackupNum);
    }


    public static Runnable createBackupArchiveDBRegularly = () -> {

        //run while app is on
        while (true){

            try {

                //wait until it's time to create the next backup
                for (int i = backupCreationIntervalInSeconds; i >= 0; i--){

                    Thread.sleep(1000);
                }

                if (!WriteToDBModel.saveArchiveProcessAtRest){
                    //just create backup
                    createBackup();
                } else {
                    //wait until data backup process finished then create backup to prevent DB corruption
                    while (WriteToDBModel.saveArchiveProcessAtRest){
                        Thread.sleep(10);
                    }

                    createBackup();

                }

                removeOldBackups();

                //catch any exception from Thread.sleep()
            } catch (InterruptedException e){
                e.printStackTrace();
            }

        }

    };


    public static void createBackup() {
        String backupName = formatBackupName(new Date());
        FileManagementModel.copyPasteFile(ArchiveDBModel.pathToArchiveDB, pathToBackupsFolder + backupName);
        updateBackupsObservableList();
        System.out.println("(from createBackup) successfully created backup: " + backupName);
    }


    public static void removeBackup(String name) {
        FileManagementModel.deleteFile(pathToBackupsFolder + name);
        updateBackupsObservableList();
    }


    public static void loadBackup(String backupName) {
        createBackup();

        Date parsedBackupName = parseBackupName(backupName, formattedDateFormat);
        String formattedBackupName = formatBackupName(parsedBackupName);
        FileManagementModel.copyPasteFile(pathToBackupsFolder + formattedBackupName, ArchiveDBModel.pathToArchiveDB);

        BackupArchiveModel.removeBackup(formattedBackupName);
        ArchiveDBModel.archive = new ArrayList<>();
        DateTimeModel.selectedDay = LocalDate.now().toString();

        removeOldBackups();
        System.out.println("(from loadBackup) successfully loaded backup: " + backupName);
    }


    public static ArrayList<Date> listBackups() {
        ArrayList<Date> parsedBackupList = new ArrayList<>();

        File backupDirectory = new File(pathToBackupsFolder);
        String[] backupList = backupDirectory.list();
        assert backupList != null;

        for (String backupName : backupList) {
            Date parsedBackupName = parseBackupName(backupName, formattedDateFormat);
            parsedBackupList.add(parsedBackupName);
        }

        parsedBackupList.sort(Comparator.naturalOrder());

        return parsedBackupList;
    }


    public static void removeOldBackups() {
        Date oldestBackup;

        while (listBackups().size() > maxBackupNum) {
            oldestBackup = new Date();

            for (Date backup : listBackups()) {

                if (backup.compareTo(oldestBackup) < 0) {
                    oldestBackup = backup;
                }
            }

            removeBackup(formatBackupName(oldestBackup));

        }
    }

    public static void updateBackupsObservableList() {

        Platform.runLater(() -> {
            availableBackupsObservableList.clear();

            for (int index = listBackups().size() - 1; index > 0; index--) {
                availableBackupsObservableList.add(listBackups().get(index).toString());
            }
        });
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
