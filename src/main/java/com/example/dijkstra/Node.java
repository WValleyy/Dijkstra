package com.example.dijkstra;

import javafx.geometry.Point2D;

public class Node {
    private int id; // định danh của node,đánh theo số nguyên
    private Point2D coord = new Point2D(0, 0);

    public Node(Point2D p) {
        this.coord = p;
    }

    public void setPoint(double x, double y) {
        coord = new Point2D(x, y);
    }

    public int getX() {
        return (int) coord.getX();
    }

    public int getY() {
        return (int) coord.getY();
    }

    public Point2D getCoord() {
        return coord;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
