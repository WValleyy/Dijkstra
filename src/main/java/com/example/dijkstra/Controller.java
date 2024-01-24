package com.example.dijkstra;

import javafx.animation.PauseTransition;
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
import javafx.util.Duration;

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

    @FXML
    private TextField sourceNodeIdTextField;

    private Iterator<Node> visitedIterator;

    Dijkstra d = new Dijkstra();

    private Map<Node, LinkedList<Node>> visitedPaths = new HashMap<>();

    private ArrayList<Node> hightLightVisitedNode = new ArrayList<>();
    private HashSet<Edge> hightLightEdge = new HashSet<>();

    private
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

        canvas = new Canvas(1000, 600);// Kích thước canvas
        gc = canvas.getGraphicsContext2D();
        drawUtils = new DrawUtils(gc);
        graph = new Graph();
        drawPane.getChildren().add(canvas);

        canvas.setOnMousePressed(this::handleMoveNode);
        canvas.setOnMouseDragged(event -> {
            if (event.isPrimaryButtonDown()) {
                handleDrag(event);
            }
        });
        d = new Dijkstra(graph);
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
            if (DrawUtils.isOnEdge(e, edge)) {
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
        //nếu selected node đã được chọn và nếu chuột dừng trong vùng vòng tron của node thì vẽ cạnh
        for (Node node : graph.getNodes()) {
            if (selectedNode != null && node != selectedNode && DrawUtils.isWithinBounds(e, node)) {
                Edge new_edge = new Edge(selectedNode, node);
                graph.addEdge(new_edge);

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
        // nếu có node thì trả về node, không thì trả về null

        if (clickedNode != null) {

            if (e.isControlDown() && deleteNode) {

                graph.deleteNode(clickedNode);
                deleteNode = false;
                redrawGraph();

            } else if (e.isControlDown() && e.isShiftDown()) {

                graph.setSource(clickedNode);

                redrawGraph();

            }
            return;
        }
        if (hoveredEdge != null) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Enter Weight");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter weight for " + hoveredEdge.toString() + " : ");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {

                String weightInput = result.get();
                try {
                    double weight = Double.parseDouble(weightInput);
                    if (weight > 0) {
                        hoveredEdge.setWeight(weight);
                        hoveredEdge = null;
                        redrawGraph();
                    } else {
                        showAlert("Weight should be positive");
                    }
                } catch (NumberFormatException ignored) {
                    showAlert("Weight should be a number");
                }
                return;
            }
        }


        for (Node node : graph.getNodes()) {
            if (DrawUtils.isOverlapping(e, node)) {
                showAlert("The two elements are overlapping. Please move them away from each other.");
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


        if (selectedNode != null) {
            if (e.isControlDown()) {
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



    void addShortestPathsEdge(Node visitedNode) {// thêm đường đi ngắn nhất vào list để highlight
        List<Node> shortestPath = d.getShortestPaths().get(visitedNode);
        if (shortestPath != null) {
            int pathSize = shortestPath.size();
            for (int i = 0; i < pathSize - 1; i++) {
                Node currentNode = shortestPath.get(i);
                Node nextNode = shortestPath.get(i + 1);
                Edge edge = graph.findEdge(currentNode, nextNode);
                hightLightEdge.add(edge);
            }
            // Highlight the last edge (if there is one)
            if (pathSize >= 1) {
                Node lastNode = shortestPath.get(pathSize - 1);
                Edge edge = graph.findEdge(lastNode, visitedNode);
                hightLightEdge.add(edge);
            }
        }
    }


    public void drawPath() {
        LinkedList<Node> vistedNode = d.getVisitNode();
        visitedIterator = vistedNode.iterator();
        processVisitedNode();
    }

    public void processVisitedNode() {
        if (visitedIterator.hasNext()) {

            Node visited = visitedIterator.next();
            hightLightVisitedNode.add(visited);
            graph.setLastestVisitedNode(visited);
            addShortestPathsEdge(visited);
            redrawGraph();


            LinkedList<Node> adjacencyNode = visitedPaths.get(visited);
            PauseTransition pause2 = new PauseTransition(Duration.seconds(2 * adjacencyNode.size() + 3));
            Iterator<Node> nodeIterator = adjacencyNode.iterator();
            processNextNode(nodeIterator);
            pause2.setOnFinished(event -> processVisitedNode());
            pause2.play();
        }else{
            graph.setLastestVisitedNode(null);
            redrawGraph();
        }
    }


    private final PauseTransition pause = new PauseTransition(Duration.seconds(2));

    private void processNextNode(Iterator<Node> nodeIterator) {
        if (nodeIterator.hasNext()) {
            Node node = nodeIterator.next();
            graph.setConsidering(node);
            System.out.println("Node under consideration : " + node.getId() + ".");
            redrawGraph();
            pause.setOnFinished(event -> processNextNode(nodeIterator));
            pause.play();
        }else{
            graph.setConsidering(null);
            redrawGraph();
        }
    }

    @FXML
    void runPressed(ActionEvent event) {
        if (graph.getSource() != null) {
            hightLightVisitedNode = new ArrayList<>();
            hightLightEdge = new HashSet<>();
            redrawGraph();
            d = new Dijkstra(graph);
            d.calculateShortestPath();
            visitedPaths = d.getVisitedPaths();
            drawPath();
            d.printTravelsal();
            d.printSelect();
        }
    }

    @FXML
    void resetPressed(ActionEvent event) {
        graph = new Graph();
        visitedPaths = new HashMap<>();
        hightLightVisitedNode = new ArrayList<>();
        hightLightEdge = new HashSet<>();

        redrawGraph();
    }

    @FXML
    void restartPressed(ActionEvent event) {
        hightLightVisitedNode = new ArrayList<>();
        hightLightEdge = new HashSet<>();
        graph.setConsidering(null);
        graph.setLastestVisitedNode(null);
        redrawGraph();
    }

    public void redrawGraph() {
        // Vẽ trạng thái của source node và target node
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (Edge edge : graph.getEdges()) {
            drawUtils.drawEdge(edge, "#555555");// ve canh thuong
        }
        for (Node node : graph.getNodes()) {
            drawUtils.drawNode(node, "#9C27B0", "#E1BEE7",  "#9C27B0"); // ve canh node thuong
        }

        if (graph.getSource() != null) {
            drawUtils.drawNode(graph.getSource(), "#00BCD4", "#B2EBF2", "#00BCD4"); // ve node nguon
        }
        for (Edge edge : hightLightEdge) {
            drawUtils.drawEdge(edge, "#FC6600");
        } // ve duong di ngan nhat
        if (graph.getConsidering() != null) {
            drawUtils.drawNode(graph.getConsidering(), "#00FF00", "#FFCCBC", "#FF5722");
        }// ve node dang xet
        for (Node node : hightLightVisitedNode) {
            drawUtils.drawNode(node, "#FFFF00", "#FC4C4E", "#FFD700");
        }// ve node da tham
        if (graph.getLastestVisitedNode() != null) {
            drawUtils.drawNode(graph.getLastestVisitedNode(), "#FF0000", "#FFCCBC", "#FF5722");
        }


    }

}

