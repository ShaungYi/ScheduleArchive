package main.Utility;


import main.Controllers.Timeline.TimeLine;
import main.Models.DateTimeModel;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class StopWatch {
    public int starTimeSecs;
    private int endTimeSecs;
    private int measuredDurationSecs;
    private String display = "00:00:00";

    public StopWatch() {
        measuredDurationSecs = 0;
        starTimeSecs = LocalTime.now().toSecondOfDay();
        endTimeSecs = starTimeSecs;

    }


    public String makeDisplay() {

        String freshAndSteamingDisplay;
        int unprocessed = measuredDurationSecs;

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
        endTimeSecs = LocalTime.now().toSecondOfDay();
//        System.out.println("startTime: "+starTimeSecs);
//        System.out.println("duration: "+measuredDurationSecs);
//        System.out.println("endTime: "+endTimeSecs);
        if (endTimeSecs < starTimeSecs){
            measuredDurationSecs = (endTimeSecs + DateTimeModel.SECONDS_IN_A_DAY) - starTimeSecs;
        } else {
            measuredDurationSecs = endTimeSecs - starTimeSecs;
        }
    }


    public int getMeasuredTime() {
        return measuredDurationSecs;
    }

    public int getStartTimeSec() {
        return starTimeSecs;
    }

    public int getEndTime(){
        return endTimeSecs;
    }


    public String doubleDigitize(Integer i) {
        String duplexI = i.toString();
        if (i < 10) {
            duplexI = "0" + duplexI;
        }
        return duplexI;
    }

    public void reset(){
        starTimeSecs = LocalTime.now().toSecondOfDay();
        measuredDurationSecs = 0;
    }

}