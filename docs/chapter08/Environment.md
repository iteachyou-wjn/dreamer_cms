<div style="display: flex;">
	<img src="https://oss.iteachyou.cc/logo.png" height="30" />
	<div style="margin-left: 5px; font-size: 30px; line-height: 30px; font-weight: bold;">梦想家内容管理系统</div>
</div>

----------
### 环境准备

1. Redis
2. Jdk1.8
3. Mysql5.7（忽略大小写）
4. Nginx

### 软件安装

#### 1. Redis安装
[Windows安装及配置方法](https://iteachyou.cc/article/4b0a638f65fa4fb1b9644cf461dba602)

[Linux安装及配置方法](https://iteachyou.cc/article/1676407365014867968)

#### 2. Jdk1.8安装
##### 2.1 下载Jdk

[下载Jdk1.8](https://www.oracle.com/java/technologies/downloads/#java8)

Linux下载：jdk-8u371-linux-x64.tar.gz
##### 2.2 安装Jdk
将下载的jdk-8u371-linux-x64.tar.gz进行解压，解压到/usr/local/Java/目录中，最终目录为：/usr/local/Java/jdk1.8.0_361
##### 2.3 配置环境变量
编辑/etc/profile文件，添加如下内容
```shell
# Java
export JAVA_HOME=/usr/local/Java/jdk1.8.0_361
export JRE_HOME=/usr/local/Java/jdk1.8.0_361/jre
export CLASSPATH=.:$JAVA_HOME/lib:$JRE_HOME/lib:$CLASSPATH
export PATH=$JAVA_HOME/bin:$JRE_HOME/bin:$PATH
```
使环境变量生效
```shell
source /etc/profile
```

#### 3. 安装Mysql5.7
[Windows安装及配置方法](https://iteachyou.cc/article/a1db138b4a89402ab50f3499edeb30c2)

[Linux安装及配置方法](https://iteachyou.cc/article/d78be30aef8e49c6b16fb92af2eb2dd3)

#### 4. 安装Nginx
Windows下安装Nginx只需要解压即可，在这里只讲Linux中的安装方法 [Linux安装及配置方法](https://iteachyou.cc/article/1664185302896316416)
