package Chap4;

import java.util.Iterator;

/**
 * 栈，数组实现
 */
public class MyStack<Item> implements Iterable<Item> {

    // 初始化为长度为1，方便第一次push的时候可以访问a[0]这个下标
    private Item[] a = (Item[]) new Object[1];

    private int N;

    public MyStack(Item... items) {
        for (int i = 0; i < items.length; i++) {
            push(items[i]);
        }
    }


    public boolean isEmpty() {
        return N == 0;
    }

    public int size() {
        return N;
    }

    private void resize(int max) {
        Item[] temp = (Item[]) new Object[max];

        for (int i = 0; i < N; i++) {
            temp[i] = a[i];
        }
        // 将容量大于N的数组传给a
        a = temp;
    }

    public void push(Item item) {
        if (N == a.length) {
            resize(a.length * 2);
        }
        a[N++] = item;
    }

    public Item pop() {
        Item item = a[--N];
        // 自减去后现在N为N-1
        a[N] = null;
        if (N == a.length / 4) {
            resize(a.length / 2);
        }
        return item;
    }

    public Item peek() {
        return a[N - 1];
    }

    // N=0但是a.length不为0，可以再次add
    public void clear() {
        for (int i = 0; i < N; i++) {
            a[i] = null;
        }
        N = 0;
    }

    @Override
    public Iterator<Item> iterator() {
        return new ReveredIterator();
    }

    private class ReveredIterator implements Iterator<Item> {

        private int i = N;
        @Override
        public boolean hasNext() {
            return i > 0;
        }

        @Override
        public Item next() {
            return a[--i];
        }
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
        MyStack<String> a = new MyStack<>();
        a.push("I");
        a.push("have");
        a.push("a");
        a.push("dream.");
        System.out.println(a);
        a.pop(); // dream.
        a.push("pig.");
        System.out.println(a.peek());
        System.out.println(a);
        // 变成空栈
        a.clear();
        System.out.println(a.size()); // 0
    }
}
