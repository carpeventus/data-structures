package Chap6;

import java.math.BigInteger;
import java.util.LinkedList;

/**
 * 二叉树
 */
public class BinaryTree<Item> {

    public static class Node<T> {
        private T data;
        private Node<T> lchild;
        private Node<T> rchild;


        public Node(T data) {
            this.data = data;
        }

        public T getData() {
            return data;
        }

        public Node<T> getLchild() {
            return lchild;
        }

        public Node<T> getRchild() {
            return rchild;
        }

        @Override
        public String toString() {
            String lchildInfo = lchild == null ? null : lchild.getData().toString();
            String rchildInfo = rchild == null ? null : rchild.getData().toString();

            return "Node{" +
                    "data=" + data +
                    ", lchild=" + lchildInfo +
                    ", rchild=" + rchildInfo +
                    '}';
        }
    }

    private Node<Item> root;
    private int nodesNum;

    public void setRoot(Item data) {
        root = new Node<>(data);
        nodesNum++;
    }

    public void addLeftChild(Item data, Node<Item> parent) {
        parent.lchild = new Node<>(data);
        nodesNum++;
    }

    public void addRightChild(Item data, Node<Item> parent) {
        parent.rchild = new Node<>(data);
        nodesNum++;
    }

    public Node<Item> parentTo(Node<Item> node) {
        return parentTo(root, node);
    }

    public Node<Item> parentTo(Node<Item> currentNode, Node<Item> node) {
        if (currentNode == null) {
            return null;
        }

        if (node.equals(currentNode.lchild) || node.equals(currentNode.rchild)) {
            return currentNode;
        }
        // 如果当前结点没找到，递归查找其左右子树
        Node<Item> p;
        if ((p = parentTo(currentNode.lchild, node)) != null) {
            return p;
            // 如果左子树中没找到，返回右子树查找结果
        } else {
            return parentTo(currentNode.rchild, node);
        }
    }

    public Node<Item> root() {
        return root;
    }

    public int degreeForNode(Node<Item> node) {
        if (node.lchild != null && node.rchild != null) {
            return 2;
        } else if (node.lchild != null || node.rchild != null) {
            return 1;
        } else {
            return 0;
        }
    }

    public int degree() {
        // 无非三种情况
        // 1. 只有一个根结点，度为0
        // 2. 斜树，度为1
        // 3.其余情况度是2
        if (root.lchild == null && root.rchild == null) {
            return 0;
            // 斜树的结点数等于其深度，包括了只有根结点的情况，所以上面的条件要先判断
        } else if (nodesNum == depth()) {
            return 1;
        } else {
            return 2;
        }
    }

    public int depthForSubTree(Node<Item> node) {
        if (node == null) {
            return 0;
        }
        // 从上到下递归，从下到上返回深度，下面就是返回某结点两个孩子中深度最大的那个，加1继续返回到上一层
        int lDepth = depthForSubTree(node.lchild);
        int rDepth = depthForSubTree(node.rchild);
        return lDepth > rDepth ? lDepth + 1 : rDepth + 1;
    }

    public int depth() {
        return depthForSubTree(root);
    }

    public int nodesNum() {
        return nodesNum;
    }

    /**
     * 前序遍历--递归
     */
    public void preOrder(Node<Item> node) {
        if (node == null) {
            return;
        }
        System.out.print(node.getData() + " ");

        preOrder(node.lchild);
        preOrder(node.rchild);
    }

    /**
     * 前序遍历--非递归
     */
    public void preOrder2(Node<Item> root) {
        // 用栈保存已经访问过的结点，便于返回到父结点
        LinkedList<Node<Item>> stack = new LinkedList<>();
        // 当前结点不为空，或者为空但有可以返回的父结点（可以进行pop操作）都可以进入循环
        while (root != null || !stack.isEmpty()) {
            // 只要当前结点，就打印，同时入栈
            while (root != null) {
                stack.push(root);
                System.out.print(root.getData()+" ");
                root = root.lchild;
            }
            // 上面while终止说明当前结点为空；返回到父结点并处理它的右子树。由于要执行pop操作，先判空
            if (!stack.isEmpty()) {
                // 返回到父结点。由于左孩子为空返回时已经弹出过父结点了，所以若是由于右孩子为空返回，会一次性返回到多层
                root = stack.pop();
                // 开始右子树的大循环（第一个while)
                root = root.rchild;
            }
        }
    }

    /**
     * 中序遍历--递归
     */
    public void inOrder(Node<Item> node) {
        if (node == null) {
            return;
        }
        inOrder(node.lchild);
        System.out.print(node.getData() + " ");
        inOrder(node.rchild);
    }

    /**
     * 中序遍历--非递归
     */
    public void inOrder2(Node<Item> root) {
        LinkedList<Node<Item>> stack = new LinkedList<>();
        while (root != null || !stack.isEmpty()) {
            while (root != null) {
                stack.push(root);
                root = root.lchild;
            }

            if (!stack.isEmpty()) {
                // 和前序遍历唯一不同的是，前序遍历是入栈时打印，中序遍历是出栈时返回到父结点才打印
                // 和前序遍历一样，由于左孩子为空返回时已经弹出过父结点了，所以若是由于右孩子为空返回，会一次性返回多层
                root = stack.pop();
                System.out.print(root.getData()+" ");
                root = root.rchild;
            }
        }
    }

    /**
     * 后序遍历--递归
     */
    public void postOrder(Node<Item> node) {
        if (node == null) {
            return;
        }
        postOrder(node.lchild);
        postOrder(node.rchild);
        System.out.print(node.getData() + " ");
    }

    /**
     * 后序遍历--非递归
     */
    public void postOrder2(Node<Item> root) {
        LinkedList<Node<Item>> stack = new LinkedList<>();
        // 存放结点被访问的信息，1表示只访问过左孩子，2表示右孩子也访问过了（此时可以打印了）
        LinkedList<Integer> visitedState = new LinkedList<>();
        while (root != null || !stack.isEmpty()) {
            while (root != null) {
                stack.push(root);
                root = root.lchild;
                // 上句访问过左孩子了，放入1
                visitedState.push(1);
            }
            // 这个while和下面的if不可交换执行顺序，否则变成了中序遍历
            // 用while而不是if是因为：结点已经访问过它的两个孩子了，先不打印而处于等待状态。随即判断若它的右孩子不为空，则仍会被push进去，待右孩子处理完后按照递归思想应该返回到等待中父结点，由于父结点访问状态已经是2，直接打印
            while (!stack.isEmpty() && visitedState.peek() == 2) {
                visitedState.pop();
                // 这里不能root = stack.pop()然后在打印root，因为如果这样的话，最后一个元素弹出赋值给root，而这个root不为空，一直while循环不会跳出
                System.out.print(stack.pop().getData()+" ");
            }
            if (!stack.isEmpty()) {
                // 注意先取出来而不删除，等到访问状态为2才能删除
                root = stack.peek();
                root = root.rchild;
                // 上句访问过右孩子了，应该更新访问状态到2
                visitedState.pop(); // 弹出1，压入2
                visitedState.push(2);
            }
        }
    }

    public void preOrder() {
        preOrder(root);
    }

    public void inOrder() {
        inOrder(root);
    }

    public void postOrder() {
        postOrder(root);
    }

    public boolean isEmpty() {
        return nodesNum == 0;
    }


    // 实际上是删除以该结点为根结点的子树,后序遍历
    public void deleteSubTree(Node<Item> node) {
        if (node == null) {
            return;
        }
        // 结点信息被清空了，但是结点本身不是null，对data进行判断，如果data已经为空就不自减了
        if (node.data != null) {
            nodesNum--;
        }
        deleteSubTree(node.lchild);
        deleteSubTree(node.rchild);
        // 删除根结点结点信息
        node.lchild = null;
        node.rchild = null;
        node.data = null;
    }

    public void clear() {
        deleteSubTree(root);
        // root.lchild和root.rchild虽然为空了但是root还不为空
        root = null;
    }

    // 根据卡特兰数递推公式 h(n)=h(n-1)*(4*n-2)/(n+1)
    //  已知 h(1) = 1;
    // 无穷数列，越到后面数字越大，使用BigInteger
    public static BigInteger numOfTreeShape(int n) {
        BigInteger a = BigInteger.ONE;
        for (int i = 2; i <= n; i++) {
            a = a.multiply(BigInteger.valueOf(4 * i - 2)).divide(BigInteger.valueOf(i + 1));
        }
        return a;
    }

    public static void main(String[] args) {
        BinaryTree<String> tree = new BinaryTree<>();
        tree.setRoot("A");
        Node<String> root = tree.root();
        tree.addLeftChild("B", root);
        tree.addRightChild("C", root);
        tree.addLeftChild("D", root.getLchild());

        tree.addLeftChild("E", root.getRchild());
        tree.addRightChild("F", root.getRchild());
        tree.addLeftChild("G", root.getLchild().getLchild());
        tree.addRightChild("H", root.getLchild().getLchild());
        tree.addRightChild("I", root.getRchild().getLchild());

        System.out.println("前序遍历如下");
        tree.preOrder();
        System.out.println("\n中序遍历如下");
        tree.inOrder();
        System.out.println("\n后序遍历如下");
        tree.postOrder();
        System.out.println("\n非递归后序遍历：");
        tree.postOrder2(tree.root());
        System.out.println();

        System.out.println(root.getRchild().getLchild().getData() + "的父结点是" + tree.parentTo(root.getRchild().getLchild()).getData());
        System.out.println("树的深度是" + tree.depth());
        System.out.println("树的度是" + tree.degree());
        System.out.println("树的结点数是" + tree.nodesNum());
        System.out.println("结点数为" + tree.nodesNum() + "的二叉树，共有" + numOfTreeShape(tree.nodesNum()) + "种不同的形态");
        // 删除左子树
        tree.deleteSubTree(root.getLchild());
        System.out.println("还剩" + tree.nodesNum() + "个结点");
        // 删除右结点的左子树
        tree.deleteSubTree(root.getRchild().getLchild());
        System.out.println("还剩" + tree.nodesNum() + "个结点");
        // 清空树
        tree.clear();
        System.out.println(tree.isEmpty());

    }
}
