package com.example.dijkstra;

import javafx.geometry.Point2D;

import java.util.ArrayList;

import java.util.List;

public class Node {

    private int id; // id của node
    private Point2D coord = new Point2D(0,0); // tọa độ của node, biểu diễn bằng class Point2D

    private List<Node> shortestPath = new ArrayList<>(); // đường đi ngắn nhất tu source node đến node này
    public Node(){};
    public Node(Point2D p){
        this.coord = p;
    }
    public void setPoint(double x, double y) {
        coord = new Point2D(x, y);
    }
    public int getX(){
        return (int) coord.getX();
    }

    public int getY(){
        return (int) coord.getY();
    }

    public Point2D getCoord(){
        return coord;
    }
    public List<Node> getShortestPath() {
        return shortestPath;
    }

    public void setShortestPath(List<Node> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }



}
