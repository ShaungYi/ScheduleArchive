package main.Controllers.Macro.Infographics.Barcomponent;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import main.Models.DateTimeModel;
import main.Models.Graphics.InfographicsModel;
import main.Models.MacroDataModel;

import java.io.IOException;

public class Barcomponent extends VBox {

    String date;
    public BarComponentController controller;
    public static String unselectedColor = "rgba(129, 104, 86, 0.84)";
    public static String selectedHexColor = "#685345";

    public Barcomponent(String date, double barFrequency){
        super();

        this.setAlignment(Pos.BOTTOM_CENTER);

        this.date = date;

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                    "barComponent.fxml"));
            Node n = fxmlLoader.load();
            this.getChildren().add(n);

            controller = fxmlLoader.getController();
            controller.thisComp = this;

            controller.configure(date, barFrequency);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setBarFrequency (double barFrequency){
        controller.updateBarHeight(barFrequency);
    }

    public void setDurationLabelText(String duration){
        controller.durationLabel.setText(duration);
    }

    public void updateDayData(String date){
        double barFrequency;
        int totalDurationOfSelectedActivitiesOnDay = 0;

        if (MacroDataModel.totalActivityDurationsByDate.containsKey(date)){
            totalDurationOfSelectedActivitiesOnDay = MacroDataModel.totalActivityDurationsByDate.get(date);
            barFrequency = (double) totalDurationOfSelectedActivitiesOnDay / InfographicsModel.maximumActivityDuration;
        } else {
            barFrequency = 0;
        }

        //update bar frequency and height
        setBarFrequency(barFrequency);

        //configure duration label
        if (totalDurationOfSelectedActivitiesOnDay != 0){
            String dur = DateTimeModel.parseDurationToString(totalDurationOfSelectedActivitiesOnDay, true);
            String uptoMinDur = dur.substring(0, dur.lastIndexOf(" "));
            this.setDurationLabelText(uptoMinDur);
        } else {
            this.setDurationLabelText("");
        }



    }


    public void setBarColor(String color){
        controller.freqBar.setStyle("-fx-fill: " + color + ";");
    }



    public String getDate() {
        return date;
    }

}
