package Chap3;

import java.util.Iterator;

/**
 * 线性表，使用数组实现，类似ArrayList
 */

// 实现Iterable为了使用for-each语句，同时要实现iterator方法
public class LinearList<Item> implements Iterable<Item> {
    private int N;
    // 初始化为长度为1，方便第一次add的时候可以访问a[0]这个下标
    private Item[] a = (Item[]) new Object[1];

    public LinearList(Item... items) {
        for (int i = 0; i < items.length; i++) {
            add(items[i]);
        }
    }


    public boolean isEmpty() {
        return N == 0;
    }

    public int size() {
        return N;
    }

    public Item get(int index) {
        checkRange(index);
        return a[index];
    }

    public void set(int index, Item item) {
        checkRange(index);
        a[index] = item;
    }

    // 先判断是不是没有容量了，若不先增容，会越界。移位从最后一个元素开始，仔细想想为什么
    public void insert(int index, Item item) {
        checkRangeForInsert(index);
        if (N == a.length) {
            resize(2 * a.length);
        }
        for (int k = N - 1; k >= index; k--) {
            a[k + 1] = a[k];
        }
        a[index] = item;
        N++;
    }

    // 移除之后再检查是否长度太小需要节约空间，否则先缩小的话，可能导致访问时越界
    public Item remove(int index) {
        checkRange(index);
        Item item = a[index];
        // 这里就需要正向遍历了
        for (int k = index; k < N - 1; k++) {
            a[k] = a[k + 1];
        }
        a[N - 1] = null;
        N--;
        if (N > 0 && N == a.length / 4) {
            resize(a.length / 2);
        }
        return item;
    }

    // 先判断是不是没有容量了，若不先增容，会越界
    public void add(Item item) {
        if (N == a.length) {
            resize(2 * a.length);
        }
        a[N++] = item;
    }


    public int indexOf(Item item) {
        if (item != null) {
            for (int i = 0; i < N; i++) {
                if (item.equals(a[i])) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < N; i++) {
                if (a[i] == null) {
                    return i;
                }
            }
        }

        return -1;
    }

    public boolean contains(Item item) {
        return indexOf(item) >= 0;
    }

    // N=0但是a.length不为0，可以再次add
    public void clear() {
        for (int i = 0; i < N; i++) {
            a[i] = null;
        }
        N = 0;
    }

    private void resize(int max) {
        Item[] temp = (Item[]) new Object[max];

        for (int i = 0; i < N; i++) {
            temp[i] = a[i];
        }
        // 将容量大于N的数组传给a
        a = temp;
    }


    // 检查数组下标是否越界，注意是N而不是a.length, 因为a的容量比N大，访问N之后的也不会触发异常
    // insert的时候允许向a[N]处插入，这里==N不会抛出异常
    private void checkRangeForInsert(int index) {
        if (index > N || index < 0) {
            throw new IndexOutOfBoundsException(index + "");
        }
    }

    // 其他情况如remove就不能访问a[N]了
    private void checkRange(int index) {
        if (index >= N || index < 0) {
            throw new IndexOutOfBoundsException(index + "");
        }
    }

    @Override
    public Iterator<Item> iterator() {
        return new Iterator<>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < N;
            }

            @Override
            public Item next() {
                return a[i++];
            }
        };
    }

    @Override
    public String toString() {
        Iterator<Item> it = iterator();
        if (!it.hasNext()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");

        while (true) {
            Item item = it.next();
            sb.append(item);
            if (!it.hasNext()) {
                return sb.append("]").toString();
            }
            sb.append(", ");
        }
    }

    public static void main(String[] args) {
        LinearList<String> b = new LinearList<>();

        b.add("god");
        b.add("yes");
        b.add("no");
        b.add("man");
        b.insert(0, "ffff");
        System.out.println(b.remove(0)); // ffff
        b.set(1, "ggg");
        System.out.println(b.get(1)); // ggg
        System.out.println(b.indexOf("no")); // 2
        System.out.println(b.size()); // 4
        /* now b have:
        god
        ggg
        no
        man
         */
        System.out.println("*******");
        LinearList<Integer> c = new LinearList<>(1, 2, 3, 4, 5);
        System.out.println(c);
        System.out.println(c.contains(5)); // true
        c.clear();
        c.add(66);
    }
}
