package Chap4;

import java.util.Iterator;

/**
 * 队列，链表实现
 */
public class MyQueue<Item> implements Iterable<Item> {

    private class Node {
        Item data;
        Node next;
    }

    // 指向第一个节点
    private Node first;
    // 指向最后一个节点
    private Node last;
    private int N;

    public int size() {
        return N;
    }

    public boolean isEmpty() {
        return N == 0;
    }



    // 入列，表尾加入元素
    public void enqueue(Item item) {
        Node oldlast = last;
        last = new Node();
        last.data = item;
        // last应该指向null，但是新的结点next默认就是null
        // 如果是第一个元素，则last和first指向同一个，即第一个
        if (isEmpty()) {
            first = last;
        } else {
            oldlast.next = last;
        }
        N++;
    }

    // 出列，即删除表头元素
    public Item dequeue() {
        Item item = first.data;
        Node next = first.next;
        // 这两行有助于垃圾回收
        first.data = null;
        first.next = null;
        first = next;
        N--;
        // 最后一个元素被删除，first自然为空了，但是last需要置空。
        // 注意是先减再判断是否为空
        if (isEmpty()) {
            last = null;
        }
        return item;
    }
    public Item getHead() {
        return first.data;
    }

    public void clear() {
        while (first != null) {
            Node next = first.next;
            // 下面两行帮助垃圾回收
            first.next = null;
            first.data = null;
            first = next;
        }
        // 所有元素都空时，last也没有有所指了。记得last置空
        last = null;
        N = 0;
    }

    @Override
    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            private Node current = first;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public Item next() {
                Item item = current.data;
                current = current.next;
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
        MyQueue<Integer> a = new MyQueue<>();
        a.enqueue(1);
        a.enqueue(2);
        a.enqueue(3);
        a.enqueue(4);
        System.out.println(a);
        a.dequeue();
        a.dequeue();
        System.out.println(a); // [3, 4]
        a.clear();
        System.out.println(a.size()); // 0
        a.enqueue(999);
        a.enqueue(777);
        System.out.println(a.getHead()); // 999
    }
}
