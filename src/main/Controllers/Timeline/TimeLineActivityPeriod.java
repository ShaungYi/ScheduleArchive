package main.Controllers.Timeline;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import main.Models.DBModels.ReadFromDBModel;
import main.Models.Graphics.General;
import main.Utility.Activity;
import main.resources.customNodes.enterPasswordPopup.EnterPasswordPopup;
import main.resources.customNodes.setPasswordPopup.SetPasswordPopup;


public class TimeLineActivityPeriod extends Pane {

    Label activityLabel;

    ImageView noteTag;
    private ImageView noteLock;
    public static Image noteImage;
    public static Image noteHoveredImage;
    public boolean noteTagIsPrompt;


    TextArea noteTextArea;

    ObservableList<Node> children = this.getChildren();
    Activity thisActivity;

    public static TimeLineActivityPeriod previousSelectedPeriod;


    public EventHandler<MouseEvent> toDoOnNoteTagClicked = (e) -> {

    };



    public EventHandler<MouseEvent> toDoOnNoteLockClicked = (MouseEvent event) -> {
        General.toggleLockImageView(noteLock, thisActivity);



        if (ReadFromDBModel.getPassword().isBlank() && thisActivity.noteIsPrivate()){
            /**pop up set password form**/

            Pane grandmotherPane = getGrandMotherPane();
            VBox motherPane = getMotherPane();

            disableMainScreen(motherPane);

            //pop up time
            SetPasswordPopup setPasswordPopup = new SetPasswordPopup(grandmotherPane, motherPane, noteTextArea, noteLock);
            General.popupOnCenterOfScreen(grandmotherPane, setPasswordPopup);

        }
    };




    private Runnable popUpNoteRunnable = () -> {
        noteTextArea.setText(thisActivity.getNote());

        //settle note area in the right position
        Bounds noteTagBounds = noteTag.localToScene(noteTag.getBoundsInLocal());
        double noteTextAreaWidth = noteTextArea.getWidth();
        double noteTextAreaLayoutX = noteTagBounds.getCenterX() - noteTextAreaWidth / 2;
        noteTextArea.setLayoutX(noteTextAreaLayoutX);

        noteTextArea.setVisible(true);

        //settle not lock in right position
        noteLock.setLayoutX(noteTextAreaLayoutX + noteTextAreaWidth - 40);

        //update note lock status
        if (thisActivity.noteIsPrivate()){
            noteLock.setImage(General.closedLock);
        } else {
            noteLock.setImage(General.openLock);
        }

        //update note lock event handler
        noteLock.setOnMouseClicked(toDoOnNoteLockClicked);

        noteLock.setVisible(true);
    };




    private EventHandler<MouseEvent> onNoteTagClicked = (MouseEvent event) -> {

        toDoOnNoteTagClicked.handle(event);


        if (thisActivity.noteIsPrivate()){

            Pane grandmotherPane = getGrandMotherPane();
            VBox motherPane = getMotherPane();
            disableMainScreen(motherPane);

            EnterPasswordPopup enterPasswordPopup = new EnterPasswordPopup(grandmotherPane, motherPane, noteTextArea, noteLock, popUpNoteRunnable);
            General.popupOnCenterOfScreen(grandmotherPane, enterPasswordPopup);
        } else {
            popUpNoteRunnable.run();
        }
    };



    private EventHandler<MouseEvent> onMouseEnteredNoteTag = (MouseEvent event) -> {
        //change note tag image to hovered
        noteTag.setImage(noteHoveredImage);
        noteTag.setFitWidth(70);
        noteTag.setFitHeight(87.5);
        noteTag.setLayoutY(-95);

        //set opacity to 1
        noteTag.setOpacity(1);
    };



    private EventHandler<MouseEvent> onMouseExitedNoteTag = (MouseEvent event) -> {
        //change note tag image to normal
        noteTag.setImage(noteImage);
        noteTag.setFitWidth(60);
        noteTag.setFitHeight(75);
        noteTag.setLayoutY(-80);

        //set opacity to 0.4 if note tag is a prompt
        if (noteTagIsPrompt){
            noteTag.setOpacity(0.4);
        }
    };

    private EventHandler<MouseEvent> onPeriodClicked = (MouseEvent event) -> {

        System.out.println("period clicked");
        if (!children.contains(noteTag)){
            addNoteTag();
            noteTagIsPrompt = true;
            noteTag.setOpacity(0.4);
            System.out.println("adding semitransparent prompt note tag");
        }

    };



    public TimeLineActivityPeriod(Activity act, TextArea ntTextArea, ImageView ntLock, boolean editable){

        noteTextArea = ntTextArea;
        thisActivity = act;

        noteLock = ntLock;
        ntLock.setImage(General.openLock);

        //initialize activity label
        activityLabel = new Label();
        children.add(activityLabel);

        //initialize note tag
        noteTag = new ImageView();
        noteTag.setImage(noteImage);
        noteTag.setFitWidth(60);
        noteTag.setFitHeight(75);
        noteTag.setLayoutY(-80);


        //add note to period if exists in activity
        String note = thisActivity.getNote();
        if (!note.isBlank()){
            addNoteTag();
        }

        //add not appearance event handler to note tag
        noteTag.setOnMouseClicked(onNoteTagClicked);


        //add edit functions event handler to this if editable
        if (editable){
            this.setOnMouseClicked(onPeriodClicked);
        }

        //add hover response handlers to note tag
        noteTag.setOnMouseEntered(onMouseEnteredNoteTag);
        noteTag.setOnMouseExited(onMouseExitedNoteTag);

    }



    public void addNoteTag(){
        if (!children.contains(noteTag)){
            children.add(noteTag);
        }
    }

    public void removeNoteTag(){
        if (children.contains(noteTag)){
            children.remove(noteTag);
        }
    }


    public Label getActivityLabel() {
        return activityLabel;
    }


    private Pane getGrandMotherPane(){
        return (Pane) noteTextArea.getParent();
    }

    private VBox getMotherPane(){
        return  (VBox) getGrandMotherPane().getChildren().get(0);
    }

    private void disableMainScreen(VBox motherPane){
        motherPane.setDisable(true);
        noteTextArea.setDisable(true);
        noteLock.setDisable(true);
        noteLock.setOpacity(1);
    }


}
