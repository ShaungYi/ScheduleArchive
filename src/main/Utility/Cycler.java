package main.Utility;

import java.util.ArrayList;

public class Cycler {

    ArrayList<String> items = new ArrayList<String>();
    int currentIndex = 0;


    public void addNewItem(String item){
        items.add(item);
    }

    public String nextItem(){

        if (!items.isEmpty()){
            String item = items.get(currentIndex);
            setNextIndex();

            return item;
        }
        else {
            return null;
        }


    }

    public void clear(){
        items.clear();
        currentIndex = 0;
    }


    private void setNextIndex(){

        if (currentIndex == items.size() - 1){
            currentIndex = 0;
        }
        else {
            currentIndex ++;
        }
    }


    public ArrayList<String> getItems() {
        return items;
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
    }
}
