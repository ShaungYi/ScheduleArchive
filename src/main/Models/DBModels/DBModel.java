package main.Models.DBModels;

import main.Utility.Activity;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class DBModel {
    public static String dbName = "Database/archive.db";
    public static ArrayList<Activity> archive = new ArrayList<>();
    public static Connection connection;

    //preparedStatement for writing data to the database
    public static PreparedStatement insertDataToActivitiesTable;
    public static PreparedStatement insertDataToEventsTable;
    public static PreparedStatement insertDataToFrequenciesTable;
    public static PreparedStatement deleteEvent;
    public static PreparedStatement deleteActivities;
    public static PreparedStatement deleteFrequencies;
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
    public static PreparedStatement getNameSuggestions;

    // preparedStatement for creating databases

    public static void connect(String name) {
        // getting the full path to the database
        File dbFile = new File(name);
        String path = "jdbc:sqlite:" + dbFile.getAbsolutePath();

        try {
            Class.forName("org.sqlite.JDBC");
            // connecting to the database
            connection = DriverManager.getConnection(path);

            // creating tables

            Statement statement = connection.createStatement();

            statement.execute("CREATE TABLE IF NOT EXISTS activities (activityID INTEGER, name TEXT, category TEXT)");
            statement.execute("CREATE TABLE IF NOT EXISTS events (activityID INTEGER, startTime INTEGER, endTime INTEGER, date TEXT)");
            statement.execute("CREATE TABLE IF NOT EXISTS frequencies (activityID INTEGER, frequency INTEGER)");

            statement.close();


            // write statements

            insertDataToActivitiesTable = connection.prepareStatement("INSERT INTO activities VALUES (?, ?, ?)");

            insertDataToEventsTable = connection.prepareStatement("INSERT INTO events VALUES (?, ?, ?, ?)");

            insertDataToFrequenciesTable = connection.prepareStatement("INSERT INTO frequencies VALUES (?, ?)");

            deleteEvent = connection.prepareStatement(
                        "DELETE FROM events " +
                            "WHERE activityID = ? AND startTime = ? AND endTime = ? AND date = ?"
            );

            deleteActivities = connection.prepareStatement("DELETE FROM activities WHERE activityID NOT IN (SELECT activityID FROM events)");

            deleteFrequencies = connection.prepareStatement("DELETE FROM frequencies WHERE frequency = 0");

            addFrequency = connection.prepareStatement(
                        "UPDATE frequencies " +
                            "SET frequency = frequency + 1 " +
                            "WHERE activityID = ?"
            );

            subFrequency = connection.prepareStatement(
                        "UPDATE frequencies " +
                            "SET frequency = frequency - 1 " +
                            "WHERE activityID = ?"
            );

            // read statements

            readDayContent = connection.prepareStatement(
                        "SELECT name, category, startTime, endTime, endTime - startTime AS duration, date " +
                            "FROM events " +
                            "JOIN activities USING (activityID) " +
                            "WHERE date = ? ORDER BY startTime"
            );

            getAllDatesEntered = connection.prepareStatement("SELECT DISTINCT date FROM events");

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
                    "SELECT endTime - startTime AS duration, date, startTime " +
                    "FROM events " +
                    "JOIN activities USING (activityID) " +
                    "WHERE name = ? "
            );

            getNameSuggestions = connection.prepareStatement(
                        "SELECT DISTINCT name, " +

                            "CASE date " +
                                "WHEN ? " +
                                    "THEN frequency + 50 " +
                                    "ELSE frequency " +
                            "END AS point " +

                            "FROM activities " +
                            "JOIN frequencies USING (activityID) " +
                            "JOIN events USING (activityID) " +
                            "WHERE name LIKE ? " +
                            "ORDER BY point DESC " +
                            "LIMIT 50"
            );

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
