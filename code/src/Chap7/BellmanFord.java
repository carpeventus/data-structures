package Chap7;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * BellmanFord最短路径算法
 */
public class BellmanFord {
    private boolean hasNegativeCycle;
    private double distTo[];
    private DiEdge[] edgeTo;

    public boolean hasNegativeCycle() {
        return hasNegativeCycle;
    }

    private void relax(EdgeWeightedDiGraph<?> graph, int v) {
        for (DiEdge edge : graph.adj(v)) {
            int w = edge.to();
            if (distTo[v] + edge.weight() < distTo[w]) {
                distTo[w] = distTo[v] + edge.weight();
                edgeTo[w] = edge;
            }
        }
    }

    public BellmanFord(EdgeWeightedDiGraph<?> graph, int s) {
        distTo = new double[graph.vertexNum()];
        edgeTo = new DiEdge[graph.vertexNum()];
        for (int i = 0; i < graph.vertexNum(); i++) {
            distTo[i] = Double.POSITIVE_INFINITY; // 1.0 / 0.0为INFINITY
        }

        distTo[s] = 0.0;
        // 以上都是初始化

        for (int pass = 0; pass < graph.vertexNum(); pass++) {
            for (int v = 0; v < graph.vertexNum(); v++) {
                relax(graph, v);
            }
        }

        // 上面即使有负权回路也不会陷入死循环，因为给定了循环范围，算法必然终止。
        // 进行V轮边的松弛后，如果没有负权回路，那么所有的distTo[v] + edge.weight() >= distTo[w]
        // 如果对于图中任意边，仍然存在distTo[v] + edge.weight() < distTo[w]，则存在负权回路
        for (int v = 0; v < graph.vertexNum(); v++) {
            for (DiEdge edge : graph.adj(v)) {
                int w = edge.to();
                if (distTo[v] + edge.weight() < distTo[w]) {
                    hasNegativeCycle = true;
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
//        List<String> vertexInfo = List.of("v0", "v1", "v2", "v3", "v4", "v5", "v6", "v7");
//        int[][] edges = {{4, 5}, {5, 4}, {4, 7}, {5, 7}, {7, 5}, {5, 1}, {0, 4}, {0, 2},
//                {7, 3}, {1, 3}, {2, 7}, {6, 2}, {3, 6}, {6, 0}, {6, 4}};
//
//        double[] weight = {0.35, 0.35, 0.37, 0.28, 0.28, 0.32, 0.38, 0.26, 0.39, 0.29,
//                0.34, 0.40, 0.52, 0.58, 0.93};
        List<String> vertexInfo = List.of("v0", "v1", "v2", "v3");
        int[][] edges = {{0, 1}, {1, 2}, {2, 0}, {0, 3}, {2, 3}};

        double[] weight = {-9 , 5, 2, 4, 6};
        EdgeWeightedDiGraph<String> graph = new EdgeWeightedDiGraph<>(vertexInfo, edges, weight);
        BellmanFord bellmanFord = new BellmanFord(graph, 0);
        System.out.println(Arrays.toString(bellmanFord.edgeTo));
        if (bellmanFord.hasNegativeCycle()) {
            System.out.println("路径中存在负权环！");
        }
//        BellmanFord[] all = new BellmanFord[graph.vertexNum()];
//
//        for (int i = 0; i < all.length; i++) {
//            all[i] = new BellmanFord(graph, i);
//        }
//
//        for (int s = 0; s < all.length; s++) {
//            for (int i = 0; i < graph.vertexNum(); i++) {
//                System.out.print(s + " to " + i + ": ");
//                System.out.print("(" + all[s].distTo(i) + ") ");
//                System.out.println(all[s].pathTo(i));
//            }
//            System.out.println();
//        }
    }
}
