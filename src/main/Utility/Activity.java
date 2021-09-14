package main.Utility;

import java.util.ArrayList;

public class Activity implements Cloneable{
    private String name;
    private String category;
    private int durationSeconds;
    int startTimeSecs;
    int endTimeSecs;
    String date;

    public Activity (String name, String category, int duration, int startTime, int endTime, String date){
        this.name = name;
        this.category = category;
        this.durationSeconds = duration;
        this.startTimeSecs = startTime;
        this.endTimeSecs = endTime;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public int getStartTimeSecs() {
        return startTimeSecs;
    }



    public int getEndTimeSecs() {
        return endTimeSecs;
    }

    public String getDate() {
        return date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDurationSeconds(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public void setStartTimeSecs(int startTimeSecs) {
        this.startTimeSecs = startTimeSecs;
    }

    public void setEndTimeSecs(int endTimeSecs) {
        this.endTimeSecs = endTimeSecs;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String toString(){
        return "Activity: "+"name = "+name+", category = "+category+", duration = "+durationSeconds+" seconds, "+"startTimeSecs: "+startTimeSecs+", endTimeSecs: "+getEndTimeSecs();
    }



    //precondition: parameter obj must be of type 'Activity'
    //postcondition: returns true if all fields in other are the same as the fields in this, false if not
    @Override
    public boolean equals(Object obj) {

        Activity other = (Activity) obj;

        //check if all field of other and this are equal
        if (this.name.equals(other.getName()) &&
        this.category.equals(other.getCategory()) &&
        this.durationSeconds == other.getDurationSeconds() &&
        this.startTimeSecs == other.getStartTimeSecs() &&
        this.endTimeSecs == other.getEndTimeSecs() &&
        this.date.equals(other.getDate())){
            //return true if fields match
            return true;
        }
        //return false if fields don't match
        return false;
    }


    //returns true if list 'activities' contains an Activity that is equal to (.equals -> true) this
    // use like this: dumbAct = new Activity(blah blah joonius sucks) -> dumbAct.arrayListContainsActivity(arraylist you are checking)
    // OOP tip: this = dumbAct
    public boolean arrayListContainsActivity(ArrayList<Activity> activities){
        for (Activity activity : activities){
            if (this.equals(activity)){
                //return true if just one activity in list matches this
                return true;
            }
        }
        //return false if loop didn't find an activity matching this
        return false;
    }
}
