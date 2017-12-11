package Chap3;

import java.util.Iterator;

/**
 * 双向链表
 */
public class DLinkedList<Item> implements Iterable<Item> {

    private class Node {
        Item data;
        Node prev;
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

    private Node index(int index) {
        // [0, N-1]的定位范围
        if (index < 0 || index >= N) {
            throw new IndexOutOfBoundsException(index + "");
        }
        // 索引在前半部分就正向查找, 在后半部分就反向查找
        if (index < N / 2) {
            Node current = first;
            for (int j = 0; j < index; j++) {
                current = current.next;
            }
            return current;
        } else {
            Node current = last;
            for (int i = N - 1; i > index; i--) {
                current = current.prev;
            }
            return current;
        }
    }

    public Item get(int index) {
        Node current = index(index);
        return current.data;
    }

    public void set(int index, Item item) {
        Node current = index(index);
        current.data = item;
    }

    // 可以在表头（index==0）和表尾插入
    public void insert(int index, Item item) {
        if (index == N) {
            add(item);
        } else {
            // 因为有prev，所以定位到当前结点就好,如果使用index(index -1)当index为0时报错
            Node current = index(index);
            Node pre = current.prev;
            Node a = new Node();
            a.data = item;
            /* 由于多处使用到current即pre.next，所以最后才改变其值
               1. 先确定新结点的两头
               2. 更新 后结点的前驱
               3. 更新 前结点的后继
            */
            a.prev = pre;
            a.next = current;
            current.prev = a;
            // 如果是insert(0，item)则pre为null,没有前结点，跳过步骤3
            if (pre == null) {
                first = a;
            } else {
                pre.next = a;
            }
            N++;
        }
    }


    public Item remove(int index) {
        /*
            定位到当前位置
            1. 前一元素的后继为下一元素
            2. 下一元素的前驱为前一元素
         */
        Node current = index(index);
        Item item = current.data;
        Node pre = current.prev;
        Node next = current.next;
        // 下面三行帮助垃圾回收
        current.prev = null;
        current.next = null;
        current.data = null;
        // 如果删除的是第一个结点，则pre为null。没有后继，跳过
        if (pre == null) {
            first = next;
        } else {
            pre.next = next;
        }
        // 如果删除的是最后一个结点，则next为null。没有前驱，跳过
        if (next == null) {
            last = pre;
        } else {
            next.prev = pre;
        }

        N--;
        return item;
    }

    // 表尾加入元素
    public void add(Item item) {
        Node oldlast = last;
        last = new Node();
        last.data = item;
        // last应该指向null，但是新的结点next默认就是null
        // 如果是第一个元素，则last和first指向同一个，即第一个
        if (isEmpty()) {
            first = last;
        } else {
            last.prev = oldlast;
            oldlast.next = last;
        }
        N++;
    }

    // 表头插入元素
    public void push(Item item) {
        Node oldfirst = first;
        first = new Node();
        first.data = item;
        // 和add一样，第一个元素加入时，last和first指向同一个结点
        if (isEmpty()) {
            last = first;
        } else {
            first.next = oldfirst;
            oldfirst.prev = first;
        }
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
        // 最后一个元素被删除，first自然为空了，但是last需要置空。
        if (isEmpty()) {
            last = null;
        } else {
            // next的引用给first，此时first的prev不为空。需要把表头的前驱设为null（因为first没有前驱）
            first.prev = null;
        }
        return item;
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

    public int indexOf(Item item) {
        int index = 0;
        if (item != null) {
            for (Node cur = first; cur != null; cur = cur.next) {
                if (item.equals(cur.data)) {
                    return index;
                }
                index++;
            }
        } else {
            for (Node cur = first; cur != null; cur = cur.next) {
                if (cur.data == null) {
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

    public Iterable<Item> reversed() {
        return new Iterable<Item>() {
            @Override
            public Iterator<Item> iterator() {
                return new Iterator<Item>() {
                    Node cur = last;

                    @Override
                    public boolean hasNext() {
                        return cur != null;
                    }

                    @Override
                    public Item next() {
                        Item item = cur.data;
                        cur = cur.prev;
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
        DLinkedList<Integer> a = new DLinkedList<>();
        a.push(2);
        a.push(1);
        a.push(3);
        a.set(2, 11);
        System.out.println(a.get(2)); // 11
        System.out.println(a);

        a.insert(0, 444);
        a.clear();

        a.add(11);
        a.add(12);
        a.add(13);
        a.push(14);
        a.remove(2); // 12
        a.pop(); // 14
        System.out.println(a.indexOf(13)); // 1


        System.out.println(a.reversed());
        for (Integer aa : a.reversed()) {
            System.out.println(aa);
        }
    }
}
