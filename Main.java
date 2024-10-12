import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main{
    final static String fileName = "Capital_Cities.txt";
    static ArrayList<CityNode> cityList;
    static Graph cityGraph;
    String start;
    String end;
    public static void main (String args[]){
        //first init capital city graph
        loadCities();
        
    }
    
    static void loadCities(){
        cityList = new ArrayList<CityNode>();
        try{
            File cities = new File(fileName);
            Scanner scan = new Scanner(cities);

            while (scan.hasNextLine()) {
                String line = scan.nextLine().trim();
                
                if (!line.isEmpty()) {
                    // Split the line using tab as the delimiter
                    String[] parts = line.split("\\s+");

                    // Extract the city name, latitude, longitude, and population
                    String cityName = parts[0].trim();  // Name (including state)
                    double latitude = Double.parseDouble(parts[1].trim());  // Latitude
                    double longitude = Double.parseDouble(parts[2].trim());  // Longitude
                    int population = Integer.parseInt(parts[3].trim());  // Population

                    // Create a new CityNode and add it to the cityList
                    CityNode city = new CityNode(cityName, latitude, longitude, population);
                    cityList.add(city);
                }
            }
            
            // Close the scanner
            scan.close();

        }catch(FileNotFoundException e){
            System.out.println("File Not Found");
            e.printStackTrace();
        }
        
    
    }
}
