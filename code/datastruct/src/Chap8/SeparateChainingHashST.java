package Chap8;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 拉链法的散列表
 */
public class SeparateChainingHashST<Key, Value> {

    private int M; // 散列表的大小
    private int N; // 键值对的个数
    private SequentialST<Key, Value>[] st; // 存放链表的数组

    public SeparateChainingHashST(int M) {
        this.M = M;
        st = (SequentialST<Key, Value>[]) new SequentialST[M];
        // 每个索引都有一条链表
        for (int i = 0; i < st.length; i++) {
            st[i] = new SequentialST<>();
        }
    }

    public SeparateChainingHashST() {
        this(31);
    }

    private int hash(Key key) {
        return (key.hashCode() & 0x7fffffff) % M;
    }

    public void put(Key key, Value value) {
        if (N >= M / 2) {
            resize(2 * M);
        }
        st[hash(key)].put(key, value);
        N++;
    }

    public Value get(Key key) {
        return st[hash(key)].get(key);
    }

    public Value delete(Key key) {
        Value value = st[hash(key)].delete(key);
        N--;
        if (N > 0 && N <= M / 8) {
            resize(M / 2);
        }
        return value;
    }

    public Set<Key> keys() {
        Set<Key> keys = new HashSet<>();
        for (int i = 0; i < M; i++) {
            keys.addAll(st[i].keys());
        }
        return keys;
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public boolean contains(Key key) {
        return st[hash(key)].contains(key);
    }

    public int size() {
        return N;
    }

    private void resize(int max) {
        SeparateChainingHashST<Key, Value> temp = new SeparateChainingHashST<>(max);
        // 所有键值对重新插入，可保证每个索引的链表长度保持在一个小的常数范围内
        for (Key key : keys()) {
            temp.put(key, get(key));
        }
        // 更新散列表的大小
        M = temp.M;
        // 更新散列表
        st = temp.st;
    }

    @Override
    public String toString() {
        Iterator<Key> keys = keys().iterator();
        if (isEmpty()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        while (true) {
            Key key = keys.next();
            sb.append(key).append("=").append(get(key));
            if (!keys.hasNext()) {
                return sb.append("}").toString();
            } else {
                sb.append(", ");
            }
        }

    }

    public static void main(String[] args) {
        SeparateChainingHashST<String, Integer> a = new SeparateChainingHashST<>();
        a.put("a", 1);
        a.put("b", 2);
        a.put("c", 3);
        a.put("d", 4);

        a.delete("c");
        System.out.println(a.keys());
        System.out.println(a.size());
        System.out.println(a);
    }
}
