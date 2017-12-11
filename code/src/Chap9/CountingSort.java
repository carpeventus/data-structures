package Chap9;

import java.util.Arrays;
import java.util.List;

/**
 * 计数排序
 */
public class CountingSort {
    public static void sort(int[] arr) {
        // 计算最大最小值，严谨实现最好用ifPresent检查下
        int max = Arrays.stream(arr).max().getAsInt();
        int min = Arrays.stream(arr).min().getAsInt();
        int N = arr.length;
        int R = max - min + 1; // 最大最小元素之间范围[min, max]的长度
        // 1. 计算频率，在需要的数组长度上额外加1
        int[] count = new int[R + 1];
        for (int i = 0; i < N; i++) {
            // 使用加1后的索引，有重复的该位置就自增
            count[arr[i] - min + 1]++;
        }
        // 2. 频率 -> 元素的开始索引
        for (int r = 0; r < R; r++) {
            count[r + 1] += count[r];
        }

        // 3. 元素按照开始索引分类，用到一个和待排数组一样大临时数组存放数据
        int[] aux = new int[N];
        for (int i = 0; i < N; i++) {
            // 填充一个数据后，自增，以便相同的数据可以填到下一个空位
            aux[count[arr[i] - min]++] = arr[i];
        }
        // 4. 数据回写
        for (int i = 0; i < N; i++) {
            arr[i] = aux[i];
        }
    }

    public static void main(String[] args) {
        int[] a = {9, 7, 6, 3, 9, 2, 7, 3, 2, 8};
        CountingSort.sort(a);
        System.out.println(Arrays.toString(a));
    }
}
