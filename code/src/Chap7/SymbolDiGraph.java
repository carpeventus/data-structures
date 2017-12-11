package Chap7;

import Chap8.BST;

/**
 * 针对有向图的符号图，使用字符串表示顶点
 */
public class SymbolDiGraph {
    // 键是顶点字符串，值是顶点
    private BST<String, Integer> st; // 符号名 -> 顶点索引
    private String[] keys; // 顶点索引 -> 符号名
    private DiGraph<String> graph;

    public SymbolDiGraph(String[][] edges) {
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
        graph = new DiGraph<>(st.size());
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

    public DiGraph<String> graph() {
        return graph;
    }

    public static void main(String[] args) {
        String[][] edges = {{"郑州", "上海", "北京", "西安", "成都"},
                {"成都", "郑州", "北京", "西安"},
                {"上海", "北京"},
                {"西安", "成都", "郑州"}};

        SymbolDiGraph sg = new SymbolDiGraph(edges);
        DiGraph<String> graph = sg.graph();
        for (int w : graph.adj(sg.indexOf("西安"))) {
            System.out.print(sg.nameOf(w) + " ");
        }
        System.out.println();
    }
}
