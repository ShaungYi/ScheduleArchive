package main.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PrototypeController {
    @FXML
    Button goToCreatorButton;

    public Button getGoToCreatorButton() {
        return goToCreatorButton;
    }

    public void setGoToCreatorButton(Button goToCreatorButton) {
        this.goToCreatorButton = goToCreatorButton;
    }

    public void disableCreatorButton(){
        goToCreatorButton.setDisable(true);
//        System.out.println("disabled creator button");
    }

    public void enableCreatorButton(){
        goToCreatorButton.setDisable(false);
//        System.out.println("enabled creator button");
    }
}
