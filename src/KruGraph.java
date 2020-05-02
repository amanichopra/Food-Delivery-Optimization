import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

public class KruGraph {
    private Vertex[] vertexArr;
    private ArrayList<MyEdge> edgeArr;
    private int vertexCount;
    private int edgeCount;

    //Implement the constructor for KruGraph
    //The format of the input file is the same as the format of the input file in Dijkstra
    public KruGraph(String graph_file)throws IOException{
        //TODO
        File file = new File(graph_file);
        Scanner sc = new Scanner(file);
        vertexCount = sc.nextInt();
        edgeCount = sc.nextInt();
        vertexArr = new Vertex[vertexCount + 1];
        edgeArr = new ArrayList<>();

        for (int i = 0; i < vertexCount + 1; i++) {
            vertexArr[i] = new Vertex(i);
        }

        for (int i = 0; i < edgeCount; i++) {
            int begin = sc.nextInt();
            int end = sc.nextInt();
            int weight  = sc.nextInt();
            addEgde(begin, end, weight);
        }


    }

    //Could be a helper function
    private void addEgde(int from, int to, int weight){
        //TODO
        edgeArr.add(new MyEdge(from, to, weight));

    }


    //Implement Kruskal with weighted union find algorithm
    public PriorityQueue<MyEdge> kruskalMST(){
        //TODO
        PriorityQueue<MyEdge> MST = new PriorityQueue<>(); // to keep track of MST
        PriorityQueue<MyEdge> edgeWeights = new PriorityQueue<>(); // to keep track of min weights

        for (MyEdge e : edgeArr) {
            edgeWeights.add(e);
        }

        while (MST.size() < vertexCount - 1) {
            MyEdge e = edgeWeights.remove();

            Vertex source = vertexArr[e.getS()];
            Vertex dest = vertexArr[e.getD()];

            if (find(source).getVertexNumber() != find(dest).getVertexNumber()) {
                MST.add(e);
                union(source, dest);
            }
        }
        return MST;
    }

    //Implement the recursion trick for the leaves to update the parent efficiently
    //Set it as static as always
    public static Vertex find(Vertex x){
        //TODO
        Vertex parent = x.getParent();

        if (x.getVertexNumber() == parent.getVertexNumber()) {
            return x;
        }
        x.updateParent(find(parent));

        return x.getParent();

    }


    //This function should union two vertices when an edge is added to the MST
    //Return true when the edge can be picked in the MST
    //Otherwise return false
    //Set it as static as always
    public static boolean union(Vertex x, Vertex y){
        //TODO
        Vertex p1 = find(x); // parent of x
        Vertex p2 = find(y); // parent of y

        if (p1.getVertexNumber() == p2.getVertexNumber()) { // if both verticies have same parent
            return false;
        }

        if (p1.getSize() <= p2.getSize()) {
            p1.updateParent(p2);
            p2.updateSize(p1.getSize() + p2.getSize());
        } else {
            p2.updateParent(p1);
            p1.updateSize(p1.getSize() + p2.getSize());
        }

        return true;
    }

    //This is what we expect for the output format
    //The test cases will follow this format
    public static void printGraph(PriorityQueue<MyEdge> edgeList){
        int turn = edgeList.size();
        for (int i = 0; i < turn; i++) {
            MyEdge edge = edgeList.poll();
            int source = edge.getS();
            int dest = edge.getD();
            if(source > dest){
                int temp = source;
                source = dest;
                dest = temp;
            }
            System.out.println("from: " + source + " to: " + dest + " weight: " + edge.getWeight());
        }
    }

    public static void main(String[] args) throws IOException {
        KruGraph graph = new KruGraph("/Users/Aman/IdeaProjects/CS 251/Project 4/src/localtestk2.txt");
        printGraph(graph.kruskalMST());
    }

}
