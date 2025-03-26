import java.util.ArrayList;

import objects.AxeObj;
import objects.Coordinates;
import storage.DynamiteObj;
import objects.MoneyObj;
import objects.KeyObj;
import storage.Forest;
import utils.Utils;

/*
 * Map class
 * contains information about the location of the agent and eveyrhting else around
 */
public class Map {
    int SIZE = 100;
    char fullMap[][] = new char[SIZE][SIZE];
    public MoneyObj money;
    public AxeObj axe;
    public DynamiteObj dynamite;
    public KeyObj key;
    Forest forest;
    Coordinates startCoordinates;

    public Map() {
        this.fullMap = new char[SIZE][SIZE];
        this.startCoordinates = new Coordinates(SIZE / 2, SIZE / 2);
        this.money = new MoneyObj();
        this.axe = new AxeObj();
        this.dynamite = new DynamiteObj();
        this.forest = new Forest();
        this.key = new KeyObj();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                this.fullMap[i][j] = '#';
            }
        }
    }

    /*
     * get info of the cordinate c
     */
    public char get(Coordinates c) {
        return this.fullMap[c.first][c.second];
    }

    /*
     * put some value to some coordinate
     */
    private void put(Coordinates c, char value) {
        this.fullMap[c.first][c.second] = value;
    }

    /*
     * updateWithView
     * given the new view presented to the map, updates the new tiles according to the
     * location and movement by agent
     */
    public void updateWithView(char view[][], Coordinates agentLocation, int direction) {
        char[][] rotated = Utils.rotateView(view, direction);
        System.out.println("Rotated");
        IOHandler.print_view(rotated);

        if (money.observed && agentLocation.equals(money.location)) {
            money.collected = true;
            this.put(agentLocation, ' ');
        }

        if (axe.observed && agentLocation.equals(axe.location)) {
            axe.collected = true;
            this.put(agentLocation, ' ');
        }

        if (key.observed && agentLocation.equals(key.location)) {
            key.collected = true;
            this.put(agentLocation, ' ');
        }

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int coordX = agentLocation.first + i - 2;
                int coordY = agentLocation.second + j - 2;
                Coordinates coords = new Coordinates(coordX, coordY);
                updateSingleField(coords, rotated[i][j]);
            }
        }
    }

    /*
     * UpdateSingleFiled
     * Update of a specific filed
     */
    private void updateSingleField(Coordinates coords, char value) {
        if (value == ' ' && forest.contains(coords)) {
            this.put(coords, ' ');
            forest.chopTree(coords);
            System.out.println("Cut tree at " + coords);
        }
        if (value == ' ' && dynamite.contains(coords)){
            this.put(coords, ' ');
            dynamite.pickUp(coords);
            System.out.println("Pick up dynamite at " + coords);
        }
        if (value == ' ' && key.doorLocation.equals(coords)){
            this.put(coords, ' ');
            key.doorOpened = true;
        }

        if (this.get(coords) != '#')
            return;

        if (value == '$')
            this.money.observe(coords);
        if (value == 'a') {
            this.axe.observe(coords);
        }
        if (value == '-'){
            this.key.doorObserve(coords);
        }
        if (value == 'k'){
            this.key.observe(coords);
        }
        if ((value == 'd') && (!this.dynamite.contains(coords))){
            this.dynamite.addDynamite(coords);
        }
        if ((value == 'T') && (!this.forest.contains(coords))) {
            this.forest.addTree(coords);
            System.out.println(this.forest);
        }

        this.put(coords, value);
    }

    /*
     * print full map
     */
    public void printFullMap(Coordinates agentCoords) {
        MapEdges edges = new MapEdges(this.SIZE, this.fullMap);

        System.out.println(edges.top + "," + edges.bottom + ";;" + edges.left + "," + edges.right);

        for (int i = edges.top; i <= edges.bottom; i++) {
            for (int j = edges.left; j <= edges.right; j++) {
                if (i == agentCoords.first && j == agentCoords.second) {
                    System.out.print("^");
                } else {
                    System.out.print(fullMap[i][j]);
                }
            }
            System.out.print('\n');
        }
    }
}
