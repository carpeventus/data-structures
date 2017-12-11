package Chap4;

import java.util.Iterator;

/**
 * 栈，链表实现
 */
public class MyStack2<Item> implements Iterable<Item> {

    private class Node {
        Item data;
        Node next;
    }

    // 指向第一个节点
    private Node first;
    private int N;

    public MyStack2(Item... items) {
        for (Item item : items) {
            push(item);
        }
    }

    public int size() {
        return N;
    }

    public boolean isEmpty() {
        return N == 0;
    }

    // 表头插入元素
    public void push(Item item) {
        Node oldfirst = first;
        first = new Node();
        first.data = item;
        first.next = oldfirst;

        N++;
    }

    // 删除表头元素
    public Item pop() {
        Item item = first.data;
        Node next = first.next;
        // 这两行有助于垃圾回收
        first.data = null;
        first.next = null;
        first = next;
        N--;
        return item;
    }

    public Item peek() {
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
        N = 0;
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

    public static void main(String[] args) {
        MyStack2<String> a = new MyStack2<>("I", "have", "a", "dream.");
        System.out.println(a);
        a.pop();
        a.push("pig.");
        System.out.println(a.peek());
        a.clear();
        System.out.println(a.isEmpty());
    }
}
