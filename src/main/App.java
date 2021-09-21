package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.Models.BackupArchiveModel;
import main.Models.DBModels.*;
import main.Models.SceneNavigationModel;
import main.Utility.EditLog;


public class App extends Application {
    public static EditLog editLog = new EditLog();

    public static SceneNavigationModel sceneNavigationModel = new SceneNavigationModel();


    @Override
    public void start(Stage primaryStage){
        System.out.println("1");


        //initializing launchscreen scene
        SceneNavigationModel.launchScreen = sceneNavigationModel.createNewScene("../resources/FXML/LaunchScreen/launchScreen.fxml");

        primaryStage.setTitle("Schedule Archive");
        primaryStage.setScene(SceneNavigationModel.launchScreen);

        primaryStage.show();
    }


    public static void setScheduleCreator(Scene newSc){
        SceneNavigationModel.scheduleCreator = newSc;
    }


    public void stop(){
        WriteToDBModel.saveDataSynchronously();
        new Thread(WriteToDBModel.exitSystemSynchronously).start();

    }


    public static void main(String[] args) {
        new Thread(BackupArchiveModel.createBackupArchiveDBRegularly).start();
        DBModel.connect(DBModel.dbName);
        BackupArchiveModel.availableBackupsObservableList = BackupArchiveModel.listBackups();
        Application.launch(args);
    }
}
