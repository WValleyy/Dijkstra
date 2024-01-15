package com.example.dijkstra;

import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.util.Pair;

import java.util.*;

public class Dijkstra {
    private Graph graph;
    private int[] distances;
    private List<Node> selectNode = new ArrayList<>();
    private Map<Node, List<Node>> shortestPaths = new HashMap<>();
    //shortestPaths.get(currentNode): Retrieves the current shortest path from the source to currentNode.
    private List<Node> traversalPath = new ArrayList<>();
    private Controller controller;


    public List<Node> getTraversalPath() {
        return traversalPath;
    }
    public Map<Node, List<Node>> getShortestPaths() {
        return shortestPaths;
    }
    public Dijkstra(Graph graph, Controller controller) {
        this.graph = graph;
        int numNodes = graph.getNodes().size();
        distances = new int[numNodes];

        for (int i = 0; i < numNodes; i++) {
            distances[i] = Integer.MAX_VALUE;
        }
        this.controller = controller;
    }

    public void calculateShortestPath() {
        Node source = graph.getSource();
        Set<Node> visited = new HashSet<>();
        PriorityQueue<Pair<Integer, Node>> unvisited = new PriorityQueue<>(Comparator.comparingInt(Pair::getKey));

        // Insert source vertex into priority queue
        unvisited.add(new Pair<>(0, source));
        selectNode.add(source);
        distances[source.getId() - 1] = 0;
        shortestPaths.put(source, new ArrayList<>());
        traversalPath.add(source);

        while (!unvisited.isEmpty()) {
            // Extract minimum distance vertex from pq
            Pair<Integer, Node> currentPair = unvisited.poll();
            Node currentNode = currentPair.getValue();
            int currentDistance = currentPair.getKey();
            selectNode.add(currentNode);

            for (Pair<Integer, Node> pair : getAdjacentNodes(currentNode)) {
                Node adjacentNode = pair.getValue();
                int edgeWeight = pair.getKey();

                if (!visited.contains(adjacentNode)) {

                    // Relaxation step
                    if (distances[adjacentNode.getId() - 1] > currentDistance + edgeWeight) {
                        distances[adjacentNode.getId() - 1] = currentDistance + edgeWeight;
                        shortestPaths.put(adjacentNode, new ArrayList<>(shortestPaths.get(currentNode)));
                        //Creates a new ArrayList for the adjacentNode and initializes it with the same elements as the shortest path to the currentNode
                        shortestPaths.get(adjacentNode).add(currentNode);
                        adjacentNode.setHighlighted(true);
                        unvisited.removeIf(p -> p.getValue().equals(adjacentNode));
                        unvisited.add(new Pair<>(distances[adjacentNode.getId() - 1], adjacentNode));
                        traversalPath.add(adjacentNode);
                    }

                }
            }

            visited.add(currentNode);

        }

        clearHighlights();
    }

    public PriorityQueue<Pair<Integer, Node>> getAdjacentNodes(Node node) {
        PriorityQueue<Pair<Integer, Node>> neighbors = new PriorityQueue<>(Comparator.comparingInt(Pair::getKey));

        for (Edge edge : graph.getEdges()) {
            if (edge.getNodeFrom() == node) {
                neighbors.add(new Pair<>(edge.getWeight(), edge.getNodeTo()));
            }
            if (edge.getNodeTo() == node) {
                neighbors.add(new Pair<>(edge.getWeight(), edge.getNodeFrom()));
            }
        }
        return neighbors;
    }

    private void redrawGraph() {
        controller.redrawGraph();
    }

    public void highlightNodesAndEdge(Node sourceNode, Node destinationNode) {
        calculateShortestPath(); // Ensure the shortest path is calculated

        List<Node> shortestPath = shortestPaths.get(destinationNode);
        if (shortestPath != null) {
            int pathSize = shortestPath.size();
            for (int i = 0; i < pathSize - 1; i++) {
                Node currentNode = shortestPath.get(i);
                Node nextNode = shortestPath.get(i + 1);
                highlightNodeAndEdge(currentNode, nextNode);
            }
            // Highlight the last edge (if there is one)
            if (pathSize >= 1) {
                Node lastNode = shortestPath.get(pathSize - 1);
                highlightNodeAndEdge(lastNode, destinationNode);
            }
        }
    }

    private void highlightNodeAndEdge(Node currentNode, Node nextNode) {
        currentNode.setHighlighted(true);
        nextNode.setHighlighted(true);

        for (Edge edge : graph.getEdges()) {
            if ((edge.getNodeFrom() == currentNode && edge.getNodeTo() == nextNode) ||
                    (edge.getNodeFrom() == nextNode && edge.getNodeTo() == currentNode)) {
                edge.setHighlighted(true);
            }
        }
    }

    public void unhighlightNodeAndEdge(Node currentNode, Node adjacentNode) {
        // Unhighlight nodes
        currentNode.setHighlighted(false);
        adjacentNode.setHighlighted(false);

        // Unhighlight edge
        for (Edge edge : graph.getEdges()) {
            if ((edge.getNodeFrom() == currentNode && edge.getNodeTo() == adjacentNode) ||
                    (edge.getNodeFrom() == adjacentNode && edge.getNodeTo() == currentNode)) {
                edge.setHighlighted(false);
            }
        }

        redrawGraph();
    }

    public Node getNextHighlightedNode(Node currentNode) {
        List<Node> path = shortestPaths.get(currentNode);
        if (path != null && path.size() > 1) {
            return path.get(1); // Assuming the next node in the path is at index 1
        }
        return null;
    }

    public void clearHighlights() {
        for (Node node : graph.getNodes()) {
            node.setHighlighted(false);
        }
        for (Edge edge : graph.getEdges()) {
            edge.setHighlighted(false);
        }

        // Redraw graph to update the view
        redrawGraph();
    }


    public void blinkAllNodes() {
        // Blink all nodes
        for (Node node : graph.getNodes()) {
            node.setHighlighted(!node.getHighlighted());
        }

        redrawGraph();
    }

    public void printPaths() {
        for (Node node : graph.getNodes()) {
            if (node == graph.getDestination()) {
                String path = "";
                List<Node> shortestPath = shortestPaths.get(node);
                for (Node node1 : shortestPath) {
                    path += node1.getId() + " -> ";
                }
                path += graph.getDestination().getId();
                System.out.println(path);
            }
        }
    }
}
