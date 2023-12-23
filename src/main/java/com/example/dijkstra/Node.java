package com.example.dijkstra;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Node extends Circle implements Comparable<Node> {

    private static List<String> nodes = new LinkedList<>();
    private String name;
    private int distance = Integer.MAX_VALUE;

    private HashMap<Node, Integer> adjacentNodes = new HashMap<Node, Integer>();
    private List<Node> shortestPath = new LinkedList<Node>();

    public void addAdjacentNode(Node node, int weight ) {
        if (nodes.contains(node.getName())) {
            if (!adjacentNodes.containsKey(node)) {
                if (weight >= 0) {
                    adjacentNodes.put(node, weight);
                } else {
                    System.out.println("weight < 0!");
                }

            } else {
                System.out.println("da them canh nay roi!");
            }

        }else {
            System.out.println("Node lien ke khong ton tai!");
        }
    }

    public Node(String name) {
        super();
        this.name = name;
        nodes.add(name);
    }

    public Node(double v, Paint paint, String name ) {
        super(v, paint);
        this.name = name;
        nodes.add(name);
    }
    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public HashMap<Node, Integer> getAdjacentNodes() {
        return adjacentNodes;
    }

    public void setShortestPath(List<Node> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public List<Node> getShortestPath() {
        return shortestPath;
    }
    public String getName() {
        return name;
    }


    @Override
    public int compareTo(Node node) {
        return Integer.compare(this.distance, node.getDistance());
    }



}
