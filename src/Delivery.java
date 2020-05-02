import java.util.*;


public class Delivery {
    private DijGraph westLafayette;//The graph
    private Node restaurant;//The vertex that the driver start
    private Node[] customer;//The vertices that the driver need to pass through
    private double slope;//Tip percentage function slope
    private double intercept;//Tip percentage function intercept
    private double [] order;//The order amount from each customer
    public Delivery (DijGraph graph,Node restaurant, Node[] customer, double slope, double intercept, double[] order){
        this.westLafayette = graph;
        this.restaurant = restaurant;
        this.customer = customer;
        this.slope = slope;
        this.intercept  = intercept;
        this.order = order;
    }

    //Finding the best path that the driver can earn most tips
    //Each time the driver only picks up three orders
    //Picking up N orders and find the maximum tips will be NP-hard
    public double bestPath(){
        int[][] permutations = new int[][]{new int[]{0, 1, 2}, new int[]{0, 2, 1}, new int[]{1, 0, 2}, new int[]{1, 2
                , 0}, new int[]{2, 0, 1}, new int[]{2, 1, 0}};

        double maxTips = 0;

        for (int i = 0; i < permutations.length; i++) {
            Dist[] shortestPaths = westLafayette.dijkstra(restaurant.getNodeNumber());
            double tips = 0;
            int distTraveled = 0;


            for (int j = 0; j < permutations[i].length; j++) {
               distTraveled += shortestPaths[customer[permutations[i][j]].getNodeNumber()].getDist();
               //System.out.println(distTraveled);
               double tipPercentage =
                       distTraveled * slope + intercept;
               tips += tipPercentage * 0.01 * order[permutations[i][j]];

               shortestPaths = westLafayette.dijkstra(customer[permutations[i][j]].getNodeNumber());

            }

            if (tips > maxTips) {
                maxTips = tips;
            }

        }




        return maxTips;
    }

}
