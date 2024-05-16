import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

//represent each metro station as a node (Vertex)

public class Vertex<T> implements Vertex_Interface<T> {
	
	// Attributes
    private T stationName;
    private ArrayList<Edge<T>> edges;
    private Vertex<T> parent;
    private boolean visited;
    private double cost;

    // Constructor
    public Vertex(T stationName) {
        this.stationName = stationName;
        edges = new ArrayList<>();
        parent = null;
        visited = false;
    }

    // Getters & Setters
    
    @Override
    public T getStationName() {
        return stationName;
    }

    @Override
    public void setStationName(T stationName) {
        this.stationName = stationName;
    }
    
    @Override
    public void addEdge(Edge<T> e) {
        edges.add(e);
    }

    @Override
    public ArrayList<Edge<T>> getEdges() {
        return edges;
    }

    @Override
    public Vertex<T> getParent() {
        return parent;
    }

    @Override
    public void setParent(Vertex_Interface<T> parent) {
        this.parent = (Vertex<T>) parent;
    }

    @Override
    public double getCost() {
        return cost;
    }

    @Override
    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public void visit() {
        this.visited = true;
    }

    @Override
    public void unvisit() {
        this.visited = false;
    }

    @Override
    public boolean isVisited() {
        return this.visited;
    }

    // Additional Method
    
    @Override
    public String toString() {
        return "Station: " + stationName;
    }
    
    @Override
    public Vertex<T> getUnvisitedNeighbor() {
        Vertex<T> result = null;

        Iterator<Vertex<T>> neighbors = getNeighborIterator();
        while (neighbors.hasNext() && (result == null)) {
            Vertex<T> nextNeighbor = neighbors.next();
            if (!nextNeighbor.isVisited())
                result = nextNeighbor;
        }
        return result;
    }

    @Override
    public boolean hasEdge(T neighbor) {
        boolean found = false;
        Iterator<Vertex<T>> neighbors = getNeighborIterator();
        while (neighbors.hasNext()) {
            Vertex<T> nextNeighbor = neighbors.next();
            if (nextNeighbor.getStationName().equals(neighbor)) {
                found = true;
                break;
            }
        }
        return found;
    }

    @Override
    public boolean hasParent() {
        return parent != null;
    }

    @Override
    public Iterator<Vertex<T>> getNeighborIterator() {
        return new NeighborIterator();
    }

	// Iterator Class
    private class NeighborIterator implements Iterator<Vertex<T>> {
        
		// Attributes
    	int edgeIndex = 0;

		// Constructor
        private NeighborIterator() {
            edgeIndex = 0;
        }

        @Override
        public boolean hasNext() {
            return edgeIndex < edges.size();
        }

        @SuppressWarnings("unchecked")
		@Override
        public Vertex<T> next() {
            Vertex<T> nextNeighbor = null;

            if (hasNext()) {
                nextNeighbor = (Vertex<T>) edges.get(edgeIndex).getDestination();
                edgeIndex++;
            } else {
                throw new NoSuchElementException();
            }

            return nextNeighbor;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

}