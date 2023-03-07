[TOC]

# COA机考复习

# programming01（Transformer）

## 1 实验要求

在Transformer类中实现以下6个方法

1. 将整数真值（十进制表示）转化成补码表示的二进制，默认长度32位

``` java
 public String intToBinary(String numStr) 
```


2. 将补码表示的二进制转化成整数真值（十进制表示）

``` java
 public String binaryToInt(String binStr)
```


3. 将十进制整数的真值转化成NBCD表示（符号位用4位表示）

``` java
public String decimalToNBCD(String decimal)
```


4. 将NBCD表示（符号位用4位表示）转化成十进制整数的真值

``` java
public String NBCDToDecimal(String NBCDStr)
```

5. 将浮点数真值转化成32位单精度浮点数表示

   - 负数以"-"开头，正数不需要正号

   - 考虑正负无穷的溢出（"+Inf", "-Inf"，见测试用例格式）


```java
public String floatToBinary(String floatStr)
```

6. 将32位单精度浮点数表示转化成浮点数真值
   - 特殊情况同上


```java
public String binaryToFloat(String binStr)
```

## 2 实验攻略

本次实验推荐使用的库函数有

```java
Integer.parseInt(String s)//将字符串s转换为十进制的数字
Float.parseFloat(String s)//将字符串s转换为浮点型
String.valueOf(int i)//将int变量i转换成字符串 
String.valueOf(float f)//将float变量f转换成字符串 
```

本次实验不允许使用的库函数有

```java
Integer.toBinaryString(int i)
Float.floatToIntBits(float value)
Float.intBitsToFloat(int bits)
```

# programming02（ALU）

## 1 实验要求

在ALU类中实现2个方法，具体如下

1.计算两个32位二进制整数补码真值的和

``` java
 public DataType add(DataType src, DataType dest)
```

2.计算两个32位二进制整数补码真值的差，dest表示被减数，src表示减数(即计算dest - src)

``` java
 public DataType sub(DataType src, DataType dest) 
```

## 2 实验攻略

### 2.1 代码实现要求

有些同学可能注意到，将传入的参数通过transformer转化为int，再通过整数的加减运算后，将结果重新转化为DataType即可轻松完成实验。在此，我们**明确禁止**各位采用这种方法来完成本次实验。

### 2.2 数据封装

从本次实验开始，我们采用统一的类DataType来封装32位的二进制数，包括二进制补码整数、NBCD码与IEEE754浮点数。核心数据结构如下

``` java
private final byte[] data = new byte[4];
```

采用这样的数据封装将保证DataType类中存放的一定是32位二进制数，并且有利于ALU等运算模块与其他模块的整合。为了方便编码，我们为DataType类提供了构造函数与toString函数，便于DataType对象与String对象之间的转化，具体可阅读DataType类源码。

# programming03（ALU）

## 1 实验要求

在ALU类中实现实现整数的二进制乘法(要求使用布斯乘法实现)。

输入和输出均为32位二进制补码，计算结果直接截取低32位作为最终输出

``` java
 public DataType mul(DataType src, DataType dest)
```

## 2 实验攻略

本次实验我们仍然**明确禁止**，将传入的参数通过transformer转化为int，再通过整数的四则运算后，将结果重新转化为DataType完成实验。

# programming04（ALU）

## 1 实验要求

实现整数的二进制除法 (dest ÷ src)，使用恢复余数除法和不恢复余数除法均可。输入为32位二进制补码，输出为32位商，并且将32位余数正确储存在余数寄存器remainderReg中。

注意：除数为0，且被除数不为0时要求能够正确抛出ArithmeticException异常。

``` java
 public DataType div(DataType src, DataType dest)
```

## 2 实验攻略

本次实验我们仍然**明确禁止**，将传入的参数通过transformer转化为int，再通过整数的四则运算后，将结果重新转化为DataType完成实验。

# programming05（FPU）

## 1 实验要求

在FPU类中实现2个方法，具体如下

1.计算两个浮点数真值的和

``` java
 public DataType add(DataType src, DataType dest)
```

2.计算两个浮点数真值的差

``` java
 public DataType sub(DataType src, DataType dest)
```

## 2 实验指导

### 2.1 代码实现要求

本次实验中，我们仍然**明确禁止**各位采用直接转浮点数进行四则运算来完成本次实验。

### 2.2 代码实现流程

对于浮点数加减运算的流程，课件与课本都有很详细的讲解，在此不再赘述。对于代码实现层面，大致可分为以下几步:

1. 处理边界情况(NaN, 0, INF)
2. 提取符号、阶码、尾数
3. 模拟运算得到中间结果
4. 规格化并舍入后返回

#### 2.2.1 处理边界情况

在框架代码中，我们提供了cornerCheck方法来检查0和INF的情况，大家直接调用即可。

此外，对于NaN的情况，我们提供了一个基于正则表达式的处理方案，可用如下代码进行检查：

``` java
String a = dest.toString();
String b = src.toString();
if (a.matches(IEEE754Float.NaN_Regular) || b.matches(IEEE754Float.NaN_Regular)) {
    return new DataType(IEEE754Float.NaN);
}
```

在util.IEEE754Float类中，我们提供了NaN的正则表达式，对于正则表达式的作用机制大家可以自行查阅。

在本次作业中，大家直接调用cornerCheck方法以及上述正则表达式的解决方案即可轻松完成第一步：对边界情况的检查。

如果你顺利实现第一步，应该可以在seecoder平台上拿到15分。

#### 2.2.2 提取符号、阶码、尾数

在本次作业中，我们使用IEE754浮点数运算标准，模拟32位单精度浮点数，符号位、指数部分与尾数部分分别为1、8、23位，同时使用3位保护位(GRS保护位)，大家经过简单操作即可完成这一步。

这一步有三个需要特殊处理的地方：

1. 当有一个操作数提取出的阶码为全1时，应该返回其本身。（为什么？考虑无穷加减其他数的结果）
2. 当提取出的阶码为全0时，说明该操作数是一个非规格化数，此时应该对阶码+1使其真实值变为1，以保证后面的对阶操作不会出错。（为什么？可以考察IEEE754浮点数标准中阶码为0和阶码为1分别表示2的多少次方）
3. 在这一步中不要忘记尾数的最前面添加上隐藏位，规格化数为1，非规格化数为0。所以提取结束后尾数的位数应该等于1+23+3=27。

#### 2.2.3 模拟运算得到中间结果

这一步是要求大家实现的重要步骤。这一步主要做两件事情。

第一件事情是对阶，采用小阶向大阶看齐的方式，小阶增加至大阶，同时尾数右移，保证对应真值不变。注意，基于GRS保护位标准，尾数右移时不能直接将最低位去掉。我们提供了对尾数进行右移的方法，方法签名如下：

``` java
 private String rightShift(String operand, int n)
```

第一个参数为待右移的尾数，第二个参数为右移的位数。请大家每次对尾数进行右移操作时都调用这个方法，否则很可能出现最后对保护位进行舍入后，尾数与结果差1的情况。

第二件事情是尾数相加或相减。这一步相对简单，大家可以调用提供的ALU类进行操作，也可以拷贝上次实验中自己写的代码进行操作。

#### 2.2.4 规格化并舍入后返回

在这一步中，我们只要求大家进行规格化的处理。这里需要大家思考的是，在上一步运算结束后，有哪几种情况会导致结果不符合规格化的条件？

1. 当运算后尾数大于27位时，此时应该将尾数右移1位并将阶码加1。
   - 注意，这个将阶码加1的操作可能会导致阶码达到"11111111"，导致溢出。针对这种阶码上溢的情况，应该返回什么？
2. 当运算后尾数小于27位时，此时应该不断将尾数左移并将阶码减少，直至尾数达到27位或阶码已经减为0。
   - 注意，若阶码已经减为0，则说明运算得到了非规格化数，此时应该怎么办？（可以考察阶码为0000 0001，尾数为0.1000 0000 0000 0000 0000 0000 00的浮点数该如何正确表示）

我们提供了相关的本地用例，大家可以仔细揣摩其中的奥妙。

对于规格化后的舍入操作，我们不要求掌握GRS保护位相关的舍入操作，感兴趣的同学可以自行查阅。我们提供了舍入操作的函数如下

``` java
 private String round(char sign, String exp, String sig_grs) 
```

请注意，在调用此方法前，请确保你传入的参数已经进行了规格化，务必确保传入的符号位为1位，阶码为8位，尾数为1+23+3=27位。

在此方法中，我们已经对GRS保护位进行了相应的处理并完成舍入，返回的结果即为32位的字符串，转化为DataType类型后即可通过测试。

至此，你已经完成了浮点数加减法的全部工作(・ω・)ノ

### 2.3 测试用例相关

本次实验中，test9方法会使用表驱动的方法进行多次的运算。如果出现了报错，但却不知道是哪一对数字报的错，可以在函数运行过程中，每当遇到expect结果跟actual结果不一样的情况时，将src、dest、expect与actual分别打印到控制台，然后再对这组数据进行单步调试。这种调试方法不但在本次作业中非常有用，并且也会让你在以后的debug生涯中受益匪浅。

例如，可以在test9中的`assertEquals`语句前插入如下代码。

```java
String expect = Transformer.intToBinary(Integer.toString(Float.floatToIntBits(input[i] + input[j])));
if (!expect.equals(result.toString())) {
    System.out.println("i = " + i + ", j = " + j);
    System.out.println("src: " + src);
    System.out.println("dest:" + dest);
    System.out.println("Expect: " + expect);
    System.out.println("Actual: " + result);
    System.out.println();
}
```

### 2.4 GRS保护位

 注：以下内容不需要掌握

GRS保护位机制使用3个保护位辅助完成舍入过程。一个27位的尾数可表示为

```
1(0) . m1 m2 m3 …… m22 m23 G R S
```

这里G为保护位（guard bit），用于暂时提高浮点数的精度。R为舍入位（rounding bit），用于辅助完成舍入。S为粘位（sticky bit）。粘位是R位右侧的所有位进行逻辑或运算后的结果，简单来说，在右移过程中，一旦粘位被置为1（表明右边有一个或多个位为1）它就将保持为1。

在round函数中，根据GRS位的取值情况进行舍入，舍入算法采用就近舍入到偶数。简单来说，在进行舍入时分为以下三种情况。

1. 当GRS取值为"101" "110" "111"时，进行舍入时应在23位尾数的最后一位上加1。
2. 当GRS取值为"000" "001" "010" "011"时，进行舍入时直接舍去保护位，不对23位尾数进行任何操作。
3. 当GRS取值为"100"时，若23位尾数为奇数则加1使其变成偶数，若23位尾数为偶数则不进行任何操作。

# programming06（FPU）

## 1 实验要求

在FPU类中实现2个方法，具体如下

1.计算两个浮点数真值的积 dest × src

``` java
 public DataType mul(DataType src, DataType dest)
```

2.计算两个浮点数真值的商 dest ÷ src

注意：除数为0，且被除数不为0时要求能够正确抛出ArithmeticException异常

``` java
 public DataType div(DataType src, DataType dest)
```



在NBCDU类中实现2个方法，具体如下

1.计算两个32位NBCD(8421码)的和，参数与返回结果为32位NBCD码。

``` java
 DataType add(DataType src, DataType dest)
```

2.计算两个32位NBCD(8421码)的差，dest表示被减数，src表示减数(即计算dest - src)

``` java
 DataType sub(DataType src, DataType dest)
```



## 2 实验指导

### 2.1 代码实现要求

本次实验中，我们仍然**明确禁止**各位采用直接转浮点数、十进制进行四则运算来完成本次实验。

### 2.2 代码实现流程

在充分掌握了浮点数的加减运算后，浮点数的乘除运算就十分简单了。其基本步骤和加法类似，相比加法运算，还可以免去对阶的过程。基本流程仍然可以分为以下四步：

1. 处理边界情况(NaN, 0, INF)
2. 提取符号、阶码、尾数
3. 模拟运算得到中间结果
4. 规格化并舍入后返回

#### 2.2.1 处理边界情况

在框架代码中，我们仍然提供了cornerCheck方法，使用正则表达式处理NaN的方法也同样适用，在此不再赘述。

注意，在除法运算中，还需要额外判断除数为0且被除数不为0的情况。

#### 2.2.2 提取符号、阶码、尾数

在本次作业中，我们使用IEE754浮点数运算标准，模拟32位单精度浮点数，符号位、指数部分与尾数部分分别为1、8、23位，同时使用3位保护位(GRS保护位)，大家经过简单操作即可完成这一步。

这一步有三个需要特殊处理的地方：

1. 当有一个操作数提取出的阶码为全1时，应该返回正无穷或负无穷，注意符号需要额外判断。（为什么？考虑无穷乘其他数的结果）
2. 当提取出的阶码为全0时，说明该操作数是一个非规格化数，此时应该对阶码+1使其真实值变为1，以保证后面的对阶操作不会出错。（为什么？可以考察IEEE754浮点数标准中阶码为0和阶码为1分别表示2的多少次方）
3. 在这一步中不要忘记尾数的最前面添加上隐藏位，规格化数为1，非规格化数为0。所以提取结束后尾数的位数应该等于1+23+3=27。

聪明的你是不是发现了，至此的所有操作都跟上次作业几乎一模一样。该怎么~~Ctrl C + Ctrl V~~操作就不用我多说了吧。

#### 2.2.3 模拟运算得到中间结果

乘除法运算对于符号位的计算非常简单，直接可以根据两个操作数的符号位得到结果的符号位，在此不作更深入的讲解。

对于阶码的计算，与加减法运算不同的是，乘除法运算不再需要对阶操作，而是直接计算结果阶码。其计算过程分别为

- 乘法：尾数相乘，阶码相加后减去偏置常数
- 除法：尾数相除，阶码相减后加上偏置常数

对于尾数的计算，在此需要大家分别实现27位无符号数的乘法与除法，运算流程可以参考课件。相信有了ALU的乘除法基础，这一步不会花费太多时间。

需要注意的是，对于27位乘法运算，返回的结果是54位的乘积。由于两个操作数的隐藏位均为1位，所以乘积的隐藏位为2位（为什么？）。为了方便后续操作，需要通过阶码加1的方式来间接实现小数点的左移，修正这个误差，以保证尾数的隐藏位均为1位。

#### 2.2.4 规格化并舍入后返回

在这一步中，我们仍然只要求大家进行规格化的处理。相比于加减法运算，乘除法的运算结果破坏规格化的情况更多，增加了阶码为负数的情况。简单分类如下：

1. 运算后54位尾数的隐藏位为0且阶码大于0，此时应该不断将尾数左移并将阶码减少，直至尾数隐藏位恢复为1或阶码已经减为0。
2. 运算后阶码小于0且54位尾数的前27位不全为0，此时应该不断将尾数右移并将阶码增加，直至阶码增加至0或尾数的前27位已经移动至全0。
3. 经过上述两步操作后，尾数基本恢复规格化，但阶码仍有可能破坏规格化，分为以下三种情况：
   - 阶码为"11111111"，发生阶码上溢，此时应该返回什么？
   - 阶码为0，则说明运算得到了非规格化数，此时应该将尾数额外右移一次，使其符合非规格化数的规范。（为什么？可以考察阶码为0000 0001，尾数为0.1000 0000 0000 0000 0000 0000 00的浮点数的规格化过程）
   - 阶码仍小于0，发生阶码下溢，此时又应该返回什么？

可能大家看到这里觉得很乱，没关系，我们提供的fpuMulTest9涵盖了这里面的所有情况，大家可以在debug的过程中体会其中的玄机。以上规格化过程可用伪代码表示如下：

```java
while (隐藏位 == 0 && 阶码 > 0) {
    尾数左移，阶码减1; // 左规
}
while (尾数前27位不全为0 && 阶码 < 0) {
    尾数右移，阶码加1; // 右规
}

if (阶码上溢) {
    将结果置为无穷;
} else if (阶码下溢) {
    将结果置为0;
} else if(阶码 == 0) {
	尾数右移一次化为非规格化数;
} else {
    此时阶码正常，无需任何操作;
}
```

对于规格化后的舍入操作，我们不要求掌握GRS保护位相关的舍入操作，感兴趣的同学可以阅读2.5节内容。我们依然提供了舍入操作的函数，方法签名如下

``` java
 private String round(char sign, String exp, String sig_grs) 
```

请注意，在调用此方法前，请确保你传入的参数已经进行了规格化，务必确保传入的符号位为1位，阶码为8位。可以传入位数大于等于27位的尾数，round函数会先取出前27位作为1位隐藏位+23位有效位+3位GRS保护位，剩余的所有位数都将舍入到保护位的最后一位中。

在此方法中，我们已经对GRS保护位进行了相应的处理并完成舍入，返回的结果即为32位的字符串，转化为DataType类型后即可进行返回。

至此，你已经完成了浮点数乘法的全部工作(・ω・)ノ

### 2.3 对浮点数除法的相关说明

浮点数除法和乘法的主要区别在第三步：模拟运算得到中间结果上面。由于27位尾数进行无符号除法后，得到的商也是27位的，已经符合了“1位隐藏位+23位有效位+3位保护位”的要求，所以不再需要额外的操作。

同时，也正是因为这个27位尾数的除法，得到的27位商的精度将会严重损失（为什么？）。因此，我们无法对除法运算提供像加减乘一样如此精心打磨的test9，也无法提供RandomTest。我们本可以通过一些额外的操作来改进这一步运算的精度（比如将尾数扩展至更多位数，进行运算前将被除数尽可能左移，将除数尽可能右移等），但考虑这会大幅增加作业难度，我们很遗憾地放弃了这个改进。

因此，由于精度限制，本次作业中的除法的所有用例都是规格化数，大家无需考虑非规格化数的情况。此外，由于大规模用例的缺失，为了让大家也能够拥有足够的测试用例对除法进行debug，我们只会隐藏很简单的一些用例，其余用例全部提供给大家。

# programming07

## 1 实验要求

### 1.1 fetch

在Cache类中，实现**基于通用映射策略的**fetch方法，查询数据块在cache中的行号。如果目标数据块不在cache内，则将数据从内存读到Cache，并返回被更新的cache行的行号

``` java
 private int fetch(String pAddr)
```

### 1.2 map

在Cache类中，实现**基于通用映射策略的**map方法，根据数据在内存中的块号进行映射，返回cache中所对应的行，-1表示未命中

```java
private int map(int blockNO)
```

### 1.3 strategy

在cacheReplacementStrategy包中，分别实现**先进先出**替换策略、**最不经常使用**替换策略、**最近最少使用**替换策略。

### 1.4 write

在Cache类中，**基于写直达和写回两种策略**完善write方法

```java
public void write(String pAddr, int len, byte[] data)
```





## 2 实验攻略

### 2.1 实验概述

本次作业为一个通用的cache系统。在这个系统中，不仅需要大家实现基本的读写功能，还需要大家实现各种各样的策略，具体有：

- 映射策略：直接映射、关联映射、组关联映射
- 替换策略：最近最少使用算法 (LRU)、先进先出算法 (FIFO)、最不经常使用算法 (LFU)
- 写策略：写直达(write through)、写回法(write back)

请认真阅读PPT与课本，任何与具体实现相关的知识都已经体现在PPT之中了。

接下来，我们将对Cache类的源码进行一个全面的解读。



### 2.2 代码导读

#### 2.2.1 代码结构

```
.
│  pom.xml	# Maven Config File
│  README.md	# This File
│
├─.idea
│
└─src
   ├─main
   │  └─java
   │      ├─cpu
   │      │
   │      ├─memory
   │      │  │  Memory.java	# 主存类，需要阅读，不需要修改
   │      │  │
   │      │  └─cache
   │      │      │  Cache.java	# Cache类，需要阅读且修改
   │      │      │
   │      │      └─cacheReplacementStrategy
   │      │              FIFOReplacement.java	# 先进先出替换策略类，需要修改
   │      │              LFUReplacement.java	# 最不经常使用替换策略类，需要修改
   │      │              LRUReplacement.java	# 最近最少使用替换策略类，需要修改
   │      │              ReplacementStrategy.java	# 替换策略接口，需要阅读，不需要修改
   │      │
   │      └─util
   │
   └─test
       └─java
           └─memory
              └─cache
                      AssociativeMappingTest.java	# 关联映射策略测试
                      DirectMappingTest.java	# 直接映射策略测试
                      SetAssociativeMappingTest.java	# 组关联映射策略测试
                      WriteBackTest.java	# 写回策略测试
                      WriteThroughTest.java	# 写直达策略测试
```



#### 2.2.2 内存结构模拟

我们模拟了一个32KB大小的cache，规定每一行大小为64B，则cache一共含有512行，如下所示。

```java
public static final int CACHE_SIZE_B = 32 * 1024; // 32 KB 总大小

public static final int LINE_SIZE_B = 64; // 64 B 行大小
```

如何模拟cache的内存结构呢？我们在Cache类中定义了一个私有内部类：CacheLine。CacheLine表示Cache中的一行。这个内部类的数据结构比较简单，大家可以自行阅读源码，在此不再赘述。



#### 2.2.3 映射方法模拟

在Cache类中，我们定义了两个变量，表示组数和每组行数，并且提供了相应的setter方法，如下所示

```java
private int SETS;   // 组数
private int setSize;    // 每组行数
public void setSETS(int SETS) {
    this.SETS = SETS;
}
public void setSetSize(int setSize) {
    this.setSize = setSize;
}
```

由于直接映射和关联映射可以视为特殊的组关联映射，因此，通过设置不同的SETS和setSize，即可实现不同映射方法模拟。

同时，不同的映射方法会导致cache行的tag位数不同。以本次实验为例，若采用直接映射，则tag的位数应该为17位，而如果采用关联映射，则tag位数应该为26位（怎么算出来？）。为了实现通用的cache系统，我们在CacheLine类里面统一使用26位的char数组来模拟tag，tag数组中以低位为准，高位补0，有效长度取决于具体的映射方法。以直接映射方法为例，当tag为1时，数组中后17位应该为“00000000000000001”，前9位补0即可。



#### 2.2.4 数据读取模拟

在成功模拟cache的内存结构和映射策略后，接下来就是要模拟cache的数据读取功能。我们提供了一个read方法。请同学们编码之前仔细阅读这段代码，这会帮助你理解整个Cache运行的流程，也会帮助你进行编码。

```java
/**
* 读取[pAddr, pAddr + len)范围内的连续数据，可能包含多个数据块的内容
*
* @param pAddr 数据起始点(32位物理地址 = 26位块号 + 6位块内地址)
* @param len   待读数据的字节数
* @return 读取出的数据，以char数组的形式返回
*/
public byte[] read(String pAddr, int len) {
    byte[] data = new byte[len];
    int addr = Integer.parseInt(Transformer.binaryToInt("0" + pAddr));
    int upperBound = addr + len;
    int index = 0;
    while (addr < upperBound) {
        int nextSegLen = LINE_SIZE_B - (addr % LINE_SIZE_B);
        if (addr + nextSegLen >= upperBound) {
            nextSegLen = upperBound - addr;
        }
        int rowNO = fetch(Transformer.intToBinary(String.valueOf(addr)));
        byte[] cache_data = cache[rowNO].getData();
        int i = 0;
        while (i < nextSegLen) {
            data[index] = cache_data[addr % LINE_SIZE_B + i];
            index++;
            i++;
        }
        addr += nextSegLen;
    }
    return data;
}
```

由于在cache中读数据可能会有跨行的问题，即要读取的数据在内存中跨过了数据块的边界，我们在read方法内已经帮大家处理好了这种情况，大家不需要再考虑数据跨行的问题。



#### 2.2.5 替换策略模拟

在cache进行映射时，一旦cache行被占用，当新的数据块装入cache中时，原先存放的数据块将会被替换掉。对于直接映射策略，每个数据块都只有唯一对应的行可以放置，没有选择的机会，因此不需要替换策略。而对于关联映射和组关联映射，就需要替换策略来决定替换哪一行了。

本次作业中，我们用策略模式来模拟替换策略，Cache类中相关代码如下

```java
private ReplacementStrategy replacementStrategy;    // 替换策略

public void setReplacementStrategy(ReplacementStrategy replacementStrategy) {
        this.replacementStrategy = replacementStrategy;
    }
```

在ReplacementStrategy接口中，我们定义了两个方法，方法的具体功能已在注释中标出，方法签名如下

```java
void hit(int rowNO);

int replace(int start, int end, char[] addrTag, byte[] input);
```

并且，我们提供了实现ReplacementStrategy接口的三个子类：FIFOReplacement类、LFUReplacement类和LRUReplacement类。

根据策略模式的思想，Cache类不需要知道replacementStrategy字段中存放的具体是哪个子类，它只需要在合适的地方调用ReplacementStrategy接口的这两个方法。依据Java的多态特性，在Cache类调用这两个方法时，会自动根据replacementStrategy字段中存放的具体策略去选择具体的实现方法。这样，通过策略模式，我们即可完成替换策略的模拟~



#### 2.2.6 写策略模拟

本次作业中新增了cache的写功能，提供了write方法。在write方法中，我们已经帮你实现好了向cache中写数据的功能，并且也处理好了数据跨行的情况。方法实现如下

```java
/**
* 向cache中写入[pAddr, pAddr + len)范围内的连续数据，可能包含多个数据块的内容
*
* @param pAddr 数据起始点(32位物理地址 = 26位块号 + 6位块内地址)
* @param len   待写数据的字节数
* @param data  待写数据
*/
public void write(String pAddr, int len, byte[] data) {
    int addr = Integer.parseInt(Transformer.binaryToInt("0" + pAddr));
    int upperBound = addr + len;
    int index = 0;
    while (addr < upperBound) {
        int nextSegLen = LINE_SIZE_B - (addr % LINE_SIZE_B);
        if (addr + nextSegLen >= upperBound) {
            nextSegLen = upperBound - addr;
        }
        int rowNO = fetch(Transformer.intToBinary(String.valueOf(addr)));
        byte[] cache_data = cache[rowNO].getData();
        int i = 0;
        while (i < nextSegLen) {
            cache_data[addr % LINE_SIZE_B + i] = data[index];
            index++;
            i++;
        }

        // TODO

        addr += nextSegLen;
    }
}
```

如果只是写数据到cache，那么本次作业就非常的简单，但是考虑了主存的内容后，一切都变的复杂了起来。由于Cache和Memory的数据之间存在一定的一致性，会不会出现Cache被修改了，主存也被修改了，但是两次修改的数据不一样呢？这个时候该以哪一份数据为准呢？这时候就需要写策略了。

由于写策略只分为写直达和写回两种策略，在本次作业中，我们简单地通过一个布尔类型的字段来模拟，如下所示

```java
public static boolean isWriteBack;   // 写策略
```

当isWriteBack为true时，表示此时Cache使用写回策略。当isWriteBack为false时，表示此时Cache使用写直达策略。这样，我们就简单地完成了写策略的模拟。

在你已经结合源代码充分理解上述内容后，接下来就可以开始快乐地编码啦！



### 2.3 实现指导

#### 2.3.1 故事的开始——通用映射策略的实现

在你充分阅读源代码后，你会发现read方法中需要调用fetch方法，故事就从这个fetch方法开始。请大家从fetch方法开始着手，结合cache的工作流程，一步步实现好cache基本的数据读取功能。

在fetch方法中，你需要计算出数据所在的块号，然后调用map方法查询数据块在cache中的行号。如果目标数据块不在cache内，则需要将数据从内存读到cache（需要阅读Memory类源码），并返回被更新的cache行的行号。

在map方法中，你需要计算出当前块号所对应的cache行号，然后将当前块号与该cache行的信息进行比较（需要比较什么信息？），来判断是否命中。命中则返回行号，未命中则返回-1。

在本次作业中，请确保fetch和map方法依照注释正确实现，因为框架代码会对这两个方法进行直接的调用。fetch和map方法均已用TODO标出。



#### 2.3.2 替换策略的实现

第二步，你需要实现好三个策略类：FIFOReplacement类、LFUReplacement类和LRUReplacement类，并在Cache类中合适的地方对hit和replace方法进行调用。注意：

- 对于FIFO策略，你应该需要用到CacheLine中的timeStamp字段，记录每一行进入Cache的时间。
- 对于LFU策略，你应该需要用到CacheLine中的visited字段，记录每一行被使用的次数。
- 对于LRU策略，你应该需要用到CacheLine中的timeStamp字段，记录每一行最后被访问时间。

至于三个策略的hit方法具体需要干什么事情，这就留给聪明的你自己思考啦。而replace函数要干的事情也很简单，就是在给定范围内，根据具体的策略，寻找出需要被替换的那一行并进行替换并返回行号。

注意，在这一步中，你可能会发现在三个Replacement类中无法访问CacheLinePool，因为他被设置为了Cache类的私有字段。这时候，你可能会想把private统统改成public来解决问题，但这并不符合面向对象的数据封装思想。更好的解决方法应该是编写get/set函数，你可能需要用到的get/set函数方法签名如下，大家可以自行使用并实现

```java
// 获取有效位
public boolean isValid(int rowNO) 
    
// 获取脏位
public boolean isDirty(int rowNO)
    
// LFU算法增加访问次数
public void addVisited(int rowNO) 
    
// 获取访问次数
public int getVisited(int rowNO)
    
// 用于LRU算法，重置时间戳
public void setTimeStamp(int rowNO)
    
// 获取时间戳
public long getTimeStamp(int rowNO) 
    
// 获取该行数据
public char[] getData(int rowNO) 
```

注意，我们在框架代码中留下了一个update方法。如果你懂得如何抽象出这个方法，那么在三个Replacement类中就可以直接调用了哦。



#### 2.3.3 写策略的实现

在顺利完成上面两步之后，你就可以进入最后一步——写策略啦。可以发现，涉及到cache往主存写数据的只有两个地方：

- write函数直接向cache写数据时
- replace函数需要替换掉一行数据时

写策略其实是代码量最小的部分，你只需要在write函数和replace函数的相应地方对isWriteBack字段判断，然后根据具体策略来做不同的事情。具体来说，写直达策略就是在write函数完成写cache后直接修改主存；写回策略就是在write函数完成写cache后设置好脏位，并在replace函数将要替换掉该行的时候将该行写回内存。

提示一下，在你需要往主存写数据的时候，你可能会发现你手上只有一个行号，并不知道内存的物理地址，这时候就需要在Cache类中编写一个根据行号计算物理地址的方法，具体怎么实现留给聪明的你啦。

需要注意的是，对于写回策略，脏位和有效位之间是存在一定的约束的，大家可以在测试时发现其中的奥妙。



#### 2.3.4 其他方法说明

在Cache类中，我们还友好地提供了计算块号的方法，大家可以自行调用，方法签名如下

```java
private int getBlockNO(String pAddr)
```

此外，我们还提供了几个仅用于测试的方法，在这些方法上的注释都已经进行了相关的说明。请大家不要修改这些方法，否则会影响到测试。



### 2.4 总结

所有需要大家完成的部分都已经用TODO标出。为了减轻大家的负担，我们归纳了本次作业中你需要完成的小任务以及步骤

1. 正确实现好通用的映射策略，然后你应该可以通过DirectMappingTest的4个用例，以及AssociativeMappingTest和SetAssociativeMappingTest的两个test01。
2. 正确实现好FIFO替换策略，然后你应该可以通过AssociativeMappingTest和SetAssociativeMappingTest的两个test02。
3. 正确实现好LFU替换策略，然后你应该可以通过AssociativeMappingTest和SetAssociativeMappingTest的两个test03。
4. 正确实现好LRU替换策略，然后你应该可以通过AssociativeMappingTest和SetAssociativeMappingTest的两个test04。
5. 正确实现好写回策略，然后你应该可以通过WriteBackTest的4个测试用例。
6. 正确实现好写直达策略，然后你应该可以通过WriteThroughTest的4个测试用例。

至此，你已经完成了全部工作(・ω・)ノ

为了降低实验难度，本次实验我们提供了全部的测试用例用例给大家进行调试。在测试中，我们不仅会检查访问特定行是否读出了正确的数据，还会访问特定的行检查它的Tag以及有效位。具体细节都已经在测试用例中给出，大家可以自行阅读。



## 3 相关资料

### 3.1 设计模式——单例模式

单例模式（Singleton Pattern）是 Java 中最简单的设计模式之一。这种模式涉及到一个单一的类，该类负责创建自己的对象，同时确保只有单个对象被创建。这个类提供了一种访问其唯一的对象的方式，可以直接访问，不需要实例化该类的对象。单例模式的要点如下：

1. 单例类只能有一个实例。
2. 单例类必须自己创建自己的唯一实例。
3. 单例类必须给所有其他对象提供这一实例。

为什么要使用单例模式？因为我们使用了OO编程语言，但是在真实的计算机系统当中，很多部件都是唯一的。所以我们需要通过单例模式来保证主存、CPU、Cache等的唯一性，在本次作业中体现为保证Memory类和Cache类分别只有一个实例，并分别提供一个访问它们的全局访问点。

如何实现单例模式？我们只需要将类的构造方法私有化，再添加一个该类类型的私有静态字段，然后提供一个该类的get方法。对于使用该类的使用者们来说，他们只能通过get方法得到该类的实例，那么它们看到的永远是相同的对象。实现代码如下

```java
public A{
private A() {}
private static A a = new A();
public static A getA() { return a; }
        }
```

本次作业中，Cache类和Memory类都采用了单例模式，大家可以自行阅读相关代码。



### 3.2 设计模式——策略模式

在软件开发中我们常常会遇到这种情况，实现某一个功能有多种策略，我们需要根据环境或者条件的不同选择不同的策略来完成该功能。

一种常用的方法是硬编码(Hard Coding)在一个类中。举个例子，比如排序，需要提供多种排序算法，可以将这些算法都写到一个类中，在该类中提供多个方法，每一个方法对应一个具体的排序算法。当然，我们也可以将这些排序算法封装在一个统一的方法中，通过if…else…或者case等条件判断语句来进行选择。这两种实现方法我们都称之为硬编码。

上述硬编码的做法有一个严重的缺点：在这个算法类中封装了多个排序算法，该类代码将较复杂，维护较为困难。如果需要增加一种新的排序算法，或者更换排序算法，都需要修改封装算法类的源代码。

为了解决这个问题，策略模式就应运而生了。策略模式通过定义一系列的算法，把它们一个个封装起来，并且使它们可相互替换。它主要解决了在有多种算法相似的情况下，使用 if...else 所带来的复杂和难以维护。

策略模式的具体实现可以参考：https://www.runoob.com/design-pattern/strategy-pattern.html

# programming08

## 1 实验要求

### 1.1 Disk

在Disk类中，实现磁头寻道相关的两个方法。

```java
public void seek(int start)
public void addPoint()
```

### 1.2 Scheduler

在Scheduler类中，实现三个磁盘调度算法：先来先服务算法、最短寻道时间优先算法、扫描算法，并计算平均寻道长度。

```java
public double FCFS(int start, int[] request)
public double SSTF(int start, int[] request)
public double SCAN(int start, int[] request, boolean direction)
```



## 2 实验攻略

### 2.1 实验概述

本次实验的主体部分为外部存储器——磁盘的模拟。

在真实的计算机系统中，磁盘所占空间一般都比较大，因此在堆栈中开辟数组的方法来进行模拟并不现实。因此，我们选择使用二进制文件来模拟磁盘，将对磁盘的读写转化为对文件的读写。在这个磁盘中，我们已经实现好了基本的读写功能，大家的工作主要集中在模拟磁头结构上面。

由于磁盘的模拟比较简单，我们还加入了对磁盘调度算法的考察。但实际上，由于在这个简单的系统中（包括我们将来要实现的功能）一次只能运行一个任务，并不涉及到多任务的调度。因此，我们简化了这部分内容，你将会看到磁盘调度算法类与我们的模拟磁盘并没有太大关系。

接下来，我们将对Disk类的源码进行一个简单的解读。



### 2.2 代码导读

#### 2.2.1 代码结构

```bash
.
│  pom.xml
│  README.md
│
└─src
   ├─main
   │  └─java
   │      ├─cpu
   │      │
   │      ├─memory
   │      │  │  Memory.java
   │      │  │
   │      │  ├─cache
   │      │  │
   │      │  └─disk
   │      │          Disk.java		# 磁盘类，需要修改
   │      │          Scheduler.java	# 磁盘调度算法类，需要修改
   │      │
   │      └─util
   │
   └─test
```



#### 2.1.2 磁盘存储结构模拟

我们模拟了一个64MB大小的磁盘，规定该磁盘拥有8个磁头，每个盘面上有256个磁道，每个磁道上有64个扇区，每扇区包含512字节的数据。具体属性定义如下

```java
public static int DISK_SIZE_B = 64 * 1024 * 1024;      // 磁盘大小 64 MB

public static final int DISK_HEAD_NUM = 8;     // 磁头数
public static final int TRACK_NUM = 256;        // 磁道数
public static final int SECTOR_PER_TRACK = 64;  // 每磁道扇区数
public static final int BYTE_PER_SECTOR = 512;  // 每扇区字节数
```

注：磁盘存储容量 ＝ 磁头数 × 磁道(柱面)数 × 每道扇区数 × 每扇区字节数

同时，我们定义了一个文件作为虚拟磁盘。这样，我们就可以将磁盘里面的数据统统放到该文件中，也就可以将对磁盘的操作转化为文件的操作了。文件定义如下

```java
private static File disk_device;    // 虚拟磁盘文件
```

与cache和memory类似，我们同样使用单例模式来构造Disk类。在Disk类的构造函数中，你可以看到我们对虚拟磁盘文件进行了创建和初始化。具体可自行查看源码。

为了方便操作，在这个磁盘中，我们的数据是线性存放的。具体来说，每个扇区的数据连续存放，扇区0占据了文件的前512个字节，扇区1占据了文件的第512到第1023个字节，以此类推，ID区域、间隙、同步字节、CRC等区域我们没有进行模拟。同理，磁道与磁道之间连续，盘面与盘面之间也连续。



#### 2.1.3 磁盘读写功能模拟

我们提供了已实现好的磁盘读写方法如下

```java
public byte[] read(String addr, int len)
public void write(String addr, int len, byte[] data)
```

这两个方法要干的事情十分简单，就是在我们创建的虚拟磁盘文件中读写数据，同时调整磁头的位置。大家可以自行查看源码。

需要额外说明的是，读写方法中接收的addr参数，是二进制表示的该数据起始位置在虚拟磁盘文件中的字节数。由于真实磁盘的地址表示已经超出了本课程的范畴，因此我们用这种方法简单地模拟磁盘中的“地址”。至于计算机内存的地址表示，我们将会在虚拟内存的章节学到。



#### 2.1.4 磁头的模拟

在Disk类中，我们定义了一个磁头对象如下

```java
private final DiskHead disk_head = new DiskHead();  // 磁头
```

DiskHead类是Disk类中的一个内部类，它记录了自己当前所在的位置。核心数据结构如下

```java
private static class DiskHead {
    int track = 0;  // 当前所在磁道号
    int sector = 0; // 当前所在扇区号
    int point = 0;  // 当前所在扇区内部的字节号
}
```

同时，我们在DiskHead类中提供了seek方法和addpoint方法，用于调整磁头的位置。其中，seek方法表示每次数据读写之前将磁头移动到指定位置，addPoint表示将磁头往后移动一个字节。这样，我们就成功模拟了一个十分简单的磁头。



### 2.3 实现指导

#### 2.3.1 Disk

在Disk类的read和write方法中，我们会对DiskHead类的seek方法和addPoint方法进行调用。因此，你需要结合read和write方法的源码，理解seek方法和addPoint方法在其中起到什么作用，然后实现这两个方法。

注意，由于我们规定该磁盘有8个磁头即8个盘面，所以每个盘面的大小为8MB。在不同盘面上，磁头的位置都是相同的（具体可以看ppt上的图）。因此，在我们的模拟规则下，第0个字节、第8MB个字节、第16MB个字节（以此类推），它们的磁头位置都应该是相同的。

#### 2.3.2 Scheduler

如2.1所述，Schedule类的内容相对独立。具体来说，每个方法都会传入磁头初始磁道号与请求访问的磁道号数组，你需要计算出平均寻道长度并返回。

注意，在SCAN算法中，你将会用到磁道总数，这个数字需要与Disk类中的TRACK_NUM保持一致。