//attributes we need:
//distance matrix
//startingCity
//endingCity
//for each city we need: its distance to every other city
    //its prize

import java.util.ArrayList;

public class Graph{
    double [][] dists;
    int n;

    public Graph(ArrayList<CityNode> cityList){
        n = cityList.size();
        dists = new double[n][n];
        for(int i=0;i<n;i++){
            CityNode cityI = cityList.get(i);
            for(int j=0;j<n;j++){
                CityNode cityJ = cityList.get(j);
                if(i==j){
                    dists[i][j]= 99999.9;
                }else{
                    dists[i][j] = CityNode.getDistance(cityI,cityJ);
                }
                
            }
        }
        
    }
}

