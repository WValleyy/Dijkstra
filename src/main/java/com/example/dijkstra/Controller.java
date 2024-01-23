package com.example.dijkstra;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import javax.swing.*;
import java.util.*;


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
    private Dijkstra dijkstra;

    // Trong class Controller
    @FXML
    private TextField sourceNodeIdTextField;

    @FXML
    private TextField targetNodeIdTextField;

    private Iterator<Node> nodeIterator;
    @FXML
    void setSourceNode(ActionEvent event) {
        try {
            int sourceNodeId = Integer.parseInt(sourceNodeIdTextField.getText());
            Node sourceNode = findNodeById(sourceNodeId);
            if (sourceNode != null) {
                graph.setSource(sourceNode);
                redrawGraph();
            } else {
                showAlert("Node with ID " + sourceNodeId + " not found.");
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid Source Node ID. Please enter a valid integer.");
        }
    }

    @FXML
    void setTargetNode(ActionEvent event) {
        try {
            int targetNodeId = Integer.parseInt(targetNodeIdTextField.getText());
            Node targetNode = findNodeById(targetNodeId);
            if (targetNode != null) {
                graph.setDestination(targetNode);
                redrawGraph();
            } else {
                showAlert("Node with ID " + targetNodeId + " not found.");
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid Target Node ID. Please enter a valid integer.");
        }
    }

    private Node findNodeById(int nodeId) {
        for (Node node : graph.getNodes()) {
            if (node.getId() == nodeId) {
                return node;
            }
        }
        return null;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


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
        dijkstra = new Dijkstra(graph, this);
        canvas.setOnMouseReleased(this::handleMouseReleased);

        canvas.setOnMouseMoved(this::handleMouseMove);


    }

    private Node getNodeAtPosition(MouseEvent e) {
        for (Node node : graph.getNodes()) {
            if (DrawUtils.isWithinBounds(e, node)) {
                return node;
            }
        }
        return null;
    }


    private void handleMouseMove(MouseEvent e) {
        //Kiểm tra xem chuột có đang di chuyển trên cạnh nào không
        hoveredEdge = null;
        for (Edge edge : graph.getEdges()) {
            if(DrawUtils.isOnEdge(e, edge)) {
                hoveredEdge = edge;
                break;
            }
        }
    }


    private void handleMouseReleased(MouseEvent e) {

        //Nếu không di chuyển chuột, gọi sự kiện handleClick
        if (pressX == e.getX() && pressY == e.getY()) {

            handleClick(e);
            pressX = 0;
            pressY = 0;
            return;
        }
        //nếu selected node đã được chọn và nếu chuột dừng trong vùng vòng tronf của node thì vẽ cạnh
        for (Node node : graph.getNodes()) {
            if(selectedNode !=null && node!= selectedNode && DrawUtils.isWithinBounds(e, node)){
                Edge new_edge = new Edge(selectedNode, node);
                graph.addEdge(new_edge);
                graph.setSolved(false);
            }
        }
        selectedNode = null;

        redrawGraph();
    }



    private void handleMoveNode(MouseEvent e) {
        pressX = e.getX();
        pressY = e.getY();
        deleteNode = e.isSecondaryButtonDown(); //trả về True nếu đang bấm chuột phải( xóa)
        //Kiểm tra xem chuột phải có đang được nhấn không
        Node clickedNode = getNodeAtPosition(e);
        if (clickedNode != null) {
            selectedNode = clickedNode;

        }


    }
    private void handleClick(MouseEvent e) {

        Node clickedNode = getNodeAtPosition(e);
        // nếu có node thì trả về node, không thì trả về null3

        if (clickedNode != null) {

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
        if(hoveredEdge != null) {
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
            }
        }
        if (!deleteNode && hoveredEdge == null) {
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

            redrawGraph();
            }

    }

    public void redrawGraph() {
        this.q = new ArrayList<>();
        this.visited = new ArrayList<>();
        // Vẽ trạng thái của source node và target node
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (Edge edge : graph.getEdges()) {
            drawUtils.drawEdge(edge);
        }
        for (Node node : graph.getNodes()) {
            drawUtils.drawNode(node);
        }
        if (graph.getSource() != null) {
            drawUtils.drawSourceNode(graph.getSource());
        }
        if (graph.getDestination() != null) {
            drawUtils.drawTargetNode(graph.getDestination());
        }
        if (graph.getVisit() != null) {
            drawUtils.drawVisitNode(graph.getVisit());
        }
    }

    void highlightNodesAndEdge(ActionEvent event) {
        if (graph.getSource() != null && graph.getDestination() != null) {
            dijkstra.highlightNodesAndEdge(graph.getSource(), graph.getDestination());
            redrawGraph();
        }
    }
    void unhighlightNodesAndEdge(ActionEvent event) {
        if (selectedNode != null) {
            Node nextNode = dijkstra.getNextHighlightedNode(selectedNode);
            if (nextNode != null) {
                dijkstra.unhighlightNodeAndEdge(selectedNode, nextNode);
                redrawGraph();
            }
        }
    }

    @FXML
    void blinkAllNodes(ActionEvent event) {
        dijkstra.blinkAllNodes();
    }


    void drawSolution(Dijkstra d){
        Map<Node, List<Node>> shortestPaths = d.getShortestPaths();
        for (Node node : graph.getNodes()) {
            if (node == graph.getDestination()) {
                List<Node> shortestPath = shortestPaths.get(node);
                if (shortestPath != null) {
                    int pathSize = shortestPath.size();
                    for (int i = 0; i < pathSize - 1; i++) {
                        Node currentNode = shortestPath.get(i);
                        Node nextNode = shortestPath.get(i + 1);
                        Edge edge = graph.findEdge(currentNode, nextNode);
                        drawUtils.drawfinalPathNode(currentNode);
                        drawUtils.drawFinalEdge(edge);
                        drawUtils.drawfinalPathNode(nextNode);
                    }
                    // Highlight the last edge (if there is one)
                    if (pathSize >= 1) {
                        Node lastNode = shortestPath.get(pathSize - 1);
                        Edge edge = graph.findEdge(lastNode, node);
                        drawUtils.drawfinalPathNode(lastNode);
                        drawUtils.drawFinalEdge(edge);
                        drawUtils.drawfinalPathNode(node);
                    }
                }
            }
        }
    }
    void drawPointingTraversal(Dijkstra d, Node desNode){
        Map<Node, List<Node>> shortestPaths = d.getShortestPaths();
        for (Node node : graph.getNodes()) {
            if (node == desNode) {
                List<Node> shortestPath = shortestPaths.get(node);
                if (shortestPath != null) {
                    int pathSize = shortestPath.size();
                    for (int i = 0; i < pathSize - 1; i++) {
                        Node currentNode = shortestPath.get(i);
                        Node nextNode = shortestPath.get(i + 1);
                        Edge edge = graph.findEdge(currentNode, nextNode);
                        drawUtils.drawPointingPathNode(currentNode);
                        drawUtils.drawPointingEdge(edge);
                        drawUtils.drawPointingPathNode(nextNode);
                    }
                    // Highlight the last edge (if there is one)
                    if (pathSize >= 1) {
                        Node lastNode = shortestPath.get(pathSize - 1);
                        Edge edge = graph.findEdge(lastNode, node);
                        drawUtils.drawPointingPathNode(lastNode);
                        drawUtils.drawPointingEdge(edge);
                        drawUtils.drawPointingPathNode(node);
                    }
                }
            }
        }
    }
    public void drawTraversal(Dijkstra d) {
        List<Node> traversalPath = d.getSelectNode();
        nodeIterator = traversalPath.iterator();
        q.add(graph.getSource());

        processNextNode(d);
}
    // Queue list contain source node
    private ArrayList<Node> q = new ArrayList<>();
    private ArrayList<Node> visited = new ArrayList<>();
    public void updateQueue(Node node) {
        ArrayList<Node> updateingNodes = dijkstra.getNeibor(node);
        if (q.size() == 0) {
            return;
        } else {
            for (Node n : updateingNodes) {
                if (!visited.contains(n) && !q.contains(n)) {
                    q.add(n);
                }
            }
        }
        if (q.contains(node)){
            q.remove(node);
        }
        drawUtils.lightUpNodeInQuere(q);
    }
    private PauseTransition pause = new PauseTransition(Duration.seconds(2));
    private void processNextNode(Dijkstra d) {
        if (nodeIterator.hasNext()) {
            Node node = nodeIterator.next();
            ArrayList<Node> neibor = d.getNeibor(node);
            graph.setVisit(node);
            drawPointingTraversal(d, node);
            drawUtils.lightUpNodeInQuere(neibor);
            pause.setOnFinished(event -> processNextNode(d));
            pause.play();
            //set pause to play right here
//            drawUtils.drawNormalNodes(neibor);

        }else{
            graph.setVisit(null);
            redrawGraph();
            drawSolution(d);
        }
    }
    @FXML
    void runPressed(ActionEvent event) {
        if (graph.getSource() != null && graph.getDestination() != null) {
            for (Node node: graph.getNodes()){
                node.setHighlighted(false);
            }
            for (Edge edge : graph.getEdges()){
                edge.setHighlighted(false);
            }
            Dijkstra d = new Dijkstra(graph,this);
            d.calculateShortestPath();
            //d.highlightNodesAndEdge(graph.getSource(), graph.getDestination());
            //drawSolution(d);
            drawTraversal(d);
            d.printPaths();
            d.printSelect();
        }
    }

    @FXML
    void resetPressed(ActionEvent event) {
        graph = new Graph();
        redrawGraph();
    }

}

