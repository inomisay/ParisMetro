import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class DirectedGraph<T>implements DirectedGraph_Interface<T> {

	// Attributes
    private HashMap<T, Vertex<T>> vertices;

	// Constructor
    public DirectedGraph() {
        this.vertices = new HashMap<>();
    }
    
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addEdge(T origin, T destination, int weight) {
	    Vertex<T> originVertex = vertices.get(origin);
	    Vertex<T> destinationVertex = vertices.get(destination);

	    if (originVertex != null && destinationVertex != null && originVertex.hasEdge(destination)) {
	        // Edge already exists, update the weight if the new weight is less
	        ArrayList<Edge<T>> edges = originVertex.getEdges();
	        for (Edge<T> existingEdge : edges) {
	            if (existingEdge.getDestination().equals(destinationVertex)) {
	                if (weight < existingEdge.getWeight()) {
	                    existingEdge.setWeight(weight);
	                }
	                // If weight is greater or equal, do nothing
	                break; // Assuming there is at most one edge between two vertices
	            }
	        }
	        // System.out.println("Edge already exists. Updating properties...");
	    } else {
	        if (vertices.get(origin) == null) {
	            originVertex = new Vertex<>(origin);
	            vertices.put(origin, originVertex);
	        }

	        if (vertices.get(destination) == null) {
	            destinationVertex = new Vertex<>(destination);
	            vertices.put(destination, destinationVertex);
	        }

	        Edge<T> edge = new Edge(originVertex, destinationVertex, weight);
	        originVertex.addEdge(edge);
	    }
	}
	/*
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addEdge(T origin, T destination, int weight) {
	    Vertex<T> originVertex = vertices.get(origin);
	    Vertex<T> destinationVertex = vertices.get(destination);

	    // Create vertices only if they don't already exist
	    if (originVertex == null) {
	        originVertex = new Vertex<>(origin);
	        vertices.put(origin, originVertex);
	    }

	    if (destinationVertex == null) {
	        destinationVertex = new Vertex<>(destination);
	        vertices.put(destination, destinationVertex);
	    }

	    // Check if the edge already exists
	    boolean edgeExists = originVertex.hasEdge((T) destinationVertex);

	    // If the edge doesn't exist, add the new edge
	    if (!edgeExists) {
	        Edge<T> edge = new Edge(originVertex, destinationVertex, weight);
	        originVertex.addEdge(edge);
	    } else {
	        // Handle the case where the edge already exists
	        // Transfer between different lines or extend the path without adding duplicate vertices
	        handleExistingEdge(originVertex, destinationVertex, weight);
	    }
	}

	// Helper method to handle the case where the edge already exists
	private void handleExistingEdge(Vertex<T> origin, Vertex<T> destination, int weight) {
	    ArrayList<Edge<T>> edges = origin.getEdges();
	    for (Edge<T> existingEdge : edges) {
	        if (existingEdge.getDestination().equals(destination)) {
	            existingEdge.setWeight(weight);
	            break; // Assuming there is at most one edge between two vertices
	        }
	    }
	}
	*/
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addEdge(T origin, T destination, int weight, String line) {
	    Vertex<T> originVertex = vertices.get(origin);
	    Vertex<T> destinationVertex = vertices.get(destination);

	    if (originVertex != null && destinationVertex != null && originVertex.hasEdge(destination)) {
	        // Edge already exists
	        // System.out.println("This Station Line has already added to the Graph!");

	    } else {
	        if (vertices.get(origin) == null) {
	            originVertex = new Vertex<>(origin);
	            vertices.put(origin, originVertex);
	        }

	        if (vertices.get(destination) == null) {
	            destinationVertex = new Vertex<>(destination);
	            vertices.put(destination, destinationVertex);
	        }

	        Edge<T> edge = new Edge(originVertex, destinationVertex, weight);
	        edge.setLine(line); // Set the line information
	        originVertex.addEdge(edge);
	    }
	}

	@SuppressWarnings("unused")
	@Override
	public Edge<T> getEdge(T origin, T destination) {
	    Vertex<T> originVertex = vertices.get(origin);
	    Vertex<T> destinationVertex = vertices.get(destination);

	    if (originVertex != null && destinationVertex != null) {
	        Edge<T> edge = getConnectingEdge(originVertex, destinationVertex);
	        if (edge != null && edge.getLine() == null) {
	            // Access arrayMap directly from the CSVReader class
	            Object[][] arrayMapFromReader = CSVReader.getArrayMap();
	            // Pass arrayMap as a parameter to getConnectingEdge method
	            Edge<T> updatedEdge = getConnectingEdge(originVertex, destinationVertex);
	            if (updatedEdge != null) {
	                edge.setLine(updatedEdge.getLine());
	            }
	        }
	        return edge;
	    }

	    // Return null if the edge is not found
	    return null;
	}

	@Override
    public int size() {
        return vertices.size();
    }
	
	@Override
    public Iterable<Vertex<T>> vertices() {
        return vertices.values();
    }

	private void resetVertices() {
        for (Vertex<T> v : vertices.values()) {
            v.unvisit();
            v.setCost(0);
            v.setParent(null);
        }
    }
	
	// Dijkstra (fewer stops) 
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findShortestPath(T origin, T destination) {
	    resetVertices();

	    Map<Vertex<T>, Double> distances = new HashMap<>();
	    Map<Vertex<T>, Vertex<T>> previousVertices = new HashMap<>();
	    Set<Vertex<T>> visited = new HashSet<>();
	    PriorityQueue<Vertex<T>> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(distances::get));

	    Vertex<T> originVertex = vertices.get(origin);
	    Vertex<T> destinationVertex = vertices.get(destination);

	    if (originVertex == null || destinationVertex == null) {
	        System.out.println("Invalid origin or destination station.");
	        return Collections.emptyList();
	    }

	    distances.put(originVertex, 0.0);
	    priorityQueue.add(originVertex);

	    while (!priorityQueue.isEmpty()) {
	        Vertex<T> currentVertex = priorityQueue.poll();

	        if (visited.contains(currentVertex)) {
	            continue;
	        }

	        visited.add(currentVertex);

	        for (Edge<T> edge : currentVertex.getEdges()) {
	            Vertex<T> neighbor = (Vertex<T>) edge.getDestination();

	            // Check if neighbor is not visited and has a valid distance value
	            if (!visited.contains(neighbor) && distances.containsKey(currentVertex) && distances.get(currentVertex) != null) {
	                double newDistance = distances.get(currentVertex) + edge.getWeight();

	                if (newDistance < distances.getOrDefault(neighbor, Double.MAX_VALUE)) {
	                    distances.put(neighbor, newDistance);
	                    previousVertices.put(neighbor, currentVertex);
	                    priorityQueue.add(neighbor);
	                }
	            }
	        }

	        if (currentVertex.equals(destinationVertex)) {
	            List<T> path = buildPath(originVertex, destinationVertex, previousVertices);

	            // Display the suggested path for the shortest journey
	            displayShortestPath(originVertex, destinationVertex, previousVertices, distances);

	            // Print stations with lines
	            printStationsWithLines(path);

	            return path;
	        }
	    }

	    return Collections.emptyList();
	}

	// Dijkstra ("minimum time)
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findCheapestPath(T origin, T destination) {
	    resetVertices();

	    Map<Vertex<T>, Double> costs = new HashMap<>();
	    Map<Vertex<T>, Vertex<T>> previousVertices = new HashMap<>();
	    PriorityQueue<Vertex<T>> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(costs::get));
	    Set<Vertex<T>> visited = new HashSet<>();

	    Vertex<T> originVertex = vertices.get(origin);
	    Vertex<T> destinationVertex = vertices.get(destination);

	    if (originVertex == null || destinationVertex == null) {
	        System.out.println("Invalid origin or destination station.");
	        return Collections.emptyList();
	    }

	    costs.put(originVertex, 0.0);
	    priorityQueue.add(originVertex);

	    while (!priorityQueue.isEmpty()) {
	        Vertex<T> currentVertex = priorityQueue.poll();

	        if (visited.contains(currentVertex)) {
	            continue;
	        }

	        visited.add(currentVertex);

	        for (Edge<T> edge : currentVertex.getEdges()) {
	            Vertex<T> neighbor = (Vertex<T>) edge.getDestination();

	            // Check if neighbor is not visited and has a valid cost value
	            if (!visited.contains(neighbor) && costs.containsKey(currentVertex) && costs.get(currentVertex) != null) {
	                double newCost = costs.get(currentVertex) + edge.getWeight();

	                if (newCost < costs.getOrDefault(neighbor, Double.MAX_VALUE)) {
	                    costs.put(neighbor, newCost);
	                    previousVertices.put(neighbor, currentVertex);
	                    priorityQueue.add(neighbor);
	                }
	            }
	        }

	        if (currentVertex.equals(destinationVertex)) {
	            List<T> path = buildPath(originVertex, destinationVertex, previousVertices);

	            // Display the suggested path for the cheapest journey
	            displayCheapestPath(originVertex, destinationVertex, previousVertices, costs);

	            // Print stations with lines
	            printStationsWithLines(path);

	            return path;
	        }
	    }

	    return Collections.emptyList();
	}

	// FindShortestPath to limit transfers and suggest more than one alternative
	@SuppressWarnings("unchecked")
	@Override
	public List<List<T>> findShortestPaths(T origin, T destination, int maxTransfers, int numAlternatives) {
	    resetVertices();

	    Map<Vertex<T>, Double> distances = new HashMap<>();
	    Map<Vertex<T>, Vertex<T>> previousVertices = new HashMap<>();
	    Set<Vertex<T>> visited = new HashSet<>();
	    PriorityQueue<Vertex<T>> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(distances::get));

	    Vertex<T> originVertex = vertices.get(origin);
	    Vertex<T> destinationVertex = vertices.get(destination);

	    if (originVertex == null || destinationVertex == null) {
	        System.out.println("Invalid origin or destination station.");
	        return Collections.emptyList();
	    }

	    distances.put(originVertex, 0.0);
	    priorityQueue.add(originVertex);

	    int alternativesFound = 0;

	    List<List<T>> alternativePaths = new ArrayList<>();

	    while (!priorityQueue.isEmpty() && alternativesFound < numAlternatives) {
	        Vertex<T> currentVertex = priorityQueue.poll();

	        if (visited.contains(currentVertex)) {
	            continue;
	        }

	        visited.add(currentVertex);

	        for (Edge<T> edge : currentVertex.getEdges()) {
	            Vertex<T> neighbor = (Vertex<T>) edge.getDestination();

	            // Check if neighbor is not visited and has a valid distance value
	            if (!visited.contains(neighbor) && distances.containsKey(currentVertex) && distances.get(currentVertex) != null) {
	                double newDistance = distances.get(currentVertex) + edge.getWeight();

	                if (newDistance < distances.getOrDefault(neighbor, Double.MAX_VALUE)) {
	                    distances.put(neighbor, newDistance);
	                    previousVertices.put(neighbor, currentVertex);
	                    priorityQueue.add(neighbor);
	                }
	            }
	        }

	        if (currentVertex.equals(destinationVertex)) {
	            List<T> path = buildPath(originVertex, destinationVertex, previousVertices);

	            // Check if the path is valid (up to maxTransfers)
	            if (isValidPath(path, maxTransfers)) {
	                alternativePaths.add(path);
	                alternativesFound++;
	            }
	        }
	    }

	    return alternativePaths;
	}
	
	// FindCheapestPath to limit transfers and suggest more than one alternative
	@SuppressWarnings("unchecked")
	@Override
	public List<List<T>> findCheapestPaths(T origin, T destination, int maxTransfers, int numAlternatives) {
	    resetVertices();

	    Map<Vertex<T>, Double> costs = new HashMap<>();
	    Map<Vertex<T>, Vertex<T>> previousVertices = new HashMap<>();
	    PriorityQueue<Vertex<T>> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(costs::get));
	    Set<Vertex<T>> visited = new HashSet<>();

	    Vertex<T> originVertex = vertices.get(origin);
	    Vertex<T> destinationVertex = vertices.get(destination);

	    if (originVertex == null || destinationVertex == null) {
	        System.out.println("Invalid origin or destination station.");
	        return Collections.emptyList();
	    }

	    costs.put(originVertex, 0.0);
	    priorityQueue.add(originVertex);

	    int alternativesFound = 0;

	    List<List<T>> alternativePaths = new ArrayList<>();

	    while (!priorityQueue.isEmpty() && alternativesFound < numAlternatives) {
	        Vertex<T> currentVertex = priorityQueue.poll();

	        if (visited.contains(currentVertex)) {
	            continue;
	        }

	        visited.add(currentVertex);

	        for (Edge<T> edge : currentVertex.getEdges()) {
	            Vertex<T> neighbor = (Vertex<T>) edge.getDestination();

	            // Check if neighbor is not visited and has a valid cost value
	            if (!visited.contains(neighbor) && costs.containsKey(currentVertex) && costs.get(currentVertex) != null) {
	                double newCost = costs.get(currentVertex) + edge.getWeight();

	                if (newCost < costs.getOrDefault(neighbor, Double.MAX_VALUE)) {
	                    costs.put(neighbor, newCost);
	                    previousVertices.put(neighbor, currentVertex);
	                    priorityQueue.add(neighbor);
	                }
	            }
	        }

	        if (currentVertex.equals(destinationVertex)) {
	            List<T> path = buildPath(originVertex, destinationVertex, previousVertices);
	            
	            // Check if the path is valid (up to maxTransfers)
	            if (isValidPath(path, maxTransfers)) {
	                alternativePaths.add(path);
	                alternativesFound++;
	            }
	        }
	    }

	    return alternativePaths;
	}

	/*
	// Find alternative paths with limit on transfers and suggest more than one alternative
	/* This is the combination of the 2 Method 
	 * public List<List<T>> findShortestPaths(T origin, T destination, int maxTransfers, int numAlternatives) and 
	 * public List<List<T>> findCheapestPaths(T origin, T destination, int maxTransfers, int numAlternatives) 
	 * which help us to use and display everything much better if we want to show them together in the console
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<List<T>> findAlternativePaths(T origin, T destination, int maxTransfers, int numAlternatives, boolean isShortest) {
	    resetVertices();

	    Map<Vertex<T>, Double> metricValues = new HashMap<>();
	    Map<Vertex<T>, Vertex<T>> previousVertices = new HashMap<>();
	    PriorityQueue<Vertex<T>> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(metricValues::get));
	    Set<Vertex<T>> visited = new HashSet<>();

	    Vertex<T> originVertex = vertices.get(origin);
	    Vertex<T> destinationVertex = vertices.get(destination);

	    if (originVertex == null || destinationVertex == null) {
	        System.out.println("Invalid origin or destination station.");
	        return Collections.emptyList();
	    }

	    metricValues.put(originVertex, 0.0);
	    priorityQueue.add(originVertex);

	    int alternativesFound = 0;

	    List<List<T>> alternativePaths = new ArrayList<>();

	    while (!priorityQueue.isEmpty() && alternativesFound < numAlternatives) {
	        Vertex<T> currentVertex = priorityQueue.poll();

	        if (visited.contains(currentVertex)) {
	            continue;
	        }

	        visited.add(currentVertex);

	        for (Edge<T> edge : currentVertex.getEdges()) {
	            Vertex<T> neighbor = (Vertex<T>) edge.getDestination();

	            // Check if neighbor is not visited and has a valid metric value
	            if (!visited.contains(neighbor) && metricValues.containsKey(currentVertex) && metricValues.get(currentVertex) != null) {
	                double newMetricValue = metricValues.get(currentVertex) + edge.getWeight();

	                if (newMetricValue < metricValues.getOrDefault(neighbor, Double.MAX_VALUE)) {
	                    metricValues.put(neighbor, newMetricValue);
	                    previousVertices.put(neighbor, currentVertex);
	                    priorityQueue.add(neighbor);
	                }
	            }
	        }

	        if (currentVertex.equals(destinationVertex)) {
	            List<T> path = buildPath(originVertex, destinationVertex, previousVertices);

	            // Check if the path is valid (up to maxTransfers)
	            if (isValidPath(path, maxTransfers)) {
	                alternativePaths.add(path);
	                alternativesFound++;
	            }
	        }
	    }

	    // Display the suggested paths
	    displayAlternativePaths(alternativePaths, isShortest);

	    // Print stations with lines
	    for (int i = 0; i < alternativePaths.size(); i++) {
	        List<T> path = alternativePaths.get(i);
	        printStationsWithLines(path);
	    }

	    return alternativePaths;
	}
	
	// Helper method to check if the path is valid (up to maxTransfers)
	private boolean isValidPath(List<T> path, int maxTransfers) {
	    int transfers = 0;

	    for (int i = 1; i < path.size() - 1; i++) {
	        Edge<T> edge = getConnectingEdge(path.get(i - 1), path.get(i));
	        if (edge == null || edge.getDestination() == null) {
	            transfers++;
	            if (transfers > maxTransfers) {
	                return false;
	            }
	        }
	    }

	    return true;
	}
	
	// Helper method to convert the Vertex path to a List<T>
	private List<T> buildPath(Vertex<T> origin, Vertex<T> destination, Map<Vertex<T>, Vertex<T>> previousVertices) {
        LinkedList<T> path = new LinkedList<>();
        Vertex<T> current = destination;

        while (current != null) {
            path.addFirst(current.getStationName());
            current = previousVertices.get(current);
        }

        return path;
    }

	// Helper methods to get the connecting edge between two vertices.
	private Edge<T> getConnectingEdge(Vertex<T> source, Vertex<T> destination) {
	    Edge<T> connectingEdge = null;

	    for (Edge<T> edge : source.getEdges()) {
	        if (edge.getDestination().equals(destination)) {
	            connectingEdge = edge;
	            break;
	        }
	    }

	    if (connectingEdge != null && connectingEdge.getLine() == null) {
	        // Access arrayMap directly from the CSVReader class
	        Object[][] arrayMap = CSVReader.getArrayMap();

	        int currentIndex = -1; // Find the index of the current station in arrayMap
	        for (int i = 0; i < arrayMap.length; i++) {
	            if (arrayMap[i][0].equals(source.getStationName())) {
	                currentIndex = i;
	                break;
	            }
	        }

	        if (currentIndex != -1) {
	            // Set the line information from arrayMap
	            connectingEdge.setLine((String) arrayMap[currentIndex][5]);
	        }
	    }

	    return connectingEdge;
	}
	
	private Edge<T> getConnectingEdge(T origin, T destination) {
	    Vertex<T> originVertex = vertices.get(origin);
	    Vertex<T> destinationVertex = vertices.get(destination);

	    if (originVertex != null && destinationVertex != null) {
	        for (Edge<T> edge : originVertex.getEdges()) {
	            if (edge.getDestination().equals(destinationVertex)) {
	                return edge;
	            }
	        }
	    }

	    // Handle the case where the edge is not found (this should not happen in a valid path)
	    return null;
	}
	
	@Override
    public Queue<T> getBreadthFirstTraversal(T source) {
        resetVertices();
        Queue<T> traversalOrder = new LinkedList<>();
        Queue<Vertex<T>> vertexQueue = new LinkedList<>();

        Vertex<T> sourceVertex = vertices.get(source);
        sourceVertex.visit();

        traversalOrder.add(source);    // Enqueue vertex label
        vertexQueue.add(sourceVertex); // Enqueue vertex

        while (!vertexQueue.isEmpty()) {
            Vertex<T> frontVertex = vertexQueue.remove();
            Iterator<Vertex<T>> neighbors = frontVertex.getNeighborIterator();

            while (neighbors.hasNext()) {
                Vertex<T> nextNeighbor = neighbors.next();
                if (!nextNeighbor.isVisited()) {
                    nextNeighbor.visit();
                    traversalOrder.add(nextNeighbor.getStationName());
                    vertexQueue.add(nextNeighbor);
                }
            }
        }

        return traversalOrder;
    }
	
	@Override
    public Queue<T> getDepthFirstTraversal(T source) {
        resetVertices();
        Queue<T> traversalOrder = new LinkedList<>();
        Stack<Vertex<T>> vertexStack = new Stack<>();

        Vertex<T> originVertex = vertices.get(source);
        originVertex.visit();

        traversalOrder.add(source);    // Enqueue vertex label
        vertexStack.push(originVertex); // Push vertex

        while (!vertexStack.isEmpty()) {
            Vertex<T> currentVertex = vertexStack.peek();
            Vertex<T> unvisitedNeighbor = currentVertex.getUnvisitedNeighbor();

            if (unvisitedNeighbor != null) {
                unvisitedNeighbor.visit();
                traversalOrder.add(unvisitedNeighbor.getStationName());
                vertexStack.push(unvisitedNeighbor);
            } else {
                vertexStack.pop(); // No unvisited neighbor, backtrack
            }
        }

        return traversalOrder;
    }

    @Override
    public void display() {
        for (Vertex<T> v : vertices.values()) {
            System.out.print(v.getStationName() + " -> ");

            Iterator<Vertex<T>> neighbors = v.getNeighborIterator();
            while (neighbors.hasNext()) {
                Vertex<T> n = neighbors.next();
                System.out.print(n.getStationName() + " / ");
            }
            System.out.println();
        }
    }
    
    @SuppressWarnings("unchecked")
	private void displayShortestPath(Vertex<T> origin, Vertex<T> destination, Map<Vertex<T>, Vertex<T>> previousVertices, Map<Vertex<T>, Double> distances) {
        LinkedList<Vertex<T>> path = new LinkedList<>();
        Vertex<T> current = destination;

        while (current != null) {
            path.addFirst(current);
            current = previousVertices.get(current);
        }

        System.out.println("\nSuggested path (Shortest): \"Fewer Stops\"\n");
        for (int i = 0; i < path.size() - 1; i++) {
            Vertex<T> currentStation = path.get(i);
            Vertex<T> nextStation = path.get(i + 1);

            List<Edge<T>> edges = currentStation.getEdges();
            for (Edge<T> edge : edges) {
                if (edge.getDestination() == nextStation) {
                	
                    // System.out.println("Station " + ((Vertex<T>) edge.getOrigin()).getStationName() + ":   \n" + currentStation.getStationName() + " – " + nextStation.getStationName() + " (" + Math.abs(edge.getWeight()) + " Second)");
                	System.out.printf("Station %-30s: %-30s – %-30s (%d Second)\n",
                            ((Vertex<T>) edge.getOrigin()).getStationName(),
                            currentStation.getStationName(),
                            nextStation.getStationName(),
                            Math.abs(edge.getWeight()));
                	break;
                }
            }
        }
        double min = Math.abs(distances.get(destination)) / 60;
        System.out.println("----------------------------------");
        System.out.println("The journey Takes: \n" + "		  " + Math.abs(distances.get(destination)) + " Second\n" + "		  " + min + " min");
    }

    @SuppressWarnings("unchecked")
	private void displayCheapestPath(Vertex<T> origin, Vertex<T> destination, Map<Vertex<T>, Vertex<T>> previousVertices, Map<Vertex<T>, Double> costs) {
        LinkedList<Vertex<T>> path = new LinkedList<>();
        Vertex<T> current = destination;

        while (current != null) {
            path.addFirst(current);
            current = previousVertices.get(current);
        }

        System.out.println("\nSuggested path(Cheapest): \"Minimum Time\"");
        for (int i = 0; i < path.size() - 1; i++) {
            Vertex<T> currentStation = path.get(i);
            Vertex<T> nextStation = path.get(i + 1);

            List<Edge<T>> edges = currentStation.getEdges();
            for (Edge<T> edge : edges) {
                if (edge.getDestination() == nextStation) {
                	//System.out.println("Station " + ((Vertex<T>) edge.getOrigin()).getStationName() + ":		" + currentStation.getStationName() + " – " + nextStation.getStationName() + " (" + Math.abs(edge.getWeight()) + " Second)");
                	System.out.printf("Station %-30s: %-30s – %-30s (%d Second)\n",
                            ((Vertex<T>) edge.getOrigin()).getStationName(),
                            currentStation.getStationName(),
                            nextStation.getStationName(),
                            Math.abs(edge.getWeight()));
                	
                	break;
                }
            }
        }
        double min = Math.abs(costs.get(destination)) / 60;
        System.out.println("----------------------------------");
        System.out.println("The journey Takes: \n" + "		  " + Math.abs(costs.get(destination)) + " Second\n" + "		  " + min + " min");
    }

    @SuppressWarnings("unchecked")
	private void displayAlternativePaths(List<List<T>> alternativePaths, boolean isShortest) {
        //String metricType = isShortest ? "Shortest" : "Cheapest";
    	String metricType;
    	if (isShortest) {
    	    metricType = "Shortest \"Fewer Stops\"";
    	} else {
    	    metricType = "Cheapest \"Minimum Time\"";
    	}

        System.out.println("\nAlternative Paths (" + metricType + "):\n");

        for (List<T> path : alternativePaths) {
            for (int i = 0; i < path.size() - 1; i++) {
                Edge<T> edge = getConnectingEdge(path.get(i), path.get(i + 1));
                if (edge != null && edge.getOrigin() != null) {
                    System.out.printf("Station %-30s: %-30s – %-30s (%d Second)\n",
                            ((Vertex<T>) edge.getOrigin()).getStationName(),
                            path.get(i),
                            path.get(i + 1),
                            Math.abs(edge.getWeight()));
                }
            }
            System.out.println("----------------------------------");
        }
    }

	@SuppressWarnings("unused")
	private void printFormattedPath(List<T> path) {
	    int totalWeight = 0;
	    System.out.println("Suggestion");

	    for (T step : path) {
	        System.out.println(step);
	        String stepString = step.toString();
	        String[] stepParts = stepString.split(" \\(Weight: ");
	        if (stepParts.length > 1) {
	            totalWeight += Integer.parseInt(stepParts[1].replace(")", ""));
	        }
	    }

	    System.out.println("Total time: " + totalWeight + " min");
	}
	
	// Helper method to print stations with lines
	private void printStationsWithLines(List<T> path) {
	    // Access arrayMap directly from the CSVReader class
	    Object[][] arrayMap = CSVReader.getArrayMap();

	    if (arrayMap == null) {
	        System.out.println("Error: arrayMap is null.");
	        return;
	    }

	    for (int i = 0; i < path.size(); i++) {
	        T station = path.get(i);
	        System.out.print(station);

	        if (i < path.size() - 1) {
	            // Access getEdge directly from the Graph interface
	            Edge<T> edge = getEdge(station, path.get(i + 1));
	            String line = edge.getLine();
	            System.out.print(" (" + line + ") -> ");
	        }
	    }
	    System.out.println();
	}

}
