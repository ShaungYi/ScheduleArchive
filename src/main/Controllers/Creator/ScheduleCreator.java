package main.Controllers.Creator;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import main.Controllers.Loader.Loader;
import main.Controllers.PrototypeController;
import main.Models.DBModels.ArchiveDBModel;
import main.Models.DBModels.ReadFromDBModel;
import main.Models.DateTimeModel;
import main.Models.SceneNavigationModel;
import main.Utility.Activity;
import main.App;
import main.Utility.StopWatch;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


public class ScheduleCreator extends PrototypeController {

    StopWatch stopWatch;


    @FXML
    BorderPane UniversalCommonAncestor;

    @FXML
    private Label stopWatchDisplay;
    @FXML



    private Label universalClockDisplay;
    @FXML
    Pane saveButton;



    static boolean isAM;



    @FXML
    private Label startTime;
    @FXML
    private Label startTimeAMPM;
    @FXML
    private Label endTime;
    @FXML
    private Label endTimeAMPM;


    @FXML
    Label noonRelationMarker;
    @FXML
    Label date;

    @FXML
    ToggleGroup ActivityTypes;

    @FXML
    ToggleButton Study;
    @FXML
    ToggleButton Entertainment;
    @FXML
    ToggleButton Spirituality;
    @FXML
    ToggleButton Exercise;
    @FXML
    ToggleButton Rest;
    @FXML
    ToggleButton Reading;
    @FXML
    ToggleButton Writing;
    @FXML
    ToggleButton Arts;
    @FXML
    ToggleButton Social;
    @FXML
    ToggleButton Media;
    @FXML
    ToggleButton Service;
    @FXML
    ToggleButton Miscellaneous;

    public void initialize(){




        stopWatch = new StopWatch();


        clockProcess.start();

        if (Loader.loadMode){
            Study.setDisable(true);
            Entertainment.setDisable(true);
            Spirituality.setDisable(true);
            Exercise.setDisable(true);
            Rest.setDisable(true);
            Reading.setDisable(true);
            Writing.setDisable(true);
            Arts.setDisable(true);
            Miscellaneous.setDisable(true);

            saveButton.setDisable(true);

        }
        else {
            startWatch();
        }


    }




    Runnable executeTicks = new Runnable() {
        @Override
        public void run() {

            while(true){

                waitUntilSynchronizedWithUniverse();

                Platform.runLater(() -> {

                    stopWatch.tick();

                    String display = stopWatch.makeDisplay();
                    stopWatchDisplay.setText(display);
                    endTime.setText(getCurrentAMPMTime());;
                    endTimeAMPM.setText(isAM ? "A.M." : "P.M.");
                });
            }
        }
    };


    Thread stopWatchProcess = new Thread(executeTicks);


    Runnable runClock = new Runnable() {
        @Override
        public void run() {

            while(true){

                waitUntilSynchronizedWithUniverse();
                isAM = LocalTime.now().getHour() < 12 && LocalTime.now().getHour() != 0;
                String currentAMPMtime = getCurrentAMPMTime();
                String currentDate = LocalDate.now().toString();

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        universalClockDisplay.setText(currentAMPMtime);
                        noonRelationMarker.setText(isAM ? "AM" : "PM");
                        date.setText(currentDate);

                    }
                });
            }
        }
    };

    Thread clockProcess = new Thread(runClock);

    static String convertToAMPM(int hour, int minute, int second){
        if (!isAM ){
            hour = hour - 12;
        }

        return  (hour < 10 ? "0"+hour : hour) + ":" + (minute < 10 ? "0"+minute : minute) + ":" + (second < 10 ? "0"+second : second);

    }

    static String getCurrentAMPMTime(){
        int hour = LocalTime.now().getHour();
        int minutes = LocalTime.now().getMinute();
        int seconds = LocalTime.now().getSecond();
        int nanos = LocalTime.now().getNano();


        isAM = LocalTime.now().getHour() < 12 && LocalTime.now().getHour() != 0;

        if (isAM){
            if(nanos > 900000000){
                return parseToDisplay(hour,minutes,seconds + 1);
            }
            else {
                return parseToDisplay(hour, minutes, seconds);
            }
        }
        else{
            if (hour == 0){
                return parseToDisplay(12, minutes, seconds + 1);
            }
            if (hour == 12){
                isAM = true;
                return parseToDisplay(12, minutes, seconds + 1);
            }
            if(nanos > 900000000){
                return parseToDisplay(hour-12, minutes, seconds + 1);
            }
            else {
                return parseToDisplay(hour-12, minutes, seconds);
            }

        }
    }


    static String parseToDisplay(int hour, int minute, int second){
        return (hour < 10 ? "0"+hour : hour) + ":" + (minute < 10 ? "0"+minute : minute) + ":" + (second < 10 ? "0"+second : second);
    }


    static void waitUntilSynchronizedWithUniverse(){
        int currentNanos = LocalTime.now().getNano();
        int untilNextTickNanos = 1000000000 - currentNanos;
        long untilNextTickMilis = (long) (untilNextTickNanos / 1000000.0);
        if (untilNextTickMilis == 0 || untilNextTickMilis > 1000){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                Thread.sleep(untilNextTickMilis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    public void startWatch() {
        stopWatch.reset();
        startTime.setText(getCurrentAMPMTime());
        startTimeAMPM.setText(isAM ? "A.M." : "P.M.");
        stopWatchProcess.start();
    }

    public void resetStopWatch(){
        stopWatch.reset();
        stopWatchDisplay.setText("00:00:00");
        startTime.setText(getCurrentAMPMTime());
        startTimeAMPM.setText(isAM ? "A.M." : "P.M.");
    }




    public static int getTimeInSeconds(int hours, int minutes, int seconds){
        return hours * 3600 + minutes * 60 + seconds;
    }




    public void proclaimActivityType(ActionEvent e){
        ToggleButton button = (ToggleButton) e.getSource();
        String id = button.getId();
        switch (id){
            case "Study":
                UniversalCommonAncestor.setStyle("-fx-background-color:aqua ;");
                break;

            case "Entertainment":
                UniversalCommonAncestor.setStyle("-fx-background-color:fuchsia ;");
                break;

            case "Spirituality":
                UniversalCommonAncestor.setStyle("-fx-background-color:gold ;");
                break;

            case "Exercise":
                UniversalCommonAncestor.setStyle("-fx-background-color:chartreuse ;");
                break;

            case "Rest":
                UniversalCommonAncestor.setStyle("-fx-background-color:bisque ;");
                break;

            case "Reading":
                UniversalCommonAncestor.setStyle("-fx-background-color:chocolate ;");
                break;

            case "Writing":
                UniversalCommonAncestor.setStyle("-fx-background-color:deepskyblue ;");
                break;

            case "Arts":
                UniversalCommonAncestor.setStyle("-fx-background-color:crimson ;");
                break;

            case "Social":
                UniversalCommonAncestor.setStyle("-fx-background-color:olive ;");
                break;

            case "Media":
                UniversalCommonAncestor.setStyle("-fx-background-color:green ;");
                break;

            case "Service":
                UniversalCommonAncestor.setStyle("-fx-background-color:coral ;");
                break;

            case "Miscellaneous":
                UniversalCommonAncestor.setStyle("-fx-background-color:blueviolet ;");
                break;
        }

        //update toggle style to selected
        ActivityTypes.getToggles().stream().map((toggle) -> (ToggleButton)toggle).forEach((togbut) -> {
            if (togbut.equals(button)){
                togbut.getStyleClass().add("toggle-button-selected");
            } else {
                togbut.getStyleClass().clear();
                togbut.getStyleClass().add("toggle-button");
            }
        });

    }

    @FXML

    public void goToStats(){
        App.sceneNavigationModel.gotoScene(SceneNavigationModel.stats, SceneNavigationModel.scheduleCreator);
    }

    public void goToSubmitField() throws IOException {
        App.sceneNavigationModel.loadNewScene("../resources/FXML/Creator/submitScreen.fxml", SceneNavigationModel.scheduleCreator);
    }





    @FXML
    public void processSubmission() throws NullPointerException, IOException {
        // auto resume when selectedDay is NULL and currentDay is equal to lastDay
        String currentDay = LocalDate.now().toString();


        if (DateTimeModel.selectedDay == null && currentDay.equals(DateTimeModel.getLastDay())) {
            DateTimeModel.selectedDay = currentDay;
            ArchiveDBModel.archive = ReadFromDBModel.readDay(currentDay);
            Loader.addNoData(stopWatch.getStartTimeSec());
        } else if (DateTimeModel.selectedDay == null) {
            DateTimeModel.selectedDay = currentDay;
        }

        String category;
        try {
            category = getSelectedActivityCategory(ActivityTypes);
        }catch (NullPointerException e){
            category = "null";
        }

        if (!category.equals("null") && stopWatch.getMeasuredTime() != 0){

            //reset toggle buttons
            ActivityTypes.getToggles().stream().map((toggle) -> (ToggleButton)toggle).forEach((togbut) -> {

                togbut.getStyleClass().clear();
                togbut.getStyleClass().add("toggle-button");

            });

            ArchiveDBModel.archive.add(new Activity("undefined", getSelectedActivityCategory(ActivityTypes), "", stopWatch.getMeasuredTime(), stopWatch.getStartTimeSec(), stopWatch.getEndTime(), LocalDate.now().toString()));
            resetStopWatch();
            ActivityTypes.getSelectedToggle().setSelected(false);
            UniversalCommonAncestor.setStyle("-fx-background-color: white");
            goToSubmitField();
        }


    }


    public String getSelectedActivityCategory(ToggleGroup group) {
        ToggleButton selected = (ToggleButton) group.getSelectedToggle();

        if (selected.equals(null)) {
            return null;
        } else if (selected.equals(Study)) {
            return "Study";
        } else if (selected.equals(Entertainment)) {
            return "Entertainment";
        } else if (selected.equals(Spirituality)) {
            return "Spirituality";
        } else if (selected.equals(Exercise)) {
            return "Exercise";
        } else if (selected.equals(Rest)) {
            return "Rest";
        } else if (selected.equals(Reading)) {
            return "Reading";
        } else if (selected.equals(Writing)) {
            return "Writing";
        } else if (selected.equals(Arts)) {
            return "Arts";
        } else if (selected.equals(Social)) {
            return "Social";
        } else if (selected.equals(Media)) {
            return "Media";
        } else if (selected.equals(Service)) {
            return "Service";
        } else if (selected.equals(Miscellaneous)) {
            return "Miscellaneous";
        }
        return "error: selected category does not exist";

    }

    public void terminateAllThreads(){
        stopWatchProcess.stop();
        clockProcess.stop();
    }

}
