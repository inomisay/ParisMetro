
// Represent each line connecting two consecutive stops in a certain direction as a directed edge

public class Edge<T> implements Edge_Interface<T> {
	
	// Attributes
    private T origin;
    private T destination;
	private int weight; // fewer stops - equal edge weights
	private String line;

	//Constructors
	public Edge(T origin, T destination, int weight) {
        this.origin = origin;
        this.destination = destination;
        this.weight = weight;
    }
	public Edge(T destination, int weight, String line) {
        this.destination = destination;
        this.weight = weight;
        this.line = line;
    }
	
    // Getters & Setters
    
    @Override
    public T getOrigin() {
        return origin;
    }

    @Override
    public void setOrigin(T origin) {
        this.origin = origin;
    }

    @Override
    public T getDestination() {
        return destination;
    }

    @Override
    public void setDestination(T destination) {
        this.destination = destination;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public void setWeight(int weight) {
        this.weight = weight;
    }
    
    @Override
    public String getLine() {
        return line;
    }

    @Override
    public void setLine(String line) {
        this.line = line;
    }
    
}