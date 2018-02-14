package Chap5;

/**
 * Boyer-Moore子字符串查找
 */
public class BoyerMoore {

    public static int search(String pat, String txt) {
        int N = txt.length();
        int M = pat.length();
        // 根据模式串得到right[]数组
        int[] right = getRight(pat);
        // 匹配失败时，i需要右移的位数
        int skip;
        for (int i = 0; i <= N - M ; i += skip) {
            skip = 0;
            for (int j = M - 1; j >= 0 ; j--) {
                if (pat.charAt(j) != txt.charAt(i + j)) {
                    skip = j - right[txt.charAt(i + j)];
                    // 如果计算出来的skip不能使得i右移，直接让i向右移动1位
                    if (skip < 1) {
                        skip = 1;
                    }
                    break;
                }
            }
            // 经过上面的循环，字符都满足pat.charAt(j) == txt.charAt(i + j)，说明找到匹配
            if (skip == 0) {
                return i;
            }
        }
        return -1;
    }

    private static int[] getRight(String pat) {
        int R = 256;
        int[] right = new int[R];
        // 先初始化为全-1
        for (int i = 0; i < R; i++) {
            right[i] = -1;
        }
        // 记录模式字符串每个字符最后一次出现的索引
        for (int j = 0; j < pat.length(); j++) {
            right[pat.charAt(j)] = j;
        }
        return right;
    }

    public static void main(String[] args) {
        int index = BoyerMoore.search("abab", "abacghababzz");
        System.out.println(index);
    }
}
