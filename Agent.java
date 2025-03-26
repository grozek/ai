import java.io.IOException;
import java.util.*;
import objects.Coordinates;
import storage.Bfs;

/* Agent Project
 * Gabriele Roznawska
 * This project's purpose is to design an agent that manages to win a game in maze game simulator
 * 
 * My approach:
 * The final solution relies on two separate search algorithms: Breadth First Search and PathSearch,
 *     BFS --> Is the last-choice decision of the agent, as it prioritizes collecting items and looking for them.
 *              It relies on the updateWithView() function that at every iteration (agents movement)feeds it all 
 *              necessary information about the newly discovered tiles. Then, adds them to queue as long as they are
 *              visitable without the need of any equipement. Is crucial to ensure we visit all accessible spots the map
 *    Path --> Performs Dijikstra's algorithm that is given the information about the goal coordinate and current 
 *   Search    coordinate of the agent. This way, once we know the place that the agent should be going next,
 *             we can ensure it does so with efficient timing and on the best path possible.
 *    
 * Map
 * In my implementation the crucial element is the map, that holds all information about the
 * current location of the agent, the discovered and undiscovered tiles, as well as the locations of 
 * the objects that are to be gathered. This way the path search knows where to look and what to 
 * expect to find. it works closely with both search algorithm to establish the global map and 
 * vist all tiles.
 * Map is updated at every iteration
 * After passing though all the map it should contain all the same information as the map printed in
 * in the terminal
 * 
 * Coordinates
 * The map works side by side with class of Coordinates that manage information about the x/y location 
 * of the tiles
 * 
 * Priority of the actions
 * In my process of selection of what matters for the agent to do first, i selected an order:
 * The actions are performed:
 *  -1--> Getting to the dollar
 *  -2--> Picking up free of cost items
 *  -3--> Performing no-cost door opening
 *  -4--> Cutting trees
 *  -5--> Using dynamite
 * 
 * 
 * Objects
 * I chose to store information about all the objects in inependent classes. This way I can easily
 * manage them and thier charactersitics as they change. Dynamite and Trees use array, while dollar
 * axe and door can just be singluar coordinates
 * 
 * More specific information is inlcuded in each java file for classes
 * 
 */

class Agent {

    //FIELDS
    int direction;
    Map map;
    Bfs bfs;
    Path path;
    int SIZE = 100;
    Coordinates agentCoordinates;

    // CONSTRUCTORS
    public Agent() {
        this.map = new Map();
        this.bfs = new Bfs(this.map.SIZE);
        this.path = new Path();
        this.direction = 0;
        this.agentCoordinates = new Coordinates(SIZE / 2, SIZE / 2);
    }

    /* searchCenter 
     * it is the main manager of our program - it sends its decisions directly to main
     * manages the update of view of bfs and the map and then also updates the path, which 
     * finds what is goin to be the next visited coordinate. Searchcenter processes that and
     * receives the move decision to follow the path. it updates agent's position and uses 
     * it in the next iteration
     */
    public char searchCenter(char view[][]) {
        char currentAction;

        this.bfs.setVisited(agentCoordinates);
        System.out.println(this.path.toString());

        System.out.println(this.direction);
        System.out.println(this.agentCoordinates);

        this.map.updateWithView(view, agentCoordinates, direction);
        this.bfs.updateWithView(view, agentCoordinates, direction);

        this.path.update(agentCoordinates, map, bfs);

        // PRINTING - COMMENTED OUT
        //this.map.printFullMap(this.agentCoordinates);
        //if (!this.path.isEmpty()) {
        //    System.out.println("nextOnPath: " + this.path.getNextStep() + "(" +
        //            map.get(this.path.getNextStep()) + ")");
        //}

        currentAction = getActionFromPath();
        System.out.println("getActionFromPath: " + currentAction);

        this.updateAgentPosition(currentAction);
        return currentAction;
    }

    /*
     * updateAgentPosition
     * based on the next move and direction of the agent, analyzes the agent's position after
     */
    public void updateAgentPosition(char nextMove) {
        switch (nextMove) {
            case 'R' -> {
                this.direction = (this.direction + 1) % 4;
            }
            case 'L' -> {
                this.direction = (this.direction + 3) % 4;
            }
            case 'F' -> {
                switch (this.direction) {
                    case 0 -> this.agentCoordinates.first--;

                    case 1 -> this.agentCoordinates.second++;

                    case 2 -> this.agentCoordinates.first++;

                    case 3 -> this.agentCoordinates.second--;
                }
            }
            default -> {
            }
        }
    }

    /*
     * getActionFromPath
     * Given information about the direction the agent is facing, relative direction and
     * the next step from the path, calculates the character move
     */
    private char getActionFromPath() {
        Coordinates nextStep = this.path.getNextStep();
        int directionToStep = agentCoordinates.neighbourToDirection(nextStep);

        int relativeDirection = (this.direction - directionToStep + 4) % 4;

        switch (relativeDirection) {
            case 0 -> {
                switch (map.get(nextStep)) {
                    case 'T' -> {
                        return 'C';
                    }
                    case '-' -> {
                        return 'U';
                    }
                    case '*' ->{
                        return 'B';
                    }
                    default -> {
                        return 'F';
                    }
                }
            }
            case 1 -> {
                return 'L';
            }
            default -> {
                return 'R';
            }
        }
    }

    /*
     * is obstacle
     * is the charcter on the tile non-easily-crossable
     * ?
     */
    public boolean is_obstacle(char x) {
        return Arrays.asList('*', '~', 'T', '-').contains(x);
    }

    /*
     * original get_action as provided
     */
    public char get_action(char view[][]) {
        int ch = 0;
        System.out.print("Enter Action(s): ");

        try {
            while (ch != -1) {
                ch = System.in.read();

                switch (ch) { // if character is a valid action, return it
                    case 'F':
                    case 'L':
                    case 'R':
                    case 'C':
                    case 'U':
                    case 'B':
                    case 'f':
                    case 'l':
                    case 'r':
                    case 'c':
                    case 'u':
                    case 'b':
                        return ((char) ch);
                }
            }
        } catch (IOException e) {
            System.out.println("IO error:" + e);
        }

        return 0;
    }

    /*
     * main
     * establishes the grounds for the search - has the ioHandler handle the input of map
     * and searchCenter managaes the decision of what moves to make.
     * IOHandler prints out the choices to the terminal. That continues for 10.001 moves - the
     * max acceptable by the game
     */
    public static void main(String[] args) throws IOException {
        char view[][];
        Agent agent = new Agent();
        char action;

        if (args.length < 2) {
            System.out.println("Usage: java Agent -p <port>\n");
            System.exit(-1);
        }

        int port = Integer.parseInt(args[1]);
        IOHandler ioHandler = new IOHandler(port);

        for (int i = 0; i < 10001; i++) {
            view = ioHandler.read();
            IOHandler.print_view(view);
            action = agent.searchCenter(view);
            if (action == 'X') {
                break;
            }
            ioHandler.printToTerminal(action);
            // COMMENTED OUT COMMENTS
            // System.out.print("END of step" + '\n');
            //System.out.print("__________________" + '\n');
        }
        System.exit(0);
    }
}
