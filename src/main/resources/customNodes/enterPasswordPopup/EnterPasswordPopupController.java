package main.resources.customNodes.enterPasswordPopup;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import main.Models.DBModels.ReadFromDBModel;
import main.Models.Graphics.General;

public class EnterPasswordPopupController {


    @FXML
    PasswordField passField;

    Pane grandmotherPane;
    Pane motherPane;
    TextArea noteTextArea;
    ImageView noteLock;

    boolean timeLineRelated;

    Runnable executeAfterAuthRunnable;

    @FXML
    public void onPassFieldTyped(){
        if (passField.getText().equals(ReadFromDBModel.getPassword())){
            System.out.println(timeLineRelated);
            if (timeLineRelated){
                General.reenableMainScreen(grandmotherPane, motherPane, noteTextArea, noteLock);
            } else {
                General.removePopup(grandmotherPane);
            }
            executeAfterAuthRunnable.run();
        }
    }

    public void setParameters(Pane gmPane, Pane mPane, TextArea ntArea, ImageView nLock, Runnable execAfterAuth, boolean tlRelated){
        grandmotherPane = gmPane;
        motherPane = mPane;
        noteTextArea = ntArea;
        noteLock = nLock;
        executeAfterAuthRunnable = execAfterAuth;
        timeLineRelated = tlRelated;
    }
}
