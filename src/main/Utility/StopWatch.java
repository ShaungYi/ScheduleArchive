package main.Utility;


import main.Controllers.Timeline.TimeLine;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class StopWatch {
    private LocalDateTime starTime;
    private LocalDateTime endTime;
    private Duration measuredDuration;
    private String display = "00:00:00";

    public StopWatch(LocalDateTime start) {
        measuredDuration = Duration.ZERO;
        starTime = start;
        endTime = LocalDateTime.now();

    }


    public String makeDisplay() {

        String freshAndSteamingDisplay;
        int unprocessed = (int) measuredDuration.getSeconds();

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
        endTime = LocalDateTime.now();
        measuredDuration = Duration.between(starTime, endTime);
    }


    public int getMeasuredTime() {
        return (int) measuredDuration.getSeconds();
    }

    public int getStartTimeSec() {
        return starTime.toLocalTime().toSecondOfDay();
    }

    public int getEndTime(){
        return endTime.toLocalTime().toSecondOfDay();
    }

    public void setStarTime(LocalDateTime starTime) {
        this.starTime = starTime;
    }

    public String doubleDigitize(Integer i) {
        String duplexI = i.toString();
        if (i < 10) {
            duplexI = "0" + duplexI;
        }
        return duplexI;
    }

    public void reset(){
        starTime = LocalDateTime.now();
        measuredDuration = Duration.ZERO;
    }

}