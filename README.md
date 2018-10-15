# JVM

JVM内存模型
![](https://i.imgur.com/4yiXJ4K.png)

<pre>
展望Java技术的未来
   1）模块化
   2）混合语言
   3）多核并行
   5）语法丰富
   6）64位虚拟机
</pre>

<pre>
编译JDK
</pre>

<pre>
Java内存区域
   运行时数据区域
	   1）程序计数器
	   2）Java虚拟机栈
	   3）本地方法栈
	   5）Java堆
	   6）方法区
	   7）运行时常量池
	   8）直接内存
    Hotspot
       1) HotSpot虚拟机对象
    OutOfMemory异常
       1）Java堆溢出
       2）虚拟机栈和本地方法栈溢出
       3）方法区和运行时常量池溢出
       5）本机直接内存溢出
</pre>
<pre>
垃圾收集器
   1）对象已死判断
       1）引用计数算法
       2）可达性分析算法
   2）垃圾收集算法
       1）标记清除算法
       2）复制算法
       3）标记整理算法
       5）分代收集算法
   3）垃圾收集器
       1）Serial收集器
       2）Parnew收集器
       3）Parallel Scavenge收集器
       5）Serial Old收集器
       6）Parallel Old收集器
       7）CMS收集器 
       8）G1收集器
       9）GC日志分析
       10）垃圾收集器参数分析
    5）内存分配与回收策略
       对象优先在Eden区分配
       大对象直接进入老年代
       长期存活的对象直接进入老年代
       动态对象年龄判断   
</pre>

<pre>
虚拟机性能监控与故障处理工具
    1）jps
    2) jstat
    3) jinfo
    5) jmap
    6) jhat
    7) jstack
    8) jconsole
</pre>

<pre>
调优案例分析
</pre>

<pre>
  1) Class文件结构
  2) 字节码指令
</pre>


<pre>
类的加载机制
  1）加载
  2）验证
  3）准备
  5) 解析
  6）初始化
类加载的双亲委派机制
</pre>

<pre>
程序编译与代码优化
</pre>

<pre>
Java的内存模型与线程
   1）线程的实现
   2）线程调度
   3）状态转换
</pre>

<pre>
线程安全与锁优化
</pre>