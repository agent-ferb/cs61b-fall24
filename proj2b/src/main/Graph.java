package main;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Graph {
    private Map<Integer, Set<Integer>> adjList;

    public Graph() {
        adjList = new HashMap<>();
    }

    public void createNode(int id) {
        adjList.putIfAbsent(id, new HashSet<>());
    }

    public void addEdge(int id1, int id2) {
        createNode(id1);
        createNode(id2);
        adjList.get(id1).add(id2);
    }

    public Set<Integer> getNodes() {
        return adjList.keySet();
    }

    // Retrieves direct neighbors (hyponyms) of a node
    public Set<Integer> getNeighbors(int id) {
        // Return hyponyms if the node exists, otherwise an empty set
        return adjList.getOrDefault(id, new HashSet<>());
    }
}
