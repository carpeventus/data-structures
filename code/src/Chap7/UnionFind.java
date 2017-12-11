package Chap7;

/**
 * 连通分量Union-Find算法
 */
public class UnionFind {
    // id相同的分量是连通的
    private int[] parentTo;
    //连通分量的个数
    private int count;

    public UnionFind(int num) {
        count = num;
        parentTo = new int[num];
        for (int i = 0; i < num; i++) {
            parentTo[i] = i;
        }
    }

    public int count() {
        return count;
    }

    public int find(int p) {
        // p = parentTo[p]说明到达树的根结点，返回根结点
        while (p != parentTo[p]) {
            p = parentTo[p];
        }
        return p;
    }

    public void union(int p,int q) {
        int pRoot = find(p);
        int qRoot = find(q);
        if (pRoot == qRoot) {
            return;
        }
        // 这行的意思就是q所在连通分量和q所在连通分量合并
        // 从树的角度来看，p树的根结点成为了q树根结点的孩子结点
        // 反过来也可以，parentTo[qRoot] = pRoot;
        parentTo[pRoot] = qRoot;
        count--;
    }
    // 所属连通分量的id
//    public int find(int p) {
//        return parentTo[p];
//    }
//
//    public void union(int p ,int q) {
//        int pID = find(p);
//        int qID = find(q);
//
//        if (pID == qID) {
//            return;
//        }
//        // 将和p同一个连通分量的结点全部归到和q一个分量中，即将p所在连通分量与q所在连通分量合并
//        for (int i = 0; i < parentTo.length; i++) {
//            if (parentTo[i] == pID) {
//                parentTo[i] = qID;
//            }
//        }
//        // 合并后，连通分量减少1
//        count--;
//    }

    public boolean isConnected(int p, int q) {
        return find(p) == find(q);
    }

}
