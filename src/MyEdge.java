import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.*;

public class MyEdge implements Comparable<MyEdge> {
    private int source;
    private int destination;
    private int weight;

    public MyEdge(int source, int destination, int weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    public int getS(){
        return source;
    }
    public int getD(){
        return  destination;
    }

    public int getWeight(){
        return  weight;
    }

    //TODO
    //Implement the comparable interface for MyEdge class
    //This will provide interface for PrioritQueue to compare the instances of MyEdge class

    @Override
    public int compareTo(MyEdge e) {
        if (weight > e.getWeight()) {
            return 1;
        } else if (weight < e.getWeight()) {
            return -1;
        } else {
            return 0;
        }
    }
}
