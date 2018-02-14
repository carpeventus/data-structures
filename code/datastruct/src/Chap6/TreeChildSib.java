package Chap6;

import java.util.ArrayList;
import java.util.List;

/**
 * 孩子兄弟表示法的树结构
 */
public class TreeChildSib<Item> {

    public static class Node<T> {
        private T data;
        private Node<T> nextChild;
        private Node<T> nextSib;

        public T getData() {
            return data;
        }

        public Node(T data) {
            this.data = data;
        }

        public Node<T> getNextChild() {
            return nextChild;
        }

        public Node<T> getNextSib() {
            return nextSib;
        }

        @Override
        public String toString() {
            String child = nextChild == null ? null : nextChild.getData().toString();
            String sib = nextSib == null ? null : nextSib.getData().toString();

            return "Node{" +
                    "data=" + data +
                    ", nextChild=" + child +
                    ", nextSib=" + sib +
                    '}';
        }
    }

    private Node<Item> root;
    // 存放所有结点，每次新增一个结点就add进来
    private List<Node<Item>> nodes = new ArrayList<>();

    // 以指定的根结点初始化树
    public TreeChildSib(Item data) {
        setRoot(data);
    }

    // 空参数构造器
    public TreeChildSib() {

    }

    public void setRoot(Item data) {
        root = new Node<>(data);
        nodes.add(root);
    }

    public void addChild(Item data, Node<Item> parent) {
        Node<Item> node = new Node<>(data);
        // 如果该parent是叶子结点，没有孩子
        if (parent.nextChild == null) {
            parent.nextChild = node;
            // parent有孩子了，只能放在n其第一个孩子的最后一个兄弟之后
        } else {
            // 从parent的第一个孩子开始，追溯到最后一个兄弟
            Node<Item> current = parent.nextChild;
            while (current.nextSib != null) {
                current = current.nextSib;
            }
            current.nextSib = node;
        }
        nodes.add(node);
    }

    public List<Node<Item>> childrenFromNode(Node<Item> node) {
        List<Node<Item>> children = new ArrayList<>();
        for (Node<Item> cur = node.nextChild; cur != null; cur = cur.nextSib) {
            {
                children.add(cur);
            }
        }
        return children;
    }

    public Node<Item> parentTo(Node<Item> node) {
        for (Node<Item> eachNode : nodes) {
            if (childrenFromNode(eachNode).contains(node)) {
                return eachNode;
            }
        }
        return null;
    }

    public boolean isEmpty() {
        return nodes.size() == 0;
    }

    public Node<Item> root() {
        return root;
    }

    public int nodesNum() {
        return nodes.size();
    }

    public int depth() {
        return nodeDepth(root);
    }

    public int nodeDepth(Node<Item> node) {
        if (node == null) {
            return 0;
        }

        int max = 0;
        if (childrenFromNode(node).size() > 0) {
            for (Node<Item> child : childrenFromNode(node)) {
                int depth = nodeDepth(child);
                if (depth > max) {
                    max = depth;
                }
            }
        }
        return max + 1;
    }

    public int degree() {

        int max = 0;
        for (Node<Item> node : nodes) {
            if (childrenFromNode(node).size() > max) {
                max = childrenFromNode(node).size();
            }
        }
        return max;
    }

    public int degreeForNode(Node<Item> node) {
        return childrenFromNode(node).size();
    }

    public void deleteNode(Node<Item> node) {
        if (node == null) {
            return;
        }
        deleteNode(node.nextChild);
        deleteNode(node.nextSib);
        node.nextChild = null;
        node.nextSib = null;
        node.data = null;
        nodes.remove(node);
    }

    public void clear() {
        deleteNode(root);
        root = null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Tree{\n");
        for (int i = 0; i < nodesNum() - 1; i++) {
            sb.append(nodes.get(i)).append(", \n");
        }
        sb.append(nodes.get(nodesNum() - 1)).append("}");
        return sb.toString();
    }

    public static void main(String[] args) {
        TreeChildSib<String> tree = new TreeChildSib<>("A");
        TreeChildSib.Node<String> root = tree.root();
        tree.addChild("B", root);
        tree.addChild("C", root);
        tree.addChild("D", root);
        TreeChildSib.Node<String> child1 = tree.childrenFromNode(root).get(0);
        TreeChildSib.Node<String> child2 = tree.childrenFromNode(root).get(1);
        TreeChildSib.Node<String> child3 = tree.childrenFromNode(root).get(2);
        tree.addChild("E", child1);
        tree.addChild("F", child2);
        tree.addChild("G", child1);
        tree.addChild("H", child3);

        System.out.println("该树结点数为" + tree.nodesNum());
        System.out.println("该树深度为" + tree.depth());
        System.out.println("该树的度为" + tree.degree());
        System.out.println(child1.getData() + "的度为" + tree.degreeForNode(child1));
        System.out.println(child2.getData() + "的父结点为" + tree.parentTo(child2).getData());

        tree.clear();
        System.out.println(child1);
        System.out.println(tree.isEmpty());
    }
}
