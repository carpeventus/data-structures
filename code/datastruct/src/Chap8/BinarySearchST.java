package Chap8;

import java.util.*;

/**
 * 基于有序数组的二分查找符号表
 */
public class BinarySearchST<Key extends Comparable<Key>, Value> {
    private Key[] keys = (Key[]) new Comparable[1];
    private Value[] values = (Value[]) new Object[1];
    private int N;

    public int rank(Key key) {
        int low = 0;
        int high = N - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            int cmp = key.compareTo(keys[mid]);
            if (cmp < 0) {
                high = mid - 1;
            } else if (cmp > 0) {
                low = mid + 1;
            } else {
                return mid;
            }
        }
        return low;
    }

    public void put(Key key, Value value) {
        // 如果传入null，就是删除键值对
        if (value == null) {
            delete(key);
            return;
        }

        // 如果容量满了，增容
        if (N == keys.length) {
            resize(2 * keys.length);
        }

        int i = rank(key);
        // 键已经存在，新值取代旧值
        if (i < N && keys[i].compareTo(key) == 0) {
            values[i] = value;
            return;
        }
        // 否则插入新的键值对，i及之后的元素都需要后移一个位置，腾出位置i给新的键值对使用
        for (int j = N; j > i; j--) {
            keys[j] = keys[j - 1];
            values[j] = values[j - 1];
        }
        keys[i] = key;
        values[i] = value;
        N++;
    }

    public Value get(Key key) {
        if (isEmpty()) {
            return null;
        }
        int i = rank(key);
        if (i < N && keys[i].compareTo(key) == 0) {
            return values[i];
        } else {
            return null;
        }
    }

    public Value getDefault(Key key, Value defaultValue) {
        if (get(key) == null) {
            return defaultValue;
        } else {
            return get(key);
        }
    }

    private void resize(int max) {
        Key[] tempKeys = (Key[]) new Comparable[max];
        Value[] tempValues = (Value[]) new Object[max];
        for (int i = 0; i < N; i++) {
            tempKeys[i] = keys[i];
            tempValues[i] = values[i];
        }
        keys = tempKeys;
        values = tempValues;
    }


    public boolean isEmpty() {
        return N == 0;
    }

    public int size() {
        return N;
    }

    public int size(Key low, Key high) {
        if (high.compareTo(low) < 0) {
            return 0;
        } else if (contains(high)) {
            return rank(high) - rank(low) + 1;
        } else {
            return rank(high) - rank(low);
        }
    }

    public boolean contains(Key key) {
        return get(key) != null;
    }

    public Key min() {
        return keys[0];
    }

    public Key max() {
        return keys[N - 1];
    }

    public Value deleteMin() {
        return delete(min());

    }

    public Value delete(Key key) {
        int i = rank(key);
        Value value = null;

        if (keys[i].compareTo(key) == 0) {
            value = values[i];
            for (int j = i; j < N - 1; j++) {
                keys[j] = keys[j + 1];
                values[j] = values[j + 1];
            }
            // 防止对象游离
            keys[N - 1] = null;
            values[N - 1] = null;
            N--;
            // 如果只用了总容量的四分之一，缩减容量一半
            if (N > 0 && N == keys.length / 4) {
                resize(keys.length / 2);
            }
        }

        return value;
    }

    public Value deleteMax() {
        return delete(max());
    }

    // k = rank(select(k))
    // key = select(rank(key)
    public Key select(int k) {
        return keys[k];
    }

    public Set<Key> keys() {
        return keys(min(), max());
    }

    public Collection<Value> values() {
        return values(min(), max());
    }

    public Collection<Value> values(Key low, Key high) {
        Collection<Value> q = new ArrayList<>();
        for (int j = rank(low); j < rank(high); j++) {
            q.add(values[j]);
        }
        if (contains(high)) {
            q.add(values[rank(high)]);
        }
        return q;
    }

    public Set<Key> keys(Key low, Key high) {
        // 保持原来的顺序，使用LinkedHashSet
        Set<Key> q = new LinkedHashSet<>();
        for (int j = rank(low); j < rank(high); j++) {
            q.add(keys[j]);
        }
        if (contains(high)) {
            q.add(keys[rank(high)]);
        }
        return q;
    }

    // 大于等于key的最小键，如果key在表中就是等于key；否则是大于key的最小键，即i的下一个位置的键
    public Key ceiling(Key key) {
        int i = rank(key);
        // i可能等于N，此时返回null，也符合
        return keys[i];
    }

    // 小于等于key的最大键
    public Key floor(Key key) {
        int i = rank(key);
        if (contains(key)) {
            return keys[i];
            // 考虑负数脚标的情况，i == 0会造成keys[-1]
        } else if (!contains(key) && i != 0) {
            return keys[i - 1];
        } else {
            // 表中不没有键key且i == 0，说明key是表中最小的，不存在比它还小的所以返回null
            return null;
        }
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        int i = 0;
        while (true) {
            sb.append(keys[i]).append("=").append(values[i]);
            if (i == N - 1) {
                return sb.append("}").toString();
            } else {
                sb.append(", ");
            }
            i++;
        }
    }

    public static void main(String[] args) {
        BinarySearchST<Integer, Double> st = new BinarySearchST<>();
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

        System.out.println("符号表的长度为" + st.size());
        System.out.println("[3, 6]之间有" + st.size(3, 6) + "个键");
        System.out.println("比9小的键的数量为" + st.rank(9));
        System.out.println("排在第4位置的键为" + st.select(4));
        System.out.println("大于等于8的最小键为" + st.ceiling(8));
        System.out.println("小于等于8的最大键为" + st.floor(8));

        System.out.println("符号表所有的键和对应的值为：" + st.keys() + " -> " + st.values());
        System.out.println("键2和键8之间的所有键及对应的值：" + st.keys(2, 8) + " -> " + st.values(2, 8));

        System.out.println(st);

    }

}
