# 数据结构与算法--Boyer-Moore和Rabin-Karp子字符串查找

## Boyer-Moore字符串查找算法

**注意，《算法4》上将这个版本的实现称为Broyer-Moore算法，我看了下没有关于“好后缀”的介绍，推测应该说的是Boyer-Moore-Horsepool算法，即Boyer-Moore算法的简化版本。**

暴力法和KMP算法，都是**从左到右比较字符串的各个字符**。换种思路，如果**从右往左比较字符呢？**这就是将要学习的Boyer-Moore算法。和KMP算法一样，需要一个额外的空间来记录失配（匹配失败）时模式字符串应该移动的位置。具体来说是这样的：

使用一个字母表`right[]`，其大小为字母表的大小。字符的查找通过ASCII码表的数字对应（如right[97]指代a这个字符）。**对于模式字符串中的每个字符，记录它最后一次出现的下标**，这句话的意思是说：如果某个字符只出现了一次，那么记录为0；如果出现了多次，记录最靠近右边的哪个字符的索引下标。对于未出现在模式字符串中的字符，约定记录为-1，下面将看到设置为-1的好处。

比如要在文本`FINDINAHAYSTACKNEEDLE`中查找`NEEDLE`，先按照上面的方法为NEEDLE建立数组right[]。结果如下

![](http://obvjfxxhr.bkt.clouddn.com/bm_456.PNG)

该表的建立只需要用到模式字符串，所以在文本中查询时可以直接根据该表查找到在匹配失败时模式字符串应该移动的位置。

算法开始先将模式字符串和文本字符串的第一个字符对齐，然后从模式字符串的最后一个字符开始从右往左比较。用一个索引`i`在文本中从左向右移动，表示模式字符串第一个字符在文本中的位置，用一个索引`j`在模式中从右往左移动，如果某个字符匹配成功，j将向左移动，如果j从模式字符串的末尾一直移动到索引0模式字符串和文本字符串的字符都相等的话，就找到了一个匹配，此时返回i就得到文本中模式串的位置。当字符不匹配时，有如下三种情况：

- 匹配失败处，文本字符串的字符不包含在模式字符串中。如下图所示，在T处匹配失败了，且T不包含在模式字符串NEEDLE中。所以无论用NEEDLE哪个字符和T对齐都是徒劳，我们可以直接跳到下一个字符和文本字符串的L对齐（模式字符串移动了`j + 1`），然后再从模式字符串的最后一位开始比较。

![](http://obvjfxxhr.bkt.clouddn.com/bm_6745.PNG)

- 如果匹配失败处，文本字符串的字符包含在模式字符串中。根据数组right[]，查找到该字符最后一次出现的索引，让该处的字符和匹配失败处的字符对齐（这两个字符当然是相同的，这种情况下模式字符串向右移动了`j - right[c]`）.如下图所示，在N处匹配失败，因为N在模式字符串NEEDLE中，且N在NEEDLE中最后一次出现的索引为0。所以让i向右移动3 - 0 = 3个位置使得文本的N和模式的N对齐。
- 如果如果匹配失败处，文本字符串的字符包含在模式字符串中，但是模式字符串需要移动的位数`j - right[c]`是一个非正整数，这将导致模式字符串向左移动或原地不动。为了避免这种情况，直接将i加1来保证模式字符串至少向右移动了一个位置。还是下图”启发式方法没有起作用的时候“：当匹配失败处文本字符为E，包含在模式中，但E在模式中最后一次出现的索引为5，这意味着i需要右移3 - 5 = -2，也就是向左移动，我们当然不希望模式字符串左移，因此这种情况下，直接将模式字符串右移一位。

![](http://obvjfxxhr.bkt.clouddn.com/bm_85.PNG)



根据上面的描述可写出如下代码

```java
package Chap5;

public class BoyerMoore {

    public static int search(String pat, String txt) {
        int N = txt.length();
        int M = pat.length();
        // 根据模式串得到right[]数组
        int[] right = getRight(pat);
        // 匹配失败时，i需要右移的位数
        int skip;
        for (int i = 0; i <= N - M ; i += skip) {
            skip = 0;
            for (int j = M - 1; j >= 0 ; j--) {
                if (pat.charAt(j) != txt.charAt(i + j)) {
                    skip = j - right[txt.charAt(i + j)];
                    // 如果计算出来的skip不能使得i右移，直接让i向右移动1位
                    if (skip < 1) {
                        skip = 1;
                    }
                    break;
                }
            }
            // 经过上面的循环，字符都满足pat.charAt(j) == txt.charAt(i + j)，说明找到匹配
            if (skip == 0) {
                return i;
            }
        }
        return -1;
    }

    private static int[] getRight(String pat) {
        int R = 256;
        int[] right = new int[R];
        // 先初始化为全-1
        for (int i = 0; i < R; i++) {
            right[i] = -1;
        }
        // 记录模式字符串每个字符最后一次出现的索引
        for (int j = 0; j < pat.length(); j++) {
            right[pat.charAt(j)] = j;
        }
        return right;
    }

    public static void main(String[] args) {
        int index = BoyerMoore.search("abab", "abacghababzz");
        System.out.println(index); // 输出6
    }
}

```

Boyer-Moore算法在最坏情况下的时间复杂度为O(MN)，平均情况下复杂度为O(N / M).

## Rabin-Karp指纹字符串查找法

Rabin-Karp算法是基于散列的字符串查找算法。需要计算模式字符串的散列函数，然后用相同的散列函数计算文本所有可能的M个字符的子字符串散列值并寻找匹配。如果找到了一个散列值和模式字符串相同的子字符串，那么再继续验证两者是否相容。

散列函数使用除留余数法是个好的选择。举个简单的例子，对于十进制数`3141592653589793`，要在其中找到模式`26535`。首先要选择散列表的大小，（这个例子中选择了素数997）。然后计算出模式的散列值为26535 % 997 = 613。接着在文本字符串中按照从左到右的顺序，对所有长度为5的子字符串按照同样的散列函数计算出散列值，和模式串的散列值比较，如果相同就说明找到匹配了。如下图所示

![](http://obvjfxxhr.bkt.clouddn.com/rabinkarp_76gdf.PNG)

对于上面的5位数值，int型就能很好的完成所有计算（不溢出）。如果是100位甚至1000位呢，这里使用Horner方法，对于数中的每一位数字，将散列值乘以进制R，加上这个数字，然后对Q取余。我们也可以用同样的方法处理字符串，只是进制R变成了256（扩展ASCII码表）。

![](http://obvjfxxhr.bkt.clouddn.com/rabinkarp_876.PNG)

上面是针对十进制的整数。**如果将字符串当做整数，那么它可以看做是有高低位之分256进制数**。将进制R改成256可以写出如下方法。

```java
private static long hash(String key, int M) {
  	long h = 0;
  	for (int i = 0; i < M; i++) {
    	h = (h * R + key.charAt(i)) % Q;
  	}
  	return 0;
}
```

这段代码什么意思呢？在这之前先了解下一个取余的基本性质：**如果在每次算术操作后都将结果除以Q取余，这等价于在完成了所有算术操作后将最后结果对Q取余。这就是同余模定理。**举个简单的例子：

```
(A + B) % Q = (A % Q + B % Q) % Q
(A * B) % Q = (A % Q * B % Q) % Q
```

现在看上面的代码，每一步算术操作都对Q取余了。按照上面的定义，我们可以先撇开取余操作，将结果算出来后才取余。理解这个循环最简单的例子就是：比如有一个三位整数532。先根据`key.charAt(i)`取出5，然后取出`5 * 10`加上本次循环取出的3得到53，最后`53 * 10`加上取出的2，得到532！将进制换成256，该方法对于字符串也是一样的。这是用代码表述，用数学公式可以表达为

![](http://obvjfxxhr.bkt.clouddn.com/rabinkarp_TIM%E5%9B%BE%E7%89%8720171208145255.jpg)

如果用ti表示`txt.charAt(i)`，那么文本txt中的起始位置i的含有M个字符的子字符串所对应的数`x_i`可以表示式①那样。文本中下一个含有M个字符的子字符串`x_i+1`（等价于将模式字符串右移了一位和文本对齐比较）可以通过下面的简单计算和移项得到。用通俗的话来说就是

```
文本中下一个含有M个字符的子字符串对应的数字 = (当前的数字 - 第一个数的值) * R + 后一个数字的值
```

![](http://obvjfxxhr.bkt.clouddn.com/rabinkarp_TIM%E5%9B%BE%E7%89%8720171208145233.jpg)

根据上面的推论，对于十进制的数，取下一个长度为M的子字符串的计算可表述为下图。

![](http://obvjfxxhr.bkt.clouddn.com/rabinkarp_779.PNG)

当然对于字符串可以一样的道理。**为了保证减去第一个数字得到的值均为正，额外加上了一个Q（对最终结果没有影响，因为Q对Q取余为0），这样取余操作才能达到预期的效果。**说了这么多，可以写出代码了...

```java 
package Chap5;

import java.math.BigInteger;
import java.util.Random;

public class RabinKarp {
    private static int R = 256;
    private static long Q = longRandomPrime();

    private static long hash(String key, int M) {
        long h = 0;
        for (int i = 0; i < M; i++) {
            h = (h * R + key.charAt(i)) % Q;
        }
        return h;
    }

    public static int search(String pat, String txt) {
        int N = txt.length();
        int M = pat.length();
        long RM = 1;
        // 计算R^(M-1) % Q  用于减去第一个数字时，该值要和第一个数字相乘
        for (int i = 0; i < M - 1; i++) {
            RM = (RM * R) % Q;
        }
        // 模式的散列值
        long patHash = hash(pat, M);
        // 文本最开始M位的散列值
        long txtHash = hash(txt, M);
        // 如果一开始就匹配了
        if (patHash == txtHash && checkEqual(pat, txt, 0)) {
            return 0;
        }
        // 否则减去第一个数字，再加上后一个数字，得到散列值继续匹配
        // 从M开始，因为模式是[0, M-1]，M是模式最后一位的下一位
        // txtHash - RM * txt.charAt(i - M)用当前hash减去第一位数字，+Q主要是防止前面的结果为负数， *R是乘以基数， +txt.charAt(i)是加上后一位数字，最后%Q取余
        for (int i = M; i < N; i++) {
//            txtHash = (txtHash + Q - RM * txt.charAt(i - M) % Q) % Q;
//            txtHash = (txtHash*R +txt.charAt(i)) % Q;
            // 等价于上面的两句,性质还是同余模定理：每一个计算后都取一次余，和所有计算结束后取余的结果一样。
            txtHash = ((txtHash - RM * txt.charAt(i - M) + Q) * R + txt.charAt(i)) % Q;
            // 找到匹配。如果第一次就匹配，返回1，可归纳出应该返回 i -M + 1
            if (txtHash == patHash && checkEqual(pat, txt, i - M + 1)) {
                return i - M + 1;
            }
        }
        return -1; // 未找到匹配
    }

    // 散列值相同时检查每个字符是否相同
    private static boolean checkEqual(String pat, String txt, int offset) {
        for (int j = 0; j < pat.length(); j++) {
            if (pat.charAt(j) != txt.charAt(offset + j))
                return false;
        }
        return true;
    }

    // 返回一个31位的随机素数,用于除留余数的Q
    private static long longRandomPrime() {
        BigInteger prime = BigInteger.probablePrime(31, new Random());
        return prime.longValue();
    }

    public static void main(String[] args) {
        int index = RabinKarp.search("abab", "abacghababzz");
        System.out.println(index);
    }
}

```

`longRandomPrime()`这个方法用于产生一个随机的31位long型的很大的素数，除留余数时用，即我们一直说的Q。`checkEqual`会在散列值相同的时候检查每个字符是否对应相同，只有字符相同时才能说找到匹配了，这样即使有散列碰撞（多个键散列成同一个数字）也能得到正确的结果。R = 256，`R^(M-1)`是一个超大的值，为了防止溢出，先对其进行取余操作（每次运算都取了一次余），①式计算出的x_i也是个很大的值，在代码中先对其取余得到散列值，即`hash(x_i) = x_i mod Q`。**反正记住，同余模定理贯穿了整个算法的实现。**

算法先得到模式的散列值，和文本前M位（模式串的长度）子字符串的散列值，先比较一次，如果散列值相等且字符内容相同说明一开始就匹配成功，返回0；否则取文本的下一个长度为M的子字符串（相当于将模式向右移动了一位），继续和模式串比较，匹配成功返回索引`i -M +1`为什么是返回这个值，可以思考一种最简单的情况当i等于M时候，此时仅仅把模式向右移动了一位——文本索引1处和模式字符串首位对齐——不如举个例子实在，如下i = M = 3，此时匹配成功返回应该1。

```
ABCDABAB
 BCD
```

下面对比下各个子字符串查找算法的优劣：

暴力查找法实现简单便于理解且在一般情况下都工作良好（最坏情况很少出现），KMP算法能保证线性级别的性能（最坏情况下也是）且不需要在文本中回退；Boyer-Moore算法的性能在一般情况下式亚线性级别的（可能是线性级别的M倍）；Rabin-Karp算法是线性级别的。

每种算法也各有缺点：暴力法查找所需的时间可能和MN成正比，KMP和Boyer-Moore算法都需要额外的内存空间。Rabin-Karp的内循环很长（很多次的算术运算，而其他算法只需要比较字符串）。这些特点都总结在了下表中

![](http://obvjfxxhr.bkt.clouddn.com/rabinkarp_hft.PNG)

---

by @sunhaiyu

2017.12.8
