import java.util.ArrayList;

public class Agent {
    int id;
    int n;
    static int uuid = 0;
    int startingCity;
    ArrayList<Integer> unvisited;
    ArrayList<Integer> visited;
    ArrayList<Integer> tour;
    int currentCity;
    int nextCity;
    double tourLength;

    public Agent(int startingCity, int id, int n) {
        this.startingCity = startingCity;
        this.id = id;
        this.n = n;
        this.currentCity = startingCity;
        initUnvisited();

    }

    private void initUnvisited() {
        unvisited = new ArrayList<Integer>();
        visited = new ArrayList<Integer>();
        for(int i=0;i<n;i++){
            if (i==startingCity){
                continue;
            }
            unvisited.add(i);
        }
        visited.add(startingCity);
        initTour();
    }

    private void initTour() {
        tour = new ArrayList<Integer>();
        tour.add(startingCity);
    }
    public void removeFromUnvisited(int s){
        unvisited.remove(Integer.valueOf(s));
    }

    public void computeTourLength(Graph graph) {
        double tempLength =0;
        for(int i=0;i< tour.size()-1;i++) {
            tempLength += CityNode.getDistance(graph.cityList.get(tour.get(i)), graph.cityList.get(tour.get(i+1)));
        }
        this.tourLength = tempLength;
    }
}
