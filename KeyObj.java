package objects;

/*
 * KeyObj
 * Stores everything about key object
 */
public class KeyObj extends CommonObj {
    public Coordinates doorLocation = new Coordinates(0,0);
    public Boolean doorObserve;
    public Boolean doorOpened;

    /*
     * Other than regular features includes data about the door
     */
    public KeyObj() {
        this.doorLocation = new Coordinates (0,0);
        this.doorObserve = false;
        this.doorOpened = false;
    } 

    /*
     * doorObserve 
     * Manages data collection for door
     */
    public void doorObserve(Coordinates coordinates) {
        this.doorObserve = true;
        this.doorLocation = coordinates;
        this.doorOpened = false;
    }
}
