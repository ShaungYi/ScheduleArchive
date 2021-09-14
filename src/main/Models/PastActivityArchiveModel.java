package main.Models;

import main.Utility.PastActivity;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;

public class PastActivityArchiveModel {


    public static ArrayList<PastActivity> pastActivities = new ArrayList<>();
    static boolean loadingComplete = false;


    public void initialize(){
//        loadAllPastActivities();

    }



    public static Comparator<PastActivity> pastActivityComparator = new Comparator<PastActivity>() {
        @Override
        public int compare(PastActivity p1, PastActivity p2) {
            int relevanceScore1 = p1.getFrequency();
            int relevanceScore2 = p2.getFrequency();

            if (p1.getDurationsByDate().equals(ArchiveDBModel.selectedDay)){
                relevanceScore1 += 50;
            }
            if (p2.getDurationsByDate().equals(ArchiveDBModel.selectedDay)){
                relevanceScore2 += 50;
            }

            if (relevanceScore1 > relevanceScore2){
                return 1;
            }
            else if (relevanceScore1 < relevanceScore2){
                return -1;
            }
            else {
                return 0;
            }
        }
    };


    public static void loadAllPastActivities(){

        try {

            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(ArchiveDBModel.getArchiveUrl());
            Statement statement = conn.createStatement();


            DatabaseMetaData metaData = conn.getMetaData();
            String[] types = {"TABLE"};
            ResultSet days = metaData.getTables(null, null, "%", types);


            days.next();
            while (days.next()) {
                String tableID = days.getString("TABLE_NAME");

                ResultSet activities = statement.executeQuery("SELECT * FROM " + tableID);

                while (activities.next()){

                    String activityName = activities.getString("name");
                    String activityCategory = activities.getString("category");
                    int activityDuration = activities.getInt("duration");
                    String activityDate = activities.getString("date");

                    loadActivity(activityName, activityCategory, activityDuration, activityDate);
                }
            }

            pastActivities.sort(pastActivityComparator);


            loadingComplete = true;

            conn.close();

            System.out.println("loaded all past activities");

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

    }




    public static void loadActivity(String activityName, String activityCategory, int activityDuration, String activityDate){


        boolean activityAlreadyExists = false;

        if (!pastActivities.isEmpty()){
            for (PastActivity pastActivity : pastActivities){

                if (pastActivity.getName().toLowerCase().equals(activityName.toLowerCase())){

                    pastActivity.incrementFrequency();
                    pastActivity.updateDates(activityDate, activityDuration);
                    pastActivity.setNetDuration(pastActivity.getNetDuration() + activityDuration);
                    activityAlreadyExists = true;

                    break;
                }
            }
        }


        if (!activityAlreadyExists){
            PastActivity newActivity = new PastActivity(activityName, activityCategory, activityDate, 1, activityDuration);
            newActivity.updateDates(activityDate, activityDuration);
            pastActivities.add(newActivity);

        }
    }
}
