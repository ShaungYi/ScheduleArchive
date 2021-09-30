package main.Controllers.Macro.Infographics;

import main.Controllers.Macro.Infographics.Barcomponent.Barcomponent;

import java.util.HashMap;

public class BarComponentManager {

    static HashMap<String, Barcomponent> barComps = new HashMap<>();

    public static void addBarComp(Barcomponent comp, String date){
        barComps.put(date, comp);
    }


    //iterate over all stored barcomps and update data
    public static  void updateAllBarCompData(){
        for (String date : barComps.keySet()){
            Barcomponent comp = barComps.get(date);
            comp.updateDayData(date);
        }
    }
}
