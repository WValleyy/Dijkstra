package com.example.dijkstra;

import javafx.util.Pair;

import java.util.*;

public class Dijkstra {
    private Graph graph;
    private Map<Integer, Double> distances;
    private final List<Node> selectNode = new ArrayList<>();
    private final Map<Node, List<Node>> shortestPaths = new HashMap<>();
    private final Map<Node, LinkedList<Node>> visitPaths = new HashMap<>();
    //shortestPaths.get(currentNode): Retrieves the current shortest path from the source to currentNode.
    private final List<Node> traversalPath = new ArrayList<>();


    private final LinkedList<Node> visited = new LinkedList<>();

    public Dijkstra() {
    }


    public Map<Node, LinkedList<Node>> getVisitedPaths() {
        return visitPaths;
    }

    public Map<Node, List<Node>> getShortestPaths() {
        return shortestPaths;
    }

    public LinkedList<Node> getVisitNode() {
        return visited;
    }

    public Dijkstra(Graph graph) {

        this.graph = graph;
        distances = new HashMap<>();
        for (Node node : graph.getNodes()) {
            distances.put(node.getId(), Double.MAX_VALUE);
        }

    }


    public void calculateShortestPath() {
        Node source = graph.getSource();

        PriorityQueue<Pair<Double, Node>> unvisited = new PriorityQueue<>(Comparator.comparingDouble(Pair::getKey));

        // Insert source vertex into priority queue
        unvisited.add(new Pair<>(0.0, source));

        distances.put(source.getId(), 0.0);
        shortestPaths.put(source, new ArrayList<>());
        traversalPath.add(source);

        while (!unvisited.isEmpty()) {
            // Extract minimum distance vertex from pq
            Pair<Double, Node> currentPair = unvisited.poll();
            Node currentNode = currentPair.getValue();
            Double currentDistance = currentPair.getKey();
            selectNode.add(currentNode);
            visited.add(currentNode);
            visitPaths.put(currentNode, new LinkedList<>());

            for (Pair<Double, Node> pair : getAdjacentNodes(currentNode)) {
                Node adjacentNode = pair.getValue();
                Double edgeWeight = pair.getKey();

                if (!visited.contains(adjacentNode)) {
                    traversalPath.add(adjacentNode);
                    visitPaths.get(currentNode).add(adjacentNode);

                    // Relaxation step
                    if (distances.get(adjacentNode.getId()) > currentDistance + edgeWeight) {
                        distances.put(adjacentNode.getId(), currentDistance + edgeWeight);
                        shortestPaths.put(adjacentNode, new ArrayList<>(shortestPaths.get(currentNode)));
                        //Creates a new ArrayList for the adjacentNode and initializes it with the same elements as the shortest path to the currentNode
                        shortestPaths.get(adjacentNode).add(currentNode);

                        unvisited.removeIf(p -> p.getValue().equals(adjacentNode));
                        unvisited.add(new Pair<>(distances.get(adjacentNode.getId()), adjacentNode));

                    }

                }
            }


        }

        //clearHighlights();
    }

    public List<Pair<Double, Node>> getAdjacentNodes(Node node) {
        List<Pair<Double, Node>> neighbors = new ArrayList<>();

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


    public void printTravelsal() {

        String path = "";
        for (Node node1 : traversalPath) {
            path += node1.getId() + " -> ";
        }

        System.out.println(path);
    }

    public void printSelect() {
        String path = "";
        for (Node node1 : selectNode) {
            path += node1.getId() + " -> ";
        }
        System.out.println(path);
    }


}
