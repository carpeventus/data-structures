package Chap7;


import java.util.*;
/**
 * 即时版本的Prim最小生成树算法
 */
public class Prim {
    private boolean marked[];
    private Edge[] edgeTo;
    private double[] distTo;
    private Map<Integer, Double> minDist;


    public Prim(EdgeWeightedGraph<?> graph) {
        marked = new boolean[graph.vertexNum()];
        edgeTo = new  Edge[graph.vertexNum()];
        distTo = new double[graph.vertexNum()];
        minDist = new HashMap<>();
        // 初始化distTo，distTo[0]不会被赋值，默认0.0正好符合我们的要求，使余下的每个值都为正无穷,
        for (int i = 0; i < graph.vertexNum(); i++) {
            distTo[i] = Double.POSITIVE_INFINITY; // 1.0 / 0.0为INFINITY
        }
        distTo[0] = 0.0;
        visit(graph, 0);
        while (!minDist.isEmpty()) {
            visit(graph, delMin());
        }
    }

    private int delMin() {
        Set<Map.Entry<Integer, Double>> entries = minDist.entrySet();
        Map.Entry<Integer, Double> min = entries.stream().min(Comparator.comparing(Map.Entry::getValue)).get();
        int key = min.getKey();
        minDist.remove(key);
        return key;
    }

    private void visit(EdgeWeightedGraph<?> graph, int v) {
        marked[v] = true;
        for (Edge e: graph.adj(v)) {
            int w = e.other(v);
            if (marked[w]) {
                continue;
            }

            if (e.weight() < distTo[w]) {
                distTo[w] = e.weight();
                edgeTo[w] = e;
                if (minDist.containsKey(w)) {
                    minDist.replace(w, distTo[w]);
                } else {
                    minDist.put(w, distTo[w]);
                }
            }
        }
    }

    public Iterable<Edge> edges() {
        List<Edge> edges = new ArrayList<>();
        edges.addAll(Arrays.asList(edgeTo).subList(1, edgeTo.length));
        return edges;
    }

    public double weight() {
        return Arrays.stream(distTo).reduce(0.0, Double::sum);
    }

    public static void main(String[] args) {
        List<String> vertexInfo = Arrays.asList("v0", "v1", "v2", "v3", "v4", " v5", "v6", "v7");
        int[][] edges = {{4, 5}, {4, 7}, {5, 7}, {0, 7},
                {1, 5}, {0, 4}, {2, 3}, {1, 7}, {0, 2}, {1, 2},
                {1, 3}, {2, 7}, {6, 2}, {3, 6}, {6, 0}, {6, 4}};

        double[] weight = {0.35, 0.37, 0.28, 0.16, 0.32, 0.38, 0.17, 0.19,
                0.26, 0.36, 0.29, 0.34, 0.40, 0.52, 0.58, 0.93};

        EdgeWeightedGraph<String> graph = new EdgeWeightedGraph<>(vertexInfo, edges, weight);
        Prim prim = new Prim(graph);
        System.out.println("MST的所有边为：" + prim.edges());
        System.out.println("最小成本和为：" + prim.weight());
    }
}
