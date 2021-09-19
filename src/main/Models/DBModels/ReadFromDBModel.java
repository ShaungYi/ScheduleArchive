package main.Models.DBModels;

import main.App;
import main.Models.DateTimeModel;
import main.Utility.Activity;
import main.Utility.PastActivity;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

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
            PreparedStatement readDayContent = DBModel.readDayContent;
            readDayContent.setString(1, date);
            ResultSet day = readDayContent.executeQuery();

            while (day.next()) {
                // extracting data from events
                name = day.getString("name");
                category = day.getString("category");
                startTime = day.getInt("startTime");
                endTime = day.getInt("endTime");
                duration = day.getInt("duration");

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
            ResultSet allDates = DBModel.getAllDatesEntered.executeQuery();

            // extracting dates from the result set and adding them to an arraylist
            while (allDates.next()) {
                allDatesList.add(allDates.getString("date"));
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
            ResultSet activityNames = DBModel.getAllActivityNamesEntered.executeQuery();

            while (activityNames.next()) {
                activityNamesList.add(activityNames.getString("name"));
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

            PreparedStatement findActivityID = DBModel.findActivityID;
            findActivityID.setString(1, name);
            findActivityID.setString(2, category);
            ResultSet id = findActivityID.executeQuery();

            // if id was found
            if (id.next()) {
                activityID = id.getInt("activityID");
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
            PreparedStatement getAllOccurrences =  DBModel.getAllOccurrences;
            getAllOccurrences.setString(1, name);
            ResultSet occurrences = getAllOccurrences.executeQuery();

            while (occurrences.next()) {
                String date = occurrences.getString("date");
                int duration = occurrences.getInt("duration");
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


    public static ArrayList<String> getNameSuggestions(String nameFragment) {
//        ArrayList<String> nameSuggestions = new ArrayList<>();
//
//        try {
//          PreparedStatement getSuggestions = DBModel.getNameSuggestions;
//          getSuggestions.setString(1, DateTimeModel.currentDay);
//          getSuggestions.setString(2, nameFragment + "%");
//          ResultSet suggestions = getSuggestions.executeQuery();
//
//          while (suggestions.next()) {
//              String name = suggestions.getString("name");
//
//              if (!name.equals("undefined") && !name.equals("new") && !name.equals("no data") && !nameSuggestions.contains(name)){
//                  nameSuggestions.add(name);
//              }
//          }
//
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }

        ArrayList<String> nameSuggestions = new ArrayList<>();

        for (int i = 0; i < 1000000; i++){
            nameSuggestions.add("hey na na na");
        }

        return nameSuggestions;

    }

}
