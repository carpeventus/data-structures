# 数据结构与算法--从平衡二叉树(AVL)到红黑树

上节学习了二叉查找树。算法的性能取决于树的形状，而树的形状取决于插入键的顺序。在最好的情况下，n个结点的树是完全平衡的，如下图“最好情况”所示，此时树的高度为`⌊log2 n⌋ + 1`，所以时间复杂度为O(lg n)当我们将键以升序或者降序插入的时候，得到的是一棵斜树，如下图中的“最坏情况”，树的高度为n，时间复杂度也变成了O(n)

![](http://obvjfxxhr.bkt.clouddn.com/bst_10.PNG)

在最坏情况下，二叉查找树的查找和插入效率很低。为了解决这个问题，引出了平衡二叉树(AVL)。

## 平衡二叉树介绍

**平衡二叉树，首先是一棵二叉查找树，但是它满足一点重要的特性：每一个结点的左子树和右子树的高度差最多为1。**这个高度差限制就完全规避了上述的最坏情况，因此查找、插入和删除的时间复杂度都变成了O(lg n)。

为了反映每个结点的高度差，在二叉查找树的结点中应该增加一个新的域——被称为平衡因子(BF)，它的值是某个根结点的左子树深度减右子树深度的值。易知，**对于一棵平衡二叉树，每个结点的平衡因子只可能是-1、0、1三种可能。**

![](http://obvjfxxhr.bkt.clouddn.com/rbt_IMG_20171026_093109.jpg-s)

上图中图1和图4是平衡树。图2根本不是二叉查找树，因为59大于58却是58的左子结点；图3中结点58的左子树高度为3而右子树的高度为0，不满足平衡二叉树的定义。不过将图3稍作改变，得到图4，它就是一棵平衡二叉树了。

将每个结点的平衡因子控制在-1、0、1三个值是靠一种称为**旋转(Rolate)**的操作保证的，视情况分为**左旋转**和**右旋转**。

![](http://obvjfxxhr.bkt.clouddn.com/avl_2.PNG)

如图插入1的时候，发现根结点3的平衡因子变成了2（正数），对结点3进行右旋转修正成上图2的样子。

![](http://obvjfxxhr.bkt.clouddn.com/avl_3.PNG)

而当插入5时，发现结点3的平衡因子为-2（负数），所以需要对结点3进行左旋转修正成上图5的样子。

![](http://obvjfxxhr.bkt.clouddn.com/avl_4.PNG)

再看插入结点9的情况，结点7的平衡因子变成了-2，按理说应该对7进行左旋转（上图11），然而得到的确实图11虚线框中的子树，9位于10的右子结点这明显就是错的。究其原因，主要是因为**不平衡结点7和它的子树10的平衡因子符号相反（一正一负），这种情况出现在新结点插入在根结点的左孩子的右子树、或者根结点的右孩子的左子树。**后者情况下（即上图情况），需要先对根结点7的子结点10先作右旋转处理再对根结点7进行左旋处理。再回头看前两种插入情况，都是在根结点的左孩子的的左子树或者根结点的右孩子的右子树上插入的，根结点的平衡因子符号和它子结点的平衡因子符号相同。

接下来看看这个旋转处理是怎么用代码表示的，以下所说的“根结点”指的是任意子树的根。

```java
public void rotateLeft(Node h) {
    Node x = h.right; // 根结点的右孩子保存为x
  	h.right = x.left; // 根结点右孩子的左孩子挂到根结点的右孩子上
  	x.left = h; // 根结点挂到根结点右孩子的左孩子上
  	h = x; // 根结点的右孩子代替h称为新的根结点
}

public void rotateRight(Node h) {
    Node x = h.left; // 根结点的左孩子保存为x
  	h.left = x.right; // 根结点左孩子的右孩子挂到根结点的左孩子上
  	x.right = h; // 根结点挂到根结点左孩子的右孩子上
  	h = x; // 根结点的左孩子代替h称为新的根结点
}
```

建议在纸上画画加深理解，其实旋转操作没那么难。

插入的话就是以下四种情况

- 在根结点的左孩子的左子树上插入，对根结点进行右旋转。调用`rotateRight`
- 在根结点的右孩子的右子树上插入，对根结点进行左旋转。调用`rotateLeft`
- 在根结点的左孩子的右子树上插入，先对根结点的左孩子进行左旋转，再对根结点进行右旋转。调用`rotateLeft(h.left);rotateRight(h);`
- 在根结点的右孩子的左子树上插入，先对根结点的右孩子进行右旋转，再对根结点进行左旋转。调用`rotateRight(h.right);rotateLeft(h);`

插入之后还要调整每个结点的平衡因子，看起来比较麻烦，代码量不小。删除操作也是比较麻烦。由于我们的重点在于讲解红黑树，平衡查找树只是抛砖引玉。所以对于平衡二叉树的介绍就到此为止。

## 2-3树介绍

为了保证查找树的平衡性，我们允许树中一个结点保存多个键 。标准二叉查找树中的结点只能保存一个键，拥有两条链接，这种结点被称为**2-结点**；如果某个结点可以存储两个键，拥有3条链接，这种结点被称为**3-结点**。

- 2-结点，左链接指向的2-3树中的键都小于该结点，右链接指向的2-3树中的键都大于该结点。
- 3-结点，左链接指向的2-3树中的键都小于该结点，中链接指向的2-3树中的键都位于该结点的两个键之间，右链接指向的2-3树中的键都大于该结点。

我们规定，一个2-结点要么拥有两个子结点，要么没有子结点；一个3-结点要么拥有三个子结点，要么没有子结点。这样的保证使得2-3树的所有叶子结点位于同一层，也就是说所有叶子结点到根结点的路径长度是一样的，达到了所谓的完美平衡。如下是一棵2-3树

![](http://obvjfxxhr.bkt.clouddn.com/23_1.PNG)

### 2-3树的查找

2-3树的查找和标准的二叉查找树如出一辙，只是多了在中链接的递归查找。具体来说：先将要查找的key与2-3树的根结点比较，若和根结点中任意一个键相等则查找命中；否则，若key小于根结点中的较小键，在根结点的左子树中递归查找；若key大于根结点中的较大者，在根结点的右子树中递归查找；若key在根结点两个键的之间，则在根结点的中子树中递归查找...下面分别展示了查找成功和失败的轨迹。

![](http://obvjfxxhr.bkt.clouddn.com/23_2.PNG)

### 2-3树的插入

插入操作，肯定是查找未命中时。如果未命中的查找结束于一个2-结点，直接插入到该结点中，使其变成3-结点就好了。可如果查找结束于一个3-结点该怎么办呢？2-3树中并不允许4-结点啊。

![](http://obvjfxxhr.bkt.clouddn.com/23_3.PNG)

有几种情况，我们一一来看。

#### 向一棵只含有一个3-结点的树中插入新键

考虑一种最简单的情况，一棵2-3树中只有一个3-结点，此时插入一个新键。我们可以这样做：先让该键暂时存放于3-结点中，随即将3个键中排名中间的键向上移（因此树的高度增加了1），左边的键成为上移键的左子结点，右边的键成为上移键的右子树，最后这个临时的4-结点被分解成了3个2-结点。如下图

![](http://obvjfxxhr.bkt.clouddn.com/23_4.PNG)

#### 向一个父结点是2-结点的3-结点中插入新键

如果树比较复杂，其实也没关系，和上面的简单情况是同样的处理方法。

![](http://obvjfxxhr.bkt.clouddn.com/23_5.PNG)



如图，排名中间的键X上移和R合并称为了3-结点。

#### 向一个父结点是3-结点的3-结点插入新键

一样的处理方法，无非就是再向上移，如下左图所示，在树的底部插入D，将排名中间的C上移和EJ合并成4-结点，继续将排名中间的E上移，和根结点M合并成为3-结点。

![](http://obvjfxxhr.bkt.clouddn.com/23_6.PNG)

如果到根结点还是4-结点呢，那就按照第一种情况处理——向一棵只含有3-结点的树中插入新键，只需将4-结点分解成3个2-结点即可，同时树的高度增加了1。

#### 局部变换与全局性质

4-结点的分解是局部的：除了相关的结点和链接之外，树的其他所有结点的状态都不会被修改。即每次变换，不是整棵树都变化了。下图能比较直观理解这种变换的局部性。

![](http://obvjfxxhr.bkt.clouddn.com/23_7.PNG)

这些局部变换不会影响树的全局有序性和平衡性：任意叶子结点到根结点的路径长度都是相等的。

### 2-3树的删除

2-3树的插入分好几种情况，但还算不难理解。删除操作的话就更难了。这里只介绍简单的情况，删除最小最大键。删除任意键在红黑树中会有介绍。

如果要删除的结点是一个3-结点，最简单，直接删除掉，因此3-结点变成了2结点。

如果删除的是一个2-结点呢？

#### 删除最小键

先看最小键的删除。如果当前要被删除的结点是一个2-结点，那就想办法把它变成一个3-结点或者4-结点，然后直接删除即可。

![](http://obvjfxxhr.bkt.clouddn.com/23_10.PNG)

如上图中的5种变换：

- 当前的结点左子结点和右子结点都是2-结点，见图中第1、4种变换，它们是将这三个结点合并成了一个4-结点。
- 当前结点的左子结点是2-结点，但是右子结点不是2-结点。见图中第2、3种情况，它们的做法是从左子结点的兄弟结点中借一个最小的键到当前结点（它们的父结点），再将当前结点中最小的键移动到左子结点中。
- 一旦要被删除结点不是2-结点就可以执行删除了，这保证了2-3树的有序性和平衡性。见图中第5种变换。

#### 删除最大键

和删除最小键的处理方法类似。如下图

![](http://obvjfxxhr.bkt.clouddn.com/23_11.PNG)

也是当前结点的左右子结点都是2-结点就将这三个结点合并成4-结点，如图左边的combine siblings；当右子结点是2-结点，左子结点不是2-结点，那么从右子结点的兄弟结点中借一个最大结点到当前结点（它们的父结点），然后将当前结点中最大的键移动到右子结点，如图中borrow from siblings。

## 红黑树

2-3树理解不难，而且和平衡二叉树比讨论情况有所减少。而接下来介绍的左倾**红黑树（Left leaning Red-Black Tree）**就是为了用简单的方法实现2-3树，进一步减少讨论的情况和代码量。2-3树中2-结点就是标准二叉查找树中的结点，为了表达3-结点需要附加额外的信息。这里讲的红黑树可能有别于常规的定义方法。接下来你会看到，我们**在结点与结点的链接上着色（而不是着色结点）。**左倾红黑树必须满足以下几点：

- 红链接均是红链接，即不存在有某个右链接是红色的，这可以保证更少的讨论情况从而减少代码量。
- 没有任何一个结点同时和两条红链接相连，也就是不允许连续的两条红链接、或者一个结点的左右链接都是红色。
- 该树是**完美黑色平衡**的，也就是说任意叶子结点到根结点的路径上黑色链接数量相同。
- 根结点始终是黑色的。

我们将两个用红色链接相连的结点表示为一个3-结点。

![](http://obvjfxxhr.bkt.clouddn.com/rbt_2.PNG)

如图，加粗的黑线（没找到彩图...）是被着色为红色的链接，a和b被红链接相连，因此a、b其实是一个3-结点。

![](http://obvjfxxhr.bkt.clouddn.com/rbt_1.PNG)

上图是个彩图了...同样的我们可以定义4-结点：某结点的左右链接都是红的，和这两条红链接相连的三个结点就是一个4-结点，这里只是提一下，左倾红黑树不会用到4-结点。下面我们如果提到“红黑树”那它指代就是“左倾红黑树”。

因此我们完全可以用附带了颜色信息的二叉查找树来表示2-3树。而且标准二叉查找树中的`get(Key key)`方法无需修改直接就能用于左倾红黑树！容易知道，**红黑树既是二叉查找树，又是2-3树。因此它结合了两者的优势：二叉查找树中高效的查找方法和2-3树中高效的平衡插入算法。**

看到一棵红黑树，如果将其直观地表示成2-3树呢？我们只需将所有左链接画平，并将与红链接相连的结点合并成一个3-结点即可。如下所示，加粗的黑色链接是红链接

![](http://obvjfxxhr.bkt.clouddn.com/rbt_3.PNG)

之前一直说链接的红黑，**表达的是指向某个结点的链接的颜色。**

![](http://obvjfxxhr.bkt.clouddn.com/rbt_4.PNG)

比如上图中C、E之间的链接是红色的，这条链接指向C，因此这条链接的颜色是属于结点C的，我们也可以简单地说“（指向）C结点（的链接）是红色的”；那么对于结点J，指向它的链接颜色是黑的。叶子结点也有左右链接，虽然它们都是空，**约定（指向null的）空链接的颜色是黑色的。**如A的左子结点的链接颜色`A.left.color = BLACK`。哦对了，还有指向根结点的链接（虽然这么说很奇怪，因为事实上并没有链接指向根结点，为了保持结点性质的一致性，我们还是这么叫了），上面左倾红黑树的定义中有说到其颜色必须是黑色的，因为根结点的左孩子有可能是红链接，如果根结点也是红链接，就违反了定义的第二条——没有任何一个结点同时和两条红链接相连。总之上面提到了一些约定，这些都是为了我们实现时更加方便，所以在代码中要时刻保证这些约定。

说了这么多，来试着用代码实现吧。

```java
package Chap8;

public class LLRB<Key, Value> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;
    private Node root;

    private class Node {
        private Key key;
        private Value value;
        private Node left, right;
        private int N; // 结点计数器，以该结点为根的子树结点总数
        private boolean color; // 指向该结点的链接颜色

        public Node(Key key, Value value, int N, boolean color) {
            this.key = key;
            this.value = value;
            this.N = N;
            this.color = color;
        }
    }

    public boolean isRed(Node x) {
        // 约定空链接为黑色
        if (x == null) {
            return BLACK;
        } else {
            return x.color == RED;
        }
    }
}
```

先给出了左倾红黑树的基本实现，在标准二叉查找树中新增了`color`域，表示指向该结点的链接颜色。对应的`isRed(Node x)`判断指向该结点的链接是不是红色的，如果x == null表示这是条空链接，出于之前的约定，应该返回黑色。

### 旋转

为了保证红黑树的特性——不存在右链接是红色的、以及没有任何一个结点同时和两条红链接相连，在对红黑树进行操作时，比如插入或者删除，难免会出现红色右链接或者连续的两条红链接，应该确保每次操作完成之前这些情况已经被修正。这种对链接颜色的修正靠的是一种称为**旋转**的操作完成的，和上述平衡树中的旋转操作基本类似，不过这里加入了对链接颜色信息的修正。

旋转操作会改变红链接的指向，比如一条红色的右链接需要转换为红色的左链接，这个操作被称为**左旋转**，右旋转和左旋转是对称的。如下图所示。

![](http://obvjfxxhr.bkt.clouddn.com/rbt_5.PNG)



![](http://obvjfxxhr.bkt.clouddn.com/rbt_6.PNG)

上面两张图，从红色右链接变到红色左链接，是**左旋转**。

![](http://obvjfxxhr.bkt.clouddn.com/rbt_7.PNG)

![](http://obvjfxxhr.bkt.clouddn.com/rbt_8.PNG)



上面两张图，从红色左链接变到红色右链接，是**右旋转**。

旋转操作也是局部的，只会影响旋转相关的结点，树中其他结点不受影响，而且旋转操作不会破坏整棵树的有序性和平衡性，如图中小于a、位于a和b之间、大于b这些大小关系在旋转前后没有改变！

由图可写出旋转操作的实现

```java
private Node rotateLeft(Node h) {
    Node x = h.right; // 根结点的右子结点保存为x
    // 其实就是h和x互换位置
    h.right = x.left; // 根结点的右子结点的左孩子挂到根结点的右子结点上
    x.left = h; // 根结点挂到根结点右子结点的左子结点上
    x.color = h.color; // 原来h是什么颜色，换过去的x也应该是什么颜色
    h.color = RED;     // 将红色右链接变成红色左链接，因此x是红色的，h和x互换位置所以换过去的h也应该是RED
    x.N = h.N;  // x的结点数和h保持一致
    h.N = size(h.left) + size(h.right) + 1; // 这里不能用原x.N赋值给h.N，因为旋转操作后原来x的子树和现在h的子树不一样
    // 返回取代h位置的结点x，h = rotateLeft(Node h)就表示x取代了h
    return x;
}

private Node retateRight(Node h) {
    Node x = h.left;
    h.left = x.right;
    x.right = h;
    x.color = h.color;
    h.color = RED; // x原来是红色的
    x.N = h.N;
    h.N = size(h.left) + size(h.right) + 1;

    return x;
}
```

### 查找和插入

查找操作直接使用标准二叉查找树的get方法，改都不用改的。

```java
// 非递归get
public Value get(Key key) {
  	Node cur = root;
  	while (cur != null) {
    	int cmp = key.compareTo(cur.key);
    	if (cmp < 0) {
      	cur = cur.left;
    	} else if (cmp > 0) {
      	cur = cur.right;
    	} else {
      	return cur.value;
    	}
  	}
  return null;
}
```

插入就稍微麻烦一点了。由于红黑树也是2-3树，所以插入情况请参考上述对2-3树插入的探讨。

#### 向2-结点中插入新键

这是最简单的情况了，按照2-3树插入的思路，直接使这个2-3结点变成3-结点。对应到红黑树中，如果新键小于父结点，只需将该键挂到父结点的左边且链接是红色；如果新键大于父结点，只需将该键挂到老键的右边且链接是红色，但这就违反了红黑树的特性（右链接不能是红色），因此上面的旋转操作就派上用场了，只需对其进行左旋转即可。

![](http://obvjfxxhr.bkt.clouddn.com/rbt_9.PNG)

#### 向3-结点中插入一个新键

如果树只由一个3-结点构成。插入有三种情况，分别是新键最大插入到结点右边、新键最小插入到结点的左边、新键位于两者之间插入到中间。

回忆2-3树中往3-结点中插入的情况，我们的做法是先将新键存在一个临时的4-结点中，然后将排名中间的键往上移，4-结点分解成了3个2-结点，同时树高增加1。这在红黑树中很好实现，4-结点也就是一个结点拥有两条红色链接，至于排名中间的键上移，只需将链接的颜色反转即可。如下是结点链接反色的示意图

![](http://obvjfxxhr.bkt.clouddn.com/rbt_11.PNG)

左图是一个4-结点，**通过将h的两个子结点的颜色变成BLACK、将h变成RED就达到了上移的目的，而且4-结点正确地被分解成了三个2-结点，h变红正好可以和上一层的2-结点合并成3-结点；或者和3-结点合并成4-结点后继续执行分解操作，如此这般一直到遇到一个2-结点为止。**这完全符合2-3树中的插入操作！反转结点链接颜色的代码非常简单，但是又相当重要，我们将看到，向3-结点中插入的种种情况最终都会转换成上面的情况。

```java
private void flipColors(Node h) {
    h.color = !h.color;
    h.left.color = !h.left.color;
    h.right.color = !h.right.color;
}
```

向一棵只有3-结点的树中插入新键分以下三种情况：

- 新键大于3-结点中的两个键，那么直接连接到3-结点较大键的右链接且颜色为红色。此时直接调用`flipColors`方法即可；
- 新键小于3-结点中的两个键，那么该键会连接到3-结点较小键的的左链接且颜色为红色，此时出现了连续两条的红链接，是不允许的，通过**右旋转**变成了情况1，再调用`flipColors`
- 新键位于3-结点的两个键之间，那么该键会链接到3-结点较小键的右链接上且颜色为红色，此时出现红色右链接，是不允许的，通过**左旋转**修正后变成了情况2，于是右旋转，变成情况1，最后调用`flipColors`.

![](http://obvjfxxhr.bkt.clouddn.com/rbt_10.PNG)

如果是在树底部的某个3-结点插入新键，有可能包含以上全部三种情况！

![](http://obvjfxxhr.bkt.clouddn.com/rbt_14.PNG)

如果你回头看各种情况的插入操作，我们**总是用红链接将新结点和它的父结点相连**。这么做是为了符合2-3树中各种插入情况。而且因为三种情况里有些情况会进行其他情况的处理，在实现时一定要注意处理的顺序。比如情况3里包含了情况2和情况1的处理，情况2中包含了情况1的处理，那么在处理时应该先判断情况3，再判断情况2，最后判断情况1。

总结一下：

- 如果右子结点是红色的而左子结点是黑色的，进行左旋转，目的是将红色右链接变成红色左链接。
- 如果右子结点是红色的而左子结点是黑色的，进行左旋转。
- 如果左右子结点均为红色，进行颜色反转。

上面的表述按顺序翻译成代码就可以实现put方法了。

它们互相转换的关系如下图所示

![](http://obvjfxxhr.bkt.clouddn.com/rbt_15.PNG)

对了还有一点，颜色反转有可能导致根结点的颜色也变成红色，但是我们**约定根结点总是黑色的**。所以每次put操作后，记得手动将`root.color`置为黑色。

```java
public void put(Key key, Value value) {
  	root = put(root, key,value);
  	// 保证根结点始终为黑色
  	root.color = BLACK;
}

private Node put(Node h, Key key, Value value) {
  	if (h == null) {
    	return new Node(key, value, 1, RED);
  	}
  	int cmp = key.compareTo(h.key);
  	if (cmp < 0) {
    	h.left = put(h.left, key, value);
  	} else if (cmp > 0){
    	h.right = put(h.right, key, value);
  	} else {
    	h.value = value;
  	}

  	/*
     下面连续三个判断是和标准二叉查找树put方法不同的地方，目的是修正红链接
     */
  // 如果右子结点是红色的而左子结点是黑色的，进行左旋转
  // 之后返回值赋给h是让x取代原h的位置，不可少
  	if (isRed(h.right) && !isRed(h.left)) {
    	h = rotateLeft(h);
  	}
  	// 如果右子结点是红色的而左子结点是黑色的，进行左旋转
  	if (isRed(h.left) && isRed(h.left.left)) {
    	h = rotateRight(h);
  	}
  	// 如果左右子结点均为红色，进行颜色反转
  	if (isRed(h.left) && isRed(h.right)) {
    	flipColors(h);
  	}

  	h.N = size(h.left) + size(h.right) + 1;
  	return h;
}
```

### 删除

红黑树的删除和上面提到的2-3树的删除是一致的。对照着上述2-3树删除的各种情况来实现红黑树的删除，理解起来就不那么复杂了。

还是先从简单的入手。

#### 删除最小键

如果要删除的是一个3-结点，那么直接删除。如果要删除的是一个2-结点，说明`h.left == BLACK && h.left.left ==BLACK`，逆向思考我们保证`h.left`或`h.left.left`任意一个是RED就说明要删除的结点是一个3-结点，之后再删除就简单了。

![](http://obvjfxxhr.bkt.clouddn.com/rbt_a.PNG)

如图当前结点B，`B.left = BLACK && B.left.left = BLACK`，此时只需flipColor将ABC合并成4-结点即可执行删除。

![](http://obvjfxxhr.bkt.clouddn.com/rbt_c.PNG)

反转颜色后使得`h.left = RED`

还有种更难的情况，在满足上述两个结点链接都是黑色的情况下，如果`h.right.left = RED`呢？如下，当前结点h = E

![](http://obvjfxxhr.bkt.clouddn.com/rbt_f.PNG)

按照2-3树删除方法，应该从A的兄弟结点借一个最小键到当前结点，再将当前结点中最小键移到A中合并成一个3-结点，再执行删除。

![](http://obvjfxxhr.bkt.clouddn.com/rbt_d.PNG)

经过一系列的变换，从图中可看出先是`rotateRight(h.right)`，再`rotateLeft(h)`，然后`filpColors(h)`最终使得`h.left.left = RED`。

其他情况如当前结点为D，`D.left.left = RED`，BC中可以直接删除B。或者如果递归到了C是当前结点，`C.left = RED`也能直接删除而无需其他操作。

![](http://obvjfxxhr.bkt.clouddn.com/rbt_b.PNG)

在递归自顶而下的过程中，我们对若干结点都进行了颜色反转及旋转操作，这些操作都可能影响数的有序性和平衡性，所以在返回的自下而上的过程中，要对树进行修正，修正的方法和put方法中的修正方法完全一样，抽取出来作为一个方法，如下

```java
private Node fixUp(Node h) {
  	if (isRed(h.right) && !isRed(h.left)) {
    	h = rotateLeft(h);
  	}
  	// 如果右子结点是红色的而左子结点是黑色的，进行左旋转
  	if (isRed(h.left) && isRed(h.left.left)) {
    	h = rotateRight(h);
  	}
  	// 如果左右子结点均为红色，进行颜色反转
  	if (isRed(h.left) && isRed(h.right)) {
    	flipColors(h);
  	}

  	h.N = size(h.left) + size(h.right) + 1;
  	return h;
}
```

有了上面讲解的基础，实现deleteMin就不难了。

```java
private Node moveRedLeft(Node h) {
  	// 当此方法被调用时，h是红色的，h.left和h.left.left都是黑色的
  	// 整个方法结束后h.left或者h.left.left其中之一被变成RED
  	flipColors(h);
  	if (isRed(h.right.left)) {
    	h.right = rotateRight(h.right);
    	h = rotateLeft(h);
      	flipColors(h);
  	}
  	return h;
}

public void deleteMin(Key key) {
  	// 这里将root设置为红色是为了和moveRedLeft里的处理一致
  	// 即当前结点h是红色的，其两个子结点都是黑色的，在反色后，当前结点h变成黑色，而它的两个子结点变成红色
  	if (!isRed(root.left) && !isRed(root.right)) {
    	root.color = RED;
  	}
  	root = deleteMin(root, key);
  	// 根结点只要不为空，删除操作后保持始终是黑色的
  	if (!isEmpty()) {
    	root.color = BLACK;
  	}
}

private Node deleteMin(Node h, Key key) {
  	if (h.left == null) {
    // 不像标准二叉查找树那样返回h.right, 因为put方法就决定了h.left和h.right要么同时为空要么同时不为空
    	return null;
  	}
  	// 合并成4-结点或者从兄弟结点中借一个过来
  	if (!isRed(h.left) && !isRed(h.left.left)) {
    	h = moveRedLeft(h);
  	}

  	h.left = deleteMin(h.left, key);
  	// 返回时，自下而上地修正路径上的结点
  	return fixUp(h);
}
```

看个删除最小键的例子。

![](http://obvjfxxhr.bkt.clouddn.com/rbtdelete_g.PNG)

#### 删除最大键

删除最大键和删除最小键是对称的，但有些不一样。删除最小键在自顶向下的过程中保证`h.left`或者`h.left.left`为红色，类似地**删除最大键在自顶向下的过程中要保证`h.right`或者`h.right.right`为红色，但是我们定义红黑树是左倾的！这意味着红色链接默认就是左链接，因此要使用删除最小键的方法来达到删除最大键的目的，必须在处理之前将红色链接变成右链接（右旋转操作），之后就和删除最小键的处理对称了。**

当前结点`h.right = BLACK && h.right.left = BLACK`，反转颜色。

![](http://obvjfxxhr.bkt.clouddn.com/rbtdelete_j.PNG)

满足上述情况的同时如果`h.left.left = RED`，说明需要从兄弟结点中借一个键过来，为此还要进行下面的变换，最后`h.right.right`变成红色。

![](http://obvjfxxhr.bkt.clouddn.com/rbtdelete_k.PNG)

实现如下

```java
private Node moveRedRight(Node h) {
  	flipColors(h);
  	// 从兄弟结点借一个键
  	if (isRed(h.left.left)) {
    	h =rotateRight(h);
    	flipColors(h);
  	}
  	return h;
}

public void deleteMax(Key key) {
  	if (!isRed(root.left) && !isRed(root.right)) {
    	root.color = RED;
  	}
  	root = deleteMax(root, key);
  	if (!isEmpty()) {
    	root.color = BLACK;
  	}
}

private Node deleteMax(Node h, Key key) {
  	// 为了和deleteMin对称处理，先将红色左链接转换成红色右链接
  	// 转换为红色右链接是最先处理的！
  	if (isRed(h.left)) {
    	h = rotateRight(h);
  	}
  	// 这个判断不能再上句之前，因为可能旋转前h.right是null，旋转后可就不是null了
  	if (h.right == null) {
    	return null;
  	}
  	// 这里条件中不是h.right.right，因为3-结点是左链接表示的
  	if (!isRed(h.right) && !isRed(h.right.left)) {
    	h = moveRedRight(h);
  	}
  	h.right = deleteMax(h.right, key);
  	return fixUp(h);
}
```

来看两个删除最大键的例子，其中第一个例子删除后就已经平衡，无需修正；第二个例子中在自下而上的过程中有修正。

![](http://obvjfxxhr.bkt.clouddn.com/rbtdelete_h.PNG)

上面的例子无修正。

![](http://obvjfxxhr.bkt.clouddn.com/rbtdelete_i.PNG)

上面的例子有修正。

#### 删除任意键

最难的方法。我自己也没太明白就来介绍这个方法可能不太妥当，只好尽力说个大概。至于代码中的控制流程（if-else的顺序）为什么是那样，本人也不理解。

```java
public void delete(Key key) {
  	if (!contains(key)) {
    	return;
  	}

  	if (!isRed(root.left) && !isRed(root.right)) {
    	root.color = RED;
  	}

  	root = delete(root, key);

  	if (!isEmpty()) {
    	root.color = BLACK;
  	}
}

private Node delete(Node h, Key key) {
  	if (key.compareTo(h.key) < 0) {
    	if (!isRed(h.left) && !isRed(h.left.left)) {
      	h = moveRedLeft(h);
    	}
    	h.left = delete(h.left, key);
  	} else { // 要么在根结点或者右子树，两种情况包含在一起了
    // 要在右子树处理，所以确保是红色右链接
    	if (isRed(h.left)) {
      	h = rotateRight(h);
    	}
      
    	// 要删除的结点在树底
    	if (key.compareTo(h.key) == 0 && (h.right == null)) {
      	return null;
    	}
    	// 这个判断必须在上个判断之后，因为确保h.right不为空后才能调用h.right.left
    	if (!isRed(h.right) && !isRed(h.right.left)) {
      	h = moveRedRight(h);
    	}
    	// 要删除的键不在树底, 用它的后继结点替代它后，删除后继结点
        if (key.compareTo(h.key) == 0) {
        	Node x = min(h.right);
         	h.key = x.key;
          	h.value = x.value;
          	h.right = deleteMin(h.right);
      	// 没有相等的键，在右子树中递归
    	} else {
      	h.right = delete(h.right, key);
    	}
  	}
  	// 自下而上的结点修正
  	return fixUp(h);
}
```

公有`delete`方法中还是延续了deleteMin/deleteMax那一套，只是增加了判断——如果key不在红黑树中，不进行任何操作直接返回。现在看私有方法：

大概的思路是：从root开始查找，如果被删除的键比根结点小，递归地在左子树中查找；否则，被删除的键和根结点相同或者比根结点大，这个条件分支是最难的地方。**进入else分支后，不管是不是和当前结点的键相同，首先就把红色左链接转换成红色右链接，这之后才判断当前结点的键是否和被删除结点的键相同。 **被删除的结点位置有两种情况，在树底和不在树底，不在树底时需要用它的后继结点替代更新被删除结点，之后再删除后继结点。两种情况下键都不相同的话，就递归地在右子树中查找。最后记得要自下而上地修正路径上各个结点，保证删除之后树的有序性和平衡性。

看一个被删除的键不在树底的例子，如下图删除D。用D的后继结点E替代了D的位置，之后删除了E，最后修正结点颜色。

![](http://obvjfxxhr.bkt.clouddn.com/rbtdelete_aaa.PNG)

代码中把“被删除键和当前键相同”、“比当前键大”这两种情况合并在一起讨论了，我尝试按照通常的思路，将这两种情况分开，即`else if (key.compareTo(h.key) == 0)`和`else > 0`；或者将`if (!isRed(h.right) && !isRed(h.right.left))`这个判断放到最后一个else里面，结果在进行了几次结点删除后都会出错。

按照上面的控制流程，执行删除就不会出错，不过如果你稍微改变下if-else语句的顺序，在若干次删除操作后就可能出现错误——多半是树的平衡性被破坏了。

### 其他API

像min()/max()、select、rank、floor、ceiling和范围查找等相关方法，**不作任何修改**，直接套用标准二叉查找树的对应方法即可。

---

by @sunhaiyu

2017.10.21

