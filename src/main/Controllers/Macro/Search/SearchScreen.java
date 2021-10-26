package main.Controllers.Macro.Search;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import main.App;
import main.Controllers.Macro.Infographics.BarComponentManager;
import main.Controllers.Macro.Search.SearchTag.SearchTag;
import main.Controllers.PrototypeController;
import main.Models.DBModels.ReadFromDBModel;
import main.Models.MacroDataModel;
import main.Models.SceneNavigationModel;
import main.Models.SearchModel;

import java.util.ArrayList;

public class SearchScreen extends PrototypeController {



    @FXML Button goToCreatorButton;

    @FXML TextField activitySearchField;

    @FXML ScrollPane activityTagBoxContainer;
    @FXML FlowPane selectedActivitiesBox;
    @FXML Button seekButton;
    @FXML Button clearButton;

    @FXML ListView suggestedActivityNamesListView;
    ObservableList<String> observableListOfsuggestedActivityNames = FXCollections.observableArrayList();






    public void initialize(){

        //set prototype property 'gotoCreatorButton'
        setGoToCreatorButton(goToCreatorButton);

        suggestedActivityNamesListView.setItems(observableListOfsuggestedActivityNames);
        //default suggested activities
        suggestAllActivities();

        //hide scrollbars in scrollpane containing the selected activities box
        activityTagBoxContainer.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        activityTagBoxContainer.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    void suggestAllActivities(){
        ArrayList<String> suggestionList =  ReadFromDBModel.getSuggestions("", "");

        observableListOfsuggestedActivityNames.addAll(suggestionList);
    }

    @FXML
    void onEntryToActivitySearchField(){
        SearchModel.searchPastActivityListForNameAndLoadToObservableList(activitySearchField.getText(), observableListOfsuggestedActivityNames, "");
        //remove already selected activity names
        observableListOfsuggestedActivityNames.removeAll(MacroDataModel.selectedActivityNames);
    }

    @FXML
    void onActivitySelectedFromSuggestions(){
        String selectedActivityName = (String) suggestedActivityNamesListView.getSelectionModel().getSelectedItem();

        //remove selected activity name from listview
        observableListOfsuggestedActivityNames.remove(selectedActivityName);

        if (!(selectedActivityName == null) && !MacroDataModel.selectedActivityNames.contains(selectedActivityName)){
            addActivityTag(selectedActivityName);

            //scroll scrollpane to bottomost position
            activityTagBoxContainer.setVvalue(1);

            // add name to model
            MacroDataModel.selectedActivityNames.add(selectedActivityName);

            //enable seek button
            seekButton.setDisable(false);
        }
    }

    void addActivityTag(String activityName){
        SearchTag tag = new SearchTag(activityName);
        selectedActivitiesBox.getChildren().add(tag);
    }

    @FXML
    public void seek() {

        //initialize Macro Data Model
        MacroDataModel.totalActivityDurationsByDate.clear();
        MacroDataModel.selectedPastActivities.clear();

        //add selected pastActivities to macro data model
        MacroDataModel.composeSelectedPastActivitiesListFromNames();
//        System.out.println("selectedPastActivities: " + MacroDataModel.selectedPastActivities);
        MacroDataModel.composeTotalActivityDurationsByDate();

        //find maximum duration of activity
        MacroDataModel.setMaximumActivityDuration();
//        System.out.println(MacroDataModel.totalActivityDurationsByDate);

        //go to infographic
        App.sceneNavigationModel.gotoScene(SceneNavigationModel.infographics, activityTagBoxContainer.getScene());
        //set macro default screen to infographics
        SceneNavigationModel.macro = SceneNavigationModel.infographics;

        //update day barcomponents
        BarComponentManager.updateAllBarCompData();


//        System.out.println("durations: "+MacroDataModel.totalActivityDurationsByDate);
//        System.out.println("selectedActivityNames: "+MacroDataModel.selectedActivityNames);
//        System.out.println("selectedActivities: "+MacroDataModel.selectedPastActivities);

//        for (PastActivity pastActivity : MacroDataModel.selectedPastActivities){
//            System.out.println(pastActivity.getName());
//            System.out.println(pastActivity.getDurationsByDate());
//        }
    }

    @FXML
    public void clear(){
        //clear activity tag box
        selectedActivitiesBox.getChildren().clear();

        //clear model
        MacroDataModel.selectedActivityNames.clear();

        //disable seek button
        seekButton.setDisable(true);

        //run animation
        new Thread(clearButtonClickedAnimation).start();

    }


    @FXML
    public void goBack(){
        App.sceneNavigationModel.gotoScene(SceneNavigationModel.stats, SceneNavigationModel.macro);
    }

    @FXML
    public void goToCreator(){
        App.sceneNavigationModel.gotoScene(SceneNavigationModel.scheduleCreator, SceneNavigationModel.macro);
    }


    @FXML
    public void onClearButtonHovered(){
////        System.out.println("mouse entered");
//        System.out.println(activityTagBoxContainer.getHeight());
//        System.out.println(activityTagBoxContainer.getWidth());
        clearButton.getStyleClass().add("clear-button-hovered");
    }

    @FXML
    public void onClearButtonUnHovered(){
        clearButton.getStyleClass().remove("clear-button-hovered");
    }

    Runnable clearButtonClickedAnimation = new Runnable() {
        @Override
        public void run() {

            //clicked style applied
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    clearButton.getStyleClass().add("clear-button-clicked");
                }
            });

            //wait 100 milis
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //remove clicked style
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    clearButton.getStyleClass().remove("clear-button-clicked");
                }
            });


        }
    };



    public void checkIfTagContainerEmptyAndDisableSeekButton(){
        if (MacroDataModel.selectedActivityNames.isEmpty()){
            seekButton.setDisable(true);
        }
    }




}
