## SSH免密码登录

ssh免密码登录实现

在此简单记录一下  
比如有两台计算机 **A** 和 **B**, 需要在 **A** 上免密码登录 **B**

1. 在 A 上生成密钥

       ssh-keygen -t rsa
      这时会在用户目录生成 **.ssh** 文件夹，里面有 id_rsa 和id_rsa.pub文件  
      ![](https://github.com/HuangZhiAn/MyBlog/raw/master/resource/images/ssh/ssh-rsa.png)
2. 将公钥拷贝到B服务器

       ssh-copy-id -i ~/.ssh/id_rsa.pub 用户名@B服务器ip
     完成之后就可以使用ssh 用户名@ip命令免密码登录了
![](https://github.com/HuangZhiAn/MyBlog/raw/master/resource/images/ssh/copy-login.png)

### SSH 登录太慢问题

> ## useDNS配置导致登陆慢
> 
> 如果ssh server的配置文件（通常是  `/etc/ssh/sshd_config` ）中设置  **`useDNS yes`**
> ，可能会导致 ssh 登陆卡住几十秒。按照网上的方法将该配置项设为 no，然后重启 ssh 服务，再次登陆就恢复正常

<!--stackedit_data:
eyJoaXN0b3J5IjpbMTI4MTAzNTgsOTM4NzQ3NDkwXX0=
-->