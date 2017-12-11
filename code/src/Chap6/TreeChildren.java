package Chap6;


import java.util.*;

/**
 * 孩子表示法的树结构
 */
public class TreeChildren<Item> {

    public static class Node<T> {
        private T data;
        private List<Integer> children;

        public Node(T data) {
            this.data = data;
            this.children = new LinkedList<>();
        }

        public Node(T data, int[] children) {
            this.data = data;
            this.children = new LinkedList<>();
            for (int child : children) {
                this.children.add(child);
            }
        }

        public T getData() {
            return data;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "data=" + data +
                    ", children=" + children +
                    '}';
        }
    }

    // 树的容量，能容纳的最大结点数
    private int treeCapacity;
    // 树的结点数目
    private int nodesNum;
    // 存放树的所有结点
    private Node<Item>[] nodes;

    public TreeChildren(int treeCapacity) {
        this.treeCapacity = treeCapacity;
        nodes = new Node[treeCapacity];
    }

    public TreeChildren() {
        treeCapacity = 128;
        nodes = new Node[treeCapacity];
    }

    public void setRoot(Item data) {
        nodes[0].data = data;
        nodesNum++;
    }


    public void addChild(Item data, Node<Item> parent) {
        if (nodesNum < treeCapacity) {
            // 新的结点放入数组中第一个空闲位置
            nodes[nodesNum] = new Node<>(data);
            // 父结点添加其孩子
            parent.children.add(nodesNum);
            nodesNum++;
        } else {
            throw new RuntimeException("树已满，无法再添加结点！");
        }
    }

    public void createTree(Item[] datas, int[][] children) {
        if (datas.length > treeCapacity) {
            throw new RuntimeException("数据过多，超出树的容量！");
        }

        for (int i = 0; i < datas.length; i++) {
            nodes[i] = new Node<>(datas[i], children[i]);
        }

        nodesNum = datas.length;
    }

    // 根据给定的结点查找再数组中的位置
    private int index(Node<Item> node) {
        for (int i = 0; i < nodesNum; i++) {
            if (nodes[i].equals(node)) {
                return i;
            }
        }
        throw new RuntimeException("无此结点");
    }

    public List<Node<Item>> childrenFromNode(Node<Item> node) {
        List<Node<Item>> children = new ArrayList<>();
        for (Integer i : node.children) {
            children.add(nodes[i]);
        }
        return children;
    }

    public Node<Item> parentTo(Node<Item> node) {
        for (int i = 0; i < nodesNum; i++) {
            if (nodes[i].children.contains(index(node))) {
                return nodes[i];
            }
        }
        return null;
    }

    // 是否为空树
    public boolean isEmpty() {
        return nodesNum == 0;
        // or return nodes[0] == null
    }

    // 树的深度
    public int depth() {
        return nodeDepth(root());
    }

    // 求以node为根结点的子树的深度
    public int nodeDepth(Node<Item> node) {
        if (node == null) {
            return 0;
        }
        // max是某个结点所有孩子中的最大深度
        int max = 0;
        // 即使没有孩子，返回1也是正确的
        if (node.children.size() > 0) {
            for (int i : node.children) {
                int depth = nodeDepth(nodes[i]);
                if (depth > max) {
                    max = depth;
                }
            }
        }
        // 这里需要+1因为depth -> max是当前结点的孩子的深度, +1才是当前结点的深度
        return max + 1;
    }

    public int degree() {
        int max = 0;
        for (int i = 0; i < nodesNum; i++) {
            if (nodes[i].children.size() > max) {
                max = nodes[i].children.size();
            }
        }
        return max;
    }

    public int degreeForNode(Node<Item> node) {
        return childrenFromNode(node).size();
    }

    public Node<Item> root() {
        return nodes[0];
    }

    // 树的结点数
    public int nodesNum() {
        return nodesNum;
    }

    // 让树为空
    public void clear() {
        for (int i = 0; i < nodesNum; i++) {
            nodes[i] = null;
            nodesNum = 0;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Tree{\n");
        for (int i = 0; i < nodesNum - 1; i++) {
            sb.append(nodes[i]).append(", \n");
        }
        sb.append(nodes[nodesNum - 1]).append("}");
        return sb.toString();
    }

    public static void main(String[] args) {
        String[] datas = {"Bob", "Tom", "Jerry", "Rose", "Jack"};
        int[][] children = {{1, 2}, {3}, {4}, {}, {}};
        TreeChildren<String> tree = new TreeChildren<>();
        tree.createTree(datas, children);

        TreeChildren.Node<String> root = tree.root();
        TreeChildren.Node<String> rightChild = tree.childrenFromNode(root).get(1);
        System.out.println(rightChild.getData() + "的度为" + tree.degreeForNode(rightChild));
        System.out.println("该树的结点数为" + tree.nodesNum());
        System.out.println("该树根结点" + tree.root());
        System.out.println("该树的深度为" + tree.depth());
        System.out.println("该树的度为" + tree.degree());
        System.out.println(tree.parentTo(rightChild));

        tree.addChild("Joe", root);
        System.out.println("该树的度为" + tree.degree());
        System.out.println(tree);

    }
}
