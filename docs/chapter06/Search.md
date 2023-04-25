<div style="display: flex;">
	<img src="https://oss.iteachyou.cc/logo.png" height="30" />
	<div style="margin-left: 5px; font-size: 30px; line-height: 30px; font-weight: bold;">梦想家内容管理系统</div>
</div>

----------
### 网站搜索
在本小节，将带领大家一起学习网站中搜索功能的实现。

模板文件必须为search.html，在模板文件中，可以使用pagelist标签展示数据和pagination标签来进行分页。

#### 前端表单
```html?linenums
<form class="navbar-left nav-search" role="search" action="/search" method="post">
    <input class="form-control" placeholder="输入关键字进行搜索..." type="text" name="entity['keywords']">
    <input type="submit" value="搜索" />
</form>
```
注意：
1. 提交地址必须为/search，方式必须为POST。
2. keywords: 搜索关键词，不可改变。
3. typeid：栏目编码，可以多个，多个使用英文逗号分隔（搜索范围：该字段可有可无，无默认为全站搜索）。
4. 字段都要包在entity对象中，如entity['typeid']

#### 后端接收
```java?linenums
cc.iteachyou.cms.controller.FrontController
```
在该类中的search方法，为后端接收前台留言的控制器。返回HTML模板。

#### 范例
```java?linenums
无
```
