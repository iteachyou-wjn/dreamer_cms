<div style="display: flex;">
	<img src="https://oss.iteachyou.cc/logo.png" height="30" />
	<div style="margin-left: 5px; font-size: 30px; line-height: 30px; font-weight: bold;">梦想家内容管理系统</div>
</div>

----------
### PageList文章分页标签

#### 适用版本
<p>
Previous_Releases_2.0.0 + 
</p>

#### 名称
<p>
文章分页标签
</p>

#### 功能
<p>
该标签是用于在列表页面调用文章列表并显示分页信息。一般该标签配合{dreamer-cms:pagination /}共同使用
</p>

#### 语法
```html?linenums
{dreamer-cms:pagelist}
	<li><a href="#"><i class="ion ion-calendar"></i>[field:createtime/]</a></li>
{/dreamer-cms:pagelist}
```

#### 函数介绍
```html?linenums
使用示例：
[field:title function="substring(0,50,'...')" /]
或
[field:createtime function="format('yyyy-MM-dd')" /]
或
[field:autoindex function="steps(5,1)" /]

1. function="substring(参数1,参数2,参数3)"
    函数说明：截取字段为指定长度，function=""，必须为双引号包裹。
    参数1：开始截取的位置
    参数2：截取长度
    参数3：替换字符，如...。注：参数3需要用英文单引号包裹，例：'...'
2. function="format(参数)"
    函数说明：格式化日期数据为指定格式
    参数：格式化字符串，参照SimpleDateFormat类，注：参数需要用英文单引号包裹，例：'yyyy-MM-dd'
3. function="steps(参数1, 参数2)"
    函数说明：指定autoindex的起始序号以及步长
    参数1：起始序号
    参数2：步长
```

#### 文件
```java?linenums
cc.iteachyou.cms.taglib.tags.PageListTag
```

#### 参数
参数名|描述
:--:|:--
formkey|表单模型的编码
addfields|附加字段（该字段须设置了formkey后生效）
cascade|是否显示该栏目的下级栏目的文章
sortWay|排序方式（asc或desc）
sortBy|排序字段（数据库中字段名）

#### 底层字段
字段名|描述
:--:|:--
autoindex|序号（从1开始）
id|文章ID
title|标题
properties|文章属性（头条h加粗b幻灯f图片b）
litpic|缩略图
tag|文章标签
remark|文章描述
categoryid|栏目ID
typenamecn|栏目中文名称
typenameen|栏目英文名称
comment|是否允许评论
subscribe|是否允许订阅
clicks|点击数
weight|权重（可以利用该字段来排序）
status|状态
createby|发布人
createtime|发布时间
updateby|更新人
updatetime|更新时间
arcurl|文章访问URL

#### 范例
```html?linenums
<div class="row">
	{dreamer-cms:pagelist pagesize="10" formkey="LUVC0AW2" addfields="versionnum,notes"}
	<div class="col-lg-4 col-md-6 col-sm-6">
		<div class="blog-post">
			<div class="blog-details">
				<ul class="list-inline blog-item-links">
					<li class="list-inline-item"><a href="#"><i class="ion ion-calendar"></i>版本号：[field:versionnum/]</a></li>
					<li class="list-inline-item"><a href="#"><i class="ion ion-chatbubbles"></i>点击：[field:clicks/]</a></li>
				</ul>
				<a href="[field:arcurl/]"><h4 class="media-heading">[field:title/]</h4></a>
				<p class="product-para">版本号：[field:versionnum/]</p>
				<p class="product-para">发布日期：[field:createtime/]</p>
				<a href="[field:arcurl/]" class="blog-post-link">查看详情 <i class="fa fa-angle-double-right"></i></a>
			</div>
		</div>
	</div>
	{/dreamer-cms:pagelist}
</div>
<div class="row">
	<div class="col-lg-12 col-md-12 col-sm-12">
	{dreamer-cms:pagination /}
	</div>
</div>
```

#### 效果
![pagelist标签](https://oss.iteachyou.cc/20190820101533.png "pagelist标签")
