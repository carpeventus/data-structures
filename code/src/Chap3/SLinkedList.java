package Chap3;


import java.util.Iterator;

/**
 * 单链表
 */
public class SLinkedList<Item> implements Iterable<Item> {

    private class Node {
        Item data;
        Node next;
    }

    // 指向第一个节点
    private Node first;
    // 指向最后一个节点
    private Node last;
    private int N;

    public SLinkedList(Item... items) {
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
        } else if (index > 0 && index < N) {
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
            current.next = null;
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
        SLinkedList<String> a = new SLinkedList<>("12", "34", "56", "78");

        System.out.println(a.get(0)); // 12
        System.out.println(a.remove(1)); // 34
        System.out.println(a.remove(1)); // 56

        System.out.println(a); // [12, 78]

        a.insert(0, "a");
        a.insert(1, "b");
        a.set(3, "d");
        for (String aa : a) {
            System.out.println(aa);
        }
        /* Out:
        a
        b
        12
        d
         */
        System.out.println("*******");
        SLinkedList<Integer> c = new SLinkedList<>(1, 2, 3, 4, 5);
        System.out.println(c);
        c.clear();
        c.add(45);
        c.add(46);
        c.push(44);
        System.out.println(c); // [44, 45, 46]
        System.out.println(c.indexOf(46)); // 2
        System.out.println(c.contains(46)); //true
        System.out.println(c.size()); //3

    }
}
