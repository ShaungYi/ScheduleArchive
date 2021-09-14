package main.Controllers.Macro.Search.SearchTag;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class SearchTag extends AnchorPane {

    SearchTagController controller;

    public SearchTag(String activityName){
        super();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                    "searchTag.fxml"));
            Node n = fxmlLoader.load();
            this.getChildren().add(n);

            // set tag text to activityName
            ((SearchTagController)fxmlLoader.getController()).setText(activityName);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
