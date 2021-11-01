package main.Controllers.Timeline;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import main.Controllers.PrototypeController;
import main.Controllers.Stats.InfographicsNavigationTab.InfographicsNavigationTab;
import main.Models.DBModels.ArchiveDBModel;
import main.Models.DateTimeModel;
import main.resources.SceneNavigationModel;
import main.Models.SearchModel;
import main.Controllers.Stats.Stats;
import main.Controllers.Stats.Table;
import main.Utility.Activity;
import main.Utility.Cycler;
import main.App;

import java.io.IOException;
import java.util.*;

public class Editor extends PrototypeController {

    @FXML
    Button goToCreatorButton;
    @FXML
    TextArea noteTextArea;

    private int startTimeSecs;
    private int endTimeSecs;

    private int colorLineHeight = 125;
    private int timeLineHeight = 17;


    private int nodeStartPointY = 6;
    private int nodeEndPointY = -6;


    public static final int WIDTH_OF_5_MINS = 120;
    public static final double WIDTH_OF_1_SEC = 0.4;
    public static final int FIVE_MINS_IN_SECONDS = 300;
    private double dateChangePoint = -1;


    private String firstDate = "";
    private String lastDate = "";


    @FXML
    Pane grandmotherPane;

    @FXML
    VBox motherPane;

    @FXML
    Pane backgroundPane;


    @FXML
    Pane portalContainerPane;
    @FXML
    HBox portalPane;
    @FXML
    Text testText;

    @FXML
    GridPane allContainerPane;

    @FXML
    Pane yearPane;
    @FXML
    Label dateDisplay;


    @FXML
    HBox colorPane;

    @FXML
    HBox nodePane;

    @FXML
    HBox timePane;


    @FXML
    Pane detailContainerPane;
    @FXML
    BorderPane detailPane;
    @FXML
    TextField categoryTitlePicker;
    @FXML
    TextField specificField;
    @FXML
    Label durationLabel;
    @FXML
    Label beganAt;
    @FXML
    Label finishedAt;



    ObservableList suggestedNamesObservableList = FXCollections.observableArrayList();
    @FXML
    ListView nameSuggestions;




    double scrollableWidth;

    HashMap colorMap = Stats.colorMap;


    int selectedIndex;


    public void initialize() {

        //add navigation tab
        InfographicsNavigationTab navTab = new InfographicsNavigationTab();
        grandmotherPane.getChildren().add(navTab);
        navTab.controller.motherPane.setTranslateY(400);
        navTab.setSelectedInfographic("timeLine");

        navTab.setLayoutX(1352);
        navTab.setLayoutY(106);



        //set prototype property 'gotoCreatorButton'
        setGoToCreatorButton(goToCreatorButton);


        nameSuggestions.setPrefWidth(200);
        nameSuggestions.setItems(suggestedNamesObservableList);
        grandmotherPane.getChildren().remove(nameSuggestions);


        detailPane.setOpacity(0);
        detailPane.setDisable(true);

        categoryTitlePicker.setPromptText("Category");


        startTimeSecs = 0;
        endTimeSecs = 0;


        dateDisplay.setText(firstDate);



        addActivities(ArchiveDBModel.archive);
        computeAndSetScrollableWidth();
        resizePanes(scrollableWidth);
        makeTimeLine(startTimeSecs, endTimeSecs);
        makeNodes(getNumNodes());
        configureDateChangePoint(startTimeSecs - startTimeSecs % FIVE_MINS_IN_SECONDS);
        setPosition(TimeLine.currentLayout);

        suggestedNamesObservableList.add("mama");
        suggestedNamesObservableList.add("dada");
    }




    @FXML
    public void undo(){

        if (App.editLog.canGoBack()){
            ArrayList<Activity> prevArchive = App.editLog.mostRecentLog();

            ArchiveDBModel.archive = prevArchive;

            colorPane.getChildren().clear();

            addActivities(ArchiveDBModel.archive);

            detailPane.setOpacity(0);
            detailPane.setDisable(true);
        }
        else {
            flashCantUnOrRedo(true);
        }

    }




    @FXML
    public void redo(){


        if (App.editLog.canGoForward()){
            ArrayList<Activity> nextArchive = App.editLog.forwardLog();


            ArchiveDBModel.archive = nextArchive;

            colorPane.getChildren().clear();

            addActivities(nextArchive);
        }
        else {
            flashCantUnOrRedo(false);
        }
    }


    public void flashCantUnOrRedo(boolean isUndo){

        Bounds referenceBounds = categoryTitlePicker.localToScreen(categoryTitlePicker.getLayoutBounds());

        Label notification = new Label();

        notification.setFont(new Font(50));
        notification.setStyle("-fx-text-fill: red;");
        notification.setOpacity(0.3);

        if (isUndo){
            notification.setText("this is your first edit");
        }
        else {
            notification.setText("this is your last edit");
        }

        new Thread( new Runnable() {
            @Override
            public void run() {

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        grandmotherPane.getChildren().add(notification);

                        notification.setLayoutX(referenceBounds.getMinX());
                        notification.setLayoutY(referenceBounds.getMinY() - 400);
                    }
                });


                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        grandmotherPane.getChildren().remove(notification);
                    }
                });

            }
        }).start();


    }





    Cycler cycler = new Cycler();


    boolean switchModeOn = false;
    @FXML
    public void handleCategoryEdit(KeyEvent event) {
        String input = event.getCharacter();

        if (event.getCharacter().equals(" ")) {
            if (cycler.getItems().isEmpty()) {
                categoryTitlePicker.setText("");
            } else {
                editCategory(cycler.nextItem());
            }
        } else {
            updateCycler(input);
            editCategory(cycler.nextItem());
        }
    }




    private void updateCycler(String input){

        input = input.toUpperCase();


        cycler.clear();

        for (String category : Stats.categorySequence){
            if (category.startsWith(input)){
                cycler.addNewItem(category);
            }
        }

        if (cycler.getItems().isEmpty()){
            cycler.setItems((ArrayList<String>) Stats.categorySequence.clone());
        }
    }


    boolean isFirstEdit = true;
    private void editCategory(String newCat){

        if (isFirstEdit){
            App.editLog.addLog(ArchiveDBModel.archive);
            isFirstEdit = false;
        }

        int colorIndex = Stats.categorySequence.indexOf(newCat);
        String newColor = Stats.textColorSequence.get(colorIndex);
        ((TimeLineActivityPeriod)colorPane.getChildren().get(selectedIndex)).getActivityLabel().setStyle("-fx-background-color:" + Stats.colorMap.get(newCat) + "; -fx-alignment: center; -fx-border-color: white; fx-border-width: 1;");
        if (newColor != null){
            categoryTitlePicker.setStyle("-fx-alignment: center; -fx-text-fill:" + newColor);
        }
        else {
            categoryTitlePicker.setStyle("-fx-alignment: center; -fx-text-fill: black");
        }
        categoryTitlePicker.setText(newCat);

        ArchiveDBModel.archive.get(selectedIndex).setCategory(newCat);
        App.editLog.addLog(ArchiveDBModel.archive);
    }



    @FXML
    public void handleNameEdit(KeyEvent event){
        String newName = specificField.getText();

        changeName(newName);


        updateSuggestedNames(newName);

        if (!suggestedNamesObservableList.isEmpty()){

            nameSuggestions.scrollTo(suggestedNamesObservableList.get(suggestedNamesObservableList.size() - 1));

            if (!grandmotherPane.getChildren().contains(nameSuggestions)){
                grandmotherPane.getChildren().add(nameSuggestions);
            }
        } else {
            hideSuggestedNames();
        }
    }


    @FXML
    public void editNote(){
        ArchiveDBModel.archive.get(selectedIndex).setNote(noteTextArea.getText());
    }



    private void updateSuggestedNames(String newName){

        SearchModel.searchPastActivityListForNameAndLoadToObservableListInReverse(newName, suggestedNamesObservableList, "");

        //remove the mysterious space in suggested list

            double suggestionsListHeight = suggestedNamesObservableList.size() * 23 + 20;
            if (suggestionsListHeight > 400) {
                suggestionsListHeight = 400;
            }
            nameSuggestions.setPrefHeight(suggestionsListHeight);

            Bounds fieldBounds = specificField.localToScene(specificField.getLayoutBounds());
            nameSuggestions.setLayoutX(fieldBounds.getMinX());
            nameSuggestions.setLayoutY(fieldBounds.getMinY() - nameSuggestions.getPrefHeight() + 3);

            suggestedNamesObservableList.remove(newName);


            specificField.getStyleClass().add("text-field-with-reverse-suggestions");

    }




    @FXML
    public void autoCompleteName() throws ConcurrentModificationException {


        String selectedName = (String)nameSuggestions.getSelectionModel().getSelectedItem();
        changeName(selectedName);
        specificField.setText(selectedName);
        hideSuggestedNames();
    }





    @FXML
    public void tellMousePos(MouseEvent e){

    }

    private void changeName(String newName){
        App.editLog.addLog(ArchiveDBModel.archive);

        Label givenEpoch = ((TimeLineActivityPeriod) colorPane.getChildren().get(selectedIndex)).activityLabel;

        givenEpoch.setText(newName);


        Activity givenActivity = ArchiveDBModel.archive.get(selectedIndex);


        givenActivity.setName(newName);


        changeFontUntilTextFitsLabel(givenEpoch);


        App.editLog.addLog(ArchiveDBModel.archive);

    }



    public void expandLeft(double deltaX){

        if (selectedIndex != 0){
            deltaX = Math.abs(deltaX);
            int deltaSecs = (int)(deltaX / WIDTH_OF_1_SEC);

            int neighborIndex = selectedIndex - 1;

            Activity selected = ArchiveDBModel.archive.get(selectedIndex);
            Activity neighbor = ArchiveDBModel.archive.get(neighborIndex);


            if (neighbor.getDurationSeconds() > 1){
                int selectedStartTime = selected.getStartTimeSecs();
                int selectedDuration = selected.getDurationSeconds();


                int neighborEndTime = neighbor.getEndTimeSecs();
                int neighborDuration = neighbor.getDurationSeconds();


                selectedDuration += deltaSecs;
                neighborDuration -= deltaSecs;

                selectedStartTime -= deltaSecs;
                neighborEndTime -= deltaSecs;

                selected.setStartTimeSecs(selectedStartTime);
                selected.setDurationSeconds(selectedDuration);

                neighbor.setEndTimeSecs(neighborEndTime);
                neighbor.setDurationSeconds(neighborDuration);



                Label selectedDisplay = ((TimeLineActivityPeriod)colorPane.getChildren().get(selectedIndex)).getActivityLabel();
                Label neighborDisplay = ((TimeLineActivityPeriod) colorPane.getChildren().get(neighborIndex)).getActivityLabel();

                selectedDisplay.setPrefWidth(selectedDuration * WIDTH_OF_1_SEC);
                selectedDisplay.setMaxWidth(selectedDuration * WIDTH_OF_1_SEC);
                selectedDisplay.setMinWidth(selectedDuration * WIDTH_OF_1_SEC);

                neighborDisplay.setPrefWidth(neighborDuration * WIDTH_OF_1_SEC);
                neighborDisplay.setMaxWidth(neighborDuration * WIDTH_OF_1_SEC);
                neighborDisplay.setMinWidth(neighborDuration * WIDTH_OF_1_SEC);


                changeFontUntilTextFitsLabel(selectedDisplay);
                changeFontUntilTextFitsLabel(neighborDisplay);


                upDateInfoPanel(selectedStartTime, selected.getEndTimeSecs(), selectedDuration);

            }







        }

    }



    //check
    public void expandRight(double deltaX){

        if (selectedIndex != ArchiveDBModel.archive.size() - 1){
            deltaX = Math.abs(deltaX);
            int deltaSecs = (int)(deltaX / WIDTH_OF_1_SEC);

            int neighborIndex = selectedIndex + 1;

            Activity selected = ArchiveDBModel.archive.get(selectedIndex);
            Activity neighbor = ArchiveDBModel.archive.get(neighborIndex);



            if (neighbor.getDurationSeconds() > 1){
                int selectedEndTime = selected.getEndTimeSecs();
                int selectedDuration = selected.getDurationSeconds();


                int neighborStartTime = neighbor.getStartTimeSecs();
                int neighborDuration = neighbor.getDurationSeconds();


                selectedDuration += deltaSecs;
                neighborDuration -= deltaSecs;

                selectedEndTime += deltaSecs;
                neighborStartTime += deltaSecs;

                selected.setEndTimeSecs(selectedEndTime);
                selected.setDurationSeconds(selectedDuration);

                neighbor.setStartTimeSecs(neighborStartTime);
                neighbor.setDurationSeconds(neighborDuration);



                Label selectedDisplay = ((TimeLineActivityPeriod)colorPane.getChildren().get(selectedIndex)).getActivityLabel();
                Label neighborDisplay = ((TimeLineActivityPeriod) colorPane.getChildren().get(neighborIndex)).getActivityLabel();

                selectedDisplay.setPrefWidth(selectedDuration * WIDTH_OF_1_SEC);
                selectedDisplay.setMaxWidth(selectedDuration * WIDTH_OF_1_SEC);
                selectedDisplay.setMinWidth(selectedDuration * WIDTH_OF_1_SEC);

                neighborDisplay.setPrefWidth(neighborDuration * WIDTH_OF_1_SEC);
                neighborDisplay.setMaxWidth(neighborDuration * WIDTH_OF_1_SEC);
                neighborDisplay.setMinWidth(neighborDuration * WIDTH_OF_1_SEC);

                changeFontUntilTextFitsLabel(selectedDisplay);
                changeFontUntilTextFitsLabel(neighborDisplay);

                upDateInfoPanel(selected.getStartTimeSecs(), selectedEndTime, selectedDuration);
            }







        }

    }


    //check
    public void shrinkRight(double deltaX){


        deltaX = Math.abs(deltaX);
        int deltaSecs = (int)(deltaX / WIDTH_OF_1_SEC);

        int neighborIndex = selectedIndex + 1;

        Activity selected = ArchiveDBModel.archive.get(selectedIndex);
        Activity neighbor = ArchiveDBModel.archive.get(neighborIndex);


        if (selected.getDurationSeconds() > 1){
            int selectedEndTime = selected.getEndTimeSecs();
            int selectedDuration = selected.getDurationSeconds();


            int neighborStartTime = neighbor.getStartTimeSecs();
            int neighborDuration = neighbor.getDurationSeconds();


            selectedDuration -= deltaSecs;
            neighborDuration += deltaSecs;

            selectedEndTime -= deltaSecs;
            neighborStartTime -= deltaSecs;

            selected.setEndTimeSecs(selectedEndTime);
            selected.setDurationSeconds(selectedDuration);

            neighbor.setStartTimeSecs(neighborStartTime);
            neighbor.setDurationSeconds(neighborDuration);



            Label selectedDisplay = ((TimeLineActivityPeriod)colorPane.getChildren().get(selectedIndex)).getActivityLabel();
            Label neighborDisplay = ((TimeLineActivityPeriod) colorPane.getChildren().get(neighborIndex)).getActivityLabel();

            selectedDisplay.setPrefWidth(selectedDuration * WIDTH_OF_1_SEC);
            selectedDisplay.setMaxWidth(selectedDuration * WIDTH_OF_1_SEC);
            selectedDisplay.setMinWidth(selectedDuration * WIDTH_OF_1_SEC);

            neighborDisplay.setPrefWidth(neighborDuration * WIDTH_OF_1_SEC);
            neighborDisplay.setMaxWidth(neighborDuration * WIDTH_OF_1_SEC);
            neighborDisplay.setMinWidth(neighborDuration * WIDTH_OF_1_SEC);



            changeFontUntilTextFitsLabel(selectedDisplay);
            changeFontUntilTextFitsLabel(neighborDisplay);



            upDateInfoPanel(selected.getStartTimeSecs(), selectedEndTime, selectedDuration);
        }








    }



    public void shrinkLeft(double deltaX){


        deltaX = Math.abs(deltaX);
        int deltaSecs = (int)(deltaX / WIDTH_OF_1_SEC);

        int neighborIndex = selectedIndex - 1;

        Activity selected = ArchiveDBModel.archive.get(selectedIndex);
        Activity neighbor = ArchiveDBModel.archive.get(neighborIndex);


        if (selected.getDurationSeconds() > 1){
            int selectedStartTime = selected.getStartTimeSecs();
            int selectedDuration = selected.getDurationSeconds();


            int neighborEndTime = neighbor.getEndTimeSecs();
            int neighborDuration = neighbor.getDurationSeconds();


            selectedDuration -= deltaSecs;
            neighborDuration += deltaSecs;

            selectedStartTime += deltaSecs;
            neighborEndTime += deltaSecs;

            selected.setStartTimeSecs(selectedStartTime);
            selected.setDurationSeconds(selectedDuration);

            neighbor.setEndTimeSecs(neighborEndTime);
            neighbor.setDurationSeconds(neighborDuration);



            Label selectedDisplay = ((TimeLineActivityPeriod)colorPane.getChildren().get(selectedIndex)).getActivityLabel();
            Label neighborDisplay = ((TimeLineActivityPeriod) colorPane.getChildren().get(neighborIndex)).getActivityLabel();

            selectedDisplay.setPrefWidth(selectedDuration * WIDTH_OF_1_SEC);
            selectedDisplay.setMaxWidth(selectedDuration * WIDTH_OF_1_SEC);
            selectedDisplay.setMinWidth(selectedDuration * WIDTH_OF_1_SEC);

            neighborDisplay.setPrefWidth(neighborDuration * WIDTH_OF_1_SEC);
            neighborDisplay.setMaxWidth(neighborDuration * WIDTH_OF_1_SEC);
            neighborDisplay.setMinWidth(neighborDuration * WIDTH_OF_1_SEC);



            changeFontUntilTextFitsLabel(selectedDisplay);
            changeFontUntilTextFitsLabel(neighborDisplay);



            upDateInfoPanel(selectedStartTime, selected.getEndTimeSecs(), selectedDuration);
        }








    }


    private void upDateInfoPanel(int newStartTimeSecs, int newEndTimeSecs, int newDuration){
        beganAt.setText( getAMPMTimeFromSeconds(newStartTimeSecs));
        finishedAt.setText(getAMPMTimeFromSeconds(newEndTimeSecs));
        durationLabel.setText(DateTimeModel.parseDurationToString(newDuration, false));
    }




    double prevX = 0;
    boolean isNewStart = true;
    private void addDragHandler(Label activity) {

        activity.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                App.editLog.addLog(ArchiveDBModel.archive);
            }
        });

        activity.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {


                selectedIndex = colorPane.getChildren().indexOf(activity.getParent());

                double mousePos = event.getScreenX();
                Bounds activityBounds = activity.localToScreen(activity.getBoundsInLocal());
                double activityPos = (activityBounds.getMinX() + activityBounds.getMaxX()) / 2;


                double deltaX;
                if (isNewStart) {
                    deltaX = 0;
                } else {
                    deltaX = mousePos - prevX;
                }
                isNewStart = false;

                double calibratedDeltaX = ((int)(deltaX / WIDTH_OF_1_SEC)) * WIDTH_OF_1_SEC;

                if (mousePos < activityPos) {
                    if (deltaX < 0){
                        expandLeft(calibratedDeltaX);
                    }
                    else if (deltaX > 0){
                        shrinkLeft(calibratedDeltaX);
                    }
                }
                else if (mousePos > activityPos){
                    if(deltaX < 0){
                        shrinkRight(calibratedDeltaX);
                    }
                    else if (deltaX > 0){
                        expandRight(calibratedDeltaX);
                    }
                }


                prevX = event.getScreenX();
            }
        });


        activity.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                isNewStart = true;

                App.editLog.addLog(ArchiveDBModel.archive);

            }
        });


    }





    @FXML
    public void insertActivity(){
        Activity selectedActivity = ArchiveDBModel.archive.get(selectedIndex);
        Label selectedLabel = ((TimeLineActivityPeriod) colorPane.getChildren().get(selectedIndex)).getActivityLabel();


        int currentSecs = selectedActivity.getDurationSeconds();
        int existingSecs = currentSecs / 2;
        int newSecs = currentSecs - existingSecs;

        int existingEndtime = selectedActivity.getEndTimeSecs();

        int newStartTime = existingEndtime - newSecs;
        int newEndTime = existingEndtime;


        Activity newActivity = new Activity("new","New", "", newSecs, newStartTime , newEndTime, selectedActivity.getDate());

        selectedActivity.setDurationSeconds(existingSecs);

        existingEndtime = newStartTime - 1;
        selectedActivity.setEndTimeSecs(existingEndtime);

        ArchiveDBModel.archive.add(selectedIndex+1, newActivity);

        selectedLabel.setPrefWidth(existingSecs * WIDTH_OF_1_SEC);
        selectedLabel.setMaxWidth(existingSecs * WIDTH_OF_1_SEC);
        selectedLabel.setMinWidth(existingSecs * WIDTH_OF_1_SEC);

        TimeLineActivityPeriod period = new TimeLineActivityPeriod(newActivity, noteTextArea, true);

        Label newLabel = period.getActivityLabel();
        newLabel.setText("new");
        newLabel.setStyle("-fx-alignment: center; -fx-border-color:  rgb(246, 255, 226); fx-border-width: 1; -fx-background-color: white");
        newLabel.setFont(new Font(30));
        newLabel.setPrefWidth(newSecs * WIDTH_OF_1_SEC);
        newLabel.setPrefHeight(colorLineHeight);
        addClickHandler(period);
        addDragHandler(newLabel);
        colorPane.getChildren().add(selectedIndex+1, period);
        changeFontUntilTextFitsLabel(newLabel);


        changeFontUntilTextFitsLabel(selectedLabel);
        changeFontUntilTextFitsLabel(newLabel);


        updateSelectedEpoch(newActivity);


        selectedIndex = ArchiveDBModel.archive.indexOf(selectedActivity);

        App.editLog.addLog(ArchiveDBModel.archive);

    }

    @FXML
    public void deleteActivity(){

        if (selectedIndex != 0){
            int mergerIndex = selectedIndex - 1;

            Activity merger = ArchiveDBModel.archive.get(mergerIndex);
            Activity engulfed = ArchiveDBModel.archive.get(selectedIndex);

            int mergerEndTime;
            int mergerDuration = merger.getDurationSeconds();


            int engulfedEndTime = engulfed.getEndTimeSecs();
            int engulfedDuration = engulfed.getDurationSeconds();

            ArchiveDBModel.archive.remove(engulfed);

            mergerEndTime = engulfedEndTime;
            mergerDuration += engulfedDuration;


            merger.setEndTimeSecs(mergerEndTime);
            merger.setDurationSeconds(mergerDuration);


            Label mergerLabel = ((TimeLineActivityPeriod) colorPane.getChildren().get(mergerIndex)).activityLabel;
            System.out.println("engulfed: " + colorPane.getChildren().get(selectedIndex));
            TimeLineActivityPeriod engulfedPeriod = (TimeLineActivityPeriod) colorPane.getChildren().get(selectedIndex);

            colorPane.getChildren().remove(engulfedPeriod);

            mergerLabel.setPrefWidth(mergerDuration * WIDTH_OF_1_SEC);
            mergerLabel.setMaxWidth(mergerDuration * WIDTH_OF_1_SEC);
            mergerLabel.setMinWidth(mergerDuration * WIDTH_OF_1_SEC);

            changeFontUntilTextFitsLabel(mergerLabel);

            updateSelectedEpoch(merger);

            selectedIndex = mergerIndex;


            App.editLog.addLog(ArchiveDBModel.archive);

        }
    }


    private void updateSelectedEpoch(Activity epoch){

        String mergerCat = epoch.getCategory();

        categoryTitlePicker.setStyle("-fx-alignment:center; -fx-text-fill:" + Stats.colorMap.get(mergerCat) + ";");
        categoryTitlePicker.setText(mergerCat);
        specificField.setText(epoch.getName());

        upDateInfoPanel(epoch.getStartTimeSecs(), epoch.getEndTimeSecs(), epoch.getDurationSeconds());

    }






    public void computeAndSetScrollableWidth() {
        scrollableWidth = (endTimeSecs - startTimeSecs) * WIDTH_OF_1_SEC + 600;
        if (scrollableWidth < 1280) {
            scrollableWidth = 1280;
        }
    }





    private void makeTimeLine(int startTiempo, int endTiempo) {
        int beginTime = startTiempo - startTiempo % FIVE_MINS_IN_SECONDS;
        allContainerPane.setMargin(colorPane, new Insets(0, 0, 0, 200 + (startTiempo - beginTime) * WIDTH_OF_1_SEC));
        int finishTime = FIVE_MINS_IN_SECONDS * (endTiempo / FIVE_MINS_IN_SECONDS) + FIVE_MINS_IN_SECONDS;

        if (!ArchiveDBModel.archive.isEmpty()) {
            for (int currentTime = beginTime; currentTime <= finishTime; currentTime += FIVE_MINS_IN_SECONDS) {
                Label timeLabel = new Label(getAMPMTimeFromSeconds(currentTime));
                timeLabel.setPrefWidth(WIDTH_OF_5_MINS);
                timeLabel.setPrefHeight(timeLineHeight);
                timeLabel.setStyle("-fx-alignment: center");
                timePane.getChildren().add(timeLabel);

            }
        }

    }


    public void addActivities(ArrayList<Activity> activities) {

        if (! activities.isEmpty()){
            Activity firstAct = activities.get(0);
            Activity lastAct = activities.get(activities.size()-1);
            if (ArchiveDBModel.archive.size() == 1) {
                this.startTimeSecs = firstAct.getStartTimeSecs();
                this.endTimeSecs = firstAct.getEndTimeSecs();
                firstDate = firstAct.getDate();
                lastDate = decrementDate(firstDate);
            } else if (!ArchiveDBModel.archive.isEmpty()) {
                this.startTimeSecs = firstAct.getStartTimeSecs();
                this.endTimeSecs = ArchiveDBModel.archive.get(ArchiveDBModel.archive.size() - 1).getEndTimeSecs();
                firstDate = firstAct.getDate();
                lastDate = ArchiveDBModel.archive.get(ArchiveDBModel.archive.size() - 1).getDate();
            }

            if (endTimeSecs - startTimeSecs < FIVE_MINS_IN_SECONDS) {
                endTimeSecs += FIVE_MINS_IN_SECONDS;
            }

            for (Activity activity : activities) {


                TimeLineActivityPeriod period = new TimeLineActivityPeriod(activity, noteTextArea, true);

                Label newActivity = period.getActivityLabel();
                newActivity.setStyle("-fx-background-color:" + Stats.colorMap.get(activity.getCategory()) + "; -fx-alignment: center; -fx-border-color:  rgb(246, 255, 226); fx-border-width: 1;");
                newActivity.setPrefHeight(colorLineHeight);
                newActivity.setPrefWidth(activity.getDurationSeconds() * WIDTH_OF_1_SEC);
                newActivity.setText(activity.getName());
                addClickHandler(period);
                addDragHandler(newActivity);


                colorPane.getChildren().add(period);

                changeFontUntilTextFitsLabel(newActivity);

                if (activity.getEndTimeSecs() > endTimeSecs) {
                    endTimeSecs += DateTimeModel.SECONDS_IN_A_DAY;
                }


            }
        }




    }


    public static String decrementDate(String date) {
        return date.substring(0, date.length() - 2) + (Integer.parseInt(date.substring(date.length() - 2)) - 1);
    }



    private void addClickHandler(TimeLineActivityPeriod period) {

        Activity represented = period.thisActivity;

        EventHandler<MouseEvent> clickHandler = (MouseEvent event) -> {
            if (!ArchiveDBModel.archive.isEmpty()) {
                String activityCategory = represented.getCategory();

                String color = "white";
                int colorIndex = Stats.categorySequence.indexOf(activityCategory);
                try {
                    color = Stats.textColorSequence.get(colorIndex);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                categoryTitlePicker.setStyle("-fx-alignment:center; -fx-text-fill:" + color + ";");
                categoryTitlePicker.setText(activityCategory);


                durationLabel.setText(DateTimeModel.parseDurationToString(represented.getDurationSeconds(), false));

                specificField.setText(represented.getName());

                beganAt.setText(getAMPMTimeFromSeconds(represented.getStartTimeSecs()));

                finishedAt.setText(getAMPMTimeFromSeconds(represented.getEndTimeSecs()));

                selectedIndex = ArchiveDBModel.archive.indexOf(represented);

                detailPane.setOpacity(1);
                detailPane.setDisable(false);

                hideSuggestedNames();
            }
        };



        period.getActivityLabel().setOnMouseClicked(clickHandler);
        period.toDoOnNoteTagClicked = clickHandler;

    }



    private void hideSuggestedNames(){

        grandmotherPane.getChildren().remove(nameSuggestions);

        specificField.getStyleClass().removeAll("text-field-with-reverse-suggestions");
        specificField.getStyleClass().add("search-text-field");
    }

    @FXML
    public void onScreenClicked(MouseEvent event) {

        hideSuggestedNames();

        if (!(event.getTarget() instanceof ImageView)) { //check that event didn't come from noteTag: show and hide? what a waste!
            if (noteTextArea.isVisible()){
                confirmOrRemoveNoteTag();
            }
            noteTextArea.setVisible(false);

        }

        Node target = (Node)event.getTarget();
        if (target instanceof Text){
            target = target.getParent();
        }

        if (TimeLineActivityPeriod.previousSelectedPeriod != null && target != TimeLineActivityPeriod.previousSelectedPeriod.getActivityLabel()) { //if user clicks anything else but the activity they are editing

            //update the note graphics of selected period
            confirmOrRemoveNoteTag();

        }

        //update previously selected target
        Node targetParent = target.getParent();
        if (targetParent instanceof TimeLineActivityPeriod){
            TimeLineActivityPeriod.previousSelectedPeriod = (TimeLineActivityPeriod) targetParent;
        }
    }

    private void confirmOrRemoveNoteTag(){
        Activity previousSelectedActivity = TimeLineActivityPeriod.previousSelectedPeriod.thisActivity;
        String note = previousSelectedActivity.getNote();

        if (!(note == null) && !note.isBlank()) {
            System.out.println("note added to selected index activity");
            TimeLineActivityPeriod.previousSelectedPeriod.addNoteTag();
            TimeLineActivityPeriod.previousSelectedPeriod.noteTag.setOpacity(1); // assert note tag
            TimeLineActivityPeriod.previousSelectedPeriod.noteTagIsPrompt = false;
        } else {
            System.out.println("no content - note removed");
            TimeLineActivityPeriod.previousSelectedPeriod.removeNoteTag(); //remove note tag
            TimeLineActivityPeriod.previousSelectedPeriod.noteTagIsPrompt = true;
        }
    }

//    private void changeFontUntilTextFitsLabel(Label label, double deltaX) {
//
//        deltaX = Math.abs(deltaX);
//
//        String text = label.getText();
//        testText.setText(text);
//        testText.setFont(label.getFont());
//
//
//        double textWidth = testText.getLayoutBounds().getWidth();
//        double labelWidth = label.localToScreen(label.getBoundsInLocal()).getWidth();
//
//
//        if (labelWidth < textWidth){
//            label.setFont(new Font(label.getFont().getSize() * ((labelWidth - deltaX) / labelWidth)));
//        }
//        else if (label.getFont().getSize() < 30){
//            label.setFont(new Font(label.getFont().getSize() * ((labelWidth + deltaX) / labelWidth)));
//        }
//
//    }

    private void changeFontUntilTextFitsLabel(Label label) {

        label.setFont(new Font(100));

        String text = label.getText();
        testText.setText(text);
        testText.setFont(new Font(100));

        double textWidth = testText.getLayoutBounds().getWidth();
        double labelWidth = label.getPrefWidth() - 6;

        double resizeFactor = labelWidth / textWidth * 0.9;

        if (labelWidth < textWidth){
            label.setFont(new Font(label.getFont().getSize() * resizeFactor));
        }



    }


    private void configureDateChangePoint(int beginTime) {
        dateChangePoint = (double) (DateTimeModel.SECONDS_IN_A_DAY - beginTime) * WIDTH_OF_1_SEC + 200;

        Line upperBorder = new Line(0, 0, 0, 200);
        upperBorder.setLayoutY(0);
        upperBorder.setLayoutX(dateChangePoint);
        upperBorder.setStrokeWidth(3);


        Line marginBorder = new Line(0, 0, 0, 200);
        marginBorder.setLayoutY(0);
        marginBorder.setLayoutX(dateChangePoint);
        marginBorder.setStrokeWidth(3);


        Line bottomBorder = new Line(0, 0, 0, 350);
        bottomBorder.setLayoutY(0);
        bottomBorder.setLayoutX(dateChangePoint);
        bottomBorder.setStrokeWidth(3);


        portalContainerPane.getChildren().add(marginBorder);
        yearPane.getChildren().add(upperBorder);
        detailContainerPane.getChildren().add(bottomBorder);


    }


    private void makeNodes(int nodeNum){
        if (!ArchiveDBModel.archive.isEmpty()){
            for (int i = 0; i <= nodeNum; i++){
                Line nodeMarker = new Line(0,nodeStartPointY,0,nodeEndPointY);
                nodeMarker.setScaleX(2);
                nodePane.getChildren().add(nodeMarker);
            }
        }

    }

    public void resizePanes(double newWidth) {

        allContainerPane.setPrefWidth(newWidth);
        allContainerPane.setMaxWidth(newWidth);
        allContainerPane.setMaxWidth(newWidth);

        yearPane.setPrefWidth(newWidth);
        yearPane.setMaxWidth(newWidth);
        yearPane.setMinWidth(newWidth);

        colorPane.setPrefWidth(newWidth);
        colorPane.setMaxWidth(newWidth);
        colorPane.setMinWidth(newWidth);

        nodePane.setPrefWidth(newWidth);
        nodePane.setMaxWidth(newWidth);
        nodePane.setMinWidth(newWidth);

        timePane.setPrefWidth(newWidth);
        timePane.setMaxWidth(newWidth);
        timePane.setMinWidth(newWidth);

        detailContainerPane.setPrefWidth(newWidth);
        detailContainerPane.setMaxWidth(newWidth);
        detailContainerPane.setMinWidth(newWidth);

        portalContainerPane.setPrefWidth(newWidth);
        portalContainerPane.setMaxWidth(newWidth);
        portalContainerPane.setMinWidth(newWidth);

    }


    private int getNumNodes() {
        return (FIVE_MINS_IN_SECONDS * (endTimeSecs / FIVE_MINS_IN_SECONDS) + FIVE_MINS_IN_SECONDS - (startTimeSecs - startTimeSecs % FIVE_MINS_IN_SECONDS)) / FIVE_MINS_IN_SECONDS;
    }


    public void goToScheduleCreator() {
        Table.updateData(ArchiveDBModel.archive);
        Stats.currentTimelineLayoutInitialized = false;

        App.sceneNavigationModel.gotoScene(SceneNavigationModel.scheduleCreator, motherPane.getScene());
    }

    public void goToMain() {
        Table.updateData(ArchiveDBModel.archive);
        Stats.currentTimelineLayoutInitialized = false;

        App.sceneNavigationModel.gotoScene(SceneNavigationModel.stats, motherPane.getScene());
    }

    public void goToTimeLine() throws IOException {
        Table.updateData(ArchiveDBModel.archive);
        App.sceneNavigationModel.loadNewScene("FXML/Timeline/timeLine.fxml", motherPane.getScene());
    }


    public void onScroll(ScrollEvent e) {
        double motherPaneLayoutX = motherPane.getLayoutX();
        double change = e.getDeltaX() + (e.getDeltaY() * 15);

        if (motherPaneLayoutX + change < 10 && motherPaneLayoutX + change > -(scrollableWidth - TimeLine.screenWidth)) {
            double newLayoutX = motherPaneLayoutX + change;
            motherPane.setLayoutX(newLayoutX);
            portalPane.setLayoutX(newLayoutX);
            shiftPortals(-newLayoutX);
            shiftDateDisplay(-change);
            checkAndUpdateDate(dateDisplay.getLayoutX());
            noteTextArea.setLayoutX(noteTextArea.getLayoutX() + change);
//            hammerDownUpperBorderOnMeetingPortals();
            shiftDetails(change);
            shiftBackground(change);

            TimeLine.currentLayout = newLayoutX;

        }

    }


    private void setPosition(double layoutX){

        double change = layoutX - motherPane.getLayoutX();

        motherPane.setLayoutX(layoutX);
        portalPane.setLayoutX(layoutX);
        shiftPortals(-layoutX);
        shiftDateDisplay(-change);
        noteTextArea.setLayoutX(noteTextArea.getLayoutX() + change);
        checkAndUpdateDate(dateDisplay.getLayoutX());
//            hammerDownUpperBorderOnMeetingPortals();
        shiftDetails(change);
        shiftBackground(change);

    }

//    private void hammerDownUpperBorderOnMeetingPortals(){
//        double borderX = upperBorder.getLayoutX();
//        double portalX = portalPane.getLayoutX();
//        if (borderX > portalX && borderX < portalX + 393){
//            upperBorder.setStartY(0);
//        }
//        else {
//            upperBorder.setStartY(-86);
//        }
//    }


    private void checkAndUpdateDate(double currentLayoutX) {

        if (dateChangePoint != -1 && currentLayoutX < dateChangePoint) {
            dateDisplay.setText(firstDate);
        } else if (dateChangePoint != -1 && currentLayoutX > dateChangePoint) {
            dateDisplay.setText(lastDate);
        }
    }


    private void shiftPortals(double newLayoutX) {
        portalPane.setLayoutX(newLayoutX);
    }


    private void shiftDateDisplay(double change) {
        dateDisplay.setLayoutX(dateDisplay.getLayoutX() + change);
    }

    private void shiftDetails(double change) {
        detailPane.setLayoutX(detailPane.getLayoutX() - change);
    }

    private void shiftBackground(double change){
        backgroundPane.setLayoutX(backgroundPane.getLayoutX() - change);
    }



    public static String getAMPMTimeFromSeconds(int seconds) {

        int template;

        if (seconds > DateTimeModel.SECONDS_IN_A_DAY) {
            template = seconds - DateTimeModel.SECONDS_IN_A_DAY;
        } else {
            template = seconds;
        }


        int hh = template / 3600;

        template = template % 3600;

        int mm = (template / 60);

        template = template % 60;


        String minutes;
        if (mm == 0) {
            minutes = "00";
        } else if (mm < 10) {
            minutes = "0" + mm;
        } else {
            minutes = ((Integer) mm).toString();
        }


        if (hh == 0) {
            return "12" + ":" + minutes + " A.M.";
        } else if (hh == 12) {
            return hh + ":" + minutes + " P.M.";
        } else if (hh < 12) {
            return hh + ":" + minutes + " A.M.";
        }
        return hh - 12 + ":" + minutes + " P.M.";
    }


}



//

