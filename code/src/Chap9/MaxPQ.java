package Chap9;

import java.util.NoSuchElementException;

/**
 * 优先队列，基于最大堆，可方便地操作堆顶的最大元素
 */
public class MaxPQ<Key extends Comparable<Key>> {

    private Key[] pq;
    // 优先队列元素个数
    private int N;

    public MaxPQ() {
        // pq[0]没有使用
        pq = (Key[]) new Comparable[2];
    }
    // 传入数组，构造有序堆
    public MaxPQ(Key... keys) {
        pq = (Key[]) new Comparable[keys.length +1];
        N = keys.length;

        for (int i = 0; i < N; i++) {
            pq[i+1] = keys[i];
        }
        for (int k = N / 2; k >= 1 ; k--) {
            sink(k);
        }
    }

    private void resize(int max) {
        Key[] temp = (Key[]) new Comparable[max];
        // 有效值区间为[1, N]
        for (int i = 1; i <= N; i++) {
            temp[i] = pq[i];
        }
        pq = temp;
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public int size() {
        return N;
    }

    public void insert(Key key) {
        // 由于下标从1开始算，存满时N就等于pq.length -1
        if (N == pq.length - 1) {
            resize(pq.length * 2);
        }
        // 注意是++N，从pq[1]开始存
        pq[++N] = key;
        swim(N);
    }

    public Key max() {
        if (isEmpty()) {
            throw new NoSuchElementException("优先队列为空");
        }
        return pq[1];
    }

    public Key delMax() {
        if (isEmpty()) {
            throw new NoSuchElementException("优先队列为空");
        }
        Key max = pq[1];
        // 第一个和最后一个元素交换
        swap(1, N);
        // 删除位置N的元素，长度减1
        pq[N--] = null;
        // 删除后要恢复堆有序状态
        sink(1);

        if (N > 0 && N == pq.length / 4) {
            resize(pq.length / 2);
        }
        return max;
    }

    private void swim(int k) {
        // k = 1说明当前元素浮到了根结点，它没有父结点可以比较，也不能上浮了，所以k <= 1时候推出循环
        while (k > 1 && less(k / 2, k)) {
            swap(k/2, k);
            // 上浮后，成为父结点，所以下标变成原父结点
            k = k / 2;
        }
    }

    private void sink(int k) {
        // 父结点的位置k最大值为 N/2,若k有左子结点无右子结点，那么2k = N；若两个子结点都有，那么2k + 1 = N
        // 有可能位置k只有左子结点，依然要比较，用2k + 1 <= N这个的条件不会执行比较，所以用2k <= N条件
        while (2 * k <= N) {
            int j = 2 * k;
            // 可以取j = N -1,less(N -1, N);由于下标从1开始，所以pq[N]是有元素的
            if (j < N && less(j,j+1)) {
                // 右子结点比左子结点大 ，取右子结点的下标
                j++;
            }
            // 左子结点或者右子结点和父结点比较
            // 如果pq[k] >= pq[j]，即父结点大于等于较大子结点时，停止下沉
            if (!less(k, j)) {
                break;
            }
            // 否则交换
            swap(k, j);
            // 下沉后，下标变成与之交换的元素下标
            k = j;
        }
    }

    private boolean less(int i, int j) {
        return pq[i].compareTo(pq[j]) < 0;
    }

    private void swap(int i, int j) {
        Key temp = pq[i];
        pq[i] = pq[j];
        pq[j] = temp;
    }

    public static void main(String[] args) {
        MaxPQ<Integer> maxPQ = new MaxPQ<>(4,3,1,2,5);
        System.out.println(maxPQ.delMax());
        System.out.println(maxPQ.delMax());
        System.out.println(maxPQ.delMax());
    }
}
