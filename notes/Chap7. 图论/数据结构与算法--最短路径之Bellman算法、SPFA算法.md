# 数据结构与算法--最短路径之Bellman算法、SPFA算法

除了Floyd算法，另外一个使用广泛且可以处理负权边的是**Bellman-Ford**算法。

## Bellman-Ford算法

假设某个图有V个顶点E条边。

该算法主要流程是：

- 初始化。到起点s的距离`distTo[s]`设置为0，其余顶点的`dist[]`设置为正无穷；
- 以任意次序放松图中的所有E条边，重复V轮；
- V轮放松结束后，判断是否存在负权回路。如果存在，最短路径没有意义。

根据流程可以给出代码，如下

```java
package Chap7;

import java.util.LinkedList;
import java.util.List;

public class BellmanFord {
    private boolean hasNegativeCycle;
    private double distTo[];
    private DiEdge[] edgeTo;

    public boolean hasNegativeCycle() {
        return hasNegativeCycle;
    }

    private void relax(EdgeWeightedDiGraph<?> graph, int v) {
        for (DiEdge edge : graph.adj(v)) {
            int w = edge.to();
            if (distTo[v] + edge.weight() < distTo[w]) {
                distTo[w] = distTo[v] + edge.weight();
                edgeTo[w] = edge;
            }
        }
    }

    public BellmanFord(EdgeWeightedDiGraph<?> graph, int s) {
        distTo = new double[graph.vertexNum()];
        edgeTo = new DiEdge[graph.vertexNum()];
        for (int i = 0; i < graph.vertexNum(); i++) {
            distTo[i] = Double.POSITIVE_INFINITY; // 1.0 / 0.0为INFINITY
        }

        distTo[s] = 0.0;
        // 以上都是初始化

        for (int pass = 0; pass < graph.vertexNum(); pass++) {
            for (int v = 0; v < graph.vertexNum(); v++) {
                relax(graph, v);
            }
        }

        // 上面即使有负权回路也不会陷入死循环，因为给定了循环范围，算法必然终止。
        // 进行V轮边的松弛后，如果没有负权回路，那么所有的distTo[v] + edge.weight() >= distTo[w]
        // 如果对于图中任意边，仍然存在distTo[v] + edge.weight() < distTo[w]，则存在负权回路
        for (int v = 0; v < graph.vertexNum(); v++) {
            for (DiEdge edge : graph.adj(v)) {
                int w = edge.to();
                if (distTo[v] + edge.weight() < distTo[w]) {
                    hasNegativeCycle = true;
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
        EdgeWeightedDiGraph<String> graph = new EdgeWeightedDiGraph<>(vertexInfo, edges, weight);
        BellmanFord[] all = new BellmanFord[graph.vertexNum()];

        for (int i = 0; i < all.length; i++) {
            all[i] = new BellmanFord(graph, i);
        }

        for (int s = 0; s < all.length; s++) {
            for (int i = 0; i < graph.vertexNum(); i++) {
                System.out.print(s + " to " + i + ": ");
                System.out.print("(" + all[s].distTo(i) + ") ");
                System.out.println(all[s].pathTo(i));
            }
            System.out.println();
        }
    }
}

```

在V轮放松完成后，如果没有负权回路存在，那么对于任何v -> w必然有`distTo[v] + edge.weight() >= distTo[w]`，说明所有`dist[w]`已经是最短路径了；如果V轮后还存在`distTo[v] + edge.weight() < distTo[w]`，说明`distTo[w]`无法收敛到最小值——陷入死循环了，我们围着那个环绕圈子，可以使得路径越来越短！这就是遇到了负权回路。

上面的例子没有负权回路存在，我们特意制造一个，看看结果。

```java
public static void main(String[] args) {
  	List<String> vertexInfo = List.of("v0", "v1", "v2", "v3");
  	int[][] edges = {{0, 1}, {1, 2}, {2, 0}, {0, 3}, {2, 3}};

  	double[] weight = {-9 , 5, 2, 4, 6};
  	EdgeWeightedDiGraph<String> graph = new EdgeWeightedDiGraph<>(vertexInfo, edges, weight);
  	BellmanFord bellmanFord = new BellmanFord(graph, 0);
  	if (bellmanFord.hasNegativeCycle()) {
    	System.out.println("路径中存在负权环！");
  	}
}
```

0 -> 1-> 2 -> 0是一个负权回路。这里也注意下，如果图中有边的权值为负，在求最短的路径的时候要先判断有没有负权回路存在再进行后续计算。

##  SPFA算法--Bellman-Ford算法的优化

其实，根据经验我们很容易知道在任意一轮中许多边都不会放松成功。**我们下次需要放松的顶点，只需是上次`dist[w]`值发生改变的那些w顶点。**为此用一个队列保存这些顶点，用一个`onPQ[]`的布尔数组，来判断某个顶点是否已经在队列中。基于队列优化的Bellm-Ford算法又称为SPFA算法(Shortest Path Faster Algorithm)。

SPFA算法的思路是：每次放松一条边v -> w，如果放松成功（即`distTo[w]`的值被更新），且w没有在队列中则将其入列。然后队列的顶点出列并放松它，直到队列为空或者找到负权回路，算法终止。

这些数据结构可以保证：

- 队列中不会出现重复的顶点；
- 在某一轮中，改变了dist[w]和edge[w]的所有w将会在下一轮处理。

如果不存在从起点s可达的负权回路，那么算法终止的条件将是队列为空；如果存在呢？队列永远不会空（又在兜圈子了）！这意味着程序永不会结束，为此，我们必须判断从s可达的路径中是否存在负权回路。如果存在，应该立即停止算法，因为负权回路使得最短路径的研究毫无意义。而且此时经V轮放松后的`edgeTo[]`中必然会形成一个环，且权值和为负数。**但很可能在全部V轮结束前就可以从edgeTo[]中找到负权回路**，所以在放松边的过程中，可以隔若干轮就检查一下`edgeTo[]`中的路径是否成负权回路。

由于不是V轮结束后才检查是否存在负权回路，而是一边放松，一边检查，所以像上面那样用`distTo[v] + edge.weight() < distTo[w]`的方法来判断已经不适用了，因为放松尚未完成，上式成立很正常（说明需要更新最短路径了）。于是我们采用一种更通用的方法：**先判断是否存在有向环，再判断该环的权值和是不是负数。**

### 寻找有向负权环

判断有向环的实现并不复杂，核心思想其实是DFS（深度优先搜索）。

```java
package Chap7;

import java.util.LinkedList;

public class NegativeDiCycle {
    private boolean[] marked;
    private DiEdge[] edgeTo;
    private boolean[] onStack;
    private LinkedList<DiEdge> cycle;

    public NegativeDiCycle(EdgeWeightedDiGraph<?> graph) {
        marked = new boolean[graph.vertexNum()];
        edgeTo = new DiEdge[graph.vertexNum()];
        onStack = new boolean[graph.vertexNum()];
        // 有向图可能不是强连通的，所以需要从每个顶点出发，寻找负权环
        for (int i = 0; i < graph.vertexNum(); i++) {
            dfs(graph, i);
        }
    }

    private void dfs(EdgeWeightedDiGraph<?> graph, int v) {
        // 模拟系统递归使用的栈，方法开始进栈；方法结束出栈
        onStack[v] = true;
        marked[v] = true;
        for (DiEdge edge : graph.adj(v)) {
            // 如果已经存在负权回路，终止递归方法
            if (this.hasNegativeDiCycle()) {
                return;
            }

            int w = edge.to();
            if (!marked[w]) {
                edgeTo[w] = edge;
                dfs(graph, w);
                // v -> w的路径，且w在栈中，说明形成有向环
            } else if (onStack[w]) {
                cycle = new LinkedList<>();

                DiEdge e = edgeTo[v];
                while (e.from() != w) {
                    cycle.push(e);
                    e = edgeTo[e.from()];
                }
                // 为避免空指针，离w最近的那条在循环外入栈
                cycle.push(e);
                // 把导致成环的边加入
                cycle.push(edge);
            }
        }
        onStack[v] = false;
    }

    public boolean hasNegativeDiCycle() {
        if (cycle != null) {
            double cycleWeight = cycle.stream().mapToDouble(DiEdge::weight).sum();
            if (cycleWeight < 0) {
                return true;
            }
        }
        return false;
    }

    public Iterable<DiEdge> cycle() {
        if (hasNegativeDiCycle()) {
            return cycle;
        }
        return null;
    }

}
```

使用DFS的原因主要是为了利用递归产生的由**系统维护的栈**（每次方法调用就相当于入栈，最先调用的最后才返回），而递归方法dfs的调用顺序正好反映了顶点的访问顺序，如先调用dfs(s)， 接着dfs(w), 然后dfs(x)，再递归调用dfs(v)，那么这是一条s -> w -> x -> v的路径。我们使用了一个`onStack[]`布尔数组来模拟方法调用的进出栈情况——进入方法体说明方法被调用，进栈；方法执行完毕，该返回到上一层方法调用中了，出栈。**`onStack[]`其实就是一条路径，`onStack[v] = true`说明顶点v位于从起点s可达的onStack[]这条路径中。**

该实现最为关键的就是理解：**当我们在v处发现某条v -> w的边，而恰好其w位于`onStack[]`中，就找到了一个环**。我们知道`onStack[]`表示的是s -> w -> x -> v的路径，现在v -> w 刚好补全w -> x -> v成为环！如下图所示

![](http://upload-images.jianshu.io/upload_images/2726327-8014c89c00bff9f1.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

好，寻找到有向环后，再判断环内所有边的权值是不是负数就好了。该实现不仅能判断，还能找出到底是哪些边造成了环。关键是以下几行

```java
DiEdge e = edgeTo[v];
while (e.from() != w) {
  	cycle.push(e);
  	e = edgeTo[e.from()];
}
// 为避免空指针，离w最近的那条在循环外入栈
cycle.push(e);
// 把导致成环的边加入
cycle.push(edge);
```

对照着上图，最先x -> v入栈，到w -> x时候，发现`e.from()`就是w，不存入。出了while循环，将这条w -> x入栈，最后别忘了把导致成环的那条边入栈。有人可能会说了为何要这么麻烦，循环外单独push了两次，这是因为`edgeTo[]`中有的值是null（比如起点s），**如果不在合适的地方终止循环，将在`e = edgeTo[e.from()]`该语句执行后，在`e.from() != w`处引起空指针异常！**

### SPFA算法的实现

可以判断负权回路的是否存在了，据此实现SPFA算法。

```java
package Chap7;

import java.util.*;

public class SPFA {
    private double[] distTo;
    private DiEdge[] edgeTo;
    private Queue<Integer> queue;
    private boolean[] onPQ;  // 顶点是否在queue中
    private int cost; // 记录放松了边的次数
    private Iterable<DiEdge> cycle; // 找到的负权回路

    public boolean hasNegativeCycle() {
        return cycle != null;
    }

    private void findNegativeCycle() {

        EdgeWeightedDiGraph<String> g = new EdgeWeightedDiGraph<>(edgeTo.length);
        for (int v = 0; v < edgeTo.length; v++) {
            if (edgeTo[v] != null) {
                g.addDiEdge(edgeTo[v]);
            }
        }
        NegativeDiCycle cycleFinder = new NegativeDiCycle(g);
        if (cycleFinder.hasNegativeDiCycle()) {
            cycle = cycleFinder.cycle();
        }
    }

    private void relax(EdgeWeightedDiGraph<?> graph, int v) {
        for (DiEdge edge : graph.adj(v)) {
            int w = edge.to();
            if (distTo[v] + edge.weight() < distTo[w]) {
                distTo[w] = distTo[v] + edge.weight();
                edgeTo[w] = edge;
                if (!onPQ[w]) {
                    queue.offer(w);
                    onPQ[w] = true;
                }
            }
            // 每调放松一条边cost自增；每放松了graph.vertexNum条边，就检查是否有负权回路
            if (cost++ % graph.vertexNum() == 0) {
                findNegativeCycle();
            }
        }
    }

    public SPFA(EdgeWeightedDiGraph<?> graph, int s) {
        distTo = new double[graph.vertexNum()];
        edgeTo = new DiEdge[graph.vertexNum()];
        queue = new LinkedList<>();
        onPQ = new boolean[graph.vertexNum()];

        for (int i = 0; i < graph.vertexNum(); i++) {
            distTo[i] = Double.POSITIVE_INFINITY; // 1.0 / 0.0为INFINITY
        }

        distTo[s] = 0.0;
        // 以上都是初始化
        queue.offer(s);
        onPQ[s] = true;

        while (!queue.isEmpty() && !hasNegativeCycle()) {
            int v = queue.poll();
            onPQ[v] = false;
            relax(graph, v);
        }
    }

    public Iterable<DiEdge> cycle() {
        return cycle;
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
        List<String> vertexInfo = List.of("v0", "v1", "v2", "v3");
        int[][] edges = {{0, 1}, {1, 2}, {2, 0}, {0, 3}, {2, 3}};

        double[] weight = {-9 , 5, 2, 4, 6};
        EdgeWeightedDiGraph<String> graph = new EdgeWeightedDiGraph<>(vertexInfo, edges, weight);

        SPFA spfa = new SPFA(graph, 0);
        if (spfa.hasNegativeCycle()) {
            System.out.print("存在负权环：");
            System.out.println(spfa.cycle());
        }
    }
}

```

程序将输出找到的负权回路，打印`[(2->0 2.0), (0->1 -9.0), (1->2 5.0)]`。对于不存在负权回路的图，SPFA当然也能正确处理。这里就不测试了。

代码中特别注意一点，我们之前有提到需要隔若干次就检查是否存在负权回路，所以用到一个int型的cost变量记录放松边的次数，每放松了V条边就检查一次。因为可能在第V次放松之后，`edgeTo[]`数组中就存在负权回路了。`findNegativeCycle`方法就是将`edgeTo[]`转化成了有向图送给`NegativeDiCycle`类，检测是否存在负权回路。

---

by @sunhaiyu

2017.9.26
