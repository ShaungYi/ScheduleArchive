package main.Models.DBModels;

import main.Models.DateTimeModel;
import main.Utility.Activity;
import main.Utility.PastActivity;

import java.sql.*;
import java.util.ArrayList;

public class ReadFromDBModel {


    public static ArrayList<Activity> readDay(String date) {
        ArrayList<Activity> dayContent = new ArrayList<>();

        try {
            String name;
            String category;
            String description;
            int duration;
            int startTime;
            int endTime;
            String eventDate;

            // Searching for the given date in the database
            PreparedStatement readDayContent = ArchiveDBModel.readDayContent;
            readDayContent.setString(1, date);
            ResultSet day = readDayContent.executeQuery();

            while (day.next()) {
                // extracting data from events
                name = day.getString(1);
                category = day.getString(2);
                description = day.getString(3);
                startTime = day.getInt(4);
                endTime = day.getInt(5);
                duration = day.getInt(6);

                // Setting previous endTime to the start time of the day if the event name is DayStart

                // subtracting DateTimeModel.SECONDS_IN_A_DAY from startTime and endTime when the time is above 12:00 am
                if (startTime < DateTimeModel.SECONDS_IN_A_DAY) {
                    eventDate = date;
                } else {
                    eventDate = DateTimeModel.addDayToDate(date, 1);
                    startTime -= DateTimeModel.SECONDS_IN_A_DAY;
                    endTime -= DateTimeModel.SECONDS_IN_A_DAY;
                }

                // saving the event to an arraylist
                dayContent.add(new Activity(name, category, description, duration, startTime, endTime, eventDate));
            }

            System.out.println("(from readDay) day: " + dayContent);
            day.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dayContent;

    }


    public static ArrayList<String> getAllDates() {
        ArrayList<String> allDatesList = new ArrayList<>();

        try {
            // searching for all dates in the database
            ResultSet allDates = ArchiveDBModel.getAllDatesEntered.executeQuery();

            // extracting dates from the result set and adding them to an arraylist

            while (allDates.next()) {
                allDatesList.add(allDates.getString(1));
            }

            allDates.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allDatesList;

    }


    public static ArrayList<String> getAllActivityNames() {
        ArrayList<String> activityNamesList = new ArrayList<>();

        try {
            ResultSet activityNames = ArchiveDBModel.getAllActivityNamesEntered.executeQuery();

            while (activityNames.next()) {
                activityNamesList.add(activityNames.getString(1));
            }

            activityNames.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return activityNamesList;

    }


    public static int getActivityID(String name, String category) {
        int activityID = -1;

        try {
            // searching for the activityId of the given name and category

            PreparedStatement findActivityID = ArchiveDBModel.findActivityID;
            findActivityID.setString(1, name);
            findActivityID.setString(2, category);
            ResultSet id = findActivityID.executeQuery();

            // if id was found
            if (id.next()) {
                activityID = id.getInt(1);
            }

            id.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return activityID;

    }


    public static PastActivity loadSelectedPastActivities(String name, String category) {
        PastActivity pastActivity = new PastActivity();

        int frequency = 0;
        int netDuration = 0;

        try {
            PreparedStatement getAllOccurrences =  ArchiveDBModel.getAllOccurrences;
            getAllOccurrences.setString(1, name);
            getAllOccurrences.setString(2, category);
            ResultSet occurrences = getAllOccurrences.executeQuery();

            while (occurrences.next()) {
                int duration = occurrences.getInt(1);
                String date = occurrences.getString(2);
                frequency ++;
                netDuration += duration;
                pastActivity.updateDates(date, duration);
            }

            occurrences.close();

            pastActivity.setName(name);
            pastActivity.setFrequency(frequency);
            pastActivity.setNetDuration(netDuration);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pastActivity;

    }


    public static ArrayList<String> getSuggestions(String nameFragment, String category) {
        ArrayList<String> suggestionList = new ArrayList<>();

        try {
            PreparedStatement getSuggestions = ArchiveDBModel.getSuggestions;
            getSuggestions.setString(1, DateTimeModel.currentDay);
            getSuggestions.setString(2, "%" + nameFragment + "%");
            getSuggestions.setString(3, "%" + category + "%");
            ResultSet suggestions = getSuggestions.executeQuery();

            while (suggestions.next()) {
                String suggestion = suggestions.getString(1);

                if (!suggestion.equals("undefined") && !suggestion.equals("new") && !suggestion.equals("no data") && !suggestion.equals("no name")){
                    suggestionList.add(suggestion);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return suggestionList;

    }


    public static int getBackupSettings(String name) {

        try {
            PreparedStatement getBackupSettingValue = SettingsDBModel.readBackupSettings;
            getBackupSettingValue.setString(1, name);
            ResultSet value = getBackupSettingValue.executeQuery();

            if (value.next()) {
                return value.getInt(1);
            }

            value.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;

    }
}
