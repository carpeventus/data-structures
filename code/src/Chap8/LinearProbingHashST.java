package Chap8;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 线性探测法的散列表
 */
public class LinearProbingHashST<Key, Value> {
    private int N; // 键值对的个数
    private int M; // 散列表的大小
    private Key[] keys;
    private Value[] values;

    public LinearProbingHashST(int cap) {
        M = cap;
        keys = (Key[]) new Object[cap];
        values = (Value[]) new Object[cap];
    }

    public LinearProbingHashST() {
        this(31);
    }

    private int hash(Key key) {
        return (key.hashCode() & 0x7fffffff) % M;
    }

    public void put(Key key, Value value) {
        if (N >= M / 2) {
            resize(2 * M);
        }
        int i;
        // 碰撞冲突，看下一个位置，如果这个过程中发现键已经存在，则更新并直接返回
        for (i = hash(key); keys[i] != null; i = (i + 1) % M) {
            if (keys[i].equals(key)) {
                values[i] = value;
                return;
            }
        }
        // 若干位置后的第一个空位，插入新键值对
        keys[i] = key;
        values[i] = value;
        N++;
    }

    public Value get(Key key) {
        // 碰撞冲突，看下一个位置，如果这个过程中发现键已经存在，则更新并直接返回
        for (int i = hash(key); keys[i] != null; i = (i + 1) % M) {
            if (keys[i].equals(key)) {
                return values[i];
            }
        }
        return null;
    }

    public Value delete(Key key) {
        Value value = null;
        int i = hash(key);
        // 碰撞冲突，看下一个位置，如果这个过程中发现键已经存在，则更新并直接返回
        while (keys[i] != null) {
            if (keys[i].equals(key)) {
                value = values[i];
                break;
            }
            i = (i + 1) % M;
        }
        // 找到键了，删除键值对
        keys[i] = null;
        values[i] = null;
        // 删除后，这条键簇中，i之后的键值对都需要重新插入
        // 因为get方法终止循环的条件是keys[i] != null，删除后键簇中那个位置是空位，之后的键都访问不到了
        i = (i + 1) % M; // i之后的第一个位置
        // 对这条键簇i之后的进行重新插入
        while (keys[i] != null) {
            Key keyRedo = keys[i];
            Value valueRedo = values[i];
            // 删了再插入
            keys[i] = null;
            values[i] = null;
            N--;
            put(keyRedo, valueRedo);

            i = (i + 1) % M;
        }

        N--;
        if (N > 0 && N <= M / 8) {
            resize(M / 2);
        }
        return value;
    }

    private void resize(int max) {
        LinearProbingHashST<Key, Value> temp = new LinearProbingHashST<>(max);
        // 所有键值对重新插入，可保证每个索引的链表长度保持在一个小的常数范围内
        for (int i = 0; i < M; i++) {
            if (keys[i] != null) {
                temp.put(keys[i], values[i]);
            }
        }
        // 更新散列表的大小
        M = temp.M;
        // 更新散列表的键和值
        keys = temp.keys;
        values = temp.values;
    }

    public Set<Key> keys() {
        Set<Key> keySet = new HashSet<>();
        for (int i = 0; i < M; i++) {
            if (keys[i] != null) {
                keySet.add(keys[i]);
            }
        }
        return keySet;
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public int size() {
        return N;
    }

    public boolean contains(Key key) {
        return get(key) != null;
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
        LinearProbingHashST<String, Integer> a = new LinearProbingHashST<>();
        a.put("a", 1);
        a.put("b", 2);
        a.put("c", 3);
        a.put("d", 4);

        a.delete("c");
        System.out.println(a.keys());
        System.out.println(a.size());
        System.out.println(a);
        System.out.println(a.get("b"));
    }
}
