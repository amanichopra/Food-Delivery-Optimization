import java.io.File;
import java.io.IOException;
import java.util.*;

public class DijGraph {
    static int MAXWEIGHT = 10000000;//The weight of edge will not exceed this number
    private Node[] nodeArr;//The vertices set in the graph
    private int nodeCount;//number of total vertices
    private int edgeCount;//number of total edges

    //Two option for the DijGraph constructor
    //Option 0 is used to build graph with for part 1: implementation for Dijkstra
    //Option 1 is used to build graph with for part 2: simple application of Dijkstra
    public DijGraph(String graph_file, int option)throws IOException{
        if (option == 0){
            File file = new File(graph_file);
            Scanner sc = new Scanner(file);
            nodeCount = sc.nextInt();
            //System.out.println("Beginning of file: " + graph_file);
            //System.out.print(nodeCount + " ");
            edgeCount = sc.nextInt();
            //System.out.println(edgeCount);
            nodeArr = new Node[nodeCount + 1];
            for(int i =0; i < nodeCount + 1; i ++){
                nodeArr[i]= new Node(i);
            }
            for(int i = 0;i < edgeCount; i ++){
                int begin = sc.nextInt();
                int end = sc.nextInt();
                int weight = sc.nextInt();
                //System.out.println(begin + " " + end + " " + weight);
                nodeArr[begin].addEdge(end, weight);
                nodeArr[end].addEdge(begin,weight);
            }
            //System.out.println("End of file: " + graph_file);
        }
        else if (option == 1){
            File file = new File(graph_file);
            Scanner sc = new Scanner(file);
            nodeCount = sc.nextInt();
            edgeCount = sc.nextInt();
            nodeArr = new Node[nodeCount + 1];
            for(int i =0; i < nodeCount + 1; i ++){
                if(i != 0){
                    nodeArr[i]= new Node(i, sc.next());
                }
            }
            for(int i = 0;i < edgeCount; i ++){
                String begin = sc.next();
                String end = sc.next();
                int weight = sc.nextInt();
                Node beginNode = findByName(begin);
                Node endNode = findByName(end);
                beginNode.addEdge(endNode.getNodeNumber(), weight);
                endNode.addEdge(beginNode.getNodeNumber(),weight);
            }
        }

    }

    //Finding the single source shortest distances by implementing dijkstra.
    //Using min heap to find the next smallest target
    public  Dist[] dijkstra(int source) {
        //System.out.println("running dijkstras w/ source " + source);
        Dist[] result = new Dist[nodeCount +1];

        Dist[] pq = new Dist[nodeCount + 1]; // min-heap based priority queue
        int pqInd = 0;
        int pqSize = 0;

        int[] dist = new int[nodeCount + 1]; // to keep track of distances
        //int[] prev = new int[nodeCount + 1]; // to keep track of prev vertex on shortest path
        boolean[] inTree = new boolean[nodeCount + 1]; // to keep track of vertices added
        inTree[0] = true;

        for (int i = 0; i <= nodeCount; i++) {
            if (i == source) {
                dist[i] = 0;
                continue;
            }
            dist[i] = Integer.MAX_VALUE;
            //prev[i] = -1;
            //inTree[i] = false;
        }
       // System.out.println("source: " + source);
        insert(pq, new Dist(source, dist[source]), pqSize++);
        //pqSize++;



        while (pqSize > 0) {
//            System.out.println("Before extracting min:");
//            for (int i = 0; i < pqSize; i++) {
//                System.out.print(pq[i].getDist() + ",");
//            }
//            System.out.println();
            Dist d = extractMin(pq, pqSize--);
            inTree[d.getNodeNumber()] = true;
//            System.out.println("min: " + d.getDist());
//            System.out.println("After extracting min:");
//            for (int i = 0; i < pqSize; i++) {
//                System.out.print(pq[i].getDist() + ",");
//            }
//            System.out.println();
//            System.out.println();

            HashMap<Integer, Integer> adjEdges = nodeArr[d.getNodeNumber()].getEdges();
            for (int i = 1; i <= nodeCount; i++) {
                if (!adjEdges.containsKey(i) || inTree[i]) {
                    continue;
                }
               // System.out.println("edge: " + i);

                int pathDist = d.getDist() + adjEdges.get(i);


//                System.out.println("current dist: " + dist[i]);
//                System.out.println("dist from min to edge: " + adjEdges.get(i));
//                System.out.println("dist from source to edge: " + pathDist);



                //System.out.println(dist[i]);
                if (pathDist < dist[i]) {
                    dist[i] = pathDist;
                    //prev[i] = d.getNodeNumber();

                    //System.out.println(indexOfV(pq, i, pqSize));
//                    if (indexOfV(pq, i, pqSize) == -1) { // if not in pq
//                        insert(pq, new Dist(i, dist[i]), pqSize++);
//                        //pqSize++;
//                    } else {
//                        pq[indexOfV(pq, i, pqSize)].updateDist(dist[i]);
//                        swim(pq, indexOfV(pq, i, pqSize));
//                    }
                    insert(pq, new Dist(i, dist[i]), pqSize++);

                }
                //System.out.println();

            }

        }

        for (int i = 0; i < dist.length; i++) {
            result[i] = new Dist(i, dist[i]);
        }

        //System.out.println();
        return result;
    }

    public static boolean allTrue(boolean[] inTree) {
        for (int i = 1; i <= inTree.length; i++) {
            if (!inTree[i]) {
                return false;
            }
        }
        return true;
    }

    //Find the vertex by the location name
    public Node findByName(String name){
        for (int x =1; x < nodeCount + 1; x++){
            if(nodeArr[x].getLocation().equals(name)){
                return nodeArr[x];
            }
        }
        return null;
    }

    //Implement insertion in min heap
    //first insert the element to the end of the heap
    //then swim up the element if necessary
    //Set it as static as always
    public static void insert(Dist [] arr, Dist value, int index){
        //TODO
//        System.out.println("arr before insert:");
//        for (Dist d : arr) {
//            try {
//                System.out.print(d.getDist() + ",");
//            } catch (NullPointerException e) {
//                continue;
//            }
//
//        }
//        System.out.println();
        if (index > arr.length - 1) { // return if index out of bounds
            return;
        }

        arr[index] = value;
        swim(arr, index);

//        System.out.println("arr after insert:");
//        for (Dist d : arr) {
//            try {
//                System.out.print(d.getDist() + ",");
//            } catch (NullPointerException e) {
//                continue;
//            }
//        }
//        System.out.println();
//        System.out.println();

    }

    public static void swim(Dist[] arr, int index) {

        if (index == 0) {
            return;
        }
        int parentInd = (int)Math.floor((index - 1) / 2); // index of parent

        if (arr[index].getDist() < arr[parentInd].getDist()) {
            swap(arr, index, parentInd);
            swim(arr, parentInd);
        }


    }

    public static void swap(Dist []arr, int index1, int index2){
        Dist temp = arr[index1];
        arr[index1] = arr[index2];
        arr[index2] = temp;
    }

    //Extract the minimum element in the min heap
    //replace the last element with the root
    //then do minheapify
    //Set it as static as always
    public static Dist extractMin (Dist[] arr, int size){
        //TODO
//        System.out.println("arr before extrmin:");
//        for (Dist d : arr) {
//            try {
//                System.out.print(d.getDist() + ",");
//            } catch (NullPointerException e) {
//                continue;
//            }
//        }
//        System.out.println();
        Dist min = arr[0];
        swap(arr, 0, size - 1);
        //arr[size - 1].updateDist(Integer.MAX_VALUE);
        minHeapify(arr, 0, size - 1);

//        System.out.println("arr after extrmin:");
//        for (Dist d : arr) {
//            try {
//                System.out.print(d.getDist() + ",");
//            } catch (NullPointerException e) {
//                continue;
//            }
//        }
//        System.out.println();
//        System.out.println();

        return min;
    }

    public static void minHeapify(Dist[] arr, int index, int size) {
        if (((int)Math.floor((size - 2) / 2) < index && index < size) || size == 1 || index == size) {
            return;
        }

        int leftChildInd = 2*index + 1;
        int rightChildInd = 2*index + 2;

        if (rightChildInd >= size) {
            if (arr[leftChildInd].getDist() < arr[index].getDist()) {
                swap(arr, index, leftChildInd);
            }
            return;
        }


        if (arr[index].getDist() > arr[leftChildInd].getDist() || arr[index].getDist() > arr[rightChildInd].getDist()) {
            if (arr[leftChildInd].getDist() <= arr[rightChildInd].getDist()) { // ensure parent swaps with smaller of
                // two children
                swap(arr, index, leftChildInd);
                minHeapify(arr, leftChildInd, size);
            } else {
                swap(arr, index, rightChildInd);
                minHeapify(arr, rightChildInd, size);
            }
        }

    }

    // 77, 32, 45, 28

    public static int indexOfV(Dist[] arr, int v, int size) {
        for (int i = 0; i < size; i++) {
            if (arr[i].getNodeNumber() == v) {
                return i;
            }
        }
        return -1;
    }

    //This will print the shortest distance result
    //The output format will be what we expect to pass the test cases
    public static void printResult(Dist[] result, int source){
        for(int x = 1;  x < result.length; x++){
            if(x != source){
                System.out.println(result[x].getNodeNumber() + " " +result[x].getDist());
            }
        }
    }

    public static void main(String[] args)throws IOException {
        DijGraph graph = new DijGraph("/Users/Aman/IdeaProjects/CS 251/Project 4/Test Cases/t5.txt", 0);
        Dist[] result  = graph.dijkstra(52);
        printResult(result, 52);

        // testing for extract min
//        int[] d = {28, 32, 45, 77};
//        int size = d.length;
//        Dist[] pq = new Dist[size];
//        for(int x =0; x < d.length; x++){
//            Dist temp = new Dist(x, d[x]);
//            DijGraph.insert(pq, temp, x);
//        }
//
//        System.out.println(extractMin(pq, size--).getDist());
//        for (Dist i : pq) {
//            System.out.print(i.getDist() + ", ");
//        }
//        System.out.println();
//        System.out.println(size);
//        System.out.println(extractMin(pq, size--).getDist());
//        for (Dist i : pq) {
//            System.out.print(i.getDist() + ", ");
//        }
//        System.out.println(
//        );
//        System.out.println(size);
    }
}
