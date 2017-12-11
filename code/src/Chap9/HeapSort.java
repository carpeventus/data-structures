package Chap9;

/**
 * 堆排序
 */
public class HeapSort {

    public static void sort(Comparable[] a) {
        // 堆的构造
        int N= a.length;
        for (int k = N / 2; k >= 1; k--) {
            sink(a, k, N);
        }
        // 最大元素交换到数组右边
        // 倒数第二个元素排定数组就已经有序了，所以N = 1时只剩一个元素不用再操作了
        while (N > 1) {
            swap(a, 1, N--);
            sink(a, 1, N);
        }
    }

    private static void sink(Comparable[] a, int k, int N) {
        // 父结点的位置k最大值为 N/2,若k有左子结点无右子结点，那么2k = N；若两个子结点都有，那么2k + 1 = N
        // 有可能位置k只有左子结点，依然要比较，用2k + 1 <= N这个的条件不会执行比较，所以用2k <= N条件
        while (2 * k <= N) {
            int j = 2 * k;
            // 可以取j = N -1,less(N -1, N);由于下标从1开始，所以pq[N]是有元素的
            if (j < N && less(a, j,j+1)) {
                // 右子结点比左子结点大 ，取右子结点的下标
                j++;
            }
            // 左子结点或者右子结点和父结点比较
            // 如果pq[k] >= pq[j]，即父结点大于等于较大子结点时，停止下沉
            if (!less(a, k, j)) {
                break;
            }
            // 否则交换
            swap(a, k, j);
            // 下沉后，下标变成与之交换的元素下标
            k = j;
        }
    }

    // 由于sort方法和sink方法都是从下标1开始算，即认为有元素的区间为[1, a.length]，但实际上传入的数组有元素区间为[0, a.length -1]
    // 所以swap(a, p, q)实际交换的元素是a[p-1]和a[q -1]
    private static void swap(Comparable[] a, int p, int q) {
        Comparable temp = a[p-1];
        a[p-1] = a[q-1];
        a[q-1] = temp;
    }
    //  // 由于sort方法和sink方法都是从下标1开始算，即认为有元素的区间为[1, a.length]，但实际上传入的数组有元素区间为[0, a.length -1]
    // 所以less(a, i, j)实际比较的元素的是a[i-1]和a[j -1]
    private static boolean less(Comparable[] a, int i, int j) {
        return a[i-1].compareTo(a[j-1]) < 0;
    }
    // less方法变了
    public static boolean isSorted(Comparable[] a) {
        for (int i = 1; i < a.length; i++) {
            if (less(a, i + 1, i)) {
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
        HeapSort.sort(a);
        System.out.println(HeapSort.toString(a));
        System.out.println(HeapSort.isSorted(a));
    }

}
