package main.Controllers.Macro.Infographics.Barcomponent;

import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import main.Models.DateTimeModel;
import main.Models.Graphics.InfographicsModel;
import main.Models.MacroDataModel;
import main.Models.SceneNavigationModel;
import main.Utility.PastActivity;
import main.resources.customNodes.activitySummaryTableView.ActivitySummaryTableView;

import java.util.HashMap;


public class BarComponentController {


    @FXML
    VBox containerPane;

    @FXML
    Pane infoPopupContainerPane;

    @FXML
    Label durationLabel;

    @FXML
    Rectangle freqBar;

    @FXML
    Label dateLabel;

    public Barcomponent thisComp;

    public void initialize(){
        //match height to infographics pane
        containerPane.setPrefHeight(InfographicsModel.infographicsPaneHeight);

        //store bar width in model
        InfographicsModel.barWidth = containerPane.getPrefWidth();

        //make info popup cover everything
    }

    public void configure(String date, double barFrequency){
        dateLabel.setText(DateTimeModel.getDayIDFromDate(date));
        updateBarHeight(barFrequency);
    }

    public void updateBarHeight(double barFrequency){
        freqBar.setHeight(InfographicsModel.maxBarHeight * barFrequency);
    }

    @FXML
    public void showInfoPopupOnMouseClicked(MouseEvent event){

        updateInfoPopup();

        if (!InfographicsModel.dayInfoPopupListViewContainerPane.getChildren().contains(InfographicsModel.dayInfoPopupListView)){
            InfographicsModel.dayInfoPopupListViewContainerPane.getChildren().add(InfographicsModel.dayInfoPopupListView);
        }

        double x = event.getSceneX();
        double y = event.getSceneY();


        double endX = x + InfographicsModel.dayInfoPopupListViewWidth;
        if (endX > SceneNavigationModel.screenWidth) {
            double pushInBy = endX - SceneNavigationModel.screenWidth;
            x -= pushInBy;
        }

        double endY = y + InfographicsModel.dayInfoPopupListViewHeight;
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        double screenHeight = screenBounds.getHeight() - 110;
        if (endY > screenHeight){
            double pushInBy = endY - screenHeight;
            y -= pushInBy;
        }


        InfographicsModel.dayInfoPopupListView.setLayoutX(x);
        InfographicsModel.dayInfoPopupListView.setLayoutY(y);

    }

    private void updateInfoPopup(){
        InfographicsModel.dayInfoPopupData.clear();
        InfographicsModel.dayInfoPopupListView.setMaxHeight(InfographicsModel.mindayInfoPopupListViewHeight);
        InfographicsModel.dayInfoPopupListView.setMinHeight(InfographicsModel.mindayInfoPopupListViewHeight);
        InfographicsModel.dayInfoPopupListViewHeight = InfographicsModel.mindayInfoPopupListViewHeight;
        System.out.println("reset: "+InfographicsModel.dayInfoPopupListView.getPrefHeight());

        for (PastActivity pastActivity : MacroDataModel.selectedPastActivities){
            HashMap<String, Integer> durationsByDate = pastActivity.getDurationsByDate();
            if (durationsByDate.keySet().contains(thisComp.date)){

                //create new chart data
                String activityName = pastActivity.getName();
                int activityDuration = durationsByDate.get(thisComp.date);
                //String activityDurationParsed = DateTimeModel.parseDurationToString(activityDuration, false);
                ActivitySummaryTableView.chartDataUnit chartDataUnit = new ActivitySummaryTableView.chartDataUnit(activityName, activityDuration);

                InfographicsModel.dayInfoPopupData.add(chartDataUnit);


                double newHeight = InfographicsModel.dayInfoPopupListView.getMinHeight() + InfographicsModel.dayInfoPopupListViewCellHeight;
                InfographicsModel.dayInfoPopupListView.setMaxHeight(newHeight);
                InfographicsModel.dayInfoPopupListView.setMinHeight(newHeight);
                InfographicsModel.dayInfoPopupListViewHeight = newHeight;
                System.out.println("after data added: " +InfographicsModel.dayInfoPopupListView.getPrefHeight());
            }
        }

    }

}
