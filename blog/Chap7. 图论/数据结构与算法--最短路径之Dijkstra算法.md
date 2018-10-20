# 数据结构与算法--最短路径之Dijkstra算法

加权图中，我们很可能关心这样一个问题：**从一个顶点到另一个顶点成本最小的路径。**比如从成都到北京，途中还有好多城市，如何规划路线，能使总路程最小；或者我们看重的是路费，那么如何选择经过的城市可以使得总路费降到最低？

- 首先路径是有向的，最短路径需要考虑到各条边的方向。
- 权值不一定就是指距离，还可以是费用等等...

最短路径的定义：**在一幅有向加权图中，从顶点s到顶点t的最短路径是所有从s到t的路径中权值最小者。**

为此，我们先要定义有向边以及有向图。

## 加权有向图的实现

首先是有向边。

```java
package Chap7;

public class DiEdge {
    private int from;
    private int to;
    private double weight;

    public DiEdge(int from, int to, double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public int from() {
        return from;
    }

    public int to() {
        return to;
    }

    public double weight() {
        return weight;
    }
    
    @Override
    public String toString() {
        return "(" +
                from +
                "->" + to +
                " " + weight +
                ')';
    }
}

```

比起无向边Edge类，更简单些，因为两个顶点有明显的先后顺序。

然后是加权有向图。

```java
package Chap7;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EdgeWeightedDiGraph<Item> {
    private int vertexNum;
    private int edgeNum;
    // 邻接表
    private List<List<DiEdge>> adj;
    // 顶点信息
    private List<Item> vertexInfo;

    public EdgeWeightedDiGraph(List<Item> vertexInfo) {
        this.vertexInfo = vertexInfo;
        this.vertexNum = vertexInfo.size();
        adj = new ArrayList<>();
        for (int i = 0; i < vertexNum; i++) {
            adj.add(new LinkedList<>());
        }
    }


    public EdgeWeightedDiGraph(List<Item> vertexInfo, int[][] edges, double[] weight) {
        this(vertexInfo);
        for (int i = 0; i < edges.length; i++) {
            DiEdge edge = new DiEdge(edges[i][0], edges[i][1], weight[i]);
            addDiEdge(edge);
        }
    }

    public EdgeWeightedDiGraph(int vertexNum) {
        this.vertexNum = vertexNum;
        adj = new ArrayList<>();
        for (int i = 0; i < vertexNum; i++) {
            adj.add(new LinkedList<>());
        }
    }

    public EdgeWeightedDiGraph(int vertexNum, int[][] edges, double[] weight) {
        this(vertexNum);
        for (int i = 0; i < edges.length; i++) {
            DiEdge edge = new DiEdge(edges[i][0], edges[i][1], weight[i]);
            addDiEdge(edge);
        }
    }

    public void addDiEdge(DiEdge edge) {
        adj.get(edge.from()).add(edge);
        edgeNum++;
    }

    // 返回与某个顶点依附的所有边
    public Iterable<DiEdge> adj(int v) {
        return adj.get(v);
    }

    public List<DiEdge> edges() {
        List<DiEdge> edges = new LinkedList<>();
        for (int i = 0; i < vertexNum; i++) {
            for (DiEdge e : adj(i)) {
                edges.add(e);
            }
        }
        return edges;
    }

    public int vertexNum() {
        return vertexNum;
    }

    public int edgeNum() {
        return edgeNum;
    }

    public Item getVertexInfo(int i) {
        return vertexInfo.get(i);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(vertexNum).append("个顶点, ").append(edgeNum).append("条边。\n");
        for (int i = 0; i < vertexNum; i++) {
            sb.append(i).append(": ").append(adj.get(i)).append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        List<String> vertexInfo = List.of("v0", "v1", "v2", "v3", "v4", "v5", "v6", "v7");
        int[][] edges = {{4, 5}, {5, 4}, {4, 7}, {5, 7}, {7, 5}, {5, 1}, {0, 4}, {0, 2},
                {7, 3}, {1, 3}, {2, 7}, {6, 2}, {3, 6}, {6, 0}, {6, 4}};

        double[] weight = {0.35, 0.35, 0.37, 0.28, 0.28, 0.32, 0.38, 0.26, 0.39, 0.29,
                0.34, 0.40, 0.52, 0.58, 0.93};

        EdgeWeightedDiGraph<String> graph = new EdgeWeightedDiGraph<>(vertexInfo, edges, weight);

        System.out.println("该图的邻接表为\n"+graph);
        System.out.println("该图的所有边："+ graph.edges());

    }
}
```

实现和加权无向图差不多，就改了`addEdge`和`adj`方法。addEdge由于边有向，不会对称地存储边；adj方法不像无向图那样邻接表中有重复的边，有向图中邻接表中的边都是唯一的，所以全部加入即可。

## 最短路径的数据结构

**1、最短路径树中的边**

和深度优先、广度优先搜索一样，我们将用到一个`edgeTo[]`表示一个树形结构，`edgeTo[v]`表示树中连接顶点v和其父结点的边（也就是起点s到v的路径上最后一条边）。

**2、起点到各个顶点的最短距离**

和Prim算法类似，需要一个`distTo[]`。Prim算法中它存放的是：到某个顶点权值最小的那条边。而最短路径中，`distTo[v]`存放的是：**从起点s开始到某顶点v的最短路径长度。**我们约定到起点s的最短路径长度为0，即`distTo[s] = 0`；同时约定从起点s到不可达的顶点的距离均为**正无穷**。

最短路径算法的基础基于一个被称为**松弛**的简单操作。放松一条边v -> w意味着**检查s到w的最短路径是否是 先从s到v，再从v到w。如果是就更新相关数据结构的内容；如果不是，不作更改。**用代码可以表示为

```java
// v -> w, v和w是边edge的两个顶点
// distTo[v] ：s到v的最短距离；distTo[w]：s到w的最短距离
if (distTo[v] + edge.weight() < distTo[w]) {
    distTo[w] = distTo[v] + e.weight();
  	edgeTo[w] = edge;
}
```

再用一幅图加深理解。

![](http://upload-images.jianshu.io/upload_images/2726327-4fd5a120e6f05e11.PNG?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

先看左边两个图：s到v的最短距离是3.1，s到w的最短距离是3.3。当在顶点v时，检查它的邻接点w，边v -> w的权值是1.3，从s到w的当然不能先从s到v，再从v到w，因为这俩加起来都4.4，比原来s到w的方案还要费劲，所以不会更改`distTo[w]`和`edgeTo[w]`。此时我们说v -> w这条边失效并忽略它。

再看右边两个图：原先s到w的方案距离为7.2，现在我们换条路走，从s先到v，再从v到w，只有4.4！这是条到w更近的路。所以更新，`distTo[w]`改成4.4，到s到w的最后一条边`edgeTo[w]`也改成了v- > w这条边。此时就称边v -> w放松成功（可以想象成一根紧绷的橡皮筋，它的长度比较长；橡皮筋放松后，长度变短。）

对顶点的放松就是：放松由该顶点引出的**所有边**。

在实现之前，对于最短路径算法我们需要了解得更多，来看几个命题。

- 当且仅当对于从v -> w的任意一条边，都有`dist[w] <= distTo[v] + edge.weight()`，那么s到w的路径都是最短路径。
- Dijkstra算法能解决边权值**非负**的加权有向图的**单点**最短路径问题，换句话说，当遇到有负权值的边，或者想通过一次运算就找到任意顶点到任意顶点的最短路径，Dijkstra就不适用了。
- 如果v是从起点s可达的，那么边v -> w**只会被放松一次**，放松v时，必有`dist[w] <= distTo[v] + edge.weight()`，该等式在算法整个流程都成立，所以`distTo[w]`只能减小。**而distTo[v]不会改变，因为每次都选择distTo[]最小的顶点，之后的放松操作不可能使得任何distTo[]的值小于dist[v]。也就是说，每次选择distTo[]最小的顶点，它的值不会小于那些已经放松过的顶点的最短路径值distTo[v]，也不会大于任意未被放松过的顶点。**所有从s可达的顶点都会按照distTo[]里最短路径的权值来依次放松。
- 最短路径算法也可以处理无向图，用有向图的数据类型，只是对应于无向图，每条边都会创建两条方向不同的有向边。例如，无向图中的边3-0，使用有向图创建3 -> 0和0 -> 3两条边，然后调用最短路径算法即可。

## Dijkstra算法的实现

```java
package Chap7;

import java.util.*;

public class Dijkstra {
    private DiEdge[] edgeTo;
    private double[] distTo;
    private Map<Integer, Double> minDist;

    public Dijkstra(EdgeWeightedDiGraph<?> graph, int s) {
        edgeTo = new DiEdge[graph.vertexNum()];
        distTo = new double[graph.vertexNum()];
        minDist = new HashMap<>();

        for (int i = 0; i < graph.vertexNum(); i++) {
            distTo[i] = Double.POSITIVE_INFINITY; // 1.0 / 0.0为INFINITY
        }
      	// 到起点距离为0
      	distTo[s] = 0.0;
        relax(graph, s);
        while (!minDist.isEmpty()) {
            relax(graph, delMin());
        }
    }

    private int delMin() {
        Set<Map.Entry<Integer, Double>> entries = minDist.entrySet();
        Map.Entry<Integer, Double> min = entries.stream().min(Comparator.comparing(Map.Entry::getValue)).get();
        int key = min.getKey();
        minDist.remove(key);
        return key;
    }

    private void relax(EdgeWeightedDiGraph<?> graph, int v) {
        for (DiEdge edge : graph.adj(v)) {
            int w = edge.to();
            if (distTo[v] + edge.weight() < distTo[w]) {
                distTo[w] = distTo[v] + edge.weight();
                edgeTo[w] = edge;
                if (minDist.containsKey(w)) {
                    minDist.replace(w, distTo[w]);
                    System.out.println(w);

                } else {
                    minDist.put(w, distTo[w]);
                }
            }
        }
    }

    public double distTo(int v) {
        return distTo[v];
    }

    public boolean hasPathTo(int v) {
        return distTo[v] != Double.POSITIVE_INFINITY;
    }

    public Iterable<DiEdge> pathTo(int v) {
        if (hasPathTo(v)) {
            LinkedList<DiEdge> path = new LinkedList<>();
            for (DiEdge edge = edgeTo[v]; edge != null; edge = edgeTo[edge.from()]) {
                path.push(edge);
            }
            return path;
        }
        return null;
    }

    public static void main(String[] args) {
        List<String> vertexInfo = List.of("v0", "v1", "v2", "v3", "v4", "v5", "v6", "v7");
        int[][] edges = {{4, 5}, {5, 4}, {4, 7}, {5, 7}, {7, 5}, {5, 1}, {0, 4}, {0, 2},
                {7, 3}, {1, 3}, {2, 7}, {6, 2}, {3, 6}, {6, 0}, {6, 4}};

        double[] weight = {0.35, 0.35, 0.37, 0.28, 0.28, 0.32, 0.38, 0.26, 0.39, 0.29,
                0.34, 0.40, 0.52, 0.58, 0.93};

        EdgeWeightedDiGraph<String> graph = new EdgeWeightedDiGraph<String>(vertexInfo, edges, weight);
        Dijkstra dijkstra = new Dijkstra(graph, 0);
        for (int i = 0; i < graph.vertexNum(); i++) {
            System.out.print("0 to " + i + ": ");
            System.out.print("(" + dijkstra.distTo(i) + ") ");
            System.out.println(dijkstra.pathTo(i));
        }
    }
}
/* Outputs

0 to 0: (0.0) []
0 to 1: (1.05) [(0->4 0.38), (4->5 0.35), (5->1 0.32)]
0 to 2: (0.26) [(0->2 0.26)]
0 to 3: (0.9900000000000001) [(0->2 0.26), (2->7 0.34), (7->3 0.39)]
0 to 4: (0.38) [(0->4 0.38)]
0 to 5: (0.73) [(0->4 0.38), (4->5 0.35)]
0 to 6: (1.5100000000000002) [(0->2 0.26), (2->7 0.34), (7->3 0.39), (3->6 0.52)]
0 to 7: (0.6000000000000001) [(0->2 0.26), (2->7 0.34)]

*/
```

和Prim算法的即时版本的几乎一样！两种算法都是添加边的方式来构造一棵树：Prim算法每次添加的是离整棵树（各个顶点）最近的树外的顶点；Dijkstra算法每次添加的是**离起点最近的树外顶点。**

Dijkstra不需要`marked[]`来记录被访问过的顶点了，**因为每条边v -> w只会被放松一次，每个顶点也只会放松一次。放松后的顶点的最短路径长度一定满足**`dist[w] <= distTo[v] + edge.weight()`，当想重复放松某个顶点时，会因为无法通过以下条件而被跳过。

```java
if (distTo[v] + edge.weight() < distTo[w]) { }
```

我们还是来跟着图走一遍。

![](http://upload-images.jianshu.io/upload_images/2726327-1261505d9aa1868a.PNG?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![](http://upload-images.jianshu.io/upload_images/2726327-fcfd49ce92ea9dba.PNG?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

- 放松顶点0，2、4被加入Map，distTo[2]为0 -> 2的权值，distTo[4]为0 -> 4的权值。
- 按权值放松顶点2，0 -> 2添加到树中。7被加入Map。distTo[7]为0 -> 2 -> 7的权值和。
- 放松顶点4，0 -> 4被加入到树中。5加到Map。dsitTo[5]为0 -> 4 -> 5的权值和。0 -> 4 -> 7没有0 ->2 -> 7路径短所以不更新distTo[7]。
- 放松顶点7，2- > 7加入到树中。3加入到Map。distTo[3]为0 -> 2 -> 3 -> 7的权值和，0 -> 2 -> 7 -> 5的权值和没有0 -> 4 -> 5的权值和小，所有不更新distTo[5]
- 放松顶点5， 4 ->5加入到树中，1加入到Map，distTo[1]为0 -> 4 -> 5 -> 1的权值和。0 -> 4 -> 5 -> 7的权值和没有0 -> 2 ->  7的权值和小，所以不更新distTo[7]
- 放松顶点3，7 -> 3加入到树中。6加入到Map。distTo[6]为0 -> 2 -> 7 -> 3 -> 6的权值和。
- 放松顶点1，5 -> 1加入到树。0 -> 4 -> 5 ->1 -> 3的权值和由于没有0 -> 2 -> 7 -> 3 的权值和小，所以不更新distTo[3]。
- 放松顶点6， 3 -> 6加入到树中。至此所有顶点都已放松一次，算法结束。

---

by @sunhaiyu

2017.9.23
