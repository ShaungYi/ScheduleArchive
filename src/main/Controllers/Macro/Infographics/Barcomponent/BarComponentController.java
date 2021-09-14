package main.Controllers.Macro.Infographics.Barcomponent;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import main.Models.InfographicsModel;


public class BarComponentController {


    @FXML
    VBox containerPane;

    @FXML
    Rectangle freqBar;

    @FXML
    Label dateLabel;

    public void initialize(){
        //match height to infographics pane
        containerPane.setPrefHeight(InfographicsModel.infographicsPaneHeight);

        //store bar width in model
        InfographicsModel.barWidth = containerPane.getPrefWidth();
    }

    public void configure(String date, double barFrequency){
        dateLabel.setText(date);
        freqBar.setHeight(InfographicsModel.maxBarHeight * barFrequency);
    }




}
