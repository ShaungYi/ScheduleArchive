package main.Controllers.Macro.Infographics;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import main.App;
import main.Controllers.Macro.Infographics.Barcomponent.Barcomponent;
import main.Controllers.PrototypeController;
import main.Models.*;
import main.Models.DBModels.ReadFromDBModel;
import main.Models.Graphics.InfographicsModel;
import main.Models.Graphics.SceneNavigationModel;


public class InfographicsController extends PrototypeController {

    @FXML
    AnchorPane grandmotherPane;
    @FXML
    Button goToCreatorButton;
    @FXML
    HBox motherPane;
    @FXML
    VBox year;
    @FXML
    Pane yearDisplayPane;
    @FXML
    HBox infographicPane;
    @FXML
    HBox monthBar;
    @FXML
    Label monthLabel;
    Bounds monthLabelBounds;
    double monthLabelOriginalX;
    @FXML
    Label yearLabel;
    Bounds yearLabelBounds;
    double yearLabelOriginalX;
    VBox y2021 = new VBox();

    public void initialize(){

        //set prototype property 'gotoCreatorButton'
        setGoToCreatorButton(goToCreatorButton);

        //save original x of labels
        monthLabelOriginalX = monthLabel.getLayoutX();
        yearLabelOriginalX = yearLabel.getLayoutX();


        //store pane dimentions in model
        InfographicsModel.infographicsPaneHeight = infographicPane.getPrefHeight();
        InfographicsModel.monthBarHeight = monthBar.getPrefHeight();
        InfographicsModel.yearPaneHeight = yearDisplayPane.getPrefHeight();

        createInfographic();


        //set month label to first month
        updateDateLabels();

        //align labels
        new Thread(totallyUnnecessaryBackgroundLabelAlignProcessBecauseOfAStupidCannotGetBoundsInInitializeMethodError).start();

    }

    private Runnable totallyUnnecessaryBackgroundLabelAlignProcessBecauseOfAStupidCannotGetBoundsInInitializeMethodError = () -> {
        System.out.println("(from exit)waiting for previous process to finish");
        //wait until the maddening initialize method finishes and the screen is loaded
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Platform.runLater( () -> {
            yearLabelBounds = yearLabel.localToScreen(yearLabel.getBoundsInLocal());
            alignLabels(yearLabelBounds, monthLabel);
        });


    };


    public void onScroll(ScrollEvent e){


        double motherPaneLayoutX = motherPane.getLayoutX();
        double change = e.getDeltaX();

        //update bounds of labels
        monthLabelBounds = monthLabel.localToScreen(monthLabel.getBoundsInLocal());
        yearLabelBounds = yearLabel.localToScreen(yearLabel.getBoundsInLocal());

        //align the two labels to each other
        alignLabels(yearLabelBounds, monthLabel);

        //update month label text, using year label layoutx as reference
        updateDateLabels();

        //labels have reached max or min but the user is scrolling towards that extreme
        if ((monthLabelBounds.getMaxX() >= SceneNavigationModel.screenWidth && change < 0) || (monthLabelBounds.getMinX() <= 0 && change > 0)){
            //do nothing
        }

        //user is not trying to scroll beyond extreme and labels are in the middle
        else if (Math.abs(monthLabel.getLayoutX() - monthLabelOriginalX) < Math.abs(change) && !((motherPaneLayoutX < -(InfographicsModel.infographicWidth - SceneNavigationModel.screenWidth) && change < 0) || (motherPaneLayoutX > 0) && change > 0)){
            //align labels to center
            double monthLabelX = monthLabel.getLayoutX();
            if (monthLabelX != monthLabelOriginalX){

                yearLabel.setLayoutX(yearLabel.getLayoutX() - (monthLabelX - monthLabelOriginalX));

                monthLabel.setLayoutX(monthLabelOriginalX);
            }


            //move all labels and pane (month label moves automatically)
            double newLayoutX = motherPaneLayoutX + change;
            motherPane.setLayoutX(newLayoutX);
            yearLabel.setLayoutX(yearLabel.getLayoutX() - change);
        }

        //pane has reached extreme
        else {
            //move labels only
            yearLabel.setLayoutX(yearLabel.getLayoutX() - change);
            monthLabel.setLayoutX(monthLabel.getLayoutX() - change);
        }


    }



    private void alignLabels(Bounds reference, Label aligned){
        double midX = (reference.getMinX() + reference.getMaxX()) / 2;
        double newLayoutX = midX - aligned.getWidth()/2;
        aligned.setLayoutX(newLayoutX);
    }

    //updates month label if year label midpoint crosses a boundary
    private void updateDateLabels(){
        //get year label mid point
        double leftLayX = yearLabel.getLayoutX();
        double rightLayX = leftLayX + yearLabel.getWidth();
        double yearMid = (leftLayX + rightLayX) / 2;

        //update date labels
        String monthToDisplay = InfographicsModel.getDisplayedDate(yearMid, "month");
        monthLabel.setText(monthToDisplay);

        String yearToDisplay = InfographicsModel.getDisplayedDate(yearMid, "year");
        yearLabel.setText(yearToDisplay);
    }



    private void createInfographic(){
        double monthWidth = 0;
        double transitionX = 0;
        String prevDate = "how many months to babylon?";

        for (String date : ReadFromDBModel.getAllDates()) {
            transitionX += InfographicsModel.barWidth;

            //change year if year and prevYear don't match
            String currentYear = DateTimeModel.getYearFromDate(date);
            String lastYear = DateTimeModel.getYearFromDate(prevDate);
            if (!prevDate.equals("how many months to babylon?") && !currentYear.equals(lastYear)){
                //finish up last year's monthBar
                System.out.println("adding final month of year");
                System.out.println("month: " + DateTimeModel.getMonthFromDate(date));
                String lastMonth = DateTimeModel.getMonthFromDate(prevDate);
                addMonth(lastMonth, monthWidth);

                String lastMonthName = DateTimeModel.getMonthProperty("name", "number", lastMonth);
                InfographicsModel.addNewDate(lastMonthName, transitionX, "month");

                changeYear(date);

                //update yearChangePoints in Model
                InfographicsModel.addNewDate(lastYear, transitionX, "year");

                //prevent the addition of another final month by initializing prevDate
                prevDate = "how many months to babylon?";
                //reset monthWidth
                monthWidth = 0;

            }

            //add barcomponent
            addDayBar(date);


            //add another month bar if month has changed
            String numericalPrevMonth = DateTimeModel.getMonthFromDate(prevDate);
            if (!numericalPrevMonth.equals(DateTimeModel.getMonthFromDate(date)) && !prevDate.equals("how many months to babylon?")){
                addMonth(numericalPrevMonth, monthWidth);
                //update model
                InfographicsModel.addNewDate(DateTimeModel.getMonthProperty("name", "number", DateTimeModel.getMonthFromDate(prevDate)), transitionX, "month");

                //reset monthwidth
                monthWidth = 0;

            }


            //enlarge width of month bar
            monthWidth += InfographicsModel.barWidth;
            //update screen width
            InfographicsModel.infographicWidth += InfographicsModel.barWidth;

            //if this is the last day, setup month and year graphically and modelically
            if (ReadFromDBModel.getAllDates().indexOf(date) == ReadFromDBModel.getAllDates().size() - 1){

                //month
                addMonth(DateTimeModel.getMonthFromDate(date), monthWidth);
                InfographicsModel.addNewDate(DateTimeModel.getMonthProperty("name", "number", DateTimeModel.getMonthFromDate(date)), transitionX + InfographicsModel.barWidth, "month");

                //year
                InfographicsModel.addNewDate(currentYear, transitionX, "year");
            }

            prevDate = date;
        }

    }

    private void changeYear(String date){
        //new year
        year = new VBox();
        InfographicsModel.addStyleClassToYear(year, date);
        motherPane.getChildren().add(year);

        //new yearDisplayPane
        yearDisplayPane = new Pane();
        yearDisplayPane.setPrefHeight(InfographicsModel.yearPaneHeight);
        year.getChildren().add(yearDisplayPane);

        //new infographics pane
        infographicPane = new HBox();
        infographicPane.setPrefHeight(InfographicsModel.infographicsPaneHeight);
        VBox.setMargin(infographicPane, new Insets(0, 0, 10, 0));
        year.getChildren().add(infographicPane);

        //new monthBar
        monthBar = new HBox();
        monthBar.setPrefHeight(InfographicsModel.monthBarHeight);
        year.getChildren().add(monthBar);

    }

    private void addMonth(String numericalDay, double width) {
        Pane monthPane = new Pane();
        System.out.println(monthPane);
        monthPane.setPrefWidth(width);
        monthPane.setStyle("-fx-background-color: " + DateTimeModel.getMonthProperty("color", "number", numericalDay));
        monthBar.getChildren().add(monthPane);


    }

    private void addDayBar(String date){
        Barcomponent barcomponent = new Barcomponent(DateTimeModel.getDayIDFromDate(date), 0);
        infographicPane.getChildren().add(barcomponent);
        BarComponentManager.addBarComp(barcomponent, date);
    }



    @FXML
    public void goBack(){
        App.sceneNavigationModel.gotoScene(SceneNavigationModel.macro, motherPane.getScene());
        //set default macro screen to search
        SceneNavigationModel.macro = SceneNavigationModel.searchScreen;
    }
    @FXML
    public void goToStats(){
        App.sceneNavigationModel.gotoScene(SceneNavigationModel.stats, motherPane.getScene());
    }
    @FXML
    public void goToCreator(){
        App.sceneNavigationModel.gotoScene(SceneNavigationModel.scheduleCreator, motherPane.getScene());
    }

}
