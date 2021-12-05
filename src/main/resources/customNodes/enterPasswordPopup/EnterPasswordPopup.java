package main.resources.customNodes.enterPasswordPopup;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class EnterPasswordPopup extends AnchorPane {

    EnterPasswordPopupController controller;

    public EnterPasswordPopup(Pane gmPane, Pane mPane, TextArea ntArea, ImageView nLock, Runnable execAfterAuth){
        super();
        universalConstructorStuff();
        controller.setParameters(gmPane, mPane, ntArea, nLock, execAfterAuth, true);

    }

    public EnterPasswordPopup(Pane gmPane, Pane mPane, Runnable execAfterAuth){
        super();
        universalConstructorStuff();
        controller.setParameters(gmPane, mPane, null, null, execAfterAuth, false);

    }


    private void universalConstructorStuff(){
        this.setStyle("-fx-background-color: transparent;");

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                    "enterPasswordPopup.fxml"));
            Node n = fxmlLoader.load();
            this.getChildren().add(n);

            controller = fxmlLoader.getController();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
