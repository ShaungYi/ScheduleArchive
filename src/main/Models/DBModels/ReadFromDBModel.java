package main.Models.DBModels;

import main.Models.DateTimeModel;
import main.Utility.Activity;
import main.Utility.PastActivity;
import main.Utility.Suggestion;

import java.sql.*;
import java.util.ArrayList;

public class ReadFromDBModel {


    public static ArrayList<Activity> readDay(String date) {
        ArrayList<Activity> dayContent = new ArrayList<>();

        try {
            String name;
            String category;
            int duration;
            int startTime;
            int endTime;
            String eventDate;

            // Searching for the given date in the database
            PreparedStatement readDayContent = ArchiveDBModel.readDayContent;
            readDayContent.setString(1, date);
            ResultSet day = readDayContent.executeQuery();

            int nameColumnID = day.findColumn("name");
            int categoryColumnID = day.findColumn("category");
            int startTimeColumnID = day.findColumn("startTime");
            int endTimeColumnID = day.findColumn("endTime");
            int durationColumnID = day.findColumn("duration");

            while (day.next()) {
                // extracting data from events
                name = day.getString(nameColumnID);
                category = day.getString(categoryColumnID);
                startTime = day.getInt(startTimeColumnID);
                endTime = day.getInt(endTimeColumnID);
                duration = day.getInt(durationColumnID);

                // Setting previous endTime to the start time of the day if the event name is DayStart

                // subtracting 86400 from startTime and endTime when the time is above 12:00 am
                if (startTime < 86400) {
                    eventDate = date;
                } else {
                    eventDate = DateTimeModel.addDayToDate(date, 1);
                    startTime -= 86400;
                    endTime -= 86400;
                }

                // saving the event to an arraylist
                dayContent.add(new Activity(name, category, duration, startTime, endTime, eventDate));
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
            int dateColumnID = allDates.findColumn("date");

            while (allDates.next()) {
                allDatesList.add(allDates.getString(dateColumnID));
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

            int nameColumnID = activityNames.findColumn("name");

            while (activityNames.next()) {
                activityNamesList.add(activityNames.getString(nameColumnID));
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

            int activityIDColumnID = id.findColumn("activityID");

            // if id was found
            if (id.next()) {
                activityID = id.getInt(activityIDColumnID);
            }

            id.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return activityID;

    }


    public static PastActivity loadSelectedPastActivities(String name) {
        PastActivity pastActivity = new PastActivity();

        int frequency = 0;
        int netDuration = 0;

        try {
            PreparedStatement getAllOccurrences =  ArchiveDBModel.getAllOccurrences;
            getAllOccurrences.setString(1, name);
            ResultSet occurrences = getAllOccurrences.executeQuery();

            int dateColumnID = occurrences.findColumn("date");
            int durationColumnID = occurrences.findColumn("duration");

            while (occurrences.next()) {
                String date = occurrences.getString(dateColumnID);
                int duration = occurrences.getInt(durationColumnID);
                frequency ++;
                netDuration += duration;
                pastActivity.updateDates(date, duration);
            }

            pastActivity.setName(name);
            pastActivity.setFrequency(frequency);
            pastActivity.setNetDuration(netDuration);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pastActivity;

    }


    public static ArrayList<Suggestion> getSuggestions() {
        ArrayList<Suggestion> nameSuggestions = new ArrayList<>();

        try {
          PreparedStatement getSuggestions = ArchiveDBModel.getNameSuggestions;
          getSuggestions.setString(1, DateTimeModel.currentDay);
          ResultSet suggestions = getSuggestions.executeQuery();
          int nameColumnID = suggestions.findColumn("name");
          int categoryColumnID = suggestions.findColumn("category");

          while (suggestions.next()) {
              String name = suggestions.getString(nameColumnID);
              String category = suggestions.getString(categoryColumnID);

              if (!name.equals("undefined") && !name.equals("new") && !name.equals("no data") && !nameSuggestions.contains(name)){
                  nameSuggestions.add(new Suggestion(name, category));
              }
          }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nameSuggestions;

    }


    public static int getBackupSettings(String name) {

        try {
            PreparedStatement getBackupSettingValue = SettingsDBModel.readBackupSettings;
            getBackupSettingValue.setString(1, name);
            ResultSet maxNumberOfBackups = getBackupSettingValue.executeQuery();

            int valueColumnID = maxNumberOfBackups.findColumn("value");

            if (maxNumberOfBackups.next()) {
                return maxNumberOfBackups.getInt("value");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;

    }
}
