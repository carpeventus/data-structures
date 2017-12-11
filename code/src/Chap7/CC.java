package Chap7;

import java.util.LinkedList;

/**
 * 寻找无向图中的连通分量
 */
public class CC {
    // 用来标记已经访问过的顶点，保证每个顶点值访问一次
    private boolean[] marked;
    // 为每个连通分量标示一个id
    private int[] id;
    // 连通分量的个数
    private int count;

    public CC(UndiGraph<?> graph) {
        marked = new boolean[graph.vertexNum()];
        id = new int[graph.vertexNum()];
        for (int s = 0; s < graph.vertexNum(); s++) {
            if (!marked[s]) {
                dfs(graph, s);
                // 一次dfs调用就是一个连通分量，第一个连通分量id为0。
                // 之后分配的id要自增，第二个连通分量的id为1，以此类推
                count++;
            }
        }
    }

    private void dfs(UndiGraph<?> graph, int v) {
        // 将刚访问到的顶点设置标志
        marked[v] = true;
        id[v] = count;
        // 从v的所有邻接点中选择一个没有被访问过的顶点
        for (int w : graph.adj(v)) {
            if (!marked[w]) {
                dfs(graph, w);
            }
        }
    }

    public boolean connected(int v, int w) {
        return id[v] == id[w];
    }

    public int id(int v) {
        return id[v];
    }

    public int count() {
        return count;
    }

    public static void main(String[] args) {
        // 边
        int[][] edges = {{0, 6}, {0, 2}, {0, 1}, {0, 5},
                {3, 4}, {3, 5}, {4, 5}, {4, 6}, {7, 8},
                {9, 10}, {9, 11}, {9, 12}, {11, 12}};

        UndiGraph<?> graph = new UndiGraph<>(13, edges);
        CC cc = new CC(graph);
        // M是连通分量的个数
        int M = cc.count();
        System.out.println(M + "个连通分量");
        LinkedList<Integer>[] components = (LinkedList<Integer>[]) new LinkedList[M];
        for (int i = 0; i < M; i++) {
            components[i] = new LinkedList<>();
        }
        // 将同一个id的顶点归属到同一个链表中
        for (int v = 0; v < graph.vertexNum(); v++) {
            components[cc.id(v)].add(v);
        }
        // 打印每个连通分量中的顶点
        for (int i = 0; i < M; i++) {
            for (int v : components[i]) {
                System.out.print(v+ " ");
            }
            System.out.println();
        }
    }

}
