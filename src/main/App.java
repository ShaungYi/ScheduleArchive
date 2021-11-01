package main;

import javafx.application.Application;
import javafx.stage.Stage;
import main.Models.BackupArchiveModel;
import main.Models.DBModels.*;
import main.Models.FileManagementModel;
import main.resources.SceneNavigationModel;
import main.Utility.EditLog;


public class App extends Application {
    public static EditLog editLog = new EditLog();
    public static SceneNavigationModel sceneNavigationModel = new SceneNavigationModel();
    public static Thread backupRegularly;
    public static String cacheDirectory = "/ScheduleArchiveCache";


    @Override
    public void start(Stage primaryStage){

        // creating cache folders
        FileManagementModel.createFolder(cacheDirectory);
        FileManagementModel.createFolder(cacheDirectory + ArchiveDBModel.pathToDBDirectory);
        FileManagementModel.createFolder(BackupArchiveModel.pathToBackupsFolder);

        // connecting to the databases
        ArchiveDBModel.connect();
        SettingsDBModel.connect();

        // starting the backup thread
        BackupArchiveModel.loadSettings();
        backupRegularly = new Thread(BackupArchiveModel.createBackupArchiveDBRegularly);
        backupRegularly.start();
        BackupArchiveModel.updateBackupsObservableList();

        //initializing launchScreen scene
        SceneNavigationModel.launchScreen = sceneNavigationModel.createNewScene("FXML/LaunchScreen/launchScreen.fxml");

        primaryStage.setTitle("Schedule Archive");
        primaryStage.setScene(SceneNavigationModel.launchScreen);

        primaryStage.show();
    }




    public void stop() {
        WriteToDBModel.saveDataSynchronously();
        new Thread(WriteToDBModel.exitSystemSynchronously).start();
    }


    public static void main(String[] args) {
        Application.launch(args);
    }
}
