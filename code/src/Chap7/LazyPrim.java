package Chap7;


import java.util.*;

/**
 * 延时版本的Prim最小生成树算法
 */
public class LazyPrim {
    private boolean marked[];
    Queue<Edge> edges;
    private Queue<Edge> mst;

    public LazyPrim(EdgeWeightedGraph<?> graph) {
        marked = new boolean[graph.vertexNum()];
        edges = new PriorityQueue<>();
        mst = new LinkedList<>();
        // 从顶点0开始访问
        visit(graph, 0);
        // 只要边还没被删除完，就循环
        while (!edges.isEmpty()) {
            // 优先队列,将权值最小的选出并删除
            Edge edge = edges.poll();
            int v = edge.either();
            int w = edge.other(v);
            // 这样的边会导致成环，跳过
            if (marked[v] && marked[w]) {
                continue;
            }
            // 加入到MST中
            mst.offer(edge);
            // 因为edges中的边肯定是有一个顶点已经visit过了，但是不知道是either还是other
            // 如果v没被标记，那么访问它；否则v被标记了，那么w肯定没被标记（marked[v] && marked[w]的情况已经被跳过了）
            if (!marked[v]) {
                visit(graph, v);
            } else {
                visit(graph, w);
            }
        }
    }

    private void visit(EdgeWeightedGraph<?> graph, int v) {
        marked[v] = true;
        for (Edge e : graph.adj(v)) {
            // v的邻接边中，将另一个顶点未被标记的边加入列表中。若另一个顶点标记了还加入，就会重复添加
            if (!marked[e.other(v)]) {
                edges.offer(e);
            }
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
        LazyPrim prim = new LazyPrim(graph);
        System.out.println("MST的所有边为：" + prim.edges());
        System.out.println("最小成本和为：" + prim.weight());
    }
}
