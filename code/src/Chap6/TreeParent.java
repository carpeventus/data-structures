package Chap6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 双亲表示法的树结构
 */
public class TreeParent<Item> {

    public static class Node<T> {
        private T data;
        private int parent;

        public Node(T data, int parent) {
            this.data = data;
            this.parent = parent;
        }

        public T getData() {
            return data;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "data=" + data +
                    ", parent=" + parent +
                    '}';
        }
    }

    // 树的容量，能容纳的最大结点数
    private int treeCapacity;
    // 树的结点数目
    private int nodesNum;
    // 存放树的所有结点
    private Node<Item>[] nodes;

    // 以和树大小初始化树
    public TreeParent(int treeCapacity) {
        this.treeCapacity = treeCapacity;
        nodes = new Node[treeCapacity];

    }

    // 以默认的树大小初始化树
    public TreeParent() {
        treeCapacity = 128;
        nodes = new Node[treeCapacity];
    }

    public void setRoot(Item data) {
        // 根结点
        nodes[0] = new Node<>(data, -1);
        nodesNum++;
    }

    public void addChild(Item data, Node<Item> parent) {
        if (nodesNum < treeCapacity) {
            // 新的结点放入数组中第一个空闲位置
            nodes[nodesNum] = new Node<>(data, index(parent));
            nodesNum++;
        } else {
            throw new RuntimeException("树已满，无法再添加结点！");
        }
    }

    // 用nodeNum是因为其中无null，用treeCapacity里面很多null值根本无需比较
    private int index(Node<Item> parent) {
        for (int i = 0; i < nodesNum; i++) {
            if (nodes[i].equals(parent)) {
                return i;
            }
        }
        throw new RuntimeException("无此结点");
    }

    public void createTree(List<Item> datas, List<Integer> parents) {
        if (datas.size() > treeCapacity) {
            throw new RuntimeException("数据过多，超出树的容量！");
        }

        setRoot(datas.get(0));
        for (int i = 1; i < datas.size(); i++) {
            addChild(datas.get(i), nodes[parents.get(i - 1)]);
        }
    }

    // 是否为空树
    public boolean isEmpty() {
        return nodesNum == 0;
        // or return nodes[0] == null
    }

    public Node<Item> parentTo(Node<Item> node) {
        return nodes[node.parent];
    }

    // 结点的孩子结点
    public List<Node<Item>> childrenFromNode(Node<Item> parent) {
        List<Node<Item>> children = new ArrayList<>();
        for (int i = 0; i < nodesNum; i++) {
            if (nodes[i].parent == index(parent)) {
                children.add(nodes[i]);
            }
        }
        return children;
    }

    // 树的度
    public int degreeForTree() {
        int max = 0;
        for (int i = 0; i < nodesNum; i++) {
            if (childrenFromNode(nodes[i]).size() > max) {
                max = childrenFromNode(nodes[i]).size();
            }
        }
        return max;
    }

    public int degreeForNode(Node<Item> node) {
        return childrenFromNode(node).size();
    }

    // 树的深度
    public int depth() {
        int max = 0;
        for (int i = 0; i < nodesNum; i++) {
            int currentDepth = 1;
            int parent = nodes[i].parent;
            while (parent != -1) {
                // 向上继续查找父结点，知道根结点
                parent = nodes[parent].parent;
                currentDepth++;
            }
            if (currentDepth > max) {
                max = currentDepth;
            }
        }
        return max;
    }


    // 树的结点数
    public int nodesNum() {
        return nodesNum;
    }

    // 返回根结点
    public Node<Item> root() {
        return nodes[0];
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
        // 按照以下定义，生成树
        List<String> datas = Arrays.asList("Bob", "Tom", "Jerry", "Rose", "Jack");
        List<Integer> parents = Arrays.asList(0, 0, 1, 2);

        TreeParent<String> tree = new TreeParent<>();
        tree.createTree(datas, parents);
        TreeParent.Node<String> root = tree.root();
        // root的第一个孩子
        TreeParent.Node<String> aChild = tree.childrenFromNode(root).get(0);
        System.out.println(aChild.getData() + "的父结点是" + tree.parentTo(aChild).getData());
        System.out.println("根结点的孩子" + tree.childrenFromNode(root));
        System.out.println("该树深度为" + tree.depth());
        System.out.println("该树的度为" + tree.degreeForTree());
        System.out.println("该树的结点数为" + tree.nodesNum());
        System.out.println(tree);


    }
}
