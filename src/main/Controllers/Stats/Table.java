package main.Controllers.Stats;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import main.Controllers.PrototypeController;
import main.Controllers.Stats.InfographicsNavigationTab.InfographicsNavigationTab;
import main.Models.DBModels.ArchiveDBModel;
import main.Models.SceneNavigationModel;
import main.Utility.Activity;
import main.App;
import main.resources.customNodes.activitySummaryTableView.ActivitySummaryTableView;

import java.util.ArrayList;

public class Table extends PrototypeController {

    @FXML
    ScrollPane motherPane;
    @FXML
    Pane grandmotherPane;

    @FXML
    Button goToCreatorButton;


    @FXML
    TableView<ActivitySummaryTableView.chartDataUnit> categoryTable;
    @FXML
    TableColumn<ActivitySummaryTableView.chartDataUnit, String> categoryCol;
    @FXML
    TableColumn<ActivitySummaryTableView.chartDataUnit, String> durationByCategoryCol;
    public static ObservableList<ActivitySummaryTableView.chartDataUnit> categoryData = FXCollections.observableArrayList(new ActivitySummaryTableView.chartDataUnit("",-1));


    @FXML
    TableView<ActivitySummaryTableView.chartDataUnit> studyTable;
    @FXML
    TableColumn<ActivitySummaryTableView.chartDataUnit, String> studyCol;
    @FXML
    TableColumn<ActivitySummaryTableView.chartDataUnit, String> studyDurationCol;
    public static ObservableList<ActivitySummaryTableView.chartDataUnit> studyData = FXCollections.observableArrayList(new ActivitySummaryTableView.chartDataUnit("",-1));


    @FXML
    TableView<ActivitySummaryTableView.chartDataUnit> entertainmentTable;
    @FXML
    TableColumn<ActivitySummaryTableView.chartDataUnit, String> entertainmentCol;
    @FXML
    TableColumn<ActivitySummaryTableView.chartDataUnit, String> entertainmentDurationCol;
    public static ObservableList<ActivitySummaryTableView.chartDataUnit> entertainmentData = FXCollections.observableArrayList(new ActivitySummaryTableView.chartDataUnit("",-1));


    @FXML
    TableView<ActivitySummaryTableView.chartDataUnit> spiritualityTable;
    @FXML
    TableColumn<ActivitySummaryTableView.chartDataUnit, String> spiritualityCol;
    @FXML
    TableColumn<ActivitySummaryTableView.chartDataUnit, String> spiritualityDurationCol;
    public static ObservableList<ActivitySummaryTableView.chartDataUnit> spiritualityData = FXCollections.observableArrayList(new ActivitySummaryTableView.chartDataUnit("",-1));


    @FXML
    TableView<ActivitySummaryTableView.chartDataUnit> exerciseTable;
    @FXML
    TableColumn<ActivitySummaryTableView.chartDataUnit, String> exerciseCol;
    @FXML
    TableColumn<ActivitySummaryTableView.chartDataUnit, String> exerciseDurationCol;
    public static ObservableList<ActivitySummaryTableView.chartDataUnit> exerciseData = FXCollections.observableArrayList(new ActivitySummaryTableView.chartDataUnit("",-1));


    @FXML
    TableView<ActivitySummaryTableView.chartDataUnit> restTable;
    @FXML
    TableColumn<ActivitySummaryTableView.chartDataUnit, String> restCol;
    @FXML
    TableColumn<ActivitySummaryTableView.chartDataUnit, String> restDurationCol;
    public static ObservableList<ActivitySummaryTableView.chartDataUnit> restData = FXCollections.observableArrayList(new ActivitySummaryTableView.chartDataUnit("",-1));


    @FXML
    TableView<ActivitySummaryTableView.chartDataUnit> readingTable;
    @FXML
    TableColumn<ActivitySummaryTableView.chartDataUnit, String> readingCol;
    @FXML
    TableColumn<ActivitySummaryTableView.chartDataUnit, String> readingDurationCol;
    public static ObservableList<ActivitySummaryTableView.chartDataUnit> readingData = FXCollections.observableArrayList(new ActivitySummaryTableView.chartDataUnit("",-1));


    @FXML
    TableView<ActivitySummaryTableView.chartDataUnit> writingTable;
    @FXML
    TableColumn<ActivitySummaryTableView.chartDataUnit, String> writingCol;
    @FXML
    TableColumn<ActivitySummaryTableView.chartDataUnit, String> writingDurationCol;
    public static ObservableList<ActivitySummaryTableView.chartDataUnit> writingData = FXCollections.observableArrayList(new ActivitySummaryTableView.chartDataUnit("",-1));


    @FXML
    TableView<ActivitySummaryTableView.chartDataUnit> artsTable;
    @FXML
    TableColumn<ActivitySummaryTableView.chartDataUnit, String> artsCol;
    @FXML
    TableColumn<ActivitySummaryTableView.chartDataUnit, String> artsDurationCol;
    public static ObservableList<ActivitySummaryTableView.chartDataUnit> artsData = FXCollections.observableArrayList(new ActivitySummaryTableView.chartDataUnit("",-1));


    @FXML
    TableView<ActivitySummaryTableView.chartDataUnit> miscellaneousTable;
    @FXML
    TableColumn<ActivitySummaryTableView.chartDataUnit, String> miscellaneousCol;
    @FXML
    TableColumn<ActivitySummaryTableView.chartDataUnit, String> miscellaneousDurationCol;
    public static ObservableList<ActivitySummaryTableView.chartDataUnit> miscellaneousData = FXCollections.observableArrayList(new ActivitySummaryTableView.chartDataUnit("",-1));
    public static Boolean miscellaneousTableInitialized = false;

    @FXML
    TableView<ActivitySummaryTableView.chartDataUnit> socialTable;
    @FXML
    TableColumn<ActivitySummaryTableView.chartDataUnit, String> socialColumn;
    @FXML
    TableColumn<ActivitySummaryTableView.chartDataUnit, String> socialDurationColumn;
    public static ObservableList<ActivitySummaryTableView.chartDataUnit> socialData = FXCollections.observableArrayList(new ActivitySummaryTableView.chartDataUnit("",-1));
    public static Boolean socialTableInitialized = false;


    @FXML
    TableView<ActivitySummaryTableView.chartDataUnit> mediaTable;
    @FXML
    TableColumn<ActivitySummaryTableView.chartDataUnit, String> mediaColumn;
    @FXML
    TableColumn<ActivitySummaryTableView.chartDataUnit, String> mediaDurationColumn;
    public static ObservableList<ActivitySummaryTableView.chartDataUnit> mediaData = FXCollections.observableArrayList(new ActivitySummaryTableView.chartDataUnit("",-1));
    public static Boolean mediaTableInitialized = false;

    @FXML
    TableView<ActivitySummaryTableView.chartDataUnit> serviceTable;
    @FXML
    TableColumn<ActivitySummaryTableView.chartDataUnit, String> serviceColumn;
    @FXML
    TableColumn<ActivitySummaryTableView.chartDataUnit, String> serviceDurationColumn;
    public static ObservableList<ActivitySummaryTableView.chartDataUnit> serviceData = FXCollections.observableArrayList(new ActivitySummaryTableView.chartDataUnit("",-1));
    public static Boolean serviceTableInitialized = false;




    public static ActivitySummaryTableView.chartDataUnit total = new ActivitySummaryTableView.chartDataUnit("Total",0);




    public void initialize(){

        //add navigation tab
        InfographicsNavigationTab navTab = new InfographicsNavigationTab();
        grandmotherPane.getChildren().add(navTab);
        navTab.controller.motherPane.setTranslateY(400);
        navTab.setSelectedInfographic("table");

        navTab.setLayoutX(1352);
        navTab.setLayoutY(106);

        //set prototype property 'gotoCreatorButton'
        setGoToCreatorButton(goToCreatorButton);

        setupCategoryChart();
        setUpActivityChart(studyTable, studyCol, studyDurationCol, studyData);
        setUpActivityChart(entertainmentTable, entertainmentCol, entertainmentDurationCol, entertainmentData);
        setUpActivityChart(spiritualityTable, spiritualityCol, spiritualityDurationCol, spiritualityData);
        setUpActivityChart(exerciseTable, exerciseCol, exerciseDurationCol, exerciseData);
        setUpActivityChart(restTable, restCol, restDurationCol, restData);
        setUpActivityChart(readingTable, readingCol, readingDurationCol, readingData);
        setUpActivityChart(writingTable, writingCol, writingDurationCol, writingData);
        setUpActivityChart(artsTable, artsCol, artsDurationCol, artsData);
        setUpActivityChart(mediaTable, mediaColumn, mediaDurationColumn, mediaData);
        setUpActivityChart(socialTable, socialColumn, socialDurationColumn, socialData);
        setUpActivityChart(serviceTable, serviceColumn, serviceDurationColumn, serviceData);
        setUpActivityChart(miscellaneousTable, miscellaneousCol, miscellaneousDurationCol, miscellaneousData);

//        System.out.println("finished Initialization");

        //hide scroll bars
        motherPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        motherPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);


    }

    public void setupCategoryChart(){
        categoryData = FXCollections.observableArrayList(
                new ActivitySummaryTableView.chartDataUnit("Study",0),
                new ActivitySummaryTableView.chartDataUnit("Entertainment",0),
                new ActivitySummaryTableView.chartDataUnit("Spirituality",0),
                new ActivitySummaryTableView.chartDataUnit("Exercise",0),
                new ActivitySummaryTableView.chartDataUnit("Rest",0),
                new ActivitySummaryTableView.chartDataUnit("Reading",0),
                new ActivitySummaryTableView.chartDataUnit("Writing",0),
                new ActivitySummaryTableView.chartDataUnit("Arts",0),
                new ActivitySummaryTableView.chartDataUnit("Social",0),
                new ActivitySummaryTableView.chartDataUnit("Media",0),
                new ActivitySummaryTableView.chartDataUnit("Service",0),
                new ActivitySummaryTableView.chartDataUnit("Miscellaneous",0),
                total);

        categoryCol.setCellValueFactory(
                new PropertyValueFactory<ActivitySummaryTableView.chartDataUnit,String>("header"));

        durationByCategoryCol.setCellValueFactory(
                new PropertyValueFactory<ActivitySummaryTableView.chartDataUnit,String>("durationParsed"));

        categoryTable.setItems(categoryData);

    }


    public Runnable scrollPaneInitializer = () -> {
        //wait until the maddening initialize method finishes and the screen is loaded
        try {
            Thread.sleep(7);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Platform.runLater( () -> {
            motherPane.setVvalue(0.0);
        });


    };

    public void setUpActivityChart(TableView<ActivitySummaryTableView.chartDataUnit> table, TableColumn<ActivitySummaryTableView.chartDataUnit, String> header, TableColumn<ActivitySummaryTableView.chartDataUnit, String> duration, ObservableList<ActivitySummaryTableView.chartDataUnit> data){

        header.setCellValueFactory(
                new PropertyValueFactory<ActivitySummaryTableView.chartDataUnit,String>("header"));
        duration.setCellValueFactory(
                new PropertyValueFactory<ActivitySummaryTableView.chartDataUnit,String>("durationParsed"));
        table.setItems(data);
    }



    public static boolean addNewActivityToData(Activity activity, ObservableList data, String headerType){



        if (!data.isEmpty()){
            String header;
            if (headerType.equals("category")){
                header = activity.getCategory();
            }else if (headerType.equals("name")){
                header = activity.getName();
            }
            else {
                return false;
            }



            int duration = activity.getDurationSeconds();





            if (((ActivitySummaryTableView.chartDataUnit)data.get(0)).getHeader().equals("")){
                data.clear();
                data.add(new ActivitySummaryTableView.chartDataUnit(header, duration));
                return true;
            }


//            if (header.equals("Service")){
//                System.out.println(Stats.parseDuration(activity.getDurationSeconds()));
//            }


            boolean fitsIn = false;

            for(int i = 0; i < data.size(); i++){

                ActivitySummaryTableView.chartDataUnit unit = (ActivitySummaryTableView.chartDataUnit) data.get(i);

                if (unit.getHeader().equals(header)){
                    int newDuration = unit.getDurationSecs() + duration;
                    data.remove(unit);
                    data.add(i ,new ActivitySummaryTableView.chartDataUnit(header, newDuration));
//                    if (unit.getHeader().equals("Service")){
//                        System.out.println(unit);
//                        System.out.println(Stats.parseDuration(unit.getDurationSecs()));
//                        System.out.println(Stats.parseDuration(newDuration));;
//                    }
                    fitsIn = true;
                }
            }
            if (!fitsIn){
                data.add(new ActivitySummaryTableView.chartDataUnit(header, duration));
            }


//            if (header.equals("category")){
//                int totalIndex = data.size() - 1;
//
//                data.remove(totalIndex);
//
//            }


        }
        updateTotal();

        categoryData.add(new ActivitySummaryTableView.chartDataUnit("update data", 0));

        categoryData.remove(categoryData.size() - 1);

//        System.out.println("total: "+ total.getDurationParsed());
        return true;


    }


    public static void updateData(ArrayList<Activity> archive){

        categoryData.clear();


//        total = new chartDataUnit("Total",0);


        categoryData.add(new ActivitySummaryTableView.chartDataUnit("Study",0));
        categoryData.add(new ActivitySummaryTableView.chartDataUnit("Entertainment",0));
        categoryData.add(new ActivitySummaryTableView.chartDataUnit("Spirituality",0));
        categoryData.add(new ActivitySummaryTableView.chartDataUnit("Exercise",0));
        categoryData.add(new ActivitySummaryTableView.chartDataUnit("Rest",0));
        categoryData.add(new ActivitySummaryTableView.chartDataUnit("Reading",0));
        categoryData.add(new ActivitySummaryTableView.chartDataUnit("Writing",0));
        categoryData.add(new ActivitySummaryTableView.chartDataUnit("Arts",0));
        categoryData.add(new ActivitySummaryTableView.chartDataUnit("Social",0));
        categoryData.add(new ActivitySummaryTableView.chartDataUnit("Media",0));
        categoryData.add(new ActivitySummaryTableView.chartDataUnit("Service",0));
        categoryData.add(new ActivitySummaryTableView.chartDataUnit("Miscellaneous",0));
        categoryData.add(total);


        studyData.clear();
        entertainmentData.clear();
        spiritualityData.clear();
        exerciseData.clear();
        restData.clear();
        readingData.clear();
        writingData.clear();
        artsData.clear();
        socialData.clear();
        mediaData.clear();
        serviceData.clear();
        miscellaneousData.clear();




        studyData.add(new ActivitySummaryTableView.chartDataUnit("", -1));
        entertainmentData.add(new ActivitySummaryTableView.chartDataUnit("", -1));
        spiritualityData.add(new ActivitySummaryTableView.chartDataUnit("", -1));
        exerciseData.add(new ActivitySummaryTableView.chartDataUnit("", -1));
        restData.add(new ActivitySummaryTableView.chartDataUnit("", -1));
        readingData.add(new ActivitySummaryTableView.chartDataUnit("", -1));
        writingData.add(new ActivitySummaryTableView.chartDataUnit("", -1));
        artsData.add(new ActivitySummaryTableView.chartDataUnit("", -1));
        socialData.add(new ActivitySummaryTableView.chartDataUnit("", -1));
        mediaData.add(new ActivitySummaryTableView.chartDataUnit("", -1));
        serviceData.add(new ActivitySummaryTableView.chartDataUnit("", -1));
        miscellaneousData.add(new ActivitySummaryTableView.chartDataUnit("", -1));



        ObservableList activityData;

        for (Activity activity : archive){
            switch (activity.getCategory()){
                case "Study":
                    activityData = Table.studyData;
                    break;
                case "Entertainment":
                    activityData = Table.entertainmentData;
                    break;
                case "Spirituality":
                    activityData = Table.spiritualityData;
                    break;
                case "Exercise":
                    activityData = Table.exerciseData;
                    break;
                case "Rest":
                    activityData = Table.restData;
                    break;
                case "Reading":
                    activityData = Table.readingData;
                    break;
                case "Writing":
                    activityData = Table.writingData;
                    break;
                case "Arts":
                    activityData = Table.artsData;
                    break;
                case "Media":
                    activityData = Table.mediaData;
                    break;
                case "Social":
                    activityData = Table.socialData;
                    break;
                case "Service":
                    activityData = Table.serviceData;
                    break;
                case "Miscellaneous":
                    activityData = Table.miscellaneousData;
                    break;
                default:
                    activityData = null;
                    break;
            }


            if (!(activityData == null)){
                addNewActivityToData(activity, categoryData, "category");
                addNewActivityToData(activity, activityData,"name");
            }


        }


        studyData.sort(Stats.chartDataUnitComparator);
        entertainmentData.sort(Stats.chartDataUnitComparator);
        spiritualityData.sort(Stats.chartDataUnitComparator);
        exerciseData.sort(Stats.chartDataUnitComparator);
        restData.sort(Stats.chartDataUnitComparator);
        readingData.sort(Stats.chartDataUnitComparator);
        writingData.sort(Stats.chartDataUnitComparator);
        artsData.sort(Stats.chartDataUnitComparator);
        socialData.sort(Stats.chartDataUnitComparator);
        mediaData.sort(Stats.chartDataUnitComparator);
        serviceData.sort(Stats.chartDataUnitComparator);
        miscellaneousData.sort(Stats.chartDataUnitComparator);

        categoryData.sort(Stats.chartDataUnitComparator);
        updateTotal();
        moveTotalToBottom();


//        System.out.println("total: "+ total.getDurationParsed());


    }

    public static void updateTotal(){
        total.setDurationSecs(0);
        for (Activity activity : ArchiveDBModel.archive){
            total.setDurationSecs(total.getDurationSecs() + activity.getDurationSeconds());
        }
    }


    public static void moveTotalToBottom(){

        ActivitySummaryTableView.chartDataUnit firstUnit = categoryData.get(0);

        if (firstUnit.getHeader().equals("Total")){
            categoryData.remove(firstUnit);
            categoryData.add(firstUnit);
        }
    }




    public void goToScheduleCreator(){
        App.sceneNavigationModel.gotoScene(SceneNavigationModel.scheduleCreator, SceneNavigationModel.table);
    }


    public void goToStats(){
        App.sceneNavigationModel.gotoScene(SceneNavigationModel.stats, SceneNavigationModel.table);
    }

}
