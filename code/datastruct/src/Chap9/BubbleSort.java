package Chap9;

/**
 * 冒泡排序
 */
public class BubbleSort {
    public static void sort(Comparable[] a) {
        for (int i = 0; i < a.length; i++) {
            // 从最后一个元素开始，不断将较小的往前推，被推到到数组左端就是最小元素
            for (int j = a.length - 1; j > i; j--) {
                // 后面的比前面的小就交换
                if (less(a[j], a[j - 1])) {
                    swap(a, j - 1, j);
                }
            }
        }
    }

    public static void sort2(Comparable[] a) {
        boolean isSorted;
        for (int i = 0; i < a.length; i++) {
            // 每次循环都先假设已经有序，若交换了说明可能还没有达到有序，变成false。
            isSorted = true;
            // 从最后一个元素开始，不断将较小的往前推，被推到到数组左端就是最小元素
            for (int j = a.length - 1; j > i; j--) {
                // 后面的比前面的小就交换
                if (less(a[j], a[j - 1])) {
                    swap(a, j - 1, j);
                    isSorted = false;
                }
            }
            // 如某轮循环没有发生交换，说明已经有序了，直接跳出循环
            if (isSorted) {
                break;
            }
        }
    }

    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    private static void swap(Comparable[] a, int p, int q) {
        Comparable temp = a[p];
        a[p] = a[q];
        a[q] = temp;
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
        BubbleSort.sort2(a);
        System.out.println(BubbleSort.toString(a));
        System.out.println(BubbleSort.isSorted(a));
    }
}
