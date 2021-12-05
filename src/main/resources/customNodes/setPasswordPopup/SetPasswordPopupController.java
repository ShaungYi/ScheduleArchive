package main.resources.customNodes.setPasswordPopup;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import main.Models.DBModels.WriteToDBModel;
import main.Models.Graphics.General;

public class SetPasswordPopupController {

    @FXML
    PasswordField passField;
    @FXML
    PasswordField confirmField;
    @FXML
    Label passwordMismatchMessageLabel;

    Pane grandmotherPane;
    VBox motherPane;
    TextArea noteTextArea;
    ImageView noteLock;
    SetPasswordPopup thisPopup;

    @FXML
    public void onDoneButtonClicked(){
        String pass = passField.getText();
        if (pass.equals(confirmField.getText())){
            savePassword(pass);
            General.reenableTimeLineMainScreen(grandmotherPane, motherPane, noteTextArea, noteLock);
        } else {
            passwordMismatchMessageLabel.setVisible(true);
        }
    }

    private void savePassword(String pass){
        WriteToDBModel.updateBackupSettings("password", pass);
    }

    public void setMotherSceneParameters(Pane gmPane, VBox mPane, TextArea ntArea, ImageView nLock){
        grandmotherPane = gmPane;
        motherPane = mPane;
        noteTextArea = ntArea;
        noteLock = nLock;
    }
}
