//package storage;

//import objects.Coordinates;

// Defines a common interface for collections of Coordinates
// This can be applicapple to any non-unique object
// such as trees or dynamites, 

public interface FieldStorage {
    public boolean isEmpty();

    public Coordinates pollNextField();
}
