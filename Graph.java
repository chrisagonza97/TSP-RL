//attributes we need:
//distance matrix
//startingCity
//endingCity
//for each city we need: its distance to every other city
    //its prize

import java.util.ArrayList;

public class Graph{
    double [][] dists;
    ArrayList<CityNode> cityList;
    int n;
    double avgLength; //average length of all edges 

    //populating distance matrex 
    public Graph(ArrayList<CityNode> cityList){
        this.n = cityList.size();
        this.cityList = cityList;
        dists = new double[n][n];
        double distSum=0;
        for(int i=0;i<n;i++){
            CityNode cityI = cityList.get(i);
            
            for(int j=0;j<n;j++){
                CityNode cityJ = cityList.get(j);
                if(i==j){
                    dists[i][j]= 99999.9;
                }else{
                    dists[i][j] = CityNode.getDistance(cityI,cityJ);
                    distSum+= dists[i][j];
                }
                
            }
            //n-1 because in Hamiltonian TSP, each node has an edge to every other node.
        }
        avgLength = distSum / ((n-1)*n);
    }
}

