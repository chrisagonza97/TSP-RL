import java.util.ArrayList;
import java.util.Random;

public class State {
    Graph graph;
    double Q[][];
    Agent agents[];
    int n;
    double q0 = .9;
    double aQ0;
    double alpha = .1;
    double gamma = .3;
    int delta = 1;
    int beta = 2;
    int W = 10;

    public State(Graph graph) {
        this.graph = graph;
        this.n = graph.dists.length;
        // initialize the Q table
        aQ0 = 1 / (graph.avgLength * n);
        initQTable();
        initAgents();
    }

    void initQTable() {
        Q = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Q[i][j] = aQ0;
            }
        }
    }

    void initAgents() {
        // one agent for every City
        agents = new Agent[n];
        for (int i = 0; i < n; i++) {
            agents[i] = new Agent(graph.cityList.get(i).id, i, n);
        }
    }

    public int getNextState(int k) {
        // find next state for agent k using formula (1)
        int currCity = agents[k].currentCity;
        double q = Math.random() * (1 + Double.MIN_VALUE);
        if (q <= q0) {
            // exploitation
            return maxNextCity(currCity, agents[k]);

        } else {
            // exploration
            return randomNextCity(currCity, agents[k]);
        }
    }

    private int randomNextCity(int k, Agent agent) {
        // calculate total weights
        Random random = new Random();
        double totalWeight = 0.0;
        double[] weights = new double[agent.unvisited.size()];
        int unvisitedSize = agent.unvisited.size();

        for (int i = 0; i < unvisitedSize; i++) {
            int tempCity = agent.unvisited.get(i);
            weights[i] = calculateFormOne(k, tempCity);
            totalWeight += weights[i];
        }

        // normalize weights to a cumulative distribution
        double[] cumulativeWeights = new double[unvisitedSize];
        // cumulativeWeights[0] = (k == 0 ? 0 : weights[0] / totalWeight); // Handle
        // edge case if k == 0
        for (int i = 1; i < unvisitedSize; i++) {
            cumulativeWeights[i] = cumulativeWeights[i - 1] + (weights[i] / totalWeight);
        }

        double randomValue = random.nextDouble(); // Random number between 0 and 1
        for (int i = 0; i < unvisitedSize; i++) {
            if (randomValue < cumulativeWeights[i]) {
                return agent.unvisited.get(i); // Return the selected city 
            }
        }

        return -1;// if this is reached, there is a bug
    }

    private int maxNextCity(int k, Agent agent) {
        double max = Double.NEGATIVE_INFINITY;
        int maxCity = -1;
        for (int i = 0; i < agent.unvisited.size(); i++) {
            int tempCity = agent.unvisited.get(i);
            double formOne = calculateFormOne(k, tempCity);
            if (formOne > max) {
                max = formOne;
                maxCity = tempCity;
            }
        }
        return maxCity;
    }

    public double calculateFormOne(int r, int u) {
        return Math.pow(Q[r][u], delta) * Math.pow(HE(r, u), beta);
    }

    private double HE(int r, int u) {
        // inverse of the distance between these two cities
        return 1 / graph.dists[r][u];
    }

    public void updateQ(int currentCity, int nextCity, Agent agent) {
        Q[currentCity][nextCity] = (1 - alpha) * Q[currentCity][nextCity] + alpha * gamma * maxQ(agent);
    }
    public void updateQ(int i, int j, Object object, double deltaQ) {
        Q[i][j] = (1 - alpha) * Q[i][j] + alpha * deltaQ;
    }
    

    private double maxQ(Agent agent) {
        // method returns a Q value
        // the Q value is the highest Q value from traveling to any city from the set of
        // unvisited cities from the current city.
        double max = Double.NEGATIVE_INFINITY;
        for(int i=0;i<agent.unvisited.size();i++){
            
            if(Q[agent.currentCity][agent.nextCity]> max){
                max = Q[agent.currentCity][agent.nextCity];
            }
        }
        return max;
    }

    public double calculateDeltaQ(int i, int j, ArrayList<Integer> bestTour, double minLength) {
        if(isInTour(i,j, bestTour)){
            return W/minLength;
        }else{
            return 0;
        }
    }

    private boolean isInTour(int i, int j, ArrayList<Integer> bestTour) {
        for(int k=0;k<bestTour.size()-1;k++){
            if(bestTour.get(k)==i && bestTour.get(k+1)==j){
                return true;
            }
        }
        return false;
    }

    
}
