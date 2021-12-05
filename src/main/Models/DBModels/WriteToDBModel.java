package main.Models.DBModels;

import main.Models.DateTimeModel;
import main.Utility.Activity;
import java.sql.*;
import java.util.ArrayList;

public class WriteToDBModel {
    public static boolean saveArchiveProcessAtRest = true;
    static Thread backupSynchronizerThread = new Thread();

    public static void saveArchive() {
        // cloning data from arraylist: archive
        ArrayList<Activity> clonedArchive = (ArrayList<Activity>) ArchiveDBModel.archive.clone();
        // extracting date from the first element of the archive
        String day = clonedArchive.get(0).getDate();
        // reading contents of the current day in the database
        ArrayList<Activity> savedData = ReadFromDBModel.readDay(day);

        // removing any unwanted data from the database
        cleanArchive(savedData, clonedArchive);

        System.out.println("(from saveArchive) saving data...");

        // connecting to the database;


        try {

            for (Activity event : clonedArchive) {
                // saving the event if it's not in the arraylist: savedData

                if (!event.arrayListContainsActivity(savedData)) {
                    String name = event.getName();
                    String category = event.getCategory();
                    String note = event.getNote();
                    boolean noteIsPrivate = event.noteIsPrivate();
                    int startTime = event.getStartTimeSecs();
                    int endTime = event.getEndTimeSecs();
                    String date = event.getDate();

                    int activityID;

                    // setting the name to no name if its null

                    if (name.equals("")) {
                        name = "no name";
                    }

                    // adding DateTimeModel.SECONDS_IN_A_DAY to endTime if the time is above 12:00 am

                    if (!date.equals(day) && endTime < DateTimeModel.SECONDS_IN_A_DAY) {



                        if (startTime <= endTime) {
                            startTime += DateTimeModel.SECONDS_IN_A_DAY;
                        }

                        endTime += DateTimeModel.SECONDS_IN_A_DAY;

                        date = day;
                    }

                    // searching for activityID for the event name and category
                    activityID = ReadFromDBModel.getActivityID(name, category);

                    // if id was not found
                    if (activityID == -1) {
                        System.out.println("(from saveArchive) saving new activity");

                        // searching for the last id entered
                        ResultSet lastIdEntered = ArchiveDBModel.getLastIDEntered.executeQuery();

                        if (lastIdEntered.next()) {
                            activityID = lastIdEntered.getInt("activityID");
                            activityID++;
                        } else {
                            activityID = 0;
                        }

                        lastIdEntered.close();

                        // saving the new activity

                        PreparedStatement saveActivity = ArchiveDBModel.insertDataToActivitiesTable;
                        saveActivity.setInt(1, activityID);
                        saveActivity.setString(2, name);
                        saveActivity.setString(3, category);
                        saveActivity.setInt(4, 1);
                        saveActivity.execute();
                    }

                    // saving the event

                    PreparedStatement saveEvent = ArchiveDBModel.insertDataToEventsTable;
                    saveEvent.setInt(1, activityID);
                    saveEvent.setBoolean(2, noteIsPrivate);
                    saveEvent.setString(3, note);
                    saveEvent.setInt(4, startTime);
                    saveEvent.setInt(5, endTime);
                    saveEvent.setString(6, date);
                    saveEvent.execute();

                    // adding 1 to the frequency of the activity

                    PreparedStatement addFrequency = ArchiveDBModel.addFrequency;
                    addFrequency.setInt(1, activityID);
                    addFrequency.execute();

                    savedData.add(event);

                    System.out.println("(from saveArchive) event saved to optimizedArchive.db");

                } else {
                    System.out.println("(from saveArchive) event already saved");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private static void cleanArchive(ArrayList<Activity> savedData, ArrayList<Activity> newData) {

        try {
            int activityID;
            String note;
            boolean noteIsPrivate;
            int endTime;
            int startTime;
            String date;

            for (Activity event : savedData) {

                if (!event.arrayListContainsActivity(newData)) {
                    activityID = ReadFromDBModel.getActivityID(event.getName(), event.getCategory());
                    note = event.getNote();
                    noteIsPrivate = event.noteIsPrivate();
                    startTime = event.getStartTimeSecs();
                    endTime = event.getEndTimeSecs();
                    date = event.getDate();

                    if (date.equals(DateTimeModel.subDayFromDate(DateTimeModel.currentDay, 1)) && endTime < DateTimeModel.SECONDS_IN_A_DAY) {
                        startTime += DateTimeModel.SECONDS_IN_A_DAY;
                        endTime += DateTimeModel.SECONDS_IN_A_DAY;
                    }

                    // deleting events that's not in the archive
                    PreparedStatement deleteEvent = ArchiveDBModel.deleteEvent;
                    deleteEvent.setInt(1, activityID);
                    deleteEvent.setString(2, note);
                    deleteEvent.setBoolean(3, noteIsPrivate);
                    deleteEvent.setInt(4, startTime);
                    deleteEvent.setInt(5, endTime);
                    deleteEvent.setString(6, date);
                    deleteEvent.execute();

                    // subtracting 1 from the frequency the activity
                    PreparedStatement subFrequency = ArchiveDBModel.subFrequency;
                    subFrequency.setInt(1, activityID);
                    subFrequency.execute();

                    System.out.println("(from cleanArchive) removed event: " + event);

                }
            }

            // deleting all activities that's not referenced in the events table
            ArchiveDBModel.deleteActivities.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public static void saveDataSynchronously(){

        if (saveArchiveProcessAtRest){
            // just back up if no backup process is in progress
            new Thread(savingData).start();

                //do nothing though edit made if synchro is already running
        }else if (!backupSynchronizerThread.isAlive()){

            backupSynchronizerThread = new Thread(saveArchiveSynchronizer);
            backupSynchronizerThread.start();

        }
    }


    public static Runnable exitSystemSynchronously = () -> {
        System.out.println("(from exit) waiting for previous process to finish");
        //wait until previous backup process finishes
        while (!saveArchiveProcessAtRest){
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("(from exit) previous process finished. exiting system...");
        //exit system

        try {
            ArchiveDBModel.connection.close();
            SettingsDBModel.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.exit(0);
    };

    private static final Runnable saveArchiveSynchronizer = new Runnable() {
        @Override
        public void run() {
            while (!saveArchiveProcessAtRest){
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //start new data backup process
            new Thread(savingData).start();


        }
    };



    private static final Runnable savingData = new Runnable() {
        @Override
        public void run() {
            //data backup process in progress
            saveArchiveProcessAtRest = false;
            saveData();
            //data backup process finished
            saveArchiveProcessAtRest = true;
        }
    };


    private static void saveData(){

        if (!ArchiveDBModel.archive.isEmpty()){
            saveArchive();
        }
    }


    public static void updateBackupSettings(String name, int value) {

        try {
            PreparedStatement updateBackupSettings = SettingsDBModel.updateBackupSettings;
            updateBackupSettings.setInt(1, value);
            updateBackupSettings.setString(2, name);
            updateBackupSettings.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void updateBackupSettings(String name, String value) {

        try {
            PreparedStatement updateBackupSettings = SettingsDBModel.updateBackupSettings;
            updateBackupSettings.setString(1, value);
            updateBackupSettings.setString(2, name);
            updateBackupSettings.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
