package main.Models;

import main.Models.DBModels.ReadFromDBModel;
import main.Utility.PastActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class MacroDataModel {

    public static ArrayList<String> selectedActivityNames = new ArrayList<>();
    public static ArrayList<PastActivity> selectedPastActivities = new ArrayList<>();
    public static HashMap<String, Integer> totalActivityDurationsByDate = new HashMap<>();


    public static void composeSelectedPastActivitiesListFromNames(){

        for (String selectedActivityName : selectedActivityNames){
            PastActivity pastActivity = ReadFromDBModel.loadSelectedPastActivities(selectedActivityName);
            selectedPastActivities.add(pastActivity);
        }
    }

    public static void composeTotalActivityDurationsByDate() {
        for (PastActivity pastActivity : selectedPastActivities){

            HashMap<String, Integer> pastActivityDurationsByDate = pastActivity.getDurationsByDate();

            for (String date : pastActivityDurationsByDate.keySet()){

                int activityDuration = pastActivityDurationsByDate.get(date);
                if (totalActivityDurationsByDate.containsKey(date)){
                    int oldTotalDuration = totalActivityDurationsByDate.get(date);
                    int newTotalDuration = oldTotalDuration + activityDuration;
                    totalActivityDurationsByDate.put(date, newTotalDuration);
                } else {
                    totalActivityDurationsByDate.put(date, activityDuration);
                }
            }
        }
    }

    public static void setMaximumActivityDuration(){

        for (String date : totalActivityDurationsByDate.keySet()){
            int duration = totalActivityDurationsByDate.get(date);
            if (duration > InfographicsModel.maximumActivityDuration){
                InfographicsModel.maximumActivityDuration = duration;
            }
        }
    }
}
