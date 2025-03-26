package storage;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import objects.Coordinates;
import utils.Utils;

/*
 * Bfs
 * Performs the breadh first search in the map to direct the agent to next move, if no
 * better move was found by patchsearch
 */
public class Bfs implements FieldStorage {
    Queue<Coordinates> nodesToVisit;
    boolean[][] visited;

    /*
     * Stores information about visited nodes in representative visited 2d array
     * And holds information abotu the upcoming nodes to visit
     */
    public Bfs(int size) {
        this.visited = new boolean[size][size];
        this.nodesToVisit = new ArrayDeque<>();
    }

    /*
     * updateWithView
     * Given the new view, creates new interpretation of the area around. As long as the nodes
     * have not been visited before and can be visited, it adds them to the list of nodes to be
     * visited. It always rotates the map before making any additions
     */
    public void updateWithView(char view[][], Coordinates agentLocation, int direction) {
        char[][] rotated = Utils.rotateView(view, direction);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int coordX = agentLocation.first + i - 2;
                int coordY = agentLocation.second + j - 2;
                Coordinates newField = new Coordinates(coordX, coordY);
                if (!isVisitable(rotated[i][j]) || isVisited(newField)) {
                    continue;
                }
                this.nodesToVisit.add(newField);
            }
        }
    }

    /*
     * isVisitable
     * Returns true only if the goal is no-cost tile
     */
    private boolean isVisitable(char field) {
        return Arrays.asList(' ', '$').contains(field);
    }

    /*
     * isVisited
     * Returns true only if the cord c has already been vistied
     */
    private boolean isVisited(Coordinates c) {
        return this.visited[c.first][c.second];
    }

    /*
     * setVisited
     * Mark coordinate as visited
     */
    public void setVisited(Coordinates c) {
        this.visited[c.first][c.second] = true;
    }

    /*
     * pollNextField()
     * return the next tile in order of being visited, as long as there is such tile
     * and its not yet visited
     */
    @Override
    public Coordinates pollNextField() {
        Coordinates coordinates = this.nodesToVisit.poll();
        while (isVisited(coordinates)) {
            if (this.nodesToVisit.poll() == null){
                break;
            }
            coordinates = this.nodesToVisit.poll();
        }
        return coordinates;
    }

    /*
     * addNewField
     * adds new spot to the list of nodes to visit
     */
    public void addNewField(Coordinates newCord) {
        this.nodesToVisit.add(newCord);
    }

    /*
     * isEmpty
     * Is our list empty
     */
    @Override
    public boolean isEmpty() {
        return nodesToVisit.isEmpty();
    }
}
