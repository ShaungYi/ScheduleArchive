package main.resources.customNodes.activitySummaryTableView;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import main.Controllers.Stats.Table;

public class ActivitySummaryTableView extends TableView {

    public ActivitySummaryTableView() {
        super();
        this.setOnMouseEntered(extendOnMouseEnter);
        this.setOnMouseExited(shrinkOnMouseExit);
    }



    EventHandler extendOnMouseEnter = event -> {
        TableView<Table.chartDataUnit> sourceTable = (TableView) event.getSource();
        TableColumn headerCol = sourceTable.getColumns().get(0);
        TableColumn durationCol = sourceTable.getColumns().get(1);


        int maxHeaderLength = 0;
        int maxDurationLength = 0;

        for (int i = 0; i < sourceTable.getItems().size(); i++){

            String header = (String)headerCol.getCellData(i);
            int headerLength = header.length();

            String duration = (String) durationCol.getCellData(i);
            int durationLength = duration.length();

            if (headerLength > maxHeaderLength){
                maxHeaderLength = headerLength;
            }

            if (durationLength > maxDurationLength){
                maxDurationLength = durationLength;
            }
        }

        if (maxHeaderLength > 25){
            headerCol.setPrefWidth(maxHeaderLength*7.6);
        }

        if (maxDurationLength > 25){
            durationCol.setPrefWidth(maxDurationLength*7.6);
        }
    };








    EventHandler shrinkOnMouseExit = event -> {
        TableView<Table.chartDataUnit> sourceTable = (TableView) event.getSource();
        TableColumn headerCol = sourceTable.getColumns().get(0);
        TableColumn durationCol = sourceTable.getColumns().get(1);

        headerCol.setPrefWidth(190.0);
        durationCol.setPrefWidth(190.0);
    };

}
