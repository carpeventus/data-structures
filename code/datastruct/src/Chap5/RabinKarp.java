package Chap5;

import java.math.BigInteger;
import java.util.Random;

/**
 * Rabin-Karp子字符串查找
 */
public class RabinKarp {
    private static int R = 256;
    private static long Q = longRandomPrime();

    private static long hash(String key, int M) {
        long h = 0;
        for (int i = 0; i < M; i++) {
            h = (h * R + key.charAt(i)) % Q;
        }
        return h;
    }

    public static int search(String pat, String txt) {
        int N = txt.length();
        int M = pat.length();
        long RM = 1;
        // 计算R^(M-1) % Q  用于减去第一个数字时，该值要和第一个数字相乘
        for (int i = 0; i < M - 1; i++) {
            RM = (RM * R) % Q;
        }
        // 模式的散列值
        long patHash = hash(pat, M);
        // 文本最开始M位的散列值
        long txtHash = hash(txt, M);
        // 如果一开始就匹配了
        if (patHash == txtHash && checkEqual(pat, txt, 0)) {
            return 0;
        }
        // 否则减去第一个数字，再加上后一个数字，得到散列值继续匹配
        // 从M开始，因为模式是[0, M-1]，M是模式最后一位的下一位
        // txtHash - RM * txt.charAt(i - M)用当前hash减去第一位数字，+Q主要是防止前面的结果为负数， *R是乘以基数， +txt.charAt(i)是加上后一位数字，最后%Q取余
        for (int i = M; i < N; i++) {
//            txtHash = (txtHash + Q - RM * txt.charAt(i - M) % Q) % Q;
//            txtHash = (txtHash*R +txt.charAt(i)) % Q;
            // 等价于上面的两句,性质还是同余模定理：每一个计算后都取一次余，和所有计算结束后取余的结果一样。
            txtHash = ((txtHash - RM * txt.charAt(i - M) + Q) * R + txt.charAt(i)) % Q;
            // 找到匹配。如果第一次就匹配，返回1，可归纳出应该返回 i -M + 1
            if (txtHash == patHash && checkEqual(pat, txt, i - M + 1)) {
                return i - M + 1;
            }
        }
        return -1; // 未找到匹配
    }

    // 散列值相同时检查每个字符是否相同, 拉斯维加斯版本
    private static boolean checkEqual(String pat, String txt, int offset) {
        for (int j = 0; j < pat.length(); j++) {
            if (pat.charAt(j) != txt.charAt(offset + j))
                return false;
        }
        return true;
    }

    // 返回一个31位的随机素数,用于除留余数的Q
    private static long longRandomPrime() {
        BigInteger prime = BigInteger.probablePrime(31, new Random());
        return prime.longValue();
    }

    public static void main(String[] args) {
        int index = RabinKarp.search("abab", "abacghababzz");
        System.out.println(index);
    }
}
