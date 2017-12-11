package Chap7;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 加权有向图
 */
public class EdgeWeightedDiGraph<Item> {
    private int vertexNum;
    private int edgeNum;
    // 邻接表
    private List<List<DiEdge>> adj;
    // 顶点信息
    private List<Item> vertexInfo;

    public EdgeWeightedDiGraph(List<Item> vertexInfo) {
        this.vertexInfo = vertexInfo;
        this.vertexNum = vertexInfo.size();
        adj = new ArrayList<>();
        for (int i = 0; i < vertexNum; i++) {
            adj.add(new LinkedList<>());
        }
    }


    public EdgeWeightedDiGraph(List<Item> vertexInfo, int[][] edges, double[] weight) {
        this(vertexInfo);
        for (int i = 0; i < edges.length; i++) {
            DiEdge edge = new DiEdge(edges[i][0], edges[i][1], weight[i]);
            addDiEdge(edge);
        }
    }

    public EdgeWeightedDiGraph(int vertexNum) {
        this.vertexNum = vertexNum;
        adj = new ArrayList<>();
        for (int i = 0; i < vertexNum; i++) {
            adj.add(new LinkedList<>());
        }
    }

    public EdgeWeightedDiGraph(int vertexNum, int[][] edges, double[] weight) {
        this(vertexNum);
        for (int i = 0; i < edges.length; i++) {
            DiEdge edge = new DiEdge(edges[i][0], edges[i][1], weight[i]);
            addDiEdge(edge);
        }
    }

    public void addDiEdge(DiEdge edge) {
        adj.get(edge.from()).add(edge);
        edgeNum++;
    }

    // 返回与某个顶点依附的所有边
    public Iterable<DiEdge> adj(int v) {
        return adj.get(v);
    }

    public List<DiEdge> edges() {
        List<DiEdge> edges = new LinkedList<>();
        for (int i = 0; i < vertexNum; i++) {
            for (DiEdge e : adj(i)) {
                edges.add(e);
            }
        }
        return edges;
    }

    public int vertexNum() {
        return vertexNum;
    }

    public int edgeNum() {
        return edgeNum;
    }

    public Item getVertexInfo(int i) {
        return vertexInfo.get(i);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(vertexNum).append("个顶点, ").append(edgeNum).append("条边。\n");
        for (int i = 0; i < vertexNum; i++) {
            sb.append(i).append(": ").append(adj.get(i)).append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        List<String> vertexInfo = List.of("v0", "v1", "v2", "v3", "v4", "v5", "v6", "v7");
        int[][] edges = {{4, 5}, {5, 4}, {4, 7}, {5, 7}, {7, 5}, {5, 1}, {0, 4}, {0, 2},
                {7, 3}, {1, 3}, {2, 7}, {6, 2}, {3, 6}, {6, 0}, {6, 4}};

        double[] weight = {0.35, 0.35, 0.37, 0.28, 0.28, 0.32, 0.38, 0.26, 0.39, 0.29,
                0.34, 0.40, 0.52, 0.58, 0.93};

        EdgeWeightedDiGraph<String> graph = new EdgeWeightedDiGraph<>(vertexInfo, edges, weight);

        System.out.println("该图的邻接表为\n"+graph);
        System.out.println("该图的所有边："+ graph.edges());

    }
}
