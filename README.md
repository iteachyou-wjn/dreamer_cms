<p align="center">
	<a href="https://www.itechyou.cn/" target="_blank"><img src="http://cdn.itechyou.cn/1-1ZQ91JT60-L.png" ></a>
</p>

# 梦想家CMS内容管理系统开源版J2EE代码

当前版本：3.0.1

梦想家CMS官网：http://www.itechyou.cn
模版标签开发教程请参考：http://doc.itechyou.cn 

QQ群交流：
- ①597652651
- ②623605199

DreamerCMS（梦想家CMS内容管理系统）公开解决了快速搭建展示型网站（如：企业官网、技术博客、信息门户等）的框架体系，是电子政务、电信综合门户、企业信息门户、知识管理平台、电子商务平台的基础性软件系统。可以帮助政府、企业或组织灵活、准确、高效、智能地管理信息内容，实现信息的采集、加工、审核、发布、存储、检索、统计、分析、 反馈等整个信息生命周期的管理。采用时下最流行的Springboot+thymeleaf框架搭建，具有灵活小巧，配置简单，标签化模版，快速开发等特点。主要解决公司搭建网站成本高、投入大、周期长等问题，也可作为初创公司很好的基础技术框架。使用过程中不需要专业的后端技术开发技能，只要使用系统提供的模版标签，即可轻轻松松建设网站。

DreamerCMS从2.0.0版本开始采用了解析式引擎与编译式引擎并存的模式，由于在解析模版时，解析式引擎拥有巨大的优势，但对于动态浏览的互动性质的页面，编译式引擎更实用高效，DreamerCMS采用双引擎并存的模式，在保持标签风格一致性的同时，也保证将来开发更多互动模块时有更好的性能和更好的扩展。
# 特点
* 免费完整开源：基于APACHE 2.0开源协议，源代码完全开源；
* 标签化建站：不需要专业的后台开发技能，只要使用系统提供的标签，就能轻松建设网站；
* 模版开发方便：支持在线上传模版包开发方便快捷；
* 每月更新：每月进行系统升级，分享更多好用的模版与插件；

# 面向对象
* 政府：可以使用Dreamer CMS来快速构建政府门户；
* 电信：可以使用Dreamer CMS来快速构建电信综合门户；
* 企业：可以使用Dreamer CMS构建信息门户，知识管理平台，也可作为基础技术框架，是企业在创立初期很好的技术选型；
* 个人开发者：可以使用Dreamer CMS承接外包项目；

# 技术框架
* 核心框架：Spring Boot 2
* 安全框架：Apache Shiro 1.5
* 视图框架：Spring MVC 4
* 工具包：Hutool 5.3.7
* 持久层框架：MyBatis 3
* 日志管理：Log4j2
* 模版框架：Thymeleaf
* JS框架：jQuery，Bootstrap
* CSS框架：Bootstrap
* 富文本：Ueditor、editor.md

# 系统结构
![系统结构](http://cdn.itechyou.cn/20200107103015.jpg "系统结构")

# 开发环境
建议开发者使用以下环境，这样避免版本带来的问题
* IDE：Spring Tool Suite 4（STS）
* DB：Mysql 5.7
* JDK：jdk8
* Redis：3.2+

# 快速入门
CMS包括两个部分（代码部分、资源部分）代码不多说。资源就是图片、模版等，该目录在application.yml中web.resource-path配置项目中配置。视频教程：
```
百度网盘下载链接：
https://pan.baidu.com/s/1plsfBl_ds_5TkVa-yRkESQ 提取码：2c8i
在线观看视频地址：
https://space.bilibili.com/482273402
```
1. 克隆项目到本地工作空间
2. 导入Eclipse或Sts等开发工具（推荐使用Spring Tools Suite 4）
3. 项目需要Redis，请自行修改application.yml中Redis配置
4. 修改项目资源目录，application.yml文件web.resource-path配置项（如D:/dreamer-cms/）
5. 将项目src/main/resources/db/dreamer-cms.zip文件解压，保证解压后的目录路径的名称和资源目录一致
6. 运行项目DreamerCMSApplication.java
7. 网站首页：http://localhost:8888 项目管理后台：http://localhost:8888/admin
8. 管理后台用户名：wangjn；密码：123456
9. 模版标签开发教程请参考：http://doc.itechyou.cn 


# 系统美图
![后台登录](http://cdn.itechyou.cn/20190821102434.png)

![系统管理](http://cdn.itechyou.cn/20190821102526.png)

![栏目管理](http://cdn.itechyou.cn/20190821102608.png)

![发布文章](http://cdn.itechyou.cn/20190821102712.png)

![标签管理](http://cdn.itechyou.cn/20190821102742.png)

![表单管理](http://cdn.itechyou.cn/20190821102847.png)

![风格管理](http://cdn.itechyou.cn/20190821102932.png)

![变量管理](http://cdn.itechyou.cn/20190821103012.png)

![前台首页](http://cdn.itechyou.cn/20190820092155.png)

![封面页面](http://cdn.itechyou.cn/20190820092156.png)

![列表页面](http://cdn.itechyou.cn/20190820092157.png)

![文章页面](http://cdn.itechyou.cn/20190820092158.png)