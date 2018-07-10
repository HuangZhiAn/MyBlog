
<!-- MarkdownTOC autolink="true" -->

- [Preface](#preface)
- [1. Jstack初识](#1-jstack%E5%88%9D%E8%AF%86)
	- [1.1 Java线程状态](#11-java%E7%BA%BF%E7%A8%8B%E7%8A%B6%E6%80%81)
	- [1.2 使用方式](#12-%E4%BD%BF%E7%94%A8%E6%96%B9%E5%BC%8F)
	- [1.3线程快照分析工具](#13%E7%BA%BF%E7%A8%8B%E5%BF%AB%E7%85%A7%E5%88%86%E6%9E%90%E5%B7%A5%E5%85%B7)
- [2. 使用实例](#2-%E4%BD%BF%E7%94%A8%E5%AE%9E%E4%BE%8B)
	- [2.1 使用Jstack找出最耗cpu的线程](#21-%E4%BD%BF%E7%94%A8jstack%E6%89%BE%E5%87%BA%E6%9C%80%E8%80%97cpu%E7%9A%84%E7%BA%BF%E7%A8%8B)
	- [2.2 项目中使用示例](#22-%E9%A1%B9%E7%9B%AE%E4%B8%AD%E4%BD%BF%E7%94%A8%E7%A4%BA%E4%BE%8B)

<!-- /MarkdownTOC -->

## Preface

Jstack使用和线程状态分析

## 1. Jstack初识

JDK自带工具，在JDK5开始提供，可以打印指定进程中线程运行的状态，包括线程数量、是否存在死锁、资源竞争情况和线程的状态等等

### 1.1 Java线程状态

引用自[https://www.cnblogs.com/kongzhongqijing/articles/3630264.html](https://www.cnblogs.com/kongzhongqijing/articles/3630264.html)
想要通过jstack命令来分析线程的情况的话，首先要知道线程都有哪些状态，下面这些状态是我们使用jstack命令查看线程堆栈信息时可能会看到的线程的几种状态：  

 - **NEW**，未启动的。不会出现在Dump中。  
 - **RUNNABLE**，在虚拟机内执行的。运行中状态，可能里面还能看到locked字样，表明它获得了某把锁。  
 - **BLOCKED**，,受阻塞并等待监视器锁。被某个锁(synchronizers)給block住了。  
 - **WATING**，无限期等待另一个线程执行特定操作。等待某个condition或monitor发生，一般停留在park(), wait(), sleep(),join() 等语句里。  
 - **TIMED_WATING**，有时限的等待另一个线程的特定操作。和WAITING的区别是wait() 等语句加上了时间限制 wait(timeout)。  
 - **TERMINATED**，已退出的。
|状态|描述|
|:----:|----|
|-F|当`'jstack pid'`没有相应的时候强制打印栈信息一般情况不需要使用.|
|-l|长列表. 打印关于锁的附加信息，一般情况不需要使用.|
|-m|打印java和native c/c++框架的所有栈信息.可以打印JVM的堆栈,显示上Native的栈帧，一般应用排查不需要使用|
|-h|打印帮助信息.|
|-help|同-h|

### 1.2 使用方式
命令格式

    jstack [ option ] pid
  
  **pid**是Java进程PID  
**Option**参数

|参数|描述|
|:----:|----|
|-F|当`'jstack pid'`没有相应的时候强制打印栈信息一般情况不需要使用.|
|-l|长列表. 打印关于锁的附加信息，一般情况不需要使用.|
|-m|打印java和native c/c++框架的所有栈信息.可以打印JVM的堆栈,显示上Native的栈帧，一般应用排查不需要使用|
|-h|打印帮助信息.|
|-help|同-h|

### 1.3线程快照分析工具

Jstack生成的线程快照是文本的格式，看起来比较困难，可以使用线程分析工具，如：[IBM Thread and Monitor Dump Analyze for Java](https://www.ibm.com/developerworks/community/groups/service/html/communitystart?communityUuid=2245aa39-fa5c-4475-b891-14c205f7333c)  
在线工具：[http://spotify.github.io/threaddump-analyzer](http://spotify.github.io/threaddump-analyzer/)  
使用工具可以更快更清晰的看清线程状况

## 2. 使用实例
### 2.1 使用Jstack找出最耗cpu的线程

**第一步**先找出Java进程ID
使用jps命令

>![jsp][1]

**第二步**找出该进程内最耗费CPU的线程
可以使用以下几个命令

- ps -Lfp pid 
- ps -mp pid -o THREAD, tid, time  
- top -Hp pid

使用top -Hp pid命令

> ![jsp][2]

其中pid为**15991**的线程cpu百分比最高，将其转换为16进制 **3e77**
使用命令`Jstack <pid> | grep 3e77`

> ![jsp][3]
> 
可以看到是**ContainerBackgroundProcessor**这个类占的cpu最高
该类为tomcat内部类，所以所写程序并没有占用很多cpu，应用cpu正常

### 2.2 项目中使用示例
项目中出现 tomcat 假死的情况  
假死是因为线程没有响应，导致阻塞请求  
使用 Jstack 工具生成线程快照，查看出问题时的线程状态，分析问题原因  
使用 jps 命令查看 Tomcat 进程 pid
>![jsp](https://github.com/HuangZhiAn/MyBlog/raw/master/resource/images/jstack/jps.png)

使用jstack生成线程快照

> ![jsp](https://github.com/HuangZhiAn/MyBlog/raw/master/resource/images/jstack/jstack-pid.png)

这次没有使用快照分析工具，而是直接使用了文本编辑器查看  
查看生成的文本发现有很多处于WATING状态的线程，都在等待获取数据库连接，从而定位到了是数据库连接问题

[1]:https://github.com/HuangZhiAn/MyBlog/raw/master/resource/images/jstack/jps.png
[2]:https://github.com/HuangZhiAn/MyBlog/raw/master/resource/images/jstack/top_Hp-pid.png
[3]:https://github.com/HuangZhiAn/MyBlog/raw/master/resource/images/jstack/jstack-pid-grep.png
<!--stackedit_data:
eyJoaXN0b3J5IjpbMTU4ODA2MTI4MSwxNzYwOTUwOTRdfQ==
-->