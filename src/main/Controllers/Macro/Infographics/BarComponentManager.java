package main.Controllers.Macro.Infographics;

import main.Controllers.Macro.Infographics.Barcomponent.Barcomponent;

import java.util.ArrayList;
import java.util.HashMap;

public class BarComponentManager {

    private static int index = 0;

    static ArrayList<Barcomponent> barComps = new ArrayList<>();

    public static void addBarComp(Barcomponent comp){
        barComps.add(comp);
    }


    //iterate over all stored barcomps and update data
    public static  void updateAllBarCompData(){
        for (Barcomponent comp : barComps){
            comp.updateDayData(comp.getDate());
        }
    }

    public static Barcomponent updateBar(String date, double barFreq){
        Barcomponent comp = null;

        try {
            comp = barComps.get(index);
            comp.controller.configure(date, barFreq);
            index++;
        } catch (IndexOutOfBoundsException ignored) {}

        return comp;
    }
}
