package Chap7;

import java.util.LinkedList;

/**
 * 求无环有向图的最长路径
 */
public class AcycliLP {
    private DiEdge[] edgeTo;
    private double[] distTo;

    public AcycliLP(EdgeWeightedDiGraph<?> graph, int s) {
        edgeTo = new DiEdge[graph.vertexNum()];
        distTo = new double[graph.vertexNum()];

        for (int i = 0; i < graph.vertexNum(); i++) {
            // 1. 改成了负无穷
            distTo[i] = Double.NEGATIVE_INFINITY; // -1.0 / 0.0为INFINITY
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
            // 2、若路径更长久更新
            if (distTo[v] + edge.weight() > distTo[w]) {
                distTo[w] = distTo[v] + edge.weight();
                edgeTo[w] = edge;
            }
        }
    }

    public double distTo(int v) {
        return distTo[v];
    }

    public boolean hasPathTo(int v) {
        return distTo[v] != Double.NEGATIVE_INFINITY;
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
}
