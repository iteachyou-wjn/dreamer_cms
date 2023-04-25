<div style="display: flex;">
	<img src="https://oss.iteachyou.cc/logo.png" height="30" />
	<div style="margin-left: 5px; font-size: 30px; line-height: 30px; font-weight: bold;">梦想家内容管理系统</div>
</div>

----------
### 命名规则

#### 模板保存位置
<p>
模板目录在资源目录下，名为templates的文件夹即是模版目录。可以通过程序中application.yml中的web.resource-path配置项来配置。默认在
<br />
D:/dreamer-cms/templates/模版名（必须为英文，默认为default）/具体功能模板文件
</p>

#### 模板文件命名规范
* index_别名.html：表示栏目封面模板；
* list_别名.htm：表示栏目列表模板；
* article_别名.html：表示内容查看页（文档模板，包括专题查看页）；
* search.html：搜索结果列表模板；
* index.html：主页模板；
```html
注： [别名]可以使用栏目名称的英文缩写。
```
