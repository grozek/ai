
import java.util.ArrayList;
//import objects.Coordinates;
//import storage.Bfs;
//import storage.FieldStorage;

/*
 * Path
 * Holds infomation about the curreent path or finds a new path
 */
public class Path {
    ArrayList<Coordinates> currentPath;

    /*
     * constructor
     */
    public Path() {
        this.currentPath = new ArrayList<>();
    }

    /*
     * isEmpty
     */
    public boolean isEmpty() {
        return this.currentPath.isEmpty();
    }

    public Coordinates getNextStep() {
        return this.currentPath.get(0);
    }

    private void removeNextStep() {
        currentPath.remove(0);
    }

    /*
     * getTarget
     * returns the goal of the path
     */
    private Coordinates getTarget() {
        if (this.currentPath.isEmpty()){
            return new Coordinates (50,50);
        }
        else{
            return currentPath.get((currentPath.size() - 1));
        }
    }

    /*
     * goesTo
     * tells if the path goes to a tile
     */
    private boolean goesTo(Coordinates coordinates) {
        if (this.isEmpty()) {
            return false;
        }

        return this.getTarget() == coordinates;
    }

    /*
     * Updtae
     * updates the decision of the path that is being pursued with the information of the 
     * objcts that have been found and collected
     */
    public void update(Coordinates agentCoordinates, Map map, Bfs bfs) {
        if (!this.isEmpty() && agentCoordinates.equals(this.getNextStep())) {
            this.removeNextStep();
        }

        if (map.money.collected && !this.goesTo(map.startCoordinates)) {
            boolean foundPathToStart = findPathTo(agentCoordinates, map.startCoordinates, map);
            if (foundPathToStart)
                return;
        }

        if (!map.money.collected && map.money.observed && !this.goesTo(map.money.location)) {
            boolean foundPathToMoney = findPathTo(agentCoordinates, map.money.location, map);
            if (foundPathToMoney)
                return;
        }

        if (!map.key.collected && map.key.observed && !this.goesTo(map.key.location)) {
            boolean foundPathToKey= findPathTo(agentCoordinates, map.key.location, map);
            if (foundPathToKey)
                return;
        }

        if (map.key.collected && map.key.doorObserve && !map.key.doorOpened && !this.goesTo(map.key.doorLocation)) {
            boolean foundPathToDoor= findPathTo(agentCoordinates, map.key.doorLocation, map);
            if (foundPathToDoor)
                return;
        }

        if (!map.axe.collected && map.axe.observed && !this.goesTo(map.axe.location)) {
            boolean foundPathToAxe = findPathTo(agentCoordinates, map.axe.location, map);
            if (foundPathToAxe)
                return;
        }

        if (!map.dynamite.collected && !map.dynamite.isEmpty() && !this.goesTo(map.dynamite.pollNextField())){
            boolean foundPathToDynamite = findNewPathFromStorage(agentCoordinates, map, map.dynamite);
            if (foundPathToDynamite){
                return;
            }
        }

        if (map.axe.collected && !map.forest.isEmpty() && !map.forest.contains(this.getTarget())) {
            System.out.println("looking path to tree");
            boolean foundPathToTree = findNewPathFromStorage(agentCoordinates, map, map.forest);
            if (foundPathToTree) {
                // foundPathToTree
                System.out.println(this.currentPath + " found path to tree");
                // this.currentPath
            }
        }

        if (this.isEmpty()) {
            if (!findNewPathFromStorage(agentCoordinates, map, bfs)) {
                Coordinates explosionGoal = tryExploding(map, agentCoordinates);
                if (!explosionGoal.equals(null)){
                    System.out.println("we explode");
                }
                else{
                    System.exit(1);
                }   
            }
        }

        System.out.println("findNewPath: " + currentPath);
    }
    

    /*
     * findPathFromStorage
     */
    private boolean findNewPathFromStorage(Coordinates agentCoordinates, Map map, FieldStorage storage) {
        while (!storage.isEmpty()) {
            Coordinates target = storage.pollNextField();
            if (target == null){
                return false;
            }
            boolean foundNewPath = findPathTo(agentCoordinates, target, map);
            if (foundNewPath)
                return true;
        }
        return false;
    }

    /*
     * findPathto
     */
    private boolean findPathTo(Coordinates start, Coordinates end, Map map) {
        ArrayList<Coordinates> endPath = new PathSearch(map, start, end).run();
        if (endPath.isEmpty()) {
            return false;
        }
        this.currentPath = endPath;
        return true;
    }

    /*
     * tryExploding
     */
    public Coordinates tryExploding(Map map, Coordinates agentCoordinates){
        //System.exit(-1);
        ArrayList<Coordinates> endPath = new ArrayList<>();
        System.out.println("TRYING EXPLODING");
        Coordinates firstWall = new Coordinates (0,0);
        //int manDistance = 99999;
        int option = 0;
        if (map.dynamite.collected == false){
           return null;
        }
        for(int j=0; j<4; j++){
            if (map.money.observed && !map.money.collected){
                option = 1;
                }
            if (map.axe.observed && !map.axe.collected){
                //new PathSearch(map, map.axe.location, agentCoordinates).explosionRun();
                endPath = new PathSearch(map, map.axe.location, agentCoordinates, true).run();
                option = 2;
            }

            int wallCount = 0;
            for (int i=0; i < endPath.size(); i++){
                char value = map.fullMap[endPath.get(i).first][endPath.get(i).second];
                if (value == '*'){
                    if (wallCount == 0){
                        firstWall = endPath.get(i);
                    }
                    wallCount++;
                }
            }
            if (wallCount <= map.dynamite.dynamiteList.size()){
                break;
            }
                System.out.println("TRIED EXPLODING");
                System.exit(1);
            }

        switch(option){
            case 1:
                return map.money.location;
            case 2: 
                return firstWall;
        }
        //System.out.println("TRIED EXPLODING");
        //System.exit(1);

        return null;
    }


    @Override
    public String toString() {
        return this.currentPath.toString();
    }

}
