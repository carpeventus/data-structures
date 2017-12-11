package Chap5;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 3向单词查找树
 *
 * @param <Value> 字符串键对应的值
 */
public class TST<Value> {
    private Node root;
    private int N;

    private class Node {
        char c; // 显式保存的字符
        Node mid, left, right; // 左中右子树
        Value val; // 和字符串关联的值
    }

    // 和R向单词查找树的实现一样
    public Value get(String key) {
        Node x = get(root, key, 0);
        if (x == null) {
            return null;
        }
        return x.val;
    }

    private Node get(Node node, String key, int d) {
        if (node == null) {
            return null;
        }

        char c = key.charAt(d);
        // 要查找的字符比当前字符小，在左子树中查找
        if (c < node.c) {
            return get(node.left, key, d);
            // 要查找的字符比当前字符大，在右子树中查找
        } else if (c > node.c) {
            return get(node.right, key, d);
            // c = node.c的前提下（要查找的字符和当前字符相等）但还没到尾字符，在中子树中查找下一个字符
        } else if (d < key.length() - 1) {
            return get(node.mid, key, d + 1);
        } else {
            return node;
        }
    }

    public void put(String key, Value val) {
        root = put(root, key, val, 0);
    }

    private Node put(Node node, String key, Value val, int d) {
        char c = key.charAt(d);
        if (node == null) {
            node = new Node();
            node.c = c;
        }
        // 要查找的字符比当前字符小，在左子树中查找
        if (c < node.c) {
            node.left = put(node.left, key, val, d);
            // 要查找的字符比当前字符大，在右子树中查找
        } else if (c > node.c) {
            node.right = put(node.right, key, val, d);
            // c = node.c的前提下（要查找的字符和当前字符相等）但还没到尾字符，在中子树中查找下一个字符
        } else if (d < key.length() - 1) {
            node.mid = put(node.mid, key, val, d + 1);
        } else {
            // 为空说明插入新键，不为空说明是更新值
            if (node.val == null) {
                N++;
            }
            // 不管为不为空，都会设置值
            node.val = val;
        }

        return node;
    }

    public boolean contains(String key) {
        return get(key) != null;
    }

    public int size() {
        return N;
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public Iterable<String> keys() {
        Queue<String> queue = new LinkedList<>();
        collect(root, "", queue);
        return queue;
    }

    public Iterable<String> keyWithPrefix(String pre) {
        Queue<String> queue = new LinkedList<>();
        Node x = get(root, pre, 0);
        if (x == null) {
            return queue;
        }
        // 如果get返回的这个结点值不为空，加入队列
        if (x.val != null) {
            queue.offer(pre);
        }
        // 对其中子树递归
        collect(x.mid, pre, queue);
        return queue;
    }

    private void collect(Node node, String pre, Queue<String> queue) {
        if (node == null) {
            return;
        }

        collect(node.left, pre, queue);
        // 如果值不为空，说明到达某字符串的尾字符，应该保存该字符串
        if (node.val != null) {
            // pre不包含当前字符node.c，所以要加上
            queue.offer(pre + node.c);
        }
        // 凡是对中子树的处理，表示检查下一个字符，所有加上当前字符node.c
        collect(node.mid, pre + node.c, queue);
        collect(node.right, pre, queue);
    }
    // 和R向单词查找树的实现一样
    public Iterable<String> keysThatMatch(String pat) {
        Queue<String> queue = new LinkedList<>();
        collect(root, "", pat, 0, queue);
        return queue;
    }

    private void collect(Node node, String pre, String pat, int d, Queue<String> queue) {
        if (node == null) {
            return;
        }
        char next = pat.charAt(d);
        // 左子树收集
        if (next == '*' || next < node.c) {
            collect(node.left, pre, pat, d, queue);
        }
        // 中子树收集
        if (next == '*' || next == node.c) {
            // 和通配模式字符串的长度一致，且值不为空才会被加入队列
            if (d == pat.length() - 1 && node.val != null) {
                queue.offer(pre + node.c);
                // 该条件保证了d == pat.length() - 1不会继续收集
            } else if (d < pat.length() - 1) {
                collect(node.mid, pre + node.c, pat, d + 1, queue);
            }
        }
        // 右子树收集
        if (next == '*' || next > node.c) {
            collect(node.right, pre, pat, d, queue);
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
            // 和TriesST不同，这里root存放了字符。所以d就是字符索引，和字符串总长度相差1；如索引3，表示长度为4。
            length = d + 1;
        }
        // 到达给定字符串末尾，返回最长前缀的长度
        if (d == s.length()-1) {
            return length;
        }

        char c = s.charAt(d);
        if (c < node.c) {
            return search(node.left, s, d, length);
        } else if (c > node.c) {
            return search(node.right, s, d, length);
        } else {
            return search(node.mid, s, d + 1, length);
        }
    }

    public static void main(String[] args) {
        TST<Integer> tST = new TST<>();
        tST.put("she", 0);
        tST.put("sells", 1);
        tST.put("sea", 2);
        tST.put("shells", 3);
        tST.put("by", 4);
        tST.put("the", 5);
        tST.put("sea", 6);
        tST.put("shore", 7);
        System.out.println(tST.keys());
        System.out.println(tST.get("sea"));
        System.out.println(tST.get("she"));
        System.out.println(tST.get("shells"));

        System.out.println(tST.keyWithPrefix("she"));
        System.out.println(tST.keysThatMatch("s**"));

        System.out.println(tST.longestPrefixOf("shell"));
        System.out.println(tST.longestPrefixOf("shells"));
        System.out.println(tST.longestPrefixOf("shellsort"));

        System.out.println(tST.size());
    }
}
