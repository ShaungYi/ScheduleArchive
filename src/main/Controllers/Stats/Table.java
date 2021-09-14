package main.Controllers.Stats;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import main.Controllers.PrototypeController;
import main.Models.ArchiveDBModel;
import main.Models.DateTimeModel;
import main.Utility.Activity;
import main.App;

import java.util.ArrayList;

public class Table extends PrototypeController {

    @FXML
    BorderPane motherPane;

    @FXML
    Button goToCreatorButton;


    @FXML
    TableView<chartDataUnit> categoryTable;
    @FXML
    TableColumn<chartDataUnit, String> categoryCol;
    @FXML
    TableColumn<chartDataUnit, String> durationByCategoryCol;
    public static ObservableList<chartDataUnit> categoryData = FXCollections.observableArrayList(new chartDataUnit("",-1));


    @FXML
    TableView<chartDataUnit> studyTable;
    @FXML
    TableColumn<chartDataUnit, String> studyCol;
    @FXML
    TableColumn<chartDataUnit, String> studyDurationCol;
    public static ObservableList<chartDataUnit> studyData = FXCollections.observableArrayList(new chartDataUnit("",-1));


    @FXML
    TableView<chartDataUnit> entertainmentTable;
    @FXML
    TableColumn<chartDataUnit, String> entertainmentCol;
    @FXML
    TableColumn<chartDataUnit, String> entertainmentDurationCol;
    public static ObservableList<chartDataUnit> entertainmentData = FXCollections.observableArrayList(new chartDataUnit("",-1));


    @FXML
    TableView<chartDataUnit> spiritualityTable;
    @FXML
    TableColumn<chartDataUnit, String> spiritualityCol;
    @FXML
    TableColumn<chartDataUnit, String> spiritualityDurationCol;
    public static ObservableList<chartDataUnit> spiritualityData = FXCollections.observableArrayList(new chartDataUnit("",-1));


    @FXML
    TableView<chartDataUnit> exerciseTable;
    @FXML
    TableColumn<chartDataUnit, String> exerciseCol;
    @FXML
    TableColumn<chartDataUnit, String> exerciseDurationCol;
    public static ObservableList<chartDataUnit> exerciseData = FXCollections.observableArrayList(new chartDataUnit("",-1));


    @FXML
    TableView<chartDataUnit> restTable;
    @FXML
    TableColumn<chartDataUnit, String> restCol;
    @FXML
    TableColumn<chartDataUnit, String> restDurationCol;
    public static ObservableList<chartDataUnit> restData = FXCollections.observableArrayList(new chartDataUnit("",-1));


    @FXML
    TableView<chartDataUnit> readingTable;
    @FXML
    TableColumn<chartDataUnit, String> readingCol;
    @FXML
    TableColumn<chartDataUnit, String> readingDurationCol;
    public static ObservableList<chartDataUnit> readingData = FXCollections.observableArrayList(new chartDataUnit("",-1));


    @FXML
    TableView<chartDataUnit> writingTable;
    @FXML
    TableColumn<chartDataUnit, String> writingCol;
    @FXML
    TableColumn<chartDataUnit, String> writingDurationCol;
    public static ObservableList<chartDataUnit> writingData = FXCollections.observableArrayList(new chartDataUnit("",-1));


    @FXML
    TableView<chartDataUnit> artsTable;
    @FXML
    TableColumn<chartDataUnit, String> artsCol;
    @FXML
    TableColumn<chartDataUnit, String> artsDurationCol;
    public static ObservableList<chartDataUnit> artsData = FXCollections.observableArrayList(new chartDataUnit("",-1));


    @FXML
    TableView<chartDataUnit> miscellaneousTable;
    @FXML
    TableColumn<chartDataUnit, String> miscellaneousCol;
    @FXML
    TableColumn<chartDataUnit, String> miscellaneousDurationCol;
    public static ObservableList<chartDataUnit> miscellaneousData = FXCollections.observableArrayList(new chartDataUnit("",-1));
    public static Boolean miscellaneousTableInitialized = false;

    @FXML
    TableView<chartDataUnit> socialTable;
    @FXML
    TableColumn<chartDataUnit, String> socialColumn;
    @FXML
    TableColumn<chartDataUnit, String> socialDurationColumn;
    public static ObservableList<chartDataUnit> socialData = FXCollections.observableArrayList(new chartDataUnit("",-1));
    public static Boolean socialTableInitialized = false;


    @FXML
    TableView<chartDataUnit> mediaTable;
    @FXML
    TableColumn<chartDataUnit, String> mediaColumn;
    @FXML
    TableColumn<chartDataUnit, String> mediaDurationColumn;
    public static ObservableList<chartDataUnit> mediaData = FXCollections.observableArrayList(new chartDataUnit("",-1));
    public static Boolean mediaTableInitialized = false;

    @FXML
    TableView<chartDataUnit> serviceTable;
    @FXML
    TableColumn<chartDataUnit, String> serviceColumn;
    @FXML
    TableColumn<chartDataUnit, String> serviceDurationColumn;
    public static ObservableList<chartDataUnit> serviceData = FXCollections.observableArrayList(new chartDataUnit("",-1));
    public static Boolean serviceTableInitialized = false;




    public static chartDataUnit total = new chartDataUnit("Total",0);




    public void initialize(){

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


    }

    public void setupCategoryChart(){
        categoryData = FXCollections.observableArrayList(
                new chartDataUnit("Study",0),
                new chartDataUnit("Entertainment",0),
                new chartDataUnit("Spirituality",0),
                new chartDataUnit("Exercise",0),
                new chartDataUnit("Rest",0),
                new chartDataUnit("Reading",0),
                new chartDataUnit("Writing",0),
                new chartDataUnit("Arts",0),
                new chartDataUnit("Social",0),
                new chartDataUnit("Media",0),
                new chartDataUnit("Service",0),
                new chartDataUnit("Miscellaneous",0),
                total);

        categoryCol.setCellValueFactory(
                new PropertyValueFactory<chartDataUnit,String>("header"));

        durationByCategoryCol.setCellValueFactory(
                new PropertyValueFactory<chartDataUnit,String>("durationParsed"));

        categoryTable.setItems(categoryData);

    }


    public void setUpActivityChart(TableView<chartDataUnit> table, TableColumn<chartDataUnit, String> header, TableColumn<chartDataUnit, String> duration, ObservableList<chartDataUnit> data){

        header.setCellValueFactory(
                new PropertyValueFactory<chartDataUnit,String>("header"));
        duration.setCellValueFactory(
                new PropertyValueFactory<chartDataUnit,String>("durationParsed"));
        table.setItems(data);
    }


    public void onScroll(ScrollEvent e){
        double motherPaneLayoutY = motherPane.getLayoutY();
        //System.out.println(motherPaneLayoutY);
        double change = e.getDeltaY();

        if (motherPaneLayoutY + change < 10 && motherPaneLayoutY + change > -2000){
            motherPane.setLayoutY(motherPaneLayoutY + change);
        }

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





            if (((chartDataUnit)data.get(0)).getHeader().equals("")){
                data.clear();
                data.add(new chartDataUnit(header, duration));
                return true;
            }


//            if (header.equals("Service")){
//                System.out.println(Stats.parseDuration(activity.getDurationSeconds()));
//            }


            boolean fitsIn = false;

            for(int i = 0; i < data.size(); i++){

                chartDataUnit unit = (chartDataUnit) data.get(i);

                if (unit.getHeader().equals(header)){
                    int newDuration = unit.getDurationSecs() + duration;
                    data.remove(unit);
                    data.add(i ,new chartDataUnit(header, newDuration));
//                    if (unit.getHeader().equals("Service")){
//                        System.out.println(unit);
//                        System.out.println(Stats.parseDuration(unit.getDurationSecs()));
//                        System.out.println(Stats.parseDuration(newDuration));;
//                    }
                    fitsIn = true;
                }
            }
            if (!fitsIn){
                data.add(new chartDataUnit(header, duration));
            }


//            if (header.equals("category")){
//                int totalIndex = data.size() - 1;
//
//                data.remove(totalIndex);
//
//            }


        }
        updateTotal();

        categoryData.add(new chartDataUnit("update data", 0));

        categoryData.remove(categoryData.size() - 1);

//        System.out.println("total: "+ total.getDurationParsed());
        return true;


    }


    public static void updateData(ArrayList<Activity> archive){

        categoryData.clear();


//        total = new chartDataUnit("Total",0);


        categoryData.add(new chartDataUnit("Study",0));
        categoryData.add(new chartDataUnit("Entertainment",0));
        categoryData.add(new chartDataUnit("Spirituality",0));
        categoryData.add(new chartDataUnit("Exercise",0));
        categoryData.add(new chartDataUnit("Rest",0));
        categoryData.add(new chartDataUnit("Reading",0));
        categoryData.add(new chartDataUnit("Writing",0));
        categoryData.add(new chartDataUnit("Arts",0));
        categoryData.add(new chartDataUnit("Social",0));
        categoryData.add(new chartDataUnit("Media",0));
        categoryData.add(new chartDataUnit("Service",0));
        categoryData.add(new chartDataUnit("Miscellaneous",0));
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




        studyData.add(new chartDataUnit("", -1));
        entertainmentData.add(new chartDataUnit("", -1));
        spiritualityData.add(new chartDataUnit("", -1));
        exerciseData.add(new chartDataUnit("", -1));
        restData.add(new chartDataUnit("", -1));
        readingData.add(new chartDataUnit("", -1));
        writingData.add(new chartDataUnit("", -1));
        artsData.add(new chartDataUnit("", -1));
        socialData.add(new chartDataUnit("", -1));
        mediaData.add(new chartDataUnit("", -1));
        serviceData.add(new chartDataUnit("", -1));
        miscellaneousData.add(new chartDataUnit("", -1));



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

        chartDataUnit firstUnit = categoryData.get(0);

        if (firstUnit.getHeader().equals("Total")){
            categoryData.remove(firstUnit);
            categoryData.add(firstUnit);
        }
    }




    public void extendOnMouseEnter(MouseEvent e){
        TableView<chartDataUnit> sourceTable = (TableView) e.getSource();
        TableColumn headerCol = sourceTable.getColumns().get(0);
        TableColumn durationCol = sourceTable.getColumns().get(1);


        int maxHeaderLength = 0;
        int maxDurationLength = 0;

        for (int i = 0; i < sourceTable.getItems().size(); i++){

            String header = (String)headerCol.getCellData(i);
            int headerLength = header.length();

            String duration = (String) durationCol.getCellData(i);
            int durationLength = duration.length();

            if (headerLength > maxHeaderLength){
                maxHeaderLength = headerLength;
            }

            if (durationLength > maxDurationLength){
                maxDurationLength = durationLength;
            }
        }

        if (maxHeaderLength > 25){
            headerCol.setPrefWidth(maxHeaderLength*7.6);
        }

        if (maxDurationLength > 25){
            durationCol.setPrefWidth(maxDurationLength*7.6);
        }


    }


    public void shrinkOnMouseExit(MouseEvent e){
        TableView<chartDataUnit> sourceTable = (TableView) e.getSource();
        TableColumn headerCol = sourceTable.getColumns().get(0);
        TableColumn durationCol = sourceTable.getColumns().get(1);

        headerCol.setPrefWidth(190.0);
        durationCol.setPrefWidth(190.0);
    }




    public void goToScheduleCreator(){
        App.sceneNavigationModel.gotoScene(App.scheduleCreator, App.table);
    }


    public void goToStats(){
        App.sceneNavigationModel.gotoScene(App.stats, App.table);
    }


    public static class chartDataUnit{
        String header;
        int durationSecs;
        String durationParsed;

        public chartDataUnit(String header, int durationSecs){
            this.header = header;
            this.durationSecs = durationSecs;
            durationParsed = durationSecs == -1 ? "" : DateTimeModel.parseDurationToString(durationSecs);
        }

        public String getHeader() {
            return header;
        }

        public String getDurationParsed() {
            return durationParsed;
        }

        public int getDurationSecs() {
            return durationSecs;
        }

        public void setDurationSecs(int durationSecs) {
            this.durationSecs = durationSecs;
            this.durationParsed = DateTimeModel.parseDurationToString(durationSecs);
        }

        public String toString(){
            return "header: "+header+", duration: "+durationParsed;
        }
    }
}
