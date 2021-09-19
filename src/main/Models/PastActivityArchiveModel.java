package main.Models;

import main.Models.DBModels.DBModel;
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

            if (p1.getDurationsByDate().equals(DateTimeModel.selectedDay)){
                relevanceScore1 += 50;
            }
            if (p2.getDurationsByDate().equals(DateTimeModel.selectedDay)){
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
            PastActivity newActivity = new PastActivity();

            newActivity.setName(activityName);
            newActivity.setCategory(activityCategory);
            newActivity.setFrequency(1);
            newActivity.setNetDuration(activityDuration);

            newActivity.updateDates(activityDate, activityDuration);
            pastActivities.add(newActivity);

        }
    }
}
