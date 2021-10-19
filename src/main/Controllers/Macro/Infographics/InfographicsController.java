package main.Controllers.Macro.Infographics;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import main.App;
import main.Controllers.Macro.Infographics.Barcomponent.Barcomponent;
import main.Controllers.PrototypeController;
import main.Models.*;
import main.Models.DBModels.ReadFromDBModel;
import main.Models.Graphics.InfographicsModel;
import main.Models.SceneNavigationModel;
import main.resources.customNodes.activitySummaryTableView.ActivitySummaryTableView;

import java.io.File;


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

        loadAllPrototypeBarcomps();

        //set prototype property 'gotoCreatorButton'
        setGoToCreatorButton(goToCreatorButton);

        //save original x of labels
        monthLabelOriginalX = monthLabel.getLayoutX();
        yearLabelOriginalX = yearLabel.getLayoutX();


        //store pane dimentions in model
        InfographicsModel.infographicsPaneHeight = infographicPane.getPrefHeight();
        System.out.println("infographicsPaneHeight: "+infographicPane.getPrefHeight());
        InfographicsModel.monthBarHeight = monthBar.getPrefHeight();
        InfographicsModel.yearPaneHeight = yearDisplayPane.getPrefHeight();
        System.out.println("yearPaneHeight: "+yearDisplayPane.getPrefHeight());

        createInfographic();


        //set month label to first month
        updateDateLabels();

        //align labels
        new Thread(totallyUnnecessaryBackgroundLabelAlignProcessBecauseOfAStupidCannotGetBoundsInInitializeMethodError).start();


        //set dayinfocontainerpane
        InfographicsModel.dayInfoPopupListViewContainerPane = grandmotherPane;



        //configure day info popup

        InfographicsModel.dayInfoPopupListView.setEditable(false);
        InfographicsModel.dayInfoPopupListView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn nameCol = new TableColumn("Activity Name");
        TableColumn durationCol = new TableColumn("Net Duration");
        InfographicsModel.dayInfoPopupListView.getColumns().clear();
        InfographicsModel.dayInfoPopupListView.getColumns().addAll(nameCol, durationCol);
        System.out.println(InfographicsModel.dayInfoPopupListView.getColumns());

        nameCol.setCellValueFactory(
                new PropertyValueFactory<ActivitySummaryTableView.chartDataUnit,String>("header"));
        durationCol.setCellValueFactory(
                new PropertyValueFactory<ActivitySummaryTableView.chartDataUnit,String>("durationParsed"));

        InfographicsModel.dayInfoPopupListView.setItems(InfographicsModel.dayInfoPopupData);



        File f = new File("src/main/resources/customNodes/activitySummaryTableView/activity-summary-table-view.css");
        InfographicsModel.dayInfoPopupListView.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));


        InfographicsModel.dayInfoPopupListView.setPrefWidth(380);
        InfographicsModel.dayInfoPopupListView.setPrefHeight(500);
    }



    private void loadAllPrototypeBarcomps(){
        for (String date : ReadFromDBModel.getAllDates()){
            new Thread(loadDayBarAndAddToManager).start();
        }
    }


    Runnable loadDayBarAndAddToManager = () -> {
        Barcomponent comp = new Barcomponent("2021-01-01", 0);
        System.out.println(comp.controller.freqBar);
        Platform.runLater(() -> {
            BarComponentManager.addBarComp(comp);
        });
    };




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

            //move info popup
            InfographicsModel.dayInfoPopupListView.setLayoutX(InfographicsModel.dayInfoPopupListView.getLayoutX() + change);
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

        //deploy lighter month label color for darker months
        if (monthToDisplay.equals("November") || monthToDisplay.equals("August")){
            monthLabel.setStyle("-fx-text-fill: rgb(243, 213, 193)");
        } else {
            monthLabel.setStyle("-fx-text-fill: #5f5543");
        }
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
            configureADayBar(date);


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
        year.setPrefHeight(800);
        InfographicsModel.addStyleClassToYear(year, date);
        motherPane.getChildren().add(year);

        //new yearDisplayPane
        yearDisplayPane = new Pane();
        yearDisplayPane.setPrefHeight(InfographicsModel.yearPaneHeight);
        yearDisplayPane.setMinHeight(InfographicsModel.yearPaneHeight);
        yearDisplayPane.setMaxHeight(InfographicsModel.yearPaneHeight);
        year.getChildren().add(yearDisplayPane);
//        yearDisplayPane.setStyle("-fx-border-color: black");

        //new infographics pane
        infographicPane = new HBox();
        infographicPane.setPrefHeight(InfographicsModel.infographicsPaneHeight);
        infographicPane.setMinHeight(InfographicsModel.infographicsPaneHeight);
        infographicPane.setMaxHeight(InfographicsModel.infographicsPaneHeight);
        VBox.setMargin(infographicPane, new Insets(0, 0, 10, 0));
        year.getChildren().add(infographicPane);
//        infographicPane.setStyle("-fx-border-color: black");


        //new monthBar
        monthBar = new HBox();
        monthBar.setPrefHeight(InfographicsModel.monthBarHeight);
        monthBar.setMinHeight(InfographicsModel.monthBarHeight);
        monthBar.setMaxHeight(InfographicsModel.monthBarHeight);
        year.getChildren().add(monthBar);
//        monthBar.setStyle("-fx-border-color: black");


    }

    private void addMonth(String numericalDay, double width) {
        Pane monthPane = new Pane();
        System.out.println(monthPane);
        monthPane.setPrefWidth(width);
        String backgroundColor = DateTimeModel.getMonthProperty("color", "number", numericalDay);
        String borderColor = DateTimeModel.getMonthProperty("border", "number", numericalDay);
        monthPane.setStyle("-fx-background-color: " + backgroundColor + "; -fx-border-width: " + "6px;" + "-fx-border-color: " + borderColor);
        monthBar.getChildren().add(monthPane);


    }

    private void configureADayBar(String date){
        Barcomponent barcomponent = BarComponentManager.updateBar(date, 0);
        barcomponent.setDate(date);
        infographicPane.getChildren().add(barcomponent);
    }



    @FXML
    public void goBack(){
        System.out.println("goback called");
        hideInfoPopup();
        App.sceneNavigationModel.gotoScene(SceneNavigationModel.searchScreen, motherPane.getScene());
        //set default macro screen to search
        SceneNavigationModel.macro = SceneNavigationModel.searchScreen;
    }
    @FXML
    public void goToStats(){
        hideInfoPopup();
        App.sceneNavigationModel.gotoScene(SceneNavigationModel.stats, motherPane.getScene());
    }
    @FXML
    public void goToCreator(){
        hideInfoPopup();
        App.sceneNavigationModel.gotoScene(SceneNavigationModel.scheduleCreator, motherPane.getScene());
    }


    @FXML
    public void hideInfoPopupOnMouseClicked(MouseEvent event){
        System.out.println(event.getTarget());
        if (!(event.getTarget() instanceof Rectangle)){
            hideInfoPopup();

            //unselect selected bar
            InfographicsModel.selectedBarcomponent.setBarColor(Barcomponent.unselectedColor);
        }
    }

    private void hideInfoPopup(){
        InfographicsModel.dayInfoPopupListViewContainerPane.getChildren().remove(InfographicsModel.dayInfoPopupListView);
    }

}
