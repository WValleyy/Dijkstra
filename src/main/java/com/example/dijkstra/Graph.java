package com.example.dijkstra;


import java.util.ArrayList;
import java.util.List;

public class Graph {
    private int count = 1;
    private final List<Node> nodes = new ArrayList<>();
    private final List<Edge> edges = new ArrayList<>();

    private Node source;

    private Node considering;

    private Node lastestVisitedNode;


    public List<Node> getNodes() {
        return nodes;
    }


    public List<Edge> getEdges() {
        return edges;
    }

    public void setSource(Node node) {
        if (nodes.contains(node))
            source = node;
    }

    public void setConsidering(Node node) {
        if (node == null){
            considering = null;
        }else
            considering = node;
    }


    public Node getSource() {
        return source;
    }

    public Node getConsidering() {
        return considering;
    }

    public Node getLastestVisitedNode(){
        return  lastestVisitedNode;
    }
    public  void setLastestVisitedNode(Node node){
        if (node == null) {
            lastestVisitedNode = null;
        }else
            lastestVisitedNode = node;
    }

    public void addNode(Node node) {
        node.setId(count);
        nodes.add(node);
        count++;

    }

    public void addEdge(Edge new_edge) {
        for (Edge edge : edges) {
            if (edge.equals(new_edge) || (edge.getNodeFrom() == new_edge.getNodeTo() && edge.getNodeTo() == new_edge.getNodeFrom())) {
                return;
            }
        }// kiem tra nếu cạnh đã tồn tại thì không thêm nữa

        edges.add(new_edge);
    }

    public void deleteNode(Node node) {
/*
        for (Edge edge : edges){
            if(edge.hasNode(node)){
                edges.remove(edge);
            }
        }
*/          // lỗi xảy ra khi vừa loop vừa xóa do không đồng bộ hóa : ConcurrentModificationException
        List<Edge> delete = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.hasNode(node)) {
                delete.add(edge);
            }
        }
        for (Edge edge : delete) {
            edges.remove(edge);
        }
        nodes.remove(node);
    }

    public Edge findEdge(Node node1, Node node2) {
        for (Edge edge : edges) {
            if (edge.getNodeFrom() == node1 && edge.getNodeTo() == node2) {
                return edge;
            }
            if (edge.getNodeFrom() == node2 && edge.getNodeTo() == node1) {
                return edge;
            }
        }
        return null;
    }


}


