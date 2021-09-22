package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.Models.BackupArchiveModel;
import main.Models.DBModels.*;
import main.Models.SceneNavigationModel;
import main.Models.SearchModel;
import main.Utility.EditLog;

import java.sql.SQLException;


public class App extends Application {
    public static EditLog editLog = new EditLog();
    public static SceneNavigationModel sceneNavigationModel = new SceneNavigationModel();
    public static Thread backupRegularly;


    @Override
    public void start(Stage primaryStage){
        // initializing DB connections
        ArchiveDBModel.connect();
        SettingsDBModel.connect();

        // starting the backup thread
        backupRegularly = new Thread(BackupArchiveModel.createBackupArchiveDBRegularly);
        backupRegularly.start();
        BackupArchiveModel.updateBackupsObservableList();

        //initializing launchscreen scene
        SceneNavigationModel.launchScreen = sceneNavigationModel.createNewScene("../resources/FXML/LaunchScreen/launchScreen.fxml");

        primaryStage.setTitle("Schedule Archive");
        primaryStage.setScene(SceneNavigationModel.launchScreen);

        primaryStage.show();
    }


    public static void setScheduleCreator(Scene newSc){
        SceneNavigationModel.scheduleCreator = newSc;
    }


    public void stop() throws SQLException {
        WriteToDBModel.saveDataSynchronously();
        new Thread(WriteToDBModel.exitSystemSynchronously).start();
    }


    public static void main(String[] args) {
        Application.launch(args);
    }
}
