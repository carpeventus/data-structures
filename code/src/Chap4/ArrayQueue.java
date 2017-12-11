package Chap4;

import java.util.Iterator;

/**
 * 循环队列，数组实现
 * @param <Item>
 */
public class ArrayQueue<Item> implements Iterable<Item> {

    private Item[] a;
    // 指向队列的第一个元素
    private int first; // 默认值0
    // 指向最后一个元素的下一位置
    private int last; // 默认值0

    public ArrayQueue(int maxsize) {
        if (maxsize <= 0) {
            maxsize = 512;
        }
        a = (Item[]) new Object[maxsize];
    }

    public ArrayQueue() {
        this(512);
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        /* 1. last > first时：长度应该是last -first
              而(last - first + a.length) % a.length
              即(last - first) % a.length + 0 = last -first

           2. last < first时：长度应该是last - first + a.length
              而(last - first + a.length) % a.length
              即last - first + a.length

           综上，统一使用下面的表达式
         */
        return (last - first + a.length) % a.length;
    }

    public void enqueue(Item item) {
        // 先判断是否已满，无论何时，即使满时，last所在都是空
        if ((last + 1) % a.length == first) {
            throw new RuntimeException("已达到最大容量，不可再入列！");
        }
        a[last] = item;
        // 当新元素入列成占据数组最后一个位置时，last应该变成0（循环结构，数组最后一个位置的下一个位置就是数组的第一个位置）；其余情况保持自增
        last = (last + 1) % a.length;
    }

    public Item dequeue() {
        Item item = a[first];
        // 既然弹出，此处应变成null
        a[first] = null;
        // first的更新和last一样的
        first = (first + 1) % a.length;
        return item;
    }
    // 获得队头元素
    public Item getHead() {
        return a[first];
    }

    // 清空相当于初始化到原来的样子
    public void clear() {
        for (int i = 0; i < a.length; i++) {
            a[i] = null;
        }
        first = 0;
        last = 0;
    }

    @Override
    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            private int current = first;

            @Override
            public boolean hasNext() {
                // 1. 空队列时（first = last = 0）
                // 2. first在last前一个位置，刚好访问了最后一个元素。之后和last相等时结束遍历
                return current != last;
            }

            @Override
            public Item next() {
                Item item = a[current];
                current = (current + 1) % a.length;
                return item;
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
        ArrayQueue<String> a = new ArrayQueue<>();
        a.enqueue("a");
        a.enqueue("b");
        a.enqueue("c");
        a.enqueue("d");
        System.out.println(a);
        a.dequeue(); // a
        a.dequeue(); // b
        for (String each: a) {
            System.out.print(each+" "); // c d
        }
        a.clear();
        System.out.println("\n"+a.isEmpty()); // true
        a.enqueue("tiger");
        a.enqueue("lion");
        System.out.println("head: "+a.getHead()); // tiger
    }
}
