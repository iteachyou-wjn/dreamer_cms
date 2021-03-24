<p align="center">
	<a href="https://www.itechyou.cn/" target="_blank"><img src="http://cdn.itechyou.cn/1-1ZQ91JT60-L.png" ></a>
</p>

# 梦想家CMS内容管理系统开源版J2EE代码

当前版本：3.5.1

梦想家CMS官网：http://www.itechyou.cn
模版标签开发教程请参考：http://doc.itechyou.cn 

QQ群交流：
- ①597652651
- ②623605199
- ③573574854

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
* Lombok

# 系统结构
![系统结构](http://cdn.itechyou.cn/20200107103015.jpg "系统结构")

# 开发环境
建议开发者使用以下环境，这样避免版本带来的问题
* IDE：Spring Tool Suite 4（STS）
* DB：Mysql 5.7
* JDK：Jdk8
* Redis：3.2+，Windows配置安装Redis教程，请参考：https://itechyou.cn/article/4b0a638f65fa4fb1b9644cf461dba602
* LomBok 项目需要使用Lombok支持，Lombok安装教程，请参考https://itechyou.cn/article/55ec2939c29147eca5bebabf19621655

# 快速入门
CMS包括两个部分（代码部分、资源部分）代码不多说。资源就是图片、模版等，该目录在application.yml中web.resource-path配置项目中配置。视频教程：
```
Dreamer CMS后台使用教程：
https://itechyou.cn/list-6s3bg7tf/dreamercms/1/10
Dreamer CMS模版开发教程：
https://itechyou.cn/list-l54xs53b/tempdev/1/10

百度网盘下载链接：
https://pan.baidu.com/s/1plsfBl_ds_5TkVa-yRkESQ 提取码：2c8i

在线观看视频地址：
https://space.bilibili.com/482273402
```
1. 克隆项目到本地工作空间
2. 导入Eclipse或Sts等开发工具（推荐使用Spring Tools Suite 4），项目需要使用Lombok支持，Lombok安装教程，请参考https://itechyou.cn/article/55ec2939c29147eca5bebabf19621655
3. 项目需要Redis，请自行修改application.yml中Redis配置
4. 修改项目资源目录，application.yml文件web.resource-path配置项（如D:/dreamer-cms/）
5. 导入数据库src/main/resources/db/db.sql，要求Mysql5.7版本，并修改application-(dev|prd).yml中数据配置
6. 将项目src/main/resources/db/dreamer-cms.zip文件解压，保证解压后的目录路径的名称和资源目录一致
7. 运行项目DreamerCMSApplication.java
8. 网站首页：http://localhost:8888 项目管理后台：http://localhost:8888/admin
9. 管理后台用户名：wangjn；密码：123456
10. 模版标签开发教程请参考：http://doc.itechyou.cn 

# 捐赠详情 

##### 感谢各位大佬的支持，以下排名不分先后！同时也感谢各位开发者为社区作出的贡献。

<table width="100%" cellspacing="0" cellpadding="0" border="0">
	<tr>
		<th width="30%">用户</th>
		<th width="30%">金额</th>
		<th width="40%">时间</th>
	</tr>
	<tr>
		<td>个.秋</td>
		<td>10.00</td>
		<td>2020-01-09 11:52:16</td>
	</tr>
	<tr>
		<td>孤狼</td>
		<td>10.00</td>
		<td>2020-09-30 09:16:23</td>
	</tr>
	<tr>
		<td>胜宇</td>
		<td>18.00</td>
		<td>2020-11-23 12:34:18</td>
	</tr>
	<tr>
		<td>二娃</td>
		<td>30.00</td>
		<td>2020-12-01 18:01:35</td>
	</tr>
	<tr>
		<td>胜宇</td>
		<td>10.00</td>
		<td>2020-12-13 18:24:58</td>
	</tr>
    <tr>
		<td>匿名用户</td>
		<td>20.00</td>
		<td>2020-12-22 23:02:35</td>
	</tr>
    <tr>
		<td>胜宇</td>
		<td>5.18</td>
		<td>2021-01-04 16:31:23</td>
	</tr>
    <tr>
		<td>胜宇</td>
		<td>10.00</td>
		<td>2021-01-05 18:03:05</td>
	</tr>
    <tr>
		<td>胜宇</td>
		<td>10.00</td>
		<td>2021-01-07 08:41:23</td>
	</tr>
</table>


同时也希望更多的用户来捐赠本项目。您的支持，是我一直坚持下去的动力，另外为大家提供使用文档（http://doc.itechyou.cn）、教程（https://www.itechyou.cn）的网站及服务器也需要运营维护，个人精力、经费都有限，希望大家多多理解！！！

<center>
    <div style="width:50%;float:left;text-align:right;">
        <img src="http://cdn.itechyou.cn/20201201174329.png" width="100%" />
    </div>
    <div style="width:50%;float:right;text-align:left;">
        <img src="http://cdn.itechyou.cn/20201201174339.jpg" width="100%" />
    </div>
    <div style="clear:both;"></div>
</center>


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





