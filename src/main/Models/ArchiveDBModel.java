package main.Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.Controllers.Loader.Loader;
import main.Utility.Activity;
import java.io.File;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class ArchiveDBModel {
    public static String pathToArchive = "Database/optimizedArchive.db";
    public static ArrayList<Activity> archive = new ArrayList<>();
    public static String selectedDay;
    public static String currentDay = LocalDate.now().toString();
    static boolean dataBackupProcessAtRest = true;
    static Thread backupSyncronizerThread = new Thread();
    public static ArrayList<String> allDates = getAllDates();
    public static String lastDay = allDates.get(getAllDates().size() - 1);


    //backup screen parameters

    //create backup every this seconds
    public static int backupCreationIntervalInSeconds = 3600;
    //if adding a backup makes backup list grow to more than this, delete the oldest backup
    public static int maxBackupNum = 6;
    //a list of available archive backup names
    public static ObservableList<String> availableBackupsObservableList = FXCollections.observableArrayList();

    public static String getArchiveUrl(){

        File file = new File("Database/archive.db");
        //System.out.println(file.getAbsolutePath());
        return ("jdbc:sqlite:"+file.getAbsolutePath());
    }


    public static String getPathToDB(String path){
        File file = new File(path);
        return ("jdbc:sqlite:"+file.getAbsolutePath());
    }

    public static ArrayList getAllDates(){
        ArrayList<String> allDates = new ArrayList<>();

        try {
            Class.forName("org.sqlite.JDBC");
            // connecting to the database
            Connection db = DriverManager.getConnection(getPathToDB(pathToArchive));
            Statement dbStatement = db.createStatement();
            // searching for all dates in the database
            ResultSet dates = dbStatement.executeQuery("SELECT DISTINCT date FROM events");

            // extracting dates from the result set and adding them to an arraylist
            while (dates.next()) {
                allDates.add(dates.getString("date"));
            }
        }

        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return allDates;
    }


    public static ArrayList<String> getAllActivityNames() {
        ArrayList<String> activityNamesList = new ArrayList<>();

        try {
            Class.forName("org.sqlite.JDBC");
            // connecting to the database
            Connection db = DriverManager.getConnection(getPathToDB(pathToArchive));
            Statement dbStatement = db.createStatement();
            ResultSet activityNames = dbStatement.executeQuery(
                    "SELECT name " +
                        "FROM activities"
            );

            while (activityNames.next()) {
                activityNamesList.add(activityNames.getString("name"));
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return activityNamesList;
    }

    public static ArrayList<Activity> readDay(String date) {
        ArrayList<Activity> newArchive = new ArrayList<>();

        try {
            Class.forName("org.sqlite.JDBC");
            // connecting to the database
            Connection db = DriverManager.getConnection(getPathToDB(pathToArchive));
            Statement dbStatement = db.createStatement();

            String name;
            String category;
            int duration;
            int starTime;
            int endTime;
            String eventDate;

            int previousEndTime = 0;

            // Searching for the given date in the database
            ResultSet day = dbStatement.executeQuery(
                        "SELECT name, category, endTime, date " +
                            "FROM events " +
                            "JOIN activities USING (activityId) " +
                            "WHERE date = '" + date + "' ORDER BY endTime"
            );

            while (day.next()){
                // extracting data from events
                name = day.getString("name");
                category = day.getString("category");
                endTime = day.getInt("endTime");

                // Setting previous endTime to the start time of the day if the event name is DayStart
                if (name.equals("DayStart") && category.equals("System")) {
                    previousEndTime = endTime;
                } else {
                    starTime = previousEndTime;
                    duration = endTime - starTime;
                    previousEndTime = endTime;

                    // subtracting 86400 from startTime and endTime when the time is above 12:00 am
                    if (starTime < 86400) {
                        eventDate = date;
                    } else {
                        eventDate = addDayToDate(date, 1);
                        starTime -= 86400;
                        endTime -= 86400;
                    }

                    // saving the event to an arraylist
                    newArchive.add(new Activity(name, category, duration, starTime, endTime, eventDate));
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return newArchive;

    }


    public static void saveArchive() {
        // cloning data from arraylist: archive
        ArrayList<Activity> clonedArchive = (ArrayList<Activity>) archive.clone();
        // extracting date from the first element of the archive
        String day = clonedArchive.get(0).getDate();
        // reading contents of the current day in the database
        ArrayList<Activity> savedData = readDay(day);

        cleanArchive(savedData, clonedArchive);

        // adding DayStart event to the clonedArchive if the savedData is empty
        if (savedData.isEmpty()) {
            clonedArchive.add(new Activity(
                    "DayStart",
                    "System",
                    0,
                    0,
                    clonedArchive.get(0).getStartTimeSecs(),
                    day
            ));
        }

        System.out.println("(from saveArchive) saving data...");

        for (Activity event : clonedArchive) {

            // saving the event if it's not in the arraylist: savedData

            if (!event.arrayListContainsActivity(savedData)) {

                try {
                    Class.forName("org.sqlite.JDBC");
                    Connection db = DriverManager.getConnection(getPathToDB(pathToArchive));
                    Statement dbStatement = db.createStatement();

                    String name = event.getName();
                    String category = event.getCategory();
                    int endTime = event.getEndTimeSecs();
                    String date = event.getDate();

                    int activityId;

                    // adding 86400 to endTime if the time is above 12:00 am

                    if (!date.equals(day) && endTime < 86400) {
                        endTime += 86400;
                    }

                    // searching for activityId for the event name and category
                    activityId = getActivityId(name, category);

                    // if id was not found
                    if (activityId == -1) {
                        System.out.println("(from saveArchive) saving new activity");

                        // searching for the last id entered
                        ResultSet lastIdEntered = dbStatement.executeQuery(
                                        "SELECT activityId " +
                                            "FROM activities " +
                                            "ORDER BY activityId DESC " +
                                            "LIMIT 1"
                        );

                        lastIdEntered.next();
                        activityId = lastIdEntered.getInt("activityId");
                        activityId++;

                        // saving the new activity

                        dbStatement.execute("INSERT INTO activities " +
                                "VALUES (" + activityId + ", '" + name + "', '" + category + "')"
                        );
                    }

                    // saving the event

                    dbStatement.execute("INSERT INTO events VALUES (" + activityId + ", '" + endTime + "', '" + date + "')");

                    dbStatement.close();
                    db.close();
                    System.out.println("(from saveArchive) event saved to optimizedArchive.db");

                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

            } else {
                System.out.println("(from saveArchive) event already saved");
            }
        }
    }



    private static void cleanArchive(ArrayList<Activity> savedData,ArrayList<Activity> newData) {

        try {
            Class.forName("org.sqlite.JDBC");
            Connection db = DriverManager.getConnection(getPathToDB(pathToArchive));
            Statement dbStatement = db.createStatement();

            int endTime;

            for (Activity event : savedData) {

                if (!event.arrayListContainsActivity(newData)) {
                    endTime = event.getEndTimeSecs();

                    if (addDayToDate(currentDay, 1).equals(event.getDate())) {
                        endTime += 86400;
                    }

                    // deleting events that's not in the archive
                    dbStatement.execute(
                                    "DELETE FROM events " +
                                        "WHERE endTime = " + endTime + " AND date = '" + currentDay + "' "
                    );

                    System.out.println("(from cleanArchive) removed event: " + event);

                }
            }

            // deleting all activities that's not referenced in the events table
            dbStatement.execute(
                    "DELETE FROM activities " +
                            "WHERE activityId NOT IN (" +
                            "SELECT activityId " +
                            "FROM events" +
                            ")"
            );

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static int getActivityId(String name, String category) {
        int activityId = -1;

        try {
            Class.forName("org.sqlite.JDBC");
            Connection db = DriverManager.getConnection(getPathToDB(pathToArchive));
            Statement dbStatement = db.createStatement();

            // searching for the activityId of the given name and category
            ResultSet searchId = dbStatement.executeQuery(
                        "SELECT activityId " +
                            "FROM activities " +
                            "WHERE name = '" + name + "' AND category = '" + category + "' " +
                            "LIMIT 1"
            );

            db.close();

            // if id was found
            if (searchId.next()) {
                activityId = searchId.getInt("activityId");
            }

        } catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }

        return activityId;

    }

    public static void createBackup(String name) {

        try {
            Class.forName("org.sqlite.JDBC");
            Connection backupDB = DriverManager.getConnection(getPathToDB("Backups/" + name));
            Statement backupStatement = backupDB.createStatement();
            backupStatement.execute("CREATE TABLE IF NOT EXISTS activities (activityId INTEGER, name TEXT, category TEXT)");
            backupStatement.execute("CREATE TABLE IF NOT EXISTS events (activityId INTEGER, endTime INTEGER, date TEXT)");
            backupStatement.execute("DELETE FROM activities");
            backupStatement.execute("DELETE FROM event");
            backupStatement.close();
            backupDB.close();
            System.out.println("(from createBackup) created backup, name: " + name);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeBackup(String name) {
        File file = new File("Backups/" + name);

        if (!file.exists()) {
            System.out.println("(from removeBackup) backup not found, name: " + name);
        }

        else if(file.delete()) {
            System.out.println("(from removeBackup) backup deleted, name: " + name);
        }
    }

    public static String addDayToDate(String date, int amount) {
        String[] intDate = date.split("-");
        LocalDate nextDay = LocalDate.of(Integer.valueOf(intDate[0]), Integer.valueOf(intDate[1]), Integer.valueOf(intDate[2])).plusDays(amount);
        return String.valueOf(nextDay);
    }


    public static void backupDataSyncronously(){

        if (dataBackupProcessAtRest){
            // just back up if no backup process is in progress
            //System.out.println("(from syncro)just start data backup thread when resting");
            new Thread(backingUpData).start();

                //do nothing though edit made if syncro is already running
        }else if (!backupSyncronizerThread.isAlive()){

            backupSyncronizerThread = new Thread(backupSyncronizer);
            backupSyncronizerThread.start();

        }
    }


    private static Runnable backupSyncronizer = new Runnable() {
        @Override
        public void run() {
            //System.out.println("(from syncro)waiting for previous process to finish");
            //wait until previous backup process finishes
            while (!dataBackupProcessAtRest){
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //System.out.println("(from syncro)previous process finished. Starting new process...");
            //start new data backup process
            new Thread(backingUpData).start();


        }
    };



    private static Runnable backingUpData = new Runnable() {
        @Override
        public void run() {
            //System.out.println("(from backingUpData runnable)data backup process started");
            //data backup process in progress
            dataBackupProcessAtRest = false;
            backupData();
            //data backup process finished
            //System.out.println("(from backingUpData runnable)data backup process finished");
            dataBackupProcessAtRest = true;
        }
    };

    public static Runnable exitSystemSyncronously = () -> {
        System.out.println("(from exit) waiting for previous process to finish");
        //wait until previous backup process finishes
        while (!dataBackupProcessAtRest){
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("(from exit)previous process finished. exiting system...");
        //exit system
        System.exit(0);

    };


    private static void backupData(){

        if (archive.isEmpty()){
            return;
        }

        if (!Loader.loadMode){
            saveArchive();
        }
    }


    public static String toTableName(String day){
        return "y" + day.replaceAll("-", "_");
    }


    public static String getPreviousDate(String date) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Get a Date object from the date string
        java.util.Date myDate = dateFormat.parse(date);

        // this calculation may skip a day (Standard-to-Daylight switch)...
        //oneDayBefore = new Date(myDate.getTime() - (24 * 3600000));

        // if the Date->time xform always places the time as YYYYMMDD 00:00:00
        //   this will be safer.
        java.util.Date oneDayBefore = new Date(myDate.getTime() - 2);

        return dateFormat.format(oneDayBefore);
    }


    //backup screen code

    public static Runnable createBackupArchiveDBRegularly = () -> {



        //run while app is on
        while (true){


            try {
                if (dataBackupProcessAtRest){
                    //just create backup
                    createBackupArchive();
                } else {
                    //wait until data backup process finished then create backup to prevent DB corruption
                    while (!dataBackupProcessAtRest){
                        Thread.sleep(10);
                    }
                    createBackupArchive();
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


    //implement this
    public static void createBackupArchive(){
        System.out.println("(from ArchiveDBModel.createBackupArchive) backup archive created");
    }
}
