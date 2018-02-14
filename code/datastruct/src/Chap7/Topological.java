package Chap7;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 拓补排序实现1，不断移除入度为0的结点
 */
public class Topological {
    private int[] in;
    private Queue<Integer> zeroInQueue; // 入度为0的队列
    private Queue<Integer> order; // 保存拓补排序的队列

    public Topological(DiGraph<?> graph) {
        zeroInQueue = new LinkedList<>();
        order = new LinkedList<>();
        in = new int[graph.vertexNum()];
        // 原图的逆向图，求逆向图的出度得到的就是原图的入度
        DiGraph<?> R = graph.reverse();
        for (int i = 0; i < graph.vertexNum(); i++) {
            int inCount = 0;
            // 逆向图的出度就是原图的入度
            for (int ignored : R.adj(i)) {
                inCount++;
            }
            // 得到各个顶点的入度数组
            in[i] = inCount;
        }
        // 以上是初始化

        // 先将入度为0的所有顶点入列
        for (int i = 0; i < in.length; i++) {
            if (in[i] == 0) {
                zeroInQueue.offer(i);
            }
        }
        // 不断取出队列中的顶点，将该顶点的邻接点的入度减去1
        while (!zeroInQueue.isEmpty()) {
            int v = zeroInQueue.poll();
            order.offer(v); // 加入到拓补序列中
            // 可以看作是移除该顶点，于是和该顶点相邻的边也删掉。从度的角度来看就是邻接点入度减小1
            for (int w : graph.adj(v)) {
                if (--in[w] == 0) {
                    zeroInQueue.offer(w);
                }
            }
        }
    }

    public Iterable<Integer> order() {
        // 是拓补序列才输出
        if (isDAG()) {
            return order;
        }
        return null;
    }

    public boolean isDAG() {
        // in.length就是图的顶点数，只要不是全部顶点都进入到了拓补排序队列中，说明遇到了有向环
        return order.size() == in.length;
    }

    public static void main(String[] args) {
        int[][] edges = {{0, 1}, {0, 5}, {0, 6}, {2, 0}, {2, 3}, {3, 5},{5, 4},{6, 4}, {6, 9}, {7, 6},
                {8, 7}, {9, 10}, {9, 11}, {9, 12}, {11, 12}};

        DiGraph<?> graph = new DiGraph<>(13, edges);
        Topological topo = new Topological(graph);
        System.out.println(topo.order());
    }
}
