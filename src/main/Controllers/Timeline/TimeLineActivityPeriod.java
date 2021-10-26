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
import main.Utility.Activity;


public class TimeLineActivityPeriod extends Pane {
    Label activityLabel;

    ImageView noteTag;
    public static Image noteImage;
    public static Image noteHoveredImage;
    public boolean noteTagIsPrompt;

    TextArea noteTextArea;

    ObservableList<Node> children = this.getChildren();
    Activity thisActivity;

    public static TimeLineActivityPeriod previousSelectedPeriod;



    public EventHandler<MouseEvent> toDoOnNoteTagClicked = (MouseEvent event) -> {};

    private EventHandler<MouseEvent> onNoteTagClicked = (MouseEvent event) -> {

        noteTextArea.setText(thisActivity.getNote());

        //settle note area in the right position
        Bounds noteTagBounds = noteTag.localToScene(noteTag.getBoundsInLocal());
        double noteTextAreaLayoutX = noteTagBounds.getCenterX() - noteTextArea.getWidth() / 2;
        noteTextArea.setLayoutX(noteTextAreaLayoutX);

        noteTextArea.setVisible(true);

        //execute to do event handler
        toDoOnNoteTagClicked.handle(event);

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



    public TimeLineActivityPeriod(Activity act, TextArea ntTextArea, boolean editable){

        noteTextArea = ntTextArea;

        thisActivity = act;

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


    public ImageView getNoteTag() {
        return noteTag;
    }
}
