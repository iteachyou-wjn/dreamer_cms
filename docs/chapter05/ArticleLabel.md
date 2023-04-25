<div style="display: flex;">
	<img src="https://oss.iteachyou.cc/logo.png" height="30" />
	<div style="margin-left: 5px; font-size: 30px; line-height: 30px; font-weight: bold;">梦想家内容管理系统</div>
</div>

----------
### Article文章页标签

#### 适用版本
<p>
Previous_Releases_2.0.0 + 
</p>

#### 名称
<p>
文章页标签
</p>

#### 功能
<p>
该标签是用于在文章内容页显示当前文章信息
</p>

#### 语法
```html?linenums
{dreamer-cms:article field="title" /}
或
{dreamer-cms:article field="title" function="substring|format" /}
```

#### 函数介绍
```html?linenums
1. function="substring(参数1,参数2,参数3)"
    函数说明：截取字段为指定长度，function=""，必须为双引号包裹。
    参数1：开始截取的位置
    参数2：截取长度
    参数3：替换字符，如...。注：参数3需要用英文单引号包裹，例：'...'
2. function="format(参数)"
    函数说明：格式化日期数据为指定格式
    参数：格式化字符串，参照SimpleDateFormat类，注：参数需要用英文单引号包裹，例：'yyyy-MM-dd'
```

#### 文件
```java?linenums
cc.iteachyou.cms.taglib.tags.ArticleTag
```

#### 参数
参数名|描述
:--:|:--
field|字段名

#### 底层字段
字段名|描述
:--:|:--
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
realname|发布人真实姓名
username|发布人用户名
createtime|发布时间
updateby|更新人
updatetime|更新时间
arcurl|文章访问URL
***|其它附加字段（需要配合自定义表单）

#### 范例
```html?linenums
<div class="row">
	<div class="col-12 col-lg-12 mx-auto">
		<div class="post post-card">
			<div class="single-post-header">
				<div class="post-tag">{dreamer-cms:article field="createtime" /}</div>
				<h1>{dreamer-cms:article field="title" /}</h1>
				<ul class="list-inline blog-item-links">
					<li class="list-inline-item">{dreamer-cms:article field="tag" /}</li>
				</ul>
			</div>
			<div class="row">
				<div class="col-12">
					<div class="templates-details">
						<a href="javascript:void(0)" class="post-image" ><img src="{dreamer-cms:article field='litpic' /}" alt="#" class="img-fluid featured-img"></a>
						{dreamer-cms:article field="body" /}
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
```

#### 效果
![article标签](https://oss.iteachyou.cc/20190828111649.png "article标签")
