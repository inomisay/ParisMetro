import java.util.ArrayList;
import java.util.Iterator;

// Interface to represent a metro station as a node (Vertex)

public interface Vertex_Interface<T> {

    /**
     * Get the station name of the vertex.
     *
     * @return The station name of the vertex.
     */
    T getStationName();

    /**
     * Set the station name of the vertex.
     *
     * @param stationName The station name to set.
     */
    void setStationName(T stationName);

    /**
     * Add an edge to the vertex.
     *
     * @param e The edge to add.
     */
    void addEdge(Edge<T> e);

    /**
     * Get the list of edges connected to the vertex.
     *
     * @return The list of edges.
     */
    ArrayList<Edge<T>> getEdges();

    /**
     * Get the parent vertex.
     *
     * @return The parent vertex.
     */
    Vertex<T> getParent();

    /**
     * Set the parent vertex.
     *
     * @param parent The parent vertex to set.
     */
    void setParent(Vertex_Interface<T> parent);

    /**
     * Get the cost associated with the vertex.
     *
     * @return The cost of the vertex.
     */
    double getCost();

    /**
     * Set the cost associated with the vertex.
     *
     * @param cost The cost to set.
     */
    void setCost(double cost);

    /**
     * Mark the vertex as visited.
     */
    void visit();

    /**
     * Mark the vertex as unvisited.
     */
    void unvisit();

    /**
     * Check if the vertex has been visited.
     *
     * @return True if the vertex has been visited, false otherwise.
     */
    boolean isVisited();

    /**
     * Get an unvisited neighbor of the vertex.
     *
     * @return An unvisited neighbor vertex.
     */
    Vertex<T> getUnvisitedNeighbor();

    /**
     * Check if the vertex has an edge to a specified neighbor.
     *
     * @param neighbor The neighbor to check for.
     * @return True if there is an edge to the specified neighbor, false otherwise.
     */
    boolean hasEdge(T neighbor);

    /**
     * Check if the vertex has a parent.
     *
     * @return True if the vertex has a parent, false otherwise.
     */
    boolean hasParent();

    /**
     * Get an iterator for the neighbors of the vertex.
     *
     * @return Iterator for the neighbors.
     */
    Iterator<Vertex<T>> getNeighborIterator();
}
