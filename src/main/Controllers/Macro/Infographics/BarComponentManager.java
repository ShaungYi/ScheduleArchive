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
//            System.out.println(comp.getDate() );
            comp.updateDayData(comp.getDate());
        }
    }

    public void resetUpdate(){
        index = 0;
    }

    public static Barcomponent updateBar(String date, double barFreq){
        Barcomponent comp = barComps.get(index);
        comp.controller.configure(date, barFreq);
        index++;
        return comp;
    }
}
