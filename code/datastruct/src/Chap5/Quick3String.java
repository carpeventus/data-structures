package Chap5;

import java.util.Arrays;

/**
 * 三向快速字符串排序
 */
public class Quick3String {
    // 切换为插入排序的阈值
    private static int M = 15;

    public static void sort(String[] a) {
        sort(a, 0, a.length - 1, 0);
    }

    private static void sort(String[] a, int low, int high, int d) {
        if (high <= low + M) {
            insertSort(a, low, high, d);
            return;
        }

        int lt = low;
        int gt = high;
        int i = low + 1;
        // 切分字符v是a[low]的第d个字符
        int v = charAt(a[low], d);
        while (i <= gt) {
            int t = charAt(a[i], d);
            if (t < v) {
                swap(a, lt++, i++);
            } else if (t > v) {
                swap(a, i, gt--);
            } else {
                i++;
            }
        }
        // 现在a[lo..lt-1] < v=a[lt..gt] < a[gt+1..high]成立
        // 切分元素相同的数组不会被递归算法访问到，对其左右的子数组递归排序
        sort(a, low, lt - 1, d);
        // 所有首字母与切分字符相等的子数组，递归排序，像MSD那样要忽略都相同的首字母
        if (v >= 0) {
            sort(a, lt, gt, d+ 1);
        }
        sort(a, gt + 1, high, d);
    }

    private static void swap(String[] a, int p, int q) {
        String temp = a[p];
        a[p] = a[q];
        a[q] = temp;
    }

    private static int charAt(String s, int d) {
        if (d < s.length()) {
            return s.charAt(d);
        } else {
            return -1;
        }
    }

    private static void insertSort(String[] a, int low, int high, int d) {
        for (int i = low + 1; i <= high; i++) {
            // 当前索引如果比它前一个元素要大，不用插入;否则需要插入
            if (less(a[i], a[i - 1], d)) {
                // 待插入的元素先保存
                String temp = a[i];
                // 元素右移
                int j;
                for (j = i; j > low && less(temp, a[j - 1], d); j--) {
                    a[j] = a[j - 1];
                }
                // 插入
                a[j] = temp;
            }
        }
    }

    private static boolean less(String v, String w, int d) {
        return v.substring(d).compareTo(w.substring(d)) < 0;
    }

    public static void main(String[] args) {
        String[] a = {"she", "sells", "seashells", "by", "the", "sea", "shore", "the",
                "shells", "she", "sells", "are", "surely", "seashells"};
        Quick3String.sort(a);
        System.out.println(Arrays.toString(a));
    }
}
