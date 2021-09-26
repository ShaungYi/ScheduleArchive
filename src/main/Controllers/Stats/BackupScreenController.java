package main.Controllers.Stats;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import main.App;
import main.Controllers.PrototypeController;
import main.Models.BackupArchiveModel;
import main.Models.DBModels.ArchiveDBModel;
import main.Models.DBModels.SettingsDBModel;
import main.Models.DBModels.WriteToDBModel;
import main.Models.DateTimeModel;
import main.Models.SceneNavigationModel;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

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
    Button applySettingsButton;

    @FXML
    Label maxNumBackupsInvalidAlertLabel;
    @FXML
    Label hoursInvalidAlertLabel;
    @FXML
    Label minutesInvalidAlertLabel;


    public void initialize() {
        availableBackupsListView.setItems(BackupArchiveModel.availableBackupsObservableList);
    }


    @FXML
    public void applySettings() {


        int maxBackupNum = Integer.parseInt(maxBackupNumField.getText());

        int hh = Integer.parseInt(backupEveryHourField.getText());
        int mm = Integer.parseInt(backupEveryMinuteField.getText());
        int backupCreationIntervalInSeconds = DateTimeModel.convertHHMMSSToSeconds(hh, mm, 0);


        //update data in ArchiveDBModel
        BackupArchiveModel.maxBackupNum = maxBackupNum;
        BackupArchiveModel.backupCreationIntervalInSeconds = backupCreationIntervalInSeconds;

        System.out.println("(from BackupScreenController.applySettings) max num backups: " + BackupArchiveModel.maxBackupNum);
        System.out.println("(from BackupScreenController.applySettings) create backup archive every " + BackupArchiveModel.backupCreationIntervalInSeconds + " seconds");

        WriteToDBModel.updateBackupSettings(SettingsDBModel.maxBackupNum, maxBackupNum);
        WriteToDBModel.updateBackupSettings(SettingsDBModel.backupCreationInterval, backupCreationIntervalInSeconds);

        System.out.println("(from apply settings) settings saved to DB");
        System.out.println(BackupArchiveModel.backupCreationIntervalInSeconds);

        App.backupRegularly.stop();
        App.backupRegularly = new Thread(BackupArchiveModel.createBackupArchiveDBRegularly);
        App.backupRegularly.start();


    }


    //when an item in availableBackupsListView is clicked
    @FXML
    public void setSelectedBackup() {

        String backupName = (String) availableBackupsListView.getSelectionModel().getSelectedItem();

        //disable load button if invalid backup selected
        if (backupName == null || backupName == "") {
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
    public void loadBackupArchive() throws SQLException {
        String formattedBackupName =
                BackupArchiveModel.formatBackupName(
                        BackupArchiveModel.parseBackupName(
                                selectedBackup,
                                BackupArchiveModel.parsedDateFormat
                        )
                );


        if (WriteToDBModel.dataBackupProcessAtRest) {
            //just create backup
            BackupArchiveModel.loadBackup(formattedBackupName);

        } else {
            //wait until data backup process finished then create backup to prevent DB corruption
            while (!WriteToDBModel.dataBackupProcessAtRest) {

            }

            BackupArchiveModel.loadBackup(formattedBackupName);

        }

        //restart app (wink)

        ArchiveDBModel.connection.close();
        ArchiveDBModel.connect();

        SceneNavigationModel.launchScreen = App.sceneNavigationModel.createNewScene("../resources/FXML/LaunchScreen/launchScreen.fxml");
        Stage primaryStage = (Stage) availableBackupsListView.getScene().getWindow();
        primaryStage.setScene(SceneNavigationModel.launchScreen);
    }

    //displays current settings stored in ArchiveDBModel in settings fields
    public void displaySettings() {
        maxBackupNumField.setText(String.valueOf(BackupArchiveModel.maxBackupNum));

        DateTimeModel.Time time = DateTimeModel.parseDuration(BackupArchiveModel.backupCreationIntervalInSeconds);
        backupEveryHourField.setText(String.valueOf(time.getHh()));
        backupEveryMinuteField.setText(String.valueOf(time.getMm()));

        System.out.println("(from BackupScreenController.displaySettings) max num backups: " + BackupArchiveModel.maxBackupNum);
        System.out.println("(from BackupScreenController.displaySettings) create backup archive every " + BackupArchiveModel.backupCreationIntervalInSeconds + "seconds");
    }


    //sets invalid label over the input field to visible and disable apply button if user input not a valid number, visible and undisable if not
    private void showInvalidInputAlertLabelAndDisableApplyButton(Label alertLabel, TextField inputField) {

        String input = inputField.getText();

        try {
            Integer.parseInt(input);
            alertLabel.setVisible(false);
            applySettingsButton.setDisable(false);

        } catch (NumberFormatException e) {
            alertLabel.setVisible(true);
            applySettingsButton.setDisable(true);
        }
    }

    @FXML
    public void maxBackupNumFieldTyped() {
        showInvalidInputAlertLabelAndDisableApplyButton(maxNumBackupsInvalidAlertLabel, maxBackupNumField);
    }

    @FXML
    public void minutesFieldTyped() {
        showInvalidInputAlertLabelAndDisableApplyButton(minutesInvalidAlertLabel, backupEveryMinuteField);
    }

    @FXML
    public void hoursFieldTyped() {
        showInvalidInputAlertLabelAndDisableApplyButton(hoursInvalidAlertLabel, backupEveryHourField);
    }


    @FXML
    public void goToScheduleCreator() {
        App.sceneNavigationModel.gotoScene(SceneNavigationModel.scheduleCreator, SceneNavigationModel.backups);
    }

    @FXML
    public void goToMain() {
        App.sceneNavigationModel.gotoScene(SceneNavigationModel.stats, SceneNavigationModel.backups);
    }

    @FXML
    public void createBackup(ActionEvent actionEvent) {
    }

    @FXML
    public void deleteBackup(ActionEvent actionEvent) {
    }

}
