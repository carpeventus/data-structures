package Chap9;

/**
 * 插入排序
 */
public class InsertSort {

    public static void sort(Comparable[] a) {
        for (int i = 1; i < a.length; i++) {
            // 当前索引如果比它前一个元素要大，不用插入;否则需要插入
            if (less(a[i], a[i-1])) {
                // 待插入的元素先保存
                Comparable temp = a[i];
                // 元素右移
                int j;
                for (j = i; j > 0 && less(temp, a[j-1]); j--) {
                    a[j] = a[j -1];
                }
                // 插入
                a[j] = temp;
            }
        }
    }

    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    public static boolean isSorted(Comparable[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            if (less(a[i + 1], a[i])) {
                return false;
            }
        }
        return true;
    }

    public static String toString(Comparable[] a) {
        if (a.length == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < a.length; i++) {
            sb.append(a[i]);
            if (i == a.length - 1) {
                return sb.append("]").toString();
            } else {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Integer[] a = {9, 1, 5, 8, 3, 7, 4, 6, 2};
        InsertSort.sort(a);
        System.out.println(InsertSort.toString(a));
        System.out.println(InsertSort.isSorted(a));
    }
}
