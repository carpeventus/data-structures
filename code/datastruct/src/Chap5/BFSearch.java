package Chap5;

/**
 * 暴力法子字符串查找
 */
public class BFSearch {
    public static int search(String p, String t) {
        int N = t.length();
        int M = p.length();
        int i = 0; // 主串的索引
        int j = 0; // 字串的索引
        while (i < N && j < M) {
            // 字符相同时，索引都加1
            if (p.charAt(j) == t.charAt(i)) {
                i++;
                j++;
            } else {
                i = i - j + 1; // 这句是关键
                j = 0;
            }
        }

        if (j == M) {
            return i - j;
        }
        else {
            return -1;
        }
    }

    public static int search_2(String p, String t) {
        int N = t.length();
        int M = p.length();
        for (int i = 0; i <= N - M; i++) {
            int j;
            for (j = 0; j < M; j++) {
                if (p.charAt(j) != t.charAt(i + j)) {
                    break;
                }
            }
            if (j == M) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        int index = BFSearch.search("good", "gootgoodgoot");
        int index_2 = BFSearch.search_2("good", "gootgoodgoot");
        System.out.println(index+ " "+ index_2);
    }
}
