package configs;

import java.util.ArrayList;
import java.util.List;

public class Node {

    String name;
    List<Node> edges;

    Node(String name) {
        this.name = name;
        this.edges = new ArrayList<Node>();
    }

    public String getName() {
        return this.name;
    }

    public List<Node> getEdges() {
        return this.edges;
    }

    void setName(String n) {
        this.name = n;
    }

    void setEdges(List<Node> e) {
        this.edges = e;
    }

    void addEdge(Node edge) {
        this.edges.add(edge);
    }

    private boolean hasCyclesUtils(Node vertex, List<Node> vertexVisited) {
        // like dfs - check if we come to vertex that we visited before
        vertexVisited.add(vertex);
        for (Node neighbor : vertex.edges) {
            if (vertexVisited.contains(neighbor)) {
                return true;
            } else if (hasCyclesUtils(neighbor, vertexVisited)) {
                return true;
            }
        }
        // when we check all the vertex neighbors down
        vertexVisited.remove(vertex);
        return false;
    }

    boolean hasCycles() {
        // call to recursive function with array list to indicate if there is cycle
        return hasCyclesUtils(this, new ArrayList<Node>());
    }

}