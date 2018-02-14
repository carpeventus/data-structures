package Chap7;

/**
 * 拓补排序实现2，基于DFS的逆后序
 */
public class TopoSort {
    private Iterable<Integer> order;

    public TopoSort(DiGraph<?> graph) {
        DiCycle cycleFinder = new DiCycle(graph);
        if (!cycleFinder.hasCycle()) {
            DFSorder dfs = new DFSorder(graph);
            order = dfs.reversePost();
        }
    }

    public TopoSort(EdgeWeightedDiGraph<?> graph) {
        DiCycle cycleFinder = new DiCycle(graph);
        if (!cycleFinder.hasCycle()) {
            DFSorder dfs = new DFSorder(graph);
            order = dfs.reversePost();
        }
    }

    public boolean isDAG() {
        return order != null;
    }

    public Iterable<Integer> order() {
        return order;
    }

    public static void main(String[] args) {
        int[][] edges = {{0, 1}, {0, 5}, {0, 6}, {2, 0}, {2, 3}, {3, 5},{5, 4},{6, 4}, {6, 9}, {7, 6},
                {8, 7}, {9, 10}, {9, 11}, {9, 12}, {11, 12}};
        DiGraph<?> graph = new DiGraph<>(13, edges);
        TopoSort topo = new TopoSort(graph);

        if (topo.isDAG()) {
            System.out.println(topo.order());
        }
    }
}
