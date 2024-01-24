package com.example.dijkstra;

public class Edge {

    private final Node from;
    private final Node to;
    private double weight = 1; // đặt độ dài cạnh ban đầu là 1

    public Edge(Node one, Node two) {
        this.from = one;
        this.to = two;
    }

    public Node getNodeFrom() {
        return from;
    }

    public Node getNodeTo() {
        return to;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public boolean hasNode(Node node) {
        return from == node || to == node;
    }

    public boolean equals(Edge edge) {
        return (from == edge.from && to == edge.to) || (from == edge.to && to == edge.from);
    } // kiểm tra xem cạnh định vẽ đã tồn tại chưa, nếu đã tồn tại thì khong vẽ nữa

    @Override
    public String toString() {
        return "Edge : "
                + getNodeFrom().getId() + " - "
                + getNodeTo().getId();
    }

}
