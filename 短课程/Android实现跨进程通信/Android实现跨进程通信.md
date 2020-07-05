# Android实现跨进程通信

### 一、概述

​	本课程针对Android进程间通信机制进行深入分析，彻底理清Android中进程间通信的原理，掌握Binder、AIDL、Messenger相关应用和底层实现。

- Android多进程开发的应用及注意点
- Android中跨进程通信的主要方式以及Binder机制的底层实现
- AIDL Messenger的实现原理（oneway、in、out、inout）

### 二、Android IPC通信介绍

**1、多进程基础**

- 进程是系统资源分配的最小单位
- 进程间的资源和数据互相隔离
- 进程内可以运行多个线程，线程的崩溃会影响到进程

**2、为什么需要多进程**

- 系统资源紧张（OOM）
- 应用架构臃肿（WebView、推送、后台任务）
- 一些黑科技（保活）

**3、使用多进程注意事项**

- Application 生命周期（onCreate）
- 数据共享失效（对象、单例、回调）
- 进程间通信（文件、Intent、AIDL）

**4、跨进程通信实现方式**

（1）跨进程通信定义

​	进程间通过内核提供的机制完成数据交换。称之为进程间通信（IPC）

（2）Linux跨进程通信方式（6种）

![Linux_IPC](D:\Projects\LearningProjects\JSeries\JNotes\短课程\Android实现跨进程通信\imgs\Linux_IPC.png)

- 管道

  管道分为2种，包括匿名管道和有名管道，匿名管道只能用于父子进程或者兄弟进程间通信，也就是亲缘进程。有名管道可以用于两个不同的进程，管道都是基于内存中的缓冲区来实现，大小都是4kb，同时管道在读写时都要确保对端的存在。读只能从头开始读，写入只能写入到末尾，管道中的数据只能向一个方向流动，如果需要双方相互通信，则需要建立2个管道，一般用于轻量级的通信，比如，经常在linux中用到的管道命令

- 消息队列

  存放在内核中的消息链表，因为存放在内核中，所有进程都可以去读去写，每个消息队列都有对应的消息队列符来表示，消息队列只有在内核重启或者特定的删除的情况下才会消失。消息队列写入不需要其他进程等待，进程将消息写入到消息队列中，其他进程通过消息队列去读取，这样就完成了跨进程通信。消息队列中的读取支持随机读取，也就是说想要读取特定类型的消息也是可以直接读取到的。

- 信号

  信号其实就是Linux系统中，进程间用于相互通信的一种机制。信号可以在任何时候发送给某一个进程，而无需知道另外一个进程的状态。如果当前进程处于一个未执行的状态，信号就会被内核保存起来。总的来说，信号是软件层次上对于中端机制的模拟，比如，kill -9

- 共享内存

  是整个IPC中效率最高的一种机制。通过多个进程读写同一块内存空间，内核中其实专门留了一块内存区，可以由需要访问的进程将其映射到自己私有的内存空间，进程就可以读取这一部分内存空间，而不需要数据的拷贝。这样就能大大地提高效率，但是由于多个进程共享一段内存，因此就需要某种同步机制。这就是下面需要介绍得信号量。

- 信号量

  信号量其实就是一个计数器，用于对多进程对于共享数据访问的控制，信号量的意图在于进程间同步，信号量的增减其实是原子操作，是由内核来实现的，就是操作系统中的PV操作。

- 套接字

  C/S结构，通过客户端与服务端简历socket链接，完成通信，多用于跨网络通信，同时也能完成同一机器上不同进程间的通信。

### 三、Binder AIDL Messenger

**1、Binder** 

- C/S架构，稳定性好，由于共享内存方式
- 性能较好，数据拷贝次数优于管道、消息队列、Socket
- 安全性高，UID/PID可见

**2、Binder架构**

![Binder_Arch](D:\Projects\LearningProjects\JSeries\JNotes\短课程\Android实现跨进程通信\imgs\Binder_Arch.png)

**3、Binder应用**

![Binder_example](D:\Projects\LearningProjects\JSeries\JNotes\短课程\Android实现跨进程通信\imgs\Binder_example.png)

**4、Android跨进程通信方式**

- AIDL（基于Binder）
- Intent、Messenger、ContentProvider（基于AIDL）
- 文件共享

**5、AIDL**

- 定义IPC过程中接口的一种描述语言
- AIDL文件在编译工程中生成接口的实现类，用于IPC通信
- 支持基本数据类型，实现Parcelable接口的对象，List，Map

**6、Messenger**

- 基于Handler、Message实现
- 串行实时通信
- 传输Bundle支持的数据类型

### **四、AIDL-项目实战**

**1、通过项目实战解决以下问题**

- AIDL如何实现IPC
- in、out、inout关键字的作用
- oneway关键字的作用
- AIDL如何实现callback
- 如何自己编码实现AIDL的核心功能

**2、AIDL-项目场景**

![AIDL_project](D:\Projects\LearningProjects\JSeries\JNotes\短课程\Android实现跨进程通信\imgs\AIDL_project.png)

**3、AIDL注意事项**

![Binder_attention](D:\Projects\LearningProjects\JSeries\JNotes\短课程\Android实现跨进程通信\imgs\Binder_attention.png)

- Binder客户端调用到服务端，服务端里面的方法并非在服务端主线程执行，而是在Binder线程池中执行。
- 申明了oneway的方法，表明客户端调用该方法以后，不会因为服务端的处理速度而阻塞。同时，申明了oneway的方法，不能指定返回值，如果指定了，则会出现上图中编译报错。
- 如果以实体对象作为Binder通信的参数，需要申请aidl接口

**4、AIDL定向tag**

​	定向tag是AIDL中语法的一部分，其中in、out、inout是三个定向tag。官网定义：

```
All non-primitive parameters require a directional tag indicating which way the data goes . Either in , out , or inout . Primitives are in by default , and connot be otherwise .
```

​	表明所有非基本数据类型的参数都需要用一个定向tag来说明数据的走向，包括：in、inout、out。基本数据类型默认是in，而且不能是其他的tag。

- **in：**表示数据只能由客户端流向服务端。服务端将受到客户端对象完整数据，但是客户端对象不会因为服务端对传参的修改而发生变动。
- **out：**数据只能由服务端流向客户端。服务端将会受到客户端对象，该对象不为空，但是里面的字段为空，但是在服务端对该对象作任何修改客户端的传参对象都会同步修改。
- **inout：**数据在服务端和客户端之间双向流通。服务端将会受到客户端传来的对象的完整信息，并且客户端将会同步服务端对该对象的任何变动。

### 五、Android IPC通信实战之Messenger

**1、问题**

- Messenger的使用场景
- Messenger如何实现IPC通信
- Messenger底层实现一来了哪些关键技术

**2、使用注意事项**

​	使用Messenger进行通信时，在Handler中解析数据时，一定要设置

```
bundle.setClassLoader(Message.class.getClassLoader());
```

​	否则就会抛出以下的异常

![Messenger_exception](D:\Projects\LearningProjects\JSeries\JNotes\短课程\Android实现跨进程通信\imgs\Messenger_exception.png)

### 六、总结

- IPC机制、Binder架构
- AIDL实现跨进程通信以及关键字的作用
- Messenger的使用和底层实现

### 七、附录

- 代码实现位于com.jeremy.jnotes.shortcourse.ipc下面

![AIDL_CODE](D:\Projects\LearningProjects\JSeries\JNotes\短课程\Android实现跨进程通信\imgs\AIDL_CODE.png)