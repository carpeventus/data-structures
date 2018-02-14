package Chap7;

import java.util.ArrayList;
import java.util.List;

/**
 * 无向图 -- 邻接矩阵
 * @param <Item> 顶点类型
 */
public class AdjMatrixGraph<Item> {
    private int vertexNum;
    private int edgeNum;
    // 邻接矩阵
    private boolean[][] adj;
    // 存放所有顶点信息
    private Item[] vertexInfo;

    // 初始化有V个顶点的图，还未加边
    public AdjMatrixGraph(Item[] vertexInfo) {
        this.vertexNum = vertexInfo.length;
        this.vertexInfo = vertexInfo;

        adj = new boolean[vertexNum][vertexNum];
    }

    public AdjMatrixGraph(Item[] vertexInfo, int[][] edges) {
        this(vertexInfo);
        for (int[] twoVertex : edges) {
            addEdge(twoVertex[0], twoVertex[1]);
        }
    }

    public AdjMatrixGraph(int vertexNum) {
        this.vertexNum = vertexNum;
        adj = new boolean[vertexNum][vertexNum];
    }

    public AdjMatrixGraph(int vertexNum,int[][] edges) {
        this(vertexNum);
        for (int[] twoVertex : edges) {
            addEdge(twoVertex[0], twoVertex[1]);
        }
    }

    public void addEdge(int i, int j) {
        // 对称矩阵，所以a[i][j] = a[j][i]
        adj[i][j] = true;
        adj[j][i] = true;
        edgeNum++;
    }

    public int vertexNum() {
        return vertexNum;
    }

    public int edgenum() {
        return edgeNum;
    }

    public Item getVertexInfo(int i) {
        return vertexInfo[i];
    }
    // 求某顶点的所有邻接顶点
    public List<Integer> adj(int i) {
        List<Integer> vertexAdj = new ArrayList<>();
        for (int j = 0; j < adj[i].length; j++) {
            if (adj[i][j]) {
                vertexAdj.add(j);
            }
        }
        return vertexAdj;
    }

    // 某顶点的度
    public int degree(int i) {
        int degree = 0;
        for (int j = 0; j < adj[i].length; j++) {
            if (adj[i][j]) {
               degree++;
            }
        }
        return degree;
    }
    // 求图的最大度数
    public int maxDegree() {
        int max = 0;
        for (int i = 0; i < vertexNum; i++) {
            if (degree(i) > max) {
                max = degree(i);
            }
        }
        return max;
    }
    // 求图的平均度数
    // 边的条数 = 顶点度之和的一半  因为一条边对应两个顶点，这两个顶点的度数之和为2，所以边的数量是度之和的一半这样的关系
    // edgeNum = sum / 2, 则sum = 2 * edgeNum, 于是avgDegree = sum / vertexNum
    public double avgDegree() {
        return 2.0 * edgeNum / vertexNum;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(vertexNum).append("个顶点, ").append(edgeNum).append("条边。\n");
        for (int i = 0; i < vertexNum; i++) {
            sb.append(i).append(": ").append(adj(i)).append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String[] vertexInfo = {"v0", "v1", "v2", "v3", "v4"};
        int[][] edges = {{0, 1}, {0, 2}, {0, 3},
                {1, 3}, {1, 4},
                {2, 4}};
        AdjMatrixGraph<String> graph = new AdjMatrixGraph<>(vertexInfo,edges);

        System.out.println("顶点3的度为" + graph.degree(3));
        System.out.println("顶点3的邻接点为"+graph.adj(3));
        System.out.println("该图的最大度数为" + graph.maxDegree());
        System.out.println("该图的平均度数为" + graph.avgDegree());
        System.out.println("邻接矩阵如下:\n" + graph);
    }
}
