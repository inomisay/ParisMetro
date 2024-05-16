import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.List;

public class Navigation {

	public static void main(String[] args) {
		//navigateParisMetro();
		testNavigationSystem("Test100.csv");
	}
	
	@SuppressWarnings("unused")
	private static void navigateParisMetro() {
        DirectedGraph<String> metroGraph = new DirectedGraph<>();
        
		// Read data from the CSV file
        CSVReader.readCSV("Paris_RER_Metro.csv", metroGraph);

        System.out.println("---------------------Welcome to Paris Metro Map---------------------\n");

        Scanner scanner = new Scanner(System.in);
        int choice = getUserChoice(scanner);

        System.out.println();
        System.out.print("Please enter the origin station:");
        String origin = scanner.nextLine();
        System.out.print("Please enter the destination station:");
        String destination = scanner.nextLine();

        String preference = "";
        if (choice == 1) {
            preference = "Fewer Stops";
            metroGraph.findShortestPath(origin, destination);
        } else if (choice == 2) {
            preference = "Minimum Time";
            metroGraph.findCheapestPath(origin, destination);
        } else {
            System.out.println("Invalid choice. Please enter 1 or 2.");
        }
        
        System.out.println("*******************************************************************");
        
        // Bonus: Find alternative paths with limited transfers
        int maxTransfers = 2;
        int numAlternatives = 5;
        boolean isShortest = choice == 1; // Use choice to determine if it's shortest or cheapest
        List<List<String>> alternativePaths = metroGraph.findAlternativePaths(origin, destination, maxTransfers, numAlternatives, isShortest);

        System.out.println("Alternative Paths (up to " + maxTransfers + " transfers):");
        for (int i = 0; i < alternativePaths.size(); i++) {
            List<String> path = alternativePaths.get(i);
            System.out.println(path);
        }
        
        // Calculate the total number of stations in alternative paths
        int numStations = 0;
        for (int i = 0; i < alternativePaths.size(); i++) {
            List<String> path = alternativePaths.get(i);
            numStations += path.size() - 1; // Each edge in the path represents a station, excluding the origin
        }
        
        System.out.println("......................................................................");
        System.out.println("Preference: " + preference);
        System.out.println("Number of Stations: " + numStations);
        
    }
	
	private static int getUserChoice(Scanner scanner) {
	    int choice;

	    do {
	        System.out.println("Please select your choice:");
	        System.out.println("1. Find Shortest Path (Fewer Stops)");
	        System.out.println("2. Find Cheapest Path (Minimum Time)");
	        System.out.print("Enter your choice (1 or 2): ");

	        while (!scanner.hasNextInt()) {
	            System.out.print("Invalid input. Please enter a number. ---> ");
	            scanner.next(); // Consume the invalid input
	        }

	        choice = scanner.nextInt();

	        if (choice != 1 && choice != 2) {
	            System.out.print("Invalid choice. Please enter 1 or 2. ---> ");
	        }
	        
	        // Consume the newline character
	        scanner.nextLine();
	        
	    } while (choice != 1 && choice != 2);

	    return choice;
	}
	
	@SuppressWarnings("unused")
	private static void testNavigationSystem(String testFile) {
	    DirectedGraph<String> metroGraph = new DirectedGraph<>();
	    CSVReader.readCSV("Paris_RER_Metro.csv", metroGraph);

	    try (BufferedReader br = new BufferedReader(new FileReader(testFile))) {
	        // Skip the header line
	        br.readLine();

	        String line;
	        int totalQueries = 0;
	        long totalTime = 0;
	        
	        while ((line = br.readLine()) != null) {
	            String[] stops = line.split(",");
	            String origin = stops[0].trim();
	            String destination = stops[1].trim();
	            int preference = Integer.parseInt(stops[2].trim()); // 0 for fewer stops, 1 for minimum time
                System.out.print("......................................................................................................................");
	            // Display the preference
	            if (preference == 0) {
	                System.out.println("Preference: Fewer Stops");
	                List<String> shortestPath = metroGraph.findShortestPath(origin, destination);
	                int numberOfStations = shortestPath.size() - 1; // Number of stations is one less than the size of the path
	                System.out.println("Number of Stations: " + numberOfStations);
	            } else {
	                System.out.println("Preference: Minimum Time");
	                List<String> cheapestPath = metroGraph.findCheapestPath(origin, destination);
	                int numberOfStations = cheapestPath.size() - 1; // Number of stations is one less than the size of the path
	                System.out.println("Number of Stations: " + numberOfStations);
	            }
	            
	            long startTime = System.currentTimeMillis();

	            System.out.print(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	            // Bonus: Find alternative paths with limited transfers
	            int maxTransfers = 2;
	            int numAlternatives = 5;
	            boolean isShortest = preference == 0; // Use preference to determine if it's shortest or cheapest
	            List<List<String>> alternativePaths = metroGraph.findAlternativePaths(origin, destination, maxTransfers, numAlternatives, isShortest);

	            System.out.println("Alternative Paths (up to " + maxTransfers + " transfers):");
	            for (int i = 0; i < alternativePaths.size(); i++) {
	                List<String> path = alternativePaths.get(i);
	                System.out.println(path);
	            }
	            
	            long endTime = System.currentTimeMillis();
	            long queryTime = endTime - startTime;

	            System.out.println("Query Time for " + origin + " to " + destination + ": " + queryTime + " ms");
	            totalQueries++;
	            totalTime += queryTime;
	        }
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
	        long averageTime = totalQueries > 0 ? totalTime / totalQueries : 0;
	        System.out.println("Average Query Time: " + averageTime + " ms");
            System.out.println("**********************************************************************************************************************");

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

}
