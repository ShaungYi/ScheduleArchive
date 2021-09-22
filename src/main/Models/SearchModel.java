package main.Models;

import javafx.collections.ObservableList;
import main.Models.DBModels.ReadFromDBModel;

import java.util.ArrayList;

public class SearchModel {

    public static void searchPastActivityListForNameAndLoadToObservableList(String targetFragment, ObservableList<String> list, String category) {
        ArrayList<String> suggestionList = ReadFromDBModel.getSuggestions(targetFragment, category);
        list.clear();

        for (String suggestion : suggestionList) {
            list.add(suggestion);
        }
    }


    public static void searchPastActivityListForNameAndLoadToObservableListInReverse(String targetFragment, ObservableList<String> list, String category) {
        ArrayList<String> suggestionList = ReadFromDBModel.getSuggestions(targetFragment, category);
        list.clear();

        for (int i = suggestionList.size() - 1; i >= 0; i--) {
            list.add(suggestionList.get(i));
        }
    }
}