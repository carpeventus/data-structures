package Chap7;

import java.util.LinkedList;

/**
 * 寻找有向图中的环
 */
public class DiCycle {
    private boolean[] marked;
    private int[] edgeTo;
    private boolean[] onStack;
    private LinkedList<Integer> cycle;

    public DiCycle(DiGraph<?> graph) {
        marked = new boolean[graph.vertexNum()];
        edgeTo = new int[graph.vertexNum()];
        onStack = new boolean[graph.vertexNum()];
        // 有向图可能不是强连通的，所以需要从每个顶点出发，寻找负权环
        for (int i = 0; i < graph.vertexNum(); i++) {
            dfs(graph, i);
        }
    }

    public DiCycle(EdgeWeightedDiGraph<?> graph) {
        marked = new boolean[graph.vertexNum()];
        edgeTo = new int[graph.vertexNum()];
        onStack = new boolean[graph.vertexNum()];
        // 有向图可能不是强连通的，所以需要从每个顶点出发，寻找负权环
        for (int i = 0; i < graph.vertexNum(); i++) {
            dfs(graph, i);
        }
    }

    private void dfs(DiGraph<?> graph, int v) {
        // 模拟系统递归使用的栈，方法开始进栈；方法结束出栈
        onStack[v] = true;
        marked[v] = true;
        for (int w : graph.adj(v)) {
            // 如果已经存在环，终止递归方法
            if (hasCycle()) {
                return;
            }
            if (!marked[w]) {
                edgeTo[w] = v;
                dfs(graph, w);
                // v -> w的路径，且w在栈中，说明形成有向环
            } else if (onStack[w]) {
                cycle = new LinkedList<>();
                for (int x = v; x != w; x = edgeTo[x]) {
                    cycle.push(x);
                }
                // 导致成环的连个顶点入栈
                cycle.push(w);
                cycle.push(v);
            }
        }
        onStack[v] = false;
    }

    private void dfs(EdgeWeightedDiGraph<?> graph, int v) {
        // 模拟系统递归使用的栈，方法开始进栈；方法结束出栈
        onStack[v] = true;
        marked[v] = true;
        for (DiEdge edge : graph.adj(v)) {
            // 如果已经存在环，终止递归方法
            if (hasCycle()) {
                return;
            }
            int w = edge.to();
            if (!marked[w]) {
                edgeTo[w] = v;
                dfs(graph, w);
                // v -> w的路径，且w在栈中，说明形成有向环
            } else if (onStack[w]) {
                cycle = new LinkedList<>();
                for (int x = v; x != w; x = edgeTo[x]) {
                    cycle.push(x);
                }
                // 导致成环的连个顶点入栈
                cycle.push(w);
                cycle.push(v);
            }
        }
        onStack[v] = false;
    }

    public boolean hasCycle() {
        return cycle != null;
    }

    public Iterable<Integer> cycle() {
        return cycle;
    }
}
