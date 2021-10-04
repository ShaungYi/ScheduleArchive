package main.Models.Graphics;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import main.Controllers.Macro.Infographics.Barcomponent.Barcomponent;
import main.resources.customNodes.activitySummaryTableView.ActivitySummaryTableView;

import java.util.ArrayList;
import java.util.HashMap;

public class InfographicsModel {


    //important javafx controls
    public static ActivitySummaryTableView dayInfoPopupListView = new ActivitySummaryTableView();
    public static double dayInfoPopupListViewHeight = 500;
    public static double dayInfoPopupListViewWidth = 380;
    public static double dayInfoPopupListViewCellHeight = 23;
    public static double mindayInfoPopupListViewHeight = 62;
    public static ObservableList<ActivitySummaryTableView.chartDataUnit> dayInfoPopupData = FXCollections.observableArrayList(new ActivitySummaryTableView.chartDataUnit("",-1));

    public static Pane dayInfoPopupListViewContainerPane;

    public static Barcomponent selectedBarcomponent = new Barcomponent("this dang date don matter", 10203214);

    //graphic dimentions parameters
    public static double barWidth;
    public static double maxBarHeight = 550;
    public static double infographicsPaneHeight;
    public static double monthBarHeight;
    public static double infographicWidth = 0;
    public static double yearPaneHeight;

    //date related data
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
