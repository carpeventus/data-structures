package Chap8;

import java.util.*;

/**
 * 二叉查找树
 */
public class BST<Key extends Comparable<Key>, Value> {

    private Node root;

    private class Node {
        private Key key;
        private Value value;
        private Node left, right;
        private int N; // 结点计数器，以该结点为根的子树结点总数

        public Node(Key key, Value value, int N) {
            this.key = key;
            this.value = value;
            this.N = N;
        }
    }
    public int size() {
        return size(root);
    }

    public int size(Key low, Key high) {
        if (high.compareTo(low) < 0) {
            return 0;
        }
        if (contains(high)) {
            return rank(high) - rank(low) + 1;
        } else {
            return rank(high) - rank(low);
        }
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean contains(Key key) {
        return get(key) != null;
    }

    private int size(Node node) {
        if (node == null) {
            return 0;
        } else {
            return node.N;
        }
    }
    // 递归实现
    public Value get(Key key) {
        return get(root, key);
    }
    // 非递归get
//    public Value get(Key key) {
//        Node cur = root;
//        while (cur != null) {
//            int cmp = key.compareTo(cur.key);
//            if (cmp < 0) {
//                cur = cur.left;
//            } else if (cmp > 0) {
//                cur = cur.right;
//            } else {
//                return cur.value;
//            }
//        }
//        return null;
//    }

    private Value get(Node node, Key key) {
        if (node == null) {
            return null;
        }
        // 和当前结点比较
        int cmp = key.compareTo(node.key);
        // 递归在左子树查找
        if (cmp < 0) {
            return get(node.left, key);
            // 递归在右子树查找
        } else if (cmp > 0) {
            return get(node.right, key);
            // 查找命中返回值
        } else {
            return node.value;
        }
    }

    public void put(Key key, Value value) {
        // 更新root
        // 第一次put：本来null的root = new Node
        // 以后的put：root = root
        root = put(root, key, value);
    }

    private Node put(Node node, Key key, Value value) {
        if (node == null) {
            return new Node(key, value, 1); // 新结点size当然是1
        }
        int cmp = key.compareTo(node.key);
        // 在node的左子树插入
        if (cmp < 0) {
            node.left = put(node.left, key, value);
            // 在node的右子树插入
        } else if (cmp > 0) {
            node.right = put(node.right, key, value);
            // 键已经存在，更新
        } else {
            node.value = value;
        }
        // 插入后更新以node为根的子树总结点数
        node.N = size(node.left) + size(node.right) + 1;
        // 除了第一次put返回新结点外，都是返回root
        return node;
    }
//    // 非递归min
//    public Key min() {
//        Node node = root;
//        while (node.left != null) {
//            node = node.left;
//        }
//        return node.key;
//    }
//    // 非递归max
//    public Key max() {
//        Node node = root;
//        while (node.right != null) {
//            node = node.right;
//        }
//        return node.key;
//    }
    // 递归实现min
    public Key min() {
        return min(root).key;
    }
    // 递归实现max
    public Key max() {
        return max(root).key;
    }

    private Node min(Node node) {
        if (node.left == null) {
            return node;
        } else {
            return min(node.left);
        }
    }

    private Node max(Node node) {
        if (node.right == null) {
            return node;
        } else {
            return max(node.right);
        }
    }

    public Key floor(Key key) {
        Node node = floor(root, key);
        if (node == null) {
            return null;
        } else {
            return node.key;
        }
    }

    private Node floor(Node node, Key key) {
        if (node == null) {
            return null;
        }
        int cmp = key.compareTo(node.key);
        // 和根结点相等直接返回根结点
        if (cmp == 0) {
            return node;
            // 比根结点小，肯定在左子树中
        } else if (cmp < 0) {
            return floor(node.left, key);
            // 比根结点大，若在右子树中就返回右子树相应结点，否则就是根结点本身
        } else {
            Node temp = floor(node.right, key);
            if (temp != null) {
                return temp;
            } else {
                return node;
            }
        }
    }

    public Key ceiling(Key key) {
        Node node = ceiling(root, key);
        if (node == null) {
            return null;
        } else {
            return node.key;
        }
    }

    private Node ceiling(Node node, Key key) {
        if (node == null) {
            return null;
        }
        int cmp = key.compareTo(node.key);
        // 和根结点相等直接返回根结点
        if (cmp == 0) {
            return node;
            // 比根结点大，肯定在右子树中
        } else if (cmp > 0) {
            return ceiling(node.right, key);
            // 比根结点小，若在左子树中就返回左子树相应结点，否则就是根结点本身
        } else {
            Node temp = ceiling(node.left, key);
            if (temp != null) {
                return temp;
            } else {
                return node;
            }
        }
    }

    public Key select(int k) {
        if (k < 0 || k >= size()) {
            throw new IllegalArgumentException("argument to select() is invalid: " + k);
        }
        return select(root, k).key;
    }

    private Node select(Node node, int k) {
        if (node == null) {
            return null;
        }
        int t = size(node.left);
        // 左子树的结点数大于k，继续在左子树查找
        if (t > k) {
            return select(node.left, k);
            // 左子树结点数小于k，得在右子树查找
        } else if (t < k) {
            return select(node.right, k - t - 1);
            // 左子树的结点数刚好等于k，返回根结点
        } else {
            return node;
        }
    }

    public int rank(Key key) {
        return rank(root, key);
    }

    private int rank(Node node, Key key) {
        if (node == null) {
            return 0;
        }
        int cmp = key.compareTo(node.key);
        // 比根结点小，应该在左子树中继续查找
        if (cmp < 0) {
            return rank(node.left, key);
            // 比根结点大，应该在右子树中查找，算排名时加上左子树和根结点的结点总和
        } else if (cmp > 0) {
            return 1 + size(node.left) + rank(node.right, key);
            // 和根结点相等，找到，排名就是其左子树结点总数
        } else {
            return size(node.left);
        }
    }

    public void deleteMin() {
        root = deleteMin(root);
    }

    private Node deleteMin(Node node) {
        if (node.left == null) {
            return node.right;
        }
        // node.left = node.left.right
        node.left = deleteMin(node.left);
        node.N = size(node.left) + size(node.right) + 1;
        return node;
    }

    public void deleteMax() {
        root = deleteMax(root);
    }

    private Node deleteMax(Node node) {
        if (node.right == null) {
            return node.left;
        }
        node.right = deleteMax(node.right);
        node.N = size(node.left) + size(node.right) + 1;
        return node;
    }

    public void delete(Key key) {
        root = delete(root, key);
    }

    private Node delete(Node node, Key key) {
        if (node == null) {
            return null;
        }
        int cmp = key.compareTo(node.key);
        // key大于当前根结点，在右子树查找
        if (cmp > 0) {
            node.right = delete(node.right, key);
            // key小于当前根结点，在左子树查找
        } else if (cmp < 0) {
            node.left = delete(node.left, key);
            // 找到给定的key
        } else {
            // 如果根结点只有一个子结点或者没有子结点，按照删除最小最大键的做法即可
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }
            // 根结点的两个子结点都不为空
            // 要删除的结点用t保存
            Node t = node;
            // t的后继结点取代t的位置
            node = min(t.right);
            node.right = deleteMin(t.right);
            node.left = t.left;

        }
        node.N = size(node.left) + size(node.right) + 1;
        return node;
    }

    public Set<Key> keys() {
        return keys(min(), max());
    }

    public Set<Key> keys(Key low, Key high) {
        Set<Key> set = new LinkedHashSet<>();
        keys(root, set, low, high);
        return set;
    }

    private void keys(Node node, Set<Key> set, Key low, Key high) {
        if (node == null) {
            return;
        }
        int cmplow = low.compareTo(node.key);
        int cmphigh = high.compareTo(node.key);
        // 当前结点比low大，左子树中可能还有结点落在范围内的，所以应该遍历左子树
        if (cmplow < 0) {
            keys(node.left, set, low, high);
        }
        // 在区间[low, high]之间的加入队列
        if (cmplow <= 0 && cmphigh >= 0) {
            set.add(node.key);
        }
        // 当前结点比high小，右子树中可能还有结点落在范围内，所以应该遍历右子树
        if (cmphigh > 0) {
            keys(node.right, set, low, high);
        }
    }

    private void values(Node node, List<Value> list,Key low, Key high) {
        if (node == null) {
            return;
        }
        int cmplow = low.compareTo(node.key);
        int cmphigh = high.compareTo(node.key);
        // 当前结点比low大，左子树中可能还有结点落在范围内的，所以应该遍历左子树
        if (cmplow < 0) {
            values(node.left, list, low, high);
        }
        // 在区间[low, high]之间的加入队列
        if (cmplow <= 0 && cmphigh >= 0) {
            list.add(node.value);
        }
        // 当前结点比high小，右子树中可能还有结点落在范围内，所以应该遍历右子树
        if (cmphigh > 0) {
            values(node.right, list, low, high);
        }
    }

    public Collection<Value> values(Key low, Key high) {
        List<Value> values = new ArrayList<>();
        values(root, values,low, high);
        return values;
    }

    public Collection<Value> values() {
        return values(min(), max());
    }


    @Override
    public String toString() {
        Iterator<Key> keys = keys().iterator();
        Iterator<Value> values = values().iterator();
        if (!keys.hasNext()) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        while (true) {
            Key key = keys.next();
            Value value = values.next();
            sb.append(key).append("=").append(value);
            if (!keys.hasNext()) {
                return sb.append("}").toString();
            }
            sb.append(", ");
        }
    }

    public static void main(String[] args) {
        BST<Integer, Double> st = new BST<>();
        st.put(1, 5567.5);
        st.put(5, 10000.0);
        st.put(3, 4535.5);
        st.put(7, 7000.0);
        st.put(12, 2500.0);
        st.put(10, 4500.0);
        st.put(17, 15000.5);
        st.put(15, 12000.5);
        st.deleteMax(); // 17
        st.deleteMin(); // 1
        st.delete(12); // 剩下[3, 5, 7, 10, 15]

        st.delete(3);
        st.delete(7);
        st.delete(15);
        st.delete(10);
        System.out.println("符号表的长度为" + st.size());
        System.out.println("[3, 6]之间有" + st.size(3, 6) + "个键");
        System.out.println("比9小的键的数量为" + st.rank(9));
//        System.out.println("排在第4位置的键为" + st.select(1));
        System.out.println("大于等于8的最小键为" + st.ceiling(8));
        System.out.println("小于等于8的最大键为" + st.floor(8));

        System.out.println("符号表所有的键和对应的值为：" + st.keys() + " -> " + st.values());
        System.out.println("键2和键8之间的所有键及对应的值：" + st.keys(2, 8) + " -> " + st.values(2, 8));

        System.out.println(st);
        /*
         *  符号表的长度为5
            [3, 6]之间有2个键
            比9小的键的数量为3
            排在第4位置的键为15
            大于等于8的最小键为10
            小于等于8的最大键为7
            符号表所有的键和对应的值为：[3, 5, 7, 10, 15] -> [4535.5, 10000.0, 7000.0, 4500.0, 12000.5]
            键2和键8之间的所有键及对应的值：[3, 5, 7] -> [4535.5, 10000.0, 7000.0]
            {3=4535.5, 5=10000.0, 7=7000.0, 10=4500.0, 15=12000.5}
         */

    }
}
