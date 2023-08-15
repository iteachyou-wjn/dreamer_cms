<div style="display: flex;">
	<img src="https://oss.iteachyou.cc/logo.png" height="30" />
	<div style="margin-left: 5px; font-size: 30px; line-height: 30px; font-weight: bold;">梦想家内容管理系统</div>
</div>

----------
### 问：后台的上传文件大小控制？
在上传文件中，有两处控制上传大小，如下
#### 1. Spring boot默认上传大小为20M，如要更改，则在application.yml中添加如下配置：

```yaml
spring: 
  servlet:
    multipart:
      max-file-size: 50MB #上传文件的大小
      max-request-size: 50MB #Tomcat请求的大小
```
#### 2. 在富文本编辑器中上传文件，大小控制在config.json，该文件位于资源目录中。分别对如下配置项进行修改即可。

```yaml
/* 前后端通信相关的配置,注释只允许使用多行方式 */
{
  /* 上传图片配置项 */
  "imageMaxSize": 2048000, /* 上传大小限制，单位B */

  /* 涂鸦图片上传配置项 */
  "scrawlMaxSize": 2048000, /* 上传大小限制，单位B */
    
  /* 抓取远程图片配置 */
  "catcherMaxSize": 2048000, /* 上传大小限制，单位B */
    
  /* 上传视频配置 */
  "videoMaxSize": 102400000, /* 上传大小限制，单位B，默认100MB */
    
  /* 上传文件配置 */
  "fileMaxSize": 51200000, /* 上传大小限制，单位B，默认50MB */
}
```

#### 3. 上线后在Nginx中，上传大小控制在nginx.conf中，如下
```shell
# 此文件yum安装的nginx，一般在/etc/nginx/nginx.conf
http {
    client_max_body_size 50m; # 上传大小为50M
}
```
