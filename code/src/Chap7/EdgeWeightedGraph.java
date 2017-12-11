package Chap7;

import java.util.*;

/**
 * 加权无向图
 */
public class EdgeWeightedGraph<Item> {

    private int vertexNum;
    private int edgeNum;
    // 邻接表
    private List<List<Edge>> adj;
    // 顶点信息
    private List<Item> vertexInfo;

    public EdgeWeightedGraph(List<Item> vertexInfo) {
        this.vertexInfo = vertexInfo;
        this.vertexNum = vertexInfo.size();
        adj = new ArrayList<>();
        for (int i = 0; i < vertexNum; i++) {
            adj.add(new LinkedList<>());
        }
    }

    public EdgeWeightedGraph(List<Item> vertexInfo, int[][] edges, double[] weight) {
        this(vertexInfo);
        for (int i = 0; i < edges.length;i++) {
            Edge edge = new Edge(edges[i][0], edges[i][1], weight[i]);
            addEdge(edge);
        }
    }

    public EdgeWeightedGraph(int vertexNum) {
        this.vertexNum = vertexNum;
        adj = new ArrayList<>();
        for (int i = 0; i < vertexNum; i++) {
            adj.add(new LinkedList<>());
        }
    }

    public EdgeWeightedGraph(int vertexNum, int[][] edges, double[] weight) {
        this(vertexNum);
        for (int i = 0; i < edges.length;i++) {
            Edge edge = new Edge(edges[i][0], edges[i][1], weight[i]);
            addEdge(edge);
        }
    }

    public void addEdge(Edge edge) {
        int v = edge.either();
        int w = edge.other(v);
        adj.get(v).add(edge);
        adj.get(w).add(edge);
        edgeNum++;
    }
    // 返回与某个顶点依附的所有边
    public Iterable<Edge> adj(int v) {
        return adj.get(v);
    }

    public List<Edge> edges() {
        List<Edge> edges = new LinkedList<>();
        for (int i = 0; i < vertexNum; i++) {
            for (Edge e: adj(i)) {
                // i肯定是边e的一个顶点，我们只取other大于i的边，避免添加重复的边
                // 比如adj(1)中的1-3边会被添加，但是adj(3)中的3-1就不会被添加
                if (e.other(i) > i) {
                    edges.add(e);
                }
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
        List<String> vertexInfo = Arrays.asList("v0", "v1", "v2", "v3", "v4");
        int[][] edges = {{0, 1}, {0, 2}, {0, 3},
                {1, 3}, {1, 4},
                {2, 4}};
        double[] weight = {30.0, 40.0, 20.5, 10.0, 59.5, 20.0};

        EdgeWeightedGraph<String> graph = new EdgeWeightedGraph<>(vertexInfo, edges, weight);
        System.out.println("该图的邻接表为\n"+graph);
        System.out.println("该图的所有边："+ graph.edges());

    }
}
