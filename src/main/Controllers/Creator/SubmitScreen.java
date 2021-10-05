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
import main.Models.SceneNavigationModel;
import main.Models.SearchModel;
import main.Controllers.Stats.Stats;
import main.Controllers.Stats.Table;
import main.Utility.Activity;
import main.App;

public class SubmitScreen extends PrototypeController {

    @FXML
    Label duration;

    @FXML
    Label startTime;
    @FXML
    Label endTime;


    @FXML
    TextField nameField;

    @FXML
    TextArea notesTextArea;

    @FXML
    Button submitButton;




    @FXML
    ListView suggestions;
    ObservableList<String> observableListOfsuggestions = FXCollections.observableArrayList();


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

        duration.setText(DateTimeModel.parseDurationToString(activityInQuestion.getDurationSeconds(), false));

        startTime.setText(DateTimeModel.parseSeconds(activityInQuestion.getStartTimeSecs()));
        endTime.setText(DateTimeModel.parseSeconds(activityInQuestion.getEndTimeSecs()));
    }


    public void submit(){
        System.out.println(notesTextArea);

        DateTimeModel.currentDay = ArchiveDBModel.archive.get(0).getDate();

        activityInQuestion.setName(nameField.getText());
        activityInQuestion.setDescription(notesTextArea.getText());

        mergeLikeActivities();


        Table.updateData(ArchiveDBModel.archive);
        Table.categoryData.sort(Stats.chartDataUnitComparator);
        Table.moveTotalToBottom();

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

        System.out.println("suggestions num: "+observableListOfsuggestions.size());

        if (observableListOfsuggestions.isEmpty()){

            hideSuggestions();
            suggestions.setDisable(true);
        }
        else{
            nameField.getStyleClass().removeAll("search-text-field");
            nameField.getStyleClass().add("text-field-with-suggestions");

            showSuggestions();
            suggestions.setDisable(false);

        }
    }


    public void completeNameField(){

        String suggestion = (String)suggestions.getSelectionModel().getSelectedItem();

        nameField.setText(suggestion);
        hideSuggestions();
    }

    public void showSuggestions(){
        suggestions.setVisible(true);
    }

    public void hideSuggestions(){
        nameField.getStyleClass().removeAll("text-field-with-suggestions");
        nameField.getStyleClass().add("search-text-field");
        suggestions.setVisible(false);
    }





    public void goToCreator(){
        App.sceneNavigationModel.gotoScene(SceneNavigationModel.scheduleCreator, suggestions.getScene());
    }

}
