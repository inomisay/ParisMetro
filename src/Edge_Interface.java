
// Interface to represent a directed edge between two stops

public interface Edge_Interface<T> {
    
    /**
     * Get the origin of the edge.
     *
     * @return The origin of the edge.
     */
    T getOrigin();

    /**
     * Set the origin of the edge.
     *
     * @param origin The starting point of the edge.
     */
    void setOrigin(T origin);

    /**
     * Get the destination of the edge.
     *
     * @return The destination of the edge.
     */
    T getDestination();

    /**
     * Set the destination of the edge.
     *
     * @param destination The ending point of the edge.
     */
    void setDestination(T destination);

    /**
     * Get the weight of the edge.
     *
     * @return The weight of the edge (number of stops, fewer stops = equal edge weights).
     */
    int getWeight();

    /**
     * Set the weight of the edge.
     *
     * @param weight The weight of the edge (number of stops, fewer stops = equal edge weights).
     */
    void setWeight(int weight);
    
    /**
     * Get the line information associated with the edge.
     *
     * @return The line information.
     */
    String getLine();

    /**
     * Set the line information associated with the edge.
     *
     * @param line The line information to set.
     */
    void setLine(String line);
}
