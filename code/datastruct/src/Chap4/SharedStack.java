package Chap4;

/**
 * 两栈共享空间，数组实现
 */
public class SharedStack<Item> {

    private Item[] a;
    // 栈顶
    private int top1;
    private int top2;

    public SharedStack(int maxsize) {
        if (maxsize <= 0) {
            maxsize = 512;
        }

        a = (Item[]) new Object[maxsize];
        top1 = -1;
        top2 = a.length;
    }

    public SharedStack() {
        this(512);
    }

    public boolean isEmpty(int stackNumber) {
        if (stackNumber == 1) {
            return top1 == -1;
        } else if (stackNumber == 2) {
            return top2 == a.length;
        }
        return true;
    }

    public int size(int stackNumber) {
        if (stackNumber == 1) {
            return top1 + 1;
        } else if (stackNumber == 2) {
            return a.length - top2;
        }
        return 0;
    }

    public void push(int stackNumber, Item item) {
        // 栈已满的状态，不能再添加
        // 先判断这句，下面的就能正确处理了
        if (top1 + 1 == top2) {
            throw new RuntimeException("栈已满！");
        } else if (stackNumber == 1) {
            a[++top1] = item;
        } else if (stackNumber == 2) {
            a[--top2] = item;
        }
    }

    // 选择弹出哪个栈的数据
    public Item pop(int stackNumber) {
        Item item = null;
        if (stackNumber == 1) {
            // 栈1空了
            if (top1 == -1) {
                throw new RuntimeException("栈1已空！");
            } else {
                item = a[top1--];
            }
        } else if (stackNumber == 2) {
            // 栈2空了
            if (top2 == a.length) {
                throw new RuntimeException("栈2已空！");
            } else {
                item = a[top2++];
            }
        }
        return item;
    }

    public String displayStack(int stackNumber) {
        if (stackNumber == 1) {
            if (top1 == -1) {
                return "[]";
            }
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (int j = top1; j >= 0; j--) {
                sb.append(a[j]);
                if (j == 0) {
                    return sb.append("]").toString();
                } else {
                    sb.append(", ");
                }
            }
        } else if (stackNumber == 2) {
            if (top2 == a.length) {
                return "[]";
            }
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (int j = top2; j < a.length; j++) {
                sb.append(a[j]);
                if (j == a.length - 1) {
                    return sb.append("]").toString();
                } else {
                    sb.append(", ");
                }
            }
        }
        return null;
    }

    public void clear(int stackNumber) {
        if (stackNumber == 1) {
            for (int j = top1; j >=0 ; j--) {
                a[j] = null;
            }
            top1 = -1;
        } else if (stackNumber == 2) {
            for (int j = top2; j < a.length; j++) {
                a[j] = null;
            }
            top2 = a.length;
        }
    }

    public void clearAll() {
        for (int i = 0; i < a.length; i++) {
            a[i] = null;
        }
        top1 = -1;
        top2 = a.length;
    }

    public static void main(String[] args) {
        SharedStack<String> a = new SharedStack<>();
        a.push(1, "a");
        a.push(1, "b");
        a.push(1, "c");
        a.push(1, "d");
        a.push(2, "4");
        a.push(2, "3");
        a.push(2, "2");
        a.push(2, "1");
        a.pop(2); // 1
        a.pop(1); // d
        System.out.println(a.size(2)); // 3
        System.out.println(a.size(1)); // 3
        System.out.println(a.displayStack(1)); // [c, b, a]
        System.out.println(a.displayStack(2)); // [2, 3, 4]
        a.clear(1);
        a.push(1,"a");
        System.out.println(a.displayStack(1)); // [a]
        a.clearAll();
        System.out.println(a.size(2)); // 0
    }
}
