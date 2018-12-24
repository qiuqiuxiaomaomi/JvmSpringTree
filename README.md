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

![](https://i.imgur.com/bxBUH8D.jpg)

![](https://i.imgur.com/MhNKVKC.jpg)

![](https://i.imgur.com/KtLoNzR.jpg)

![](https://i.imgur.com/wK83ocO.jpg)

<pre>
Java内存区域
   运行时数据区域
	   1）程序计数器
          程序计数器是一块较小的内存空间，它可以看做是当前线程所有执行的字节码的行号指示器。字节码计时器的工作就是通过改变这个计数器的值来选取下一条徐亚哟执行的字节码指令，分支，循环，跳转，异常处理。
          每个线程都有自己独立的程序计数器
              1）如果当前正在执行的是一个java方法，这个计数器记录的就是正在执行的字节码指令的地址
              2）如果正在执行的是native方法，那么程序计数器值则为Undefined 

	   2）Java虚拟机栈
          每个线程都有自己独立的Java虚拟机栈,生命周期与线程相同
          异常情况
              1）如果线程请求的栈深度大于虚拟机所允许的深度，将跑出StackOverflowError
              2）如果虚拟机栈可以动态扩展，如果扩展无法申请到足够的内存，就会抛出OutOfMemoryError

	   3）本地方法栈

	   5）Java堆
          Java堆是被所有线程共享的区域,在虚拟机启动时创建。
          所有的对象，数组都要在堆上分配，但是随着JIT编译器的发展和逃逸分析技术逐渐成熟，栈上分配，标量替换优化技术将会导致了一些微妙的变化发生，所有的对象都分配在堆上也逐渐变得不是那么绝对了。

	   6）方法区
          方法区与JAVA堆一样，是各个线程共享的内存区域，它用于存储已经被虚拟机加载的类信息，常量，静态变量，及时编译后的代码等数据。Java规范将方法区描述为堆的一个逻辑部分。

	   7）运行时常量池
          运行时常量池是方法区的一部分，Class文件中除了有类的版本，字段，方法，接口等描述信息外，还有一项信息是常量池，用于存放编译器生成的各种字面量和符号引用，这部分内容将在类加载后进入方法区的运行时常量池中存放。

	   8）直接内存
          直接内存并不是虚拟机运行时数据区的一部分，也不是Java虚拟机规范中定义的内存区域，但是会频繁的被程序使用。例如NIO，可直接在堆外分配内存。

    对象的创建
       jvm遇到一条new指令时，首先将去检查这个指令的参数是否能在常量池中定位到一个类的符号引用，并且检查这个符号引用代表的类是否
	   已经被加载，解析，初始化过，如果没有，那必须先执行相应的类加载过程
 
          在类加载检查通过后，接下来JVM将为新生对象分配内存，对象所需的内存大小在类加载完成之后便可确定，为对象分配空间等
       同于把一块确定大小的内存从JAVA堆中划分出来。
          1) 如果JAVA堆中内存是绝对规整的，所有用过的内存都放在一边，空闲的内存放在另一边，中间放着一个
       指针作为分界点的指示器，那所分配内存就仅仅是把那个指针向空闲空间挪动一段与对象大小相等的距离，这种分配方式称之为指针碰撞
          2) 如果JAVA堆中内存并不是规整的，已经使用的内存和空闲的内存相互交错，那就没有办法简单的进行指针碰撞了，JVM就必须维护一个列表,记录上哪些内存块是可用的，在分配的时候从列表中找到一块足够大的空间划分给对象实例，并更新列表上的记录，这种分配方式称之为空闲列表。

          选择哪一种分配方式由JAVA堆是否规整决定，而JAVA堆是否规整又由所采用的的垃圾收集器是是否带有压缩整理功能决定，因此，在使用Serial, Parnew等等Compact过程的收集器时，系统采用的分配算法是指针碰撞，而使用CMS这种基于MARK-SWEEP算法收集器时，通常采用空闲列表。

          内存分配的原子性
          在并发情况下，可能出现正在给对象A分配内存，指针还没来得及修改，对象B又使用了原来的指针分配内存的情况
	      解决方案
             1）CAS配上重试机制保证更新操作的原子性
             2）把内存分配的动作按照线程划分在不同的空间中进行，即每个线程在JAVA堆中预先分配一小块内存，称之为本地线程分配缓冲（TLAB），哪个线程要分配内存，就是在哪个线程的TLAB上分配，只有TLAB用完并分配新的TLAB时，才需要同步锁定。
   
          内存分配完成后，JVM需要将分配到的内存空间都初始化为0值，如果是TLAB这一工作可以提前到TLAB分配时进行。这一操作保证了对象的实例字段在JAVA代码中不赋值就直接使用，程序能访问到这些字段的数据类型所对应的0值。	

          对象的内存布局
            1）对象头
	           第一部分用于存放对象自身的运行时数据
		          1）hash码
			      2）GC分代年龄
                  3）锁状态标志
                  5）线程持有的锁
                  6）偏向线程ID
                  7）偏向时间戳
			      这部分称之为MARK WORD
		       第二部分是类型指针
		         即对象指向它的元数据的指针，虚拟机通过这个指针来确定这个对象是哪个类的实例，并不是所有的虚拟机实现都必须在对象数据上保留类型指针。
            2）实例数据
                 各种类型的字段内容，无论是父类继承下来的，还是子类自有的。
            3）对齐填充	
                 并不是必须的，HotSpot VM的内存管理系统要求内存管理系统要求对象的起始地址必须是8字节的整数倍。
    对象的访问定位
        1）使用句柄访问
        2）使用直接内存访问
        HotSpot采取第二种方式访问对象。

    生存还是死亡：
        即使在可达性算法中不可达的对象，也并非是非死不可的，这时候它们暂时处于缓刑阶段，要真正宣告一个对象死亡，至少要经历两次
        标记过程：
           如果对象在进行可达性分析后发现没有与GC ROOTS相连接的引用链，那它将会被第一次标记并且进行一次筛选，筛选的条件是次对象是否有必要执行finalize()方法，当对象没有覆盖finalize()方法，或者finalize()方法已经被JVM调用过，虚拟机将这两种情况都视为"没有必要执行

    "方法区回收"
        在堆中，尤其是在新生代中，常规应用进行一次垃圾收集一般可以进行一次垃圾收集一般可以回收70%-95%的空间，而永久代的垃圾收集效率比较低。

    永久代的垃圾收集主要回收两部内容：
        1）废弃常量
        2）无用的类
	       1）该类所有的实例都已经回收，也就是JAVA堆中不存在该类的任何实例
		   2）加载该类的ClassLoader已经被回收
		   3）该类对应的java.lang.Class对象没有在任何地方被引用，无法在任何地方通过反射访问该类的方法。

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
          引用计数法，目前微软的COM，Python，Sequirrel都在用这种方式管理内存
          缺陷：
             很难解决对象之间循环引用的问题

       2）可达性分析算法
          目前主流的商用程序语言的主流实现都是使用可达性分析来判定对象是否存活的。这个算法的基本思想是通过一系列的称为"GC ROOTS"
       的对象作为起始点，从这些节点开始向下搜索，搜索走过的路径称为引用链，当一个对象到GC ROOTS没有任何的引用链时，则证明这个对象是不可达的。

       GC ROOTS的对象包括：
         1）虚拟机栈（栈帧中的本地变量表）中引用的对象
	     2）方法区中类静态属性引用的对象
	     3）方法区中常量引用的对象
	     5）本地方法中JNI引用的对象。
       
       引用的分类
	    1）强引用
		    类似于 Object obj = new Object()
		2）软引用
	        用来描述一些还有用但并非必须的对象，对于软引用关联着的对象，在系统将要发生内存溢出异常前，将会把这些对象列入
	    回收范围之中进行第二次回收，如果这次回收还是没有足够的内存才会跑出内存溢出异常。
	    3）弱引用
	        弱引用也是用来描述非必须对象的，但是它的强度比软引用更弱一些，被弱引用关联的对象只能生存到下一次垃圾收集发生之前。当
	    垃圾收集器工作时，不管当前内存是否足够，都会回收掉弱引用关联的对象。
	    5）虚引用
	        虚引用也称为幽灵引用或者幻影引用，它是最弱的一种引用关系，一个对象是否有虚引用的存在，完全不会对其生存时间构成影响。
	    为一个对象设置为虚引用关联的唯一目的就是能在这个对象呗收集器回收的时候收到一个系统通知。

   2）垃圾收集算法
       1）标记清除算法
             两阶段：
                 1）标记阶段
	             2）清除阶段
             不足：
                 1）效率问题，标记和清除两个过程的效率都不高
                 2）空间问题，标记清除之后会产生大量不连续的内存碎片，空间碎片太多可能会导致以后在程序运行过程中需要分配较大对象时，无法找到足够的连续内存而不得不提前触发另一次垃圾收集动作。

       2）复制算法
          复制算法：
             它将可用内存按照容量划分为大小相等的凉快，每次只使用其中的一块，当这一块的内存用完了，就将还存活着的对象复制到另外一块上，然后再把已使用过的内存空间一次清理掉，这样使得每次都是对整个半区进行内存回收，内存分配时也就不用考虑内存碎片等复杂情况，只要移动堆顶指针，按顺序分配内存即可，实现简单，运行高效，只是这种算法的代价是将内存缩小为原来的一半，未免太高了些。

             HotSpot虚拟机默认的Endian和Survivor的大小比例为8:1	

       3）标记整理算法
          复制收集算法在对象存活率较高时就要进行较多的复制操作，效率将会变低，以应对被使用的内存中所有对象100%存活的极端对象，所以在老年代一般不能直接使用这种算法。

       5）分代收集算法
          只是根据对象存活周期的不同将内存划分为几块，一般是把Java堆分为新生代和老年代，这样就可以根据各个年代的特点采用最适当的手机算法，，在新生代中，每次垃圾收集时都发现有大批对象死去，只有少量存活，那就选用复制算法，只需要付出少量存活对象的复制成本就可以完成收集，而老年代中因为对象存活率高，没有额外空间对它进行分配担保，就必须使用标记-清理或者标记-整理算法进行回收。  

       HotSpot的实现
          1）可以作为GC ROOTS的节点主要在全局性的引用中，现在很多应用仅仅方法区就有数百兆，如果逐个检查这里面的引用，那么必然会消耗很多的时间。
	      2）可达性分析对执行时间的敏感还体现在GC停顿上，因为这项分析工作必须在一个能确保一致性的快照中进行----这里一致性的意思是指在整个分析周期中，整个执行系统看起来就像被冻结在某个时间点上，不可以出现分析过程中对象引用关系还在不断变化的情况。这一点导致GC进行时必须停顿所有JAVA执行线程（Stop the World）的其中一个重要原因，

          HotSpot使用OopMap的数据结构来存放对象引用
              安全点：
                 在OopMap的协助下，HotSpot可以快速且准确的完成GC ROOTS枚举     

   3）垃圾收集器
       1）Serial收集器
          特点： 1）单线程收集器， 一个CPU，一条收集线程，
	            该收集器是虚拟机运行在Client模式下的默认新生代收集器。
	            相对于其他收集器：
                    1）简单而高效（相对于单线程）

       2）Parnew收集器
          ParNew收集器其实就是Serial收集器的多线程版本
          ParNew收集器除了多线程收集之外，其他与Serial收集器变比没有太多创新之处，但它却是许多运行在Server模式下的虚拟机中首选的新生代收集器

       3）Parallel Scavenge收集器
          是一个新生代收集器，它也是使用复制算法的收集器，有事并行的多线程收集器(标记-整理算法)
          不同之处：
              Parallel Scavenge收集器的特点是它的关注点与其他收集器不同，CMS收集器的关注点是尽可能地缩短垃圾收集时用户线程的停顿时间，而Parallel Scavenge收集器的特点是它的关注点与其他收集器不同，CMS收集器的关注点是尽可能地缩短垃圾收集时用户线程的停顿时间，而Paralle Scavenge收集器的目标则是达到一个可控制的吞吐量，吞吐量都是CPU运行用户代码时间与垃圾收集时间的比值。			

       5）Serial Old收集器
          Serial Old收集器是Serial收集器的老年代收集版本。
        
       6）Parallel Old收集器
          标记-整理算法,Parallel Scavenge收集器的老年代版本

       7）CMS收集器 
          CMS收集器是一种以获得最短回收停顿时间为目标的收集器。目前很大一部分的Java应用尤其重视服务的相应速度，希望系统停顿时间最短，CMS收集器非常适合这类场景。
              1）初始标记(stop the world)
                 标记GC ROOTS能直接关联到的对象，速度很快，
              2）并发标记
                 进行GC tracing的过程，
              3）重新标记(stop the world)
                 修正并发标记期间因为用户程序继续运作而导致标记产生变动的那一部分对象。
              5）并发清除 

          CMS收集器的三个缺点：
              1）CMS收集器对CPU资源非常敏感
	          2）CMS收集器无法处理浮动垃圾
	             "浮动垃圾"：由于CMS收集器在并发清理阶段用户线程还在运行着，伴随着程序运行自然还会有新的垃圾不断生成，这一部分垃圾出现在标记过程之后CMS无法在当次收集中处理掉它们，只好留待下一次GC时再清理掉，这一部分垃圾就称为浮动垃圾。
	          3）CMS收集器是标记清除算法实现的收集器，收集结束后会有大量空间碎片，空间碎片过多时，将会给大对象分配带来很大麻烦，往往会出现老年代还有很大空间剩余，但是无法找到足够大的连续空间来分配当前对象，不得不提前触发一次FULL GC，为了解决这个问题，CMS提供了压缩开关+UseCMSCompactAtFullCollection开关参数，用于在CMS收集器顶不住要进行Full GC时开启内存碎片的合并整理过程，内存整理的过程无法并发的，空间碎片问题没有了，但是停顿时间不得不边长。

       8）G1收集器
          G1(Garbage First)收集器是当今收集器技术发展的最前沿成果之一，
             1）并行与并发
             2）分代收集
             3）空间整合 基于标记-整理算法，不会产生内存空间碎片
             4）可预测的停顿
                降低停顿时间是G1和CMS收集器的共同关注点，但是G1除了追求低停顿外，还能建立可预测的停顿时间模型。垃圾收集不再区分新生代，老年代。

       9）GC日志分析
       10）垃圾收集器参数分析
    5）内存分配与回收策略
          1) 对象优先在Eden区分配
          2) 大对象直接进入老年代
          3) 长期存活的对象直接进入老年代
       动态对象年龄判断   
</pre>

<pre>
虚拟机性能监控与故障处理工具
    1）jps
       虚拟机进程状况工具    
    2) jstat
       虚拟机统计信息监控工具
    3) jinfo
       Java配置信息工具
    5) jmap
       Java内存映像工具
    6) jhat
       虚拟机堆转储快照分析工具
    7) jstack
       Java堆栈跟踪工具
    8) jconsole
       Java监视与管理控制台
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

Tomcat Servlet

![](https://i.imgur.com/gtp5iLR.jpg)

Servlet原理解析
Tomcat系统架构

![](https://i.imgur.com/zWiOxdW.jpg)

Jetty系统架构![](https://i.imgur.com/O1D1enP.jpg)

<pre>
Tomcat VS Jetty

  架构比较：
    从架构上来说Jetty比Tomcat更简单
    Jetty架构：
         所有组件都是基于Handler来实现的，主要的功能扩展都是基于Handler来实现;	
    Tomcat架构：
	     以多级容器构建起来，
	
       从设计模板来说，Jetty中的Handler的设计实际上就是一个责任链模式，另外一个用到的设计模式就是观察者模式，Jetty很容易的被扩展和裁剪。
       相比之下，Tomcat臃肿很多，Tomcat的整体设计更加复杂，Tomcat的核心是它的容器设计，使得开发人员如果想要扩展TOMCAT,就必须先要了解TOMCAT的整体结构，然后才能按照它的规范来扩展，从表面上看，Tomcat的功能要比Jetty强大，因为Tomcat帮你做了很多工作，而JETTY只告诉你能怎么做，如何做由你去实现。

    性能比较：
        Tomcat在处理少数非常繁忙的连接上更有优势，也就是说连接的生命周期短，Tomcat总体性能更高
        Jetty正好相反，Jetty可以同时处理大量连接而且可以长时间保持这些连接，例如一些WEB聊天应用就非常适合用Jetty，淘宝
             的旺旺就用Jetty
        由于Jetty的架构非常简单，作为服务器它可以按需加载组件，这样不需要的就可以去掉，无形中可以减少服务器本身的内存开销，处理一次请求也可以减少产生的零时对象这样性能也会提高。 Jetty默认使用NIO技术，在处理 IO请求上更占优势，而Tomcat模式使用BIO，在
	    处理静态资源时，Tomcat的性能较差。
</pre>

Spring 框架

![](https://i.imgur.com/ACrPuI2.jpg)

![](https://i.imgur.com/M29bUdD.jpg)

<pre>
  Spring的设计理念
     其实Spring就是面向Bean的编程。Bean在Spring中才是真正的主角。

     Spring解决了一个非常关键的问题，它可以让你把对象之间的依赖关系转而用配置文件来管理，也就是它的依赖注入机制，而这个注入关系在一个叫IOC的容器中管理，而在IOC容器中又是被Bean包裹的对象，Spring正是通过把对象包装在BEAN中从而达到管理这些 对象以及一些列额外操作的目的。

  核心组件如何协同工作：
    如果把Bean比作一场演出的演员，Context就是这场演出的舞台背景，而Core就是演出的道具了，
	Context就是Bean关系的集合，这个关系集合又叫IOC容器
	Core就是发现，建立维护每个Bean之间的关系所需要的一系列工具

  Bean组件：
    org.springframework.beans包下。这个包下的所有类主要解决三件事情。
	1）Bean的定义
	2）Bean的创建
	3）Bean的解析
	Bean的创建是典型的工厂模式

  Context组件：
     org.springframework.context包下。
	 ApplicationContext是Context的顶级父类，ApplicationContext继承了BeanFactory，这也说明了Spring容器中运行的主要对象是Bean，另外ApplicationContext
	 继承了ResourceLoader，是的ApplicationContext可以访问任何外部资源。
	 总体来说ApplicationContext必须完成以下几件事情：
            1）标识一个应用环境
            2）利用BeanFactory创建Bean对象
            3）保存对象关系表
            5）能够捕获各种事件
        Context作为Spring的IOC容器 ，基本上整合了Spring的大部分功能，或者说是大部分功能的基础。	

  Core组件
    Core组件作为Spring的核心组件，其中包含了很多关键类，一个重要的组成部分就是定了资源的访问方式。
    Context组件把资源的加载，解析，描述工作委托给了ResourcePatternResolver类来完成，它把资源的加载，解析，资源的定义整合
  在一起便于其他组件使用。	

</pre>

![](https://i.imgur.com/Au9D1V0.jpg)

<pre>
IOC容器：
    IOC容器实际上是Context组件结合Core,Bean组件共同构建的一个Bean的关系网。
	如果构建关系网，就在AbstractApplicationContext类的refresh方法

IOC容器的扩展性
    对Spring的Ioc容器来说，主要有BeanFactoryPostProcessor和BeanPostProcessor，它们分别在构建BeanFactory和构建Bean对象时调用，还有就是InitializingBean和DisposableBean，它们分别在Bean实例创建和销毁时调用，用户可以实现在这些接口中定义的方法，Spring会在适当的时候调用它们，这些扩展点通常也是我们使用Spring来完成特定任务的地方。Spring的所有特性功能都是基于IOC容器工作的。	

代理

Spring AOP

</pre>

SpringMVC

DispatcherServlet初始化工作

![](https://i.imgur.com/6nBGU8L.jpg)
<pre>
DispatcherServlet初始化工作
   DispatcherServlet类继承了HttpServlet，在Servlet的init方法调用时DispatcherServlet执行SpringMVC的初始化工作
   1) initMultipartResolver：初始化MultipartResolver，用于处理文件上传服务，如果有文件上传，会将当前的HttpServletRequest包装成
   2) DefaultMultipartHttpServletRequest，并且将每个上传的内容封装成CommonsMultipartFile对象。
   3) initLocaleResolver：用于处理应用的国际化问题，通过解析请求的Locale和设置相应的Locale来控制应用中的字符编码问题。
   5) initThemeResolver: 用于定义一个主题
   6) initHandlerMappings:用于定义用户设置的请求映射关系，例如将用户请求的URL映射程一个个Handler。
                   	默认有BeanNameUrlHandlerMapping和DefaultAnnotationHandlerMapping
   7) initHandlerAdapters:用于根据Handler的类型定义不同的处理机制，例如 定义中将URL映射程一个Controller实例，
   8) initHandlerExceptionResolvers:当Handler处理出错时，会通过这个Handler来统一处理，默认的实现类是                           SimpleMappingExceptionResolver,将错误日志记录在log文件中，并且转到默认的错误页面
   9) initRequestToViewNameTranslators：将指定的viewName按照定义的RequstToViewNameTranlator替换成想要的格式，如加上前缀或者后缀。
   10) initViewResolvers:用于将View解析成页面。
</pre>

Mybatis框架系统

![](https://i.imgur.com/wWnyASW.jpg)

<pre>
Mybatis框架系统
    Mybatis通过SQL MAP将JAVA对象映射成SQL语句，将结果集再转化成Java对象。
    Mybatis主要完成两件事情
        1）根据JDBC规范建立与数据库的连接。
        2）通过反射打通Java对象与数据库参数交互之间相互转化的关系。

    Mybatis框架的一个重要组成部分就是SqlMap配置文件，SqlMap配置文件是Statement语句。
</pre>

![](https://i.imgur.com/XEkX16N.jpg)

![](https://i.imgur.com/u3WuaoT.jpg)

![](https://i.imgur.com/06acNKu.jpg)

CDN 静态化失效问题

![](https://i.imgur.com/hdqITkM.jpg)

CDN分级

![](https://i.imgur.com/klP2bdK.jpg)

<pre>
大浏览量系统的静态化架构设计
    Java系统的弱点：
         1）不擅长处理大量的连接请求，每个连接消耗的内存较多
         2）Servlet容器解析HTTP较慢
    1）如何改造动态系统
         1）动静分离
         2）动态内容结构化
		 3）组装动态内容：ESI, CSI
		 
	2）几种静态化方案的设计与选择
         1）Nginx + Cache + Java结构的虚拟机单机部署	
            优点：
               1）没有网络瓶颈，不需要改造网络
	           2）机器增加，也没有网卡瓶颈
	           3）机器数增多，故障风险减少
            缺点：
               1）机器增加，缓存命中率下降
               2）缓存分散，失效难度增加
               3）Cache和JBOSS都会争抢内存

         2) Nginx + Cache + Java结构实体机单机部署
            优点:
               1）既没有网络瓶颈，也能使用大内存
	           2）减少Varnish机器，提高命中率
	           3）提升命中率，能较少GZIP压缩
	           5）减少Cache失效的压力
         3) CDN化
	        实现CDN的分级
</pre>

<pre>
Spring的注解机制

      @Configuration处理器 ConfigurationClassPostProcessor
      @Autowired, @Value, @Inject 处理器 AutowiredAnnotationBeanPostProcessor
      @Required处理器 RequireBeanPostProcessor
      @PostConstruct, @PreDestroy, @Resource 处理器CommonAnnotationBeanPostProcessor
      PersistenceAnnotationBeanPostProcessor
      @EventListener处理器 EventListenerMethodProcessor

      Spring框架的核心就是IOC,通过controller一类注解的bean的实例化过程可以大体总结spring注解的工作原理：

       1）利用asm技术扫描class文件，转化成Springbean结构，把符合扫描规则的（主要是是否有相关的
          注解标注，例如@Component）bean注册到Spring 容器中beanFactory
       2）注册处理器，包括注解处理器
       3）实例化处理器（包括注解处理器），并将其注册到容器的beanPostProcessors列表中
       5）创建bean的过程中，属性注入或者初始化bean时会调用对应的注解处理器进行处理
</pre>

<pre>
SpringBean的生命周期：

      1）Instance实例化
      2）设置属性值
      3）调用BeanNameAware的setBeanName方法
      5）调用BeanPostProcessor的预初始化方法
      6）调用InitializationBean的afterPropertiesSet方法
      7）调用定制的初始化方法 callCustom的init-method
      8）调用BeanPostProcessor的后初始化方法
      9）Bean可以使用了
      10）容器关闭
      11）调用DisposableBean的destroy方法
      12）调用定制的销毁方法CallCustom的destroy-method方法
</pre>

BeanFactory的继承关系

![](https://i.imgur.com/8zXVojV.png)

BeanFactory 与 ApplicationContext

![](https://i.imgur.com/3u4t1wX.png)

<pre>
        ApplicationContext继承了BeanFactory，BeanFactory是Spring中比较原始
    的Factory，它不支持AOP、Web等Spring插件，而ApplicationContext不仅包含
    了BeanFactory的所有功能，还支持Spring的各种插件，还以一种面向框架的方式工作
    以及对上下文进行分层和实现继承。

        BeanFactory是Spring框架的基础设施，面向Spring本身；而ApplicationContext
    面向使用Spring的开发者，相比BeanFactory提供了更多面向实际应用的功能，几乎所有场合
    都可以直接使用ApplicationContext而不是底层的BeanFactory。
</pre>

BeanFactory中Bean的生命周期

![](https://i.imgur.com/P7WevC5.png)

ApplicationContext中Bean的生命周期

![](https://i.imgur.com/ylihR1P.png)



Spring中BeanFactory和ApplicationContext的生命周期及其区别详解

https://blog.csdn.net/qq_32651225/article/details/78323527

Spring源码分析-深入浅出AOP(图文分析)

https://blog.csdn.net/c_unclezhang/article/details/78769426


<pre>
Spring的自动配置技术与源码分析
</pre>

<pre>
@Profile注解的功能
</pre>

![](https://i.imgur.com/6KwWcgX.png)

<pre>
Aware接口

      在实际项目中，你不可避免的要用到Spring 容器本身的功能资源，这时你的Bean 必须要意识
      到Spring 容器的存在，才能调用Spring 所提供的资源，这就是所谓的Spring Aware。

      其实Spring Aware 本来就是Spring设计用来框架内部使用的，若使用了Spring Aware ，
      你的Bean 将会和Spring 框架糯合。

      Spring Aware 的目的是为了让Bean 获得Spring 容器的服务。因为ApplicationContext 
      接口集成了MessageSource 接口、ApplicationEventPublisher 接口和
      ResourceLoader 接口，所以Bean 继承ApplicationContextAware 可以获得Spring 
      容器的所有服务，原则上用到什么接口，就实现什么接口！
</pre>

![](https://i.imgur.com/Nurutxz.png)

<pre>
ImportSelector

     用法应该是这样：
        1)定义一个Annotation, Annotation中定义一些属性，到时候会根据这些属性的不同返
          回不同的class数组。
        2)在selectImports方法中，获取对应的Annotation的配置，根据不同的配置来初始化
          不同的class。
        3)实现ImportSelector接口的对象应该是在Annotation中由@Import Annotation来
          引入。这也就意味着，一旦启动了注解，那么就会实例化这个对象。

     使用场景：
             比如我有一个需求，要根据当前环境是测试还是生产来决定我们的日志是输出到本地
         还是阿里云。

             那假设我定义两个logger：LoggerA 和 LoggerB ,两个logger实现 Logger接口。
         我们再定义一个Annotation。
</pre>

<pre>
@Lazy懒加载

      单实例默认在容器启动的时候创建。
      懒加载是在类第一次使用的时候创建。
</pre>

![](https://i.imgur.com/TQvqqYR.png)

<pre>
@Condition
@Autowired(require=false)
@Primary
</pre>

<pre>
Processor
     例如：
         BeanPostProcessors
</pre>

![](https://i.imgur.com/dejtpZC.png)

<pre>
AOP
</pre>

![](https://i.imgur.com/q01gp9B.png)

![](https://i.imgur.com/lEt7vEn.png)

![](https://i.imgur.com/KpssM1b.png)

<pre>
异步处理：

      DeferredResult和Callable都是为了异步生成返回值提供基本的支持。简单来说就是一个请求进来，如果你使用了DeferredResult
      或者Callable，在没有得到返回数据之前，DispatcherServlet和所有Filter就会退出Servlet容器线程，但响应保持打开状态，一
      旦返回数据有了，这个DispatcherServlet就会被再次调用并且处理，以异步产生的方式，向请求端返回值。

      1）DeferredResult

      2）Callable

      理解Callable 和 Spring DeferredResult
      https://www.cnblogs.com/aheizi/p/5659030.html
</pre>

<pre>
TaskExecutor
</pre>

父子容器

![](https://i.imgur.com/eFxk3XT.png)

![](https://i.imgur.com/pZqrLj4.png)

<pre>
Servlet与DispatchServlet
</pre>

![](https://i.imgur.com/qs63qtZ.png)

源码解读

![](https://i.imgur.com/xW4liQD.png)

![](https://i.imgur.com/D34TOuh.png)

![](https://i.imgur.com/zlCMIor.png)

![](https://i.imgur.com/ndbRwav.png)

![](https://i.imgur.com/9EHWo4g.png)

![](https://i.imgur.com/ndY3b8s.png)

![](https://i.imgur.com/hJNUBzp.png)

![](https://i.imgur.com/7H3qlEI.png)

![](https://i.imgur.com/VOCCH6N.png)

![](https://i.imgur.com/HevcKJc.png)

![](https://i.imgur.com/s5xV7hB.png)

![](https://i.imgur.com/DHNeuFX.png)

![](https://i.imgur.com/OMCBzdr.png)

![](https://i.imgur.com/UMH26XB.png)

![](https://i.imgur.com/aJqvU88.png)

![](https://i.imgur.com/sdGwooe.png)

<pre>
Spring初始化流程
</pre>