package main.Utility;

import java.util.ArrayList;
import java.util.HashMap;

public class PastActivity {

    String name;
    String category;
    int frequency;
    int netDuration;

    HashMap<String, Integer> durationsByDate = new HashMap<>();

    public PastActivity() {
        String name;
        String category;
        int frequency;
        int netDuration;
    }

    public void setName(String newName) {name = newName;}

    public void setCategory(String newCategory) {category = newCategory;}

    public void setFrequency(int newFrequency) {frequency = newFrequency;}

    public void setNetDuration(int NewNetDuration) {netDuration = NewNetDuration;}

    public void incrementFrequency() {
        this.frequency++;
    }


    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public HashMap<String, Integer> getDurationsByDate() {
        return durationsByDate;
    }

    public void updateDates(String date, Integer duration) {

        if (durationsByDate.containsKey(date)){
            durationsByDate.computeIfPresent(date, (k, v) -> v + duration);
        } else {
            durationsByDate.put(date, duration);
        }
    }

    public int getFrequency() {
        return frequency;
    }

    public int getNetDuration() {
        return netDuration;
    }

    public String toString(){
        return "Activity.java: "+"name = "+name+", category = "+category+", netDuration = "+netDuration+" seconds, "+"frequency: "+frequency;
    }
}