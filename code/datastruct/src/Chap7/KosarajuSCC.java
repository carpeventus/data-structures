package Chap7;

import java.util.LinkedList;

/**
 * 寻找有向图中的强连通分量
 */
public class KosarajuSCC {
    // 用来标记已经访问过的顶点，保证每个顶点值访问一次
    private boolean[] marked;
    // 为每个连通分量标示一个id
    private int[] id;
    // 连通分量的个数
    private int count;

    public KosarajuSCC(DiGraph<?> graph) {
        marked = new boolean[graph.vertexNum()];
        id = new int[graph.vertexNum()];
        // 对原图G取反得到Gr
        DFSorder order = new DFSorder(graph.reverse());
        // 按Gr的逆后序进行dfs
        for (int s : order.reversePost()) {
            if (!marked[s]) {
                dfs(graph, s);
                // 一次dfs调用就是一个连通分量，第一个连通分量id为0。
                // 之后分配的id要自增，第二个连通分量的id为1，以此类推
                count++;
            }
        }
    }

    private void dfs(DiGraph<?> graph, int v) {
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

    public boolean stronglyConnected(int v, int w) {
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
        int[][] edges = {{0, 1}, {0, 5}, {2, 3},{2, 0}, {3, 2},
                {3, 5}, {4, 2}, {4, 3},{4, 5}, {5, 4}, {6, 0}, {6, 4},
                {6, 9}, {7, 6}, {7, 8}, {8, 9},{8, 7}, {9, 10},
                {9, 11}, {10, 12}, {11, 4}, {11, 12}, {12, 9}};

        DiGraph<?> graph = new DiGraph<>(13, edges);
        KosarajuSCC cc = new KosarajuSCC(graph);
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
                System.out.print(v + " ");
            }
            System.out.println();
        }
    }
}
