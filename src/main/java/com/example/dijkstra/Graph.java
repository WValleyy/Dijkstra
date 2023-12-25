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
// set source node
    public void setSource(Node node){
        if(nodes.contains(node))
            source = node;
    }



    public Node getSource(){
        return source;
    }

    public boolean isSource(Node node){
        return node == source;
    }



    public void addNode(Node node){
        node.setId(count);
        nodes.add(node);
        count++;
        if(node.getId()==1)
            source = node;
    }

    public void addEdge(Edge new_edge){
        for(Edge edge : edges){
            if(edge.equals(new_edge)){
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
        source = null;

    }

}


