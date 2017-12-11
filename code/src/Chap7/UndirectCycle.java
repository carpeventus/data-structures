package Chap7;

/**
 * 判断无向图是否有环
 */
public class UndirectCycle {
    private boolean marked[];
    private boolean hasCycle;

    public UndirectCycle(UndiGraph<?> graph) {
        marked = new boolean[graph.vertexNum()];
        for (int s = 0; s < graph.vertexNum(); s++) {
            if (!marked[s]) {
                dfs(graph, s, s);
            }
        }
    }

    private void dfs(UndiGraph<?> graph, int v, int u) {
        // 将刚访问到的顶点设置标志
        marked[v] = true;
        // 从v的所有邻接点中选择一个没有被访问过的顶点
        for (int w : graph.adj(v)) {
            if (!marked[w]) {
                dfs(graph, w, v);
            } else if (w != u) {
                hasCycle = true;
            }
        }
    }

    public boolean hasCycle() {
        return hasCycle;
    }
}
