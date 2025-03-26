//package objects;

/*
 * CommonObj
 * Extends its properties to objects that can be collected on a map to avoid repetition in
 * the separate objec classes
 */
public abstract class CommonObj {
    public Boolean observed;
    public Boolean collected;
    public Coordinates location;

    /*
     * Each object can be observed - when seen on full map
     * Collected - when currently held, not used (if it can get used)
     * Location - the coordinates of the item
     */
    public CommonObj() {
        this.observed = false;
        this.collected = false;
        this.location = new Coordinates(-1, -1);
    }
    
    /*
     * observe
     * Get info about item
     */
    public void observe(Coordinates coordinates) {
        this.observed = true;
        this.location = coordinates;
    }
}
