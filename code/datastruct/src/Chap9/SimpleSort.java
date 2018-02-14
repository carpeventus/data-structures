package Chap9;

/**
 * 简单排序
 */
public class SimpleSort {
    public static void sort(Comparable[] a) {
        // 倒数第二个元素也确定了，那最后一个元素自然而然就确定了，其实i < a.length - 1就好
        // 但是i < length也是可以的，进入第二层循环后j = a.length直接跳出循环
        for (int i = 0; i < a.length; i++) {
            for (int j = i +1; j < a.length; j++) {
                // 如果后面的比当前元素小，就交换
                if (less(a[j], a[i])) {
                    swap(a, i, j);
                }
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
        SimpleSort.sort(a);
        System.out.println(SimpleSort.toString(a));
        System.out.println(isSorted(a));
    }

}
