import java.util.List;
import java.util.Queue;

/**
 * Interface for a directed graph.
 *
 * @param <T> The type of elements stored in the vertices of the graph.
 */
public interface DirectedGraph_Interface<T> {

    /**
     * Adds a directed edge between the specified origin and destination vertices with the given weight.
     *
     * @param origin      The origin vertex.
     * @param destination The destination vertex.
     * @param weight      The weight of the edge.
     */
    void addEdge(T origin, T destination, int weight);

    /**
     * Adds a directed edge between two vertices with the specified weight and line information.
     *
     * @param origin      the origin station of the edge
     * @param destination the destination station of the edge
     * @param weight      the weight (e.g., time in seconds) of the edge
     * @param line        the line information associated with the edge
     */
    void addEdge(T origin, T destination, int weight, String line);

    /**
     * Gets the edge between two vertices in the graph.
     *
     * @param origin      The origin vertex.
     * @param destination The destination vertex.
     * @return The edge between the given vertices.
     */
    Edge<T> getEdge(T origin, T destination);

    
    /**
     * Returns the number of vertices in the graph.
     *
     * @return The number of vertices.
     */
    int size();

    /**
     * Returns an iterable of all vertices in the graph.
     *
     * @return An iterable of vertices.
     */
    Iterable<Vertex<T>> vertices();

    /**
     * Finds the shortest path between the origin and destination vertices.
     *
     * @param origin      The origin vertex.
     * @param destination The destination vertex.
     * @return The list of vertices representing the shortest path.
     */
    List<T> findShortestPath(T origin, T destination);

    /**
     * Finds the cheapest path between the origin and destination vertices.
     *
     * @param origin      The origin vertex.
     * @param destination The destination vertex.
     * @return The list of vertices representing the cheapest path.
     */
    List<T> findCheapestPath(T origin, T destination);

    /**
     * Finds alternative paths between two vertices with limited transfers and suggests more than one alternative.
     *
     * @param origin         The origin vertex.
     * @param destination    The destination vertex.
     * @param maxTransfers   The maximum number of transfers allowed.
     * @param numAlternatives The maximum number of alternative paths to find.
     * @return A list of alternative paths, each represented as a list of vertices.
     */
    List<List<T>> findShortestPaths(T origin, T destination, int maxTransfers, int numAlternatives);

    /**
     * Finds alternative paths between two vertices with limited transfers and suggests more than one alternative.
     *
     * @param origin         The origin vertex.
     * @param destination    The destination vertex.
     * @param maxTransfers   The maximum number of transfers allowed.
     * @param numAlternatives The maximum number of alternative paths to find.
     * @return A list of alternative paths, each represented as a list of vertices.
     */
    List<List<T>> findCheapestPaths(T origin, T destination, int maxTransfers, int numAlternatives);

    /**
     * Finds alternative paths between the origin and destination vertices.
     *
     * @param origin         The origin vertex.
     * @param destination    The destination vertex.
     * @param maxTransfers   The maximum number of transfers allowed in the alternative paths.
     * @param numAlternatives The maximum number of alternative paths to find.
     * @param isShortest      A flag indicating whether to find the shortest paths.
     * @return A list of alternative paths, where each path is represented as a list of vertices.
     */
    List<List<T>> findAlternativePaths(T origin, T destination, int maxTransfers, int numAlternatives, boolean isShortest);

    /**
     * Performs a breadth-first traversal starting from the specified source vertex.
     *
     * @param source The source vertex.
     * @return A queue representing the order of traversal.
     */
    Queue<T> getBreadthFirstTraversal(T source);

    /**
     * Performs a depth-first traversal starting from the specified source vertex.
     *
     * @param source The source vertex.
     * @return A queue representing the order of traversal.
     */
    Queue<T> getDepthFirstTraversal(T source);

    /**
     * Displays the vertices and their adjacent vertices in the graph.
     */
    void display();
}
