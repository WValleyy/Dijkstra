package com.example.dijkstra;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class DrawUtils {
    private final GraphicsContext gc;
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
// check xem chuột có ở gần node nào hay không
    public static boolean isOverlapping(MouseEvent e, Node n) {
        double x = e.getX();
        double y = e.getY();

        Point2D point = n.getCoord();
        double distance = point.distance(x, y);

        return distance < 2 * radius;
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
                (int) (v.getX() + t * (w.getX() - v.getX())),
                (int) (v.getY() + t * (w.getY() - v.getY()))
        ));
    }

    private static int distToSegment(Point2D p, Point2D v, Point2D w) {
        return (int) Math.sqrt(distToSegmentSquared(p, v, w));
    }

    public static boolean isOnEdge(MouseEvent e, Edge edge) {
        double x = e.getX();
        double y = e.getY();
        Point2D mouse_coor = new Point2D(x, y);
        int dist = distToSegment(mouse_coor,
                edge.getNodeFrom().getCoord(),
                edge.getNodeTo().getCoord());
        return dist < 6;
    }

    public static Color parseColor(String colorStr) {
        int red = Integer.valueOf(colorStr.substring(1, 3), 16);
        int green = Integer.valueOf(colorStr.substring(3, 5), 16);
        int blue = Integer.valueOf(colorStr.substring(5, 7), 16);

        return Color.rgb(red, green, blue);
    }


    public void drawWeightText(String text, int x, int y) { //vẽ văn bản cho trọng số tại vị trí (x,y)
        gc.setFill(Color.web("#cccccc")); //background màu đỏ

        Text textNode = new Text(text);
        textNode.setFont(gc.getFont()); // same font

        double t_width = textNode.getBoundsInLocal().getWidth(); // trả về chiều rộng văn bản
        double ascent = textNode.getBaselineOffset();

        gc.fillText(text, x - t_width / 2, y + ascent / 2); // viết tại vị trí trung tâm
    }


    public void drawWeight(Edge edge) {
        Point2D from = edge.getNodeFrom().getCoord();
        Point2D to = edge.getNodeTo().getCoord();
        int x = (int) (from.getX() + to.getX()) / 2;
        int y = (int) (from.getY() + to.getY()) / 2;

        int rad = radius / 2;
        gc.fillOval(x - rad, y - rad, 2 * rad, 2 * rad);
        drawWeightText(String.valueOf(edge.getWeight()), x, y);
        // Vẽ hình tròn sau đó vẽ trọng số tại trung điểm của cạnh
    }

    public void drawEdge(Edge edge, String color) {
        gc.setFill(parseColor("#555555"));
        drawBaseEdge(edge, color);
        drawWeight(edge);

    }


    private void drawBaseEdge(Edge edge, String color) {
        Point2D from = edge.getNodeFrom().getCoord();
        Point2D to = edge.getNodeTo().getCoord();

        gc.setStroke(parseColor(color)); // Default color
        gc.setLineWidth(3);
        gc.strokeLine(from.getX(), from.getY(), to.getX(), to.getY());
    }


    public void drawNode(Node node, String color1, String color2, String color3) {

        gc.setFill(parseColor(color1)); // Default color


        gc.fillOval(node.getX() - radius, node.getY() - radius, 2 * radius, 2 * radius);

        radius -= 5;
        gc.setFill(parseColor(color2));
        gc.fillOval(node.getX() - radius, node.getY() - radius, 2 * radius, 2 * radius);

        radius += 5;
        gc.setFill(parseColor(color3));

        drawCentreText(String.valueOf(node.getId()), node.getX(), node.getY());
    }


    public void drawCentreText(String text, int x, int y) {


        Text textNode = new Text(text);
        textNode.setFont(gc.getFont());

        double t_width = textNode.getBoundsInLocal().getWidth();
        double ascent = textNode.getBaselineOffset();

        gc.fillText(text, x - t_width / 2, y + ascent / 2);
        //đảm bảo căn giữa theo chiều ngang hay dọc
    }

}
