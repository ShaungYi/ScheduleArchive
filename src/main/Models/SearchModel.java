package main.Models;
import javafx.collections.ObservableList;
import main.Models.DBModels.ReadFromDBModel;

import java.util.ArrayList;

public class SearchModel {

    public static void searchPastActivityListForNameAndLoadToObservableList(String targetFragment, ObservableList<String> list, String category) {
        list.clear();
        ArrayList<String> suggestionList = ReadFromDBModel.getSuggestions(targetFragment, category);

        for (String suggestion : suggestionList) {

            if (!list.contains(suggestion)) {
                list.add(suggestion);
            }
        }
    }


    public static void searchPastActivityListForNameAndLoadToObservableListInReverse(String targetFragment, ObservableList<String> list, String category) {
        list.clear();
        ArrayList<String> suggestionList = ReadFromDBModel.getSuggestions(targetFragment, category);

        for (int i = suggestionList.size() - 1; i >= 0; i--) {
            String suggestion = suggestionList.get(i);

            if (!list.contains(suggestion)) {
                list.add(suggestion);
            }
        }
    }
}