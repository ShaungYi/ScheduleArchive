package main.Models;

import main.Controllers.Timeline.TimeLine;

import java.util.ArrayList;
import java.util.HashMap;

public class DateTimeModel {

    public static ArrayList<String> allDates = new ArrayList<>();
    static final String [] monthNamesInOrder = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    static final String [] numericalMonthNamesInOrder = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    static final String [] monthColorsInOrder = {"red", "orange", "yellow", "green", "blue", "violet", "purple", "magenta", "cyan", "lime", "crimson", "coral"};
    static final HashMap<String, String[]> monthData = new HashMap<>();



    //<get>s month, number, color <from> month || number || color
    public static String getMonthProperty(String get, String from, String input){
        //create monthData if it isn't initialized
        if (monthData.isEmpty()){
            monthData.put("name", monthNamesInOrder);
            monthData.put("number", numericalMonthNamesInOrder);
            monthData.put("color", monthColorsInOrder);
        }

        //find index of input in all arrays
        int universalInputIndex = -1;
        String [] sourceArray = monthData.get(from);
        for (int i = 0; i < sourceArray.length; i++){
//            System.out.println(sourceArray[i]);
            if (sourceArray[i].equals(input)){
                universalInputIndex = i;
            }
        }

        //return target value matching universal index
        if (universalInputIndex >= 0){
            return monthData.get(get)[universalInputIndex];
        }
        //return empty string if value not found
        return "";
    }


    public static String getDayIDFromDate(String date){
        String id = date.substring(8, 10);
        if (date.indexOf("Part") >= 0){
            id += "-" + date.substring(date.lastIndexOf("_") + 1, date.length());
        }
        return id;
    }

    public static String getMonthFromDate(String date){
        return date.substring(5,7);
    }

    public static String getYearFromDate(String date){
        return date.substring(0,4);
    }


    public static int convertHHMMSSToSeconds(int hh, int mm, int ss){
        return hh * 3600 + mm * 60 + ss;
    }

    public static String parseSeconds(int seconds){
        int template;

        if (seconds > TimeLine.SECONDS_IN_A_DAY){
            template = seconds - TimeLine.SECONDS_IN_A_DAY;
        }
        else {
            template = seconds;
        }



        int hh = ((Integer)(template / 3600));

        template = template % 3600;

        int mm = ((Integer)(template / 60));

        int ss = template % 60;




        String minutes;
        if (mm == 0){
            minutes = "00";
        }
        else if (mm < 10){
            minutes = "0" + mm;
        }
        else {
            minutes = ((Integer) mm).toString();
        }


        if (hh == 0){
            return "12" + ":" + minutes + ":" + (ss < 10 || ss == 0 ? "0" + ss : ss) + " A.M.";
        }
        else if (hh == 12){
            return hh + ":" + minutes + ":" + (ss < 10 || ss == 0 ? "0" + ss : ss) +" P.M.";
        }
        else if (hh < 12){
            return hh + ":" + minutes + ":" + (ss < 10 || ss == 0 ? "0" + ss : ss) + " A.M.";
        }
        return hh - 12 + ":" + minutes + ":" + (ss < 10 || ss == 0 ? "0" + ss : ss) + " P.M.";
    }





    public static String parseDurationToString(int durationSecs){



        Time time = parseDuration(durationSecs);

        int hh = time.getHh();
        int mm = time.getMm();
        int ss = time.getSs();


        String hours = hh + (hh == 1 ? " hour " : " hours ");
        String minuts = mm + (mm == 1 ? " minute " : " minutes ");
        String seconds = ss + (ss == 1 ? " second" : " seconds");



        if (hh == 0 && mm == 0){
            return seconds;
        }
        else if (hh == 0){
            return minuts + seconds;
        }
        else if (mm == 0){
            return hours + seconds;
        }
        else {
            return  hours + minuts + seconds;
        }
    }

    public static Time parseDuration(int durationSecs) {
        int template = durationSecs;

        int hh = ((Integer)(template / 3600));

        template = template % 3600;

        int mm = ((Integer)(template / 60));

        template = template % 60;

        int ss = ((Integer) template);

        Time time = new Time(hh, mm, ss);
        return time;
    }



    public static class Time {
        int hh;
        int mm;
        int ss;

        public Time(int h, int m, int s){
            hh = h;
            mm = m;
            ss = s;
        }


        public int getHh() {
            return hh;
        }

        public int getMm() {
            return mm;
        }

        public int getSs() {
            return ss;
        }
    }
}
