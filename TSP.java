import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class TSP {
    final static String fileName = "Capital_Cities.txt";
    static ArrayList<CityNode> cityList;
    static Graph cityGraph;
    static State state;
    static String start = "Albany,Ny";
    static int episodes = 100;
    static int n;
    static double globalLow = Double.POSITIVE_INFINITY;
    static ArrayList<Integer> globalBestTour;

    public static void main(String args[]) {
        // first init capital city graph
        loadCities();
        cityGraph = new Graph(cityList);
        n = cityList.size();
        state = new State(cityGraph);
        buildTour();
        printOutput();
    }

    private static void printOutput() {
        System.out.println("Length of shortest tour: "+globalLow);
        System.out.println("Shortest tour: ");
        for(int i=0;i<globalBestTour.size();i++){
            System.out.println(""+cityList.get(globalBestTour.get(i)).id +" ("+ cityList.get(globalBestTour.get(i)).name+")");
        }
    }

    static void buildTour() {

        int epi = 0;
        while (epi < episodes) {
            state.initAgents();
            // Step 2. of the algorithm
            for (int i = 0; i < n; i++) {// city iteration
                if (i != n - 1) {
                    for (int j = 0; j < n; j++) { // agent iteration
                        Agent tempAgent = state.agents[j];
                        int nextCity = state.getNextState(j);
                        if (i != n - 2) {
                            // if this is not the last city
                            // then the set of cities to be traveled to from the next city is equal to the
                            // set of cities to be traveled to by the current city minus the next city
                            // essentially, remove next city from unvisited
                            // add next city to visited
                            tempAgent.removeFromUnvisited(nextCity);

                        }
                        if (i == n - 2) {
                            // if this is the last city
                            // essentially, set unvisited to be the starting city
                            // or, remove next city from unvisited and add starting city
                            tempAgent.removeFromUnvisited(nextCity);
                            tempAgent.unvisited.add(tempAgent.startingCity);
                        }
                        tempAgent.nextCity = nextCity;
                        tempAgent.tour.add(nextCity);
                    }
                } else {
                    // in this cycle all agents go back to the initial city.
                    for (int j = 0; j < n; j++) {
                        Agent tempAgent = state.agents[j];
                        int nextCity = tempAgent.startingCity;
                        tempAgent.tour.add(nextCity);
                    }
                }
                for (int j = 0; j < n; j++) {
                    Agent tempAgent = state.agents[j];
                    state.updateQ(tempAgent.currentCity, tempAgent.nextCity, tempAgent);
                    tempAgent.currentCity = tempAgent.nextCity;
                }
                //System.out.println("loop");
            }
            // In this step delayed reinforcement is computed and AQ-values are updated
            // using formula(2), in which the next state evaluation term γ·Max AQ(rk1,z) is
            // null for all z
            //save best tour
            ArrayList<Integer> bestTour = new ArrayList<Integer>();
            double minLength = Double.POSITIVE_INFINITY;
            for(int i=0; i<n;i++){
                //compute Lk, the length of the tour done by agent K
                Agent tempAgent = state.agents[i];
                tempAgent.computeTourLength(cityGraph);
                if(tempAgent.tourLength<minLength){
                    bestTour = tempAgent.tour;
                    minLength = tempAgent.tourLength;
                }
            }
            if(minLength <globalLow){
                //global lowest tour
                globalLow = minLength;
                globalBestTour = bestTour;
            }
            //for each edge Q[r][s] compute delayed reinforcement term deltaAQ(r,s)
            for(int i=0;i<n;i++){
                for(int j=0;j<n;j++){
                    if(i==j){
                        continue;
                    }
                    //iteration best not global
                    //deltaQ is 0 if (r,s) is not an element of the best iter. tour
                    //meaning we need to save the iteration best tour
                    double deltaQ = state.calculateDeltaQ(i,j, bestTour, minLength);
                    state.updateQ(i, j, null, deltaQ);
                }
            }
            epi++;
        }
    }

    static void loadCities() {
        cityList = new ArrayList<CityNode>();
        try {
            File cities = new File(fileName);
            Scanner scan = new Scanner(cities);

            while (scan.hasNextLine()) {
                String line = scan.nextLine().trim();

                if (!line.isEmpty()) {
                    // Split the line using tab as the delimiter
                    String[] parts = line.split("\\s+");

                    // Extract the city name, latitude, longitude, and population
                    String cityName = parts[0].trim(); // Name (including state)
                    double latitude = Double.parseDouble(parts[1].trim()); // Latitude
                    double longitude = Double.parseDouble(parts[2].trim()); // Longitude
                    int population = Integer.parseInt(parts[3].trim()); // Population

                    // Create a new CityNode and add it to the cityList
                    CityNode city = new CityNode(cityName, latitude, longitude, population);
                    cityList.add(city);
                }
            }

            // Close the scanner
            scan.close();

        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
            e.printStackTrace();
        }

    }
}
