package Chap7;

import java.util.*;

/**
 * Kruskal最小生成树算法
 */
public class Kruskal {
    private Queue<Edge> mst;
    public Kruskal(EdgeWeightedGraph<?> graph) {
        mst = new LinkedList<>();
        Queue<Edge> edges = new PriorityQueue<>(graph.edges());
        UnionFind uf = new UnionFind(graph.vertexNum());
        // 只要mst边小于n-1就进入循环，在循环内添加一条后就变成了n-1条
        while (!edges.isEmpty() && mst.size() < graph.vertexNum() -1) {
            // 优先队列，删除最小权值的边
            Edge edge = edges.poll();
            int v = edge.either();
            int w = edge.other(v);

            if (uf.isConnected(v, w)) {
                continue;
            }
            uf.union(v, w);
            mst.offer(edge);
        }
    }

    public Iterable<Edge> edges() {
        return mst;
    }

    public double weight() {
        return mst.stream().mapToDouble(Edge::weight).sum();
    }

    public static void main(String[] args) {
        List<String> vertexInfo = Arrays.asList("v0", "v1", "v2", "v3", "v4", " v5", "v6", "v7");
        int[][] edges = {{4, 5}, {4, 7}, {5, 7}, {0, 7},
                {1, 5}, {0, 4}, {2, 3}, {1, 7}, {0, 2}, {1, 2},
                {1, 3}, {2, 7}, {6, 2}, {3, 6}, {6, 0}, {6, 4}};

        double[] weight = {0.35, 0.37, 0.28, 0.16, 0.32, 0.38, 0.17, 0.19,
                0.26, 0.36, 0.29, 0.34, 0.40, 0.52, 0.58, 0.93};

        EdgeWeightedGraph<String> graph = new EdgeWeightedGraph<>(vertexInfo, edges, weight);
        Kruskal kruskal = new Kruskal(graph);
        System.out.println("MST的所有边为：" + kruskal.edges());
        System.out.println("最小成本和为：" + kruskal.weight());
    }
}
