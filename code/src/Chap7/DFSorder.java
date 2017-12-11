package Chap7;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 基于深度优先的顶点排序，前序、后序、逆后序
 */
public class DFSorder {
    private boolean[] marked;

    private Queue<Integer> pre; // 前序
    private Queue<Integer> post; // 后序
    private LinkedList<Integer> reversePost; // 逆后序

    public DFSorder(DiGraph<?> graph) {
        pre = new LinkedList<>();
        post = new LinkedList<>();
        reversePost = new LinkedList<>();
        marked = new boolean[graph.vertexNum()];
        // 有向图可能不是强连通的，所以需要从每个顶点出发，寻找有向环
        for (int v = 0; v < graph.vertexNum(); v++) {
            if (!marked[v]) {
                dfs(graph, v);
            }
        }
    }

    public DFSorder(EdgeWeightedDiGraph<?> graph) {
        pre = new LinkedList<>();
        post = new LinkedList<>();
        reversePost = new LinkedList<>();
        marked = new boolean[graph.vertexNum()];
        // 有向图可能不是强连通的，所以需要从每个顶点出发，寻找有向环
        for (int v = 0; v < graph.vertexNum(); v++) {
            if (!marked[v]) {
                dfs(graph, v);
            }
        }
    }

    private void dfs(EdgeWeightedDiGraph<?> graph, int v) {
        pre.offer(v);
        marked[v] = true;

        for (DiEdge edge : graph.adj(v)) {
            int w = edge.to();
            if (!marked[w]) {
                dfs(graph, w);
            }
        }

        post.offer(v);
        reversePost.push(v);
    }

    private void dfs(DiGraph<?> graph, int v) {
        pre.offer(v);
        marked[v] = true;

        for (int w:graph.adj(v)) {
            if (!marked[w]) {
                dfs(graph, w);
            }
        }

        post.offer(v);
        reversePost.push(v);
    }

    public Iterable<Integer> pre() {
        return pre;
    }

    public Iterable<Integer> post() {
        return post;
    }

    public Iterable<Integer> reversePost() {
        return reversePost;
    }
}
