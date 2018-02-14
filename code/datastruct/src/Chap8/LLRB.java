package Chap8;

import java.util.*;

/**
 * （左斜）红黑树
 */
public class LLRB<Key extends Comparable<Key>, Value> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;
    private Node root;

    private class Node {
        private Key key;
        private Value value;
        private Node left, right;
        private int N; // 结点计数器，以该结点为根的子树结点总数
        private boolean color;

        public Node(Key key, Value value, int N, boolean color) {
            this.key = key;
            this.value = value;
            this.N = N;
            this.color = color;
        }
    }

    public boolean isRed(Node x) {
        // 约定空链接为黑色
        if (x == null) {
            return BLACK;
        } else {
            return x.color == RED;
        }
    }

    private Node rotateLeft(Node h) {
        Node x = h.right; // 根结点的右子结点保存为x
        // 其实就是h和x互换位置
        h.right = x.left; // 根结点的右子结点的左孩子挂到根结点的右子结点上
        x.left = h; // 根结点挂到根结点右子结点的左子结点上
        x.color = h.color; // 原来h是什么颜色，换过去的x也应该是什么颜色
        h.color = RED;     // 将红色右链接变成红色左链接，因此x是红色的，h和x互换位置所以换过去的h也应该是RED
        x.N = h.N;  // x的结点数和h保持一致
        h.N = size(h.left) + size(h.right) + 1; // 这里不能用原x.N赋值给h.N，因为旋转操作后原来x的子树和现在h的子树不一样
        // 返回取代h位置的结点x，h = rotateLeft(Node h)就表示x取代了h
        return x;
    }

    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = RED; // x原来是红色的
        x.N = h.N;
        h.N = size(h.left) + size(h.right) + 1;

        return x;
    }

    // 非递归get
    public Value get(Key key) {
        Node cur = root;
        while (cur != null) {
            int cmp = key.compareTo(cur.key);
            if (cmp < 0) {
                cur = cur.left;
            } else if (cmp > 0) {
                cur = cur.right;
            } else {
                return cur.value;
            }
        }
        return null;
    }

    private void flipColors(Node h) {
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }

    public void put(Key key, Value value) {
        root = put(root, key, value);
        root.color = BLACK;
    }

    private Node put(Node h, Key key, Value value) {
        if (h == null) {
            return new Node(key, value, 1, RED);
        }
        int cmp = key.compareTo(h.key);
        if (cmp < 0) {
            h.left = put(h.left, key, value);
        } else if (cmp > 0) {
            h.right = put(h.right, key, value);
        } else {
            h.value = value;
        }

        /*
           下面连续三个判断是和标准二叉查找树put方法不同的地方，目的是修正红链接
         */
        // 如果右子结点是红色的而左子结点是黑色的，进行左旋转
        // 之后返回值赋给h是让x取代原h的位置，不可少
        if (isRed(h.right) && !isRed(h.left)) {
            h = rotateLeft(h);
        }
        // 如果右子结点是红色的而左子结点是黑色的，进行左旋转
        if (isRed(h.left) && isRed(h.left.left)) {
            h = rotateRight(h);
        }
        // 如果左右子结点均为红色，进行颜色反转
        if (isRed(h.left) && isRed(h.right)) {
            flipColors(h);
        }

        h.N = size(h.left) + size(h.right) + 1;
        return h;
    }

    private Node fixUp(Node h) {
        if (isRed(h.right) && !isRed(h.left)) {
            h = rotateLeft(h);
        }
        // 如果右子结点是红色的而左子结点是黑色的，进行左旋转
        if (isRed(h.left) && isRed(h.left.left)) {
            h = rotateRight(h);
        }
        // 如果左右子结点均为红色，进行颜色反转
        if (isRed(h.left) && isRed(h.right)) {
            flipColors(h);
        }

        h.N = size(h.left) + size(h.right) + 1;
        return h;
    }

    private Node moveRedLeft(Node h) {
        // 当此方法被调用时，h是红色的，h.left和h.left.left都是黑色的
        // 整个方法结束后h.left或者h.left.left其中之一被变成RED
        flipColors(h);
        // 从兄弟结点借一个键
        if (isRed(h.right.left)) {
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
            flipColors(h);
        }
        return h;
    }

    public void deleteMin() {
        // 这里将root设置为红色是为了和moveRedLeft里的处理一致
        // 即当前结点h是红色的，其两个子结点都是黑色的，在反色后，当前结点h变成黑色，而它的两个子结点变成红色
        if (!isRed(root.left) && !isRed(root.right)) {
            root.color = RED;
        }
        root = deleteMin(root);
        // 根结点只要不为空，删除操作后保持始终是黑色的
        if (!isEmpty()) {
            root.color = BLACK;
        }
    }

    private Node deleteMin(Node h) {
        if (h.left == null) {
            // 不像标准二叉查找树那样返回h.right, 因为put方法就决定了h.left和h.right同时为空或者同时不为空
            return null;
        }
        // 合并成4-结点或者从兄弟结点中借一个过来
        if (!isRed(h.left) && !isRed(h.left.left)) {
            h = moveRedLeft(h);
        }

        h.left = deleteMin(h.left);
        return fixUp(h);
    }

    private Node moveRedRight(Node h) {
        flipColors(h);
        // 从兄弟结点借一个键
        if (isRed(h.left.left)) {
            h = rotateRight(h);
            flipColors(h);
        }
        return h;
    }

    public void deleteMax() {
        if (!isRed(root.left) && !isRed(root.right)) {
            root.color = RED;
        }
        root = deleteMax(root);
        if (!isEmpty()) {
            root.color = BLACK;
        }
    }

    private Node deleteMax(Node h) {
        // 为了和deleteMin对称处理，先将红色左链接转换成红色右链接
        // 转换为红色右链接是最先处理的！
        if (isRed(h.left)) {
            h = rotateRight(h);
        }
        // 这个判断不能再上句之前，因为可能旋转前h.right是null，旋转后可就不是null了
        if (h.right == null) {
            return null;
        }
        // 这里条件中不是h.right.right，因为3-结点是左链接表示的
        if (!isRed(h.right) && !isRed(h.right.left)) {
            h = moveRedRight(h);
        }
        h.right = deleteMax(h.right);
        return fixUp(h);
    }

    public void delete(Key key) {
        if (!contains(key)) {
            return;
        }

        if (!isRed(root.left) && !isRed(root.right)) {
            root.color = RED;
        }

        root = delete(root, key);

        if (!isEmpty()) {
            root.color = BLACK;
        }
    }

    private Node delete(Node h, Key key) {
        if (key.compareTo(h.key) < 0) {
            if (!isRed(h.left) && !isRed(h.left.left)) {
                h = moveRedLeft(h);
            }
            h.left = delete(h.left, key);
        } else { // 要么在根结点或者右子树，两种情况包含在一起了
            // 要在右子树处理，所以确保是红色右链接
            if (isRed(h.left)) {
                h = rotateRight(h);
            }

            // 要删除的结点在树底
            if (key.compareTo(h.key) == 0 && (h.right == null)) {
                return null;
            }
            // 这个判断必须在上个判断之后，因为确保h.right不为空后才能调用h.right.left
            if (!isRed(h.right) && !isRed(h.right.left)) {
                h = moveRedRight(h);
            }
            // 要删除不在树底,用它的后继结点的信息更新它后，删除后继结点
            if (key.compareTo(h.key) == 0) {
               Node x = min(h.right);
               h.key = x.key;
               h.value = x.value;
               h.right = deleteMin(h.right);
                // 没有相等的键，在右子树中递归
            } else {
                h.right = delete(h.right, key);
            }
        }
        return fixUp(h);
    }

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

    public int size() {
        return size(root);
    }


    private int size(Node node) {
        if (node == null) {
            return 0;
        } else {
            return node.N;
        }
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

    public Set<Key> keys() {
        if (isEmpty()) {
            return new LinkedHashSet<>();
        }
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

    private void values(Node node, List<Value> list, Key low, Key high) {
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
        values(root, values, low, high);
        return values;
    }

    public Collection<Value> values() {
        if (isEmpty()) {
            return new ArrayList<>();
        }
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
        LLRB<Integer, Double> rbt = new LLRB<>();
        rbt.put(1, 5567.5);
        rbt.put(5, 10000.0);
        rbt.put(3, 4535.5);
        rbt.put(7, 7000.0);
        rbt.put(12, 2500.0);
        rbt.put(10, 4500.0);
        rbt.put(17, 15000.5);
        rbt.put(15, 12000.5);
        rbt.deleteMax(); // 17
        rbt.deleteMin(); // 1

        rbt.delete(12); // 剩下[3, 5, 7, 10, 15]
        rbt.delete(3);

        System.out.println("符号表的长度为" + rbt.size());
        System.out.println("[3, 6]之间有" + rbt.size(3, 6) + "个键");
        System.out.println("比9小的键的数量为" + rbt.rank(9));
        System.out.println("排在第4位置的键为" + rbt.select(1));
        System.out.println("大于等于8的最小键为" + rbt.ceiling(8));
        System.out.println("小于等于8的最大键为" + rbt.floor(8));
        System.out.println("符号表所有的键和对应的值为：" + rbt.keys() + " -> " + rbt.values());
        System.out.println("键2和键8之间的所有键及对应的值：" + rbt.keys(2, 8) + " -> " + rbt.values(2, 8));
        System.out.println(rbt);
    }
}