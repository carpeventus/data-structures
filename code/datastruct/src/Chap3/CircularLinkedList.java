package Chap3;


import java.util.Iterator;

/**
 * 单向循环链表
 *
 * last不再指向null而是first，即使得last.next = first
 * 同时遍历判断条件从current != null 变成判断长度 i < N
 */
public class CircularLinkedList<Item> implements Iterable<Item> {

    private class Node {
        Item data;
        Node next;
    }

    // 指向第一个节点
    private Node first;
    // 指向最后一个节点
    private Node last;
    private int N;

    public CircularLinkedList(Item... items) {
        for (Item item : items) {
            add(item);
        }
    }

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

        Node current = first;
        for (int j = 0; j < index; j++) {
            current = current.next;
        }
        return current;
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
        // 如果index==0,因为没有设置头结点所以只需单向链接就行
        if (index == 0) {
            push(item);
        } else if (index == N) {
            add(item);
        }
        else if (index > 0 && index < N) {
            Node a = new Node();
            // 其他插入的位置在index-1和index之间, 需要定位到index-1的位置，
            Node current = index(index - 1);
            a.data = item;
            a.next = current.next;
            current.next = a;
            N++;
        } else {
            throw new IndexOutOfBoundsException(index + "");
        }
    }


    public Item remove(int index) {
        // 和insert一样，index==0处理方式也不一样
        Item item;
        if (index == 0) {
            item = pop();
            // 和insert不一样（它可以在表尾null处插入），remove则不该移除本来就是null的值
            // 表尾的删除也稍有不同
        } else if(index == N -1) {
            Node current = index(index - 1);
            item = current.next.data;
            current.next = first;
            last = current;
        } else if (index > 0 && index < N) {
            Node current = index(index - 1);
            // 定位到index的上一个了，所以取next
            item = current.next.data;
            Node next = current.next.next;
            // 下面两行帮助垃圾回收
            current.next.next = null;
            current.next.data = null;
            current.next = next;
            N--;
        } else {
            throw new IndexOutOfBoundsException(index + "");
        }
        return item;
    }

    public void add(Item item) {
        Node oldlast = last;
        last = new Node();
        last.data = item;
        // 如果是第一个元素，则last和first指向同一个，即第一个
        if (isEmpty()) {
            first = last;
            last.next = first;
        } else {
            oldlast.next = last;
            // last被新结点取代，next默认是null，所以每次add都要将它的next指向first
            last.next = first;
        }
        N++;
    }

    public void push(Item item) {
        Node oldfirst = first;
        first = new Node();
        first.data = item;
        if (isEmpty()) {
            last = first;
            // 这句是循环链表
            last.next = first;
        } else {
            first.next = oldfirst;
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
        // 注意是先减再判断是否为空
        if (isEmpty()) {
            last = null;
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
        int i = 0;

        if (item != null) {
            for (Node cur = first; i < N; cur = cur.next) {
                if (item.equals(cur.data)) {
                    return index;
                }
                index++;
                i++;
            }
        } else {
            for (Node cur = first; i < N; cur = cur.next) {
                if (cur.data == null) {
                    return index;
                }
                index++;
                i++;
            }
        }
        return -1;
    }

    public boolean contains(Item item) {
        return indexOf(item) >= 0;
    }

    // 因为是循环链表，无头无尾，用长度判断比较方便
    @Override
    public Iterator<Item> iterator() {
        return new Iterator<>() {
            private Node current = first;
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < N;
            }

            @Override
            public Item next() {
                Item item = current.data;
                current = current.next;
                i++;
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

    public void combineList(CircularLinkedList<Item> b) {
        // 原表的尾和第二个链表的头相连
        last.next = b.first;
        // 第二个链表的尾和原表的头相连
        b.last.next = first;
        // 更新原表的last
        last = b.last;
        // 更新长度
        N += b.N;
    }

    public static void main(String[] args) {
        CircularLinkedList<String> a = new CircularLinkedList<>();

        a.push("1");
        a.push("2");
        a.push("3");
        System.out.println(a.size());
        a.set(1, "22");
        System.out.println(a.get(1));
        a.clear();
        a.add("1");
        a.add("2");
        a.add("3");
        a.insert(2, "4");
        a.remove(1);
        System.out.println(a); // [1, 4, 3]
        System.out.println(a.indexOf("4")); // 1

        CircularLinkedList<String> b = new CircularLinkedList<>("10", "40", "30");
        a.combineList(b);
        System.out.println(a);
    }
}
