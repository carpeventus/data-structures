package Chap7;

import java.util.*;

/**
 * SPFA算法,Bellman-Ford算法的优化,基于队列
 */
public class SPFA {
    private double[] distTo;
    private DiEdge[] edgeTo;
    private Queue<Integer> queue;
    private boolean[] onPQ;  // 顶点是否在queue中
    private int cost; // 记录放松了边的次数
    private Iterable<DiEdge> cycle; // 找到的负权回路

    public boolean hasNegativeCycle() {
        return cycle != null;
    }

    private void findNegativeCycle() {

        EdgeWeightedDiGraph<String> g = new EdgeWeightedDiGraph<>(edgeTo.length);
        for (int v = 0; v < edgeTo.length; v++) {
            if (edgeTo[v] != null) {
                g.addDiEdge(edgeTo[v]);
            }
        }
        NegativeDiCycle cycleFinder = new NegativeDiCycle(g);
        if (cycleFinder.hasNegativeDiCycle()) {
            cycle = cycleFinder.cycle();
        }
    }

    private void relax(EdgeWeightedDiGraph<?> graph, int v) {
        for (DiEdge edge : graph.adj(v)) {
            int w = edge.to();
            if (distTo[v] + edge.weight() < distTo[w]) {
                distTo[w] = distTo[v] + edge.weight();
                edgeTo[w] = edge;
                if (!onPQ[w]) {
                    queue.offer(w);
                    onPQ[w] = true;
                }
            }
            // 每调放松一条边cost自增；每放松了graph.vertexNum条边，就检查是否有负权回路
            if (cost++ % graph.vertexNum() == 0) {
                findNegativeCycle();
            }
        }
    }

    public SPFA(EdgeWeightedDiGraph<?> graph, int s) {
        distTo = new double[graph.vertexNum()];
        edgeTo = new DiEdge[graph.vertexNum()];
        queue = new LinkedList<>();
        onPQ = new boolean[graph.vertexNum()];

        for (int i = 0; i < graph.vertexNum(); i++) {
            distTo[i] = Double.POSITIVE_INFINITY; // 1.0 / 0.0为INFINITY
        }

        distTo[s] = 0.0;
        // 以上都是初始化
        queue.offer(s);
        onPQ[s] = true;

        while (!queue.isEmpty() && !hasNegativeCycle()) {
            int v = queue.poll();
            onPQ[v] = false;
            relax(graph, v);
        }
    }

    public Iterable<DiEdge> cycle() {
        return cycle;
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
//
        SPFA spfa = new SPFA(graph, 0);
        if (spfa.hasNegativeCycle()) {
            System.out.print("存在负权环：");
            System.out.println(spfa.cycle());
        }
//        SPFA[] all = new SPFA[graph.vertexNum()];
//
//        for (int i = 0; i < all.length; i++) {
//            all[i] = new SPFA(graph, i);
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
