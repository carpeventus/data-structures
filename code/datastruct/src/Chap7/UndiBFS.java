package Chap7;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * BFS广度优先搜索-针对无向图
 */
public class UndiBFS {
    // 用来标记已经访问过的顶点，保证每个顶点值访问一次
    private boolean[] marked;
    // 起点
    private final int s;
    // 到该顶点的路径上的最后一条边
    private int[] edgeTo;


    public UndiBFS(UndiGraph<?> graph, int s) {
        this.s = s;
        marked = new boolean[graph.vertexNum()];
        edgeTo = new int[graph.vertexNum()];
        bfs(graph, s);
    }

    public void bfs(UndiGraph<?> graph, int s) {
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
        UndiBFS search = new UndiBFS(graph, 3);
        search.printPathTo(5);
        search.printPathTo(4);
        search.printPathTo(3);
        search.printPathTo(2);
        search.printPathTo(1);
        System.out.println(Arrays.toString(search.edgeTo));

    }

}
