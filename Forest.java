//package storage;

import java.util.ArrayList;
import java.util.List;
//import objects.Coordinates;

/*
 * Forest
 * Manages the storage of information about trees
 */
public class Forest implements FieldStorage {
    List<Coordinates> trees = new ArrayList<>();
    Boolean raft;

    /*
     * We store trees in an array
     */
    public Forest() {
        this.trees = new ArrayList<>();
        this.raft = false;
    }

    /*
     * isEmpty
     * if the list is empty then there is no more treess we see
     */
    @Override
    public boolean isEmpty() {
        return this.trees.isEmpty();
    }

    /*
     * addTree
     * adding tree to the array holding infomration from the global map 
     */
    public void addTree(Coordinates treeCords) {
        trees.add(treeCords);
    }

    /*
     * chopTree
     * chops the tree to make raft
     */
    public void chopTree(Coordinates treeCords) {
        raft = true;
        trees.remove(treeCords);
    }

    /*
     * pollNextField()
     * returns the top of the list of trees
     */
    @Override
    public Coordinates pollNextField() {
        return trees.get(0);
    }

    /*
     * contains
     * Informs whether the list contains centrain tree
     */
    public Boolean contains(Coordinates treeCords) {
        return (trees.contains(treeCords));
    }

    @Override
    public String toString() {
        return "Forest(" + this.trees.toString() + ")";
    }
}