package Chap7;

import java.util.LinkedList;

/**
 * 寻找负权重的有向环
 */
public class NegativeDiCycle {
    private boolean[] marked;
    private DiEdge[] edgeTo;
    private boolean[] onStack;
    private LinkedList<DiEdge> cycle;

    public NegativeDiCycle(EdgeWeightedDiGraph<?> graph) {
        marked = new boolean[graph.vertexNum()];
        edgeTo = new DiEdge[graph.vertexNum()];
        onStack = new boolean[graph.vertexNum()];
        // 有向图可能不是强连通的，所以需要从每个顶点出发，寻找负权环
        for (int i = 0; i < graph.vertexNum(); i++) {
            dfs(graph, i);
        }
    }

    private void dfs(EdgeWeightedDiGraph<?> graph, int v) {
        // 模拟系统递归使用的栈，方法开始进栈；方法结束出栈
        onStack[v] = true;
        marked[v] = true;
        for (DiEdge edge : graph.adj(v)) {
            // 如果已经存在负权回路，终止递归方法
            if (this.hasNegativeDiCycle()) {
                return;
            }

            int w = edge.to();
            if (!marked[w]) {
                edgeTo[w] = edge;
                dfs(graph, w);
                // v -> w的路径，且w在栈中，说明形成有向环
            } else if (onStack[w]) {
                cycle = new LinkedList<>();

                DiEdge e = edgeTo[v];
                while (e.from() != w) {
                    cycle.push(e);
                    e = edgeTo[e.from()];
                }
                // 为避免空指针，离w最近的那条在循环外入栈
                cycle.push(e);
                // 把导致成环的边加入
                cycle.push(edge);
            }
        }
        onStack[v] = false;
    }

    public boolean hasNegativeDiCycle() {
        if (cycle != null) {
            double cycleWeight = cycle.stream().mapToDouble(DiEdge::weight).sum();
            if (cycleWeight < 0) {
                return true;
            }
        }
        return false;
    }

    public Iterable<DiEdge> cycle() {
        if (hasNegativeDiCycle()) {
            return cycle;
        }
        return null;
    }

}
