package Chap5;

import java.util.LinkedList;
import java.util.Queue;

/**
 * （R向）单词查找树
 *
 * @param <Value> 字符串键对应的值
 */
public class TrieST<Value> {
    private static int R = 256;

    private Node root;
    private int N; // 记录查找树的键的总数

    private static class Node {
        private Object val;
        private Node[] next = new Node[256];
    }

    public Value get(String key) {
        Node x = get(root, key, 0);
        if (x == null) {
            return null;
        }
        return (Value) x.val;
    }

    // 返回以字符串key为首的子树(如果key在符号表中，否则返回null)
    private Node get(Node node, String key, int d) {
        if (node == null) {
            return null;
        }
        // d记录了单词查找树的层数, 如果定义在根结点root时为0（树的层数一般定义是根结点处是第一层）。
        // root没有保存字符，所以d = 1表示字符串的第0个字符d = key.length表示字符串最后一个字符key.length - 1
        if (d == key.length()) {
            return node;
        }
        char c = key.charAt(d);
        return get(node.next[c], key, d + 1);
    }

    public boolean contains(String key) {
        return get(key) != null;
    }

    public void put(String key, Value value) {
        root = put(root, key, value, 0);
    }

    private Node put(Node node, String key, Value value, int d) {
        // 为每个未检查的字符新建一个结点
        if (node == null) {
            node = new Node();
        }
        // 并将值保存在最后一个字符中
        if (d == key.length()) {
            // 为空说明插入新键，不为空说明是更新值
            if (node.val == null) {
                N++;
            }
            // 不管为不为空，都会设置值
            node.val = value;
            return node;
        }

        char c = key.charAt(d);
        node.next[c] = put(node.next[c], key, value, d + 1);
        return node;
    }

    public int size() {
        return N;
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public Iterable<String> keys() {
        // 前缀为空，说明任何字符都可以入列，该方法收集所有字符串

        // ！另外一种实现，一行就可以了
        // return keyWithPrefix("");
        Queue<String> queue = new LinkedList<>();
        collect(root, "", queue);
        return queue;
    }

    public Iterable<String> keyWithPrefix(String pre) {
        Queue<String> queue = new LinkedList<>();
        // 先找出给定前缀的子树，该子树包含了所有以给定前缀开头的字符串，从中收集并保存在队列中
        collect(get(root, pre, 0), pre, queue);
        return queue;
    }

    private void collect(Node node, String pre, Queue<String> queue) {
        if (node == null) {
            return;
        }
        // 如果值不为空，说明到达某字符串的尾字符，应该保存该字符串
        if (node.val != null) {
            queue.offer(pre);
        }

        for (char c = 0; c < R; c++) {
            // 这里pre + c是字符的拼接，c是一个字符不要当成了数字
            collect(node.next[c], pre + c, queue);
        }
    }

    public Iterable<String> keysThatMatch(String pat) {
        Queue<String> queue = new LinkedList<>();
        collect(root, "", pat, queue);
        return queue;
    }

    private void collect(Node node, String pre, String pat, Queue<String> queue) {
        int d = pre.length();

        if (node == null) {
            return;
        }
        // 和通配模式字符串的长度要一致，且值不为空才会被加入队列
        if (d == pat.length() && node.val != null) {
            queue.offer(pre);
        }
        // 检查到通配模式字符串的长度就行了
        if (d == pat.length()) {
            return;
        }

        char next = pat.charAt(d);
        for (char c = 0; c < R; c++) {
            // 是*就将结点数组next中所有字符都递归收集，或者指定了字符，就按照指定的字符来递归收集
            if (next == '*' || next == c) {
                collect(node.next[c], pre + c, pat, queue);
            }
        }
    }

    // 返回给定字符串在符号表中存在且拥有最长前缀的字符串
    public String longestPrefixOf(String s) {
        int length = search(root, s, 0, 0);
        return s.substring(0, length);
    }

    private int search(Node node, String s, int d, int length) {
        // 遇到空链接了，返回路径上最近的一个键
        if (node == null) {
            return length;
        }
        // 不为空说明符号表中存在这个字符串，是当前给定字符串的最长前缀，更新length
        if (node.val != null) {
            length = d;
        }
        // 到达给定字符串末尾，返回最长前缀的长度
        if (d == s.length()) {
            return length;
        }

        char c = s.charAt(d);
        return search(node.next[c], s, d + 1, length);
    }

    public void delete(String key) {
        root = delete(root, key, 0);
    }

    private Node delete(Node node, String key, int d) {
        if (node == null) {
            return null;
        }

        // 到达给定字符串末尾，停止递归
        if (d == key.length()) {
            // 要删除的键确实存在于符号表中才减小个数
            if (node.val != null) {
                node.val = null;
                N--;
            }
        } else {
            char c = key.charAt(d);
            // 没有到字符串末尾就递归删除
            node.next[c] = delete(node.next[c], key, d + 1);
        }

        // 接下来检查子树，如果结点值不为空，不能删除
        if (node.val != null) {
            return node;
        }
        // 如果结点值为空，但是该结点有链接不为空，不能删除
        for (char c = 0; c < R; c++) {
            if (node.next[c] != null) {
                return node;
            }
        }

        // 不是以上两种情况，说明结点的值为空，而且它的所有链接都为空，可以删除
        return null;
    }

    public static void main(String[] args) {
        TrieST<Integer> trieST = new TrieST<>();
        trieST.put("she", 0);
        trieST.put("sells", 1);
        trieST.put("sea", 2);
        trieST.put("shells", 3);
        trieST.put("by", 4);
        trieST.put("the", 5);
        trieST.put("sea", 6);
        trieST.put("shore", 7);
        System.out.println(trieST.keys());
        System.out.println(trieST.get("sea"));
        System.out.println(trieST.get("she"));
        System.out.println(trieST.get("shells"));

        System.out.println(trieST.keyWithPrefix("she"));
        System.out.println(trieST.keysThatMatch("s**"));
        System.out.println(trieST.longestPrefixOf("shell"));
        System.out.println(trieST.longestPrefixOf("shells"));
        System.out.println(trieST.longestPrefixOf("shellsort"));

        trieST.delete("she");
        System.out.println(trieST.get("shells"));
        trieST.delete("shells");
        System.out.println(trieST.keys());

        System.out.println(trieST.size());
    }
}
