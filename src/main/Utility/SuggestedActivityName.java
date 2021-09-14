package main.Utility;

public class SuggestedActivityName {


    String name;
    int frequency;
    String latestDate;

    public SuggestedActivityName(String name, int frequency, String latestDate) {
        this.name = name;
        this.frequency = frequency;
        this.latestDate = latestDate;
    }


    public String getName() {
        return name;
    }

    public int getFrequency() {
        return frequency;
    }

    public String getLatestDate() {
        return latestDate;
    }

}
