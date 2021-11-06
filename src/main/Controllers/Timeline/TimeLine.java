package main.Controllers.Timeline;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.Label;
import main.Controllers.PrototypeController;
import main.Controllers.Stats.InfographicsNavigationTab.InfographicsNavigationTab;
import main.Controllers.Stats.Stats;
import main.Controllers.Stats.Table;
import main.Models.DBModels.ArchiveDBModel;
import main.Models.DateTimeModel;
import main.resources.SceneNavigationModel;
import main.Utility.Activity;
import main.App;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class TimeLine extends PrototypeController {

    @FXML public Pane grandmotherPane;
    @FXML TextArea noteTextArea;

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
    public static final int SECONDS_IN_A_DAY = DateTimeModel.SECONDS_IN_A_DAY;
    public static final double screenWidth = 1280.0;


    private String firstDate = "";
    private String lastDate = "";


    @FXML
    Pane backgroundPane;

    @FXML
    VBox motherPane;

    @FXML
    Button goToCreatorButton;


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
    Label categoryTitle;
    @FXML
    Label specificLabel;
    @FXML
    Label durationLabel;
    @FXML
    Label beganAt;
    @FXML
    Label finishedAt;


    double scrollableWidth;

    HashMap colorMap = Stats.colorMap;

    ArrayList<Activity> archive = ArchiveDBModel.archive;

    public static double currentLayout = 2000;


    public void initialize() {


        archive.sort(Activity.activityComparator);
        for (Activity activity : archive){
            System.out.println(activity);
        }

        //add navigation tab
        InfographicsNavigationTab navTab = new InfographicsNavigationTab();
        grandmotherPane.getChildren().add(navTab);
        navTab.controller.motherPane.setTranslateY(400);
        navTab.setSelectedInfographic("timeLine");

        navTab.setLayoutX(1352);
        navTab.setLayoutY(106);


        //set prototype property 'gotoCreatorButton'
        setGoToCreatorButton(goToCreatorButton);

        detailPane.setOpacity(0);
        startTimeSecs = 0;
        endTimeSecs = 0;


        dateDisplay.setText(firstDate);


        addActivities(archive);


        computeAndSetScrollableWidth();


        resizePanes(scrollableWidth);


        makeTimeLine(startTimeSecs, endTimeSecs);
        makeNodes(getNumNodes());


        configureDateChangePoint(startTimeSecs - startTimeSecs % FIVE_MINS_IN_SECONDS);

        if (currentLayout > 100) {
            currentLayout = -(scrollableWidth - screenWidth);
            Stats.currentTimelineLayoutInitialized = true;
        }
        setPosition(currentLayout);



    }


    public void computeAndSetScrollableWidth() {
        scrollableWidth = (endTimeSecs - startTimeSecs) * WIDTH_OF_1_SEC + 600;
        if (scrollableWidth < 1280) {
            scrollableWidth = 1280;
        }
    }


    private void makeTimeLine(int startTimeSecs, int endTimeSecs) {
        int beginTime = startTimeSecs - startTimeSecs % FIVE_MINS_IN_SECONDS;
        allContainerPane.setMargin(colorPane, new Insets(0, 0, 0, 200 + (startTimeSecs - beginTime) * WIDTH_OF_1_SEC));
        int finishTime = FIVE_MINS_IN_SECONDS * (endTimeSecs / FIVE_MINS_IN_SECONDS) + FIVE_MINS_IN_SECONDS;

        if (!archive.isEmpty()) {
            for (int currentTime = beginTime; currentTime <= finishTime; currentTime += FIVE_MINS_IN_SECONDS) {
                Label timeLabel = new Label(getAMPMTimeFromSeconds(currentTime));
                timeLabel.setPrefWidth(WIDTH_OF_5_MINS);
                timeLabel.setPrefHeight(timeLineHeight);
                timeLabel.setStyle("-fx-alignment: center");
                timePane.getChildren().add(timeLabel);

            }
        }

    }


    public void addActivities(ArrayList<Activity> archive) {


        if (archive.size() == 1) {
            Activity firstAct = archive.get(0);
            startTimeSecs = firstAct.getStartTimeSecs();
            endTimeSecs = firstAct.getEndTimeSecs();
            firstDate = firstAct.getDate();
            lastDate = decrementDate(firstDate);
        } else if (!archive.isEmpty()) {
            startTimeSecs = archive.get(0).getStartTimeSecs();
            endTimeSecs = archive.get(archive.size() - 1).getEndTimeSecs();
            firstDate = archive.get(0).getDate();
            lastDate = archive.get(archive.size() - 1).getDate();
        }

        if (endTimeSecs - startTimeSecs < FIVE_MINS_IN_SECONDS) {
            endTimeSecs += FIVE_MINS_IN_SECONDS;
        }

        for (Activity activity : archive) {

            TimeLineActivityPeriod period = new TimeLineActivityPeriod(activity, noteTextArea, false);

            Label newActivity = period.getActivityLabel();


            newActivity.setStyle("-fx-background-color:" + Stats.colorMap.get(activity.getCategory()) +
                    "; -fx-alignment: center; " +
                    "-fx-border-color:  rgb(246, 255, 226);" +
                    " fx-border-width: 1;");

            newActivity.setPrefHeight(colorLineHeight);
            newActivity.setMaxHeight(colorLineHeight);


            newActivity.setPrefWidth(activity.getDurationSeconds() * WIDTH_OF_1_SEC);
            newActivity.setText(activity.getName());
            addHoverHandler(newActivity, activity.getStartTimeSecs(), activity.getEndTimeSecs(), activity.getDurationSeconds(), activity.getCategory(), activity.getName());


            colorPane.getChildren().add(period);

            changeFontUntilTextFitsLabel(newActivity);

            if (activity.getEndTimeSecs() > endTimeSecs) {
                endTimeSecs += SECONDS_IN_A_DAY;
            }
        }


    }


    public static String decrementDate(String date) {
        return date.substring(0, date.length() - 2) + (Integer.parseInt(date.substring(date.length() - 2)) - 1);
    }


    private void addHoverHandler(Label activity, int activityStartTimeSecs, int activityEndTimeSecs, int activityDuration, String activityCategory, String activityName) {
        activity.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                categoryTitle.setText(activityCategory);


                String color = "white";
                int colorIndex = Stats.categorySequence.indexOf(activityCategory);
                try {
                    color = Stats.textColorSequence.get(colorIndex);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                categoryTitle.setStyle("-fx-alignment: center; -fx-text-fill:" + color + ";");

                durationLabel.setText(DateTimeModel.parseDurationToString(activityDuration, false));

                specificLabel.setText(activityName);

                beganAt.setText(getAMPMTimeFromSeconds(activityStartTimeSecs));

                finishedAt.setText(getAMPMTimeFromSeconds(activityEndTimeSecs));
                detailPane.setOpacity(1);
            }
        });
        activity.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                detailPane.setOpacity(0);
            }
        });
    }


    private void changeFontUntilTextFitsLabel(Label label) {

        String text = label.getText();
        testText.setText(text);

        double textWidth = testText.getLayoutBounds().getWidth();
        double labelWidth = label.getPrefWidth();

        double resizeFactor = labelWidth / textWidth * 0.9;
        Font font = new Font(label.getFont().getSize() * resizeFactor);

        if (font.getSize() > 100) {
            font = new Font(100);
        }
        label.setFont(font);


    }


    private void configureDateChangePoint(int beginTime) {
        dateChangePoint = (double) (SECONDS_IN_A_DAY - beginTime) * WIDTH_OF_1_SEC + 200;

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


    private void makeNodes(int nodeNum) {
        if (!archive.isEmpty()) {
            for (int i = 0; i <= nodeNum; i++) {
                Line nodeMarker = new Line(0, nodeStartPointY, 0, nodeEndPointY);
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
        App.sceneNavigationModel.gotoScene(SceneNavigationModel.scheduleCreator, motherPane.getScene());
        Stats.currentTimelineLayoutInitialized = false;
    }

    public void goToMain() {
        Stats.currentTimelineLayoutInitialized = false;

        App.sceneNavigationModel.gotoScene(SceneNavigationModel.stats, motherPane.getScene());
    }

    public void goToEditor() throws IOException {
        Table.updateData(ArchiveDBModel.archive);
        App.sceneNavigationModel.loadNewScene("FXML/Timeline/editor.fxml", motherPane.getScene());
    }


    public void onScroll(ScrollEvent e) {
        double motherPaneLayoutX = motherPane.getLayoutX();

        double change;
        double changeX = e.getDeltaX();
        double changeY = e.getDeltaY() * 15;

        if (changeX == 0){
            change = changeY;
        } else {
            change = changeX;
        }

        if (motherPaneLayoutX + change < 10 && motherPaneLayoutX + change > -(scrollableWidth - TimeLine.screenWidth)) {

            shiftBackground(change);

            double newLayoutX = motherPaneLayoutX + change;
            motherPane.setLayoutX(newLayoutX);
            portalPane.setLayoutX(newLayoutX);
            shiftPortals(-newLayoutX);
            shiftDateDisplay(-change);
            noteTextArea.setLayoutX(noteTextArea.getLayoutX() + change);
            checkAndUpdateDate(dateDisplay.getLayoutX());
            shiftDetails(change);

            TimeLine.currentLayout = newLayoutX;

        }

    }

    private void setPosition(double layoutX) {

        double change = layoutX - motherPane.getLayoutX();

        motherPane.setLayoutX(layoutX);
        portalPane.setLayoutX(layoutX);
        shiftPortals(-layoutX);
        shiftDateDisplay(-change);
        checkAndUpdateDate(dateDisplay.getLayoutX());
        shiftDetails(change);
        shiftBackground(change);

    }

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

    @FXML
    public void hideNoteAreaOnScreenClicked(MouseEvent event){

        if (!(event.getTarget() instanceof ImageView)) { //check that event didn't come from noteTag: show and hide? what a waste!
            noteTextArea.setVisible(false);
        }
    }


    public static String getAMPMTimeFromSeconds(int seconds) {

        int template;

        if (seconds > SECONDS_IN_A_DAY) {
            template = seconds - SECONDS_IN_A_DAY;
        } else {
            template = seconds;
        }


        int hh = ((Integer) (template / 3600));

        template = template % 3600;

        int mm = ((Integer) (template / 60));

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


    private void demo() {
        Label newActivity = new Label("activity 1");
        newActivity.setStyle("-fx-background-color: maroon;-fx-alignment: center");
        newActivity.setPrefHeight(colorLineHeight);
        newActivity.setPrefWidth(450 * WIDTH_OF_1_SEC);
        colorPane.getChildren().add(newActivity);
        addHoverHandler(newActivity, 83000, 83450, 450, "Reading", "activity1");

        newActivity = new Label("activity 2");
        newActivity.setStyle("-fx-background-color: deepskyblue;-fx-alignment: center");
        newActivity.setPrefHeight(colorLineHeight);
        newActivity.setPrefWidth(250 * WIDTH_OF_1_SEC);
        colorPane.getChildren().add(newActivity);
        addHoverHandler(newActivity, 83450, 83700, 250, "Writing", "activity2");


        newActivity = new Label("activity 3");
        newActivity.setStyle("-fx-background-color: violet;-fx-alignment: center");
        newActivity.setPrefHeight(colorLineHeight);
        newActivity.setPrefWidth(500 * WIDTH_OF_1_SEC);
        colorPane.getChildren().add(newActivity);
        addHoverHandler(newActivity, 83700, 84200, 500, "Miscellaneous", "activity3");


        newActivity = new Label("activity 4");
        newActivity.setStyle("-fx-background-color: chartreuse;-fx-alignment: center");
        newActivity.setPrefHeight(colorLineHeight);
        newActivity.setPrefWidth(900 * WIDTH_OF_1_SEC);
        colorPane.getChildren().add(newActivity);
        addHoverHandler(newActivity, 84200, 85100, 900, "Exercise", "activity4");


        newActivity = new Label("activity 5");
        newActivity.setStyle("-fx-background-color: gold;-fx-alignment: center");
        newActivity.setPrefHeight(colorLineHeight);
        newActivity.setPrefWidth(1200 * WIDTH_OF_1_SEC);
        colorPane.getChildren().add(newActivity);
        addHoverHandler(newActivity, 85100, 86300, 1200, "Spirituality", "activity5");


        newActivity = new Label("activity 6");
        newActivity.setStyle("-fx-background-color: fuchsia;-fx-alignment: center");
        newActivity.setPrefHeight(colorLineHeight);
        newActivity.setPrefWidth(600 * WIDTH_OF_1_SEC);
        colorPane.getChildren().add(newActivity);
        addHoverHandler(newActivity, 86300, 86900, 600, "Entertainment", "activity6");


        newActivity = new Label("activity 7");
        newActivity.setStyle("-fx-background-color: bisque;-fx-alignment: center");
        newActivity.setPrefHeight(colorLineHeight);
        newActivity.setPrefWidth(500 * WIDTH_OF_1_SEC);
        colorPane.getChildren().add(newActivity);
        addHoverHandler(newActivity, 86900, 87400, 500, "Rest", "activity7");


        newActivity = new Label("activity 8");
        newActivity.setStyle("-fx-background-color: crimson;-fx-alignment: center");
        newActivity.setPrefHeight(colorLineHeight);
        newActivity.setPrefWidth(1000 * WIDTH_OF_1_SEC);
        colorPane.getChildren().add(newActivity);
        addHoverHandler(newActivity, 87400, 888400, 1000, "Arts", "activity1");


    }

}


//

