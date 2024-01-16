package com.example.dijkstra;


import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private int count = 1;
    private List<Node> nodes = new ArrayList<>();
    private List<Edge> edges = new ArrayList<>();

    private Node source;
    private Node destination;

    private Node visit;

    private boolean solved = false;

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setNodes(List<Node> nodes){
        this.nodes = nodes;
    }

    public List<Node> getNodes(){
        return nodes;
    }

    public void setEdges(List<Edge> edges){
        this.edges = edges;
    }

    public List<Edge> getEdges(){
        return edges;
    }

    public boolean isNodeReachable(Node node){
        for(Edge edge : edges)
            if(node == edge.getNodeFrom() || node == edge.getNodeTo())
                return true;

        return false;
    }

    public void setSource(Node node){
        if(nodes.contains(node))
            source = node;
    }

    public void setVisit(Node node){
        if(nodes.contains(node))
            visit = node;
    }


    public Node getSource(){
        return source;
    }
    public Node getVisit(){
        return visit;
    }

    public Node getDestination(){
        return destination;
    }

    public boolean isSource(Node node){
        return node == source;
    }

    public boolean isDestination(Node node){
        return node == destination;
    }

    public void addNode(Point2D coord){
        Node node = new Node(coord);
        addNode(node);
    }

    public void addNode(Node node){
        node.setId(count);
        nodes.add(node);
        count++;

    }

    public void addEdge(Edge new_edge) {
        for (Edge edge : edges) {
            if (edge.equals(new_edge) || (edge.getNodeFrom() == new_edge.getNodeTo() && edge.getNodeTo() == new_edge.getNodeFrom())) {
                return;
            }
        }

        edges.add(new_edge);
    }

    public void deleteNode(Node node){
/*
        for (Edge edge : edges){
            if(edge.hasNode(node)){
                edges.remove(edge);
            }
        }
*/          // lỗi xảy ra khi vừa loop vừa xóa do không đồng bộ hóa : ConcurrentModificationException
        List<Edge> delete = new ArrayList<>();
        for (Edge edge : edges){
            if(edge.hasNode(node)){
                delete.add(edge);
            }
        }
        for (Edge edge : delete){
            edges.remove(edge);
        }
        nodes.remove(node);
    }

    public void clear(){
        count = 1;
        nodes.clear();
        edges.clear();
        solved = false;

        source = null;
        destination = null;
    }
    public Edge findEdge(Node node1, Node node2){
        for (Edge edge : edges){
            if (edge.getNodeFrom() == node1 && edge.getNodeTo() == node2){
                return edge;
            }
            if (edge.getNodeFrom() == node2 && edge.getNodeTo() == node1){
                return edge;
            }
        }
        return null;
    }

    public void setDestination(Node targetNode) {
        destination=targetNode;
    }

}


