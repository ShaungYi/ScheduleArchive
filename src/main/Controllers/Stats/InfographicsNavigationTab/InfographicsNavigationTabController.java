package main.Controllers.Stats.InfographicsNavigationTab;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import main.App;
import main.Controllers.Stats.BarDisplay;
import main.Controllers.Stats.PiChart;
import main.Models.DBModels.ArchiveDBModel;
import main.Models.SceneNavigationModel;

import java.util.HashMap;

public class InfographicsNavigationTabController {

    @FXML
    public BorderPane goToBarChartButton;
    @FXML
    public BorderPane goToPiChartButton;
    @FXML
    public BorderPane goToTablesButton;
    @FXML
    public BorderPane goToTimeLineButton;

    public HashMap<String, BorderPane> navButtons = new HashMap<>();

    @FXML
    public VBox motherPane;


    public void initialize(){
        navButtons.put("bar", goToBarChartButton);
        navButtons.put("pi", goToPiChartButton);
        navButtons.put("table", goToTablesButton);
        navButtons.put("timeLine", goToTimeLineButton);

    }

    @FXML
    public void gotoBarChart() {
        BarDisplay.updateBarChart();
        App.sceneNavigationModel.gotoScene(SceneNavigationModel.bars, motherPane.getScene());
    }

    @FXML
    public void gotoPiChart() {
        if (!ArchiveDBModel.archive.isEmpty()){
            PiChart.updatePi();
        }

        App.sceneNavigationModel.gotoScene(SceneNavigationModel.piChart, motherPane.getScene());
    }

    @FXML
    public void gotoTables() {
        App.sceneNavigationModel.gotoScene(SceneNavigationModel.table, motherPane.getScene());
    }

    @FXML
    public void gotoTimeLine() {
        App.sceneNavigationModel.loadNewScene("../resources/FXML/Timeline/timeLine.fxml", motherPane.getScene());

    }
}
