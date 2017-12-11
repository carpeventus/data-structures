package Chap8;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 基于无序链表的顺序查找符号表
 */
public class SequentialST<Key, Value> {
    private Node first;
    private int N;

    private class Node {
        Key key;
        Value value;
        Node next;

        public Node(Key key, Value value, Node next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public Value get(Key key) {
        for (Node cur = first; cur != null; cur = cur.next) {
            if (key.equals(cur.key)) {
                return cur.value;
            }
        }
        return null;
    }
    // 若是键不存在，则返回一个指定的默认值
    public Value getDefault(Key key, Value value) {
        for (Node cur = first; cur != null; cur = cur.next) {
            if (key.equals(cur.key)) {
                return cur.value;
            }
        }
        return value;
    }

    public void put(Key key, Value value) {
        // 如果传入null，就是删除键值对
        if (value == null) {
            delete(key);
            return;
        }

        for (Node cur = first; cur != null; cur = cur.next) {
            if (key.equals(cur.key)) {
                cur.value = value;
                return;
            }
        }
        /*  新键值对。new Node的next指向first，然后取代它成为新的first, 是以下代码的简化版

            Node oldfirst = first;
            first = new Node();
            first.next = oldfirst;
        */
        first = new Node(key, value, first);
        N++;
    }
// 未采用的延时删除，使用下面的delete
//    public void delete(Key key) {
//        put(key, null);
//    }

    public Value delete(Key key) {
        if (isEmpty()) {
            return null;
        }

        Node cur = first;
        Value value = null;
        // 删除的键值对如果在链表头，处理方式不一样
        if (key.equals(cur.key)) {
            value = cur.value;
            Node next = cur.next;
            // 下面三行是帮助垃圾回收
            cur.key = null;
            cur.value = null;
            cur.next = null;
            first = next;
            N--;
        } else {
            // 现在pre是first，而cur是first的下一个结点
            Node pre = cur;
            cur = cur.next;

            while (cur != null) {
                if (key.equals(cur.key)) {
                    value = cur.value;
                    Node next = cur.next;
                    // 下面三行是帮助垃圾回收
                    cur.key = null;
                    cur.value = null;
                    cur.next = null;
                    pre.next = next;
                    N--;
                    return value;
                }
                // 下轮比较指向下一个结点，所以更新pre和cur
                pre = cur;
                cur = cur.next;
            }
        }
        return value;
    }

    public Set<Key> keys() {
        // 保证和values是一样的顺序
        Set<Key> keys = new LinkedHashSet<>();
        for (Node cur = first;cur != null;cur = cur.next) {
            keys.add(cur.key);
        }
        return keys;
    }

    public Collection<Value> values() {
        Collection<Value> values = new ArrayList<>();
        for (Node cur = first;cur != null;cur = cur.next) {
            values.add(cur.value);
        }
        return values;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        Node cur = first;

        while (true) {
            sb.append(cur.key).append("=").append(get(cur.key));
            if (cur.next == null) {
                return sb.append("}").toString();
            } else {
                sb.append(", ");
            }
            cur = cur.next;
        }
    }
    public boolean contains(Key key) {
        return get(key) != null;
    }

    public int size() {
        return N;
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public static void main(String[] args) {
        SequentialST<String, Integer> st = new SequentialST<>();
        st.put("admin", 8888);
        st.put("password", 123456);
        st.put("pcNumber", 5);
        st.put("money", 6666);
        Integer password = st.delete("password");
        st.delete("money");
        System.out.println(password);

        System.out.println(st.get("pcNumber"));
        System.out.println(st.get("admin"));
        System.out.println(st.keys());
        System.out.println(st.values());
        System.out.println(st);
    }
}