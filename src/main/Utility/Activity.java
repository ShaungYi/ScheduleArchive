package main.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class Activity implements Cloneable{
    private String name;
    private String category;
    private int durationSeconds;
    private String description;
    int startTimeSecs;
    int endTimeSecs;
    String date;

    public static Comparator<Activity> activityComparator = (Activity a1, Activity a2) -> {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date date1 = new Date();
        Date date2 = new Date();

        try {
            date1 = sdf.parse(a1.getDate());
            date2 = sdf.parse(a2.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date1.after(date2)){
            System.out.println("date1 greater than date2");
            return 1;
        } else if (date1.equals(date2)){
            int start1 = a1.getStartTimeSecs();
            int start2 = a2.getStartTimeSecs();

            if (start1 > start2){
                return 1;
            } else if (start1 == start2){
                return 0;
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    };

    public Activity (String name, String category, String description, int duration, int startTime, int endTime, String date){
        this.name = name;
        this.category = category;
        this.description = description;
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

    public String getDescription() {
        return description;
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

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
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


    public String toString(){
        return "Activity: "+"name = "+name+", category = "+category+", description: "+description+", startTimeSecs: "+startTimeSecs+", endTimeSecs: "+getEndTimeSecs()+", duration = "+durationSeconds+" seconds, Date: "+getDate();
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
