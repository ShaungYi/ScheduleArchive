package main.Controllers.Stats;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import main.Controllers.PrototypeController;
import main.Models.DBModels.DBModel;
import main.Utility.Activity;
import main.App;

public class PiChart extends PrototypeController {

    @FXML
    BorderPane motherPane;

    @FXML
    Button goToCreatorButton;

    @FXML
    PieChart piChart;
    static ObservableList<PieChart.Data> piChartData;

    public void initialize(){

        //set prototype property 'gotoCreatorButton'
        setGoToCreatorButton(goToCreatorButton);

        piChartData = FXCollections.observableArrayList();

        for (String category : Stats.categorySequence){
            piChartData.add(new PieChart.Data(category, 100.0/9.0));
        }

        piChart.setData(piChartData);

        for (int i = 0; i < piChartData.size(); i++){
            piChartData.get(i).getNode().setStyle( "fx-background-color: "+Stats.colorSequence.get(i));
            //System.out.println("fx-pie-color: "+Stats.colorSequence.get(i));
        }
    }


    public static void updatePi(){

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
        for (Activity activity : DBModel.archive){
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

        int total = netSecondsStudy + netSecondsEntertainment + netSecondsSpirituality + netSecondsExercise + netSecondsRest + netSecondsReading + netSecondsWriting + netSecondsArts + netSecondsMiscellaneous + netSecondsSocial + netSecondsMedia + netSecondsService;

        double studyPercentage = Stats.calculatePercentage(total,netSecondsStudy);
        double entertainmentPercentage = Stats.calculatePercentage(total,netSecondsEntertainment);
        double spiritualityPercentage = Stats.calculatePercentage(total,netSecondsSpirituality);
        double exercisePercentage = Stats.calculatePercentage(total,netSecondsExercise);
        double restPercentage = Stats.calculatePercentage(total,netSecondsRest);
        double readingPercentage = Stats.calculatePercentage(total,netSecondsReading);
        double writingPercentage = Stats.calculatePercentage(total,netSecondsWriting);
        double artsPercentage = Stats.calculatePercentage(total,netSecondsArts);
        double socialPercentage = Stats.calculatePercentage(total,netSecondsSocial);
        double mediaPercentage = Stats.calculatePercentage(total,netSecondsMedia);
        double servicePercentage = Stats.calculatePercentage(total,netSecondsService);
        double miscellaneousPercentage = Stats.calculatePercentage(total,netSecondsMiscellaneous);


        piChartData.clear();
        piChartData.add(new PieChart.Data("Study "+roundPercentage(studyPercentage)  + "%", studyPercentage));
        piChartData.add(new PieChart.Data("Entertainment " + roundPercentage(entertainmentPercentage) + "%", entertainmentPercentage));
        piChartData.add(new PieChart.Data("Spirituality " + roundPercentage(spiritualityPercentage) + "%", spiritualityPercentage));
        piChartData.add(new PieChart.Data("Exercise " + roundPercentage(exercisePercentage) + "%", exercisePercentage));
        piChartData.add(new PieChart.Data("Rest " + roundPercentage(restPercentage) + "%", restPercentage));
        piChartData.add(new PieChart.Data("Reading " + roundPercentage(readingPercentage) + "%", readingPercentage));
        piChartData.add(new PieChart.Data("Writing " + roundPercentage(writingPercentage) + "%", writingPercentage));
        piChartData.add(new PieChart.Data("Arts" + roundPercentage(artsPercentage) + "%", artsPercentage));
        piChartData.add(new PieChart.Data("Social" + roundPercentage(socialPercentage) + "%", socialPercentage));
        piChartData.add(new PieChart.Data("Media" + roundPercentage(mediaPercentage) + "%", mediaPercentage));
        piChartData.add(new PieChart.Data("Service" + roundPercentage(servicePercentage) + "%", servicePercentage));
        piChartData.add(new PieChart.Data("Miscellaneous" + roundPercentage(miscellaneousPercentage) + "%", miscellaneousPercentage));
    }

    public void onMouseHover(){

    }

    public void onMouseExit(){

    }


    public void onScroll(ScrollEvent e){
        double motherPaneLayoutY = motherPane.getLayoutY();
        double change = e.getDeltaY();

        if (motherPaneLayoutY + change < 10 && motherPaneLayoutY + change > -200){
            motherPane.setLayoutY(motherPaneLayoutY + change);
        }

    }


    public void goToScheduleCreator(){
        App.sceneNavigationModel.gotoScene(App.scheduleCreator, App.piChart);
    }

    public void goToMain(){
        App.sceneNavigationModel.gotoScene(App.stats, App.piChart);
    }


    public static double roundPercentage(double raw) {
        raw *= 10;
        int base = (int) raw;
        double remainder = raw - base;

        if (remainder >= 0.5) {
            base += 1;
        }

        return (double) base / 10;
    }



}
