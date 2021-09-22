package main.Models.DBModels;

import java.io.File;
import java.sql.*;

public class SettingsDBModel {
    public static Connection connection;
    public static String pathToSettingsDB = "Databases/settings.db";
    public static PreparedStatement updateBackupSettings;
    public static PreparedStatement readBackupSettings;
    public static String maxBackupNum = "maxBackupNum";
    public static String backupCreationInterval= "backupCreationInterval";

    public static void connect() {
        // getting the full path to the database
        File settingsDB = new File(pathToSettingsDB);
        String fullPathToSettingsDB = "jdbc:sqlite:" + settingsDB.getAbsolutePath();

        try {
            Class.forName("org.sqlite.JDBC");
            // connecting to the database
            connection = DriverManager.getConnection(fullPathToSettingsDB);

            // creating tables

            Statement statement = connection.createStatement();

            statement.execute("CREATE TABLE IF NOT EXISTS backupSettings (name TEXT, value TEXT)");

            statement.close();

            // write statements

            PreparedStatement createNewSetting = connection.prepareStatement(
                        "INSERT INTO backupSettings (name, value) " +
                            "SELECT ?, ? " +

                            "WHERE NOT EXISTS " +
                            "(" +
                                "SELECT * " +
                                "FROM backupSettings " +
                                "WHERE name = ?" +
                            ")"
            );

            updateBackupSettings = connection.prepareStatement("UPDATE backupSettings SET value = ? WHERE name = ?");

            // read statements

            readBackupSettings = connection.prepareStatement("SELECT value FROM backupSettings WHERE name = ? LIMIT 1");

            // creating settings

            createSetting(maxBackupNum, 6, createNewSetting);

            createSetting(backupCreationInterval, 3600, createNewSetting);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }


    private static void createSetting (String name, int value, PreparedStatement insertStatement) {
        try {
            insertStatement.setString(1, name);
            insertStatement.setInt(2, value);
            insertStatement.setString(3, name);
            insertStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

