package main.Models.Graphics;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import main.Utility.Activity;
import main.resources.customNodes.enterPasswordPopup.EnterPasswordPopup;
import main.resources.customNodes.setPasswordPopup.SetPasswordPopup;

public class General {
    public static Image openLock;
    public static Image closedLock;


    public static void toggleLockImageView(ImageView lock, Activity associatedActivity){
        if (associatedActivity.noteIsPrivate()){
            associatedActivity.unlockNote();
            lock.setImage(General.openLock);
        } else {
            associatedActivity.lockNote();
            lock.setImage(General.closedLock);
        }
    }


    public static void reenableMainScreen(Pane grandmotherPane, Pane motherPane, TextArea noteTextArea, ImageView noteLock){

        removePopup(grandmotherPane);

        motherPane.setDisable(false);
        if (noteTextArea != null && noteLock != null){
            afterTimeLineReenable(noteTextArea, noteLock);
        }
    }

    public static void removePopup(Pane grandmotherPane){
        ObservableList<Node> children = grandmotherPane.getChildren();
        for (int i = 0; i < children.size(); i++){
            Node child = children.get(i);
            if (child instanceof EnterPasswordPopup || child instanceof SetPasswordPopup){
                grandmotherPane.getChildren().remove(child);
            }
        }
    }

    public static void afterTimeLineReenable(TextArea noteTextArea, ImageView noteLock){
        noteTextArea.setDisable(false);
        noteLock.setDisable(false);

    }

    public static void popupOnCenterOfScreen(Pane screen, Pane popup){

        screen.getChildren().add(popup);
        Pane popupMain = (Pane) popup.getChildren().get(0);
        popup.setLayoutX((screen.getWidth() - popupMain.getPrefWidth()) / 2);
        popup.setLayoutY((screen.getHeight() - popupMain.getPrefHeight()) / 2);
    }

}
