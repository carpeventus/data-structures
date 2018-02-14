package Chap7;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * DFS深度优先搜索-针对无向图
 */
public class UndiDFS {
    // 用来标记已经访问过的顶点，保证每个顶点值访问一次
    private boolean[] marked;
    // 起点
    private final int s;
    // 到该顶点的路径上的最后一条边
    private int[] edgeTo;

    public UndiDFS(UndiGraph<?> graph, int s) {
        this.s = s;
        marked = new boolean[graph.vertexNum()];
        edgeTo = new int[graph.vertexNum()];
        dfs(graph, s);
    }

    private void dfs(UndiGraph<?> graph, int v) {
        // 将刚访问到的顶点设置标志
        marked[v] = true;
//        System.out.println(v);
        // 从v的所有邻接点中选择一个没有被访问过的顶点
        for (int w : graph.adj(v)) {
            if (!marked[w]) {
                edgeTo[w] = v;
                dfs(graph, w);
            }
        }
    }

    // 连通图的任意一个顶点都有某条路径能到达任意一个顶点，如果v在这个连通图中，必然存在起点到v的路径
    // 现在marked数组中的值都是true，所以数组中若有这个v（在这个连通图中）, 返回true就表示路径存在
    public boolean hasPathTo(int v) {
        return marked[v];
    }

    public Iterable<Integer> pathTo(int v) {
        if (hasPathTo(v)) {
            LinkedList<Integer> path = new LinkedList<>();
            for (int i = v; i != s; i = edgeTo[i]) {
                path.push(i);
            }
            // 最后将根结点压入
            path.push(s);
            return path;
        }
        // 到v不存在路径，就返回null
        return null;
    }

    public void printPathTo(int v) {
        System.out.print(s+" to "+ v+": ");

        if (hasPathTo(v)) {
            for (int i : pathTo(v)) {
                if (i == s) {
                    System.out.print(i);
                } else {
                    System.out.print("-" + i);
                }
            }
            System.out.println();
        } else {
            System.out.println("不存在路径！");
        }
    }

    public static void main(String[] args) {
        List<String> vertexInfo = Arrays.asList("v0", "v1", "v2", "v3", "v4", "v5");
        int[][] edges = {{3, 5},{0, 2}, {0, 1}, {0, 5},
                {1, 2}, {3, 4}, {2, 3}, {2, 4}};

        UndiGraph<String> graph = new UndiGraph<>(vertexInfo, edges);

        UndiDFS search = new UndiDFS(graph, 3);
        System.out.println(Arrays.toString(search.edgeTo));
        search.printPathTo(4);

    }
}
