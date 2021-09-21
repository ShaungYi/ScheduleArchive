package main.Models;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import main.Models.DBModels.ReadFromDBModel;

import java.util.ArrayList;

public class SearchModel {

    static Thread loadToObservableListThread = new Thread();

    public static void searchPastActivityListForNameAndLoadToObservableList(String targetFragment, ObservableList<String> list){

        ArrayList<String> result = ReadFromDBModel.getNameSuggestions(targetFragment);

        Platform.runLater( () -> {
            for (int i = result.size() - 1; i >= 0; i--){
                list.add(result.get(i));
            }
        });


    }

    public static void searchPastActivityListForNameAndLoadToObservableListInReverse(String targetFragment, ObservableList<String> list){

        ArrayList<String> result = ReadFromDBModel.getNameSuggestions(targetFragment);

        Platform.runLater( () -> {
            for (String name : result){
                list.add(name);
            }
        });

    }

    public static void loadToObservableListSynchronously(String targetFragment, ObservableList<String> list, boolean inReverse){

        //stop previous loading process
        loadToObservableListThread.stop();

        //clear list for another load
        list.clear();

        //construct loader runnable that loads in reverse or not depending on inReverse
        Runnable loadToObservableListRunnable = inReverse
                ?
                () -> searchPastActivityListForNameAndLoadToObservableListInReverse(targetFragment, list)
                :
                () -> searchPastActivityListForNameAndLoadToObservableList(targetFragment, list);


        //reset loader thread with new loader runnable
        loadToObservableListThread = new Thread(loadToObservableListRunnable);

        //start new loading process
        loadToObservableListThread.start();
    }
}
