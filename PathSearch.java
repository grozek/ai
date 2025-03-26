import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Queue;
//import objects.Coordinates;


/*
 * PathSearch
 * Performs the algorithm that looks for the best route to get to coordinates of choice
 */
public class PathSearch {

    // FIELDS
    Map map;
    int[][] distances;
    boolean[][] visited;
    Coordinates agentCord;
    Coordinates goalCord;
    public Boolean explosionRun;


    /*
     * PatchSearh constructor
     * Sets up variables and the 2d table of distances and visited coordinates that
     * are critical in the process of adding/visiting neighbours
     */
    public PathSearch(Map map, Coordinates agentCord, Coordinates goalCord) {
        this.explosionRun = false;
        this.map = map;
        this.agentCord = agentCord;
        this.goalCord = goalCord;
        this.distances = new int[map.SIZE][map.SIZE];
        this.visited = new boolean[map.SIZE][map.SIZE];
        for (int i = 0; i < map.SIZE; i++) {
            for (int j = 0; j < map.SIZE; j++) {
                this.distances[i][j] = Integer.MAX_VALUE;
            }
        }
    }

    /*
     * PathSearch constructor
     * Mostly same as the one above, but used only in case of having explosion run. 
     * Temporaily changes the explosionRun to true
     */
    public PathSearch(Map map, Coordinates agentCord, Coordinates goalCord, Boolean explosionRun) {
        this.explosionRun = true;
        this.map = map;
        this.agentCord = agentCord;
        this.goalCord = goalCord;
        this.distances = new int[map.SIZE][map.SIZE];
        this.visited = new boolean[map.SIZE][map.SIZE];
        for (int i = 0; i < map.SIZE; i++) {
            for (int j = 0; j < map.SIZE; j++) {
                this.distances[i][j] = Integer.MAX_VALUE;
            }
        }
    }

    /*
     * isVisited
     * Informs if coordinate c is visited or not 
     */
    private boolean isVisited(Coordinates c) {
        return this.visited[c.first][c.second];
    }

    /*
     * setVisited
     * Mark this coordinate c as visited
     */
    private void setVisited(Coordinates c) {
        this.visited[c.first][c.second] = true;
    }   

    /*
     * getDistance
     * Return the field representing the distance of specific coordinate c
     */
    private int getDistance(Coordinates c) {
        return this.distances[c.first][c.second];
    }

    /*
     * setDistance
     * Set distance between for a given coordinate
     */
    private void setDistance(Coordinates c, int distance) {
        this.distances[c.first][c.second] = distance;
    }

    /*
     * isReachable
     * Return boolean only if agent can access filed at no cost 
     */
    private boolean isReachable(Coordinates c) {
        return Arrays.asList(' ', '$').contains(map.get(c));
    }

    /*
     * isReachableExplosion
     * Case of isReachable used only when we try to explode - thus wall is pasable
     */
    public boolean isReachableExplosion(Coordinates c){
        return Arrays.asList(' ', '$', '*').contains(map.get(c));
    }

    /*
     * getUnvisitedNeighbours
     * Returns the list of all non-visited neighbours of given coordinate c
     */
    private ArrayList<Coordinates> getUnvisitedNeighbours(Coordinates c) {
        ArrayList<Coordinates> neighbours = new ArrayList<>();
        Coordinates[] potentialNeighbours = c.getNeighbourCoordinates();

        for (Coordinates neighbour : potentialNeighbours) {
                if (isReachable(neighbour) && !isVisited(neighbour)) {
                    neighbours.add(neighbour);
                }
                if (!isVisited(neighbour) && neighbour.equals(this.goalCord)) {
                   neighbours.add(neighbour);
                }
        }
        return neighbours;
    }

    /* run()
    * Calls the actual run(). Used for debugging
    */
    public ArrayList<Coordinates> run() {
        return this.run(false);
    }


    /* run()
    * Manages the Path Search
    * Iterates through neighbours of the current location of the agent and chooses to visit
    * those that are the closest to the goal. If by the end of the search no path is found 
    * then returns an empty array as a sign of failure. Otherwise return the path.
    */
    public ArrayList<Coordinates> run(boolean debug) {
        Queue<Coordinates> nodesToVisit = new ArrayDeque<>();

        nodesToVisit.add(agentCord);
        setVisited(agentCord);
        setDistance(agentCord, 0);

        if (debug) {
            System.out.println("Goal " + goalCord);
            System.out.println("Agent " + agentCord);
        }

        boolean success = false;

        while (!nodesToVisit.isEmpty()) {
            Coordinates currentCoord = nodesToVisit.poll();
            int currentDistance = getDistance(currentCoord);

            if (debug) {
                System.out.println("Curretn: " + currentCoord);
            }

            if (currentCoord.equals(goalCord)) {
                success = true;
                break;
            }

            ArrayList<Coordinates> neighbours = getUnvisitedNeighbours(currentCoord);

            for (Coordinates neighbour : neighbours) {
                nodesToVisit.add(neighbour);
                setVisited(neighbour);
                setDistance(neighbour, currentDistance + 1);
            }
        }

        System.out.println("Success: " + success);
        System.out.println(goalCord);

        if (!success && explosionRun == false) {
            return new ArrayList<>();
        }
        if (explosionRun == true){
            explosionRun = false;
        }

        ArrayList<Coordinates> path = reconstructPath(goalCord);
        return path;
    }

    /* Reconstruct path
     * Having received a path, performs the reverse path construction, so that the returned array
     * begins at the location of agent and the agent can begin to move
     */
    private ArrayList<Coordinates> reconstructPath(Coordinates goalCord) {
        ArrayList<Coordinates> path = new ArrayList<>();
        Coordinates currentField = goalCord;
        while (!currentField.equals(agentCord)) {
            path.add(currentField);
            Coordinates[] neighbours = currentField.getNeighbourCoordinates();

            Coordinates neighbourClosestToAgent = neighbours[0];
            int minDistanceToAgent = Integer.MAX_VALUE;

            for (Coordinates neighbour : neighbours) {
                System.out.println("neighbour:" + neighbour);
                if (getDistance(neighbour) < minDistanceToAgent) {
                    neighbourClosestToAgent = neighbour;
                    minDistanceToAgent = getDistance(neighbour);
                }
            }
            currentField = neighbourClosestToAgent;
        }
        Collections.reverse(path);
        return path;
    }
}
