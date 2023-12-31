package com.example.dijkstra;


public class Edge {
    private Node from;
    private Node to;
    private int weight = 1;

    public Edge(Node from, Node to){
        this.from = from;
        this.to = to;
    }// 2 đầu của cạnh

    public Node getNodeFrom(){
        return from;
    }

    public Node getNodeTo(){
        return to;
    }

    public void setWeight(int weight){
        this.weight = weight;
    }

    public int getWeight(){
        return weight;
    }

    public boolean hasNode(Node node){
        return from == node || to ==node;
    }

    public boolean equals(Edge edge) {
        return (from ==edge.from && to ==edge.to) || (from ==edge.to && to ==edge.from) ;
    }

    @Override
    public String toString() {
        return "Edge ~ "
                + getNodeFrom().getId() + " - "
                + getNodeTo().getId();
    }
}
