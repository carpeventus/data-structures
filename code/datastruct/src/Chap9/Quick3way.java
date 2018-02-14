package Chap9;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 三向切分的快速排序
 */
public class Quick3way {

    public static void sort(Comparable[] a) {
        shuffle(a);
        sort(a, 0, a.length - 1);
    }

    private static void sort(Comparable[] a, int low, int high) {
        if (high <= low) {
            return;
        }

        int lt = low;
        int gt = high;
        int i = low + 1;
        // 切分元素
        Comparable v = a[low];
        while (i <= gt) {
            int cmp = a[i].compareTo(v);
            if (cmp < 0) {
                swap(a, lt++, i++);
            } else if (cmp > 0) {
                swap(a, i, gt--);
            } else {
                i++;
            }
        }
        // 现在a[lo..lt-1] < v=a[lt..gt] < a[gt+1..high]成立
        // 切分元素相同的数组不会被递归算法访问到，对其左右的子数组递归排序
        sort(a, low, lt - 1);
        sort(a, gt + 1, high);

    }


    private static void shuffle(Comparable[] a) {
        // asList返回的是实际上是ArrayList，而ArrayList的底层是数组，所以打乱了b，a也被打乱了
        List<Comparable> b = Arrays.asList(a);
        Collections.shuffle(b);
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
        Integer[] a = {2, 4, 5, 4, 5, 2, 1, 5, 1, 2, 4, 1};
        Quick3way.sort(a);
        System.out.println(Quick3way.toString(a));
    }
}
