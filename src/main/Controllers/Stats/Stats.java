package main.Controllers.Stats;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import main.App;
import main.Controllers.Loader.Loader;
import main.Controllers.PrototypeController;
import main.Models.DBModels.ArchiveDBModel;
import main.Models.DBModels.WriteToDBModel;
import main.Models.DateTimeModel;
import main.resources.SceneNavigationModel;
import main.resources.customNodes.activitySummaryTableView.ActivitySummaryTableView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class Stats extends PrototypeController {

    @FXML
    Button gotoCreatorButton;


    public static final ArrayList<String> categorySequence = new ArrayList<>();
    public static final ArrayList<String> colorSequence = new ArrayList<>();
    public static final ArrayList<String> textColorSequence = new ArrayList<>();
    public static final ArrayList<String> borderColorSequence = new ArrayList<>();
    public static final HashMap<String, String> colorMap = new HashMap<>();


    public static boolean currentTimelineLayoutInitialized = false;


    public static Comparator<ActivitySummaryTableView.chartDataUnit> chartDataUnitComparator = new Comparator<ActivitySummaryTableView.chartDataUnit>() {
        @Override
        public int compare(ActivitySummaryTableView.chartDataUnit u1, ActivitySummaryTableView.chartDataUnit u2) {
            return ((Integer)u2.getDurationSecs()).compareTo((Integer) u1.getDurationSecs());
        }
    };



    public void initialize(){

        //set prototype property 'gotoCreatorButton'
        setGoToCreatorButton(gotoCreatorButton);

        makeCategorySequence();
        makeColorSequence();
        makeTextColorSequence();
        makeBorderColorSequence();
        makeColorMap();
    }

    private void makeCategorySequence(){
        categorySequence.clear();
        categorySequence.add("Study");
        categorySequence.add("Entertainment");
        categorySequence.add("Spirituality");
        categorySequence.add("Exercise");
        categorySequence.add("Rest");
        categorySequence.add("Reading");
        categorySequence.add("Writing");
        categorySequence.add("Arts");
        categorySequence.add("Social");
        categorySequence.add("Media");
        categorySequence.add("Service");
        categorySequence.add("Miscellaneous");
        categorySequence.add("NoData");
        categorySequence.add("New");
    }

    private void makeColorSequence(){
        colorSequence.clear();
        colorSequence.add("rgb(201, 252, 252)");
        colorSequence.add("rgb(255, 180, 249)");
        colorSequence.add("rgb(248, 234, 163)");
        colorSequence.add("rgb(227, 255, 200)");
        colorSequence.add("rgb(252, 235, 213)");
        colorSequence.add("rgb(226, 183, 150)");
        colorSequence.add("rgb(171, 220, 236)");
        colorSequence.add("rgb(236, 108, 133)");
        colorSequence.add("rgb(184, 184, 117)");
        colorSequence.add("rgb(117, 185, 117)");
        colorSequence.add("rgb(250, 188, 164)");
        colorSequence.add("rgb(206, 173, 236)");
        colorSequence.add("rgb(185, 185, 185)");
        colorSequence.add("white");

    }

    private void makeTextColorSequence(){
        textColorSequence.clear();
        textColorSequence.add("aqua");
        textColorSequence.add("fuchsia");
        textColorSequence.add("gold");
        textColorSequence.add("chartreuse");
        textColorSequence.add("bisque");
        textColorSequence.add("chocolate");
        textColorSequence.add("deepskyblue");
        textColorSequence.add("crimson");
        textColorSequence.add("olive");
        textColorSequence.add("green");
        textColorSequence.add("coral");
        textColorSequence.add("blueviolet");
        textColorSequence.add("grey");
        textColorSequence.add("rgb(242, 252, 231)");

    }

    private void makeBorderColorSequence(){
        borderColorSequence.clear();
        borderColorSequence.add("rgb(136, 185, 185)");
        borderColorSequence.add("rgb(197, 136, 192)");
        borderColorSequence.add("rgb(199, 181, 94)");
        borderColorSequence.add("rgb(166, 201, 134)");
        borderColorSequence.add("rgb(207, 175, 132)");
        borderColorSequence.add("rgb(175, 132, 100)");
        borderColorSequence.add("rgb(104, 173, 196)");
        borderColorSequence.add("rgb(185, 105, 121)");
        borderColorSequence.add("rgb(146, 146, 73)");
        borderColorSequence.add("rgb(77, 145, 77)");
        borderColorSequence.add("rgb(185, 121, 98)");
        borderColorSequence.add("rgb(131, 87, 172)");
        borderColorSequence.add("grey");
        borderColorSequence.add("white");

    }

    public void makeColorMap(){
        colorMap.clear();

        for (int i = 0; i < colorSequence.size(); i++){
            colorMap.put(categorySequence.get(i), colorSequence.get(i));
        }
    }



    @FXML
    public void goToCreator(){
        App.sceneNavigationModel.gotoScene(SceneNavigationModel.scheduleCreator, SceneNavigationModel.stats);
    }


    @FXML
    public void goToBarChart(){
        BarDisplay.updateBarChart();
        App.sceneNavigationModel.gotoScene(SceneNavigationModel.bars, SceneNavigationModel.stats);
    }


    @FXML
    public void goToTable(){
        App.sceneNavigationModel.gotoScene(SceneNavigationModel.table, SceneNavigationModel.stats);
    }


    @FXML
    public void goToPiChart(){
        if (!ArchiveDBModel.archive.isEmpty()){
            PiChart.updatePi();
        }

        App.sceneNavigationModel.gotoScene(SceneNavigationModel.piChart, SceneNavigationModel.stats);
    }

    @FXML
    public void goToLoader() throws IOException {
        App.sceneNavigationModel.loadNewScene("FXML/Loader/loader.fxml", SceneNavigationModel.stats);


        if (Loader.resumeMode || Loader.loadMode) {

            //set selectedDay temporaryly to changing day
            String temp = DateTimeModel.selectedDay;
            DateTimeModel.selectedDay = DateTimeModel.selectedDay.replaceAll("-", "_");;

            WriteToDBModel.saveDataSynchronously();

            //reset selectedDay
            DateTimeModel.selectedDay = temp;

        }
    }

    @FXML
    public void goToMacro(){
        App.sceneNavigationModel.gotoScene(SceneNavigationModel.macro, SceneNavigationModel.stats);
    }

    @FXML
    public void goToBackups(){
        App.sceneNavigationModel.gotoScene(SceneNavigationModel.backups, SceneNavigationModel.stats);
    }


    @FXML
    public void goToTimeLine(){
        App.sceneNavigationModel.loadNewScene("FXML/Timeline/timeLine.fxml", SceneNavigationModel.stats);
    }


    static double calculatePercentage(int total, int value){
        return (double) value / (double)total * 100;
    }


}
