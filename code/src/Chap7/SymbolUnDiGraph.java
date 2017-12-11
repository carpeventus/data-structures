package Chap7;

import Chap8.BST;

/**
 * 针对无向图的符号图，使用字符串表示顶点
 */
public class SymbolUnDiGraph {
    // 键是顶点字符串，值是顶点
    private BST<String, Integer> st; // 符号名 -> 顶点索引
    private String[] keys; // 顶点索引 -> 符号名
    private UndiGraph<String> graph;

    public SymbolUnDiGraph(String[][] edges) {
        st = new BST<>();
        for (String[] infos : edges) {
            for (String info : infos) {
                // 重复的字符串不再分配顶点
                if (!st.contains(info)) {
                    st.put(info, st.size());
                }
            }
        }

        keys = new String[st.size()];
        for (String name : st.keys()) {
            keys[st.get(name)] = name;
        }

        // 构造图, 每一行的第一个顶点和该行的其他顶点相连
        graph = new UndiGraph<>(st.size());
        for (String[] infos : edges) {
            int v = st.get(infos[0]);
            for (int i = 1; i < infos.length; i++) {
                graph.addEdge(v, st.get(infos[i]));
            }
        }

    }

    public boolean contains(String name) {
        return st.contains(name);
    }

    public int indexOf(String name) {
        return st.get(name);
    }

    public String nameOf(int v) {
        return keys[v];
    }

    public UndiGraph<String> graph() {
        return graph;
    }

    public static void main(String[] args) {
        String[][] edges = {{"产品A", "经销商1", "经销商3", "经销商4", "经销商6"},
                {"产品B", "经销商1", "经销商2", "经销商3", "经销商5", "经销商6"},
                {"产品C", "经销商1", "经销商3", "经销商4", "经销商5", "经销商6"},
                {"产品D", "经销商1", "经销商6"}};

        SymbolUnDiGraph sg = new SymbolUnDiGraph(edges);
        UndiGraph<String> graph = sg.graph();
        System.out.println("经销商4有经营下面几种产品");
        for (int w : graph.adj(sg.indexOf("经销商4"))) {
            System.out.print(sg.nameOf(w) + " ");
        }
        System.out.println();

        System.out.println("产品C在下面几个经销商有售");
        for (int w : graph.adj(sg.indexOf("产品C"))) {
            System.out.print(sg.nameOf(w) + " ");
        }
        System.out.println();
    }
}
