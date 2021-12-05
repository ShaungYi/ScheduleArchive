package main.resources.customNodes.setPasswordPopup;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class SetPasswordPopup extends AnchorPane {

    SetPasswordPopupController controller;



    public SetPasswordPopup(Pane gmPane, Pane mPane, TextArea ntArea, ImageView nLock){
        super();
        universalConstructorStuff();
        controller.setParameters(gmPane, mPane, ntArea, nLock, true);

    }

    public SetPasswordPopup(Pane gmPane, Pane mPane){
        super();
        universalConstructorStuff();
        controller.setParameters(gmPane, mPane, null, null, false);

    }

    public void universalConstructorStuff(){
        this.setStyle("-fx-background-color: transparent;");

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                    "setPasswordPopup.fxml"));
            Node n = fxmlLoader.load();
            this.getChildren().add(n);

            controller = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
