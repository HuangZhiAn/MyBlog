---


---

<h2 id="preface">Preface</h2>
<p>Jstack使用和线程状态分析</p>
<h2 id="jstack初识">1. Jstack初识</h2>
<p>JDK自带工具，在JDK5开始提供，可以打印指定进程中线程运行的状态，包括线程数量、是否存在死锁、资源竞争情况和线程的状态等等</p>
<h3 id="java线程状态">1.1 Java线程状态</h3>
<p>引用自<a href="https://www.cnblogs.com/kongzhongqijing/articles/3630264.html">https://www.cnblogs.com/kongzhongqijing/articles/3630264.html</a><br>
想要通过jstack命令来分析线程的情况的话，首先要知道线程都有哪些状态，下面这些状态是我们使用jstack命令查看线程堆栈信息时可能会看到的线程的几种状态：</p>
<blockquote>
<p><strong>NEW</strong>，未启动的。不会出现在Dump中。<br>
<strong>RUNNABLE</strong>，在虚拟机内执行的。运行中状态，可能里面还能看到locked字样，表明它获得了某把锁。<br>
<strong>BLOCKED</strong>，,受阻塞并等待监视器锁。被某个锁(synchronizers)給block住了。<br>
<strong>WATING</strong>，无限期等待另一个线程执行特定操作。等待某个condition或monitor发生，一般停留在park(), wait(),<br>
sleep(),join() 等语句里。<br>
<strong>TIMED_WATING</strong>，有时限的等待另一个线程的特定操作。和WAITING的区别是wait() 等语句加上了时间限制 wait(timeout)。<br>
<strong>TERMINATED</strong>，已退出的。</p>
</blockquote>
<h3 id="使用方式">1.2 使用方式</h3>
<p>命令格式</p>
<pre><code>jstack [ option ] pid
</code></pre>
<p><strong>pid</strong>是Java进程PID<br>
Option参数</p>
<ul>
<li>-F： 当 ’jstack pid’  没有相应的时候强制打印栈信息一般情况不需要使用</li>
<li>-l： 长列表. 打印关于锁的附加信息，一般情况不需要使用</li>
<li>-m： 打印java和native c/c++框架的所有栈信息.可以打印JVM的堆栈,显示上Native的栈帧，一般应用排查不需要使用</li>
<li>-h 或 -help：打印帮助信息</li>
</ul>
<h3 id="线程快照分析工具">1.3线程快照分析工具</h3>
<p>Jstack生成的线程快照是文本的格式，看起来比较困难，可以使用线程分析工具，如：<a href="https://www.ibm.com/developerworks/community/groups/service/html/communitystart?communityUuid=2245aa39-fa5c-4475-b891-14c205f7333c">IBM Thread and Monitor Dump Analyze for Java</a><br>
在线工具：<a href="http://spotify.github.io/threaddump-analyzer/">http://spotify.github.io/threaddump-analyzer</a><br>
使用工具可以更快更清晰的看清线程状况</p>
<h2 id="使用实例">2. 使用实例</h2>
<h3 id="使用jstack找出最耗cpu的线程">2.1 使用Jstack找出最耗cpu的线程</h3>
<p><strong>第一步</strong>先找出Java进程ID<br>
使用jps命令</p>
<blockquote>
<p><img src="https://github.com/HuangZhiAn/MyBlog/raw/master/resource/jps.png" alt="jsp"></p>
</blockquote>
<p><strong>第二步</strong>找出该进程内最耗费CPU的线程<br>
可以使用以下几个命令</p>
<ul>
<li>ps -Lfp pid</li>
<li>ps -mp pid -o THREAD, tid, time</li>
<li>top -Hp pid</li>
</ul>
<p>使用top -Hp pid命令</p>
<blockquote>
<p><img src="https://github.com/HuangZhiAn/MyBlog/raw/master/resource/top_Hp-pid.png" alt="jsp"></p>
</blockquote>
<p>其中pid为<strong>15991</strong>的线程cpu百分比最高，将其转换为16进制 <strong>3e77</strong><br>
使用命令<code>Jstack &lt;pid&gt; | grep 3e77</code></p>
<blockquote>
<p><img src="https://github.com/HuangZhiAn/MyBlog/raw/master/resource/jstack-pid-grep.png" alt="jsp"></p>
</blockquote>
<p>可以看到是<strong>ContainerBackgroundProcessor</strong>这个类占的cpu最高<br>
该类为tomcat内部类，所以所写程序并没有占用很多cpu，应用cpu正常</p>
<h3 id="项目中使用示例">2.2 项目中使用示例</h3>
<p>项目中出现 tomcat 假死的情况<br>
假死是因为线程没有响应，导致阻塞请求<br>
使用 Jstack 工具生成线程快照，查看出问题时的线程状态，分析问题原因<br>
使用 jps 命令查看 Tomcat 进程 pid</p>
<blockquote>
<p><img src="https://github.com/HuangZhiAn/MyBlog/raw/master/resource/jps.png" alt="jsp"></p>
</blockquote>
<p>使用jstack生成线程快照</p>
<blockquote>
<p><img src="https://github.com/HuangZhiAn/MyBlog/raw/master/resource/jstack-pid.png" alt="jsp"></p>
</blockquote>
<p>这次没有使用快照分析工具，而是直接使用了文本编辑器查看<br>
查看生成的文本发现有很多处于WATING状态的线程，都在等待获取数据库连接，从而定位到了是数据库连接问题</p>

