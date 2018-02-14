package Chap9;

/**
 * 希尔排序
 */
public class ShellSort {

    public static void sort(Comparable[] a) {
        int h = 1;
        while (h < a.length / 3) {
            h = 3 * h + 1; // 1, 4, 13...
        }
        // 上面的代码是将初始增量设置为 小于N/3且满足序列3h + 1的最大值

        // 最后一次增量h = 1，之后h = 0退出while
        while (h >= 1) {
            // 和插入排序比将i = 1换成了i = h；i还是自增1，表示处理下一个子数组（交替处理各个子数组）
            for (int i = h; i < a.length; i++) {
                // a[i - 1]换成了a[i - h]
                if (less(a[i], a[i - h])) {
                    // 待插入的元素先保存
                    Comparable temp = a[i];
                    // 元素右移
                    int j;
                    // j > 0（即j >= 1）换成了j >= h;less(temp, a[j - 1])中1换成了h；j--换成了j = j - h
                    for (j = i; j >= h && less(temp, a[j - h]); j = j - h) {
                        // a[j - 1]换成了a[j - h]
                        a[j] = a[j - h];
                    }
                    // 插入
                    a[j] = temp;
                }
            }
            // 缩小增量h，最终肯定能缩小到1
            h = h / 3; // ...13, 4, 1
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
        ShellSort.sort(a);
        System.out.println(ShellSort.toString(a));
        System.out.println(ShellSort.isSorted(a));
    }
}
