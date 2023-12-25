package com.example.dijkstra;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import javax.swing.*;
import java.util.Optional;


public class Controller extends Canvas {

    @FXML
    private AnchorPane drawPane;

    private Graph graph;
    private Canvas canvas;
    private GraphicsContext gc;
    private DrawUtils drawUtils;
    private Node selectedNode;

    double pressX;
    double pressY;
    private Edge hoveredEdge;
    private boolean deleteNode = false;

    @FXML
    public void initialize() {

        canvas = new Canvas(1000,600);// Kích thước canvas
        gc = canvas.getGraphicsContext2D();
        drawUtils = new DrawUtils(gc);
        graph = new Graph();
        drawPane.getChildren().add(canvas);

        canvas.setOnMousePressed(this::handleMoveNode);
        canvas.setOnMouseDragged( event -> {
                if (event.isPrimaryButtonDown()) {
            handleDrag(event);
        }
    });
        canvas.setOnMouseReleased(this::handleMouseReleased);

        canvas.setOnMouseMoved(this::handleMouseMove);


    }

    private void handleMouseMove(MouseEvent e) {
        hoveredEdge = null; // moi lan an chuot xuong reset lai
        for (Edge edge : graph.getEdges()) {
            if(DrawUtils.isOnEdge(e, edge)) {
                hoveredEdge = edge;
                break;
            }
        }// nếu chuoot gan canh thi set canh day la hovered, khong thi thoi
    }


    private void handleMouseReleased(MouseEvent e) {


        if (pressX == e.getX() && pressY == e.getY()) {

            handleClick(e);
            pressX = 0;
            pressY = 0;
            return;
        }// nếu khi thả chuột ra, tọa độ không đổi thì tính là click, làm ntn bởi nếu click xong drag rồi thả thì nếu setOnMouseClick nó vẫn tính và tạo node mới
        for (Node node : graph.getNodes()) {
            if(selectedNode !=null && node!= selectedNode && DrawUtils.isWithinBounds(e, node)){
                Edge new_edge = new Edge(selectedNode, node);
                graph.addEdge(new_edge);

            }// nếu không thì xét xem trong các node có node nằm trong tọa độ chuot tại thời điểm thả không, nếu có thì tạo cạnh
            // xử lí cho sự kiện press xong drag chuột để tạo cạnh
        }
        selectedNode = null;// giải phóng node đang chọn

        redrawGraph();// vẽ lại
    }



    private void handleMoveNode(MouseEvent e) {
        pressX = e.getX();
        pressY = e.getY();
        deleteNode = e.isSecondaryButtonDown();
        Node clickedNode = getNodeAtPosition(e);
        if (clickedNode != null) {
            selectedNode = clickedNode;

        }// mỗi khi ấn xuống, neu có toa do chuot nằm trong node nào đó thì sẽ set node thành selected


    }
    private void handleClick(MouseEvent e) {// xử lí sự kiện click chuột

        Node clickedNode = getNodeAtPosition(e);
        if (clickedNode != null) {// nếu đang click vào node thì xử lí xóa hoặc chọn source node

            if (e.isControlDown() && deleteNode) {

                graph.deleteNode(clickedNode);
                deleteNode = false;
                redrawGraph();

            }else if (e.isControlDown() && e.isShiftDown()) {

                graph.setSource(clickedNode);

                redrawGraph();

            }
            return;
        }
        if(hoveredEdge != null) {// nếu đang chọn cạnh thì set weight
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Enter Weight");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter weight for " + hoveredEdge.toString() + " : ");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                String weightInput = result.get();
            try {
                int weight = Integer.parseInt(weightInput);
                if (weight > 0) {
                    hoveredEdge.setWeight(weight);
                    hoveredEdge = null;
                    redrawGraph();
                } else {
                    JOptionPane.showMessageDialog(null, "Weight should be positive");
                }
            } catch (NumberFormatException ignored) {}
            return;
        }}


        for(Node node : graph.getNodes()) {
            if (DrawUtils.isOverlapping(e, node)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Chồng lấn");
                alert.setHeaderText(null);
                alert.setContentText("Hai phần tử đang chồng lấn. Vui lòng di chuyển chúng ra khỏi nhau.");

                alert.showAndWait();
                return;
            }// check xem có chồng lấn khi tạo node không
        }
        if (!deleteNode && hoveredEdge == null) {// nếu ấn chuot phai va khong co canh nao duoc chọn thì tạo node
            Point2D p = new Point2D(e.getX(), e.getY());
            Node newNode = new Node(p);
            graph.addNode(newNode);
            redrawGraph();

        }
    }

    private void handleDrag(MouseEvent e) {


        if (selectedNode != null ) {
            if(e.isControlDown()){
                double x = e.getX();
                double y = e.getY();
                double radius = 20; // Assuming radius is 20

                // Ensure the node stays within the canvas bounds
                if (x - radius >= 0 && x + radius <= canvas.getWidth() && y - radius >= 0 && y + radius <= canvas.getHeight()) {

                    selectedNode.setPoint(x, y);
                }

            }
// xử lí khi drag node
            redrawGraph();// liên tục vẽ lại node và cạnh khi thay đổi tọa độ node
            }



    }

    private Node getNodeAtPosition(MouseEvent e) {
        for (Node node : graph.getNodes()) {
            if (DrawUtils.isWithinBounds(e, node)) {
                return node;
            }
        }
        return null;
    }// tìm xem có node nào tại tọa độ chuột không

    private void redrawGraph() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (Edge edge : graph.getEdges()) {
            drawUtils.drawEdge(edge);
        }
        for (Node node : graph.getNodes()) {
            if (graph.isSource(node)) {
                drawUtils.drawSourceNode(node);
            }else {
                drawUtils.drawNode(node);
            }
        }
    }


    @FXML
    void runPressed(ActionEvent event) {
        if (graph.getSource() != null) {
            Dijkstra d =new Dijkstra(graph);
            d.calculateShortestPath();
            d.printPaths(graph.getNodes());
            d.printTravelsal();
        }

    }


}
