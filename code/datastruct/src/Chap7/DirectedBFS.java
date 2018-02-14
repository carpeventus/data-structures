package Chap7;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * BFS广度优先搜索-针对有向图
 */
public class DirectedBFS {
    // 用来标记已经访问过的顶点，保证每个顶点值访问一次
    private boolean[] marked;
    // 起点
    private final int s;
    // 到该顶点的路径上的最后一条边
    private int[] edgeTo;


    public DirectedBFS(DiGraph<?> graph, int s) {
        this.s = s;
        marked = new boolean[graph.vertexNum()];
        edgeTo = new int[graph.vertexNum()];
        bfs(graph, s);
    }

    public void bfs(DiGraph<?> graph, int s) {
        marked[s] = true;
        // offer入列, poll出列
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(s);
        while (!queue.isEmpty()) {
            int v = queue.poll();
//            System.out.print(v+" ");
            for (int w: graph.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    marked[w] = true;
                    queue.offer(w);
                }
            }
        }
    }

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
                    System.out.print("->" + i);
                }
            }
            System.out.println();
        } else {
            System.out.println("不存在路径！");
        }
    }

    public static void main(String[] args) {
        int[][] edges = {{4, 2},{2, 3}, {3, 2}, {6, 0},
                {0, 1}, {2, 0}, {11, 12}, {12, 9}, {9, 10},
                {9, 11}, {8, 9}, {10, 12},{11, 4}, {4,3},{3,5},
                {7, 8}, {8, 7}, {5,4}, {0, 5}, {6, 4}, {6,9}, {7,6}};

        DiGraph<String> graph = new DiGraph<>(13, edges);

        DirectedBFS search = new DirectedBFS(graph, 6);
        System.out.println(Arrays.toString(search.edgeTo));
        search.printPathTo(10);

    }
}
