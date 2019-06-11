# 数据结构与算法--KMP算法查找子字符串

>  部分内容和图片来自这三篇文章: [这篇文章](http://www.cnblogs.com/yjiyjige/p/3263858.html)、[这篇文章](http://m.blog.csdn.net/v_july_v/article/details/7041827)、[还有这篇](http://www.cnblogs.com/tangzhengyue/p/4315393.html)他们写得非常棒。结合他们的解释和自己的理解，完成了本文。

上一节介绍了暴力法查找子字符串，同时也发现了该算法效率并不高。**当失配位置之前已经有若干字符匹配时，暴力法很多步骤是多余的**。举个KMP算法的例子，看图1

![](http://upload-images.jianshu.io/upload_images/2726327-4e36adcbc8453ecd.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

可以看到子串p和主串t在红框处失配了，失配之前的字符串ABC已经匹配。ABC第一个字符A和后面的字符都不同，所以可以放心地直接将子串p的p[0]对齐失配处`i`，让p[0]和t[i]接着比较。如图2

![](http://upload-images.jianshu.io/upload_images/2726327-1f8aa0e61df603d3.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

这是子串p第一个字符和其后的各个字符都不同的情况，如果其后存在相同的字符呢，比如下面图3

![](http://upload-images.jianshu.io/upload_images/2726327-f41e969bab7f6dde.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

失配处之前的字符串已经匹配且为ABA存在相同相同的字符A。这下我们不敢轻易将p[0]移动到和t[i]对齐比较了。因为有个相同的A，所以应该尝试着先移动到那个地方，万一就能匹配上了呢。图4

![](http://upload-images.jianshu.io/upload_images/2726327-0636d3cfec494ea2.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

不巧，匹配再次失败了，只是这种情况下失败了而已。试想如果主串t是ABABADHIJK，子串p还是ABAD，按照上面的步骤，刚好就匹配成功！所以现在知道为什么不要一下就移动到将p[0]与t[i]对齐了吧，因为有可能，所以得尝试。**上一步是必须的，不是多余步骤**。接着看类似的例子，图5

![](http://upload-images.jianshu.io/upload_images/2726327-d784111b498f9071.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

失配处之前的字符串ABCAB已经匹配，且存在相同的字符串AB。按照上面的思路，应该移动到下图的位置。如图6

![](http://upload-images.jianshu.io/upload_images/2726327-ec3a077d8b7fa52b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

这样想来，我们可能认为只要子串中有重复字符，就应该像上面那样移动。再看这个例子，没图了抱歉，凑合着看吧。

```
ABCADEFGHI
ABCADZ
```

在t串的E和p串的Z处失配，之前的字符串ABCAD已经匹配，且存在相同的字符A，试着移动将p[0]移动到第二个A处，如下

```
ABCADEFGHI
   ABCADZ
```

可以看到，在子串中虽然存在相同字符A，但是第一个A之后的B即p[1]和第二个A之后的D即p[4]不同，这个信息我们事先就可以知道。所以即使将A对齐了，p[1]和t[4]比较，`t[4] == p[4]`也就是p[1]和p[4]比较，肯定不匹配的。这步是多余的！我们可以直接移动到让p[0]和t[i]对齐。如下

```
ABCADEFGHI
     ABCADZ
```

所以，**存在相同字符并不能作为子串移动位置的判断条件。实际上，确定子串移动位置的是字符串相同前缀、后缀的最大长度。**

## 字符串相同前缀、后缀的最大长度以及next数组

什么叫字符串的前缀，后缀呢？

- 前缀：除开末尾字符，所有**包含首字符**的字符串集合；
- 后缀：除开首字符外，所有**包含末尾字符**的字符串集合

举个简单的例子，如字符串ABCA，它的前缀有`A, AB, ABC`，它的后缀有`BCA, CA, A`，前后缀比较，只有一对相同字符串，且长度为1，所以字符串ABCA相同前缀、后缀的最大长度为1。再看字符串ABCAB，它的前缀有`A, AB, ABC, ABCA`，它的后缀有`BCAB, CAB, AB, B`，只有一对相同的字符串AB，且长度为2，故最大长度为2。

既然确定模式字符串移动位置的是字符串相同前缀、后缀的最大长度，这里说的字符串具体来说指的是**失配位置之前的字符串**。即失配位置之前的字符串的相同前缀、后缀的最大长度k，决定了模式串p[k]应该和失配处t[i]对齐。由于**在模式字符串的每个位置都可能失配**，所以需要求出模式字符串**失配位置之前的字符串**的相同前后缀的最大长度，用一个数组存储起来，这个数组称为**next数组**。

如模式字符串ABCDABX，如果在X处失配，求出X之前的子字符串ABCDAB的相同前后缀最大长度为2，如表格中最后一行。又X处索引为6，所以`next[6] = 2`。如果在D处失配则求出D之前字符串ABC的相同前后缀的最大长度，为0，见表中第3行数据。又D的索引为3，则`next[3] = 0`。再看在第二个字符处失配，B之前有一个子字符串A，只有一个字符谈不上前后缀，所以相同前后缀的最大长度为0，见表中第1行数据。

那如果在第一个字符A处就失配了呢？**由于第一个字符A之前没有子字符串了，按照约定，我们令其相同前后缀的最大长度为-1。所以`next[0] = -1`**。下面next数组的代码实现中会具体说明这个约定的值next[0]的值为什么不是-8， -9或者是0。

![](http://upload-images.jianshu.io/upload_images/2726327-828a9fba6b551924.PNG?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

按照上面的思路，给出一个字符串就可以写出它的next数组了。还是上面的`ABCDABX`

| 失配字符：索引 | A ：0 | B ：1 | C ：2 | D ：3 | A ：4 | B ：5 | X : 6 |
| ------- | ---- | ---- | ---- | ---- | ---- | ---- | ----- |
| next数组值 | -1   | 0    | 0    | 0    | 0    | 1    | 2     |

这个表格next数组的值和上面的“最大公共元素长度”相比，其实就是先令`next[0] = -1`，再将这些最大公共元素长度的值填入`next[1]~next[size - 1]`即可。由此得到**next数组为[-1, 0, 0, 0, 1, 2]**。

好，求出了next数组就好办了。**当某一个字符在位置`j`处失配时，next[j]就指示了模式字符串应该移动到哪个位置。**根据next[j]移动到哪儿呢？具体来说就是让next[j]成为新的`j`，让模式字符串移动，直到p[j]与失配处t[i]对齐，然后让p[j]再和t[i]比较一次。

为了验证这一说法，再次看图1

![](http://upload-images.jianshu.io/upload_images/2726327-4e36adcbc8453ecd.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

模式字符串ABCE的next数组为[-1, 0, 0, 0]，在`j = 3`处失配，`next[3] = 0`，所以让p[0]和t[i]对齐比较。

![](http://upload-images.jianshu.io/upload_images/2726327-1f8aa0e61df603d3.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

同样的再看图5

![](http://upload-images.jianshu.io/upload_images/2726327-d784111b498f9071.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

模式字符串的next数组为[-1, 0, 0, 0, 1, 2]，在`j = 5`处失配，`next[5] = 2`，所以让p[2]和t[i]对齐比较。

![](http://upload-images.jianshu.io/upload_images/2726327-ec3a077d8b7fa52b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

第二遍看这些图，是不是清晰多了！

## next数组的代码实现

关键是如何通过代码来求模式字符串的next数组，像上面那样列出字符串的所有前后缀，然后比较出相同前后缀的最大长度吗？当然不是，那不是最好的方法。**求next数组其实可以看成：字符串自己和自己匹配的过程。**

```java
private static int[] getNext(String p) {
    int M = p.length();
    int[] next = new int[M];
    next[0] = -1;
    int j = 0;
    int k = -1;
    while (j < M - 1) {
    	if (k == -1 || p.charAt(k) == p.charAt(j)) {
      	next[++j] = ++k;
    	} else {
      	k = next[k];
    	}
  	}
  	return next;
}
```

拿字符串ABCA作为例子，next数组是[-1, 0, 0, 1]。

首先next数组的长度应该和模式字符串的长度一样。所以有`int[] next = new int[M];`然后next[0]无脑设置成-1。为什么是-1呢？**其实可以发现next[1]也是个定值，为0。这是因为索引1之前只有一个字符，它没有前缀后缀之说。**`if`分支里的条件必须是`k == -1`，这样当第一次进入if分支时，才能保证`next[0+1] = -1 + 1`，即`next[1] = 0`。接下来该填next[2]了，理论上为0。自增后`k = 0，j = 1`, 比较p[0]与p[1]，不相同，转向else分支，next[0]赋值给`k`，因为要给next[2]填入值，所以必须要进入if分支，要么只有`next[0] = -1`赋值给`k`后，才能保证一定能进入if分支。从而`next[1 + 1] = -1 + 1`即`next[2] = 0`。这样就解释了为什么以上的代码实现中，next[0]为什么要设置成-1。

其他的，`while`里之所以是`j < M - 1`而不是`j < M`，是因为下面这句`next[++j] = ++k;`是先自增后存入的，这意味着最后能存到`next[M - 1]`，刚刚存满数组。如果条件是`j < M`则会越界。另外`p.charAt(k)`表示的是前缀的单个字符， `p.charAt(j)`表示的是后缀的单个字符。

上面代码实现中，并不是列出了所有的前后缀再一一比较的。那么这种实现一定正确吗？我们来看。

![](http://upload-images.jianshu.io/upload_images/2726327-9d48e22dd275a9a7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

`next[j] == k`应该很好理解，k代表的就是`j`位置前字符串相同前后缀的最大长度，在这里是2。现在比较p[k]和p[j]，相同，所以最大长度应该变成3，进入if分支，`next[j + 1] = next[j] + 1 = k + 1`。

![](http://upload-images.jianshu.io/upload_images/2726327-d68eaa7f2b49b390.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

如果p[k]和p[j]不相同呢？

![](http://upload-images.jianshu.io/upload_images/2726327-ed3cc2851966a9d3.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

这时next[j + 1]怎么填写呢？理论上来说，列出所有前后缀后，一一比较可以得知有相同前后缀为AB，最大长度为2。转入else分支， `k = next[k]`，这是什么意思呢？看起来**是一种递归**，如果递归直到`k == -1`，则说明找不到相同的前后缀，`next[j + 1] = 0`。看下图，这实际上是**模式字符串自己的前缀在和自己的后缀作匹配**。ABAC可看作模式字符串，其next数组为[-1, 0, 0, 1]，它和DABABC在索引`k`处失配，回忆文章开头，当失配位置之前已经有一些字符匹配，应该怎么移动模式字符串呢？失配处k，`k = next[k] = 1`，所以让`ABAC`的第1个位置和`j`位置对齐。哈！对齐后刚好B和位置`j`的B相同（如右图），接着进入if分支，则`next[j + 1] = k + 1 = 2`，与理论值吻合。现在再看`k = next[k]`，是不是一目了然？！

![](http://upload-images.jianshu.io/upload_images/2726327-da26f909e4aee867.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

如果模式字符串自己和自己匹配这个事搞不懂，没关系，我们换个角度看问题。看下图

![](http://upload-images.jianshu.io/upload_images/2726327-f85621c016973692.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

已知：

- next[j] = k
- next[k] = 绿色色块所在的索引
- next[绿色色块所在的索引] = 黄色色块所在的索引

1. 由next[j] = k,可知字符串`A1 == A2`

2. 由next[k] = 绿色色块所在的索引，可知`B1 == B2`，又`A1 == A2`，所以A1的后缀B2与A2的后缀B3相同。所以**`B1 == B2 == B3`**

3. 由next[绿色色块所在的索引] == 黄色色块所在的索引，可以得到`C1 == C2`。又`B1 == B2 == B3`，可知它们的后缀`C2 == C3 ==C4`，综上有`C1 == C2 == C3 == C4`。

现在假如`p[k] != p[j]`，则**最大长度的前后缀A1和A2分别添加了一个字符后的新字符肯定不是相同的前后缀了，我们退而看看原先第二长的相同前后缀，B1和B3，它们分别加上后一个字符后是否会相同呢，如果相同，则B1与B3加上后一个字符后的新字符就成为了最大长度的相同前后缀；如果不同，再选原先第三长的子串C1和C4，递归**查找，直到最后`k == -1`。如何比较B1和B3后一位字符呢？`k = next[k]`就是令新的k值为绿色色块（也是串B1后一位字符）所在的索引，**此时再让p[k]和p[j]位置对齐（B1和B3重合）比较的就自然是B1与B3的后一位字符了**。

## KMP算法的实现

好了，next数组怎么求讲了，主串和子串匹配时如何根据求得的next数组来移动模式字符串也讲过了。是时候上KMP的实现代码了！

```java
public static int search(String p, String t) {
    // 根据模式字符串获得next数组
    int[] next = getNext(p);
    int N = t.length();
    int M = p.length();
    int i = 0;
    int j = 0;
    while (i < N && j < M) {
    	if (j == -1 || p.charAt(j) == t.charAt(i)) {
      		i++;
      		j++;
    	} else {
      		j = next[j];
    	}
  	}
  	if (j == M) {
    	return i - j;
  	} else {
    	return -1;
  	}
}
```

先是获得模式字符串的next数组，然后`i, j`分别是指针主串和子串的指针，当然一开始指向0。如果字符相同，则执行if分支，直到遇到失配字符，转入else分支，next数组指示了子串的哪个位置和失配位置t[i]对齐再次比较。有种情况比较特殊：**如果子串在第一个字符(j = 0)处就失配了，那么先转else让`j = next[0] = -1`，紧接着进入if分支，主串指针`i`向右移动一位，子串指针`j`回到位置0，这和暴力法是一个做法。**`i - j`的含义是子串开头在主串中的索引，我们要返回的的正是这个值。从整个代码来看，可以发现**i从未回退过**，这正是KMP算法的优点之一。

## KMP算法的优化

以上KMP算法的实现已经比之前的暴力法好多了，但它也存在多余比较的情况，看下图。

![](http://upload-images.jianshu.io/upload_images/2726327-1f79e95ac56d90be.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

按照上面KMP算法的思想，`B != C`在`j`处失配。由于`j = next[j] = 1`，应该让p[1]和t[i]对齐继续比较。如下

![](http://upload-images.jianshu.io/upload_images/2726327-4ab579f9562616b9.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

可是我们发现，**子串移动之后还是B和C比较，我们刚才失配时就得知B不匹配了，这次的B当然还是不匹配，这步就是多余的。**如果聪明些，我们应该使用next[1]的值而不是next[3]，从而可以直接将子串移动到p[0]与t[i]对齐。也就是说令`next[3] = next[1]`。

**这种情况发生在当`p[j] = p[next[j]] `**，而`next[j] = k`，条件简化为`p[j] = p[k]`。首先会让`p[k]`与`t[i]`对齐，然而这步是不必要的，所以还不如跳过这步，让`p[next[k]`与`t[i]`对齐，即用`next[k]`的值取代`next[j]`的值。说取代还是太麻烦了，为何不一开始就改变next数组，只要遇到某个字符满足`p[j] = p[k]`，next[j]的值就直接使用next[k]的值好了。

由此看来，next数组的求法就得改变了。

看字符串AA，按照原来next数组的求法肯定是[-1, 0]，因为前两位是定值。

我们来检验`p[j] = p[k]`这个条件。next[0]还是-1这个改不了。在`j = 1`处，`k = next[1] = 0`，`p[1] == p[0]`条件满足！所以应该用next[0]的值取代next[1]。此时next数组变成[-1, -1]

再看上面ABAB，按照原来的next数组求法是[-1, 0, 0, 1]，`k = next[1] = 0`，`p[1] != p[0]`条件不满足，next数组值还是0不改变。`next[2] = 0`，`p[2] == p[0]`条件满足。所以用next[0]的值取代next[2]；`k = next[3] = 1`，`p[3] == p[1]`条件满足，应该用next[1]的值取代next[3]。综上，此时next数组百变成[-1, 0, -1, 0]

好，现在知道怎么求优化后的next数组了。那么用代码怎么实现呢？其实改动的地方就一处。先上代码，再解释。

```java
private static int[] betterGetNext(String p) {
    int M = p.length();
    int[] next = new int[M];
    next[0] = -1;
    int j = 0;
    int k = -1;
    while (j < M - 1) {
      	if (k == -1 || p.charAt(k) == p.charAt(j)) {
        	if (p.charAt(k + 1) == p.charAt(j + 1)) {
          	next[++j] = next[++k];
        	} else {
          	next[++j] = ++k;
        	}
      	} else {
        	k = next[k];
      	}
    }
    return next;
}
```

当`k == -1`或者当前字符相同时多了一句判断`if (p.charAt(k + 1) == p.charAt(j + 1))`，它紧接着预判下一个字符是否也相等，如果相等，则满足条件`p[j] = p[k]`，想想为什么？

1. 当前两个比较的字符相同的情况下，下一个字符也相同。下图当前比较的两个字符`p[k - 1] == p[j - 1]`，预判下一个字符`p[k] == p[j]`，且`next[j] = k`，满足条件，所以next[j]应该直接使用next[k]的值。

![](http://upload-images.jianshu.io/upload_images/2726327-9d48e22dd275a9a7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

2. `k == -1`时，再举个例子ABAB字符串，假设当前字符B(第一个B)在`j - 1`处，因为k==-1，所以预判下一个字符`p[0] == p[j]`，且`next[j] = k = 0`，满足条件，所以next[j]直接使用next[0]的值即-1。

以上两种情况都满足`next[j] = k`，`p[k] = p[j]`。现在应该清楚增加的那句if判断是怎么工作的了吧。

试试用新的next数组实现测试下。

```java
package Chap5;


public class KMPSearch {
    private static int[] getNext(String p) {
        int M = p.length();
        int[] next = new int[M];
        next[0] = -1;
        int j = 0;
        int k = -1;
        while (j < M - 1) {
            if (k == -1 || p.charAt(k) == p.charAt(j)) {
                next[++j] = ++k;
            } else {
                k = next[k];
            }
        }
        return next;
    }

    private static int[] betterGetNext(String p) {
        int M = p.length();
        int[] next = new int[M];
        next[0] = -1;
        int j = 0;
        int k = -1;
        while (j < M - 1) {
            if (k == -1 || p.charAt(k) == p.charAt(j)) {
                if (p.charAt(k + 1) == p.charAt(j + 1)) {
                    next[++j] = next[++k];
                } else {
                    next[++j] = ++k;
                }
            } else {
                k = next[k];
            }
        }
        return next;
    }

    public static int search(String p, String t) {
        // 根据模式字符串获得next数组
        int[] next = betterGetNext(p);
        int N = t.length();
        int M = p.length();
        int i = 0;
        int j = 0;
        while (i < N && j < M) {
            if (j == -1 || p.charAt(j) == t.charAt(i)) {
                i++;
                j++;
            } else {
                j = next[j];
            }
        }
        if (j == M) {
            return i - j;
        } else {
            return -1;
        }
    }


    public static void main(String[] args) {
        int index = search("abab", "abacghababzz");
        System.out.println(index);
    }
}
```

输出6，没毛病。

---

by @sunhaiyu

2017.8.4
