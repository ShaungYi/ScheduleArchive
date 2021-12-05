package main.resources.customNodes.setPasswordPopup;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class SetPasswordPopup extends AnchorPane {

    SetPasswordPopupController controller;



    public SetPasswordPopup(Pane gmPane, VBox mPane, TextArea ntArea, ImageView nLock){
        super();

        this.setStyle("-fx-background-color: transparent;");

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                    "setPasswordPopup.fxml"));
            Node n = fxmlLoader.load();
            this.getChildren().add(n);

            controller = fxmlLoader.getController();
            controller.setMotherSceneParameters(gmPane, mPane, ntArea, nLock);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
