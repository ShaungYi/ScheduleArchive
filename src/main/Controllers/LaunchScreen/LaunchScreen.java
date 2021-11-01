package main.Controllers.LaunchScreen;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import main.App;
import main.Controllers.PrototypeController;
import main.Controllers.Timeline.TimeLineActivityPeriod;
import main.resources.SceneNavigationModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.Objects;

public class LaunchScreen extends PrototypeController {
    @FXML
    Label logoPiece1;
    @FXML
    Label logoPiece2;

    @FXML
    Label loadingLabel;

    @FXML
    Label dot1;
    @FXML
    Label dot2;
    @FXML
    Label dot3;


    public void initialize(){
        new Thread(setupRunnable).start();
        loadingAnimationThread.start();
        Font font = Font.font("Times New Roman", 200);
        logoPiece1.setFont(font);
        logoPiece2.setFont(font);

    }


    Runnable setupRunnable = new Runnable() {
        @Override
        public void run() {

            // loading new scenes

            SceneNavigationModel.stats = App.sceneNavigationModel.createNewScene("FXML/Stats/stats.fxml");
            SceneNavigationModel.bars = App.sceneNavigationModel.createNewScene("FXML/Stats/barDisplay.fxml");
            SceneNavigationModel.table = App.sceneNavigationModel.createNewScene("FXML/Stats/table.fxml");
            SceneNavigationModel.piChart = App.sceneNavigationModel.createNewScene("FXML/Stats/piChart.fxml");
            SceneNavigationModel.backups = App.sceneNavigationModel.createNewScene("FXML/Stats/backupScreen.fxml");

            SceneNavigationModel.macro = App.sceneNavigationModel.createNewScene("FXML/Macro/searchScreen.fxml");
            SceneNavigationModel.searchScreen = SceneNavigationModel.macro;
            SceneNavigationModel.infographics = App.sceneNavigationModel.createNewScene("FXML/Macro/infographics.fxml");

            //load note images

            Image normalImage = null;
            Image hoveredImage = null;

            normalImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/main/resources/Images/note-tag.png")));
            hoveredImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/main/resources/Images/note-tag-hovered.png")));

            TimeLineActivityPeriod.noteImage = normalImage;
            TimeLineActivityPeriod.noteHoveredImage = hoveredImage;

            Platform.runLater(new Runnable() {
                @Override
                public void run() {

                    loadingAnimationThread.stop();

                    Scene creator = App.sceneNavigationModel.loadNewScene("FXML/Creator/scheduleCreator.fxml", SceneNavigationModel.launchScreen);
                    SceneNavigationModel.scheduleCreator = creator;

                }
            });
        }
    };

    Runnable loadingAnimationRunnable = new Runnable() {
        @Override
        public void run() {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            double opacityIncrement = 0.001;
            while (loadingLabel.getOpacity() < 1){

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                double currentIncrement = opacityIncrement;

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        loadingLabel.setOpacity(loadingLabel.getOpacity() + currentIncrement);
                    }
                });
                opacityIncrement = opacityIncrement * 1.5;
            }
            loadingLabel.setOpacity(1);

            while (true){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        dot1.setOpacity(1);
                    }
                });
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        dot2.setOpacity(1);
                    }
                });
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        dot3.setOpacity(1);
                    }
                });
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                dot1.setOpacity(0);
                dot2.setOpacity(0);
                dot3.setOpacity(0);

            }

        }
    };

    Thread loadingAnimationThread = new Thread(loadingAnimationRunnable);

}
