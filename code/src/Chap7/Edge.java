package Chap7;

/**
 * 加权无向图的边
 */
public class Edge implements Comparable<Edge> {
    private int either;
    private int other;
    private double weight;

    public Edge(int either, int other, double weight) {
        this.either = either;
        this.other = other;
        this.weight = weight;

    }

    public double weight() {
        return weight;
    }

    public int either() {
        return either;
    }

    public int other(int v) {
        if (v == either) {
            return other;
        } else if (v == other) {
            return either;
        } else throw new RuntimeException("该边无此顶点！");
    }

    @Override
    public int compareTo(Edge that) {
        return Double.compare(this.weight, that.weight);
    }

    @Override
    public String toString() {
        return "(" +
                either +
                "-" + other +
                " " + weight +
                ')';
    }
}