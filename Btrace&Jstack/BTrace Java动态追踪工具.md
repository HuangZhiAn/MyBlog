## Preface
项目中学习使用到一个Java程序追踪工具——BTrace Java动态追踪工具，可以在程序运行期间编写脚本，查看程序运行情况。特此记录分享，希望对各位有所帮助

## 1. 工具使用介绍
### 1.1 主页和下载页
主页:  [https://github.com/btraceio/btrace](https://github.com/btraceio/btrace)
下载页: [https://github.com/btraceio/btrace/releases/tag/v1.3.11](https://github.com/btraceio/btrace)
使用该工具需要设置**JAVA_HOME**环境变量，编辑文件

    vi /etc/profile

在文件末尾添加

    export JAVA_HOME = ../../java_path
    
### 1.2 简介
BTrace被用来动态跟踪运行java程序，使用字节码追踪方式，让它可以在程序正在运行时进行追踪，而不需要跟随应用程序一起编译重启  
安全性：Btrace除了打印输出一些信息外，几乎禁止了一切对原运行代码的操作，包括禁止创建对象，禁止捕捉和抛出异常等。

有以下几种常用的使用场景
1. 查看代码是否执行到了某一行
2. 查看某一个方法中所有外部调用的响应时间，方便定位方法响应慢的具体位置及原因
3. 查看哪里调用了某个方法及其对应的调用栈，如：监控代码对数据库连接池的获取和释放情况，看是否存在连接泄漏
4. 查看方法传入的参数和返回值

在下载的压缩包里有一个samples文件夹，里面有一些常用脚本示例
![samples][1]

### 1.4 使用方式

1. 解压下载的压缩包，给bin目录下的文件赋予运行的权限  

	    chmod +x ../解压目录/bin/*

2. 找到要监控的 JVM进程 PID ,可通过 **jps** 或 **ps -ef | grep java** 命令找到
![jps][2]
3. 切换到进程拥有账户，如果已经是进程的启动用户，则忽略本步骤

4. btracec 命令对监控脚本进行预编译，这一点很重要，可以在运行前发现错误。特别是应用到线上环境，必须强制先预编译一下，看是否报错。
![btracec][3]
5. 如需修改监控只需要停止运行后 修改脚本 然后运行脚本即可。

7. BTrace脚本在进程重启后会失效。

## 项目中使用实例
### 2.1 监控数据库连接获取和释放

项目中有遇到 tomcat 线程在一直等待数据库连接的情况，怀疑是数据库连接泄漏，咨询了项目上的同事后，仍然无法定位到是哪里的数据库操作导致连接泄漏，于是使用 Btrace 对运行中的代码进行追踪，监控数据库连接和释放情况  
编写 Btrace 脚本[BTraceConnection.java](https://github.com/HuangZhiAn/MyBlog/raw/master/resource/images/btrace/BTraceConnection.java)
项目使用tomcat jndi数据源，应用程序获取连接时会调用  org.apache.tomcat.dbcp.dbcp.PoolingDataSource类的**getConnection()** 方法
![getConnection][4]
释放连接时会调用org.apache.tomcat.dbcp.dbcp.PoolableConnection类的**close()** 方法
![close][5]

因此，记录每次连接获取和释放，若获取次数大于释放次数则说明存在数据库连接泄漏  
使用`Threads.jstack()` 打印调用栈，用于定位具体调用代码  
将脚本上传服务器，btracec编译，btrace运行,将结果输出到trace.log文件
![log][6]
跑半天后，`ctrl+c`停止运行，得到100多M的log，部分截图如下
查找One connection is opened的数量和One connection is closed的数量
发现两者数量完全一样，由此推断程序没有发生数据库泄漏的情况
![connection_open.png][7]
![connection_close.png][8]

后经证实是数据库连接池最大连接数太小的原因  

更多的脚本示例可到samples目录查看

[1]:https://github.com/HuangZhiAn/MyBlog/raw/master/resource/images/btrace/samples.png
[2]:https://github.com/HuangZhiAn/MyBlog/raw/master/resource/images/btrace/jps.png
[3]:https://github.com/HuangZhiAn/MyBlog/raw/master/resource/images/btrace/btracec.png
[4]:https://github.com/HuangZhiAn/MyBlog/raw/master/resource/images/btrace/getConnection.png
[5]:https://github.com/HuangZhiAn/MyBlog/raw/master/resource/images/btrace/close.png "close.png"
[6]:https://github.com/HuangZhiAn/MyBlog/raw/master/resource/images/btrace/log.png
[7]:https://github.com/HuangZhiAn/MyBlog/raw/master/resource/images/btrace/connection_open.png "connection_open.png"
[8]:https://github.com/HuangZhiAn/MyBlog/raw/master/resource/images/btrace/connection_close.png "connection_close.png"

<!--stackedit_data:
eyJoaXN0b3J5IjpbLTEzNjQxMDUwODFdfQ==
-->