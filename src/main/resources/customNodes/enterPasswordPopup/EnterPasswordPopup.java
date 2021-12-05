package main.resources.customNodes.enterPasswordPopup;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import main.Models.DBModels.ReadFromDBModel;

import java.io.IOException;

public class EnterPasswordPopup extends AnchorPane {

    EnterPasswordPopupController controller;

    public EnterPasswordPopup(Pane gmPane, VBox mPane, TextArea ntArea, ImageView nLock, Runnable execAfterAuth){
        super();


        this.setStyle("-fx-background-color: transparent;");

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                    "enterPasswordPopup.fxml"));
            Node n = fxmlLoader.load();
            this.getChildren().add(n);

            controller = fxmlLoader.getController();
            controller.setMotherSceneParameters(gmPane, mPane, ntArea, nLock, execAfterAuth);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
