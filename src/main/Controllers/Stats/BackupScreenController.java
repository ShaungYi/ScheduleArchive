package main.Controllers.Stats;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import main.App;
import main.Controllers.PrototypeController;
import main.Models.DBModels.BackupArchiveModel;
import main.Models.DateTimeModel;
import main.Models.SceneNavigationModel;

public class BackupScreenController extends PrototypeController {

    @FXML
    ListView availableBackupsListView;
    String selectedBackup;
    @FXML
    Button loadBackupButton;
    @FXML
    TextField maxBackupNumField;
    @FXML
    TextField backupEveryHourField;
    @FXML
    TextField backupEveryMinuteField;
    @FXML
    TextField backupEverySecondField;


    public void initialize(){
        availableBackupsListView.setItems(BackupArchiveModel.availableBackupsObservableList);
    }


    @FXML
    public void applySettings(){

        try {
            //get user inputs and convert to archiveDBModel form
            int maxBackupNum = Integer.parseInt(maxBackupNumField.getText());

            int hh = Integer.parseInt(backupEveryHourField.getText());
            int mm = Integer.parseInt(backupEveryMinuteField.getText());
            int ss = Integer.parseInt(backupEverySecondField.getText());
            int backupCreationIntervalInSeconds = DateTimeModel.convertHHMMSSToSeconds(hh, mm, ss);


            //update data in ArchiveDBModel
            BackupArchiveModel.maxBackupNum = maxBackupNum;
            BackupArchiveModel.backupCreationIntervalInSeconds = backupCreationIntervalInSeconds;

            System.out.println("(from BackupScreenController.applySettings) max num backups: " + BackupArchiveModel.maxBackupNum);
            System.out.println("(from BackupScreenController.applySettings) create backup archive every " + BackupArchiveModel.backupCreationIntervalInSeconds + " seconds");

        } catch (NumberFormatException e) {
            System.out.println("(from BackupScreenController.applySettings) invalid settings");
            //do nothing if cannot format any of the number string user inputs
        }

    }


    //when an item in availableBackupsListView is clicked
    @FXML
    public void setSelectedBackup(){

        String backupName = (String) availableBackupsListView.getSelectionModel().getSelectedItem();

        //disable load button if invalid backup selected
        if (backupName == null || backupName == ""){
            loadBackupButton.setDisable(true);
            System.out.println("(from BackupScreenController.setSelectedBackup) invalid selection");
        }
        // enable load button and update selected day
        else {
            loadBackupButton.setDisable(false);
            selectedBackup = backupName;
            System.out.println("(from BackupScreenController.setSelectedBackup) backup to load: " + selectedBackup);
        }
    }



    @FXML
    public void loadBackupArchive(){
        //to be implemented by joonius
    }

    //displays current settings stored in ArchiveDBModel in settings fields
    public void displaySettings(){
        maxBackupNumField.setText(String.valueOf(BackupArchiveModel.maxBackupNum));

        DateTimeModel.Time time = DateTimeModel.parseDuration(BackupArchiveModel.backupCreationIntervalInSeconds);
        backupEveryHourField.setText(String.valueOf(time.getHh()));
        backupEveryMinuteField.setText(String.valueOf(time.getMm()));
        backupEverySecondField.setText(String.valueOf(time.getSs()));

        System.out.println("(from BackupScreenController.displaySettings) max num backups: " + BackupArchiveModel.maxBackupNum);
        System.out.println("(from BackupScreenController.displaySettings) create backup archive every " + BackupArchiveModel.backupCreationIntervalInSeconds + "seconds");
    }





    @FXML
    public void goToScheduleCreator() {
        App.sceneNavigationModel.gotoScene(SceneNavigationModel.scheduleCreator, SceneNavigationModel.backups);
    }

    @FXML
    public void goToMain() {
        App.sceneNavigationModel.gotoScene(SceneNavigationModel.stats, SceneNavigationModel.backups);
    }
}
