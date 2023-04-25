<div style="display: flex;">
	<img src="https://oss.iteachyou.cc/logo.png" height="30" />
	<div style="margin-left: 5px; font-size: 30px; line-height: 30px; font-weight: bold;">梦想家内容管理系统</div>
</div>

----------
### List文章列表标签

#### 适用版本
<p>
Previous_Releases_2.0.0 + 
</p>

#### 名称
<p>
文章列表标签
</p>

#### 功能
<p>
用于获取栏目下的文章
</p>

#### 语法
```html?linenums
{dreamer-cms:list typeid="A54547W2" pagenum="0" pagesize="8" flag="p"}
	<a href="[field:arcurl/]">[field:title /]</a>
{/dreamer-cms:list}
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
cc.iteachyou.cms.taglib.tags.ListTag
```

#### 参数
参数名|描述
:--:|:--
typeid|后台栏目管理列表页面中的编码，通过唯一编码，获取指定栏目
pagenum|页数
pagesize|获取数量
flag|文章属性（p图片）
cascade|是否显示该栏目的下级栏目的文章
addfields|附加字段，多个字段间用英文逗号分隔
formkey|表单模型的编码（可以在后台表单管理列表中获取）
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
hasthumbnail|是否有缩略图（有输出yes-thumbnail；无输出no-thumbnail）<br />可用于Css的class名，从而实现动态的输出列表样式
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
{dreamer-cms:list typeid="A54547W2" pagenum="1" pagesize="4" flag="p"}
<div class="col-lg-3 col-md-4 col-sm-6">
    <div class="feature-wrap">
    	<div class="feature-image"><img src="[field:litpic /]" class="ion ion-ios-cloud-outline"></i></div>
    	<a href="[field:arcurl/]">[field:title function="substring(0,100,'...')" /]</a>
    	<p></p>
    </div>
</div>
{/dreamer-cms:list}
```

#### 效果
![list标签](https://oss.iteachyou.cc/20200619172512.png "list标签")
