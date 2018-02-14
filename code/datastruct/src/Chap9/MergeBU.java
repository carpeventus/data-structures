package Chap9;

/**
 * 自底向上的归并排序，非递归
 */
public class MergeBU {

    public static void sort(Comparable[] a) {
        Comparable[] aux = new Comparable[a.length];
        // sz = 1, 2, 4, 8...
        for (int sz = 1; sz < a.length; sz = sz + sz) {
            // sz = 1: low= 0, 2, 4, 6, 8, 10...
            // sz = 2: low= 0, 4, 8, 12, 16...
            // sz = 4: low= 0, 8, 16, 24...
            for (int low = 0; low < a.length-sz; low += (sz + sz)) {
                // sz = 1: 归并子数组 (0,0,1) (2,2,3) (4,4,5)...
                // sz = 2: 归并子数组 (0,1,3) (4,5,7) (8,9,11)...
                // sz = 4: 归并子数组 (0,3,7) (8,11,15) (16,19,23)...

                // 可由归纳法得到mid = low + sz -1; high = low + 2sz -1
                // 最后一个子数组可能比sz小，所以通过low + 2sz -1计算high可能比原数组还要大，因此和a.length - 1取最小
                merge(a, aux, low,  low + sz - 1, Math.min(low + sz + sz - 1, a.length - 1));
            }
        }
    }

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
        Integer[] a = {9, 1, 8, 5, 7, 3, 6, 4, 10, 2, 11, 13, 12, 17, 16, 15, 14, 19, 0};
        MergeBU.sort(a);
        System.out.println(MergeBU.toString(a));
    }
}
