package objects;

/*
 * Coordinates
 * Save crucial information about the whole program - the coordinates
 * Have two fields: first - the first coordinate x, and second - the coordinate y
 */
public class Coordinates {
    public int first = 0;
    public int second = 0;

    /*
     * Classic constructor
     */
    public Coordinates(int first, int second) {
        this.first = first;
        this.second = second;
    }

    /*
     * getNeighbourCoordinates
     * Calculates the location of neighbouring cooridnates and returns their coordinates
     * in an array form
     */
    public Coordinates[] getNeighbourCoordinates() {
        Coordinates[] neighbours = new Coordinates[4];

        neighbours[0] = new Coordinates(this.first - 1, this.second);
        neighbours[1] = new Coordinates(this.first, this.second - 1);
        neighbours[2] = new Coordinates(this.first + 1, this.second);
        neighbours[3] = new Coordinates(this.first, this.second + 1);
        return neighbours;
    }

    /*
     * areNeighbours
     * Verifies if two cords are neighbours
     */
    public boolean areNeighbours(Coordinates c) {
        for (Coordinates neighbour : this.getNeighbourCoordinates()) {
            if (neighbour.equals(c)) {
                return true;
            }
        }
        return false;
    }


    /*
     * neighbourToDirection
     * Calculates the relation of one c to its neighbours
     */
    public int neighbourToDirection(Coordinates c) {
        assert this.areNeighbours(c);
        if (this.first - 1 == c.first) {
            return 0;
        }
        if (this.first + 1 == c.first) {
            return 2;
        }
        if (this.second - 1 == c.second) {
            return 3;
        }
        return 1;
    }

    // equals override
    // so that we can establish equivalence between different coordinates
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true; // Same memory reference
        if (obj == null || getClass() != obj.getClass())
            return false;
        Coordinates that = (Coordinates) obj;
        return this.first == that.first && this.second == that.second;
    }

    @Override
    public String toString() {
        return "(" + this.first + ", " + this.second + ")";
    }
}
