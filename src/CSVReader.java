import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Reads data from a CSV file and populates a stopsMap with stopId as keys and stopName as values.
 *
 * @param csvFile   The path to the CSV file.
 * @param stopsMap  The map to store stopId-stopName pairs.
 */
public class CSVReader {
	
	// Static variable to store arrayMap
    private static Object[][] arrayMap;
    
	@SuppressWarnings("rawtypes")
	public static void readCSV(String csvFile, DirectedGraph graph) {
		
		// Attributes
	    String line;
	    String csvSplitter = ",";
	    int tableSize = 0;
	    int tempMap = 0;

	    try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
        	
	    	// Count the lines to determine the table size
            while ((line = br.readLine()) != null) {
            	tableSize++;
            }
            
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

	        arrayMap= new Object [tableSize][6];
	        
	        // Read and generate the array
	    	br.readLine(); // Skip the header line

	        while ((line = br.readLine()) != null) {
	        	
	            String[] data = line.split(csvSplitter);

	            /* Metro Map
	            String stopName = data[1].trim();
	            String stopTime = data[2].trim();
	            String stopSequence = data[3].trim();
	            String Retour_Aller = data[4].trim();
	            String RoadName = data[5].trim();
	            String VehicalType = data[6].trim();*/
	            
	            for (int j = 0; j < 6; j++) {
                    arrayMap[tempMap][j] = data[j + 1].trim();
                }
                tempMap++;

	        }
	        
	        // Add data to the graph
	        addToGraph(graph);
            
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	private static void addToGraph(DirectedGraph graph) {

        // Indexes start at StopName in our csv file
        for (int i = 0; i < arrayMap.length; i++) {
            // Check for null points
            if (arrayMap[i][2] == null) {
                break;
            }

            int stopSequence = Integer.parseInt((String) arrayMap[i][2]);
            int direction = Integer.parseInt((String) arrayMap[i][3]);

            // first check that if this one is the new way or not
            if (stopSequence != 1) { // It is the continuation of the last way

                // this one means that the i never be 1 here
                // calculate Time
                int time1 = Integer.parseInt((String) arrayMap[i - 1][1]);
                int time2 = Integer.parseInt((String) arrayMap[i][1]);
                int stopsTime = time2 - time1; // gets the time between the stops.it is weight

                // Get name
                String stopName0 = (String) arrayMap[i - 1][0]; // the source
                String stopName1 = (String) arrayMap[i][0]; // the index
                graph.addEdge(stopName0, stopName1, stopsTime);

            } /*else { // It is the new way
                String stopName1 = (String) arrayMap[i][0]; // the index , the Source index is null so the weight is 0 too.
                String line = (String) arrayMap[i][4]; // line information
                graph.addEdge(null, stopName1, 0);
                graph.addEdge(null, stopName1, 0, line);

            }*/
        }
    }
	
	// Getter method for arrayMap
    public static Object[][] getArrayMap() {
        return arrayMap;
    }

}
