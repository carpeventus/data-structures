package Chap9;

/**
 * 自顶向下的归并排序，递归
 */
public class MergeSort {

    private static void merge(Comparable[] a, Comparable[] aux, int low, int mid, int high) {
        int i = low; // 左半数组的指针 [0, mid]
        int j = mid + 1; // 右半数组的指针 [mid +1, high]
        // 将待归并的数组元素全归并到一个新数组中
        for (int k = low; k <= high; k++) {
            aux[k] = a[k];
        }

        for (int k = low; k <= high; k++) {
            // 左半数组指针超出，被取完。于是取右半数组中的元素
            if (i > mid) {
                a[k] = aux[j++];
                // 右半数组被取完，取左半数组中的元素
            } else if (j > high) {
                a[k] = aux[i++];
                // 已满足i <= mid && j <= high
                // 右半数组的元素小，就取右半数组中元素
            } else if (less(aux[j], aux[i])) {
                a[k] = aux[j++];
                // 已满足i <= mid && j <= high
                // 左半数组元素小或者相等，取左半数组中的元素，相等时取左边保证了排序稳定性
            } else {
                a[k] = aux[i++];
            }
        }
    }

    private static void sort(Comparable[] a, Comparable[] aux, int low, int high) {
        // high = low说明数组被划分到只有一个元素，无需排序和归并直接返回
        // high <= low + 15说明当数组很小时直接换用插入排序，当数组长度不超过16时都使用插入排序
        if (high <= low + 15) {
            InsertSort.sort(a);
            return;
        }

        int mid = low + (high - low) / 2;

        sort(a, aux, low, mid);
        sort(a, aux, mid + 1, high);
        // a[mid] <= a[mid + 1]已经有序，跳过归并操作
        if (a[mid].compareTo(a[mid + 1]) > 0) {
            merge(a, aux, low, mid, high);
        }
    }

    public static void sort(Comparable[] a) {
        Comparable[] aux = new Comparable[a.length];
        sort(a, aux, 0, a.length - 1);
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
        MergeSort.sort(a);
        System.out.println(MergeSort.toString(a));
        System.out.println(MergeSort.isSorted(a));
    }
}
