package Chap5;

/**
 * KMP子字符串查找
 */
public class KMPSearch {
    private static int[] getNext(String p) {
        int M = p.length();
        int[] next = new int[M];
        next[0] = -1;
        int j = 0;
        int k = -1;
        while (j < M - 1) {
            if (k == -1 || p.charAt(k) == p.charAt(j)) {
                next[++j] = ++k;
            } else {
                k = next[k];
            }
        }
        return next;
    }

    private static int[] betterGetNext(String p) {
        int M = p.length();
        int[] next = new int[M];
        next[0] = -1;
        int j = 0;
        int k = -1;
        while (j < M - 1) {
            if (k == -1 || p.charAt(k) == p.charAt(j)) {
                if (p.charAt(k + 1) == p.charAt(j + 1)) {
                    next[++j] = next[++k];
                } else {
                    next[++j] = ++k;
                }
            } else {
                k = next[k];
            }
        }
        return next;
    }

    public static int search(String p, String t) {
        // 根据模式字符串获得next数组
        int[] next = betterGetNext(p);
        int N = t.length();
        int M = p.length();
        int i = 0;
        int j = 0;
        while (i < N && j < M) {
            if (j == -1 || p.charAt(j) == t.charAt(i)) {
                i++;
                j++;
            } else {
                j = next[j];
            }
        }
        if (j == M) {
            return i - j;
        } else {
            return -1;
        }
    }


    public static void main(String[] args) {
        int index = search("abab", "abacghababzz");
        System.out.println(index);
    }
}
