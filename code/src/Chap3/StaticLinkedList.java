package Chap3;


import java.util.Iterator;

/**
 * 静态链表
 *
 * 可以使用数组代替指针（一般不这样做）。数组的每个元素包含两个值，一个是data，另外一个是下一元素的下标值
 * 由于是数组实现，故长度固定
 */


public class StaticLinkedList<Item> implements Iterable<Item> {

    private class Node<E> {
        E data;
        int next;
    }

    private int N;
    private Node<Item>[] a;

    public boolean isEmpty() {
        return N == 0;

    }

    public int size() {
        return N;
    }

    // 初始化数组
    public StaticLinkedList(int capacity) {
        a = (Node<Item>[]) new Node[capacity];
        for (int i = 0; i < capacity - 1; i++) {
            a[i] = new Node<>();
            a[i].next = i + 1;
        }
        a[capacity - 1] = new Node<>();
        a[capacity - 1].next = 0;
    }

    public StaticLinkedList(Item... items) {
        this(512);
        for (Item item : items) {
            add(item);
        }
    }

    private int getSpace() {
        // a[0]的next所指就是第一个空闲元素的下标
        int i = a[0].next;
        a[0].next = a[i].next;
        return i;
    }

    private void checkRange(int index) {
        if (index < 0 || index >= N) {
            throw new IndexOutOfBoundsException(index + "");
        }
    }

    private void checkRangeForInsert(int index) {
        if (index < 0 || index > N) {
            throw new IndexOutOfBoundsException(index + "");
        }
    }

    private Node<Item> index(int index) {
        // 最后一个元素的next就是第一个结点所在的下标
        int k = a.length - 1;
        for (int i = 0; i <= index; i++) {
            k = a[k].next;
        }
        return a[k];
    }

    public void insert(int index, Item item) {
        checkRangeForInsert(index);
        // 即使index为0，也能正确处理
        Node<Item> e = index(index - 1);
        int i = getSpace();
        a[i].data = item;
        // 让插入处的下标（也就是前一元素的next）赋给新元素的next，表示新元素在插入处元素之前
        a[i].next = e.next;
        // 把新元素的下标给前一元素的next，表示插入处的前一元素在新元素之前
        e.next = i;
        N++;
    }

    public Item get(int index) {
        checkRange(index);
        Node<Item> e = index(index);
        return e.data;
    }

    public void set(int index, Item item) {
        checkRange(index);
        Node<Item> e = index(index);
        e.data = item;
    }


    public Item remove(int index) {
        checkRange(index);
        // 移除和插入一样需要定位到前一元素
        Node<Item> e = index(index - 1);
        int j = e.next;
        Item item = a[e.next].data;
        // 帮助垃圾回收
        a[e.next].data = null;
        e.next = a[e.next].next;
        // 这里不要填入e.next,因为在上句已经被改变
        freeSpace(j);
        N--;
        return item;
    }

    // 让被移除的位置成为下一个插入的位置
    private void freeSpace(int index) {
        // 把备用表第一个元素的下标给当前被释放的元素next
        a[index].next = a[0].next;
        // 再将备用表第一个元素换成被删除的位置。优先填入这个后，再填入上步中原来的备用表第一位置
        a[0].next = index;
    }

    // 链表的末尾插入
    public void add(Item item) {
        int index = a.length - 1;
        // 到0表示链表的末尾
        while (a[index].next != 0) {
            index = a[index].next;
        }

        int i = getSpace();
        a[i].data = item;
        // 找出最后一个元素, 将最后一个结点的下标给
        a[index].next = i;
        // 末尾元素的next为0
        a[i].next = 0;
        N++;
    }

    // 链表的开头插入
    public void push(Item item) {
        int i = getSpace();
        a[i].data = item;
        a[i].next = a[a.length - 1].next;
        a[a.length - 1].next = i;
        N++;
    }

    // 弹出表头
    public Item pop() {
        int index = a[a.length - 1].next;
        Item item = a[index].data;
        a[a.length - 1].next = a[index].next;
        // 帮助垃圾回收
        a[index].data = null;
        freeSpace(index);
        N--;
        return item;
    }

    public void clear() {
        for (int i = 0; i < a.length - 1; i++) {
            a[i].next = i + 1;
            a[i].data = null;
        }
        a[a.length - 1].next = 0;
        a[a.length - 1].data = null;
        N = 0;
    }

    public int indexOf(Item item) {
        int index = 0;
        if (item != null) {
            for (int i = a[a.length - 1].next; i != 0; i = a[i].next) {
                if (a[i].data.equals(item)) {
                    return index;
                }
                index++;
            }
        } else {
            for (int i = a[a.length - 1].next; i != 0; i = a[i].next) {
                if (a[i].data == null) {
                    return index;
                }
                index++;
            }

        }
        return -1;
    }

    public boolean contains(Item item) {
        return indexOf(item) >= 0;
    }

    @Override
    public Iterator<Item> iterator() {
        return new Iterator<>() {
            int index = a[a.length - 1].next;

            @Override
            public boolean hasNext() {
                return index != 0;
            }

            @Override
            public Item next() {
                Item item = a[index].data;
                index = a[index].next;
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
        StaticLinkedList<String> a = new StaticLinkedList<>(1000);

        a.push("123");
        a.push("456");
        a.push("789");
        a.clear();

        a.push("456");
        a.push("123");
        a.add("789");
        a.add("10");
        a.remove(1); // 456
        a.pop(); // 123
        System.out.println(a.get(1)); // 10
        a.set(1, "1011");
        a.insert(0, "234");
        a.insert(2, "456");
        System.out.println(a.contains("789")); // true
        System.out.println(a.size()); // 4
        for (String each : a) {
            System.out.println(each);
        }
        System.out.println(a.indexOf("1011")); // 3
        StaticLinkedList<Integer> b = new StaticLinkedList<>(1, 2, 32, 434);
        System.out.println(b);
    }
}

