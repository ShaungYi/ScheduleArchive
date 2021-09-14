package main.Models;

import javafx.collections.ObservableList;
import main.App;
import main.Utility.PastActivity;

import java.util.ArrayList;

public class SearchModel {

    public static ArrayList<String> browsPastActivities(String query){

        ArrayList<String> results = new ArrayList<>();

        for (PastActivity pastActivity : PastActivityArchiveModel.pastActivities){
            String name = pastActivity.getName();

            name = name.trim();

            if (name.toLowerCase().contains(query.toLowerCase().trim())
                    && !name.equals("undefined")
                    && !name.equals("new")
                    && !name.equals("no data")
                    && !results.contains(name)){
                results.add(name);
            }
        }

        return results;
    }

    public static void searchPastActivityListForNameAndLoadToObservableList(String targetFragment, ObservableList<String> list){

        ArrayList<String> result = browsPastActivities(targetFragment);

//        System.out.println(result);
        for (int i = result.size() - 1; i >= 0; i--){
            list.add(result.get(i));
        }

    }

    public static void searchPastActivityListForNameAndLoadToObservableListInReverse(String targetFragment, ObservableList<String> list){

        ArrayList<String> result = browsPastActivities(targetFragment);

//        System.out.println(result);
        for (String name : result){
            list.add(name);
        }

    }
}
