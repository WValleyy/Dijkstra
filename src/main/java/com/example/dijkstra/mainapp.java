package com.example.dijkstra;
/*
import com.example.dijkstra.Dijkstra;
import com.example.dijkstra.Edge;
import com.example.dijkstra.Graph;
import com.example.dijkstra.Node;
import javafx.util.Pair;

import java.util.List;

public class mainapp {

    public static void main(String[] args) {

        Node node1 = new Node();
        Node node2 = new Node();
        Node node3 = new Node();
        Node node4 = new Node();
        Node node5 = new Node();


        Edge e12 = new Edge(node1, node2);
        Edge e13 = new Edge(node1, node3);
        Edge e14 = new Edge(node1, node4);
        Edge e24 = new Edge(node2, node4);
        Edge e34 = new Edge(node3, node4);
        Edge e35 = new Edge(node3, node5);
        Edge e25 = new Edge(node2, node5);
        Edge e23 = new Edge(node2, node3);

        e12.setWeight(1);
        e13.setWeight(2);
        e24.setWeight(1);
        e34.setWeight(2);
        e14.setWeight(3);
        e35.setWeight(1);
        e25.setWeight(4);
        e23.setWeight(3);

        Graph g = new Graph();
        g.addNode(node1);
        g.addNode(node2);
        g.addNode(node3);
        g.addNode(node4);
        g.addNode(node5);

        g.addEdge(e12);
        g.addEdge(e13);
        g.addEdge(e14);
        g.addEdge(e24);
        g.addEdge(e34);
        g.addEdge(e35);
        g.addEdge(e25);
        g.addEdge(e23);

        g.setSource(node1);
        g.setDestination(node5);
        // Iterate through all nodes as source and run Dijkstra's algorithm
        Dijkstra d = new Dijkstra(g);
        d.calculateShortestPath();
        d.printTravelsal();
        d.printPaths();
        d.printSelect();
        for (Pair<Integer, Node> pair : d.getAdjacentNodes(node2)) {
            Node adjacentNode = pair.getValue();
            int edgeWeight = pair.getKey();
            System.out.println(adjacentNode.getId() + " " + edgeWeight);

        }
    }
}
*/