import java.io.File;
import java.io.IOException;
import java.util.*;


public class DeliverySystem {

    //This main method shows how we read the delivery order file.
    //shows what we expect for testing
    //However there is nothing for you to implement
    //You could use this for testing
    public static void main(String[] args)throws IOException {
        DijGraph westLafayetteMap = new DijGraph("/Users/Aman/IdeaProjects/CS 251/Project 4/src/localmap.txt", 1);
        File diliveryFile = new File("/Users/Aman/IdeaProjects/CS 251/Project 4/src/localdelivery.txt");
//        DijGraph westLafayetteMap = new DijGraph(args[0], 1);
//        File diliveryFile = new File(args[1]);

        Scanner sc = new Scanner(diliveryFile);
        while(sc.hasNextLine()){
            String restaurantName = sc.next();
            Node restaurant = westLafayetteMap.findByName(restaurantName);
            double slope = sc.nextDouble();
            double intercept = sc.nextDouble();
            Node[] customer = new Node[3];
            double[] order = new double[3];
            for(int x =0; x <3; x++){
                customer[x] = westLafayetteMap.findByName(sc.next());
                order[x] = sc.nextDouble();
            }
            Delivery driver = new Delivery(westLafayetteMap,restaurant,customer,slope,intercept,order);
            System.out.println("The Order from " + restaurantName + ": "+ String.format("%.2f", driver.bestPath()));
        }

    }

}
