package Chap7;

import java.util.*;

/**
 * Dijkstra最短路径算法
 */
public class Dijkstra {
    private DiEdge[] edgeTo;
    private double[] distTo;
    private Map<Integer, Double> minDist;

    public Dijkstra(EdgeWeightedDiGraph<?> graph, int s) {
        edgeTo = new DiEdge[graph.vertexNum()];
        distTo = new double[graph.vertexNum()];
        minDist = new HashMap<>();

        for (int i = 0; i < graph.vertexNum(); i++) {
            distTo[i] = Double.POSITIVE_INFINITY; // 1.0 / 0.0为INFINITY
        }

        distTo[s] = 0.0;
        relax(graph, s);
        while (!minDist.isEmpty()) {
            relax(graph, delMin());
        }
    }

    private int delMin() {
        Set<Map.Entry<Integer, Double>> entries = minDist.entrySet();
        Map.Entry<Integer, Double> min = entries.stream().min(Comparator.comparing(Map.Entry::getValue)).get();
        int key = min.getKey();
        minDist.remove(key);
        return key;
    }

    private void relax(EdgeWeightedDiGraph<?> graph, int v) {
        for (DiEdge edge : graph.adj(v)) {
            int w = edge.to();
            if (distTo[v] + edge.weight() < distTo[w]) {
                distTo[w] = distTo[v] + edge.weight();
                edgeTo[w] = edge;
                if (minDist.containsKey(w)) {
                    minDist.replace(w, distTo[w]);
                } else {
                    minDist.put(w, distTo[w]);
                }
            }
        }
    }

    public double distTo(int v) {
        return distTo[v];
    }

    public boolean hasPathTo(int v) {
        return distTo[v] != Double.POSITIVE_INFINITY;
    }

    public Iterable<DiEdge> pathTo(int v) {
        if (hasPathTo(v)) {
            LinkedList<DiEdge> path = new LinkedList<>();
            for (DiEdge edge = edgeTo[v]; edge != null; edge = edgeTo[edge.from()]) {
                path.push(edge);
            }
            return path;
        }
        return null;
    }

    public static void main(String[] args) {
        List<String> vertexInfo = List.of("v0", "v1", "v2", "v3", "v4", "v5", "v6", "v7");
        int[][] edges = {{4, 5}, {5, 4}, {4, 7}, {5, 7}, {7, 5}, {5, 1}, {0, 4}, {0, 2},
                {7, 3}, {1, 3}, {2, 7}, {6, 2}, {3, 6}, {6, 0}, {6, 4}};

        double[] weight = {0.35, 0.35, 0.37, 0.28, 0.28, 0.32, 0.38, 0.26, 0.39, 0.29,
                0.34, 0.40, 0.52, 0.58, 0.93};
        EdgeWeightedDiGraph<String> graph = new EdgeWeightedDiGraph<>(vertexInfo, edges, weight);
        Dijkstra[] all = new Dijkstra[graph.vertexNum()];

        for (int i = 0; i < all.length; i++) {
            all[i] = new Dijkstra(graph, i);
        }

        for (int s = 0; s < all.length; s++) {
            for (int i = 0; i < graph.vertexNum(); i++) {
                System.out.print(s + " to " + i + ": ");
                System.out.print("(" + all[s].distTo(i) + ") ");
                System.out.println(all[s].pathTo(i));
            }
            System.out.println();
        }
    }
}