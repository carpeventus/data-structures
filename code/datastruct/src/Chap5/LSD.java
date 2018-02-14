package Chap5;

import java.util.Arrays;

/**
 * 低位优先的字符串排序
 */
public class LSD {
    public static void sort(String[] a, int W) {
        // 每位数字范围0~9，基为10
        int R = 256;
        int N = a.length;
        String[] aux = new String[N];
        int[] count = new int[R+1];

        // 共需要d轮计数排序, 从最后一位开始，符合从右到左的顺序
        for (int d = W - 1; d >= 0; d--) {
            // 1. 计算频率，在需要的数组长度上额外加1
            for (int i = 0; i < N; i++) {
                // 使用加1后的索引，有重复的该位置就自增
                count[a[i].charAt(d) + 1]++;
            }
            // 2. 频率 -> 元素的开始索引
            for (int r = 0; r < R; r++) {
                count[r + 1] += count[r];
            }

            // 3. 元素按照开始索引分类，用到一个和待排数组一样大临时数组存放数据
            for (int i = 0; i < N; i++) {
                // 填充一个数据后，自增，以便相同的数据可以填到下一个空位
                aux[count[a[i].charAt(d)]++] = a[i];
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

    public static void main(String[] args) {
        String[] a = {"4PGC938", "2IYE230", "3CIO720", "1ICK750", "1OHV845", "4JZY524", "1ICK750", "3CIO720",
        "1OHV845", "1OHV845","2RLA629", "2RLA629", "3ATW723"};
        LSD.sort(a, 7);
        System.out.println(Arrays.toString(a));
    }
}
