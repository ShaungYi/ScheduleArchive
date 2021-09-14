package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.Models.ArchiveDBModel;
import main.Models.SceneNavigationModel;
import main.Utility.EditLog;

import java.io.IOException;
import java.time.LocalDate;

public class App extends Application {


    public static EditLog editLog = new EditLog();


    //stuff related to scenes
    public static Scene scheduleCreator, stats, bars, table, piChart, macro, launchScreen, backups;
    public static SceneNavigationModel sceneNavigationModel = new SceneNavigationModel();


    @Override
    public void start(Stage primaryStage){
        System.out.println("1");

        //generate past activity archive
        //PastActivityArchiveModel.loadAllPastActivities();

        //initializing scenes

        scheduleCreator = sceneNavigationModel.createNewScene("../resources/FXML/Creator/scheduleCreator.fxml");
        stats = sceneNavigationModel.createNewScene("../resources/FXML/Stats/stats.fxml");
        bars = sceneNavigationModel.createNewScene("../resources/FXML/Stats/barDisplay.fxml");
        table = sceneNavigationModel.createNewScene("../resources/FXML/Stats/table.fxml");
        piChart = sceneNavigationModel.createNewScene("../resources/FXML/Stats/piChart.fxml");
        launchScreen = sceneNavigationModel.createNewScene("../resources/FXML/LaunchScreen/launchScreen.fxml");
        backups = sceneNavigationModel.createNewScene("../resources/FXML/Stats/backupScreen.fxml");
        System.out.println("user data: "+scheduleCreator.getUserData());

        System.out.println("3");
        primaryStage.setTitle("Schedule Archive");
        primaryStage.setScene(launchScreen);

        primaryStage.show();

        System.out.println("4");

    }


    public static void setScheduleCreator(Scene newSc){
        scheduleCreator = newSc;
    }


    public void stop(){

        ArchiveDBModel.backupDataSyncronously();
        new Thread(ArchiveDBModel.exitSystemSyncronously).start();

    }


    public static void main(String[] args) {
        ArchiveDBModel.createBackup("test.db");
        ArchiveDBModel.removeBackup("test.db");
        Application.launch(args);
    }
}
