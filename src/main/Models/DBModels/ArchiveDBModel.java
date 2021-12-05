package main.Models.DBModels;

import main.App;
import main.Utility.Activity;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class ArchiveDBModel {
    public static Connection connection;
    public static String pathToDBDirectory = "/Databases/";
    public static String archiveDBName = "archive.db";
    public static String pathToArchiveDB = App.appDataDirectory + pathToDBDirectory + archiveDBName;
    public static ArrayList<Activity> archive = new ArrayList<>();

    //preparedStatement for writing data to the database
    public static PreparedStatement insertDataToActivitiesTable;
    public static PreparedStatement insertDataToEventsTable;
    public static PreparedStatement deleteEvent;
    public static PreparedStatement deleteActivities;
    public static PreparedStatement addFrequency;
    public static PreparedStatement subFrequency;

    // preparedStatements for reading data from database
    public static PreparedStatement readDayContent;
    public static PreparedStatement getAllDatesEntered;
    public static PreparedStatement getAllActivityNamesEntered;
    public static PreparedStatement findActivityID;
    public static PreparedStatement getLastIDEntered;
    public static PreparedStatement readAllFromActivitiesTable;
    public static PreparedStatement readAllFromEventsTable;
    public static PreparedStatement getAllOccurrences;
    public static PreparedStatement getSuggestions;

    // preparedStatement for creating databases

    public static void connect() {
        // getting the full path to the database
        File archiveDB = new File(pathToArchiveDB);
        String fullPathToArchiveDB = "jdbc:sqlite:" + archiveDB.getAbsolutePath();

        try {
            Class.forName("org.sqlite.JDBC");
            // connecting to the database
            connection = DriverManager.getConnection(fullPathToArchiveDB);

            // creating tables

            Statement statement = connection.createStatement();

            statement.execute(
                        "CREATE TABLE " +
                            "IF NOT EXISTS " +
                            "activities (" +
                            "activityID INTEGER, " +
                            "name TEXT, " +
                            "category TEXT, " +
                            "frequency INTEGER" +
                            ")"
            );

            statement.execute(
                        "CREATE TABLE " +
                            "IF NOT EXISTS " +
                            "events " +
                            "(" +
                            "activityID INTEGER, " +
                            "note INTEGER, " +
                            "noteIsPrivate BIT" +
                            "startTime INTEGER, " +
                            "endTime INTEGER, " +
                            "date TEXT" +
                            ")"
            );

            statement.close();


            // write statements

            insertDataToActivitiesTable = connection.prepareStatement("INSERT INTO activities VALUES (?, ?, ?, ?)");

            insertDataToEventsTable = connection.prepareStatement("INSERT INTO events VALUES (?, ?, ?, ?, ?, ?)");

            deleteEvent = connection.prepareStatement(
                        "DELETE FROM events " +
                            "WHERE activityID = ? AND note = ? AND startTime = ? AND endTime = ? AND date = ?"
            );

            deleteActivities = connection.prepareStatement("DELETE FROM activities WHERE frequency = 0");

            addFrequency = connection.prepareStatement(
                        "UPDATE activities " +
                            "SET frequency = frequency + 1 " +
                            "WHERE activityID = ?"
            );

            subFrequency = connection.prepareStatement(
                        "UPDATE activities " +
                            "SET frequency = frequency - 1 " +
                            "WHERE activityID = ?"
            );

            // read statements

            readDayContent = connection.prepareStatement(
                        "SELECT name, category, note, noteIsPrivate, startTime, endTime, endTime - startTime AS duration, date " +
                            "FROM events " +
                            "JOIN activities USING (activityID) " +
                            "WHERE date = ? ORDER BY startTime"
            );

            getAllDatesEntered = connection.prepareStatement("SELECT DISTINCT date FROM events ORDER BY date");

            getAllActivityNamesEntered = connection.prepareStatement("SELECT name FROM activities");

            findActivityID = connection.prepareStatement(
                            "SELECT activityID " +
                                "FROM activities " +
                                "WHERE name = ? AND category = ? " +
                                "LIMIT 1"
            );

            getLastIDEntered = connection.prepareStatement(
                    "SELECT activityID " +
                    "FROM activities " +
                    "ORDER BY activityID DESC " +
                    "LIMIT 1"
            );

            readAllFromActivitiesTable = connection.prepareStatement("SELECT * FROM activities");

            readAllFromEventsTable = connection.prepareStatement("SELECT * FROM events");

            getAllOccurrences = connection.prepareStatement(
                    "SELECT endTime - startTime AS duration, date " +
                    "FROM events " +
                    "JOIN activities USING (activityID) " +
                    "WHERE name = ? OR category = ?"
            );

            getSuggestions = connection.prepareStatement(
                        "SELECT DISTINCT name, " +
                            "CASE date " +
                                "WHEN ? " +
                                    "THEN frequency + 50 " +
                                    "ELSE frequency " +
                            "END AS score " +
                            "FROM activities " +
                            "JOIN events USING (activityID) " +
                            "WHERE name LIKE ? AND category LIKE ? " +
                            "ORDER BY score DESC, date "
            );

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
