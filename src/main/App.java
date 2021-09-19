package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.Models.DBModels.*;
import main.Models.SceneNavigationModel;
import main.Utility.EditLog;

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
        primaryStage.setTitle("Schedule Archive");
        primaryStage.setScene(launchScreen);

        primaryStage.show();
    }


    public static void setScheduleCreator(Scene newSc){
        scheduleCreator = newSc;
    }


    public void stop(){
        WriteToDBModel.backupDataSynchronously();
        new Thread(WriteToDBModel.exitSystemSynchronously).start();

    }


    public static void main(String[] args) {
        DBModel.connect(DBModel.dbName);
        BackupArchiveModel.createBackup();
        Application.launch(args);
    }
}
