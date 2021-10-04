package main.Controllers.LaunchScreen;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import main.App;
import main.Controllers.PrototypeController;
import main.Models.DBModels.ReadFromDBModel;
import main.Models.SceneNavigationModel;

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
            // update suggestionList
            ReadFromDBModel.updateSuggestionList();

            // loading new scenes

            SceneNavigationModel.scheduleCreator = App.sceneNavigationModel.createNewScene("../resources/FXML/Creator/scheduleCreator.fxml");
            SceneNavigationModel.stats = App.sceneNavigationModel.createNewScene("../resources/FXML/Stats/stats.fxml");
            SceneNavigationModel.bars = App.sceneNavigationModel.createNewScene("../resources/FXML/Stats/barDisplay.fxml");
            SceneNavigationModel.table = App.sceneNavigationModel.createNewScene("../resources/FXML/Stats/table.fxml");
            SceneNavigationModel.piChart = App.sceneNavigationModel.createNewScene("../resources/FXML/Stats/piChart.fxml");
            SceneNavigationModel.backups = App.sceneNavigationModel.createNewScene("../resources/FXML/Stats/backupScreen.fxml");

//            SceneNavigationModel.macro = App.sceneNavigationModel.createNewScene("../resources/FXML/Macro/searchScreen.fxml");
//            SceneNavigationModel.searchScreen = SceneNavigationModel.macro;
//            SceneNavigationModel.infographics = App.sceneNavigationModel.createNewScene("../resources/FXML/Macro/infographics.fxml");

            Platform.runLater(new Runnable() {
                @Override
                public void run() {

                    loadingAnimationThread.stop();

                    Scene creator = App.sceneNavigationModel.loadNewScene("../resources/FXML/Creator/scheduleCreator.fxml", SceneNavigationModel.launchScreen);
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
//                System.out.println(loadingLabel.getOpacity());
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
