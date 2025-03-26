//package storage;

import java.util.ArrayList;
import java.util.List;
//import objects.Coordinates;

/*
 * DynamiteObj
 * The class managing existance and explosions of dynamite
 */
public class DynamiteObj implements FieldStorage {
    public List<Coordinates> dynamiteList = new ArrayList<>();
    public Boolean collected;


    /*
     * Dynamite Coordinates are stored in a list. Only one can be picked up at a time
     */
    public DynamiteObj() {
        this.dynamiteList = new ArrayList<>();
        this.collected = false;
    }

    @Override
    public boolean isEmpty() {
        return this.dynamiteList.isEmpty();
    }

    /*
     * addDynamite
     * adding it to the list of known coordinates
     */
    public void addDynamite(Coordinates coords) {
        dynamiteList.add(coords);
    }

    /*
     * pickUp
     * Picks up the dynamite at current loaction
     */
    public void pickUp(Coordinates coords){
        collected = true;
        dynamiteList.remove(coords);
    }

    /*
    * blowUp
    * uses dynamite
    */
    public void blowUp(Coordinates coords) {
        // dynamiteList.remove(coords);
        collected = false;
    }

    @Override
    public Coordinates pollNextField() {
        return dynamiteList.get(0);
    }

    public Boolean contains(Coordinates coords) {
        return (dynamiteList.contains(coords));
    }

    @Override
    public String toString() {
        return "Forest(" + this.dynamiteList.toString() + ")";
    }
}