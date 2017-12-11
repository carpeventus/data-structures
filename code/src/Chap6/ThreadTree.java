package Chap6;

/**
 * 线索二叉树
 */
public class ThreadTree<Item> {
    public static class Node<T> {
        private T data;
        private Node<T> lchild;
        private Node<T> rchild;
        private boolean isleftThread;
        private boolean isRightThread;

        public Node(T data) {
            this.data = data;
            isleftThread = false;
            isRightThread = false;
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
    private Node<Item> preNode;
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

    public Node<Item> root() {
        return root;
    }

    /**
     * 中序遍历线索化二叉树
     */
    public void inOrderThread(Node<Item> node) {
        if (node == null) {
            return;
        }
        inOrderThread(node.lchild);

        if (node.lchild == null) {
            node.lchild = preNode;
            node.isleftThread = true;
        }

        if (preNode != null && preNode.rchild == null) {
            preNode.rchild = node;
            preNode.isRightThread = true;
        }
        // preNode始终表示上一个访问的结点
        preNode = node;
        inOrderThread(node.rchild);
    }

    public void inOrderThread() {
        inOrderThread(root);
    }

    public void inOrderTraversal(Node<Item> node) {
        // 不断深入左子树,只要某个结点左孩子为空，则标志位肯定为true
        while (node != null) {
            while (!node.isleftThread) {
                node = node.lchild;
            }

            System.out.print(node.getData() + " ");
            while (node.isRightThread && node.rchild != null) {
                node = node.rchild;
                System.out.print(node.getData() + " ");
            }
            node = node.rchild;
        }
    }

    public void inOrderTraversal() {
        inOrderTraversal(root);
    }

    /**
     * 前序遍历线索化二叉树
     */
    public void preOrderThread(Node<Item> node) {
        if (node == null) {
            return;
        }

        if (node.lchild == null) {
            node.lchild = preNode;
            node.isleftThread = true;
        }

        if (preNode != null && preNode.rchild == null) {
            preNode.rchild = node;
            preNode.isRightThread = true;
        }
        // preNode始终表示上一个访问的结点
        preNode = node;
        // 这里需要判断，因为node.lchild和node.rchild可能已经被设置了标志。若还递归就会打乱了已设置好的标志位，而且还会StackOverflow
        // 而中序遍历递归是，标志位均未被设置，所以无需判断
        if (!node.isleftThread) {
            preOrderThread(node.lchild);
        }
        if (!node.isRightThread) {
            preOrderThread(node.rchild);
        }
    }

    public void preOrderThread() {
        preOrderThread(root);
    }

    public void preOrderTraversal(Node<Item> node) {
        while (node != null) {
            while (!node.isleftThread) {
                System.out.print(node.getData() + " ");
                node = node.lchild;
            }

            System.out.print(node.getData() + " ");
            node = node.rchild;
        }
    }

    public void preOrderTraversal() {
        preOrderTraversal(root);
    }

    public static void main(String[] args) {
        ThreadTree<String> tree = new ThreadTree<>();
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

        System.out.println("中序线索化并遍历");
        tree.inOrderThread();
        tree.inOrderTraversal();

        // 线索化只能调用一次！！！一旦设置好，就不要去打乱了。所以想运行上面的需要注释掉下面的
//        System.out.println("前序线索化并遍历");
//        tree.preOrderThread();
//        tree.preOrderTraversal();
    }
}
