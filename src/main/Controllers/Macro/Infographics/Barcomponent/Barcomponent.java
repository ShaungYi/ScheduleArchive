package main.Controllers.Macro.Infographics.Barcomponent;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import main.Controllers.Macro.Search.SearchTag.SearchTagController;
import main.Models.InfographicsModel;

import java.io.IOException;

public class Barcomponent extends AnchorPane {


    private BarComponentController controller;

    public Barcomponent(String date, double barFrequency){
        super();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                    "barComponent.fxml"));
            Node n = fxmlLoader.load();
            this.getChildren().add(n);

            controller = fxmlLoader.getController();

            controller.configure(date, barFrequency);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
