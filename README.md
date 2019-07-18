# 梦想家CMS内容管理系统开源版J2EE代码

当前版本：2.0.1

梦想家CMS官网：http://www.itechyou.cn
模版标签开发教程请参考：http://doc.itechyou.cn 

QQ群交流：
- ①597652651

Dreamer CMS 梦想家内容发布系统是国内首款java开发的内容发布系统，采用最流行的springboot+thymeleaf框架搭建，灵活小巧，配置简单。

# 特点
* 免费完整开源：基于APACHE 2.0开源协议，源代码完全开源；
* 标签化建站：不需要专业的后台开发技能，只要使用系统提供的标签，就能轻松建设网站；
* 模版开发方便：支持在线上传模版包开发方便快捷；
* 每月更新：每月进行系统升级，分享更多好用的模版与插件；

# 面向对象
* Dreamer CMS是企业在创立初期很好的技术基础框架，加快公司项目开发进度，当然也可以对现有的系统进行升级；
* 个人开发者也可以使用Dreamer CMS承接外包项目；
* 初学JAVA的同学可以下载源代码来进行学习交流；

# 技术框架
* 核心框架：Spring Boot 2
* 安全框架：Apache Shiro 1.5
* 视图框架：Spring MVC 4
* 持久层框架：MyBatis 3
* 日志管理：Log4j2
* 模版框架：Thymeleaf
* JS框架：jQuery，Bootstrap
* CSS框架：Bootstrap
* 富文本：Ueditor、editor.md

# 系统结构
![系统结构](http://img.075400.cn/20190821103015.jpg "系统结构")

# 开发环境
建议开发者使用以下环境，这样避免版本带来的问题
* IDE：Spring Tool Suite 4（STS）
* DB：Mysql 5.7
* JDK：jdk8
* Redis：3.2+

# 快速入门
CMS包括两个部分（代码部分、资源部分）代码不多说。资源就是图片、模版等，该目录在application.yml中web.upload-path配置项目中配置。
1. 克隆项目到本地工作空间
2. 导入Eclipse或Sts等开发工具（推荐使用Spring Tools Suite 4）（项目中缺少的google/kaptcha.jar在附件中，大家自行下载）
3. 项目需要Redis，请自行修改application.yml中Redis配置
4. 修改项目资源目录，application.yml文件web.upload-path配置项（如D:/dreamer-cms/）
5. 将项目src/main/resources/db/dreamer-cms.zip文件解析，保证解压后的目录路径和名称和资源目录一致
6. 运行项目DreamerBlogApplication.java
7. 网站首页：http://localhost:8888 项目管理后台：http://localhost:8888/admin
8. 管理后台用户名：wangjn；密码：123456
9. 模版标签开发教程请参考：http://doc.itechyou.cn 


# 快速入门
* 克隆项目到本地
* 导入Eclipse或Sts等开发工具
* 将项目主页附件标签页里的dreamer-blog.zip附件下载并解压到D盘根目录
* 运行项目DreamerBlogApplication.java
* 项目首页：http://localhost:8888；项目管理后台：http://localhost:8888/admin

# 系统美图
![后台登录](http://img.075400.cn/20190821102434.png)

![系统管理](http://img.075400.cn/20190821102526.png)

![栏目管理](http://img.075400.cn/20190821102608.png)

![发布文章](http://img.075400.cn/20190821102712.png)

![标签管理](http://img.075400.cn/20190821102742.png)

![表单管理](http://img.075400.cn/20190821102847.png)

![风格管理](http://img.075400.cn/20190821102932.png)

![变量管理](http://img.075400.cn/20190821103012.png)

![前台首页](http://img.075400.cn/20190820092155.png)

![封面页面](http://img.075400.cn/20190820092156.png)

![列表页面](http://img.075400.cn/20190820092157.png)

![文章页面](http://img.075400.cn/20190820092158.png)