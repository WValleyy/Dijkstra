package com.example.dijkstra;

import javafx.scene.paint.Color;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Dijkstra {
    private boolean safe = false;
    private String message = null;

    private Graph graph;

    private Map<Node, Integer> distances;

    private List<Node> traversalPath = new ArrayList<>();
    private List<Node> selectNode = new ArrayList<>();
    public class NodeComparator implements Comparator<Node>  {
        @Override
        public int compare(Node node1, Node node2) {
            return Integer.compare(distances.get(node1), distances.get(node2));
        }
    };

    public Dijkstra(Graph graph){
        this.graph = graph;
        distances = new HashMap<>();

        for(Node node : graph.getNodes()){
            distances.put(node, Integer.MAX_VALUE);
        }


    }

    public void calculateShortestPath() {
        Node source = graph.getSource();
        HashSet<Node> visted = new HashSet<>();
        PriorityQueue<Node> unvisted = new PriorityQueue<>(new NodeComparator());
        unvisted.add(source);
        selectNode.add(source);
        distances.put(source,0);
        while (!unvisted.isEmpty()) {
            Node currentNode = unvisted.poll();
            selectNode.add(currentNode);

            getAdjacentNodes(currentNode)
                    .entrySet().stream()
                    .filter(entry -> !visted.contains(entry.getKey()))
                    .forEach(entry -> {
                        evaluateDistanceAndPath(entry.getKey(), entry.getValue(), currentNode);
                        unvisted.add(entry.getKey());
                        traversalPath.add(entry.getKey());
                    });
            visted.add(currentNode);
        }
    }

    private void evaluateDistanceAndPath(Node adjacentNode, Integer edgeWeight, Node currentNode) {
        int newDistance = distances.get(currentNode) + edgeWeight;
        if (newDistance < distances.get(adjacentNode)) {
            distances.put(adjacentNode, newDistance);

            adjacentNode.setShortestPath(Stream.concat(currentNode.getShortestPath().stream(), Stream.of(currentNode)).toList());
        }
    }

    public HashMap<Node, Integer> getAdjacentNodes(Node node) {
        HashMap<Node, Integer> neighbors = new HashMap<>();

        for(Edge edge : graph.getEdges()){
            if(edge.getNodeFrom()==node)
                neighbors.put(edge.getNodeTo(), edge.getWeight());
            if(edge.getNodeTo()==node)
                neighbors.put(edge.getNodeFrom(), edge.getWeight());
        }
        return  neighbors;
    }
    public void printPaths(List<Node> nodes) {
        nodes.forEach(node -> {
            String path = node.getShortestPath().stream()
                    .map(Node::getId).map(Objects::toString)
                    .collect(Collectors.joining(" -> "));
            System.out.println((path.isBlank()
                    ? "%s : %s".formatted(node.getId(), distances.get(node))
                    : "%s -> %s : %s".formatted(path, node.getId(), distances.get(node)))
            );
        });
    }
    public void printTravelsal() {
        String path = traversalPath.stream()
                .map(Node::getId).map(Objects::toString)
                .collect(Collectors.joining(" -> "));
        System.out.println(path);

    }

}
