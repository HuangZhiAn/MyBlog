## SSH免密码登录

ssh免密码登录很简单

在此简单记录一下  
比如有两台计算机**A**和**B**, 需要在**A**上免密码登录**B**

1. 在A上生成密钥

       ssh-keygen -t rsa
      这时会在用户目录生成 **.ssh** 文件夹，里面有 id_rsa 和id_rsa.pub文件  
      ![](https://github.com/HuangZhiAn/MyBlog/raw/master/resource/images/ssh/ssh-rsa.png)
2. 将公钥拷贝到B服务器

       ssh-copy-id -i ~/.ssh/id_rsa.pub 用户名@B服务器ip
     完成之后就可以使用ssh 用户名@ip命令免密码登录了
![](https://github.com/HuangZhiAn/MyBlog/raw/master/resource/images/ssh/copy-login.png)
<!--stackedit_data:
eyJoaXN0b3J5IjpbOTM4NzQ3NDkwXX0=
-->