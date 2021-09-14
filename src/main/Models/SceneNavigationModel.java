package main.Models;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.App;
import main.Controllers.Loader.Loader;
import main.Controllers.PrototypeController;
import main.Controllers.Stats.BackupScreenController;
import main.Controllers.Timeline.Editor;

import java.awt.*;
import java.io.IOException;

public class SceneNavigationModel{

    static Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    public static final double screenWidth = screenSize.getWidth();
    public static final double screenHeight = 800;



    public void gotoScene(Scene scene, Scene currentScene){
        Stage stage = (Stage) currentScene.getWindow();
        executeOnSwitchingScene(scene, currentScene);
        stage.setScene(scene);
    }

    public Scene loadNewScene(String fxmlUrl, Scene currentScene){
        Stage stage = (Stage) currentScene.getWindow();
        Scene newScene = createNewScene(fxmlUrl);
        executeOnSwitchingScene(newScene, currentScene);
        stage.setScene(newScene);
        return newScene;
    }



    private void executeOnSwitchingScene(Scene targetScene, Scene currentScene){


        //get controllers of scenes
        FXMLLoader targetSceneLoader = (FXMLLoader) targetScene.getUserData();
        PrototypeController targetSceneController = targetSceneLoader.getController();

        FXMLLoader currentSceneLoader = (FXMLLoader) currentScene.getUserData();
        PrototypeController currentSceneController = currentSceneLoader.getController();

        // saving data when exiting the editor screen
        if (currentSceneController instanceof Editor) {
            ArchiveDBModel.backupDataSyncronously();
        }



        //disable or enable creator button of the target scene based on whether load mode or resume mode

        if (!(targetSceneController.getGoToCreatorButton() == null))
        if (Loader.disAbleCreator){
            targetSceneController.disableCreatorButton();
        } else {
            targetSceneController.enableCreatorButton();
        }


        //display current backup settings when going to backups screen
        if (targetSceneController instanceof BackupScreenController){
            ((BackupScreenController) targetSceneController).displaySettings();
        }
    }



    public Scene createNewScene(String fxmlUrl){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlUrl));
            Parent root = loader.load();
            Scene scene = new Scene(root, screenWidth, screenHeight);
            scene.setUserData(loader);
            return scene;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static double getScreenWidth() {
        return screenWidth;
    }

    public static double getScreenHeight() {
        return screenHeight;
    }

    public void setScheduleCreator(Scene scheduleCreator) {
        App.scheduleCreator = scheduleCreator;
    }
}
