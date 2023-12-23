package com.example.dijkstra;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;

public class Controller  {

    @FXML
    private ComboBox<String> fromVertex;

    @FXML
    private Pane graph;

    @FXML
    private TextField nbVertices;

    @FXML
    private ComboBox<String> toVertex;

    @FXML
    private ComboBox<String> startVertex;

    @FXML
    private TextField weight;

    List<Node> node_lists = new ArrayList<>();
    ObservableList<String> vertexItems = FXCollections.observableArrayList();

    public void initialize() {
        toVertex.setItems(vertexItems);
        fromVertex.setItems(vertexItems);
        startVertex.setItems(vertexItems);
    }

    @FXML
    public void generatePress() {
        // Xóa tất cả các nút hiện tại trong Pane
        graph.getChildren().clear();
        vertexItems.clear();
        // Lấy số lượng vertices từ TextField
        int numberOfVertices = 0;
        try {
            numberOfVertices = Integer.parseInt(nbVertices.getText());

            // Tính toán các thông số cần thiết và tạo hình tròn
            // ...

        } catch (NumberFormatException e) {
            // Xử lý trường hợp khi không thể chuyển đổi thành số nguyên
            System.out.println("Invalid number format. Please enter a valid number of vertices.");
        }

        // Tính toán các thông số cần thiết
        double centerX = graph.getWidth() / 2.0;
        double centerY = graph.getHeight() / 2.0;
        double radius = 20.0; // Bán kính của hình tròn

        // Tạo và thêm các hình tròn vào Pane
        for (int i = 0; i < numberOfVertices; i++) {

            double x = centerX - 50 + 50*i;
            double y = centerY - 50 + 100*(i%2);

            Node node = createNode(i);
            node.setCenterX(x);
            node.setCenterY(y);
            Label label = createLabel(i);

            label.setLayoutX(x-5); // Adjust label position based on node position
            label.setLayoutY(y-13); // Adjust label position based on node position



            graph.getChildren().addAll(node, label);

            // Thêm sự kiện để cập nhật DoubleProperty khi vị trí của Node thay đổi

            node_lists.add(node);
            vertexItems.add(node.getName());

            node.setOnMousePressed(this::handleMousePressed);
            node.setOnMouseDragged(event -> handleMouseDragged(event, label));

        }


    }

    private Node createNode(int i) {

        // Handle mouse events for node movement
        return new Node(20, Color.BLACK, String.valueOf(i));
    }

    private Label createLabel(int i) {
        Label label = new Label(String.valueOf(i));
        label.setStyle("-fx-background-color: none; -fx-border-color: none;");
        label.setStyle("-fx-text-fill: WHITE ; -fx-font-size: 16px;");
        label.setMinWidth(10); // Adjust width as needed
        label.setAlignment(javafx.geometry.Pos.CENTER);


        return label;
    }

    private double xOffset = 0;
    private double yOffset = 0;

    private void handleMousePressed(MouseEvent event) {
        xOffset = event.getSceneX() - ((Node) event.getSource()).getCenterX();
        yOffset = event.getSceneY() - ((Node) event.getSource()).getCenterY();
    }

    private void handleMouseDragged(MouseEvent event, Label label) {
        Node g = (Node) event.getSource();
        double x = event.getSceneX() - xOffset;
        double y = event.getSceneY() - yOffset;
        g.setCenterX(x);
        g.setCenterY(y);
        label.setLayoutX(x-5);
        label.setLayoutY(y-13);


    }

    @FXML
    void addEdgePressed(ActionEvent event) {
        String fromVertexValue = fromVertex.getValue();
        String toVertexValue = toVertex.getValue();
        int weightValue = Integer.parseInt(weight.getText());

        if (fromVertexValue != null && toVertexValue != null && !fromVertexValue.equals(toVertexValue)) {

            createEdge(fromVertexValue, toVertexValue, weightValue);

        }
    }
    private void createEdge(String fromVertex,String toVertex, int weightValue) {
        // Assuming you have a list of nodes representing vertices
        Node fromNode = node_lists.stream()
                .filter(node -> node.getName().equals(fromVertex))
                .findFirst()
                .orElse(null);
        Node toNode = node_lists.stream()
                .filter(node -> node.getName().equals(toVertex))
                .findFirst()
                .orElse(null);
        fromNode.addAdjacentNode(toNode, weightValue);
        // Create a line between the centers of the two nodes
        double fromX = fromNode.getCenterX();
        double fromY = fromNode.getCenterY();
        double toX = toNode.getCenterX();
        double toY = toNode.getCenterY();

        double angle = Math.atan2(toY - fromY, toX - fromX);

        double fromNodeRadius = fromNode.getRadius(); // Assuming the nodes are circles
        double toNodeRadius = toNode.getRadius();

        double fromNodeBoundaryX = fromX + fromNodeRadius * Math.cos(angle);
        double fromNodeBoundaryY = fromY + fromNodeRadius * Math.sin(angle);

        double toNodeBoundaryX = toX - toNodeRadius * Math.cos(angle);
        double toNodeBoundaryY = toY - toNodeRadius * Math.sin(angle);

        Line edge = new Line(fromNodeBoundaryX, fromNodeBoundaryY, toNodeBoundaryX, toNodeBoundaryY);

        // Add the line to the graph
        graph.getChildren().add(edge);
    }


    @FXML
    void cretateGraphPressed(ActionEvent event) {


    }

    @FXML
    void findPressed(ActionEvent event) {
        String startVertexValue = startVertex.getValue();

        if (startVertexValue != null) {

            Node startNode = node_lists.stream()
                    .filter(node -> node.getName().equals(startVertexValue))
                    .findFirst()
                    .orElse(null);
            Dijkstra dijkstra = new Dijkstra();
            dijkstra.calculateShortestPath(startNode);
            dijkstra.printPaths(node_lists);

        }

    }






}
