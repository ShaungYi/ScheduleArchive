package main.Controllers.Macro.Search.SearchTag;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import main.Controllers.Macro.Search.SearchScreen;
import main.Models.MacroDataModel;
import main.Models.SceneNavigationModel;


public class SearchTagController{


    @FXML
    HBox tagBodyHbox;

    @FXML
    Label activityNameLabel;
    String activityName;

    @FXML
    Button removeButton;


    public void initialize(){
        activityNameLabel.setText(activityName);
    }

    public void setText(String text){
        activityNameLabel.setText(text);
    }

    @FXML
    public void removeTagSelf(){

        SearchTag container = (SearchTag) tagBodyHbox.getParent();
        Group parent = new Group(container);
        Group tagBox = (Group) container.getParent();
        tagBox.getChildren().remove(container);

        //also remove data
        MacroDataModel.selectedActivityNames.remove(activityNameLabel.getText());

        //call checkIfTagBoxEmpty in SearchScreen controller
        FXMLLoader searchScreenLoader = (FXMLLoader) SceneNavigationModel.searchScreen.getUserData();
        SearchScreen searchScreenController = searchScreenLoader.getController();
        searchScreenController.checkIfTagContainerEmptyAndDisableSeekButton();
    }


}
