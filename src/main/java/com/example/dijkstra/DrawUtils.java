package com.example.dijkstra;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;


import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DrawUtils {
    private GraphicsContext gc;
    private static int radius = 20;

    public DrawUtils(GraphicsContext graphicsContext) {
        this.gc = graphicsContext;
    }

    public static boolean isWithinBounds(MouseEvent e, Node n) {
        double x = e.getX();
        double y = e.getY();

        Point2D p = n.getCoord();

        return p.distance(x, y) <= radius;
    }

    public static boolean isOverlapping(MouseEvent e, Node n) {
        double x =  e.getX();
        double y =  e.getY();

        Point2D point = n.getCoord();
        double distance = point.distance(x, y);

        return distance < 2*radius;
    }

    private static int dist2(Point2D v, Point2D w) {
        return (int) (Math.pow((v.getX() - w.getX()), 2) + Math.pow(v.getY() - w.getY(), 2));
    }
    private static int distToSegmentSquared(Point2D p, Point2D v, Point2D w) {
        double l2 = dist2(v, w);
        if (l2 == 0) return dist2(p, v);
        double t = ((p.getX() - v.getX()) * (w.getX() - v.getX()) + (p.getY() - v.getY()) * (w.getY() - v.getY())) / l2;
        if (t < 0) return dist2(p, v);
        if (t > 1) return dist2(p, w);
        return dist2(p, new Point2D(
                (int)(v.getX() + t * (w.getX() - v.getX())),
                (int)(v.getY() + t * (w.getY() - v.getY()))
        ));
    }
    private static int distToSegment(Point2D p, Point2D v, Point2D w) {
        return (int) Math.sqrt(distToSegmentSquared(p, v, w));
    }

    public static boolean isOnEdge(MouseEvent e, Edge edge) {
        double x = e.getX();
        double y = e.getY();
        Point2D mouse_coor = new Point2D(x, y);
        int dist = distToSegment( mouse_coor,
                edge.getNodeFrom().getCoord(),
                edge.getNodeTo().getCoord() );
        if (dist<6)
            return true;
        return false;
    }

    public static Color parseColor(String colorStr) {
        int red = Integer.valueOf(colorStr.substring(1, 3), 16);
        int green = Integer.valueOf(colorStr.substring(3, 5), 16);
        int blue = Integer.valueOf(colorStr.substring(5, 7), 16);

        return Color.rgb(red, green, blue);
    }


    public void drawWeightText(String text, int x, int y) { //vẽ văn bản cho trọng số tại vị trí (x,y)
        gc.setFill(Color.web("#cccccc")); //background màu xám nhạt
        Font font = gc.getFont(); // default hoặc custom
        Text textNode = new Text(text);
        textNode.setFont(gc.getFont()); // same font

        double t_width = textNode.getBoundsInLocal().getWidth(); // trả về chiều rộng văn bản
        double ascent = textNode.getBaselineOffset();

        gc.fillText(text, x - t_width / 2, y + ascent / 2); // viết tại vị trí trung tâm
    }

    public void drawWeight(Edge edge) {
        Point2D from = edge.getNodeFrom().getCoord();
        Point2D to = edge.getNodeTo().getCoord();
        int x = (int)(from.getX() + to.getX())/2;
        int y = (int)(from.getY() + to.getY())/2;

        int rad = radius/2;
        gc.fillOval(x-rad, y-rad, 2*rad, 2*rad);
        drawWeightText(String.valueOf(edge.getWeight()), x, y);
        // Vẽ hình tròn sau đó vẽ trọng số tại trung điểm của cạnh
    }





    public void drawEdge(Edge edge) {
        gc.setFill(parseColor("#555555"));
        drawBaseEdge(edge);
        drawWeight(edge);
    }

    private void drawBaseEdge(Edge edge) {
        Point2D from = edge.getNodeFrom().getCoord();
        Point2D to = edge.getNodeTo().getCoord();

        if (edge.getHighlighted()) {
            gc.setStroke(parseColor("#FFD700"));

            // Highlighted color
        } else {
            gc.setStroke(parseColor("#555555")); // Default color
        }

        gc.setLineWidth(3);
        gc.strokeLine(from.getX(), from.getY(), to.getX(), to.getY());
    }



    public void drawSourceNode(Node node){
        gc.setFill(parseColor("#00BCD4"));
        gc.fillOval(node.getX() - radius, node.getY() - radius, 2 * radius, 2 * radius);

        radius-=5;
        gc.setFill(parseColor("#B2EBF2"));
        gc.fillOval(node.getX() - radius, node.getY() - radius, 2 * radius, 2 * radius);

        radius+=5;
        gc.setFill(parseColor("#00BCD4"));
        drawCentreText(String.valueOf(node.getId()), node.getX(), node.getY());
    }

    public void drawTargetNode(Node node) {
        gc.setFill(parseColor("#FF5722")); // Màu sắc cho target node
        gc.fillOval(node.getX() - radius, node.getY() - radius, 2 * radius, 2 * radius);

        radius -= 5;
        gc.setFill(parseColor("#FFCCBC")); // Màu sắc cho phần fill bên trong target node
        gc.fillOval(node.getX() - radius, node.getY() - radius, 2 * radius, 2 * radius);

        radius += 5;
        gc.setFill(parseColor("#FF5722"));
        drawCentreText(String.valueOf(node.getId()), node.getX(), node.getY());
    }




    public void drawNode(Node node) {
        if (node.getHighlighted()) {
            gc.setFill(parseColor("#FFFF00")); // Highlighted color, you can change it as desired
        } else {
            gc.setFill(parseColor("#9C27B0")); // Default color
        }

        gc.fillOval(node.getX() - radius, node.getY() - radius, 2 * radius, 2 * radius);

        radius -= 5;
        gc.setFill(parseColor("#E1BEE7"));
        gc.fillOval(node.getX() - radius, node.getY() - radius, 2 * radius, 2 * radius);

        radius += 5;
        gc.setFill(parseColor("#9C27B0"));
        drawCentreText(String.valueOf(node.getId()), node.getX(), node.getY());
    }



    public void drawCentreText(String text, int x, int y) {

        Font font = gc.getFont(); // Use the default font or set a custom one
        Text textNode = new Text(text);
        textNode.setFont(gc.getFont()); // Set the same font as the GraphicsContext

        double t_width = textNode.getBoundsInLocal().getWidth();
        double ascent = textNode.getBaselineOffset();

        gc.fillText(text, x - t_width / 2, y + ascent / 2);
        //đảm bảo căn giữa theo chiều ngang hay dọc

    }

}
