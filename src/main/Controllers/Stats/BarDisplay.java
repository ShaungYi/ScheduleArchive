package main.Controllers.Stats;

import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import main.Controllers.PrototypeController;
import main.Models.DBModels.ArchiveDBModel;
import main.Models.SceneNavigationModel;
import main.Utility.Activity;
import main.App;

public class BarDisplay extends PrototypeController {
    static XYChart.Series activitySeries = new XYChart.Series();

    @FXML
    Button goToCreatorButton;

    @FXML
    javafx.scene.chart.BarChart compositionBar;

    @FXML
    CategoryAxis compoCatAxis;

    @FXML
    NumberAxis compoNumAxis;

    public void initialize(){

        //set prototype property 'gotoCreatorButton'
        setGoToCreatorButton(goToCreatorButton);

        activitySeries.setName("Activities");

        for (String category : Stats.categorySequence){
            activitySeries.getData().add( new XYChart.Data(category,0.0));
        }

        compositionBar.getData().add(activitySeries);

    }







    public static void updateBarChart(){


        int netSecondsStudy = 0;
        int netSecondsEntertainment = 0;
        int netSecondsSpirituality = 0;
        int netSecondsExercise = 0;
        int netSecondsRest = 0;
        int netSecondsReading = 0;
        int netSecondsWriting = 0;
        int netSecondsArts = 0;
        int netSecondsSocial = 0;
        int netSecondsMedia = 0;
        int netSecondsService = 0;
        int netSecondsMiscellaneous = 0;
        for (Activity activity : ArchiveDBModel.archive){
            String cat = activity.getCategory();
            switch (cat){
                case "Study":
                    netSecondsStudy += activity.getDurationSeconds();
                    break;
                case "Entertainment":
                    netSecondsEntertainment += activity.getDurationSeconds();
                    break;
                case "Spirituality":
                    netSecondsSpirituality += activity.getDurationSeconds();
                    break;
                case "Exercise":
                    netSecondsExercise += activity.getDurationSeconds();
                    break;
                case "Rest":
                    netSecondsRest += activity.getDurationSeconds();
                    break;
                case "Reading":
                    netSecondsReading += activity.getDurationSeconds();
                    break;
                case "Writing":
                    netSecondsWriting += activity.getDurationSeconds();
                    break;
                case "Arts":
                    netSecondsArts += activity.getDurationSeconds();
                    break;
                case "Social":
                    netSecondsSocial += activity.getDurationSeconds();
                    break;
                case "Media":
                    netSecondsMedia += activity.getDurationSeconds();
                    break;
                case "Service":
                    netSecondsService += activity.getDurationSeconds();
                    break;
                case "Miscellaneous":
                    netSecondsMiscellaneous += activity.getDurationSeconds();
                    break;

            }
        }
        activitySeries.getData().clear();
        int total = netSecondsStudy + netSecondsEntertainment + netSecondsSpirituality + netSecondsExercise + netSecondsRest + netSecondsReading + netSecondsWriting + netSecondsArts + netSecondsMiscellaneous + netSecondsSocial + netSecondsMedia + netSecondsService;
        activitySeries.getData().add(new XYChart.Data("Study", Stats.calculatePercentage(total,netSecondsStudy)));
        activitySeries.getData().add(new XYChart.Data("Entertainment", Stats.calculatePercentage(total,netSecondsEntertainment)));
        activitySeries.getData().add(new XYChart.Data("Spirituality", Stats.calculatePercentage(total,netSecondsSpirituality)));
        activitySeries.getData().add(new XYChart.Data("Exercise", Stats.calculatePercentage(total,netSecondsExercise)));
        activitySeries.getData().add(new XYChart.Data("Rest", Stats.calculatePercentage(total,netSecondsRest)));
        activitySeries.getData().add(new XYChart.Data("Reading", Stats.calculatePercentage(total,netSecondsReading)));
        activitySeries.getData().add(new XYChart.Data("Writing", Stats.calculatePercentage(total,netSecondsWriting)));
        activitySeries.getData().add(new XYChart.Data("Arts", Stats.calculatePercentage(total,netSecondsArts)));
        activitySeries.getData().add(new XYChart.Data("Social", Stats.calculatePercentage(total,netSecondsSocial)));
        activitySeries.getData().add(new XYChart.Data("Media", Stats.calculatePercentage(total,netSecondsMedia)));
        activitySeries.getData().add(new XYChart.Data("Service", Stats.calculatePercentage(total,netSecondsService)));
        activitySeries.getData().add(new XYChart.Data("Miscellaneous", Stats.calculatePercentage(total,netSecondsMiscellaneous)));
    }


    public  void goToScheduleCreator(){
        App.sceneNavigationModel.gotoScene(SceneNavigationModel.scheduleCreator, SceneNavigationModel.bars);
    }

    public  void goToMain(){
        App.sceneNavigationModel.gotoScene(SceneNavigationModel.stats, SceneNavigationModel.bars);
    }

}
