package Chap7;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Floyd最短路径算法
 */
public class Floyd {
    private double[][] dist;
    private int[][] edge;

    public Floyd(EdgeWeightedDiGraph<?> graph) {
        dist = new double[graph.vertexNum()][graph.vertexNum()];
        edge = new int[graph.vertexNum()][graph.vertexNum()];
        // 将邻接表变成了邻接矩阵
        for (int i = 0; i < dist.length; i++) {
            for (int j = 0; j < dist.length; j++) {
                // 赋值给
                edge[i][j] = i;
                if (i == j) {
                    dist[i][j] = 0.0;
                } else {
                    dist[i][j] = Double.POSITIVE_INFINITY;
                }
            }
        }

        for (int v = 0; v < graph.vertexNum(); v++) {
            for (DiEdge edge : graph.adj(v)) {
                int w = edge.to();
                dist[v][w] = edge.weight();
            }
        }

        for (int k = 0; k < graph.vertexNum(); k++) {
            for (int v = 0; v < dist.length; v++) {
                for (int w = 0; w < dist.length; w++) {
                    if (dist[v][k] + dist[k][w] < dist[v][w]) {
                        dist[v][w] = dist[v][k] + dist[k][w];
                        edge[v][w] = edge[k][w];
                    }
                }
            }
        }
    }

    public boolean hasPathTo(int s, int v) {
        return dist[s][v] != Double.POSITIVE_INFINITY;
    }

    public Iterable<Integer> pathTo(int s, int v) {
        if (hasPathTo(s, v)) {
            LinkedList<Integer> path = new LinkedList<>();
            for (int i = v; i != s; i = edge[s][i]) {
                path.push(i);
            }
            // 起点要加入
            path.push(s);
            return path;
        }

        return null;
    }

    public double distTo(int s, int w) {
        return dist[s][w];
    }

    public static void main(String[] args) {
//        List<String> vertexInfo = List.of("v0", "v1", "v2", "v3", "v4", "v5", "v6", "v7");
//        int[][] edges = {{4, 5}, {5, 4}, {4, 7}, {5, 7}, {7, 5}, {5, 1}, {0, 4}, {0, 2},
//                {7, 3}, {1, 3}, {2, 7}, {6, 2}, {3, 6}, {6, 0}, {6, 4}};
//
//        double[] weight = {0.35, 0.35, 0.37, 0.28, 0.28, 0.32, 0.38, 0.26, 0.39, 0.29,
//                0.34, 0.40, 0.52, 0.58, 0.93};
        List<String> vertexInfo = List.of("v0", "v1", "v2", "v3");
        int[][] edges = {{0, 1}, {1, 3}, {1, 2}, {3, 2}};
        double[] weight = {1, 2, 2, 3};
        EdgeWeightedDiGraph<String> graph = new EdgeWeightedDiGraph<>(vertexInfo, edges, weight);
        Floyd floyd = new Floyd(graph);
        for (int[] a: floyd.edge) {
            System.out.println(Arrays.toString(a));
        }
        for (int s = 0; s < graph.vertexNum(); s++) {
            for (int w = 0; w < graph.vertexNum(); w++) {
                System.out.print(s + " to " + w + ": ");
                System.out.print("(" + floyd.distTo(s, w) + ") ");
                System.out.println(floyd.pathTo(s, w));
            }
            System.out.println();
        }
    }
}
