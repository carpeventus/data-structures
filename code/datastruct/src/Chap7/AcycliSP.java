package Chap7;

import java.util.LinkedList;

/**
 * 无环加权有向图的最短路径算法
 */
public class AcycliSP {
    private DiEdge[] edgeTo;
    private double[] distTo;

    public AcycliSP(EdgeWeightedDiGraph<?> graph, int s) {
        edgeTo = new DiEdge[graph.vertexNum()];
        distTo = new double[graph.vertexNum()];

        for (int i = 0; i < graph.vertexNum(); i++) {
            distTo[i] = Double.POSITIVE_INFINITY; // 1.0 / 0.0为INFINITY
        }

        distTo[s] = 0.0;
        // 以上是初始化
        TopoSort topo = new TopoSort(graph);

        if (!topo.isDAG()) {
            throw new RuntimeException("该图存在有向环，本算法无法处理！");
        }

        for (int v : topo.order()) {
            relax(graph, v);
        }
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
        int[][] edges = {{5, 4}, {4, 7}, {5, 7}, {5, 1}, {4, 0}, {0, 2},
                {3, 7}, {1, 3}, {7, 2}, {6, 2}, {3, 6}, {6, 0}, {6, 4}};

        double[] weight = {0.35, 0.37, 0.28, 0.32, 0.38, 0.26, 0.39, 0.29,
                0.34, 0.40, 0.52, 0.58, 0.93};

        EdgeWeightedDiGraph<String> graph = new EdgeWeightedDiGraph<>(8, edges, weight);
        AcycliSP acycliSP = new AcycliSP(graph, 5);
            for (int i = 0; i < graph.vertexNum(); i++) {
                System.out.print(5 + " to " + i + ": ");
                System.out.print("(" + acycliSP.distTo(i) + ") ");
                System.out.println(acycliSP.pathTo(i));
            }
            System.out.println();
    }
}
/*
5 to 0: (0.73) [(5->4 0.35), (4->0 0.38)]
5 to 1: (0.32) [(5->1 0.32)]
5 to 2: (0.6200000000000001) [(5->7 0.28), (7->2 0.34)]
5 to 3: (0.61) [(5->1 0.32), (1->3 0.29)]
5 to 4: (0.35) [(5->4 0.35)]
5 to 5: (0.0) []
5 to 6: (1.13) [(5->1 0.32), (1->3 0.29), (3->6 0.52)]
5 to 7: (0.28) [(5->7 0.28)]

* */
