package asundukov.collibra.engine.graph.impl;

import asundukov.collibra.engine.graph.Graph;

public class GraphSynchronized implements Graph {

    private final Graph graph;

    public GraphSynchronized(Graph graph) {
        this.graph = graph;
    }

    @Override
    public synchronized void add(String nodeId) {
        graph.add(nodeId);
    }

    @Override
    public synchronized void remove(String nodeId) {
        graph.remove(nodeId);
    }

    @Override
    public synchronized void addEdge(String from, String to, int weight) {
        graph.addEdge(from, to, weight);
    }

    @Override
    public synchronized void removeEdge(String from, String to) {
        graph.removeEdge(from, to);
    }

}
