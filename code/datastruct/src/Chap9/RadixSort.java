package Chap9;

import java.util.Arrays;

/**
 * 基数排序
 */
public class RadixSort {
    public static void sort(int[] a) {
        // 每位数字范围0~9，基为10
        int R = 10;
        int N = a.length;
        int[] aux = new int[N];
        int[] count = new int[R+1];
        // 以关键字来排序的轮数，由位数最多的数字决定，其余位数少的数字在比较高位时，自动用0进行比较
        // 将数字转换成字符串，字符串的长度就是数字的位数，字符串最长的那个数字也拥有最多的位数
        int W = Arrays.stream(a).map(s -> String.valueOf(s).length()).max().getAsInt();

        // 共需要d轮计数排序, 从d = 0开始，说明是从个位开始比较，符合从右到左的顺序
        for (int d = 0; d < W; d++) {
            // 1. 计算频率，在需要的数组长度上额外加1
            for (int i = 0; i < N; i++) {
                // 使用加1后的索引，有重复的该位置就自增
                count[digitAt(a[i], d) + 1]++;
            }
            // 2. 频率 -> 元素的开始索引
            for (int r = 0; r < R; r++) {
                count[r + 1] += count[r];
            }

            // 3. 元素按照开始索引分类，用到一个和待排数组一样大临时数组存放数据
            for (int i = 0; i < N; i++) {
                // 填充一个数据后，自增，以便相同的数据可以填到下一个空位
                aux[count[digitAt(a[i], d)]++] = a[i];
            }
            // 4. 数据回写
            for (int i = 0; i < N; i++) {
                a[i] = aux[i];
            }
            // 重置count[]，以便下一轮统计使用
            for (int i = 0; i < count.length; i++) {
                count[i] = 0;
            }

        }
    }
    // 根据d，获取某个值的个位、十位、百位等，d = 0取出个位，d = 1取出十位，以此类推。对于不存在的高位，用0补
    private static int digitAt(int value, int d) {
        return (value / (int) Math.pow(10, d)) % 10;
    }

    public static void main(String[] args) {
        int[] a = {244, 167, 1234, 321, 29, 98, 1444, 111, 99, 6};
        RadixSort.sort(a);
        System.out.println(Arrays.toString(a));
    }
}
