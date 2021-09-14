package main.Controllers.Loader;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import main.Controllers.Creator.ScheduleCreator;
import main.Controllers.PrototypeController;
import main.Controllers.Stats.Table;
import main.Controllers.Timeline.TimeLine;
import main.Models.ArchiveDBModel;
import main.Utility.Activity;
import main.App;


import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Loader extends PrototypeController {




    @FXML
    ListView backups;
    ObservableList<String> backupData = FXCollections.observableArrayList();



    Connection conn;
    Statement statement;


    @FXML
    Button loadButton;
    @FXML
    Button resumeButton;



    public static boolean resumeMode = false;

    public static boolean loadMode = false;

    public static boolean disAbleCreator = false;

    public static int currentTimeInSeconds = ScheduleCreator.getTimeInSeconds(
            LocalTime.now().getHour(),
            LocalTime.now().getMinute(),
            LocalTime.now().getSecond()
    );


    public void initialize(){
        setupBackups();
    }


    public void loadData() {
        ArchiveDBModel.selectedDay = (String)backups.getSelectionModel().getSelectedItem();
        // reading contents of selectedDay on the database
        ArrayList<Activity> loadedDay = ArchiveDBModel.readDay(ArchiveDBModel.selectedDay);
        // overwriting archive to the contents of the selectedDay
        ArchiveDBModel.archive = loadedDay;
    }

    public void setupBackups() {
        backups.setItems(backupData);

        for (String date : ArchiveDBModel.allDates) {
            backupData.add(date);
        }

        if (!backupData.isEmpty()){
            ArchiveDBModel.lastDay = backupData.get(backupData.size() - 1);

            backups.scrollTo(backupData.size() - 1);
        }

    }



    @FXML
    public void resumeCreation() throws IOException {
        loadData();

        // getting data for NoData
        int gapStartTime = ArchiveDBModel.archive.get(ArchiveDBModel.archive.size()-1).getEndTimeSecs();
        int gapEndTime = currentTimeInSeconds;

        if (gapEndTime < gapStartTime){
            gapEndTime += TimeLine.SECONDS_IN_A_DAY;
        }

        // adding NoData to archive
        ArchiveDBModel.archive.add(new Activity("no data", "NoData", gapEndTime - gapStartTime, gapStartTime, gapEndTime, LocalDate.now().toString()));

        resumeMode = true;
        loadMode = false;

        // exiting
        ArchiveDBModel.currentDay = ArchiveDBModel.archive.get(0).getDate();
        Table.updateData(ArchiveDBModel.archive);
        wrapUpAndGoToCreator();
    }


    public void loadDaysOfOld() throws IOException {
        loadData();
        Table.updateData(ArchiveDBModel.archive);
        disableAndGotoStats();
    }

    @FXML
    public void goToStats(){
        App.sceneNavigationModel.gotoScene(App.stats, backups.getScene());
    }


    private void disableAndGotoStats() throws IOException {

        //disable creator button
        disAbleCreator = true;
        loadMode = true;
        App.editLog.wipe();

        App.stats = App.sceneNavigationModel.createNewScene("../resources/FXML/Stats/stats.fxml");
        App.sceneNavigationModel.gotoScene(App.stats, backups.getScene());

    }


    private void wrapUpAndGoToCreator() {
        App.scheduleCreator = App.sceneNavigationModel.loadNewScene("../resources/FXML/Creator/scheduleCreator.fxml", backups.getScene());
        //reenable creator
        disAbleCreator = false;

        App.editLog.wipe();
        ArchiveDBModel.selectedDay = (String)backups.getSelectionModel().getSelectedItem();


    }



    private boolean canResume(){
        // getting the selectedDay
        String currentSelectedDate = (String)backups.getSelectionModel().getSelectedItem();
        String currentDay = ArchiveDBModel.currentDay;

        // checking if the selected date is equal to the currentDate or day before the currentDate
        if (ArchiveDBModel.addDayToDate(currentSelectedDate, 1).equals(currentDay) || currentSelectedDate.equals(ArchiveDBModel.currentDay)){
            // getting the dayStartTime of the selectedDay
            ArrayList<Activity> day = ArchiveDBModel.readDay(currentSelectedDate);
            int dayStartTime = day.get(0).getStartTimeSecs();
            int currentTimeInSec = currentTimeInSeconds;

            // adding 86400 to currentTimeInSec if the date has changed
            if (currentDay.equals(ArchiveDBModel.addDayToDate(currentSelectedDate, 1))) {
                currentTimeInSec += 86400;
            }

            System.out.println("(from canResume) currentTime in seconds: " + currentTimeInSec);
            System.out.println("(from canResume) dayStartTime of the selectedDay: " + dayStartTime);

            // returning True if the time pass since the dayStartTime of the selected day is less than 86400
            if (currentTimeInSec - dayStartTime < 86400) {
                return true;
            }
        }
        return false;
    }


    public void assessSelectedDay() throws ParseException {

        String selectedDayTemp = (String)backups.getSelectionModel().getSelectedItem();

        if (!(selectedDayTemp == null)){


            loadButton.setDisable(false);

            if (selectedDayTemp.equals(ArchiveDBModel.lastDay) && canResume() && (selectedDayTemp.equals(ArchiveDBModel.currentDay) || selectedDayTemp.equals(ArchiveDBModel.getPreviousDate(ArchiveDBModel.currentDay)))){
                resumeButton.setDisable(false);
            }
            else {
                resumeButton.setDisable(true);
            }

        }
        else {
            loadButton.setDisable(true);
        }


    }


}
