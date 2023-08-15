<div style="display: flex;">
	<img src="https://oss.iteachyou.cc/logo.png" height="30" />
	<div style="margin-left: 5px; font-size: 30px; line-height: 30px; font-weight: bold;">梦想家内容管理系统</div>
</div>

----------
### 问：网站打包上线后，图片、视频等上传的资源404？
一般产生该问题，是由于项目的资源目录配置错误导致，可以仔细检查项目中application-(dev|prd).yml文件中的

```yaml
web:
  resource-path: D:/dreamer-cms/
```

如上配置，则要保证在D:/ 盘中存在dreamer-cms目录，并保证该目录中的目录文件结构为以下示例：

```yaml
dreamer-cms
  |- htmls
  |- templates
  |- uploads
  |- config.json
```

如果上述配置没有问题，还需要在后台管理系统中，网站配置中修改网址为上线服务器的IP或域名。如：

```yaml
http://cms.iteachyou.cc/
或
http://10.182.2.202:8888/
```
