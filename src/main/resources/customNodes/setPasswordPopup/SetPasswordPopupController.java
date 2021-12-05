package main.resources.customNodes.setPasswordPopup;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
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
    Pane motherPane;
    TextArea noteTextArea;
    ImageView noteLock;

    boolean timeLineRelated;

    @FXML
    public void onDoneButtonClicked(){
        String pass = passField.getText();
        if (pass.equals(confirmField.getText())){
            savePassword(pass);

            if (timeLineRelated){
                General.reenableMainScreen(grandmotherPane, motherPane, noteTextArea, noteLock);
            } else {
                General.reenableMainScreen(grandmotherPane, motherPane, null, null);
            }
        } else {
            passwordMismatchMessageLabel.setVisible(true);
        }
    }

    private void savePassword(String pass){
        WriteToDBModel.updateBackupSettings("password", pass);
    }

    public void setParameters(Pane gmPane, Pane mPane, TextArea ntArea, ImageView nLock, boolean tlRelated){
        grandmotherPane = gmPane;
        motherPane = mPane;
        noteTextArea = ntArea;
        noteLock = nLock;
        timeLineRelated = tlRelated;
    }
}
