package Chap7;

/**
 * 关键路径算法
 */
public class CPM {
    private AcycliLP lp;
    private int s; // 虚拟的起点
    private int t; // 虚拟的终点
    private int jobsNum; // 任务个数

    public CPM(double[] jobDuration, int[][] successorAfter) {
        jobsNum = jobDuration.length;
        // 设置两个虚拟顶点，代表起点和终点
        EdgeWeightedDiGraph<?> graph = new EdgeWeightedDiGraph<>(jobsNum + 2);

        s = jobDuration.length; // 起点
        t = s + 1; // 终点

        for (int i = 0; i < jobsNum; i++) {
            // 每个顶点都可能成为最先开工的，所以虚拟起点指向所有顶点，且费时都为0
            graph.addDiEdge(new DiEdge(s, i, 0.0));
            // 每个顶点都可能成为工程收尾的活动，所有顶点都指向该虚拟终点，费时自然是每个活动所持续的时间
            graph.addDiEdge(new DiEdge(i, t, jobDuration[i]));
            // 任务i必须在任务j之前完成， 即加入i -> j的有向边
            for (int j = 0; j < successorAfter[i].length; j++) {
                int successor = successorAfter[i][j];
                graph.addDiEdge(new DiEdge(i, successor, jobDuration[i]));
            }
            // 找到到每个活动的最长路径
            lp = new AcycliLP(graph, s);
        }

    }

    public void printJobExecuteOrder() {
        System.out.println("各任务开始时间表：");
        for (int i = 0; i < jobsNum; i++) {
            System.out.println(i + ": " + lp.distTo(i));
        }
        System.out.println("\n按照以下顺序执行任务，开始时间相同的任务同时执行。");
        for (DiEdge edge : lp.pathTo(t)) {
            // 遇到起点不打印箭头
            if (edge.from() == s) {
                System.out.print(edge.to());
            }
            // 最后一个任务在前一个顶点的就打印过了，遇到最后一条边换行就行
            else if (edge.to() == t) {
                System.out.println();
            } else {
                System.out.print(" -> " + edge.to());
            }
        }

        System.out.println("总共需要" + lp.distTo(t));
    }

    public static void main(String[] args) {
        // 每个任务的持续时间
        double[] duration = {41.0, 51.0, 50.0, 36.0, 38.0, 45.0, 21.0, 32.0, 32.0, 29.0};
        // 必须在这些任务之前完成，如successorAfter[0]表示任务0的后继任务1、7、9，也就是说0必须在1、7、9之前做完
        // {} 表示该任务不要求在哪个任务执行前就得完成，说明它可能是作为收尾的任务
        int[][] successorAfter = {{1, 7, 9}, {2}, {}, {}, {}, {}, {3, 8}, {3, 8}, {2}, {4, 6}};
        CPM cpm = new CPM(duration, successorAfter);
        cpm.printJobExecuteOrder();
    }

}
