/*package com.example.dijkstra;

import java.util.List;

public class mainapp {

    public static void main(String[] args) {

        Node node1 = new Node();
        Node node2 = new Node();
        Node node3 = new Node();
        Node node4 = new Node();
        Node node5 = new Node();
        Node node6 = new Node();

        Edge e12 = new Edge(node1, node2);
        Edge e13 = new Edge(node1, node3);
        Edge e24 = new Edge(node2, node4);
        Edge e34 = new Edge(node3, node4);
        Edge e45 = new Edge(node4, node5);
        Edge e56 = new Edge(node5, node6);
        Edge e26 = new Edge(node2, node6);

        e12.setWeight(12);
        e13.setWeight(27);
        e24.setWeight(20);
        e34.setWeight(15);
        e45.setWeight(10);
        e56.setWeight(25);
        e26.setWeight(18);
        Graph g = new Graph();
        g.addNode(node1);
        g.addNode(node2);
        g.addNode(node3);
        g.addNode(node4);
        g.addNode(node5);
        g.addNode(node6);

        g.addEdge(e12);
        g.addEdge(e13);
        g.addEdge(e24);
        g.addEdge(e34);
        g.addEdge(e45);
        g.addEdge(e56);
        g.addEdge(e26);

        // Iterate through all nodes as source and run Dijkstra's algorithm
        for (Node source : g.getNodes()) {
            // Reset the graph for each source
            g.setSource(source);
            Dijkstra d = new Dijkstra(g);
            d.calculateShortestPath();

            // Print the shortest paths for each source
            System.out.println("Shortest paths from Node " + source.getId() + ":");
            d.printPaths();
            d.printTraversal();
            System.out.println("------------------------------");
        }
    }
}
*/