package main.Models;

import javafx.collections.ObservableList;
import main.App;
import main.Models.DBModels.ReadFromDBModel;
import main.Utility.PastActivity;

import java.util.ArrayList;

public class SearchModel {

    public static void searchPastActivityListForNameAndLoadToObservableList(String targetFragment, ObservableList<String> list){

        ArrayList<String> result = ReadFromDBModel.getNameSuggestions(targetFragment);

        for (int i = result.size() - 1; i >= 0; i--){
            list.add(result.get(i));
        }

    }

    public static void searchPastActivityListForNameAndLoadToObservableListInReverse(String targetFragment, ObservableList<String> list){

        ArrayList<String> result = ReadFromDBModel.getNameSuggestions(targetFragment);

//        System.out.println(result);
        for (String name : result){
            list.add(name);
        }

    }
}
