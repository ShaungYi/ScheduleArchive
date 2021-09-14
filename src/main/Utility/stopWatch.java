package main.Utility;


import main.Controllers.Timeline.TimeLine;

public class stopWatch {
    private int starTimeSec;
    private int measuredSecs;
    private String display = "00:00:00";

    public stopWatch(int startTime) {

        measuredSecs = 0;
        starTimeSec = startTime;

    }


    public String makeDisplay() {

        String freshAndSteamingDisplay;
        int unprocessed = measuredSecs;

        Integer hh;
        Integer mm;
        Integer ss;

        hh = unprocessed / 3600;
        unprocessed = unprocessed % 3600;

        mm = unprocessed / 60;
        unprocessed = unprocessed % 60;

        ss = unprocessed;

        freshAndSteamingDisplay = doubleDigitize(hh) + ":" + doubleDigitize(mm) + ":" + doubleDigitize(ss);
        return freshAndSteamingDisplay;
    }

    public void tick() {
        measuredSecs++;

    }


    public void recalibrate(int currentTimeInSeconds) {
        if (currentTimeInSeconds < starTimeSec) {
            measuredSecs = (TimeLine.SECONDS_IN_A_DAY - starTimeSec) + currentTimeInSeconds;
        } else {
            measuredSecs = currentTimeInSeconds - starTimeSec;
        }
    }

    public int getMeasuredTime() {
        return measuredSecs;
    }

    public void setMeasuredSecs(int measuredSecs) {
        this.measuredSecs = measuredSecs;
    }

    public int getStarTimeSec() {
        return starTimeSec;
    }

    public void setStarTimeSec(int starTimeSec) {
        this.starTimeSec = starTimeSec;
    }

    public String doubleDigitize(Integer i) {
        String duplexI = i.toString();
        if (i < 10) {
            duplexI = "0" + duplexI;
        }
        return duplexI;
    }

}