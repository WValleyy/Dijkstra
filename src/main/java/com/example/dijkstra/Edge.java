package com.example.dijkstra;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Edge {
    private final BooleanProperty highlightedd = new SimpleBooleanProperty(false);
    private Node from;
    private Node to;
    private int weight = 1;
    private boolean highlighted;

    public Edge(Node one, Node two){
        this.from = one;
        this.to = two;
    }

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
    public void setHighlighted(boolean highlighted){
        this.highlighted=highlighted;
        this.highlightedd.set(highlighted);
    }
    public boolean getHighlighted(){
        return highlighted;
    }
    // highlightProperty
    public BooleanProperty highlightedProperty() {
        return highlightedd;
    }
}
