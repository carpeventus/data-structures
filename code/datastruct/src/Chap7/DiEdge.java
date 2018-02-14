package Chap7;

/**
 * 加权有向图中的有向边
 */
public class DiEdge {
    private int from;
    private int to;
    private double weight;

    public DiEdge(int from, int to, double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public int from() {
        return from;
    }

    public int to() {
        return to;
    }

    public double weight() {
        return weight;
    }

    @Override
    public String toString() {
        return "(" +
                from +
                "->" + to +
                " " + weight +
                ')';
    }
}
