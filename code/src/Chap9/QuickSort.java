package Chap9;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 快速排序
 */
public class QuickSort {

    private static int partition(Comparable[] a, int low, int high) {
        // 下面使用++i和--j的形式，因此i和j的定义如下
        int i = low;
        int j = high + 1;
        // 切分元素保存下来
        Comparable v = a[low];

        while (true) {
            // 从左到右扫描，直到遇到大于等于v的元素为止
            while (less(a[++i], v)) {
                if (i == high) {
                    break;
                }
            }
            // 从右到左扫描，直到遇到小于等于v的元素为止
            while (less(v, a[--j])) {
                if (j == low) {
                    break;
                }
            }
            // 由于指针是先自增，所以先判断指针是否相遇，相遇就退出while
            if (i >= j) {
                break;
            }
            // 若没有相遇就交换元素
            swap(a, i, j);
        }
        // 切分元素交换到合适的位置
        swap(a, low, j);
        return j;
    }

    public static void sort(Comparable[] a) {
        // 随机打乱数组，大大减小最坏情况的概率
        shuffle(a);
        sort(a, 0, a.length - 1);
    }

    private static void shuffle(Comparable[] a) {
        // asList返回的是实际上是ArrayList，而ArrayList的底层是数组，所以打乱了b，a也被打乱了
        List<Comparable> b = Arrays.asList(a);
        Collections.shuffle(b);
    }

    private static void sort(Comparable[] a, int low, int high) {
        // high = low说明数组被划分到只有一个元素，不能再切分，直接返回
        // high <= low + 15 说明当数组长度不超过16时都换用插入排序

        if (high <= low + 15) {
            InsertSort.sort(a);
            return;
        }
        // 切分元素已经排定
        int j = partition(a, low, high);
        // 对切分元素左数组排序
        sort(a, low, j - 1);
        // 对切分元素右数组排序
        sort(a, j + 1, high);
        // 三者结合起来的数组有序！
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

        QuickSort.sort(a);
        System.out.println(QuickSort.toString(a));
    }
}
