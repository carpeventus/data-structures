package Chap9;

/**
 * 简单选择排序
 */
public class SelectSort {
    public static void sort(Comparable[] a) {
        for (int i = 0; i < a.length; i++) {
            // min保存目前最小元素的下标，刚开始假设位置i的元素最小，然后和之后每个元素比较，
            int minIndex = i;
            // 只要后面更小就更新min，因此一轮循环下来min肯定是最小元素下标
            for (int j = i + 1; j < a.length; j++) {
                if (less(a[j], a[minIndex])) {
                    minIndex = j;
                }
            }
            if (i != minIndex) {
                // 最小元素换到位置i
                swap(a, i, minIndex);
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
            if (less(a[i+1], a[i])) {
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
        SelectSort.sort(a);
        System.out.println(SelectSort.toString(a));
        System.out.println(SelectSort.isSorted(a));
    }

}
