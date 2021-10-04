package main.Controllers.Stats.InfographicsNavigationTab;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import main.Controllers.Macro.Search.SearchTag.SearchTagController;

import java.io.IOException;

public class InfographicsNavigationTab extends AnchorPane {

    public InfographicsNavigationTabController controller;

    public InfographicsNavigationTab(){
        super();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                    "InfographicsNavigationTab.fxml"));
            Node n = fxmlLoader.load();
            this.getChildren().add(n);

            controller = fxmlLoader.getController();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSelectedInfographic(String selectedKey){

        for (String key : controller.navButtons.keySet()){
            controller.navButtons.get(key).setTranslateX(0);
        }

        controller.navButtons.get(selectedKey).setTranslateX(-30);
    }
}
