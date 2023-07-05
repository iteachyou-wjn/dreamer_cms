<div style="display: flex;">
	<img src="https://oss.iteachyou.cc/logo.png" height="30" />
	<div style="margin-left: 5px; font-size: 30px; line-height: 30px; font-weight: bold;">梦想家内容管理系统</div>
</div>

----------
### 问：网站启动后，后台正常，前台404？
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
