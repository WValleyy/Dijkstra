package com.example.dijkstra;



import java.util.List;

public class mainapp {


    public static void main(String[] args) {

        Node node1 = new Node();
        Node node2 = new Node();
        Node node3 = new Node();
        Node node4 = new Node();
        Edge e12 = new Edge(node1, node2);
        Edge e13 = new Edge(node1, node3);
        Edge e24 = new Edge(node2, node4);
        Edge e34 = new Edge(node3, node4);
        e12.setWeight(12);
        e13.setWeight(27);
        e24.setWeight(20);
        e34.setWeight(15);
        Graph g =new Graph();
        g.addNode(node1);
        g.addNode(node2);
        g.addNode(node3);
        g.addNode(node4);
        g.addEdge(e12);
        g.addEdge(e13);
        g.addEdge(e24);
        g.addEdge(e24);
        g.setSource(node1);
        Dijkstra d =new Dijkstra(g);

        d.calculateShortestPath();

        d.printPaths(List.of(node1, node2, node3, node4));;
        d.printTravelsal();






    }

}