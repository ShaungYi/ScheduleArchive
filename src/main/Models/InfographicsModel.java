package main.Models;

import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashMap;

public class InfographicsModel {
    public static double barWidth;
    public static double maxBarHeight = 600;
    public static double infographicsPaneHeight;
    public static double monthBarHeight;
    public static double infographicWidth = 0;
    public static double yearPaneHeight;
    public static HashMap<String, VBox> years = new HashMap<>();
    //pairs double date change layout x with string month name
    public static ArrayList<date_layoutX_pair> monthChangePoints = new ArrayList<>();
    public static ArrayList<date_layoutX_pair> yearChangePoints = new ArrayList<>();


    public static int maximumActivityDuration = 0;


    public static String getYearStyleClass(String date){

        return "y" + date.substring(0, 4);

    }

    public static void addYear(String year, VBox yearPane){
        years.put(year, yearPane);
    }

    public static void addStyleClassToYear(VBox year, String date){

        year.getStyleClass().add(getYearStyleClass(date));

    }

    //adds new month data to dateChangePoints
    public static void addNewDate(String dateName, double transitionX, String dateType){
        ArrayList<date_layoutX_pair> changePoints = dateType == "month" ? monthChangePoints : yearChangePoints;
        changePoints.add(new date_layoutX_pair(dateName, transitionX));
    }

    //get month to be displayed in month label
    public static String getDisplayedDate(double layoutX, String dateType) {

        ArrayList<date_layoutX_pair> changePoints = dateType == "year" ? yearChangePoints : monthChangePoints;

        for (int i = 0; i < changePoints.size(); i++){
            date_layoutX_pair pair = changePoints.get(i);
            if (pair.transitionX > layoutX){
                return pair.monthName;
            }
        }return "";
    }


    private static class date_layoutX_pair {
        public String monthName;
        public double transitionX;
        public date_layoutX_pair(String m, double x){
            monthName = m;
            transitionX = x;
        }
    }
}
