package main.Utility;

import java.util.ArrayList;
import java.util.HashMap;

public class PastActivity {

    String name;
    String category;

    HashMap<String, Integer> durationsByDate = new HashMap<>();
    int frequency;
    int netDuration;

    public PastActivity(String name, String category, String date, int frequency, int netDuration) {
        this.name = name;
        this.category = category;
        this.frequency = frequency;
        this.netDuration = netDuration;
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

    public void incrementFrequency() {
        this.frequency++;
    }

    public int getNetDuration() {
        return netDuration;
    }

    public void setNetDuration(int netDuration) {
        this.netDuration = netDuration;
    }

    public String toString(){
        return "Activity.java: "+"name = "+name+", category = "+category+", netDuration = "+netDuration+" seconds, "+"frequency: "+frequency;
    }
}