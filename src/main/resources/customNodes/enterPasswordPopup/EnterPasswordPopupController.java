package main.resources.customNodes.enterPasswordPopup;

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
import main.Models.DBModels.ReadFromDBModel;
import main.Models.Graphics.General;

public class EnterPasswordPopupController {


    @FXML
    PasswordField passField;

    Pane grandmotherPane;
    VBox motherPane;
    TextArea noteTextArea;
    ImageView noteLock;

    Runnable executeAfterAuthRunnable;

    @FXML
    public void onPassFieldTyped(){
        if (passField.getText().equals(ReadFromDBModel.getPassword())){
            General.reenableTimeLineMainScreen(grandmotherPane, motherPane, noteTextArea, noteLock);
            executeAfterAuthRunnable.run();
        }
    }

    public void setMotherSceneParameters(Pane gmPane, VBox mPane, TextArea ntArea, ImageView nLock, Runnable execAfterAuth){
        grandmotherPane = gmPane;
        motherPane = mPane;
        noteTextArea = ntArea;
        noteLock = nLock;
        executeAfterAuthRunnable = execAfterAuth;
    }
}
