package Chap9;

import java.util.*;

/**
 * 桶排序
 * 桶的数量为数组长度arr.length
 * 映射函数使用 bucketIndex = (value * arr.length) / (maxValue + 1) ,加1是为了保证最大元素可以存到数组最后一个位置
 */
public class BucketSort {
    public static void sort(int[] arr) {
        // 建立桶，个数和待排序数组长度一样
        int N = arr.length;
        LinkedList<Integer>[] bucket =(LinkedList<Integer>[]) new LinkedList[N];

        // 待排序数组中的最大值
        int maxValue = Arrays.stream(arr).max().getAsInt();
        // 根据每个元素的值，分配到对应范围的桶中
        for (int i = 0; i < arr.length; i++) {
            int index = toBucketIndex(arr[i], maxValue, N);
            // 没有桶才建立桶(延时)
            if (bucket[index] == null) {
                bucket[index] = new LinkedList<>();
            }
            // 有桶直接使用
            bucket[index].add(arr[i]);
        }

        // 对每个非空的桶排序，排序后顺便存入临时的List，则list中已经有序）
        List<Integer> temp = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            if (bucket[i] != null) {
                Collections.sort(bucket[i]);
                temp.addAll(bucket[i]);
            }
        }

        // 将temp中的数据写入原数组
        for (int i = 0; i < N; i++) {
            arr[i] = temp.get(i);
        }
    }

    // 映射函数，将值转换为应存放到的桶数组的索引
    private static int toBucketIndex(int value, int maxValue, int N) {
        return (value * N) / (maxValue + 1);
    }

    public static void main(String[] args) {
        int[] a = {44, 67, 32, 21, 9, 98, 44, 111, 99, 6};
        BucketSort.sort(a);
        System.out.println(Arrays.toString(a));
    }
}
