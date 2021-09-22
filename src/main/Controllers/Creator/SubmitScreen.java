package main.Controllers.Creator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import main.Controllers.PrototypeController;
import main.Models.DBModels.ArchiveDBModel;
import main.Models.DBModels.WriteToDBModel;
import main.Models.DateTimeModel;
import main.Models.PastActivityArchiveModel;
import main.Models.SceneNavigationModel;
import main.Models.SearchModel;
import main.Controllers.Stats.Stats;
import main.Controllers.Stats.Table;
import main.Utility.Activity;
import main.App;

public class SubmitScreen extends PrototypeController {


    @FXML Label duration;


    @FXML
    Label startTime;
    @FXML
    Label endTime;


    @FXML
    TextField nameField;
    @FXML
    Button submitButton;




    @FXML
    ListView suggestions;
    ObservableList<String> observableListOfsuggestions = FXCollections.observableArrayList();
    @FXML
    GridPane suggestionsContainer;


    Activity activityInQuestion;


    public void initialize(){
        updateActivityInQuestion();
        setUpDisplays();
        setUpSuggestionsBar();
    }

    private void setUpSuggestionsBar(){
        suggestions.setItems(observableListOfsuggestions);
    }


    private void updateActivityInQuestion(){
        ;
        activityInQuestion = ArchiveDBModel.archive.get(ArchiveDBModel.archive.size() - 1);
    }

    public void setUpDisplays(){

        duration.setText(DateTimeModel.parseDurationToString(activityInQuestion.getDurationSeconds()));

        startTime.setText(DateTimeModel.parseSeconds(activityInQuestion.getStartTimeSecs()));
        endTime.setText(DateTimeModel.parseSeconds(activityInQuestion.getEndTimeSecs()));
    }


    public void submit(){

        DateTimeModel.currentDay = ArchiveDBModel.archive.get(0).getDate();

        activityInQuestion.setName(nameField.getText());



        mergeLikeActivities();


        Table.updateData(ArchiveDBModel.archive);
        Table.categoryData.sort(Stats.chartDataUnitComparator);
        Table.moveTotalToBottom();


        PastActivityArchiveModel.loadActivity(activityInQuestion.getName(), activityInQuestion.getCategory(), activityInQuestion.getDurationSeconds(), activityInQuestion.getDate());


        WriteToDBModel.saveDataSynchronously();



        App.editLog.wipe();



        goToCreator();
    }


    public void mergeLikeActivities(){

        if (ArchiveDBModel.archive.size() > 1){
            Activity prevActivity = ArchiveDBModel.archive.get(ArchiveDBModel.archive.indexOf(activityInQuestion) - 1);


            if (activityInQuestion.getName().equals(prevActivity.getName()) && activityInQuestion.getCategory().equals(prevActivity.getCategory())){

                activityInQuestion.setDurationSeconds(prevActivity.getDurationSeconds() + activityInQuestion.getDurationSeconds());
                activityInQuestion.setStartTimeSecs(prevActivity.getStartTimeSecs());

                ArchiveDBModel.archive.remove(prevActivity);
            }
        }
    }

    @FXML
    public void onEntryToNameField(){

        observableListOfsuggestions.clear();

        if (nameField.getText().equals("")) {
            submitButton.setDisable(true);
        } else {
            submitButton.setDisable(false);
        }

        SearchModel.searchPastActivityListForNameAndLoadToObservableList(nameField.getText(), observableListOfsuggestions, activityInQuestion.getCategory());

        if (observableListOfsuggestions.isEmpty()){
            hideSuggestions();
            suggestions.setDisable(true);
        }
        else{
            showSuggestions();
            suggestions.setDisable(false);

        }
    }


    public void completeNameField(){

        String suggestion = (String)suggestions.getSelectionModel().getSelectedItem();

        nameField.setText((String)suggestions.getSelectionModel().getSelectedItem());
        hideSuggestions();
    }

    public void showSuggestions(){
        suggestionsContainer.setOpacity(1);
    }

    public void hideSuggestions(){
        suggestionsContainer.setOpacity(0);
    }





    public void goToCreator(){
        App.sceneNavigationModel.gotoScene(SceneNavigationModel.scheduleCreator, suggestionsContainer.getScene());
    }

}
